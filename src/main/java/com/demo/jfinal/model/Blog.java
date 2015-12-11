/**
 * Dec 9, 2015
 * Blog.java
 * 2015 3:27:31 PM
 * Created By tian
 */
package com.demo.jfinal.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author tian
 * @Time Dec 9, 2015 3:27:31 PM
 */

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
	
}
