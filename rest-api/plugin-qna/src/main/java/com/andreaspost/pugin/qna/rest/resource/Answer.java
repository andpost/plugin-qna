package com.andreaspost.pugin.qna.rest.resource;

import java.time.LocalDateTime;

/**
 * Answer resource - represents an answer to a {@link Question}.
 * 
 * @author Andreas Post
 */
public class Answer extends BaseResource {

	private String content;

	/**
	 * Empty constructor.
	 */
	public Answer() {
		super();
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
		super(id, createdAt, createdBy);
		this.content = content;
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
}
