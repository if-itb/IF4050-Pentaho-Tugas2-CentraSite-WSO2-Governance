package sample.model;

import sample.model.impl.Action2Impl;

import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;
/**
 * Interface for action object
 *
 */
@RegistryObject(objectTypeName = "{http://namespaces.CentraSite.com/csaf}Action")
@Bean(implementation = Action2Impl.class)
public interface Action2 extends Action{
}
