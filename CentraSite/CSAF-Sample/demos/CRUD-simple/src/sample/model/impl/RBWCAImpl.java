package sample.model.impl;

import sample.model.RBWCA;

import com.softwareag.centrasite.appl.framework.beans.DynamicRegistryBean;
import com.softwareag.centrasite.appl.framework.beans.standard.Concept;


/**
 * Implementation of the {@link RBWCA} bean interface.
 */

public class RBWCAImpl extends DynamicRegistryBean implements RBWCA {

	Concept concept;

	public Concept getClassifiedWith() {
		return concept;
	}

	public void setClassifiedWith(Concept concept) {
		this.concept = concept;
	}

}
