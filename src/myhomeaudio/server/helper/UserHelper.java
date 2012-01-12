/**
 * 
 */
package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Hashtable;
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

import com.google.gson.Gson;

import myhomeaudio.server.manager.UserManager;
import myhomeaudio.server.node.NodeCommands;
import myhomeaudio.server.user.User;

/**
 * @author grimmjow
 * 
 */
public class UserHelper extends Helper implements HelperInterface, NodeCommands {

	@Override
	public String getOutput(String uri, String data) {
		String body = "\"failed\"";

		StringTokenizer tokenizedUri = new StringTokenizer(uri, "/");
		tokenizedUri.nextToken(); // throw the first part away

		if (tokenizedUri.hasMoreTokens()) {

			UserManager um = UserManager.getInstance();

			String method = tokenizedUri.nextToken();

			Gson gson = new Gson();
			Hashtable hasht = gson.fromJson(data, Hashtable.class);

			if (hasht == null) {
				// We don't have any data, go ahead and fail
				body = "\"failed\"";
			} else if (method.equals("login")) {
				// Login the user
				if (hasht.containsKey("username") && hasht.containsKey("password")) {
					User loginUser = new User((String) hasht.get("username"),
							(String) hasht.get("password"));

					int result = um.loginUser(loginUser);
					switch (result) {
					case UserManager.LOGIN_OK:
						body = "\"ok\"";
						break;
					case UserManager.LOGIN_FAILED:
						body = "\"failed\"";
						break;
					}
				}
				this.statusCode = HttpStatus.SC_OK;

			} else if (method.equals("logout")) {
				// Logout the user
				if (hasht.containsKey("username") && hasht.containsKey("password")) {
					User logoutUser = new User((String) hasht.get("username"),
							(String) hasht.get("password"));

					int result = um.logoutUser(logoutUser);
					switch (result) {
					case UserManager.LOGOUT_OK:
						body = "\"ok\"";
						break;
					case UserManager.LOGOUT_FAILED:
						body = "\"failed\"";
						break;
					}
				}
				this.statusCode = HttpStatus.SC_OK;

			} else if (method.equals("register")) {
				// Register a new user
				if (hasht.containsKey("username") && hasht.containsKey("password")) {
					User newUser = new User((String) hasht.get("username"),
							(String) hasht.get("password"));

					int result = um.registerUser(newUser);
					switch (result) {
					case UserManager.REGISTER_OK:
						body = "\"ok\"";
						break;
					case UserManager.REGISTER_BAD_PASSWORD:
						body = "\"bad password\"";
						break;
					case UserManager.REGISTER_DUPLICATE_USERNAME:
						body = "\"duplicate\"";
						break;
					case UserManager.REGISTER_FAILED:
						body = "\"failed\"";
						break;
					}
				}
				this.statusCode = HttpStatus.SC_OK;
			} else {
			}
		}
		return body;
	}
}
