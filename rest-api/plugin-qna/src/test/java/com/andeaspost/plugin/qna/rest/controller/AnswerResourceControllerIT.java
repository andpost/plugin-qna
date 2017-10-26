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

import com.andreaspost.pugin.qna.rest.resource.Answer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;

public class AnswerResourceControllerIT extends TestsBase {

	private static final Logger LOG = Logger.getLogger(AnswerResourceControllerIT.class.getName());

	@Test
	public void getAnswerNegative() {
		// known questionId - unknown answerId
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions/4/answers/abc");
		response.then().assertThat().statusCode(Status.NOT_FOUND.getStatusCode());

		// unknown questionId
		response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions/4/answers/abc");
		response.then().assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
	}

	/**
	 * Tests list answers via GET.
	 */
	@Test
	public void listAnswers() {
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions/1/answers");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		List<Answer> ansList = null;

		try {
			ansList = new ObjectMapper().readValue(response.body().asString(), new TypeReference<List<Answer>>() {
			});
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to parse resource response.", e);
			fail(e.getMessage());
		}

		assertNotNull("Answer list resource must not be null.", ansList);

		ansList.forEach(ans -> checkSingleResource(ans));
	}

	/**
	 * Negative lists test - tries to get answers for unknown
	 */
	@Test
	public void listAnswersNegative() {
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("questions/abc/answers");

		response.then().assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
	}

	/**
	 * Tests adding an answer to a given question.
	 */
	@Test
	public void addAnswer() {
		Answer answer = new Answer();

		answer.setContent("42");
		answer.setCreatedBy("Tester");

		Response response = given().headers(headers).contentType(CONTENT_TYPE).body(answer).expect().log().all()
				.post("questions/1/answers");

		response.then().assertThat().statusCode(Status.CREATED.getStatusCode());

		String location = response.getHeader("location");

		assertNotNull("Location header must not be null", location);
		assertTrue("Location header must include resource path.", location.contains("questions/1/answers/"));
	}

	/**
	 * Tests adding answers to unknown questions.
	 */
	@Test
	public void addAnswerNegative() {
		Answer answer = new Answer();

		answer.setContent("bad answer");
		answer.setCreatedBy("Tester");

		Response response = given().headers(headers).contentType(CONTENT_TYPE).body(answer).expect().log().all()
				.post("questions/abc/answers");

		response.then().assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
	}

	private void checkSingleResource(Answer ans) {
		assertNotNull("Answer resource must not be null.", ans);
		assertNotNull("Content must not be null.", ans.getContent());
		assertNotNull("Creation date must not be null.", ans.getCreatedAt());
		assertNotNull("Creator info must not be null.", ans.getCreatedBy());
		assertNotNull("Self URL must not be null.", ans.getHref());
	}
}
