package sample.model;

import java.util.Collection;

import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.beans.standard.ExternalLink;
import com.softwareag.centrasite.appl.framework.beans.standard.Service;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.FileAttribute;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;

/**
 * Java bean interface representing JAXR registry objects of type
 * {http://sample.namespace.sample}TWFA. Mapes an existing RO to RB
 */
@RegistryObject(objectTypeKey = Constants.OBJECT_TYPE_KEY_Service)
@Bean(implementationClass = "sample.model.impl.RBWFAImpl")
public interface RBWFA extends Service {

	/*
	 * Maps file attribute to a RB type
	 */
	@FileAttribute(attributeName = "Functional-Requirements", targetType = ExternalLink.class)
	public Collection<ExternalLink> getFunctionalRequirements();

	public void setFunctionalRequirements(
			Collection<ExternalLink> functionalRequirements);

}
