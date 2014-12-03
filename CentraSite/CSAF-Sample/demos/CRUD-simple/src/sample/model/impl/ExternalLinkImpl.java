package sample.model.impl;

import sample.model.ExternalLink;

import com.softwareag.centrasite.appl.framework.beans.DynamicRegistryBean;

/**
 * Default implementation.
 * 
 * @author <a href="mailto:Dragomir.Nikolov@softwareag.com">Dragomir Nikolov</a>
 * 
 */
public class ExternalLinkImpl extends DynamicRegistryBean implements
		ExternalLink {

	private String uri;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softwareag.centrasite.appl.framework.persistence.beanmodel.ExternalLink#getUri()
	 */
	public String getUri() {
		return this.uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softwareag.centrasite.appl.framework.persistence.beanmodel.ExternalLink#setUri(java.lang.String)
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
