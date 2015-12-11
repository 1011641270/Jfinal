/**
 * Dec 9, 2015
 * IndexController.java
 * 2015 3:04:16 PM
 * Created By tian
 */
package com.demo.jfinal.controller.index;

import com.jfinal.core.Controller;

/**
 * @author tian
 * @Time  Dec 9, 2015 3:04:16 PM
 */
public class IndexController extends Controller{
	
	
	public void index(){
		renderText("hello JFinal");
	}

}
