package myhomeaudio.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.MethodNotSupportedException;
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
	//protected static String host = "192.168.10.101";
	protected static String host = "127.0.0.1";
	
	public static void main(String[] args) {
		System.out.println("Starting...");
		doServerDiscovery();
		try {
			ServerSocket nodeSocket = new ServerSocket(NodeClient.nodePort);
			
			HttpParams params = new SyncBasicHttpParams();
			params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8*1024);
			params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
			params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true);
			params.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");
			
			HttpProcessor httpProc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
					new ResponseDate(),
					new ResponseServer(),
					new ResponseContent(),
					new ResponseConnControl(),
			});
			
			HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
			registry.register("*", new HttpNodeRequestHandler());
			
			HttpService httpService = new HttpService(httpProc, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory(), registry, params);
			
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

	private static void doServerDiscovery() {
		try {
			System.out.println("Discovering server...");
			Socket serverDiscovery = new Socket(NodeClient.host,
					NodeClient.serverPort);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			serverDiscovery.close();
			System.out.println("Server discovery complete.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static class HttpNodeRequestHandler implements HttpRequestHandler {
		public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET") && !method.equals("POST")) {
				// Method is not GET or POST
				throw new MethodNotSupportedException(method + " method not supported.");
			}
			String target = request.getRequestLine().getUri();
			if (request instanceof HttpEntityEnclosingRequest) {
				// Request has data, so we save it
				HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
				byte[] entityContent = EntityUtils.toByteArray(entity);
				
				FileOutputStream outFile = new FileOutputStream("song.mp3");
				outFile.write(entityContent);
				outFile.close();
				
			}
		}
	}
}