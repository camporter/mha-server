package myhomeaudio.server.http.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import myhomeaudio.server.http.HTTPMimeType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.Header;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

/**
 * A Helper takes care of generating a response to send back to the client when
 * a client request is sent to the server.
 * <p>
 * Helpers are registered to specific URIs in the ClientHandler. Each Helper
 * takes care of a specific kind of request that might be received.
 * 
 * @author Cameron
 * 
 */
public class Helper implements HelperInterface, HttpRequestHandler, HTTPMimeType {

	// The HTTP status code to send back to the client
	protected int httpStatus = HttpStatus.SC_OK;
	protected String contentType = MIME_TEXT;

	public String getOutput(ArrayList<String> uriSegments, String data) {
		// The default helper produces no output
		this.httpStatus = HttpStatus.SC_FORBIDDEN;
		return "";
	}

	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {

		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		
		// Check the HTTP method type
		if (!method.equals("GET") && !method.equals("POST"))
			throw new MethodNotSupportedException(method + " method not supported");
		
		// Pull out the request data as a string
		String requestData = "";
		if (request instanceof HttpEntityEnclosingRequest) {
			// The request has data (should be POST)
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			requestData = EntityUtils.toString(entity);
		}

		// Get and split up the segments of the request uri
		ArrayList<String> uriSegments = new ArrayList<String>();
		StringTokenizer tokenizedUri = new StringTokenizer(request.getRequestLine().getUri(), "/");

		while (tokenizedUri.hasMoreTokens()) {
			uriSegments.add(tokenizedUri.nextToken().toLowerCase());
		}

		StringEntity body = new StringEntity(this.getOutput(uriSegments, requestData));
		body.setContentType(this.contentType);
		response.setEntity(body);
		response.setStatusCode(this.httpStatus);

	}
}
