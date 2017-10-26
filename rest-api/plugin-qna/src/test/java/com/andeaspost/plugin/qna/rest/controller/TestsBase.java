package com.andeaspost.plugin.qna.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Before;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ConnectionConfig;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;

/**
 * Abstract base class for several tests.
 * 
 * @author Andreas Post
 */
public abstract class TestsBase {

	private static final Logger LOG = Logger.getLogger(TestsBase.class.getName());

	protected static final String CONTENT_TYPE = MediaType.APPLICATION_JSON + "; " //
			+ MediaType.CHARSET_PARAMETER + "=" + StandardCharsets.UTF_8;

	protected static final String BASE_PATH = "plugin-qna/rest";

	protected Headers headers;

	@Before
	public void init() throws MalformedURLException {
		List<Header> headerList = new ArrayList<Header>();
		headerList = new ArrayList<Header>();
		headerList.add(new Header(HttpHeaders.ACCEPT_LANGUAGE, "en"));

		headers = new Headers(headerList);

		Properties props = loadProperties("test-config.properties");
		String hostProperty = (String) props.get("test.hostname");
		String portProperty = (String) props.get("test.port");

		URL baseURL = new URL("http://" + hostProperty + ":" + portProperty + "/" + BASE_PATH);

		RestAssured.baseURI = String.format("%s://%s:%s", baseURL.getProtocol(), baseURL.getHost(), baseURL.getPort());
		RestAssured.port = baseURL.getPort();
		RestAssured.basePath = BASE_PATH;
		RestAssured.config = RestAssured.config()
				.connectionConfig(new ConnectionConfig().closeIdleConnectionsAfterEachResponse());
	}

	/**
	 * Load props for test.
	 * 
	 * @param propertyRessource
	 * @return
	 */
	protected Properties loadProperties(final String propertyRessource) {

		if (propertyRessource == null) {
			throw new IllegalArgumentException("Missing propertyRessource name.");
		}

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		Properties result = null;
		InputStream is = null;

		try {
			is = classLoader.getResourceAsStream(propertyRessource);

			if (is != null) {
				result = new Properties();
				result.load(is);
			} else {
				throw new IllegalArgumentException("File not found: " + propertyRessource);
			}
		} catch (IOException e) {
			result = null;
			throw new IllegalStateException("Error accessing file: " + propertyRessource);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Could not close stream", e);
				}
			}
		}
		return result;
	}
}
