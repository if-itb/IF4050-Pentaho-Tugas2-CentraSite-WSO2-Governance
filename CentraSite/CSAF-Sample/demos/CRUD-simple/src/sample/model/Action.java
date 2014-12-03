package sample.model;

import java.util.Calendar;
import java.util.List;

import sample.model.impl.ActionImpl;

import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.beans.standard.Concept;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Association;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Classification;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.ClassificationConcept;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.ClassifiedInstance;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.ClassifiedInstances;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.ExternalLink;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Property;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Slot;

/**
 * Interface for action object
 * 
 * @author bgmpa
 * 
 */
@RegistryObject(objectTypeName = "{http://namespaces.CentraSite.com/csaf}Action")
@Bean(implementation = ActionImpl.class)
@ClassifiedInstances(instances = {
		@ClassifiedInstance(classificationScheme = "CSAF-Taxonomy", conceptPath = "/ClassificationInstances/ActionTypes/Action1", beanType = Action1.class),
		@ClassifiedInstance(classificationScheme = "CSAF-Taxonomy", conceptPath = "/ClassificationInstances/ActionTypes/Action2", beanType = Action2.class) })
public interface Action extends RegistryBean {
	/**
	 * Interface of the decision.
	 */
	final String RELATED_TO_ASSOCIATION = "uddi:c3558cb9-574b-352b-77ca-de4390edd2e8";
	
	public interface DecisionType {
		/**
		 * Returns the decisions textual value.
		 */
		String getDecision();
	}

	/**
	 * @return as
	 */
	@Slot(name = "{http://namespaces.CentraSite.com/csaf}acceptedByDesigner")
	Boolean isAcceptedByDesigner();

	/**
	 * 
	 */
	void setAcceptedByDesigner(Boolean isAcceptedByDesigner);

	/**
	 * Returns the review date.
	 */
	Calendar getReviewDate();

	/**
	 * Sets the review date.
	 */
	void setReviewDate(Calendar reviewDate);

	/**
	 * Returns the decision type.
	 */
	DecisionType getDecisionType();

	/**
	 * Sets the decision type.
	 */
	void setDecisionType(DecisionType pDecisionType);

	/**
	 * Returns the recommendations.
	 */
	String[] getRecommendations();

	/**
	 * Sets the recommendations.
	 */
	void setRecommendations(String[] pRecommendations);

	/**
	 * Returns the references.
	 */
	String[] getReferences();

	/**
	 * Sets the references.
	 */
	void setReferences(String[] pReferences);

	/**
	 * Returns the findings, which are attached to the bean.
	 */
	@Classification(classificationScheme = "CSAF-Taxonomy", conceptPath = "/ClassificationInstances/Entry", targetType = Entry.class)
	List<Entry> getEntries();

	/**
	 * 
	 * @param pFindings
	 */
	public void setEntries(List<Entry> pFindings);

	/**
	 * Returns the request date.
	 */
	@Slot(name = "{http://namespaces.CentraSite.com/csaf}requestDate")
	Calendar getRequestDate();

	/**
	 * Sets the request date.
	 */
	void setRequestDate(Calendar pDate);

	/**
	 * 
	 */
	public long getTimestamp();

	/**
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(long timestamp);
	
	@Property(target = "name")
	public String getName();
	
	public void setName(String name);

	/**
	 * Returns the description
	 */
	@Property(target = "description")
	public String getDescription();

	/**
	 * Sets the description
	 * 
	 * @param description
	 */
	public void setDescription(String description);

	/**
	 * Returns the attachments
	 * 
	 * @return List<ExternalLink>
	 */
	@ExternalLink(type = sample.model.ExternalLink.class)
	public List<sample.model.ExternalLink> getAttachments();

	/**
	 * Sets the attachments
	 * 
	 * @param attachments
	 */
	public void setAttachments(
			List<sample.model.ExternalLink> attachments);	
		
	@Association(key=RELATED_TO_ASSOCIATION)
	public List<com.softwareag.centrasite.appl.framework.beans.standard.Association> getRelatedToAssociations();
	/**
	 * Sets RelatedTo associations
	 * 
	 * @param associations
	 */
	public void setRelatedToAssociations(List<com.softwareag.centrasite.appl.framework.beans.standard.Association> relatedToAssociations);
	
	@Classification(classificationScheme="ObjectType", conceptKey = Constants.OBJECT_TYPE_KEY_Concept)
	@ClassificationConcept
	public Concept getCategory();
	/**
	 * Sets category
	 * 
	 * @param category
	 */
	public void setCategory(Concept category);	
		
	@Property(targetType =  com.softwareag.centrasite.appl.framework.beans.standard.Organization.class, target="submittingOrganization")
	public List<com.softwareag.centrasite.appl.framework.beans.standard.Organization> getSubmittingOrganizations();
	/**
	 * sets organizations
	 * 
	 * @param organizations
	 */
	public void setSubmittingOrganizations(List<com.softwareag.centrasite.appl.framework.beans.standard.Organization> submittingOrganizations);
}
