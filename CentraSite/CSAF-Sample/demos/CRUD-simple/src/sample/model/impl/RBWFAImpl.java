package sample.model.impl;

import java.util.Collection;

import sample.model.RBWFA;

import com.softwareag.centrasite.appl.framework.beans.standard.ExternalLink;
import com.softwareag.centrasite.appl.framework.beans.standard.impl.ServiceImpl;

/**
 * Implementation of the {@link RBWFA} bean interface.
 */

public class RBWFAImpl extends ServiceImpl implements RBWFA {

	Collection<ExternalLink> functionalRequirements;

	public Collection<ExternalLink> getFunctionalRequirements() {
		return functionalRequirements;
	}

	public void setFunctionalRequirements(
			Collection<ExternalLink> functionalRequirements) {
		this.functionalRequirements = functionalRequirements;
	}

}
