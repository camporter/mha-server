package myhomeaudio.server.helper;

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
	public String getOutput();

	/**
	 * Sets the URI and HTTP body that the helper needs to do generate an
	 * output.
	 * 
	 * @param uri
	 *            URI from the client
	 * @param body
	 *            Body of the HTTP request being sent
	 */
	public void setData(String uri, String body);
}
