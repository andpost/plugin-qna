package com.andreaspost.pugin.qna.interceptors;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * Provides logging on method level. Logs entry, exit and exceptions.<br />
 * <br />
 * Method entry and exit require at least {@link Level#FINER}.
 * 
 * @author Andreas Post
 */
@Provider
@PreMatching
public class MethodLoggingInterceptor {

	private final static Logger LOG = Logger.getLogger(MethodLoggingInterceptor.class.getName());

	/**
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object intercept(InvocationContext context) throws Exception {
		logMethodEntry(context);

		try {
			Object result = context.proceed();
			logMethodResult(context, result);
			return result;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Log method entry.
	 * 
	 * @param context
	 */
	private void logMethodEntry(InvocationContext context) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.entering(context.getMethod().getDeclaringClass().getName(), //
					context.getMethod().getName(), context.getParameters());
		}
	}

	/**
	 * Log method result.
	 * 
	 * @param context
	 * @param result
	 */
	private void logMethodResult(InvocationContext context, Object result) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.exiting(context.getMethod().getDeclaringClass().getName(), //
					context.getMethod().getName(), result);
		}
	}
}
