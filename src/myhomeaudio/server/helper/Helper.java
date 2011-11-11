package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Date;

import myhomeaudio.server.http.HTTPStatus;

public class Helper implements HelperInterface, HTTPStatus {
	
	protected String uri = "";
	protected String body = "";
	
	/**
	 * Builds the HTTP header that will be sent back to the client.
	 * 
	 * @param httpStatus
	 *            Status code of the response.
	 * @param hasContent
	 *            Indicates whether content is included in the response.
	 * @param mimeType
	 *            The mime-type of the content being served in the response. Not
	 *            needed if hasContent is false.
	 * @param contentLength
	 *            The size (in bytes) of the content being sent. Not needed if
	 *            hasContent is false.
	 * @return headerString
	 * 			  HTTP header string
	 * @throws IOException
	 */
	protected String buildHeader(int httpStatus, boolean hasContent, String mimeType,
			int contentLength) {
		String headerString = "HTTP/1.0 ";

		switch (httpStatus) {
		case HTTP_OK:
			headerString += HTTP_OK + " OK\r\n";
			break;
		case HTTP_NOT_FOUND:
			headerString += HTTP_NOT_FOUND + " Not Found\r\n";
			break;
		}

		// Add server software name
		headerString += "Server: My Home Audio\r\n";

		// Add the date
		headerString += "Date: " + (new Date()) + "\r\n";

		if (hasContent) {
			// Add the content type
			headerString += "Content-type: " + mimeType + "\r\n";

			// Add the content length
			headerString += "Content-length: " + contentLength + "\r\n";
		}
		
		headerString += "\r\n";
		return headerString;
	}
	
	@Override
	public String getOutput() {
		// The default helper produces no output
		return "";
	}

	@Override
	public void setData(String uri, String body) {
		this.uri = uri;
		this.body = body;
		
	}
}
