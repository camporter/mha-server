package myhomeaudio.server.helper;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import myhomeaudio.server.node.NodeCommands;

/* Filename: NodeHelper.java
 * 
 * Helps ClientWorker.java handle client requests concerning nodes
 * 
 * Requests
 * -server/node/Stream
 *  	>GET, return stream interface of what is playing in the room
 * 
 * 
 */

public class NodeHelper extends Helper implements HelperInterface, NodeCommands {

	public String getOutput() {
		String output = "";

		return output;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext context)
			throws HttpException, IOException {

		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
		if (!method.equals("GET") && !method.equals("POST")) {
			throw new MethodNotSupportedException(method + " method not supported");
		}
		String requestData = "";
		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
			requestData = EntityUtils.toString(entity);
		}

		String uri = request.getRequestLine().getUri();
		StringEntity body = new StringEntity(this.getOutput(uri, requestData));
		response.setEntity(body);
		response.setStatusCode(this.statusCode);

	}

}
