package sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.registry.JAXRException;

import org.apache.log4j.Logger;

import sample.model.RBWRA;

import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.CSAppFrameworkException;
import com.softwareag.centrasite.appl.framework.SessionContext;
import com.softwareag.centrasite.appl.framework.beans.standard.Organization;
import com.softwareag.centrasite.appl.framework.lcm.LCMAdminManager;
import com.softwareag.centrasite.appl.framework.lcm.LCModel;
import com.softwareag.centrasite.appl.framework.lcm.LCState;
import com.softwareag.centrasite.appl.framework.persistence.BeanPool;

/**
 * Modal class communicating with CSAFSample main program Explains how could be
 * used Life Cycle Management functionality into CentraSite Application
 * Framework
 * 
 * @author <a href="mailto:Mario.Radev@softwareag.com">Mario Radev</a>
 * 
 */
public class LifeCycleManagementSample {
	private static final String LCMODEL_NEW_DISPALY_NAME = "NEW LCMODEL NAME";
    /**
	 * Represents messages for this class
	 */
	private static Logger logger = Logger
			.getLogger(LifeCycleManagementSample.class);
	/**
	 * LifeCycle Model Admin Manager for managing LifeCycle Model
	 */
	private LCMAdminManager lcam;
	/**
	 * Sets LifeCycle Model to a registry bean with relationship attribute
	 */
	private String lcModelKey;
	/**
	 * the description of the life cycle model
	 */
	private final static String LC_MODEL_DESCRIPTION = "LC Model Description";
	/**
	 * the display name of the life cycle model
	 */
	private final static String LC_MODEL_DISPLAY_NAME = "LC Model Display name";
	/**
	 * the name of the first life cycle state
	 */
	private final static String LC_STATE_1_NAME = "LC State1 Name";
	/**
	 * the description of the first life cycle state
	 */
	private final static String LC_STATE_1_DESCRIPTION = "LC State1 Description";
	/**
	 * the name of the second life cycle state
	 */
	private final static String LC_STATE_2_NAME = "LC State2 Name";
	/**
	 * the description of the second life cycle state
	 */
	private final static String LC_STATE_2_DESCRIPTION = "LC State2 Description";
	private final SessionContext context;
	private String rbwraId;

	/**
	 * Sets LifeCycle Model to a registry bean with relationship attribute
	 */

	public LifeCycleManagementSample(SessionContext context, String rbwraId) {
		this.context = context;
		this.rbwraId = rbwraId;
	}

	/**
	 * Gets the bean pool from the CSAFSample
	 * 
	 * @return
	 */
	private BeanPool getBeanPool() {
		return context.getCurrentBeanPool();
	}

	/**
	 * Execute sequently methods for managing the life cycle
	 * 
	 * @throws Exception
	 */
	protected void run() throws Exception {
		// sets a life cycle model to an existing registry bean
		setLCModelToRB();
		// updates the current life cycle model
		updateLCModel();
		// changes the life cycle state
		changeLCState();
		// find LCState by name
		findLCModel();
		// deletes used life cycle state
		deleteLCState();
	}

	private void findLCModel() throws CSAppFrameworkException {
	    this.lcam = context.getLCMAdminManager();
	    logger.info("Finding Life Cycle Model.");
	    List<LCModel> result = lcam.findLCModelByDisplayName(LCMODEL_NEW_DISPALY_NAME,false);
	    if(! (result.size() ==1)){
	        logger.error("There should be one model with that name");
	    }else{
	        logger.info("Model with name -" + result.get(0).getDisplayName() + "- exists");
	    }
        
    }

    /**
	 * Sets a life cycle model to a registry bean
	 * 
	 * @throws CSAppFrameworkException
	 * @throws JAXRException
	 */
	private void setLCModelToRB() throws CSAppFrameworkException, JAXRException {
		logger.info("Setting lifecycle model to an existing registry bean.");
		LCModel lcModel = createLCModel();
		// gets an already existing registry bean with relationship attribute
		RBWRA rbwra = getBeanPool().read(RBWRA.class, rbwraId);
		// sets the initial state from the created life cycle model
		rbwra.setLCState(lcModel.getInitialState());
		// gets thte key of the liffe cycle model
		lcModelKey = lcModel.getKey();
		// update the data to the repository
		getBeanPool().flush();
	}

