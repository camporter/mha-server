package myhomeaudio.node;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

import myhomeaudio.server.discovery.DiscoverySearch;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.util.EntityUtils;

//Client side
//TODO create multiple clients automatically with threads

public class NodeClient {

	protected static int serverPort = 9090;
	protected static int nodePort = 9091;
	protected static String host = "";

	public static void main(String[] args) {
		System.out.println("Starting...");

		String nodeName = "cams-lappy-0";

		if (args.length > 0) {
			nodeName = args[0];
		} else {
			System.err.println("A Node name must be provided!");
			// System.exit(1);
		}
		
		// Do the server discovery 
		ServerDiscovery sd = new ServerDiscovery();
		if (!sd.doDiscovery()) {
			System.err.println("Unable to discover server.");
			//System.exit(1);
		}
		
		//host = sd.getAddress().getHostAddress();
		serverPort = sd.getNodePort();
		
		try {
			ServerSocket nodeSocket = new ServerSocket(NodeClient.nodePort);

			HttpParams params = new SyncBasicHttpParams();
			params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024);
			params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
			params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
			params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

			HttpProcessor httpProc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
					new ResponseDate(), new ResponseServer(), new ResponseContent(),
					new ResponseConnControl(), });

			HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
			registry.register("play", new PlayRequestHandler());
			registry.register("pause", new PauseRequestHandler());
			registry.register("name", new NameRequestHandler(nodeName));

			HttpService httpService = new HttpService(httpProc,
					new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory(),
					registry, params);

			while (true) {
				Socket serverSocket = nodeSocket.accept();
				DefaultHttpServerConnection connection = new DefaultHttpServerConnection();
				System.out.println("Incoming connection from " + serverSocket.getInetAddress());
				connection.bind(serverSocket, params);

				HttpContext context = new BasicHttpContext(null);
				try {
					while (connection.isOpen()) {
						httpService.handleRequest(connection, context);
					}
				} catch (ConnectionClosedException e) {
					System.err.println("Connection closed");
				} catch (HttpException e) {
					System.err.println("HTTP violation " + e.getMessage());
				} finally {
					connection.shutdown();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static class PlayRequestHandler implements HttpRequestHandler {

		public void handle(final HttpRequest request, final HttpResponse response,
				final HttpContext context) throws HttpException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET") && !method.equals("POST")) {
				// Method is not GET or POST
				throw new MethodNotSupportedException(method + " method not supported.");
			}
			System.out.println("Playing song...");
			String target = request.getRequestLine().getUri();
			if (request instanceof HttpEntityEnclosingRequest) {
				// Request has data, so we save it
				HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
				byte[] entityContent = EntityUtils.toByteArray(entity);

				FileOutputStream outFile = new FileOutputStream("song.mp3");
				outFile.write(entityContent);
				outFile.close();
				String command2 = "killall mplayer";
				Process child2 = Runtime.getRuntime().exec(command2);
				String command = "mplayer song.mp3";
				Process child = Runtime.getRuntime().exec(command);

			}
		}
	}

	static class PauseRequestHandler implements HttpRequestHandler {

		public void handle(final HttpRequest request, final HttpResponse response,
				final HttpContext context) throws HttpException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET")) {
				// Method is not a GET
				throw new MethodNotSupportedException(method + " method not supported.");
			}

			System.out.println("Pausing song...");

			response.setStatusCode(HttpStatus.SC_OK);

		}
	}

	static class NameRequestHandler implements HttpRequestHandler {

		private String nodeName;

		public NameRequestHandler(String nodeName) {
			this.nodeName = nodeName;
		}

		public void handle(final HttpRequest request, final HttpResponse response,
				final HttpContext context) throws HttpException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET")) {
				// Method is not a GET
				throw new MethodNotSupportedException(method + " method not supported.");
			}

			System.out.println("Sending name...");
			// Send the name of the node as data
			response.setEntity(new StringEntity(this.nodeName));
			response.setStatusCode(HttpStatus.SC_OK);

		}
	}
}
