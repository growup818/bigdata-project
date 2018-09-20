package org.flume.system.redis.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 缓存注解
 * 
 * @author sdc
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {

	/**
	 * 缓存key的名称
	 * @return
	 */
	String key();
	
	/**
	 * key是否转换成md5值，有的key是整个对象，多个参数需要唯一，需要把多个参数转换成,md5值，导致redis的key很长
	 * 需要转换成md5值作为redis的key
	 * @return
	 */
	boolean keyTransformMd5() default false;
	
	/**
	 * key 过期日期 秒
	 * @return
	 */
	int expireTime() default 60;
	
	/**
	 * 时间单位，默认为秒
	 * @return
	 */
    TimeUnit dateUnit() default TimeUnit.SECONDS;
	
}
