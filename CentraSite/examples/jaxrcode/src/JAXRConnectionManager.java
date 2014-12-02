/*++ Source Code Control System information
* Copyright 2011 by Software AG
*
* Uhlandstrasse 12, D-64297 Darmstadt, GERMANY
*
* All rights reserved
*
* This software is the confidential and proprietary
* information of Software AG ('Confidential Information').
* You shall not disclose such Confidential Information and
* shall use it only in accordance with the terms of the license
* agreement you entered into with Software AG or its distributors.
*
*
*/
package com.centrasite.tutorials.jaxr.classes;

import java.net.PasswordAuthentication;
import java.util.HashSet;
import java.util.Properties;

import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.JAXRException;

import com.centrasite.tutorials.jaxr.interfaces.CentraSiteJAXRConnector;

public class JAXRConnectionManager implements CentraSiteJAXRConnector {
	
	/**
	 * constructor.
	 */
	public JAXRConnectionManager() {
		init();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void init() {
		System.setProperty("javax.xml.registry.ConnectionFactoryClass",
						   "com.centrasite.jaxr.ConnectionFactoryImpl");	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection(String hostname, String uname,
			String password) throws JAXRException {
		System.out.println("connecting...");
		
		Properties p = new Properties();
		p.setProperty("javax.xml.registry.queryManagerURL", "http://"
				+ hostname + "/CentraSite/CentraSite");
		ConnectionFactory connFactory = ConnectionFactory.newInstance();
		connFactory.setProperties(p);

		Connection con = connFactory.createConnection();

		HashSet<PasswordAuthentication> credentials = new HashSet<PasswordAuthentication>(
				1);
		credentials.add(new PasswordAuthentication(uname, password
				.toCharArray()));
		con.setCredentials(credentials);
	
		return con;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void closeConnection(Connection con) throws JAXRException {
		if(con == null)
			return;
		con.close();	
	}

}
