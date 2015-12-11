#JFinal后端开发指南

一、JFinal初识

1MVC架构，设计精巧，使用简单

2遵循COC原则，零配置，无xml

3独创Db + Record模式，灵活便利

4ActiveRecord支持，使数据库开发极致快速

5自动加载修改后的java文件，开发过程中无需重启web server

6AOP支持，拦截器配置灵活，功能强大

7Plugin体系结构，扩展性强

8多视图支持，支持FreeMarker、JSP、Velocity

9强大的Validator后端校验功能

10功能齐全，拥有struts2的绝大部分功能

11体积小仅218K，且无第三方依赖

二、Jfinal开发核心

1.web.xml:

	<filter>
		<filter-name>jfinal</filter-name>
		<filter-class>com.jfinal.core.JFinalFilter</filter-class>
		<init-param>
			<param-name>configClass</param-name>
			<param-value>com.demo.jfinal.config.CoreConfig</param-value>
		</init-param>
	</filter>

	<filter-mapping>
		<filter-name>jfinal</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

2.CoreConfig

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

三．Jfinal MVC思想


1. Model：

public class Blog extends Model<Blog> {

	private static final long serialVersionUID = -3649555563326235483L;

	// 方便于访问数据库，不是必须
	public static final Blog dao = new Blog();
	
	/**
	 * 分页查询数据，jfinal使用原生sql处理数据,会节省解析性能
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Blog> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select * ","from blog order by id asc");
	}
	
	public Blog getLastInsertBlog(){
		return findFirst("select * from blog order by id desc"); //降序
	}
	

2.Controller：

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

3.View：

Jfinal支持FreeMarker、JSP、Velocity

四．Jfinal 组合插件：

1.Druid:

 配置插件：

    /** 数据库监控druid **/
    		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"),
    				getProperty("user"), getProperty("password"));
    		dp.addFilter(new StatFilter()); //sql监控
    		dp.addFilter(new WallFilter()); //防止sql注入
    		WallFilter wall = new WallFilter();
    		wall.setDbType("mysql");  //mysql
    		dp.addFilter(wall);
    		me.add(dp);
		
配置Handler：

    DruidStatViewHandler dvh =  new DruidStatViewHandler("/druid");
    		me.add(dvh);
		
访问路径：

        localhost:8080/Jfinal/druid/index.html

2.Redis:

  配置插件：

		/** redis缓存支持根据不同模块使用缓存，目前我创建一个关于blog的缓存块 **/
		RedisPlugin blogRedis = new RedisPlugin(BlogConst.BLOGTABLE,"localhost");
		me.add(blogRedis);

  Redis非web环境下测试：

		public static void main(String[] args) {
		
		RedisPlugin redisPlugin = new RedisPlugin("test", "localhost");
		redisPlugin.start();
		
		Redis.use("test").set("testDemo", "tdd");
		System.out.println(Redis.use("test").get("testDemo"));
		}

3.Quartz:

 配置插件：

 		QuartzPlugin quartzPlugin =  new QuartzPlugin("job.properies");
		me.add(quartzPlugin);

配置job.properies：

  a.job=com.demo.jfinal.job.JobDemo
  
  a.cron=*/5 * * * * ?
  
  a.enable=true

JobDemo类：

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println("JobDemo开始执行啦" + System.currentTimeMillis());
	}

4.log4j

log4j.properties：

    log4j.rootLogger=INFO,appender1,appender2
    
    log4j.appender.appender1=org.apache.log4j.ConsoleAppender
    
    log4j.appender.appender1.layout=org.apache.log4j.PatternLayout
    
    log4j.appender.appender1.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss:SSS}[%p]: %m%n
    
    log4j.appender.appender2=org.apache.log4j.jdbc.JDBCAppender
    
    log4j.appender.appender2.driver=com.mysql.jdbc.Driver
    
    log4j.appender.appender2.URL=jdbc:mysql://localhost:3306/jfinal_demo?useUnicode=true&characterEncoding=UTF-8
    
    log4j.appender.appender2.user=root
    
    log4j.appender.appender2.password=root
    
    log4j.appender.appender2.sql=insert into log (create_time,log) VALUES ('%d{yyyy-MM-dd hh:mm:ss}', '%c %p %m %n')
    
    log4j.appender.appender2.layout=org.apache.log4j.PatternLayout


