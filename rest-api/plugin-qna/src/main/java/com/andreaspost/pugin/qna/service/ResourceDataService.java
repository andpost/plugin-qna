package com.andreaspost.pugin.qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.andreaspost.pugin.qna.interceptors.MethodLoggingInterceptor;
import com.andreaspost.pugin.qna.rest.resource.Answer;
import com.andreaspost.pugin.qna.rest.resource.Question;

/**
 * Small service for some mock data.
 * 
 * @author Andreas Post
 */
@Stateless
@Interceptors({ MethodLoggingInterceptor.class })
public class ResourceDataService {

	private static List<Question> mockQuestions;

	private static Map<String, List<Answer>> mockAnswers;

	static {
		mockQuestions = new ArrayList<>();

		mockQuestions
				.add(new Question(String.valueOf(0), "honestatis dico autem aliquip natum", LocalDateTime.parse("2016-09-22T01:00:00.000"), "admin"));
		mockQuestions
				.add(
						new Question(String.valueOf(1), "placerat gravida sociis fermentum pri",
								LocalDateTime.parse("2016-10-03T10:23:59.000"), "tester"));
		mockQuestions.add(
				new Question(String.valueOf(2), "sonet ex duis aptent docendi vivamus", LocalDateTime.parse("2016-10-05T17:30:00.000"), "admin"));
		mockQuestions
				.add(new Question(String.valueOf(3), "vero justo ludus dico per", LocalDateTime.parse("2016-10-22T19:00:00.000"), "anothertester"));
		mockQuestions.add(new Question(String.valueOf(4), "euripidis rutrum eripuit mi bibendum", LocalDateTime.parse("2017-01-13T15:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(5), "Stet clita kasd gubergren", LocalDateTime.parse("2017-02-15T11:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(6), "sed diam voluptua. At vero", LocalDateTime.parse("2017-03-26T13:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(7), "takimata sanctus est Lorem", LocalDateTime.parse("2017-04-01T02:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(8), "consetetur sadipscing elitr, sed", LocalDateTime.parse("2017-05-05T20:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(9), "At vero eos et accusam", LocalDateTime.parse("2017-06-30T23:56:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(10), "Stet clita kasd gubergren", LocalDateTime.parse("2017-07-09T12:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(11), "nonumy eirmod tempor invidunt", LocalDateTime.parse("2017-08-01T10:00:00.000"),
				"anothertester"));
		mockQuestions.add(new Question(String.valueOf(12), "sadipscing elitr, sed", LocalDateTime.parse("2017-10-26T09:00:00.000"),
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
	 * Returns the amount of questions with the given query data.
	 * 
	 * @param createdBy
	 *            If not NULL queries only answers of this user.
	 * @return
	 */
	public int countQuestions(String createdBy) {
		if (createdBy == null) {
			return mockQuestions.size();
		}

		return mockQuestions.stream().filter(q -> q.getCreatedBy().equals(createdBy)).collect(Collectors.toList()).size();
	}

	/**
	 * The list of all questions.
	 * 
	 * @param createdBy
	 *            If not NULL queries only answers of this user.
	 * @param sortOptions
	 *            Information on how to sort the result.
	 * @param limit
	 *            The given result limit.
	 * @param offset
	 *            The current offset.
	 * @return
	 */
	public Collection<Question> listQuestions(String createdBy, SortOptions sortOptions, int limit, int offset) {
		Collection<Question> result = mockQuestions;

		if (createdBy == null) {
			return paginate(sortQuestions(result, sortOptions), limit, offset);
		}

		return paginate(sortQuestions(result.stream().filter(q -> q.getCreatedBy().equals(createdBy)).collect(Collectors.toList()), sortOptions),
				limit,
				offset);
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
	 * Some sorting...not the best impl so far, but a start to have some sorting feature.
	 * 
	 * If we have the mongo backend, we let the db do the sorting.
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

	/**
	 * Creates some paging functionality. Unnecessary if provided by some database backend.
	 * 
	 * @param questions
	 * @param limit
	 * @param offset
	 * @return
	 */
	private Collection<Question> paginate(Collection<Question> questions, int limit, int offset) {
		List<Question> collect = questions.stream().skip(offset).limit(limit).collect(Collectors.toList());

		return collect;
	}
}
