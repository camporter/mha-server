package myhomeaudio.server.http.helper;

import java.util.ArrayList;

/**
 * Defines the basic operations of a helper.
 * 
 * @author Cameron
 * 
 */
public interface HelperInterface {

	/**
	 * Gets the helper's final HTTP output after it has been created.
	 * 
	 * @return The HTTP output to be sent back to the client.
	 */
	public String getOutput(ArrayList<String> uriSegments, String data);
}
