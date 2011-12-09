package myhomeaudio.server.helper;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class Helper implements HelperInterface, HttpRequestHandler {
	
	// The HTTP status code to send back to the client
	protected int statusCode = 0;
	
	@Override
	public String getOutput(String uri, String data) {
		// The default helper produces no output
		this.statusCode = HttpStatus.SC_FORBIDDEN;
		return "";
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException,
			IOException {
		
		response.setStatusCode(this.statusCode);
		
	}
}
