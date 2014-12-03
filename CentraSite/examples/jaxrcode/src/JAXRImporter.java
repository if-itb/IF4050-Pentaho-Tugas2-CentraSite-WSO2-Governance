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

import java.util.Scanner;

import javax.xml.registry.JAXRException;

import com.centrasite.jaxr.JAXRAccessor;
import com.centrasite.jaxr.schema.SchemaImporter;
import com.centrasite.jaxr.webservice.WSDLCallbackException;
import com.centrasite.jaxr.webservice.WSDLCallbackParameters;
import com.centrasite.jaxr.webservice.WebServiceRegistrator;
import com.centrasite.jaxr.webservice.WsdlImportParameters;
import com.centrasite.tutorials.jaxr.interfaces.CentraSiteJAXRImporter;

/**
 * The implementation of the CentraSiteWSDLImportter
 * 
 * @author Software AG
 * @version 1.0
 * @see CentraSiteJAXRImporter
 */
public class JAXRImporter implements CentraSiteJAXRImporter{
	
	private static final int REUSE = 1;
	private static final int OVERWRITE = 2;
	private static final int NEW_VERSION = 3;
	
	private JAXRAccessor jaxr = null;
	
	/**
	 * constructor.
	 * @throws JAXRException 
	 */
	public JAXRImporter() {	
	}
	
	/**
	 * 
	 */
	public void openCon() throws JAXRException {
		jaxr = new JAXRAccessor("http://localhost:53305/CentraSite/CentraSite","Administrator", "manage");
	}
	
	/**
	 * 
	 */
	public void closeCon() {
		jaxr.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void importWSDL(String wsdlFile, String orgName, String productConceptName) throws Exception {
		WebServiceRegistrator wsr = new WebServiceRegistrator(wsdlFile, jaxr);
		wsr.setOrganization(orgName);
		//wsr.setProductConcept(productConceptName);
		wsr.register();
	}// end importWSDL
	
	/**
	 * {@inheritDoc}
	 */
	public void importXSD(String xsdFile) throws Exception {
		SchemaImporter si = new SchemaImporter(xsdFile, jaxr);
		si.add();
	}// end importXSD
	
	/**
	 * {@inheritDoc}
	 */
	public void customImportWSDL(String wsdlFile, String orgName, String productConceptName) throws Exception {
		boolean create = dialogueNew(); // console inquiry if user wants to create a new version of the WSDL
		WebServiceRegistrator wsr = null;
		WsdlImportParameters importParameters = new WsdlImportParameters(jaxr);
	    importParameters.setMode(WsdlImportParameters.MODE_REUSE_IMPORT_FILES); // reuse all importet/included files in the registry
        importParameters.setInteractiveResolution(true); // callback exceptions can now be thrown, by default this is false
        boolean retry;
        do { // do try to register new version of service
        	retry = false;
        	try {
        		wsr = new WebServiceRegistrator(wsdlFile, jaxr, importParameters, null, null);
        		wsr.setOrganization(orgName);
        		wsr.setProductConcept(productConceptName);
        		wsr.setCreateVersion(create); // set create new version to true
        		wsr.register(); // try to register the WSDL, i.e. web service
        		
        	} catch(WSDLCallbackException we) { // catches exception if there are untreated XSD files left
        		WSDLCallbackParameters cb = we.getCallbackParameters();
        		String action;
        		switch(dialogueAction(cb.getLocation())) { // 
        			case REUSE: 
        				importParameters.addReuseFileObject(cb, cb.getDefaultUsedObject()); // use the file again
        				break;
        			case OVERWRITE: 
        				action = "overwrite";
        				importParameters.addReuseFileOverwrite(cb, dialoguePath(action)); // overwrite the file with a new one
        				break;
        			case NEW_VERSION: 
        				action = "create a new version of";
        				importParameters.addReuseFileCreateVersion(cb, dialoguePath(action)); // create a new version
        				break;	
        			default:
        				importParameters.addReuseFileObject(cb, cb.getDefaultUsedObject()); // use the file again
        				break;
        		} // end switch
        		retry = true;
        	}// end catch	
        } while(retry); // until the service could be registered without exception
        System.out.println("Completed!");
	}
	
	/**
	 * Console inquiry requesting if a new version of the WSDL should be created.
	 * 
	 * @return
	 */
	public boolean dialogueNew() {
		boolean create;
		System.out.println("Do you want to create a new Version of the WSDL?");
		System.out.println("0: No");
		System.out.println("1: Yes");
		System.out.print("Decision: ");
		Scanner sc = new Scanner(System.in);
		int tmp = sc.nextInt();
		
		if(tmp == 0){
			create = false;
		} else if(tmp == 1){
			create = true;
		} else {
			create = false;
		}
		return create;
	}// end createNewVersion
	
	/**
	 * Console inquiry requesting what to do with the XSD file.
	 * 
	 * @param name - the location and name of the XSD file
	 * @return
	 */
	public int dialogueAction(String name) {
		System.out.println ((char)33 + "[2J");
		int action;
		System.out.println();
		System.out.println("What do you want to do with " + name + "?");
		System.out.println("1: REUSE");
		System.out.println("2: OVERWRITE");
		System.out.println("3: NEW_VERSION");
		System.out.print("Decision: ");
		Scanner sc = new Scanner(System.in);
		action = sc.nextInt();
		return action;
	}// end xsdAction
	
	/**
	 * Console inquiry requesting the path to the new XSD file.
	 * 
	 * @param action - the chosen action
	 * @return
	 */
	public String dialoguePath(String action) {
		String path;
		System.out.println();
		System.out.println("You chose to " + action + " the file. Please specify the path to the new file!");
		System.out.print("New File: ");
		Scanner sc = new Scanner(System.in);
		path = sc.nextLine();
		return path;
	}// end getXSDPath

}
