package sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.registry.JAXRException;

import org.apache.log4j.Logger;

import sample.model.Action;
import sample.model.Item;

import com.softwareag.centrasite.appl.framework.CSAppFrameworkException;
import com.softwareag.centrasite.appl.framework.Configuration;
import com.softwareag.centrasite.appl.framework.SessionContext;
import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.RevisionBean;
import com.softwareag.centrasite.appl.framework.persistence.BeanPool;
import com.softwareag.centrasite.appl.framework.persistence.RegistryProvider;
import com.softwareag.centrasite.appl.framework.persistence.impl.StandaloneRegistryProvider;
import com.softwareag.centrasite.appl.framework.persistence.revision.RevisionManager;
import com.softwareag.centrasite.appl.framework.persistence.search.Search;

/**
 * Main program demonstrating basic Revision Managing operations how could be
 * used with the CentraSite Application Framework.
 * 
 * @author <a href="mailto:Mario.Radev@softwareag.com">Mario Radev</a>
 * 
 */
public class RevisionManagementSample {
	/**
	 * the uddi key of the created Item
	 */
	private String itemId;
	/**
	 * Provides connection to Database
	 */
	private RegistryProvider provider;
	/**
	 * if it is set to true all created registry beans will be deleted
	 */
	private boolean deleteRegistryBeans;
	/**
	 * Username to connect to CentraSite Database
	 */
	private String registryUsername;
	/**
	 * Password to connect to CentraSite Database
	 */
	private String registryPassword;
	/**
	 * Provide session
	 */
	private SessionContext context;

	/**
	 * Manages the revision
	 */
	private RevisionManager revisionManager;
	/**
	 * Checkpoint name used in the revision
	 */
	private static String CHECKPOINT_NAME = "MyCheckPoint";
	/**
	 * Stores the registry beans that will be deleted if deleteRegistryBeans's
	 * value is true
	 */
	private Collection<RegistryBean> rbToDelete = new LinkedList<RegistryBean>();
	/**
	 * logs messages
	 */
	private static Logger logger = Logger
			.getLogger(RevisionManagementSample.class);

	/**
	 * get the current BeanPool
	 */
	private BeanPool getBeanPool() {
		return context.getCurrentBeanPool();
	}

	/**
	 * Sets a connection to a database and initialize all managers used into
	 * this sample. Parameter hasBrowserBehaviour must be false to enable
	 * RevsionManager.
	 */
	private void setConnection(boolean hasBrowserBahaviour)
			throws CSAppFrameworkException {
		try {
			logger.info("Connecting to CentraSite...");
			// By the default localhost is used as URL to CentraSite
			provider = new StandaloneRegistryProvider(registryUsername,
					registryPassword, hasBrowserBahaviour);

			Configuration conf = new Configuration();
			conf.setRegistryProvider(provider);

			// Add the bean types to be used.
			conf.addBeanType(Item.class);
			conf.addBeanType(Action.class);

			context = SessionContext.createInstance(conf);

		} catch (CSAppFrameworkException e) {
			throw new CSAppFrameworkException(
					"Could not establish connection to CentraSite.", e);
		}
	}

	/**
	 * Parses the arguments starting the application. Sets the connection.
	 * 
	 * @param args
	 * @throws CSAppFrameworkException
	 */
	private void init(String[] args) throws CSAppFrameworkException {
		// method parses arguments starting the applicaton
		parseArguments(args);
		// method sets the connection to the repository and initializes all
		// needed managers
		this.setConnection(false);
	}

