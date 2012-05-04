package myhomeaudio.server.http.helper.node;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import myhomeaudio.node.Configuration;
import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.http.helper.ByteHelper;
import myhomeaudio.server.http.helper.Helper;
import myhomeaudio.server.http.helper.HelperInterface;
import myhomeaudio.server.node.NodeCommands;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

public class PauseHelper extends ByteHelper implements HelperInterface,
		NodeCommands, StatusCode {

	public String getOutput(ArrayList<String> uriSegments, byte[] data) {

		// Set the content-type
		this.contentType = HTTPMimeType.MIME_JSON;
		this.httpStatus = HttpStatus.SC_BAD_REQUEST;

		// Create the JSON object to represent the response
		JSONObject body = new JSONObject();
		body.put("status", STATUS_FAILED);

		try {
			
			System.out.println("Pausing song...");
			Process child2 = Runtime.getRuntime().exec("killall mpg123");
			
			body.put("status", STATUS_OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.httpStatus = HttpStatus.SC_OK;

		return body.toString();
	}
}
