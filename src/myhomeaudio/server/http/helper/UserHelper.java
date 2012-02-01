/**
 * 
 */
package myhomeaudio.server.http.helper;

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.http.HttpStatus;
import com.google.gson.Gson;

import myhomeaudio.server.http.StatusCode;
import myhomeaudio.server.manager.UserManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.user.User;

/**
 * @author grimmjow
 * 
 */
public class UserHelper extends Helper implements HelperInterface, NodeCommands, StatusCode {

	@Override
	public String getOutput(String uri, String data) {
		String body = "{\"status\":" + STATUS_FAILED + "}";

		StringTokenizer tokenizedUri = new StringTokenizer(uri, "/");
		tokenizedUri.nextToken(); // throw the first part away

		if (tokenizedUri.hasMoreTokens()) {

			UserManager um = UserManager.getInstance();

			String method = tokenizedUri.nextToken();

			Gson gson = new Gson();
			Hashtable hasht = gson.fromJson(data, Hashtable.class);

			if (hasht == null) {
				// We don't have any data, go ahead and fail
				this.httpStatus = HttpStatus.SC_BAD_REQUEST;
			} else if (method.equals("register")) {
				// Register a new user
				if (hasht.containsKey("username") && hasht.containsKey("password")) {
					User newUser = new User((String) hasht.get("username"),
							(String) hasht.get("password"));

					int result = um.registerUser(newUser);

					body = "{\"status\":" + result + "}";
				}
				this.httpStatus = HttpStatus.SC_OK;
			} else {
				this.httpStatus = HttpStatus.SC_BAD_REQUEST;
			}
		}
		return body;
	}
}
