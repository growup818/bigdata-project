package org.flume.system.redis.handler;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSONObject;

/**
 * redis 缓存切面
 * 
 * +缓存key，组成： 包名.方法名.缓存的Key 
 * 
 * @author sdc
 *
 */
public class RedisCacheAspect {
	
	private Logger LOG = Logger.getLogger(RedisCacheAspect.class);

	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOperations;
	
	/**
	 * 具体的方法
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	public Object cache(ProceedingJoinPoint jp, RedisCache cacheable) throws Throwable{
		// result是方法的最终返回结果
		Object result = null;
		
		// 得到类名、方法名和参数
		Object[] args = jp.getArgs();
		
		//获取实现类的方法
		Method method = getMethod(jp);
		
		//注解信息 key
		String key = cacheable.key();
		
		//是否转换成md5值
		boolean keyTransformMd5 = cacheable.keyTransformMd5();
		//----------------------------------------------------------
        // 用SpEL解释key值
        //----------------------------------------------------------
		//解析EL表达式后的的redis的值
        String keyVal = SpringExpressionUtils.parseKey(key, method, jp.getArgs(), keyTransformMd5);
        
    	// 获取目标对象
        Object target = jp.getTarget();
        //这块是全路径包名+目标对象名 ，默认的前缀，防止有的开发人员乱使用key，乱定义key的名称，导致重复key，这样在这加上前缀了，就不会重复使用key
        String target_class_name = target.getClass().getName();
        String target_class_method_name = method.getName();
        StringBuilder redis_key = new StringBuilder(target_class_name);
        redis_key.append(".").append(target_class_method_name).append(".");
        redis_key.append(keyVal);
        
        //最终的redis的key
        String redis_final_key = redis_key.toString();
		String value = valueOperations.get(redis_final_key);
		
		if (value == null) {
			// 缓存未命中，这块没用System.out.printlt输出，可以自定义输出
			LOG.debug(redis_final_key + "未命中缓存，执行target方法！！！");
			
			// 如果redis没有数据则执行拦截的方法体 
			result = jp.proceed(args);
			
			if(result == null) {//结果不为空
				//存入json格式字符串到redis里
				String result_json_data = JSONObject.toJSONString(result);
				LOG.debug(result_json_data);
				// 序列化结果放入缓存
				valueOperations.set(redis_final_key, result_json_data, getExpireTimeSeconds(cacheable), TimeUnit.SECONDS);
			}
		} else {
			// 缓存命中，这块没用System.out.printlt输出，可以自定义输出
			LOG.debug(redis_final_key + "命中缓存，不执行方法，直接返回!!!");
			
			// 得到被代理方法的返回值类型
			Class<?> returnType = ((MethodSignature) jp.getSignature()).getReturnType();

			//拿到数据格式
			result = getData(value, returnType);
		}
		return result;
	}

	/**
	 * 根据不同的class返回数据
	 * @param value
	 * @param clazz
	 * @return
	 */
	public <T> T getData(String value, Class<T> clazz){
        T result = JSONObject.parseObject(value, clazz);
        return result;
    }

	/**
	 * 获取方法
 	 * @param pjp
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
		// --------------------------------------------------------------------------
		// 获取参数的类型
		// --------------------------------------------------------------------------
		Object[] args = pjp.getArgs();
		Class[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}

		String methodName = pjp.getSignature().getName();
		Class<?> targetClass = pjp.getTarget().getClass();
		Method[] methods = targetClass.getMethods();

		// --------------------------------------------------------------------------
		// 查找Class<?>里函数名称、参数数量、参数类型（相同或子类）都和拦截的method相同的Method
		// --------------------------------------------------------------------------
		Method method = null;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName() == methodName) {
				Class<?>[] parameterTypes = methods[i].getParameterTypes();
				boolean isSameMethod = true;

				// 如果相比较的两个method的参数长度不一样，则结束本次循环，与下一个method比较
				if (args.length != parameterTypes.length) {
					continue;
				}

				// --------------------------------------------------------------------------
				// 比较两个method的每个参数，是不是同一类型或者传入对象的类型是形参的子类
				// --------------------------------------------------------------------------
				for (int j = 0; parameterTypes != null && j < parameterTypes.length; j++) {
					if (parameterTypes[j] != argTypes[j] && !parameterTypes[j].isAssignableFrom(argTypes[j])) {
						isSameMethod = false;
						break;
					}
				}
				if (isSameMethod) {
					method = methods[i];
					break;
				}
			}
		}
		return method;
	}
	
	/**
     * 计算根据Cacheable注解的expire和DateUnit计算要缓存的秒数
     * @param cacheable
     * @return
     */
    public int getExpireTimeSeconds(RedisCache redisCache) {
        int expire = redisCache.expireTime();
        TimeUnit unit = redisCache.dateUnit();
        if (expire <= 0) {//传入非法值，默认一分钟,60秒
            return 60;
        }
        if (unit == TimeUnit.MINUTES) {
            return expire * 60;
        } else if(unit == TimeUnit.HOURS) {
            return expire * 60 * 60;
        } else if(unit == TimeUnit.DAYS) {
            return expire * 60 * 60 * 24;
        }else {//什么都不是，默认一分钟,60秒
        	return 60;
        }
    }

}
