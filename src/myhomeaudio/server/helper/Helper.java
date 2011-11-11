package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Date;

import myhomeaudio.server.http.HTTPStatus;

public class Helper implements HelperInterface, HTTPStatus {
	
	protected String uri = "";
	protected String body = "";
	
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
