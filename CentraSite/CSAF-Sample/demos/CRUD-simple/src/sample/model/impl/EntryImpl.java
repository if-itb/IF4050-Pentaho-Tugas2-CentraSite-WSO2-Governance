package sample.model.impl;

import java.util.List;

import sample.model.Entry;
import sample.model.ExternalLink;

import com.softwareag.centrasite.appl.framework.beans.DynamicRegistryBean;

/**
 * Default implementation of {@link Entry}.
 */
public class EntryImpl extends DynamicRegistryBean implements Entry {
	/**
	 */
	public static class EntryCodeImpl extends DynamicRegistryBean implements
			EntryCode {

		private String code;

		public String getCode() {
			return this.code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}

	/**
	 */
	public static class EntryStatusImpl extends DynamicRegistryBean implements
			EntryStatus {

		private String status;

		public String getStatus() {
			return this.status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	private EntryCode code;
	private EntryStatus status;
	private String comment;
	private String correctiveMeasure;
	private String explanation;
	private String title;
	private List<ExternalLink> attachments;

	public List<ExternalLink> getAttachments() {
		return this.attachments;
	}

	public EntryCode getCode() {
		return this.code;
	}

	public String getComment() {
		return this.comment;
	}

	public String getCorrectiveMeasure() {
		return this.correctiveMeasure;
	}

	public String getExplanation() {
		return this.explanation;
	}

	public EntryStatus getStatus() {
		return this.status;
	}

	public String getTitle() {
		return this.title;
	}

	public void setAttachments(List<ExternalLink> attachments) {
		this.attachments = attachments;
	}

	public void setCode(EntryCode code) {
		this.code = code;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setCorrectiveMeasure(String correctiveMeasure) {
		this.correctiveMeasure = correctiveMeasure;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public void setStatus(EntryStatus status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
