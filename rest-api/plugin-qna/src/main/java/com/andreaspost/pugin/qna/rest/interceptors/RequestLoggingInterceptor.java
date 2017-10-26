package com.andreaspost.pugin.qna.rest.interceptors;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * Provide some automatic logging of requests.
 * 
 * @author Andreas Post
 */
@Provider
@PreMatching
public class RequestLoggingInterceptor implements ContainerRequestFilter {

	private final static Logger LOG = Logger.getLogger(RequestLoggingInterceptor.class.getName());

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOG.log(Level.INFO, "Incoming request: " + requestContext.getUriInfo().getRequestUri().toString());

		// TODO: some more logging?
	}

}
