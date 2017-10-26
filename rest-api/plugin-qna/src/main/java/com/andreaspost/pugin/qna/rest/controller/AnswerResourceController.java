package com.andreaspost.pugin.qna.rest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.andreaspost.pugin.qna.rest.resource.Answer;
import com.andreaspost.pugin.qna.service.ResourceDataService;

@Path(AnswerResourceController.RESOURCE_PATH)
public class AnswerResourceController {

	public static final String RESOURCE_PATH = QuestionResourceController.RESOURCE_PATH + "{qid}/answers/";

	@Inject
	ResourceDataService dataService;

	@Context
	protected UriInfo uriInfo;

	@PathParam("qid")
	private String questionId;

	/**
	 * List a specific answer for a given questionId and answerId.
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnswer(@PathParam("id") String answerId) {
		Answer answer = dataService.getAnswer(questionId, answerId);

		if (answer == null) {
			return Response.status(Status.NOT_FOUND).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
		}

		addResourceURL(answer);

		return Response.ok(answer).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
	}

	/**
	 * List answers for given questionId.
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAnswers() {
		List<Answer> answers = dataService.listAnswers(questionId);

		if (answers == null) {
			return Response.status(Status.NOT_FOUND).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
		}

		answers.forEach(ans -> addResourceURL(ans));

		return Response.ok(answers).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
	}

	/**
	 * Adds an answer to a given question.
	 * 
	 * @param answer
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAnswer(Answer answer) {
		Answer newAnswer = dataService.addAnswer(questionId, answer);

		if (newAnswer == null) {
			return Response.status(Status.NOT_FOUND).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
		}

		addResourceURL(newAnswer);

		URI location;

		try {
			location = new URI(newAnswer.getHref());
		} catch (URISyntaxException e) {
			return Response.serverError().header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
		}

		return Response.created(location).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
	}

	/**
	 * Add the self url for the given resource.
	 * 
	 * @param q
	 */
	private void addResourceURL(Answer ans) {
		String baseUrlString = RESOURCE_PATH.replace("{qid}", questionId);
		ans.setHref(uriInfo.getBaseUri().toString() + baseUrlString + ans.getId());
	}
}
