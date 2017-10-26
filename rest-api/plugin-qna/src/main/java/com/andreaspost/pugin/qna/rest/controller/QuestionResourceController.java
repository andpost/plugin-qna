package com.andreaspost.pugin.qna.rest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.andreaspost.pugin.qna.interceptors.MethodLoggingInterceptor;
import com.andreaspost.pugin.qna.rest.interceptors.RequestLoggingInterceptor;
import com.andreaspost.pugin.qna.rest.resource.Question;
import com.andreaspost.pugin.qna.service.ResourceDataService;
import com.andreaspost.pugin.qna.service.SortOptions;
import com.andreaspost.pugin.qna.service.SortOptions.SortOrder;

/**
 * Resource controller for {@link Question} resources.
 * 
 * @author Andreas Post
 */
@Path(QuestionResourceController.RESOURCE_PATH)
@Interceptors({ RequestLoggingInterceptor.class, MethodLoggingInterceptor.class })
public class QuestionResourceController {

	public static final String RESOURCE_PATH = "rest/questions/";

	private static final Logger LOG = Logger.getLogger(QuestionResourceController.class.getName());

	@Inject
	ResourceDataService dataService;

	@Context
	protected UriInfo uriInfo;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuestion(@PathParam("id") String id) {

		Question question = dataService.getQuestion(id); // replace with some backend stuff

		if (question == null) {
			return Response.status(Status.NOT_FOUND).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
		}

		addResourceURL(question);

		return Response.ok(question).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listQuestions(@QueryParam("createdBy") String user, @QueryParam("sort") String sort) {

		SortOptions sortOptions;

		try {
			sortOptions = createSortOptions(sort);
		} catch (IllegalArgumentException e) {
			LOG.log(Level.WARNING, "Error creating sort options.", e);
			return Response.status(Status.BAD_REQUEST).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
		}

		Collection<Question> questions = dataService.listQuestions(user, sortOptions);

		questions.forEach(q -> addResourceURL(q));

		return Response.ok(questions).header(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8).build();
	}

	/**
	 * Create a new question.
	 * 
	 * @param question
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createQuestion(Question question) {
		Question newQuestion = dataService.addNewQuestion(question);

		addResourceURL(newQuestion);

		URI location;

		try {
			location = new URI(newQuestion.getHref());
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
	private void addResourceURL(Question q) {
		q.setHref(uriInfo.getBaseUri().toString() + RESOURCE_PATH + q.getId());

		String answerUrlString = AnswerResourceController.RESOURCE_PATH.replace("{qid}", q.getId());
		q.setAnswersHref(uriInfo.getBaseUri().toString() + answerUrlString);
	}

	/**
	 * Create {@link SortOptions} if we have some.
	 * 
	 * @param sortParam
	 * @return
	 */
	private SortOptions createSortOptions(String sortParam) {
		SortOptions so = new SortOptions();

		if (sortParam == null) {
			return so;
		}

		for (String elem : sortParam.split(",")) {
			so.addSortOption(elem.substring(1, elem.length()), SortOrder.of(elem.substring(0, 1)));
		}

		return so;
	}
}
