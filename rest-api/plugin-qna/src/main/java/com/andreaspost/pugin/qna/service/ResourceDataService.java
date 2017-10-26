package com.andreaspost.pugin.qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;

import com.andreaspost.pugin.qna.rest.resource.Question;

/**
 * Small service for some mock data.
 * 
 * @author Andreas Post
 */
@Stateless
public class ResourceDataService {

	private static List<Question> mockQuestions;

	static {
		mockQuestions = new ArrayList<>();

		mockQuestions.add(new Question(String.valueOf(0), "honestatis dico autem aliquip natum", LocalDateTime.now(), "admin"));
		mockQuestions
				.add(
						new Question(String.valueOf(1), "placerat gravida sociis invenire fermentum pri repudiandae", LocalDateTime.now(), "admin"));
		mockQuestions.add(new Question(String.valueOf(2), "sonet ex duis aptent docendi vivamus", LocalDateTime.now(), "admin"));
		mockQuestions.add(new Question(String.valueOf(3), "vero justo ludus dico per", LocalDateTime.now(), "admin"));
		mockQuestions.add(new Question(String.valueOf(4), "euripidis rutrum eripuit mi bibendum", LocalDateTime.now(), "admin"));
	}

	/**
	 * Get a {@link Question} by id;
	 * 
	 * @param id
	 * @return
	 */
	public Question getQuestion(String id) {
		for (Question question : mockQuestions) {
			if (question.getId().equals(id)) {
				return question;
			}
		}
		return null;
	}

	/**
	 * The list of all questions.
	 * 
	 * @return
	 */
	public Collection<Question> getQuestions() {
		return mockQuestions;
	}

	/**
	 * Add a question.
	 * 
	 * @param q
	 * @return
	 */
	public Question addNewQuestion(Question q) {
		q.setId(String.valueOf(mockQuestions.size()));
		q.setCreatedAt(LocalDateTime.now());

		mockQuestions.add(q);

		return q;
	}
}
