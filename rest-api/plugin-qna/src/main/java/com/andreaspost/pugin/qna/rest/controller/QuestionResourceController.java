package com.andreaspost.pugin.qna.rest.controller;

import java.net.URI;
import java.net.URISyntaxException;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.andreaspost.pugin.qna.interceptors.MethodLoggingInterceptor;
import com.andreaspost.pugin.qna.rest.Constants;
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
@Path(Constants.QUESTION_RESOURCE_PATH)
@Interceptors({ RequestLoggingInterceptor.class, MethodLoggingInterceptor.class })
public class QuestionResourceController {

	private static final Logger LOG = Logger.getLogger(QuestionResourceController.class.getName());

	@Inject
	ResourceDataService dataService;

	@Context
	protected UriInfo uriInfo;

	@GET
	@Path("{id}")
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response getQuestion(@PathParam("id") String id) {

		Question question = dataService.getQuestion(id); // replace with some backend stuff

		if (question == null) {
			return Response.status(Status.NOT_FOUND).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
		}

		addResourceURL(question);

		return Response.ok(question).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

	@GET
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response listQuestions(@QueryParam("createdBy") String user, @QueryParam("sort") String sort) {

		SortOptions sortOptions;

		try {
			sortOptions = createSortOptions(sort);
		} catch (IllegalArgumentException e) {
			LOG.log(Level.WARNING, "Error creating sort options.", e);
			return Response.status(Status.BAD_REQUEST).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
		}

		Collection<Question> questions = dataService.listQuestions(user, sortOptions);

		questions.forEach(q -> addResourceURL(q));

		return Response.ok(questions).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

	/**
	 * Create a new question.
	 * 
	 * @param question
	 * @return
	 */
	@POST
	@Consumes(Constants.MEDIA_TYPE_JSON)
	public Response createQuestion(Question question) {
		Question newQuestion = dataService.addNewQuestion(question);

		addResourceURL(newQuestion);

		URI location;

		try {
			location = new URI(newQuestion.getHref());
		} catch (URISyntaxException e) {
			return Response.serverError().header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
		}

		return Response.created(location).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

	/**
	 * Add the self url for the given resource.
	 * 
	 * @param q
	 */
	private void addResourceURL(Question q) {
		q.setHref(uriInfo.getBaseUri().toString() + Constants.QUESTION_RESOURCE_PATH + q.getId());

		String answerUrlString = Constants.ANSWERS_RESOURCE_PATH.replace("{qid}", q.getId());
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
