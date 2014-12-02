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
package com.centrasite.tutorials.jaxr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.Connection;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.UnsupportedCapabilityException;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.RegistryEntry;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Slot;

import com.centrasite.jaxr.CentraSiteQueryManager;
import com.centrasite.jaxr.infomodel.CentraSiteRegistryObject;
import com.centrasite.jaxr.type.CentraSiteClassificationAttribute;
import com.centrasite.jaxr.type.CentraSiteFileAttribute;
import com.centrasite.jaxr.type.CentraSiteRelationShipAttribute;
import com.centrasite.jaxr.type.CentraSiteTypeDescription;

/**
 * 
 * @author wrkpki
 */
public class JAXRHandler implements CentraSiteJAXRHandler {
	/**
	 * class attributes
	 */
	private Connection con = null;
	private RegistryService regService = null;
	private JAXRConnectionManager conMngr = null;

	/**
	 * constructor
	 */
	public JAXRHandler() {
		init();
	}// end constructor

	/**
	 * 
	 * @param con
	 */
	public void init() {
		conMngr = new JAXRConnectionManager();
	}// end init

	/**
	 * @throws JAXRException
	 * 
	 */
	public void openCon(String hostname, String username, String password)
			throws JAXRException {
		if (con == null) {
			con = conMngr.getConnection(hostname, username, password);
			regService = con.getRegistryService();
		} else {
			return;
		}
	} // end open
	
	/**
	 * 
	 * @throws JAXRException
	 */
	public void closeCon() throws JAXRException {
		if(con.isClosed())
			return;
		conMngr.closeConnection(con);
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveObject(Object obj) throws JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager();
		blm.saveObjects(Collections.singleton(obj));
	}// end saveObject

	/**
	 * {@inheritDoc}
	 */
	public Service createService(String name, String desc) throws JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager(); 
		InternationalString sname = blm.createInternationalString(name);
		Service service = blm.createService(sname);
		InternationalString description = blm.createInternationalString(desc);
		service.setDescription(description);

