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

/**
 * @author grimmjow
 * 
 */
public class UserHelper extends Helper implements HelperInterface, NodeCommands {

	@Override
	public String getOutput(String uri, String data) {
		String body = "";

		StringTokenizer tokenizedUri = new StringTokenizer(uri, "/");
		tokenizedUri.nextToken(); // throw the first part away

		if (tokenizedUri.hasMoreTokens()) {

			UserManager um = UserManager.getInstance();

			String method = tokenizedUri.nextToken();
			if (method.equals("login")) {
				// Login the user
				Gson gson = new Gson();
				Hashtable hasht = gson.fromJson(data.trim(), Hashtable.class);
				
				if (hasht != null && hasht.containsKey("username") && hasht.containsKey("password")) {
					
				}
				
				this.statusCode = HttpStatus.SC_OK;

			} else if (method.equals("logout")) {

			} else if (method.equals("register")) {

			}

		} else {

		}
		return body;
	}
}
