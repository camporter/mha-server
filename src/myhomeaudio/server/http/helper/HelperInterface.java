package myhomeaudio.server.http.helper;

/**
 * Helper Interface defines the basic operations of each helper
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
	public String getOutput(String uri, String data);
}