		ServiceBinding serviceBinding = blm.createServiceBinding(); // create service binding with specification links
		service.addServiceBinding(serviceBinding);
		return service;
	}// end createService
	
	/**
	 * {@inheritDoc}
	 */
	public ClassificationScheme createTaxonomy(String name, String desc) throws JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager(); // create BusinessLifeCycleManager
		InternationalString inName = blm.createInternationalString(name); // convert name into InternationalString
		InternationalString inDesc = blm.createInternationalString(desc); // convert description into InternationalString
		
		ClassificationScheme taxonomy = blm.createClassificationScheme(inName, inDesc); // create the taxonomy, i.e. the ClassificationScheme
		
		return taxonomy;
	}// end createTaxonomy
	
	/**
	 * {@inheritDoc}
	 */
	public Concept createConcept(RegistryObject obj, String name, String desc) throws JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager(); // create BusinessLifeCycleManager
		InternationalString inName = blm.createInternationalString(name);
		
		Concept concept = blm.createConcept(obj, inName, desc);
		
		return concept;
	} // end createConcept
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public Service findServiceByName(String name) throws JAXRException {
		CentraSiteQueryManager cqm = (CentraSiteQueryManager) regService.getBusinessQueryManager();
		
		Collection<String> qualifiers = Collections
				.singleton(FindQualifier.EXACT_NAME_MATCH);
		Collection<String> namePatterns = Collections.singleton(name);
		BulkResponse response = cqm.findServices(null, qualifiers,
				namePatterns, null, null);
		Collection<Service> servCol = response.getCollection();
		Iterator<Service> iter = servCol.iterator();
		
		Service service = null;
		if (iter.hasNext()) {
			service = (Service) iter.next();
		} else {
			return null;
		}

		return service;
	}// end findService
	
	/**
	 * {@inheritDoc}
	 */
	public ClassificationScheme findTaxonomyByName(String name) throws JAXRException {
		CentraSiteQueryManager cqm = (CentraSiteQueryManager) regService.getBusinessQueryManager();
		ClassificationScheme taxonomy = cqm.findClassificationSchemeByName(Collections.singleton(FindQualifier.EXACT_NAME_MATCH), name);

		return taxonomy;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public void deleteObject(Key key) throws JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager();

		BulkResponse response = blm.deleteObjects(Collections.singleton(key));
		System.out.println(response.getExceptions());
	}// end deleteService

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public void addSlotToService(Service service, String slotName,
			String slotValue) throws JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager();
		Slot slot = blm.createSlot(slotName, slotValue, null);
		service.addSlot(slot);
	}// end addSlotToService

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public CentraSiteRegistryObject createClassificationAttribute(
			CentraSiteRegistryObject ro, String type, String conceptKey,
			String classificationAttributeName, Boolean keepExisting)
			throws JAXRException {
		CentraSiteQueryManager cqm = (CentraSiteQueryManager) regService.getBusinessQueryManager();
		CentraSiteTypeDescription td = cqm.getTypeDescription(type);
		
		CentraSiteClassificationAttribute clAttr = null;
		for (CentraSiteClassificationAttribute ca : td
				.getClassificationAttributes()) {
			if (ca.getName().equals(classificationAttributeName)) {
				clAttr = ca;
				break;
			}
		}
		Concept classifingConcept = (Concept) cqm.getRegistryObject(conceptKey);
		System.out.println(classifingConcept.toString());
		ArrayList<Object> values = new ArrayList<Object>();

		if (keepExisting) {
			values.addAll(ro.getClassificationValue(clAttr));
		}
		values.add(classifingConcept);
		ro.setClassificationValue(clAttr, values);

		System.out.println("created classification attr...");
		return ro;
	}// end createClassificationAttribute

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public CentraSiteRegistryObject createClassificationAttribute(
			CentraSiteRegistryObject ro, String type, String conceptKey,
			String classificationAttributeName) throws JAXRException {
		return createClassificationAttribute(ro, type, conceptKey,
				classificationAttributeName, true);
	}// end createClassificationAttribute

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public CentraSiteRegistryObject createRelationshipAttribute(
			CentraSiteRegistryObject ro, String type, String relationshipName,
			Collection<RegistryObject> targetROs, Boolean keepExisting)
			throws JAXRException {
		CentraSiteQueryManager cqm = (CentraSiteQueryManager) regService.getBusinessQueryManager();
		CentraSiteTypeDescription td = cqm.getTypeDescription(type);

		CentraSiteRelationShipAttribute attribute = null;
		for (CentraSiteRelationShipAttribute ca : td
				.getRelationShipAttributes()) {
			if (ca.getName().equals(relationshipName)) {
				attribute = ca;
				break;
			}
		}
		Collection<RegistryObject> tros = new ArrayList<RegistryObject>();
		tros.addAll(targetROs);
		if (keepExisting) {
			tros.addAll(ro.getRelationShipValue(attribute));
		}
		ro.setRelationShipValue(attribute, targetROs);

		return ro;
	} // end createRelationshipAttribute
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public CentraSiteRegistryObject createRelationshipAttribute(
			CentraSiteRegistryObject ro, String type, String relationshipName,
			Collection<RegistryObject> targetROs) throws JAXRException {
		return createRelationshipAttribute(ro, type, relationshipName,
				targetROs, true);
	} // end createRelationshipAttribute

	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public CentraSiteRegistryObject createFileAttribute(
			CentraSiteRegistryObject ro, String type, String fileName,
			String fileAttrName, Boolean keepExisting) throws JAXRException {
		CentraSiteQueryManager cqm = (CentraSiteQueryManager) regService.getBusinessQueryManager();
		CentraSiteTypeDescription td = cqm.getTypeDescription(type);

		CentraSiteFileAttribute attribute = null;
		for (CentraSiteFileAttribute fa : td.getFileAttributes()) {
			if (fa.getName().equals(fileAttrName)) {
				attribute = fa;
				break;
			}
		}
		Collection<String> values = new ArrayList<String>();
		if (keepExisting) {
			values.addAll(ro.getFileValue(attribute));
		}
		values.add(fileName);
		ro.setFileValue(attribute, values);
		return ro;
	} // end createFileAttribute
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws JAXRException
	 */
	public CentraSiteRegistryObject createFileAttribute(
			CentraSiteRegistryObject ro, String type, String fileName,
			String fileAttrName) throws JAXRException {
		return createFileAttribute(ro, type, fileName, fileAttrName, true);
	} // end createFileAttribute
	
	/**
	 * {@inheritDoc}
	 */
	public RegistryEntry createUserDefinedObject(String name, String description, String type) throws InvalidRequestException, UnsupportedCapabilityException, JAXRException {
		BusinessLifeCycleManager blm = regService.getBusinessLifeCycleManager();
		RegistryEntry userTypeObject = (RegistryEntry) blm.createObject(type);
	
		userTypeObject.setName(blm.createInternationalString(name));
		userTypeObject.setDescription(blm.createInternationalString(description));
		
		return userTypeObject;
	} // end createUserDefinedObject
	
}// end class JAXRTutorial
