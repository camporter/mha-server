package myhomeaudio.server.http.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class ByteHelper extends Helper {

	public String getOutput(ArrayList<String> uriSegments, byte[] data) {
		// The default helper produces no output
		this.httpStatus = HttpStatus.SC_FORBIDDEN;
		return "";
	}
	
	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {

		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		
		// Check the HTTP method type
		if (!method.equals("GET") && !method.equals("POST"))
			throw new MethodNotSupportedException(method + " method not supported");
		
		// Pull out the request data as a string
		byte[] requestData = null;
		if (request instanceof HttpEntityEnclosingRequest) {
			// The request has data (should be POST)
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			requestData = EntityUtils.toByteArray(entity);
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
