/**
 * Dec 9, 2015
 * CoreConfig.java
 * 2015 2:52:54 PM
 * Created By tian
 */
package com.demo.jfinal.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.demo.jfinal.constant.Const;
import com.demo.jfinal.constant.blog.BlogConst;
import com.demo.jfinal.controller.blog.BlogController;
import com.demo.jfinal.controller.index.IndexController;
import com.demo.jfinal.model.Blog;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.redis.RedisPlugin;

/**
 * JFinal无需XML配置，所以操作都能通过编码完成
 * 
 * @author tian
 * @Time Dec 9, 2015 2:52:54 PM
 */
public class CoreConfig extends JFinalConfig {

	/** 常量配置 **/
	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("system_config_info.txt");
		// 设置开发模式
		me.setDevMode(getPropertyToBoolean("devMode", true));
		me.setUrlParaSeparator(Const.SYMBOLAMPERSAND);
	}

	/** 处理器配置 接管所有web请求，可在此层做功能性的拓展 **/
	@Override
	public void configHandler(Handlers me) {
		DruidStatViewHandler dvh =  new DruidStatViewHandler("/druid");
		me.add(dvh);
		
	}

	/** 拦截器配置 类似与struts2拦截器，处理action **/
	@Override
	public void configInterceptor(Interceptors me) {

		me.add(new TxByMethods("find","update","delete","save")); //声明式事物管理
		
	}

	/** 插件配置 JFinal集成了很多插件：redis,druid,quartz... **/
	@Override
	public void configPlugin(Plugins me) {

		/** 数据库监控druid **/
		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password"));
		dp.addFilter(new StatFilter()); //sql监控
		dp.addFilter(new WallFilter()); //防止sql注入
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");  //mysql
		dp.addFilter(wall);
		me.add(dp);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		me.add(arp);
		
		//表映射关系
		arp.addMapping(BlogConst.BLOGTABLE, "id", Blog.class);
		
		/** redis缓存支持根据不同模块使用缓存，目前我创建一个关于blog的缓存块 **/
		RedisPlugin blogRedis = new RedisPlugin(BlogConst.BLOGTABLE,
				"localhost");
		me.add(blogRedis);

		/** 定时任务插件 目前配置了一个每5秒钟执行一次的定时脚本**/
		QuartzPlugin quartzPlugin =  new QuartzPlugin("job.properies");
		me.add(quartzPlugin);

	}

	/** 路由配置：1.如下配置；2.写个类如：BlogRoute继承Routes，配置，me.add(new BlogRoute());也可 **/
	/** 路径设置：1.第三个参数；2.可通过注解@ActionKey("/index")方式 **/
	@Override
	public void configRoute(Routes me) {

		me.add("/", IndexController.class, "/index");
		/** controller配置路径 **/
		me.add("/blog", BlogController.class);

	}
	
	/*插件关闭之前【方法可选择性使用】**/	
	public void beforeJFinalStop(){
		System.out.println("插件关闭");
	};
	
	/*插件加载完后【方法可选择性使用】**/
	public void afterJFinalStart(){
		System.out.println("加载完毕");
	};

}
