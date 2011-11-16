package myhomeaudio.server.helper;

import org.apache.http.HttpStatus;


public class Helper implements HelperInterface {
	
	protected String uri = "";
	protected String data = "";
	protected int statusCode = 0;
	
	@Override
	public String getOutput() {
		// The default helper produces no output
		statusCode = HttpStatus.SC_NOT_FOUND;
		return "";
	}

	@Override
	public void setData(String uri, String data) {
		this.uri = uri;
		this.data = data;
		
	}

	public int getStatusCode() {
		return this.statusCode;
	}
}
