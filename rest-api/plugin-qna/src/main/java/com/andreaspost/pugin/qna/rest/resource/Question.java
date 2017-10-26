package com.andreaspost.pugin.qna.rest.resource;

import java.time.LocalDateTime;

/**
 * Resource for questions in our Q&A plugin.
 * 
 * @author Andreas Post
 */
public class Question extends BaseResource {

	private String content;

	private String answersHref;

	/**
	 * Empty constructor.
	 */
	public Question() {
		super();
	}

	/**
	 * Constructor for main stuff.
	 * 
	 * @param content
	 * @param createdAt
	 * @param createdBy
	 */
	public Question(String id, String content, LocalDateTime createdAt, String createdBy) {
		super(id, createdAt, createdBy);
		this.content = content;
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
