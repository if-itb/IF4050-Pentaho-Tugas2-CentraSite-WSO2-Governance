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
package com.centrasite.tutorials.jaxr.tests;


import static org.junit.Assert.*;

import java.util.Collections;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.centrasite.jaxr.CentraSiteQueryManager;
import com.centrasite.jaxr.infomodel.CentraSiteRegistryObject;
import com.centrasite.jaxr.infomodel.Constants;
import com.centrasite.jaxr.type.CentraSiteFileAttribute;
import com.centrasite.jaxr.type.CentraSiteTypeDescription;
import com.centrasite.tutorials.jaxr.classes.JAXRHandler;

/**
 * Tests for JAXRHandler class.
 * 
 * @author wrkpki
 * @version 1.0
 * @see JAXRHandler
 *
 */
public class JAXRHandlerTest {
	
	private static JAXRHandler handler = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		handler = new JAXRHandler();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		handler.closeCon();
	}

	@Before
	public void setUp() throws Exception {
		handler.openCon("localhost:53305", "Administrator", "manage");
	}
	
	@After
	public void tearDown() throws Exception {
		//handler.closeCon();
	}
	
	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#createService(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateService() {
		try {
			Service service1 = handler.createService("jUnitTestService", "description");	
			
			assertNotNull("service should not be null", service1);
			assertEquals("name of the service should be 'jUnitTestService'", "jUnitTestService", service1.getName().getValue());
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#createTaxonomy(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateTaxonomy() {
		try {
			ClassificationScheme taxonomy = handler.createTaxonomy("jUnitTestTaxonomy", "description");
			
			assertNotNull("taxonomy should not be null", taxonomy);
			assertEquals("name of the taxonomy should be 'jUnitTestTaxonomy'", "jUnitTestTaxonomy", taxonomy.getName().getValue());
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#saveObject(java.lang.Object)}.
	 */
	@Test
	public void testSaveObject() {
		try {
			Service service1 = handler.createService("jUnitTestService2", "description");
			
			handler.saveObject(service1);
			Service service2 = handler.findServiceByName("jUnitTestService2");
			
			assertNotNull("service2 should not be null", service2);
			assertEquals("service2 should be equal to service1", service2, service1);
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#findServiceByName(java.lang.String)}.
	 */
	@Test
	public void testFindServiceByName() {
		try {
			Service service1 = handler.createService("jUnitTestServiceForSearching", "test service for search method");
			handler.saveObject(service1);
			
			Service service2 = handler.findServiceByName("jUnitTestServiceForSearching");
			assertNotNull("service2 should not be null", service2);
			assertEquals("service2 should be equal to service1", service2, service1);
			assertEquals("the name of service2 should be 'jUnitTestServiceForSearching'", "jUnitTestServiceForSearching", service2.getName().getValue());
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#findTaxonomyByName(String name)}.
	 */
	@Test
	public void testFindTaxonomyByName() {
		try {
			ClassificationScheme taxonomy = handler.createTaxonomy("jUnitTestTaxonomyForSearching", "Taxonomy for jUnit test");
			
			handler.saveObject(taxonomy);
			
			ClassificationScheme tax = handler.findTaxonomyByName("jUnitTestTaxonomyForSearching");
			
			assertNotNull("tax should not be null", tax);
			assertEquals("tax should be equal to taxonomy", tax, taxonomy);
			assertEquals("the name of tax should be 'jUnitTestTaxonomyForSearching'", "jUnitTestTaxonomyForSearching", tax.getName().getValue());
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#deleteObject(javax.xml.registry.infomodel.Key)}.
	 */
	@Test
	public void testDeleteObject() {
		try {
			Service service = handler.createService("serviceToDelete", "will be deleted");
			handler.saveObject(service);
			
			handler.deleteObject(service.getKey());
			
			service = handler.findServiceByName("serviceToDelete");
			assertNull("the service should be null as there should be no service with this name anymore", service);
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#addConceptsToConcept(java.lang.String, java.util.Collection)}.
	 */
	@Test
	public void testCreateConcept() {
		try {
			ClassificationScheme tax = handler.createTaxonomy("myTaxonomy", "desc");
			
			Concept con = handler.createConcept(tax, "myConcept", "a test concept");
			handler.saveObject(tax);
			tax = handler.findTaxonomyByName("myTaxonomy");
			
			assertTrue("taxonomy should have child concept 'con'", tax.getChildrenConcepts().contains(con));
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#addSlotToService(javax.xml.registry.infomodel.Service, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddSlotToService() {
		try {
			Service service = handler.createService("slotService", "to add a slot");
			
			handler.addSlotToService(service, "jUnitSlot", "test slot");
			
			assertFalse("service should have a slot", service.getSlots().isEmpty());
			assertNotNull("service should have a slot 'jUnitSlot'", service.getSlot("jUnitSlot"));
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#createClassificationAttribute(com.centrasite.jaxr.infomodel.CentraSiteRegistryObject, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)}.
	 */
	@Test
	public void testCreateClassificationAttribute() {
		try {
			Service service = handler.createService("classified Service", "a classified service");
			handler.saveObject(service);
			service = handler.findServiceByName("classified Service");
			
			Service classService = (Service) handler.createClassificationAttribute( // add the classification attribute to the service
					(CentraSiteRegistryObject) service, "Service", Constants.PRODUCTS_KEY_CentraSite, "Product" , true);
		   
		    assertTrue("service should be classified as CentraSite product",
		    		  ((CentraSiteRegistryObject)classService).isClassifiedWith(Constants.PRODUCTS_KEY_CentraSite));
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#createRelationshipAttribute(com.centrasite.jaxr.infomodel.CentraSiteRegistryObject, java.lang.String, java.lang.String, java.util.Collection, java.lang.Boolean)}.
	 */
	@Test
	public void testCreateRelationshipAttribute() {
		try {
			Service relatingService = handler.createService("relating Service", "a service using another service");
			
			Service relatedService = handler.createService("related Service", "a related service");

			Service relService = (Service) handler.createRelationshipAttribute(
					(CentraSiteRegistryObject)relatingService, "Service", "Uses", Collections.singleton((RegistryObject)relatedService), true);
			
			assertTrue("relatingService should have relation to relatedService with 'Uses' association",
					  ((CentraSiteRegistryObject)relService).isAssociatedTo(relatedService.getKey().getId(), Constants.ASSOCIATION_TYPE_KEY_Uses));
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#createFileAttribute(com.centrasite.jaxr.infomodel.CentraSiteRegistryObject, java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)}.
	 */
	@Test
	public void testCreateFileAttribute() {
		try {
			Service service1 = handler.createService("file service", "a service with a file attribute");
			
			Service service2 = (Service) handler.createFileAttribute((CentraSiteRegistryObject)service1, "Service", "http://...", "Functional-Requirements");
			
			CentraSiteQueryManager cqm = (CentraSiteQueryManager) ((CentraSiteRegistryObject)service2).getRegistryService().getBusinessQueryManager();
			CentraSiteTypeDescription td = cqm.getTypeDescription("Service");

			CentraSiteFileAttribute attribute = null;
			for (CentraSiteFileAttribute fa : td.getFileAttributes()) {
				if (fa.getName().equals("Functional-Requirements")) {
					attribute = fa;
					break;
				}
			}
			
			assertTrue("service1 should contain file attribute with value 'http://...'",
					  ((CentraSiteRegistryObject)service2).getFileValue(attribute).contains("http://..."));
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.centrasite.tutorials.jaxr.classes.JAXRHandler#createUserDefinedObject(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateUserDefinedObject() {
		fail("Not yet implemented");
	}

}
