/**
 * Dec 10, 2015
 * TestLog.java
 * 2015 10:23:02 PM
 * Created By tian
 */
package com.demo.jfinal;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @author tian
 * @Time Dec 10, 2015 10:23:02 PM
 */
public class TestLog {

	static Logger log = Logger.getLogger(TestLog.class.getName());

	public static void main(String[] args) throws IOException, SQLException {

		log.debug("Debug");
		log.info("Info");
	}
}
