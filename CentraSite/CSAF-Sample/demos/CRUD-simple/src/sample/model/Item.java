package sample.model;

import java.util.List;

import sample.model.impl.ItemImpl;

import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.mapping.CascadeStyle;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Association;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Slot;

/**
 * Java bean interface representing JAXR registry objects of type  {http://namespaces.CentraSite.com/csaf}Item.
 */
@RegistryObject(objectTypeName = "{http://namespaces.CentraSite.com/csaf}Item")
@Bean(implementation = ItemImpl.class)
public interface Item extends RegistryBean{
    
   
	/**
     * Returns the short name of the item. Maps to {http://namespaces.CentraSite.com/csaf}shortName slot. 
     */
    @Slot(name = "{http://namespaces.CentraSite.com/csaf}shortName")
    String getShortName();
    
    /**
     * Sets the short name property of the item.
     */
    void setShortName(String shortName);
    
    /**
     * Returns.
     */
    @Association(type = "HasAction", targetType = Action.class, cascadeStype = CascadeStyle.DELETE)
    List<Action> getActions();
    
    /**
     * @param list
     */
    public void setActions(List<Action> list);
}
