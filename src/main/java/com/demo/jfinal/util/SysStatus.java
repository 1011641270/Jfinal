/**
 * Dec 9, 2015
 * SysStatus.java
 * 2015 8:58:59 PM
 * Created By tian
 */
package com.demo.jfinal.util;

import java.util.HashMap;

/**
 * @author tian
 * @Time Dec 9, 2015 8:58:59 PM
 */
public class SysStatus {

	public static String CODECOMMONERROR = "400400"; // 默认错误编码
	public static String CODESUCCESS = "200000"; // 成功

	public static HashMap<String, String> map = new HashMap<String, String>();

	public static HashMap<String, String> initStatusMap() {

		map.put(SysStatus.CODECOMMONERROR, "系统错误");
		map.put(SysStatus.CODESUCCESS, "成功");

		return map;
	}

}
