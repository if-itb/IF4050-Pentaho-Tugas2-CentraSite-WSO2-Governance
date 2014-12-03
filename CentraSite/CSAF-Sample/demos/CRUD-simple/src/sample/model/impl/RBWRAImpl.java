package sample.model.impl;

import java.util.Collection;

import sample.model.RBWRA;
import sample.model.WSPolicy;

import com.softwareag.centrasite.appl.framework.lcm.beans.LCAwareDynamicRegistryBean;

/**
 * Implementation of the {@link RBWRA} bean interface.
 */

public class RBWRAImpl extends LCAwareDynamicRegistryBean implements RBWRA {
	private Collection<WSPolicy> relationship;

	public Collection<WSPolicy> getRelationship() {
		return relationship;
	}

	public void setRelationship(Collection<WSPolicy> relationship) {
		this.relationship = relationship;
	}

}
