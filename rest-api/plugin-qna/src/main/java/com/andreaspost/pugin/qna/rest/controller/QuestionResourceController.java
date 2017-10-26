package com.andreaspost.pugin.qna.rest.controller;

import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.andreaspost.pugin.qna.rest.Constants;
import com.andreaspost.pugin.qna.rest.resource.Question;

/**
 * Resource controller for {@link Question} resources.
 * 
 * @author Andreas Post
 */
@Path(Constants.QUESTION_RESOURCE_PATH)
public class QuestionResourceController {

	@GET
	@Path("{id}")
	@Produces(Constants.MEDIA_TYPE_JSON)
	public Response getQuestion(@PathParam("id") String id) {
		Question dummy = new Question("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam?",
				LocalDateTime.now(), "admin");

		return Response.ok(dummy).header(Constants.CONTENT_ENC_KEY, Constants.CHARSET_UTF8).build();
	}

}
