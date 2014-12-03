package sample.model;

import java.util.Collection;

import sample.CSAFSample;

import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.lcm.beans.LifeCycleAware;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Relationship;

/**
 * Java bean interface representing JAXR registry objects of TWRA. Maps an
 * existing RO to RB.
 */

@RegistryObject(objectTypeName = CSAFSample.TWRA_TYPE_NAME)
@Bean(implementationClass = "sample.model.impl.RBWRAImpl")
public interface RBWRA extends LifeCycleAware {

	/*
	 * Maps attribute to a RB type
	 */
	@Relationship(attributeName = "HasOutput", targetTypes = WSPolicy.class)
	public Collection<WSPolicy> getRelationship();

	public void setRelationship(Collection<WSPolicy> relationship);

}
