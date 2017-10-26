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

		mockQuestions
				.add(new Question(String.valueOf(0), "honestatis dico autem aliquip natum", LocalDateTime.parse("2017-09-22T01:00:00.000"), "admin"));
		mockQuestions
				.add(
						new Question(String.valueOf(1), "placerat gravida sociis invenire fermentum pri repudiandae",
								LocalDateTime.parse("2017-10-03T10:23:59.000"), "tester"));
		mockQuestions.add(
				new Question(String.valueOf(2), "sonet ex duis aptent docendi vivamus", LocalDateTime.parse("2017-10-05T17:30:00.000"), "admin"));
		mockQuestions
				.add(new Question(String.valueOf(3), "vero justo ludus dico per", LocalDateTime.parse("2017-10-22T19:00:00.000"), "anothertester"));
		mockQuestions.add(new Question(String.valueOf(4), "euripidis rutrum eripuit mi bibendum", LocalDateTime.parse("2017-10-26T15:00:00.000"),
				"anothertester"));

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
	public Collection<Question> listQuestions(String user, SortOptions sortOptions) {
		if (user == null) {
			return sortQuestions(mockQuestions, sortOptions);
		}

		return sortQuestions(mockQuestions.stream().filter(q -> q.getCreatedBy().equals(user)).collect(Collectors.toList()), sortOptions);
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

	/**
	 * Some sorting...not finished yet.
	 * 
	 * @param questions
	 * @param sortOptions
	 * @return
	 */
	private Collection<Question> sortQuestions(Collection<Question> questions, SortOptions sortOptions) {
		if (sortOptions.getSortOptions().containsKey("createdAt")) {
			switch (sortOptions.getSortOptions().get("createdAt")) {
			case ASC:
				return questions.stream().sorted((q1, q2) -> q1.getCreatedAt().compareTo(q2.getCreatedAt())).collect(Collectors.toList());
			case DESC:
				return questions.stream().sorted((q1, q2) -> q2.getCreatedAt().compareTo(q1.getCreatedAt())).collect(Collectors.toList());
			default:
				break;
			}
		}

		return questions;
	}
}
