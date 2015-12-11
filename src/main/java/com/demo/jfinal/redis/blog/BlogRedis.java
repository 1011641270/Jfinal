/**
 * Dec 10, 2015
 * BlogRedis.java
 * 2015 11:15:39 AM
 * Created By tian
 */
package com.demo.jfinal.redis.blog;

import com.demo.jfinal.constant.blog.BlogConst;
import com.demo.jfinal.model.Blog;
import com.demo.jfinal.util.BeanUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

/**
 * @author tian
 * @Time Dec 10, 2015 11:15:39 AM
 */
public class BlogRedis {

	public static Cache getCache() {

		Cache blogCache = Redis.use(BlogConst.BLOGTABLE);
		return blogCache;

	}

	public static void CacheBlog(Object keyObject,Object valueObject) {

		Cache blogCache = getCache();
		blogCache.set(keyObject,valueObject);
		
	}
	
	public static Blog getBlogFromCache(Object keyObject){

		Cache blogCache = getCache();
		Blog blog = blogCache.get(keyObject);

		return BeanUtil.isBeanEmpty(blog)?null:blog;
	
	}
	
	public static void removeCacheBlog(Object keyObject){
		
		Cache blogCache = getCache();
		blogCache.del(keyObject);
		
	}

}
