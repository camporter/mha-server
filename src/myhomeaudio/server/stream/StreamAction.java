package myhomeaudio.server.stream;

/**
 * Actions that can be done to a stream.
 * 
 * @author Cameron
 *
 */
public interface StreamAction {
	public static final int RESUME = 0;
	public static final int PAUSE = 1;
	public static final int PREVIOUS = 2;
	public static final int NEXT = 3;
}
