package sample;

import java.util.LinkedList;
import java.util.List;

import javax.xml.registry.JAXRException;

import org.apache.log4j.Logger;

import sample.model.Action;
import sample.model.Item;
import sample.model.RBWCA;
import sample.model.RBWRA;

import com.softwareag.centrasite.appl.framework.CSAppFrameworkException;
import com.softwareag.centrasite.appl.framework.SessionContext;
import com.softwareag.centrasite.appl.framework.persistence.BeanPool;
import com.softwareag.centrasite.appl.framework.persistence.search.Predicates;
import com.softwareag.centrasite.appl.framework.persistence.search.Search;

/**
 * Modal class communicating with CRUDSample main program Explains how could be
 * used Search functionality into CentraSite Application Framework
 * 
 * @author <a href="mailto:Mario.Radev@softwareag.com">Mario Radev</a>
 * 
 */
public class SearchSample {

	private static Logger logger = Logger.getLogger(SearchSample.class);

	private SessionContext context;

	private String rbwraId;

	/**
	 * Search for this predicate
	 */
	private final static String PREDICATE_SEARCH = "name";
	/**
	 * Search for this nested predicate
	 */
	private final static String NESTED_PROPERTY_SEARCH = "actions.description";
	/**
	 * Item name used
	 */
	private final static String ITEM_NAME = "Item name";
	/**
	 * Item description used
	 */
	private final static String ITEM_DESCRIPTION = "Item description";
	/**
	 * Action description used for nested predicate search
	 */
	private final static String ACTION_DESCRIPTION = "Action description";

	/**
	 * s Constructor recieves SessionContext and registry bean with relationship
	 * attribute ID
	 * 
	 * @param context
	 * @param rbwraId
	 */
	public SearchSample(SessionContext context, String rbwraId) {
		this.context = context;
		this.rbwraId = rbwraId;

	}

	/**
	 * Gets the bean pool from CSAFSample application
	 */
	private BeanPool getBeanPool() {
		return context.getCurrentBeanPool();
	}

	/**
	 * Execute sequently some search functionality
	 * 
	 * @throws CSAppFrameworkException
	 * @throws JAXRException
	 */
	protected void run() throws CSAppFrameworkException, JAXRException {
		// finds using predicate
		predicateSearch();
		// searches by a nested property
		nestedPropertySearch();
		// find registry bean beanpool.search - RBs keys to delete
		removeKeysSearch();
	}

	/**
	 * Searches registry bean using some predicate
	 * 
	 * @throws CSAppFrameworkException
	 */
	@SuppressWarnings("unchecked")
	void predicateSearch() throws CSAppFrameworkException {
		logger.info("Searching by a predicate.");
		// reads an existing registry bean rom its id
		RBWRA rbwra = getBeanPool().read(RBWRA.class, rbwraId);

		// search all registry beans with relationship attribute
		Search searchRBWRA = getBeanPool().createSearch(RBWRA.class);
		// search concrete registry bean with a description
		searchRBWRA.addPredicate(Predicates.eq(PREDICATE_SEARCH, rbwra
				.getName()));

		// result of the search
		List<RBWRA> resultRBWRA = (List<RBWRA>) searchRBWRA.result();
		// details about the found registry bean
		if (resultRBWRA.size() != 0) {
			logger.info("Total found: " + resultRBWRA.size());
			for (RBWRA currentRbwra : resultRBWRA) {
				logger.info("RBWRA's name: " + currentRbwra.getName());
				logger.info("RBWRA's description: "
						+ currentRbwra.getDescription());
			}
		} else {
			logger
					.error("No Registry Bean With Relationship Attribute found into database.");
		}
	}

	/**
	 * Searches a nested property
	 * 
	 * @throws CSAppFrameworkException
	 */
	@SuppressWarnings("unchecked")
	void nestedPropertySearch() throws CSAppFrameworkException {
		logger.info("Searching by a nested property...");

		// creates new registry bean of Item type
		Item item = getBeanPool().create(Item.class);
		// creates new registry bean of Action type
		Action action = getBeanPool().create(Action.class);
		action.setDescription(ACTION_DESCRIPTION);
		List<Action> actionsList = new LinkedList<Action>();
		actionsList.add(action);
		// adds the current action to the current item
		item.setActions(actionsList);
		item.setName(ITEM_NAME);
		item.setDescription(ITEM_DESCRIPTION);

		getBeanPool().flush();
		// search all existing items
		Search searchItems = getBeanPool().createSearch(Item.class);
		// a condition which of the existing items to be resulted
		// condition is nested because the search of the property is nested
		searchItems.addPredicate(Predicates.eq(NESTED_PROPERTY_SEARCH, action
				.getDescription()));
		// get the result of the set upper condition
		List<Item> resultItem = (List<Item>) searchItems.result();
		if (resultItem.size() != 0) {
			logger.info("Total found:" + resultItem.size());
			for (Item currentItem : resultItem) {
				logger.info("Item's name: " + currentItem.getName());
				logger.info("Item's description: "
						+ currentItem.getDescription());
			}
		} else {
			logger.error("No Item registry bean found into database");
		}
	}

	/**
	 * gets all used keys into the CRUDSample which to delete
	 * 
	 * @throws CSAppFrameworkException
	 * @throws JAXRException
	 */
	void removeKeysSearch() throws CSAppFrameworkException, JAXRException {
		logger.info("Search all created registy objects:");
		// search all registry beans of type Item
		searchAllAnotherKeys();
		// search all registry beans with relationship attributes keys and their
		// attribute's uddi keys
		searchRBWRAKeys();
		// search all registry beans with classifcation attributes keys and
		// their attribute's uddi keys
		searchRBWCAKeys();
		// search all registry beans with file attributes keys and their
		// attribute's uddi keys
	}

	/**
	 * Search for existing registry beans of type Item
	 * 
	 * @throws CSAppFrameworkException
	 * @throws JAXRException
	 */
	@SuppressWarnings("unchecked")
	private void searchAllAnotherKeys() throws CSAppFrameworkException,
			JAXRException {
		logger.info("Searching for registry beans of type Item.");

		// search for all Entry registry beans uddi keys
		Search searchItem = getBeanPool().createSearch(Item.class);
		List<Item> existingItem = (List<Item>) searchItem.result();
		if (existingItem != null) {
			CSAFSample.rbToDelete.addAll(existingItem);
		}
	}

	/**
	 * Search registry beans with classification attribute
	 * 
	 * @throws CSAppFrameworkException
	 */
	@SuppressWarnings("unchecked")
	private void searchRBWCAKeys() throws CSAppFrameworkException,
			JAXRException {
		logger
				.info("Searching for Registry Beans With Classification Attribute...");
		// search all registry beans with classification attributes
		Search searchRBWCA = getBeanPool().createSearch(RBWCA.class);
		List<RBWCA> existingRBWCA = (List<RBWCA>) searchRBWCA.result();
		if (existingRBWCA != null) {
			CSAFSample.rbToDelete.addAll(existingRBWCA);
		}
	}

	/**
	 * Search registry beans with relationship attribute
	 * 
	 * @throws CSAppFrameworkException
	 */
	@SuppressWarnings("unchecked")
	private void searchRBWRAKeys() throws CSAppFrameworkException,
			JAXRException {
		// search all registry beans with relationship attributes
		Search searchRBWRA = getBeanPool().createSearch(RBWRA.class);
		List<RBWRA> existingRBWRA = (List<RBWRA>) searchRBWRA.result();
		if (existingRBWRA != null) {
			CSAFSample.rbToDelete.addAll(existingRBWRA);
		}

	}
}