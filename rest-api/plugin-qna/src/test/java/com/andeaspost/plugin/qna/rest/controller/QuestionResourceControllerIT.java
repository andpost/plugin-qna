package com.andeaspost.plugin.qna.rest.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.andreaspost.pugin.qna.rest.resource.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;

/**
 * REST API Tests for {@link Question} resource.
 * 
 * @author Andreas Post
 */
public class QuestionResourceControllerIT extends TestsBase {

	private static final Logger LOG = Logger.getLogger(QuestionResourceControllerIT.class.getName());

	/**
	 * Basic test for GET by id.
	 */
	@Test
	public void getQuestion() {
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions/1");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		Question q = null;

		try {
			q = new ObjectMapper().readValue(response.body().asString(), new TypeReference<Question>() {
			});
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to parse resource response.", e);
			fail(e.getMessage());
		}

		checkSingleResource(q);

		// TODO if answer URL is given, try following the link
	}

	/**
	 * Tests that we get a 404 if resource is not found.
	 */
	@Test
	public void getQuestionNegative() {
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions/abc");

		response.then().assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
	}

	/**
	 * Tests list questions via GET.
	 */
	@Test
	public void listQuestions() {
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		List<Question> qList = parseQuestionListResponse(response);

		qList.forEach(q -> checkSingleResource(q));
	}

	/**
	 * Tests limitation and pagination of questions.
	 */
	@Test
	public void listQuestionsWithPagination() {
		int limit = 5;
		Response response = given().headers(headers).contentType(CONTENT_TYPE).queryParam("limit", limit).expect().log().all().get("questions");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		List<Question> qList = parseQuestionListResponse(response);

		assertTrue("Expected a result with less than or equal as " + limit + " entries, but received: " + qList.size(), qList.size() <= limit);

		qList.forEach(q -> checkSingleResource(q));

		// TODO some more tests checking link headers and follow the links
	}

	/**
	 * Tests filtering questions by user.
	 */
	@Test
	public void listQuestionsByUser() {
		String userName = "tester";
		Response response = given().headers(headers).contentType(CONTENT_TYPE).queryParam("createdBy", userName).expect().log().all()
				.get("questions");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		List<Question> qList = parseQuestionListResponse(response);

		qList.forEach(q -> assertTrue("Filtered result must only contain entries for: " + userName, q.getCreatedBy().equals(userName)));
	}

	/**
	 * Tests sorting of questions in ascending order (by createdBy).
	 */
	@Test
	public void listQuestionsSortedByCreationDateAsc() {
		String sortCriteria = "+createdAt";
		Response response = given().headers(headers).contentType(CONTENT_TYPE).queryParam("sort", sortCriteria).expect().log().all()
				.get("questions");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		List<Question> qList = parseQuestionListResponse(response);

		Question prev = null;

		for (Question next : qList) {
			if (prev != null) {
				assertTrue("Sorting must be ascending.",
						prev.getCreatedAt().isBefore(next.getCreatedAt()) //
								|| prev.getCreatedAt().equals(next.getCreatedAt()));
			}
			prev = next;
		}
	}

	/**
	 * Tests sorting of questions in descending order (by createdBy).
	 */
	@Test
	public void listQuestionsSortedByCreationDateDesc() {
		String sortCriteria = "-createdAt";
		Response response = given().headers(headers).contentType(CONTENT_TYPE).queryParam("sort", sortCriteria).expect().log().all()
				.get("questions");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		List<Question> qList = parseQuestionListResponse(response);

		Question prev = null;

		for (Question next : qList) {
			if (prev != null) {
				assertTrue("Sorting must be descending.",
						prev.getCreatedAt().isAfter(next.getCreatedAt()) //
								|| prev.getCreatedAt().equals(next.getCreatedAt()));
			}
			prev = next;
		}
	}

	@Test
	public void createQuestion() {
		Question q = new Question();
		q.setContent("Unit Test");
		q.setCreatedBy("tester");

		Response response = given().headers(headers).contentType(CONTENT_TYPE).body(q).expect().log().all()
				.post("questions");

		response.then().assertThat().statusCode(Status.CREATED.getStatusCode());

		String location = response.getHeader("location");

		assertNotNull("Location header must not be null", location);
		assertTrue("Location header must include resource path.", location.contains("questions/"));
	}

	private void checkSingleResource(Question q) {
		assertNotNull("Question resource must not be null.", q);
		assertNotNull("Content must not be null.", q.getContent());
		assertNotNull("Creation date must not be null.", q.getCreatedAt());
		assertNotNull("Creator info must not be null.", q.getCreatedBy());
		assertNotNull("Self URL must not be null.", q.getHref());
		assertNotNull("Answers URL must not be null.", q.getAnswersHref());
	}

	/**
	 * Convenience method for parsing a response with a list of questions.
	 * 
	 * @param response
	 * @return
	 */
	private List<Question> parseQuestionListResponse(Response response) {
		List<Question> qList = null;

		try {
			qList = new ObjectMapper().readValue(response.body().asString(), new TypeReference<List<Question>>() {
			});
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to parse resource response.", e);
			fail(e.getMessage());
		}

		assertNotNull("Questions list resource must not be null.", qList);

		return qList;
	}

}
