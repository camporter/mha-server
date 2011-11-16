package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Date;

import myhomeaudio.server.http.HTTPStatus;

public class Helper implements HelperInterface, HTTPStatus {
	
	protected String uri = "";
	protected String data = "";
	
	@Override
	public String getOutput() {
		// The default helper produces no output
		return "";
	}

	@Override
	public void setData(String uri, String data) {
		this.uri = uri;
		this.data = data;
		
	}

	public int getStatusCode() {
		return HTTPStatus.HTTP_NOT_FOUND;
	}
}