Job表：

    Create Table: CREATE TABLE `log` (
    
      `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    
      `create_time` datetime NOT NULL,
    
      `log` varchar(200) NOT NULL,
    
      PRIMARY KEY (`id`)
    
    ) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8

应用：

 		public void find() {
		
		//先从缓存里面找数据
		Blog cacheBlog = BlogRedis.getBlogFromCache(getParaToInt(BlogConst.ID));

		if(!BeanUtil.isBeanEmpty(cacheBlog)){
			logger.info("查找成功");
			renderJson(ControllerCommon.ctrCommon.returnJsonToClient(cacheBlog));
		}else {
			//缓存没有数据，从DB读
			Blog blog = BlogService.blogService.findBlogById(getParaToInt(BlogConst.ID));
			if (BeanUtil.isBeanEmpty(blog)) {
				logger.error("查找失败");
				ControllerCommon.errorMsg("数据为空");
			}
			logger.info("查找成功");
			renderJson(ControllerCommon.ctrCommon.returnJsonToClient(blog));
		}
	}

5..JFinal单元测试：

JFinalModelCase：

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


编写测试类（需继承JFinalModelCase）

	@Test
	public void testFind() {

		Blog blog = BlogService.blogService.findBlogById(1);
		System.out.println(blog);
	}

	@Test
	public void testJson() {

		// HashMap<String, String> map = ControllerCommon.errorMsg("失败");
		// String string = JsonKit.toJson(map);

		String columns = BlogConst.CONTENT + Const.SYMBOLCOMMA
				+ BlogConst.TITLE;
		Blog blog = Blog.dao.findById(1, columns);

		ControllerCommon.errorMsg("执行");

		JFinalTestUtil.print(JsonKit.toJson(ControllerCommon.ctrCommon
				.returnJsonToClient(blog)));

	}

	@Test
	public void testFindFirst() {

		Blog blog = Blog.dao.getLastInsertBlog();
		JFinalTestUtil.print(String.valueOf(blog.getInt("id")));
		
	}

五、开发实例

index：

http://localhost:8080/Jfinal/blog/index?pageNumber=1&pageSize=10

  {
  
    "result": {
  
      "pageSize": 10,
  
      "pageNumber": 1,
  
      "list": [
  
        {
  
          "content": "JFinal Demo Content here",
  
          "id": 1,
  
          "title": "JFinal Demo Title here"
  
        },
  
        {
  
          "content": "test 1",
  
          "id": 2,
  
          "title": "test 1"
  
        },
  
        {
  
          "content": "test 2",
  
          "id": 3,
  
          "title": "test 2"
  
        },
  
        {
  
          "content": "test 3",
  
          "id": 4,
  
          "title": "test 3"
  
        },
  
        {
  
          "content": "test 4",
  
          "id": 5,
  
          "title": "test 4"
  
        }
  
      ],
  
      "totalRow": 5,
  
      "totalPage": 1
  
    },
  
    "status": "200000",
  
    "msg": "成功"
  
  }

find:

http://localhost:8080/Jfinal/blog/find?id=2

  {
  
    "result": {
  
      "content": "test 1",
  
      "title": "test 1"
  
    },
  
    "status": "200000",
  
    "msg": "成功"
  
  }

delete:

    http://localhost:8080/Jfinal/blog/delete?id=1

  {
  
    "result": true,
  
    "status": "200000",
  
    "msg": "成功"
  
  }

save:

    http://localhost:8080/Jfinal/blog/save?title=1&content=1

  {
  
    "result": "",
  
    "status": "200000",
  
    "msg": "成功"
  
  }

update:

http://localhost:8080/Jfinal/blog/update?id=6&title=2

  {
  
    "result": true,
  
    "status": "200000",
    
    "msg": "成功"
  
  }

六、Jfinal扩展

 1. 本demo中我建立了blog模块，其他模块建议以下架构风格：一个模块一个service，一个模块一个Redis，一个模块多个Model，一    	    个模块一个Controller
 
 2. 项目扩展：建议抽象出BaseModel，所有的model只需要集成它，减少代码的冗余
 
 3. 日志：实际开发过程中会使用到更多可用字段：userId，iP等；建议抽象log代码块，或者在filter中做相应处理
 
 4. 为了极速开发，中小型项目，可以不使用Service层，而且业务全部放入Model，称之为充血领域模型
