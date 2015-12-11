/**
 * Dec 9, 2015
 * JFinalModelCase.java
 * 2015 8:32:12 PM
 * Created By tian
 */
package com.demo.jfinal;

import org.junit.After;
import org.junit.BeforeClass;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.demo.jfinal.constant.blog.BlogConst;
import com.demo.jfinal.model.Blog;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * @author tian
 * @Time Dec 9, 2015 8:32:12 PM
 */
public class JFinalModelCase {

	protected static DruidPlugin dp;
	protected static ActiveRecordPlugin activeRecord;

	/**
	 * 数据连接地址
	 */
	private static final String URL = "jdbc:mysql://127.0.0.1/jfinal_demo?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_TYPE = "mysql";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dp = new DruidPlugin(URL, USERNAME, PASSWORD, DRIVER);
		dp.addFilter(new StatFilter());
		dp.setInitialSize(3);
		dp.setMinIdle(2);
		dp.setMaxActive(5);
		dp.setMaxWait(60000);
		dp.setTimeBetweenEvictionRunsMillis(120000);
		dp.setMinEvictableIdleTimeMillis(120000);

		WallFilter wall = new WallFilter();
		wall.setDbType(DATABASE_TYPE);
		dp.addFilter(wall);

		dp.getDataSource();
		dp.start();

		activeRecord = new ActiveRecordPlugin(dp);
		activeRecord.setDialect(new MysqlDialect()).setDevMode(true)
				.setShowSql(true); // 是否打印sql语句

		// 映射数据库的表和继承与model的实体
		// 只有做完该映射后，才能进行junit测试
		activeRecord.addMapping(BlogConst.BLOGTABLE, Blog.class); // 测试其他Model可以继续添加配置
		activeRecord.start();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		activeRecord.stop();
		dp.stop();
	}

}
