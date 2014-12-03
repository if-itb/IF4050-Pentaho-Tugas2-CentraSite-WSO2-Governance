package sample.model;

import sample.CSAFSample;

import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.beans.standard.Concept;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.ClassificationAttribute;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;

/**
 * @author bgepe
 * 
 *         Java bean interface representing JAXR registry objects TWCA (Type
 *         With Classification Attribute).
 */

@RegistryObject(objectTypeName = CSAFSample.TWCA_TYPE_NAME)
@Bean(implementationClass = "sample.model.impl.RBWCAImpl")
public interface RBWCA extends RegistryBean {
	// Registry Bean With Classification Attribute

	@ClassificationAttribute(attributeName = "classifiedWith", targetType = Concept.class)
	public Concept getClassifiedWith();

	public void setClassifiedWith(Concept service);

}
