package sample.model.impl;

import java.util.List;

import sample.model.Action;
import sample.model.Item;

import com.softwareag.centrasite.appl.framework.beans.DynamicRegistryBean;

/**
 * Implementation of the {@link Item} bean interface.
 */
public class ItemImpl extends DynamicRegistryBean implements
		Item {

	private String shortName;
	private List<Action> actions;

	/**
	 * {@inheritDoc}
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> list) {
		actions = list;
	}

}
