package sample;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.registry.JAXRException;

import org.apache.log4j.Logger;

import sample.model.Action;
import sample.model.Entry;
import sample.model.Item;
import sample.model.RBWCA;
import sample.model.RBWFA;
import sample.model.RBWRA;
import sample.model.WSPolicy;
import sample.model.Entry.EntryCode;

import com.centrasite.importexport.BatchEventCallback;
import com.centrasite.jaxr.importexport.IEventCallback;
import com.centrasite.importexport.ImportOperation;
import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.CSAppFrameworkException;
import com.softwareag.centrasite.appl.framework.Configuration;
import com.softwareag.centrasite.appl.framework.SessionContext;
import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.standard.Concept;
import com.softwareag.centrasite.appl.framework.beans.standard.ExternalLink;
import com.softwareag.centrasite.appl.framework.persistence.BeanMode;
import com.softwareag.centrasite.appl.framework.persistence.BeanPool;
import com.softwareag.centrasite.appl.framework.persistence.RegistryProvider;
import com.softwareag.centrasite.appl.framework.persistence.impl.StandaloneRegistryProvider;
import com.softwareag.centrasite.appl.framework.persistence.search.Predicates;
import com.softwareag.centrasite.appl.framework.persistence.search.Search;
import com.softwareag.centrasite.appl.framework.types.AttributeDescription;
import com.softwareag.centrasite.appl.framework.types.TypeDescription;
import com.softwareag.centrasite.appl.framework.types.TypeManager;

/**
 * Sample program demonstrating the functionality of the CentraSite Application
 * Framework.
 * 
 * @author <a href="mailto:Alexander.Pankov@softwareag.com">Alexander Pankov</a>
 * 
 * 
 */
public class CSAFSample {

	/**
	 * Local name of the created type with relationship attribute (TWRA)
	 */
	public static final String TWRA_TYPE_LOCAL_NAME = "TWRA";
	/**
	 * Local name of the created type with classification attribute (TWCA)
	 */
	public static final String TWCA_TYPE_LOCAL_NAME = "TWCA";
	/**
	 * Namespace of the created types
	 */
	public static final String TYPE_NAMESPACE = "http://namespaces.CentraSite.com/csaf";

	/**
	 * Name of the type with relationship attribute into the repository
	 */
	public static final String TWRA_TYPE_NAME = "{" + TYPE_NAMESPACE + "}"
			+ TWRA_TYPE_LOCAL_NAME;
	/**
	 * Name of the type with classification attribute into the repository
	 */
	public static final String TWCA_TYPE_NAME = "{" + TYPE_NAMESPACE + "}"
			+ TWCA_TYPE_LOCAL_NAME;
	private static final String DEFAULT_REPOSITORY_SEARCHED_TYPE = "{"
			+ TYPE_NAMESPACE + "}Item";
	/**
	 * logger used for the example to log messages
	 */
	private static Logger logger = Logger.getLogger(CSAFSample.class);

	public static Collection<RegistryBean> rbToDelete = new LinkedList<RegistryBean>();
	/**
	 * Used association into relationship attribute
	 */
	private static final String HAS_OUTPUT_CONCEPT_PATH = "/uddi:d612efe1-f11f-3bf2-f899-e0e21e952a87/HasOutput";
	/**
	 * Concept uddi key to search existing EntryCode
	 */
	private final static String CONCEPT_UDDI_KEY = "uddi:2f50b112-ef84-11dc-bf42-917dfb18024d";
	/**
	 * The Registry provider used in samples
	 */
	private RegistryProvider provider;
	/**
	 * The SessionContext context object used as entry point to the framework.
	 */
	private SessionContext context;

	/**
	 *if delete Registry Beans is set to true then all registry objects and
	 * types will be deleted in the end of the sample application
	 */
	private boolean deleteRegistryBeans = true;

	/**
	 * Username used for connecting to the JAXR registry.
	 */
	private String registryUsername;

	/**
	 * Password used for connecting to the JAXR registry.
	 */
	private String registryPassword;
	/**
	 * url to connect to CentraSite repository
	 */
	private String centrasiteDBUrl;
	/**
	 * zip file path. File will be uploaded to the CentraSite repository
	 */
	private String zipfile;
	/**
	 * UDDI key of the created Item object that will be deleted at the end
	 */

	private String itemObjectId;

	/**
	 * UDDI key of the created RBWRA object that will be deleted at the end
	 */
	private String rbwraId;

