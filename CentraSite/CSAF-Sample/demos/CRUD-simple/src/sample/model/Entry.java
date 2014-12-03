package sample.model;

import java.util.List;

import sample.model.impl.EntryImpl;
import sample.model.impl.EntryImpl.EntryCodeImpl;
import sample.model.impl.EntryImpl.EntryStatusImpl;

import com.centrasite.jaxr.infomodel.Constants;
import com.softwareag.centrasite.appl.framework.beans.RegistryBean;
import com.softwareag.centrasite.appl.framework.beans.annotations.Bean;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Classification;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Property;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.RegistryObject;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Slot;
import com.softwareag.centrasite.appl.framework.persistence.mapper.annotations.Classification.MappedTo;

/**
 * 
 */
@RegistryObject(objectTypeKey = Constants.OBJECT_TYPE_KEY_Classification)
@Bean(implementation = EntryImpl.class)
public interface Entry extends RegistryBean {
	/**
	 * The code of a entry is its type.
	 */
        @RegistryObject(objectTypeKey = Constants.OBJECT_TYPE_KEY_Concept)
	@Bean(implementation = EntryCodeImpl.class)
	public interface EntryCode extends RegistryBean {
		/**
		 * Returns the entry code.
		 */
	    @Property(target = "value")
		public String getCode();

		/**
		 * 
		 * @param code
		 */
		public void setCode(String code);
	}

	/**
	 * The status of a entry is used to indicate, whether and how the entry
	 * has been processed.
	 */
        @RegistryObject(objectTypeKey = Constants.OBJECT_TYPE_KEY_Concept)
	@Bean(implementation = EntryStatusImpl.class)
	public interface EntryStatus extends RegistryBean {
		/**
		 * Returns the textual description of the status.
		 */
		@Property(target = "value")
		public String getStatus();

		/**
		 * 
		 * @param status
		 */
		public void setStatus(String status);
	}

	/**
	 * Returns a list of attachments, which have been linked to the entry.
	 */
	public List<ExternalLink> getAttachments();

	/**
	 * Sets a list of attachments, which have been linked to the entry.
	 */
	public void setAttachments(List<ExternalLink> attachments);

	/**
	 * Returns the entry code.
	 */
	@Classification(classificationScheme = "CSAF-Taxonomy", parentConcept = "/ClassificationInstances/EntryCodeType", mappedTo = MappedTo.TARGET_CONCEPT)
	public EntryCode getCode();

	/**
	 * Sets the entry code.
	 */
	public void setCode(EntryCode code);

	/**
	 * Returns the entry comment.
	 */
	@Slot(name = "{http://namespaces.CentraSite.com/csaf}comment")
	public String getComment();

	/**
	 * Sets the entry comment.
	 */
	public void setComment(String comment);

	/**
	 * Returns the entry corrective measure.
	 */
	@Slot(name = "{http://namespaces.CentraSite.com/csaf}correctiveMeasure")
	public String getCorrectiveMeasure();

	/**
	 * Sets the findings entry measure.
	 */
	public void setCorrectiveMeasure(String correctiveMeasure);

	/**
	 * Returns the entry explanation.
	 */
	@Slot(name = "{http://namespaces.CentraSite.com/csaf}explanation")
	public String getExplanation();

	/**
	 * Sets the entry explanation.
	 */
	public void setExplanation(String explanation);

	/**
	 * Returns the entry status.
	 */
	@Classification(classificationScheme = "CSAF-Taxonomy", parentConcept = "/ClassificationInstances/EntryStatusType", mappedTo = MappedTo.TARGET_CONCEPT)
	public EntryStatus getStatus();

	/**
	 * Sets the entry status.
	 */
	public void setStatus(EntryStatus status);

	/**
	 * Returns the entry title.
	 */
	@Slot(name = "{http://namespaces.CentraSite.com/csaf}title")
	public String getTitle();

	/**
	 * Sets the entry title.
	 */
	public void setTitle(String title);
}