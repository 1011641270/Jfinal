/**
 * Dec 9, 2015
 * ControllerCommon.java
 * 2015 8:50:49 PM
 * Created By tian
 */
package com.demo.jfinal.controller;

import java.util.HashMap;

import com.demo.jfinal.constant.Const;
import com.demo.jfinal.util.SysStatus;
import com.jfinal.core.Controller;

/**
 * @author tian
 * @Time Dec 9, 2015 8:50:49 PM
 */
public class ControllerCommon extends Controller {

	public static ControllerCommon ctrCommon = new ControllerCommon();

	public static volatile boolean isError = false;
	public static HashMap<String, Object> resultMap = new HashMap<String, Object>();

	public static HashMap<String, Object> initHashMap() {

		resultMap.put(Const.STATUS, SysStatus.CODESUCCESS);
		resultMap.put(Const.MESSAGE,
				SysStatus.initStatusMap().get(SysStatus.CODESUCCESS));

		return resultMap;
	}

	public static HashMap<String, Object> errorMsg(Object msg) {

		isError = true;
		resultMap.clear();
		
		resultMap.put(Const.STATUS, SysStatus.CODECOMMONERROR);
		resultMap.put(Const.MESSAGE, msg);
		return resultMap;

	}

	public static HashMap<String, Object> errorCode(String code) {

		isError = true;
		resultMap.clear();
		
		resultMap.put(Const.STATUS, code);
		resultMap.put(Const.MESSAGE, SysStatus.initStatusMap().get(code));
		return resultMap;

	}

	public static boolean isError() {
		return isError;
	}

	public HashMap<String, Object> returnJsonToClient(Object object) {

		if (!isError()) {
			resultMap.clear();
			resultMap = initHashMap();
			resultMap.put(Const.RESULT, object);
		}else {
			isError = false;  //初始化
		}

		return resultMap;

	}

}
