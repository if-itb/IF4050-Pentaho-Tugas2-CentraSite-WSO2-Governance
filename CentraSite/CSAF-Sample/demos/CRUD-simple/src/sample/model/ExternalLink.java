package sample.model;

import sample.model.impl.ExternalLinkImpl;

import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Property;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;

/**
 * The class represent a JAXR External link.
 *
 */
@RegistryObject(objectTypeKey = Constants.OBJECT_TYPE_KEY_ExternalLink)
@Bean(implementation = ExternalLinkImpl.class)
public interface ExternalLink extends RegistryBean {
	/**
	 * Returns the external links URI.
	 */
	@Property (target ="externalURI")
	public String getUri();

    /**
     * Sets the external links URI.
     */
	public void setUri(String uri);
}