	/**
	 * Creates a life cycle model. Add states to the model. Initialize the
	 * states and the sequence of the states. Saves the model.
	 * 
	 * @return
	 * @throws CSAppFrameworkException
	 * @throws JAXRException
	 */
	private LCModel createLCModel() throws CSAppFrameworkException,
			JAXRException {

		// creates an LifeCycle Model
		this.lcam = context.getLCMAdminManager();
		LCModel lcModel = lcam.createLCModel();
		lcModel.setDisplayName(LC_MODEL_DISPLAY_NAME);
		lcModel.setDescription(LC_MODEL_DESCRIPTION);
		// sets the model to an existing organization
		Organization organization = getBeanPool().read(Organization.class,
				Constants.DEFAULT_ORGANIZATION_KEY);
		lcModel.setOrganization(organization, false);

		// Create and add LifeCycle States
		LCState lcState1 = lcModel.createLCState();
		lcState1.setName(LC_STATE_1_NAME);
		lcState1.setDescription(LC_STATE_1_DESCRIPTION);

		LCState lcState2 = lcModel.createLCState();
		lcState2.setName(LC_STATE_2_NAME);
		lcState2.setDescription(LC_STATE_2_DESCRIPTION);

		Collection<LCState> states = new ArrayList<LCState>();
		states.add(lcState1);
		states.add(lcState2);
		// sets LifeCycle Model states
		lcModel.addStates(states);
		// sets LifeCycle Model an initial state
		lcModel.setInitialState(lcState1);
		// sets next state to an existing initial state
		Collection<LCState> nextStates = new LinkedList<LCState>();
		nextStates.add(lcState2);
		lcState1.addNextStates(nextStates);

		Collection<String> typesToBeEnabledForLCM = new ArrayList<String>();
		// types that will be enabled for LCM
		typesToBeEnabledForLCM.add(Constants.OBJECT_TYPE_KEY_Service);
		lcModel.addEnabledTypes(typesToBeEnabledForLCM);
		// saves the LifeCycle model to LifeCycle Admin Manager
		lcam.saveLCModel(lcModel);
		return lcModel;
	}

	/**
	 * Gets an existing life cycle model. Changes some of its value and saves
	 * it.
	 * 
	 * @throws Exception
	 */
	public void updateLCModel() throws Exception {
		logger.info("Update the lifecycle model.");
		// gets lcm by key
		LCModel lcm = lcam.getLCModel(lcModelKey);
		// set display name
		lcm.setDisplayName(LCMODEL_NEW_DISPALY_NAME);
		// save the life cycle model
		lcam.saveLCModel(lcm);
	}

	/**
	 * Gets an existing life cycle state from a life cycle model. Sets a new
	 * state to a registry bean with relationship attribute.
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void changeLCState() throws CSAppFrameworkException {
		logger.info("Change lifecycle state to the model.");
		// find a LCM by key
		LCModel lcm = lcam.getLCModel(lcModelKey);
		// read an existing registry bean with relationship attribute
		RBWRA rbwra = getBeanPool().read(RBWRA.class, rbwraId);
		// change existing state
		LCState changingLCState = null;
		// gets all existing states from life cycle model
		Collection<LCState> lcStates = lcm.getStates();
		for (LCState currentState : lcStates) {
			// set changingLCState with a new state due to a given value
			if (currentState.getName().equals(LC_STATE_2_NAME)) {
				changingLCState = currentState;
			}
		}
		// set new state to a registry bean
		rbwra.setLCState(changingLCState);
	}

	/**
	 * Deletes a life cycle state
	 * 
	 * @throws CSAppFrameworkException
	 */
	private void deleteLCState() throws CSAppFrameworkException {
		logger.info("Delete the lifecycle state.");
		LCModel lcm = lcam.getLCModel(lcModelKey);
		Collection<LCState> lcStates = lcm.getStates();
		if (lcStates != null) {
			for (LCState currentLCState : lcStates) {
				lcm.removeState(currentLCState);
			}
		}
	}
}