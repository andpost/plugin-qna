package com.andreaspost.pugin.qna.rest.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.andreaspost.pugin.qna.rest.Constants;
import com.andreaspost.pugin.qna.rest.resource.Answer;
import com.andreaspost.pugin.qna.service.ResourceDataService;

@Path(Constants.ANSWERS_RESOURCE_PATH)
public class AnswerResourceController {

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
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response getAnswer(@PathParam("id") String answerId) {
		Answer answer = dataService.getAnswer(questionId, answerId);

		if (answer == null) {
			return Response.status(Status.NOT_FOUND).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
		}

		addResourceURL(answer);

		return Response.ok(answer).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

	/**
	 * List answers for given questionId.
	 * 
	 * @return
	 */
	@GET
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response listAnswers() {
		List<Answer> answers = dataService.listAnswers(questionId);

		if (answers == null) {
			return Response.status(Status.NOT_FOUND).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
		}

		answers.forEach(ans -> addResourceURL(ans));

		return Response.ok(answers).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

	/**
	 * Add the self url for the given resource.
	 * 
	 * @param q
	 */
	private void addResourceURL(Answer ans) {
		String baseUrlString = Constants.ANSWERS_RESOURCE_PATH.replace("{qid}", questionId);
		ans.setHref(uriInfo.getBaseUri().toString() + baseUrlString + ans.getId());
	}
}