	/**
	 *Executes sequently revision management functionality
	 * 
	 * @throws JAXRException
	 * @throws CSAppFrameworkException
	 */
	private void run() throws JAXRException, CSAppFrameworkException {

		try {
			this.createRegistryBean();
			this.testRevisioning();
			if (deleteRegistryBeans) {
				this.deleteRB();
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			provider.getConnection().close();
		}
	}

	/**
	 * Parses arguments
	 * 
	 * @param args
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
	}

	/**
	 * Checks is revisinoning enabled. Sets Checkpoint to a registry bean.
	 * Restores the registry bean. Delete all revision beans flagged with the
	 * checkpoint name.
	 * 
	 * @throws JAXRException
	 */
	private void testRevisioning() throws JAXRException {
		// connection could use revisioning

		try {
			revisionManager.enableRevisioning();
		} catch (CSAppFrameworkException e) {
			logger.error("Could not enable revisioning", e);
			e.printStackTrace();
		}
		if (revisionManager.isRevisioningEnabled()) {
			// set a checkpoint to registry bean
			setCheckpointToRB();
			// restore registry beans using Revision Manager
			restoreRB();

			// delete all revision beans flagged with the checkpoint name.
			try {
				revisionManager.deleteBeans(CHECKPOINT_NAME);

			} catch (Exception e) {
				logger.error(
						"Could not delete registry beans from checkpoint.", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a simple registry bean of Item type.
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void createRegistryBean() throws CSAppFrameworkException {
		// Creates a new registry bean of Item type
		Item item = getBeanPool().create(Item.class);
		// Creates a new registry bean of Action type
		Action action = getBeanPool().create(Action.class);
		List<Action> actions = new LinkedList<Action>();
		action.setName("Action name");
		action.setDescription("Action description");
		actions.add(action);
		// set the current registry bean action to the item registry bean
		item.setActions(actions);
		item.setName("Item Name");
		item.setDescription("Item Description");
		// updates the item registry bean
		getBeanPool().update(item);

	}

	/**
	 * Restore an existing registry bean.
	 */
	private void restoreRB() {
		Item item = getBeanPool().read(Item.class, itemId);
		try {
			Collection<RevisionBean> revs = revisionManager
					.getRevisionBeans(item);

			Collection<RevisionBean> restoreRBs = new ArrayList<RevisionBean>();
			for (RevisionBean rev : revs) {
				if (rev.isRevision()) {
					restoreRBs.add(rev);
				}
			}
			revisionManager.restoreBeans(restoreRBs);
		} catch (CSAppFrameworkException e) {
			logger.error("Could not restore registry beans.", e);
			e.printStackTrace();
		}
	}

	/**
	 * Sets a checkpoint to a registry bean
	 */
	private void setCheckpointToRB() {
		// reads registry bean from the repository
		Item item = getBeanPool().read(Item.class, itemId);
		// set revision manager checkpoint to a registry bean
		try {
			revisionManager.setCheckpoint(item, CHECKPOINT_NAME);
		} catch (CSAppFrameworkException e) {
			logger.error("Could not set checkpoint.", e);
			e.printStackTrace();

		}
	}

	/**
	 * Deletes the existing registry beans of Action type and Item type
	 * 
	 * @throws CSAppFrameworkException
	 * @throws JAXRException
	 */
	@SuppressWarnings("unchecked")
	private void deleteRB() throws CSAppFrameworkException, JAXRException {

		// search all registry bean of type Item and get their uddi keys
		Search searchActions = getBeanPool().createSearch(Action.class);
		List<Action> existingActions = (List<Action>) searchActions.result();
		if (existingActions != null) {
			rbToDelete.addAll(existingActions);
		}

		// search all registry bean of type Item and get their uddi keys
		Search searchItems = getBeanPool().createSearch(Item.class);
		List<Item> existingItems = (List<Item>) searchItems.result();
		if (existingItems != null) {
			rbToDelete.addAll(existingItems);
		}
		getBeanPool().delete(rbToDelete);

	}

	/**
	 * Starts the revision management sample
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RevisionManagementSample revisionManagementSample = new RevisionManagementSample();
		// initialize the persistence framework
		revisionManagementSample.init(args);
		// run the sample
		revisionManagementSample.run();
	}
}