package com.andreaspost.pugin.qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import com.andreaspost.pugin.qna.rest.resource.Answer;
import com.andreaspost.pugin.qna.rest.resource.Question;

/**
 * Small service for some mock data.
 * 
 * @author Andreas Post
 */
@Stateless
public class ResourceDataService {

	private static List<Question> mockQuestions;

	private static Map<String, List<Answer>> mockAnswers;

	static {
		mockQuestions = new ArrayList<>();

		mockQuestions.add(new Question(String.valueOf(0), "honestatis dico autem aliquip natum", LocalDateTime.now(), "admin"));
		mockQuestions
				.add(
						new Question(String.valueOf(1), "placerat gravida sociis invenire fermentum pri repudiandae", LocalDateTime.now(), "tester"));
		mockQuestions.add(new Question(String.valueOf(2), "sonet ex duis aptent docendi vivamus", LocalDateTime.now(), "admin"));
		mockQuestions.add(new Question(String.valueOf(3), "vero justo ludus dico per", LocalDateTime.now(), "anothertester"));
		mockQuestions.add(new Question(String.valueOf(4), "euripidis rutrum eripuit mi bibendum", LocalDateTime.now(), "anothertester"));

		mockAnswers = new HashMap<>();

		for (Question question : mockQuestions) {
			mockAnswers.put(question.getId(), new ArrayList<>());
		}
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
	 * @param user
	 *            If not NULL returns only answers of this user.
	 * @return
	 */
	public Collection<Question> getQuestions(String user) {
		if (user == null) {
			return mockQuestions;
		}

		return mockQuestions.stream().filter(q -> q.getCreatedBy().equals(user)).collect(Collectors.toList());
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

	/**
	 * Returns a specific answer by questionId and answerId.
	 * 
	 * @param questionId
	 * @param answerId
	 * @return
	 */
	public Answer getAnswer(String questionId, String answerId) {
		if (mockAnswers.containsKey(questionId)) {
			for (Answer answer : mockAnswers.get(questionId)) {
				if (answer.getId().equals(answerId)) {
					return answer;
				}
			}
		}

		return null;
	}

	/**
	 * List answers by questionId. Returns NULL if there is no question with the given questionId.
	 * 
	 * @param questionId
	 * @return
	 */
	public List<Answer> listAnswers(String questionId) {
		if (mockAnswers.containsKey(questionId)) {
			return mockAnswers.get(questionId);
		}

		return null;
	}

	/**
	 * Adds an answer to a question. Returns NULL if there is no question with the given questionId.
	 * 
	 * @param questionId
	 * @param answer
	 * @return
	 */
	public Answer addAnswer(String questionId, Answer answer) {
		if (mockAnswers.containsKey(questionId)) {
			answer.setId(String.valueOf(mockAnswers.get(questionId).size()));
			answer.setCreatedAt(LocalDateTime.now());

			mockAnswers.get(questionId).add(answer);

			return answer;
		}

		return null;
	}
}