	/**
	 * UDDI key of the created RBWCA object that will be deleted at the end
	 */
	private String rbwcaId;

	/**
	 * UDDI key of the created RBWCA object that will be deleted at the end
	 */
	private String rbwfaId;
	/**
	 * The BACKED bean mode provides advantages like locking, lazy loading, etc.
	 */
	private BeanMode beanMode = BeanMode.BACKED;

	/**
	 * Starts the application
	 * 
	 * @param args
	 * @throws CSAppFrameworkException
	 */
	public static void main(String[] args) throws Exception {

		CSAFSample sample = new CSAFSample();
		// initialize the persistence framework
		sample.init(args);
		// run the sample
		sample.run();

	}

	/**
	 * Initializes the framework by creating SessionContext instance with a
	 * stand-alone registry access.
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void init(String[] args) throws CSAppFrameworkException {
		// parse the arguments. check if created objects should be deleted
		parseArguments(args);
		// Initialize SessionContext instance using stand-alone registry
		// provider. Alternative is the PluggableUI based provider.
		// BrowserBehaviour is set to true because of using the TypeManager
		setConnection(true);
	}

	/**
	 * Parses the arguments for configuration arguments. Looks for the
	 * "-deleteObjects" flag.
	 */
	private void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ("-delete".equalsIgnoreCase(args[i])) {
				i++;
				deleteRegistryBeans = Boolean.parseBoolean(args[i]);
				continue;
			} else if ("-user".equalsIgnoreCase(args[i])) {
				i++;
				registryUsername = args[i];
				continue;
			} else if ("-pass".equalsIgnoreCase(args[i])) {
				i++;
				registryPassword = args[i];
				continue;
			} else if ("-centrasiteurl".equalsIgnoreCase(args[i])) {
				i++;
				centrasiteDBUrl = args[i];
				continue;
			} else if ("-zipfile".equalsIgnoreCase(args[i])) {
				i++;
				zipfile = args[i];
				continue;
			}
		}

		if (registryUsername == null || registryUsername.length() == 0) {
			throw new IllegalArgumentException(
					"Please specify username to connect to CentraSite");
		}

		if (registryPassword == null || registryPassword.length() == 0) {
			throw new IllegalArgumentException(
					"Please specify password to connect to CentraSite");
		}
		if (centrasiteDBUrl == null || centrasiteDBUrl.length() == 0) {
			throw new IllegalArgumentException(
					"Please specify CentraSite Database URL to connect to CentraSite");
		}
		if (zipfile == null || zipfile.length() == 0) {
			throw new IllegalArgumentException(
					"Please specify zipfile to be uploaded to CentraSite repository");
		}
	}

	/**
	 * Gets the current bean pool
	 */
	private BeanPool getBeanPool() {
		return context.getCurrentBeanPool();
	}

	/**
	 * Create the connection and initialize all managers for the current
	 * example.
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void setConnection(boolean hasBrowserBahaviour)
			throws CSAppFrameworkException {
		// try {
		logger.info("Connecting to CentraSite...");
		// By the default localhost is used as URL to CentraSite
		provider = new StandaloneRegistryProvider(centrasiteDBUrl, registryUsername, registryPassword, hasBrowserBahaviour);

		Configuration conf = new Configuration();
		conf.setBeanMode(beanMode);
		conf.setRegistryProvider(provider);

		// Add the bean types to be used.
		conf.addBeanType(Item.class);
		conf.addBeanType(Action.class);
		conf.addBeanType(Entry.class);
		conf.addBeanType(ExternalLink.class);

		context = SessionContext.createInstance(conf);
		if (context == null) {
			throw new CSAppFrameworkException("Could not establish a session.");
		}
	}

	/**
	 * Checks if a type(for executing CSAFSample) exists in the JAXR registry.
	 * 
	 * @throws JAXRException
	 */
	@SuppressWarnings("finally")
	private boolean isModelExisting() throws JAXRException {
		// checks is there such a type into the repository
		logger.info("Checking for needed types...");
		TypeDescription typeDescription = null;
		TypeManager typeManager = this.context.getTypeManager();
		try {
			typeDescription = typeManager
					.getType(DEFAULT_REPOSITORY_SEARCHED_TYPE);
		} catch (CSAppFrameworkException e) {
			logger
					.error("Error while searching for an existing type into CentraSite repository");
		} finally {
			return (typeDescription != null) ? true : false;
		}
	}

	/**
	 * Parameter isToReplace sets if the existing model to be overwritten or
	 * not. If isToReplace is set to true the method overwrites the zip file. If
	 * isToReplace is set to false the method checkes if the model exists and if
	 * it exists logs a warning message, if not exists- imports it. Parameter
	 * isModelExisting is the result from isModelExisting method which specifies
	 * if the model needed for executing the sample is already imported.
	 * 
	 * @throws Exception
	 */

	private boolean doImport(boolean isToReplace, boolean isModelExisting)
			throws Exception {
		if (isModelExisting) {
			if (isToReplace) {
				logger.info("Replacing the needed model...");
				importZip(isToReplace);
				return true;
			} else {
				logger
						.info("CSAF SDK model(for executing CSAFSample) is already imported!");
				return false;
			}
		} else {
			importZip();
			return true;
		}
	}

	/**
	 * Method imports the zipFile, using centraSiteURL and CentraSite
	 * credentials. The option to replace the file is default set to false.
	 * 
	 * @throws Exception
	 */
	private void importZip() throws Exception {
		importZip(false);
	}

	/**
	 * Method imports the zipFile, using centraSiteURL and CentraSite
	 * credentials. An option to replace the already imported file is included.
	 * 
	 * @throws Exception
	 */
	private void importZip(boolean isToReplace) throws Exception {
		IEventCallback eventCallback = new BatchEventCallback();
		ImportOperation automaticImportOperation = null;
		logger.debug("Importing zip in CentraSite ... URL / ZIPFile");
		try {
			automaticImportOperation = new ImportOperation(
					this.centrasiteDBUrl, this.registryUsername,
					this.registryPassword, this.zipfile);
		} catch (Exception e) {
			logger.error("Failed to create ImportOperation", e);
			throw e;
		}

		try {
			automaticImportOperation.setEventCallback(eventCallback);
			automaticImportOperation.setReplace(isToReplace);
			automaticImportOperation.execute();
			logger.debug("Import done.");
		} catch (Exception e) {
			logger.error("Exception occurred while importing the zip ", e);
			throw e;
		}
	}

	/**
	 * Creates, reads, updates and deletes sample data.
	 * 
	 * @throws Exception
	 */
	private void run() throws Exception {
		try {
			boolean isExisting = isModelExisting();
			doImport(false, isExisting);
			logger.info("Creating types...");
			testTypeManagement();
			logger.info("Adding attributes to types...");
			testAttributeSupport();
			logger.info("Creating registry beans...");
			testBeanCreation();
			logger.info("Updating registry beans...");
			testReadAndUpdateBeans();
			logger.info("Managing the LifeCycle...");
			testLifeCycleManagement();
			logger.info("Searching registry beans...");
			testSearch();
			logger.info("Done.");
		}
		finally
		{
            if (deleteRegistryBeans) {
                logger.info("Deleting registry beans...");
                deleteRegistryBeans();
                logger.info("Deleting types...");
                testDelete();
            }
            logger.info("Done.");
            
			provider.getConnection().close();
		}
	}

	/**
	 * Manages the LifeCycle for a registry bean. Creates LifeCycle Model,
	 * LifeCicle States - create, update, delete. Sets a model to an existing
	 * registry bean.
	 * 
	 * @throws Exception
	 */
	private void testLifeCycleManagement() throws Exception {
		// create LifeCycle Management object
		LifeCycleManagementSample lcmSample = new LifeCycleManagementSample(
				this.context, this.rbwraId);
		lcmSample.run();
	}

	/**
	 * Search functionality - different types of search
	 * 
	 * @throws JAXRException
	 * @throws CSAppFrameworkException
	 */
	private void testSearch() throws CSAppFrameworkException, JAXRException {
		SearchSample searchSample = new SearchSample(this.context, this.rbwraId);
		searchSample.run();
	}

	/**
	 * Show how to use the TypeManager, create two different types - for adding
	 * relationship attribute and for adding classification attribute.
	 * 
	 * @throws CSAppFrameworkException
	 */

	private void testTypeManagement() throws CSAppFrameworkException {
		// gets TypeManager from session context
		TypeManager tm = context.getTypeManager();
		TypeDescription twra;
		TypeDescription twca;
		
		twra = tm.getType(TWRA_TYPE_NAME);
		if (twra == null)
		{
    		// create type that will contain relationship attribute
    		logger.info("Creating Type With Realationship Attribute...");
    
    		twra = tm.createType("TypeWithRelationshipAttribute",
    				"TypeWithRelationshipAttribute describtion",
    				TWRA_TYPE_LOCAL_NAME, TYPE_NAMESPACE);
            // save type that will contain relationship attribute
            tm.saveType(twra);
		}
		else
		{
		    logger.info("Type With Realationship Attribute already exists...");
		}

		twca = tm.getType(TWCA_TYPE_NAME);
		if (twca == null)
		{
    		// create type that will contain classification attribute
    		logger.info("Creating Type With Classification Attribute...");
    
    		twca = tm.createType("TypeWithClassificationAttribute",
    				"TWCA decription", TWCA_TYPE_LOCAL_NAME, TYPE_NAMESPACE);
            // save type that will contain classification attribute
            tm.saveType(twca);
		}
		else
		{
		    logger.info("Type With Classification Attribute already exists...");
		}
	}

	/**
	 * Gets an existing types - with relationship attribute and with
	 * classification attribute. Passing attribute name properly the
	 * relationship/classification attribute is set to an existing type. After
	 * adding the attributes, types are saved again.
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void testAttributeSupport() throws CSAppFrameworkException {
		// gets TypeManager from session context
		TypeManager tm = context.getTypeManager();
		TypeDescription twra;
		AttributeDescription relationshipAttribute;
		TypeDescription twca;
		AttributeDescription classificationAttribute;
		// gets already created type from the repository - type with
		// relationship attribute
		twra = tm.getType(TWRA_TYPE_NAME);
		// gets already created type from the repository - type with
		// classification attribute
		twca = tm.getType(TWCA_TYPE_NAME);
		
		if (twra.getAttributeByName("HasOutput") == null)
		{
    		logger.info("Adding attribute to Type With Relationship Attribute.");
    		// create relationship attribute
    		relationshipAttribute = tm.createRelationshipAttribute(
    				"HasOutput", "Attribute description",
    				HAS_OUTPUT_CONCEPT_PATH, Constants.OBJECT_TYPE_KEY_WSPolicy);
    		// adds relationship attribute to the that must contain relationship
    		// attribute
    		twra.addAttribute(relationshipAttribute);
    		// saves type with the relationship attribute to the repository
    		tm.saveType(twra);
		}
		else
		{
		    logger.info("Relationship Attribute already exists...");
		}
		
		if (twca.getAttributeByName("classifiedWith") == null)
		{
    		logger.info("Adding attribute to Type With Classification Attribute.");
    		// create classification attribute
    		classificationAttribute = tm.createClassificationAttribute(
    				"classifiedWith", "description",
    				Constants.CLASSIFICATION_SCHEME_PRODUCTS);
    		// adds relationship attribute to the that must contain classification
    		// attribute
    		twca.addAttribute(classificationAttribute);
    		// saves type with the relationship classification to the repository
    		tm.saveType(twca);
		}
		else
		{
		    logger.info("Classification Attribute already exists...");
		}

	}

	/**
	 * Create registry beans of existing types
	 * 
	 * @throws Exception
	 */
	private void testBeanCreation() throws Exception {
		// create registry bean with simple objects
		createRegistryBeans();
		// create registry bean with relationship attribute
		createRegistryBeanWithRelationshipAttribute();
		// create registry bean with classification attribute
		createRegistryBeanWithClassificationAttribute();
		// create registry bean with file attribute
		createRegistryBeanWithFileAttribute();
	}

	/**
	 * Creates sample model objects and persists them in the registry.
	 * 
	 * @throws Exception
	 */
	private void createRegistryBeans() throws Exception {
		logger.info("Creating registry bean of type Item...");

		/*
		 * Create Item object with references to other objects.
		 */
		Item itemObject = getBeanPool().create(Item.class);
		itemObject.setName("CRUDSample");

		// add Action representing (mapped as) JAXR association
		Action actionObject = getBeanPool().create(Action.class);
		actionObject.setRequestDate(Calendar.getInstance());
		itemObject.getActions().add(actionObject);

		// add Entry object representing (mapped as) JAXR classification
		Entry entryObject = getBeanPool().create(Entry.class);
		actionObject.getEntries().add(entryObject);

		// add EntryCode object representing (mapped as) JAXR concept
		EntryCode entryCode = getBeanPool().read(EntryCode.class,
				CONCEPT_UDDI_KEY);

		entryObject.setCode(entryCode);

		itemObjectId = itemObject.getKey().getId();

		getBeanPool().update(itemObject);
	}

	/**
	 * Update the already created registry beans:
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void testReadAndUpdateBeans() throws CSAppFrameworkException {
		// reads and updates simple registry bean
		readAndUpdateRegistryBeans();
		// reads and updates registry bean with file attribute
		readAndUpdateRegistryBeanWithRelationshipAttribute();
		// reads and updates registry bean with file attribute
		readAndUpdateRegistryBeanWithClassificationAttribute();
		// reads and updates registry bean with file attribute
		readAndUpdateRegistryBeanWithFileAttribute();
	}

	/**
	 * Deletes the already defined types: Type With Relationship Attribute and
	 * Type With Classification Attribute
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void testDelete() throws CSAppFrameworkException {
		// delete types created earlier
		logger.info("Deleting Type With Relationship Attribute...");
		// deletes type with relationship attribute
		deleteType(TWRA_TYPE_NAME);
		logger.info("Deleting Type With Classification Attribute...");
		// deltes type with classification attribute
		deleteType(TWCA_TYPE_NAME);
	}

	/**
	 * Creates a registry bean with file attribute
	 */
	private void createRegistryBeanWithFileAttribute() {
		logger.info("Creating Registry Bean With File Attribute...");
		// create registry bean from an existing type into the repository
		// and set values to its properties
		ExternalLink el = getBeanPool().create(ExternalLink.class);
		el.setName("EL");
		el.setExternalURI("http://www.google.com");

		// Creates a new bean instance of RBWFA class
		RBWFA rbwfa = getBeanPool().create(RBWFA.class);
		rbwfa.setName("Registry Bean With File Attribute");

		// adds the external link to the functional requirements property
		// to registry bean with file attribute
		Collection<ExternalLink> functionalRequirements = new ArrayList<ExternalLink>();
		functionalRequirements.add(el);
		rbwfa.setFunctionalRequirements(functionalRequirements);
		// updates the current registry bean
		getBeanPool().update(rbwfa);
		// gets the registry object key from the created RBWFA object
		rbwfaId = rbwfa.getKey().getId();
		// add the registry bean of type External and registry bean with file
		// attribute to a list.
		// elements from the list will be removed at the end of the program
		rbToDelete.add(el);
		rbToDelete.add(rbwfa);
	}

	/**
	 * Ceates a registry bean with classification attribute
	 */
	private void createRegistryBeanWithClassificationAttribute() {
		logger.info("Creating Registry Bean With Classification Attribute...");
		// Creates a new bean instance of RBWCA class
		RBWCA rbwca = getBeanPool().create(RBWCA.class);
		rbwca.setName("Registry Bean With Classification Attribute");

		// read for the registry a bean of specified type and having a given id
		Concept concept = getBeanPool().read(Concept.class,
				Constants.PRODUCTS_KEY_CentraSite);

		rbwca.setClassifiedWith(concept);

		// updates the current registry bean
		getBeanPool().update(rbwca);
		// gets the registry object key from the created RBWCA object
		rbwcaId = rbwca.getKey().getId();
	}

	/**
	 * Creates a registry bean with relationship attribute
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void createRegistryBeanWithRelationshipAttribute()
			throws CSAppFrameworkException {
		logger.info("Creating Registry Bean With Relationship Attribute...");
		// Creates a new bean instance of RBWRA class
		RBWRA rbwra = getBeanPool().create(RBWRA.class);

		// Creates a new bean instance of WSPolicy class. The WSPolicy is added
		// to an ArrayList. Adds relationship attribute of type WSPolicy,
		// described into RBWRA interface, implemented by RBWRAImpl. Into RBWRA
		// are defined the getter and setter type to the relationship attribute
		// and into RBWRAImpl these methods are implemented.

		WSPolicy wspolicy = getBeanPool().create(WSPolicy.class);
		Collection<WSPolicy> relationshipAttr = new ArrayList<WSPolicy>();
		wspolicy.setName("WSpolicy");
		wspolicy.setDescription("WSpolicy description");
		relationshipAttr.add(wspolicy);
		
		// set the relationship attribute to the registry bean
		rbwra.setRelationship(relationshipAttr);
		rbwra.setName("RBWRA Name");
		rbwra.setDescription("RBWRA Description");
		// updates the current registry bean
		getBeanPool().update(rbwra);
		// gets the registry object key from the created RBWRA object
		rbwraId = rbwra.getKey().getId();
		
		// add registry beans to a list which will be deleted at end of the
		// CSAFSample
		rbToDelete.add(rbwra);
		rbToDelete.add(wspolicy);
	}

	/**
	 * Search a concrete registry bean with predicate. Update its value and save
	 * to the repository.
	 * 
	 * @throws CSAppFrameworkException
	 */

	@SuppressWarnings("unchecked")
	private void readAndUpdateRegistryBeans() throws CSAppFrameworkException {
		logger.info("Updating registry beans of type Item...");

		// read all Items with name "CRUDSample"
		Search search = getBeanPool().createSearch(Item.class);
		search.addPredicate(Predicates.eq("name", "CRUDSample"));

		List<Item> items = (List<Item>) search.result();
		if (items != null) {
			for (Item i : items) {
				logger.debug("Items found: " + i);
			}
		}
		/*
		 * Read the created Item based on id.
		 */
		Item itemObject = getBeanPool().read(Item.class, itemObjectId);
		String newName = "CRUDSampleUpdated";
		itemObject.setName(newName);

		Action action = itemObject.getActions().get(0);
		action.setRequestDate(Calendar.getInstance());

		// reflect the changes in the registry
		getBeanPool().flush();
	}

	/**
	 * Reads and Updates concrete existing registry bean of Type With
	 * Relationship Attribute
	 */
	private void readAndUpdateRegistryBeanWithRelationshipAttribute() {
		// gets rbwra object from the registry
		logger.info("Updating Registry Bean With Relationship Attribute...");
		RBWRA rbwra = getBeanPool().read(RBWRA.class, rbwraId);
		// sets new description to the rbwra
		rbwra.setDescription("new RBWRA description");
		// sets new description to the relationship attribute
		WSPolicy wsp = getBeanPool().create(WSPolicy.class);
		wsp.setDescription("new Relationship Attribute type description");
		Collection<WSPolicy> wsps = new LinkedList<WSPolicy>();
		wsps.add(wsp);

		rbwra.setRelationship(wsps);
		// updates the current registry bean
		getBeanPool().update(rbwra);
		getBeanPool().flush();
	}

	/**
	 * Reads and Updates concrete existing registry bean of Type With
	 * Classification Attribute
	 */
	private void readAndUpdateRegistryBeanWithClassificationAttribute() {
		// gets rbwca object from the registry
		logger.info("Updating Registry Bean With Classification Attribute...");
		RBWCA rbwca = getBeanPool().read(RBWCA.class, rbwcaId);
		// sets new description to the rbwca
		rbwca.setDescription("new RBWRA description");
		// sets new description to the classification attribute
		Concept concept = getBeanPool().read(Concept.class,
				Constants.PRODUCTS_KEY_CentraSite);
		concept.setDescription("new Classification Attribute description");
		// add concept to already existing registry bean
		rbwca.setClassifiedWith(concept);
		// updates the current registry bean
		getBeanPool().flush();
	}

	/**
	 * Reads and Updates concrete existing registry bean of Type With File
	 * Attribute
	 */
	private void readAndUpdateRegistryBeanWithFileAttribute() {
		logger.info("Updating Registry Bean With File Attribute...");
		// gets rbwfa object from the registry
		RBWFA rbwfa = getBeanPool().read(RBWFA.class, rbwfaId);
		// sets new description to the rbwca
		rbwfa.setDescription("new RBWFA description");

		// add new value to the file attribute
		ExternalLink el = getBeanPool().create(ExternalLink.class);
		el.setExternalURI("http://www.eclipse.org");
		Collection<ExternalLink> els = new LinkedList<ExternalLink>();
		els.add(el);
		rbwfa.setFunctionalRequirements(els);

		// add registryu bean of externallink type to a list. Elements will be
		// removed from the list at the ened of the sample application
		rbToDelete.add(el);
		// updates the current registry bean
		getBeanPool().flush();
	}

	/**
	 * Delete all registry beans used into the example
	 */
	private void deleteRegistryBeans() {
		for (RegistryBean currentRB : rbToDelete) {
			if (currentRB != null) {
				logger.info("Delete: " + currentRB);
				getBeanPool().delete(currentRB);
			}
		}
	}

	/**
	 * Deletes a type into the registry from a given type name
	 * 
	 * @param typeName
	 *            Name of the type into the registry to be deleted
	 * @throws CSAppFrameworkException
	 */
	private void deleteType(String typeName) throws CSAppFrameworkException {
		TypeManager tm = context.getTypeManager();
		tm.deleteType(tm.getType(typeName));
	}
}
