package com.andreaspost.pugin.qna.rest.controller;

import java.text.MessageFormat;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.andreaspost.pugin.qna.rest.resource.BaseResource;

/**
 * Abstract base class for resource controllers.
 * 
 * @author Andreas Post
 */
abstract class AbstractResourceController<R extends BaseResource> {

	private static final String PAGINATION_PATTERN = "{0}?offset={1}&limit={2}";

	private static final int LIMIT_DEFAULT = 25;

	@Context
	private UriInfo uriInfo;

	protected UriInfo getUriInfo() {
		return uriInfo;
	}

	/**
	 * Adds response headers for pagination.
	 * 
	 * @param responseBuilder
	 *            The {@link ResponseBuilder} of the current {@link Response}.
	 * @param count
	 *            The total amount of entries.
	 * @param limit
	 *            The given result limit.
	 * @param offset
	 *            The current offset.
	 */
	protected void addPaginationHeaders(ResponseBuilder responseBuilder, int count, int limit, int offset) {

		responseBuilder.header("X-Total-Count", count);

		if (limit < count) {
			String baseResourceUriString = getUriInfo().getBaseUri().toString() + getResourcePath();

			addNextLink(responseBuilder, baseResourceUriString, count, limit, offset);
			addLastLink(responseBuilder, baseResourceUriString, count, limit, offset);
			responseBuilder.link(MessageFormat.format(PAGINATION_PATTERN, baseResourceUriString, 0, limit), "first");
			addPrevLink(responseBuilder, baseResourceUriString, count, limit, offset);
		}
	}

	/**
	 * Add the self url for the given resource.
	 * 
	 * @param resource
	 */
	protected void addResourceURL(R resource) {
		resource.setHref(getUriInfo().getBaseUri().toString() + getResourcePath() + resource.getId());
	}

	protected int getDefaultLimit() {
		return LIMIT_DEFAULT;
	}

	/**
	 * Returns the primitive int value or {@link #getDefaultLimit()} if NULL.
	 * 
	 * @param limit
	 * @return
	 */
	protected int getLimit(Integer limit) {
		if (limit != null) {
			return limit.intValue();
		}
		return getDefaultLimit();
	}

	/**
	 * Returns the primitive int value or 0 if NULL.
	 * 
	 * @param offset
	 * @return
	 */
	protected int getOffset(Integer offset) {
		if (offset != null) {
			return offset.intValue();
		}
		return 0;
	}

	/**
	 * Returns the path to the resource, i.e. something like '/resource-name/'.
	 * 
	 * @return
	 */
	abstract String getResourcePath();

	/**
	 * Creates a rel=next header link if necessary and adds to the given {@link ResponseBuilder}.
	 * 
	 * @param responseBuilder
	 *            The {@link ResponseBuilder} of the current {@link Response}.
	 * @param baseResourceUriString
	 *            The base URI of the resource.
	 * @param count
	 *            The total amount of entries.
	 * @param limit
	 *            The given result limit.
	 * @param currentOffset
	 *            The current offset.
	 */
	private void addNextLink(ResponseBuilder responseBuilder, String baseResourceUriString, int count, int limit, int currentOffset) {
		if (currentOffset + limit < count) {
			int newOffset = currentOffset + limit;

			responseBuilder.link(MessageFormat.format(PAGINATION_PATTERN, baseResourceUriString, newOffset, limit), "next");
		}
	}

	/**
	 * Creates a rel=last header link if necessary and adds to the given {@link ResponseBuilder}.
	 * 
	 * @param responseBuilder
	 *            The {@link ResponseBuilder} of the current {@link Response}.
	 * @param baseResourceUriString
	 *            The base URI of the resource.
	 * @param count
	 *            The total amount of entries.
	 * @param limit
	 *            The given result limit.
	 * @param currentOffset
	 *            The current offset.
	 */
	private void addLastLink(ResponseBuilder responseBuilder, String baseResourceUriString, int count, int limit, int currentOffset) {
		if (currentOffset + limit < count) {
			int newOffset = (count / limit) * limit;

			responseBuilder.link(MessageFormat.format(PAGINATION_PATTERN, baseResourceUriString, newOffset, limit), "last");
		}
	}

	/**
	 * Creates a rel=prev header link if necessary and adds to the given {@link ResponseBuilder}.
	 * 
	 * @param responseBuilder
	 *            The {@link ResponseBuilder} of the current {@link Response}.
	 * @param baseResourceUriString
	 *            The base URI of the resource.
	 * @param count
	 *            The total amount of entries.
	 * @param limit
	 *            The given result limit.
	 * @param currentOffset
	 *            The current offset.
	 */
	private void addPrevLink(ResponseBuilder responseBuilder, String baseResourceUriString, int count, int limit, int currentOffset) {
		if (currentOffset - limit >= 0) {
			int newOffset = currentOffset - limit;

			responseBuilder.link(MessageFormat.format(PAGINATION_PATTERN, baseResourceUriString, newOffset, limit), "prev");
		}
	}
}
