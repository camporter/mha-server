package myhomeaudio.server.handler;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

import myhomeaudio.server.helper.ClientHelper;
import myhomeaudio.server.helper.NodeHelper;
import myhomeaudio.server.helper.SongHelper;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
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

public class ClientHandler extends Thread {

	// Port which the ClientHandler will listen on
	private int clientListenPort;

	private ServerSocket serverSocket;

	private final HttpParams httpParameters;
	private final HttpService httpService;

	public ClientHandler(int port) {
		this.setName("ClientHandler");
		this.clientListenPort = port;

		// Setup our HTTP parameters
		this.httpParameters = new SyncBasicHttpParams();
		this.httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
				.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
				.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
				.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
				.setParameter(CoreProtocolPNames.ORIGIN_SERVER, "MyHomeAudio");

		// Create an interceptor array that will handle the response
		HttpResponseInterceptor[] httpResponseInter = new HttpResponseInterceptor[] {
				new ResponseDate(), new ResponseServer(), new ResponseContent(),
				new ResponseConnControl() };

		// Create a new HttpProcessor, pass it our interceptors
		HttpProcessor httpProcessor = new ImmutableHttpProcessor(httpResponseInter);

		// Create registry that stores key used to process request URI
		HttpRequestHandlerRegistry httpRequestRegistry = new HttpRequestHandlerRegistry();
		httpRequestRegistry.register("/song*", new SongHelper());
		httpRequestRegistry.register("/node*", new NodeHelper());
		httpRequestRegistry.register("/client*", new ClientHelper());
		// TODO: Add other helpers to the request registry

		this.httpService = new HttpService(httpProcessor, new DefaultConnectionReuseStrategy(),
				new DefaultHttpResponseFactory(), httpRequestRegistry, this.httpParameters);
	}

	public void run() {

		// Open the port we want to listen on
		try {
			this.serverSocket = new ServerSocket(this.clientListenPort);
		} catch (IOException e) {
			System.err.println("Unable ClientHandler unable to bind to port "
					+ this.clientListenPort);
		}

		while (!Thread.interrupted()) {
			try {
				Socket socket = this.serverSocket.accept();
				DefaultHttpServerConnection httpConnection = new DefaultHttpServerConnection();
				httpConnection.bind(socket, this.httpParameters);

				Thread worker = new WorkerThread(this.httpService, httpConnection);
				worker.start();
			} catch (InterruptedIOException e) {
				break;
			} catch (IOException e) {
				System.err.println("I/O error initializing connection thread: " + e.getMessage());
				break;
			}
		}

	}

	static class WorkerThread extends Thread {

		private final HttpService httpService;
		private final HttpServerConnection httpConnection;

		public WorkerThread(final HttpService httpService, final HttpServerConnection httpConnection) {
			super();
			this.httpService = httpService;
			this.httpConnection = httpConnection;
		}

		public void run() {
			HttpContext httpContext = new BasicHttpContext(null);
			try {
				while (!Thread.interrupted() && this.httpConnection.isOpen()) {
					this.httpService.handleRequest(httpConnection, httpContext);
				}
			} catch (ConnectionClosedException ex) {
				System.err.println("Client closed connection");
			} catch (IOException ex) {
				System.err.println("I/O error: " + ex.getMessage());
			} catch (HttpException ex) {
				System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
			} finally {
				try {
					this.httpConnection.shutdown();
				} catch (IOException ignore) {
				}
			}
		}
	}
}
