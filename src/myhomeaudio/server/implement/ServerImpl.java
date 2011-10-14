/**
 * 
 */
package myhomeaudio.server.implement;

import myhomeaudio.server.ClientConnect;
import myhomeaudio.server.interfaces.ServerInterface;

/**
 * @author grimmjow
 *
 */
public class ServerImpl implements ServerInterface {
	protected static int port = 9090;
	protected static String host = "localhost";
	static ClientConnect conn;
	static String msg;
	
	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#findServer()
	 */
	@Override
	public boolean findServer() {
			conn = new ClientConnect(port, host);
			conn.start();
			return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#next()
	 */
	@Override
	public boolean next() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#pause()
	 */
	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#play()
	 */
	@Override
	public boolean play() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#prev()
	 */
	@Override
	public boolean prev() {
		// TODO Auto-generated method stub
		return false;
	}

}
