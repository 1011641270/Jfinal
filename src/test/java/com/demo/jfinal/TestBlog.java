package com.demo.jfinal;

import org.junit.Test;

import com.demo.jfinal.constant.Const;
import com.demo.jfinal.constant.blog.BlogConst;
import com.demo.jfinal.controller.ControllerCommon;
import com.demo.jfinal.model.Blog;
import com.demo.jfinal.service.blog.BlogService;
import com.demo.jfinal.util.JFinalTestUtil;
import com.jfinal.kit.JsonKit;

/**
 * Unit test for simple App.
 */

public class TestBlog extends JFinalModelCase {

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

	
}
