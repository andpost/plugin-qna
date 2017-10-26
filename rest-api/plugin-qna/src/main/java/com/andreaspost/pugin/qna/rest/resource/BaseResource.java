package com.andreaspost.pugin.qna.rest.resource;

import java.time.LocalDateTime;

import com.andreaspost.pugin.qna.rest.converter.LocalDateTimeDeserializer;
import com.andreaspost.pugin.qna.rest.converter.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Base class for resources...
 * 
 * @author Andreas Post
 */
public abstract class BaseResource {

	private String href;

	private String id;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime createdAt;

	private String createdBy;

	/**
	 * Empty constructor.
	 */
	public BaseResource() {

	}

	/**
	 * Constructor for main fields.
	 * 
	 * @param id
	 * @param content
	 * @param createdAt
	 * @param createdBy
	 */
	public BaseResource(String id, LocalDateTime createdAt, String createdBy) {
		this.id = id;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
	}

	/**
	 * Constructor for all fields.
	 * 
	 * @param href
	 * @param id
	 * @param createdAt
	 * @param createdBy
	 */
	public BaseResource(String href, String id, LocalDateTime createdAt, String createdBy) {
		this.href = href;
		this.id = id;
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
