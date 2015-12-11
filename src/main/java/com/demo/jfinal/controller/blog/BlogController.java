/**
 * Dec 9, 2015
 * BlogController.java
 * 2015 3:03:27 PM
 * Created By tian
 */
package com.demo.jfinal.controller.blog;

import org.apache.log4j.Logger;
import com.demo.jfinal.constant.Const;
import com.demo.jfinal.constant.blog.BlogConst;
import com.demo.jfinal.controller.ControllerCommon;
import com.demo.jfinal.model.Blog;
import com.demo.jfinal.redis.blog.BlogRedis;
import com.demo.jfinal.service.blog.BlogService;
import com.demo.jfinal.util.BeanUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author tian
 * @Time Dec 9, 2015 3:03:27 PM
 */
public class BlogController extends Controller {

	public static Logger logger = Logger.getLogger(BlogController.class);
	/**
	 * 分页查询
	 */
	public void index() {

		String pageNumber = this.getPara(Const.PAGENUMBER);
		String pageSize = this.getPara(Const.PAGESIZE);

		Page<Blog> blogPage = BlogService.blogService.paginateBlog(pageNumber,pageSize);
		
		if(blogPage == null){
			ControllerCommon.errorMsg("参数不能为空");
			logger.error("查找失败");
		}
		
		logger.info("查找成功");
		renderJson(ControllerCommon.ctrCommon.returnJsonToClient(blogPage));
	}

	/**
	 * 查找元素
	 */
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

	/**
	 * 删除元素
	 */
	public void delete() {

		boolean result = BlogService.blogService
				.deleteBlogById(getParaToInt(BlogConst.ID));
		if (!result) {
			ControllerCommon.errorMsg("删除失败");
		}
		renderJson(ControllerCommon.ctrCommon.returnJsonToClient(result));
	}

	/**
	 * 添加元素
	 * 
	 * 有话要说：对于添加元素，这种方法应该是最容易想到的，但！还有两种方式应该是更加简化代码的 1. 关键代码:Blog blog =
	 * this.getModel(Blog.class); JFinal可以通过参数名匹配直接组装成“实体类”，然后
	 * 如果需要判断参数是否正确，可通过blog.getInt(); blog.getStr();等方式校验响应参数，之后进行保存；
	 * 
	 * 2.可以通过第三方json工具，把传过来的参数直接转成map，在调用Blog.dao.setAttrs(map).save();也可
	 */
	public void save() {

		String title = this.getPara(BlogConst.TITLE);
		String content = this.getPara(BlogConst.CONTENT);

		String result = BlogService.blogService.saveBlog(title, content);

		if (!StrKit.isBlank(result)) {
			ControllerCommon.errorMsg(result);
		}

		renderJson(ControllerCommon.ctrCommon.returnJsonToClient(result));
	}

	/**
	 * 更新元素
	 * 
	 * 1.可以利用第三方json工具，解析请求参数，再进行数据统一处理
	 */
	public void update() {

		boolean result = BlogService.blogService.updateBlog(getParaToInt(BlogConst.ID),getPara(BlogConst.TITLE));
		if (!result) {
			ControllerCommon.errorMsg("更新失败");
		}

		renderJson(ControllerCommon.ctrCommon.returnJsonToClient(result));
	}
}
