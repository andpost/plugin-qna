package com.andreaspost.pugin.qna.rest.resource;

import java.time.LocalDateTime;

import com.andreaspost.pugin.qna.rest.converter.LocalDateTimeDeserializer;
import com.andreaspost.pugin.qna.rest.converter.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Answer resource - represents an answer to a {@link Question}.
 * 
 * @author Andreas Post
 */
public class Answer {

	private String href;

	private String id;

	private String content;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime createdAt;

	private String createdBy;

	/**
	 * Empty constructor.
	 */
	public Answer() {

	}

	/**
	 * Constructor for main fields.
	 * 
	 * @param id
	 * @param content
	 * @param createdAt
	 * @param createdBy
	 */
	public Answer(String id, String content, LocalDateTime createdAt, String createdBy) {
		this.id = id;
		this.content = content;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The content of the answer.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
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
}
