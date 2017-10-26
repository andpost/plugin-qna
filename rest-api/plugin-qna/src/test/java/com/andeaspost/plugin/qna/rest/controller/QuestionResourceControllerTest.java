package com.andeaspost.plugin.qna.rest.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
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
public class QuestionResourceControllerTest extends TestsBase {

	private static final Logger LOG = Logger.getLogger(QuestionResourceControllerTest.class.getName());

	/**
	 * Basic test for GET by id.
	 */
	@Test
	public void getQuestion() {
		Response response = given().headers(headers).contentType(CONTENT_TYPE).expect().log().all().get("question/123");

		response.then().assertThat().statusCode(Status.OK.getStatusCode());

		Question q = null;

		try {
			q = new ObjectMapper().readValue(response.body().asString(), new TypeReference<Question>() {
			});
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Unable to parse resource response.", e);
			fail(e.getMessage());
		}

		assertNotNull("Question resource must not be null.", q);
		assertNotNull("Content must not be null.", q.getContent());
		assertNotNull("Creation date must not be null.", q.getCreatedAt());
		assertNotNull("Creator info must not be null.", q.getCreatedBy());
		assertNotNull("Self URL must not be null.", q.getHref());

		// TODO if answer URL is given, try following the link
	}

}
