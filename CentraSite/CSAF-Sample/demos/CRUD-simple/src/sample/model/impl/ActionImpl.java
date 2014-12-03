/**
 * 
 */
package sample.model.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sample.model.Action;
import sample.model.Entry;
import sample.model.ExternalLink;

import com.softwareag.centrasite.appl.framework.beans.DynamicRegistryBean;
import com.softwareag.centrasite.appl.framework.beans.standard.Association;
import com.softwareag.centrasite.appl.framework.beans.standard.Concept;
import com.softwareag.centrasite.appl.framework.beans.standard.Organization;

/**
 * Default implementation for {@link Action}
 * 
 * @author bgmpa
 * 
 */
public class ActionImpl extends DynamicRegistryBean implements Action {

	private Calendar reviewDate;
	private Calendar requestDate;
	private long timestamp;
	private Boolean acceptedByDesigner;
	private DecisionType decisionType;
	private String[] recommendations, references;
	private List<Entry> entries = new ArrayList<Entry>();
	private String description;
	private List<ExternalLink> attachments;
	private List<Organization> submittingOrganizations;

	private List<Association> relatedToAssociations;
	private Concept category;

	/**
	 * 
	 * 
	 */
	public static class DecisionTypeImpl implements DecisionType {
		private String description;

		public String getDecision() {
			return description;
		}
	}

	/**
	 * 
	 */
	public Calendar getReviewDate() {
		return reviewDate;
	}

	/**
	 * 
	 */
	public void setReviewDate(Calendar reviewDate) {
		this.reviewDate = reviewDate;
	}

	/**
	 * 
	 */
	public void setRequestDate(Calendar requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * 
	 */
	public DecisionType getDecisionType() {
		return decisionType;
	}

	/**
	 * 
	 */
	public void setDecisionType(DecisionType pDecisionType) {
		decisionType = pDecisionType;
	}

	/**
	 * 
	 */
	public String[] getRecommendations() {
		return recommendations;
	}

	/**
	 * 
	 */
	public void setRecommendations(String[] pRecommendations) {
		recommendations = pRecommendations;
	}

	/**
	 * 
	 */
	public String[] getReferences() {
		return references;
	}

	/**
	 * 
	 */
	public void setReferences(String[] pReferences) {
		references = pReferences;
	}

	/**
	 * 
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * 
	 */
	public void setEntries(List<Entry> pFindings) {
		entries = pFindings;
	}

	/**
	 * 
	 */
	public Boolean isAcceptedByDesigner() {
		return acceptedByDesigner;
	}

	/**
	 * 
	 */
	public void setAcceptedByDesigner(Boolean acceptedByDesigner) {
		this.acceptedByDesigner = acceptedByDesigner;
	}

	/**
	 * 
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 
	 */
	public Calendar getRequestDate() {
		return requestDate;
	}

	/**
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 */
	public List<ExternalLink> getAttachments() {
		return attachments;
	}

	/**
	 * 
	 */
	public void setAttachments(List<ExternalLink> attachments) {
		this.attachments = attachments;
	}

	public List<Association> getRelatedToAssociations() {
		return relatedToAssociations;
	}

	public void setRelatedToAssociations(List<Association> relatedToAssociations) {
		this.relatedToAssociations = relatedToAssociations;
	}

	public Concept getCategory() {
		return category;
	}

	public void setCategory(Concept category) {
		this.category = category;
	}

	public List<Organization> getSubmittingOrganizations() {
		return submittingOrganizations;
	}
	
	public void setSubmittingOrganizations(
			List<Organization> submittingOrganizations) {
		this.submittingOrganizations = submittingOrganizations;
	}
	
}
