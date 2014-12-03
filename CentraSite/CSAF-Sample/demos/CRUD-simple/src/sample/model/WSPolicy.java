package sample.model;

import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;

/**
 * Java bean interface representing JAXR registry objects of type  WSPolicy.
 */
@RegistryObject(objectTypeKey = Constants.OBJECT_TYPE_KEY_WSPolicy)
@Bean(implementationClass = "sample.model.impl.WSPolicyImpl")
public interface WSPolicy extends RegistryBean{
    
    

}
