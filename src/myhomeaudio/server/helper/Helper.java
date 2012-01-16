package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

public class Helper implements HelperInterface, HttpRequestHandler {

	// The HTTP status code to send back to the client
	protected int httpStatus = 0;

	@Override
	public String getOutput(String uri, String data) {
		// The default helper produces no output
		this.httpStatus = HttpStatus.SC_FORBIDDEN;
		return "";
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {

		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("GET") && !method.equals("POST")) {
			throw new MethodNotSupportedException(method + " method not supported");
		}
		String requestData = "";
		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			requestData = EntityUtils.toString(entity);
		}

		String uri = request.getRequestLine().getUri();
		StringEntity body = new StringEntity(this.getOutput(uri, requestData));
		response.setEntity(body);
		response.setStatusCode(this.httpStatus);

	}
}
