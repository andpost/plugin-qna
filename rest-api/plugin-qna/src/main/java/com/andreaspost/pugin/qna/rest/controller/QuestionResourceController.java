package com.andreaspost.pugin.qna.rest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.andreaspost.pugin.qna.rest.Constants;
import com.andreaspost.pugin.qna.rest.resource.Question;
import com.andreaspost.pugin.qna.service.ResourceDataService;

/**
 * Resource controller for {@link Question} resources.
 * 
 * @author Andreas Post
 */
@Path(Constants.QUESTION_RESOURCE_PATH)
public class QuestionResourceController {

//	private static Map<String, Question> dummyQuestions;
//
//	private static Question createdQuestion;
//
//	static {
//		dummyQuestions = new HashMap<>();
//
//		dummyQuestions.put(String.valueOf(0), new Question(String.valueOf(0), "honestatis dico autem aliquip natum", LocalDateTime.now(), "admin"));
//		dummyQuestions
//				.put(String.valueOf(1),
//						new Question(String.valueOf(1), "placerat gravida sociis invenire fermentum pri repudiandae", LocalDateTime.now(), "admin"));
//		dummyQuestions.put(String.valueOf(2), new Question(String.valueOf(2), "sonet ex duis aptent docendi vivamus", LocalDateTime.now(), "admin"));
//		dummyQuestions.put(String.valueOf(3), new Question(String.valueOf(3), "vero justo ludus dico per", LocalDateTime.now(), "admin"));
//		dummyQuestions.put(String.valueOf(4), new Question(String.valueOf(4), "euripidis rutrum eripuit mi bibendum", LocalDateTime.now(), "admin"));
//	}

	@Inject
	ResourceDataService dataService;

	@Context
	protected UriInfo uriInfo;

	@GET
	@Path("{id}")
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response getQuestion(@PathParam("id") String id) {

		Question dummy = dataService.getQuestion(id); // replace with some backend stuff

		if (dummy == null) {
			return Response.status(Status.NOT_FOUND).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
		}

		addResourceURL(dummy);

		return Response.ok(dummy).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

	@GET
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response listQuestions() {
		Collection<Question> questions = dataService.getQuestions();

		questions.forEach(q -> addResourceURL(q));

		return Response.ok(questions).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

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
	}

}
