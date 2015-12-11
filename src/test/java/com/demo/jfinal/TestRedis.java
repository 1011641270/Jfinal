/**
 * Dec 10, 2015
 * TestRedis.java
 * 2015 11:22:29 AM
 * Created By tian
 */
package com.demo.jfinal;

import com.jfinal.plugin.redis.Redis;
import com.jfinal.plugin.redis.RedisPlugin;

/**
 * 无web条件下使用
 * @author tian
 * @Time  Dec 10, 2015 11:22:29 AM
 */
public class TestRedis {
	
	public static void main(String[] args) {
		
		RedisPlugin redisPlugin = new RedisPlugin("test", "localhost");
		redisPlugin.start();
		
		Redis.use("test").set("testDemo", "tdd");
		System.out.println(Redis.use("test").get("testDemo"));
		
	}

}
