package sample.model;

import sample.model.impl.Action1Impl;

import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.ClassifiedInstanceProperty;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Property;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Slot;
/**
 * Interface for action object
 *
 */
@RegistryObject(objectTypeName = "{http://namespaces.CentraSite.com/csaf}Action")
@Bean(implementation = Action1Impl.class)
public interface Action1 extends Action{
	
	@Property(target = "name")
    String getName();
    
    /**
     * Returns the registry objects description.
     */
    @Property(target="description")
    String getDescription();
    
    /**
     * 
     * @return String
     */
    @ClassifiedInstanceProperty
    @Slot(name = "{http://namespaces.CentraSite.com/csaf}customerProject")
    String getCustomerProject();
    
    /**
     * 
     * @param pr
     */
    void setCustomerProject(String pr);
}
