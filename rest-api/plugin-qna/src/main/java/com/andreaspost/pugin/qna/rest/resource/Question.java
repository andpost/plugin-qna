package com.andreaspost.pugin.qna.rest.resource;

import java.time.LocalDateTime;

import com.andreaspost.pugin.qna.rest.converter.LocalDateTimeDeserializer;
import com.andreaspost.pugin.qna.rest.converter.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Resource for questions in our Q&A plugin.
 * 
 * @author Andreas Post
 */
public class Question {

	private String href;

	private String content;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime createdAt;

	private String createdBy;

	private String answersHref;

	/**
	 * The self URL.
	 * 
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @param href
	 *            the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * The content - the question itself ;)
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content of the question.
	 * 
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * THE URL to get the answers to this question.
	 * 
	 * @return the answersHref
	 */
	public String getAnswersHref() {
		return answersHref;
	}

	/**
	 * @param answersHref
	 *            the answersHref to set
	 */
	public void setAnswersHref(String answersHref) {
		this.answersHref = answersHref;
	}

}
