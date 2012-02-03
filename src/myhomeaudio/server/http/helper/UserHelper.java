/**
 * 
 */
package myhomeaudio.server.http.helper;

import java.util.ArrayList;

import myhomeaudio.server.http.HTTPMimeType;
import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.manager.UserManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.user.User;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author grimmjow
 * 
 */
public class UserHelper extends Helper implements HelperInterface, NodeCommands, StatusCode {

	@Override
	public String getOutput(ArrayList<String> uriSegments, String data) {

		// Set the content-type
		this.contentType = HTTPMimeType.MIME_JSON;
		this.httpStatus = HttpStatus.SC_BAD_REQUEST;

		// Create the JSON object to represent the response
		JSONObject body = new JSONObject();
		body.put("status", STATUS_FAILED);

		UserManager um = UserManager.getInstance();

		try {
			// Convert the request into a JSON object
			JSONObject jsonRequest = (JSONObject) JSONValue.parse(data);

			if (uriSegments.get(1).equals("register")) {
				// Register a new user
				if (jsonRequest.containsKey("username") && jsonRequest.containsKey("password")) {
					User newUser = new User((String) jsonRequest.get("username"),
							(String) jsonRequest.get("password"));

					int result = um.registerUser(newUser);

					body.put("status", result);
				}
				this.httpStatus = HttpStatus.SC_OK;
			}

		} catch (Exception e) {

		}

		return body.toString();
	}
}
