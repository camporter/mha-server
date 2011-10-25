/**
 * 
 */
package myhomeaudio.server.implement;

import myhomeaudio.server.interfaces.NodeInterface;
import myhomeaudio.server.interfaces.ServerInterface;
import myhomeaudio.server.interfaces.StreamInterface;
import myhomeaudio.server.interfaces.UserInterface;

/**
 * @author Ryan Brown
 *
 */
public class ServerImpl implements ServerInterface {

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#addUser(myhomeaudio.server.interfaces.UserInterface)
	 */
	@Override
	public boolean addUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#connect()
	 */
	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#connect(java.lang.String)
	 */
	@Override
	public boolean connect(String ipAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#connectToNode(java.lang.String)
	 */
	@Override
	public NodeInterface connectToNode(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#disconnect()
	 */
	@Override
	public boolean disconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#getAvailableStreams()
	 */
	@Override
	public StreamInterface[] getAvailableStreams() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#listUsers()
	 */
	@Override
	public UserInterface[] listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public UserInterface login(String username, String deviceID,
			String hashedPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#removeUser(myhomeaudio.server.interfaces.UserInterface)
	 */
	@Override
	public boolean removeUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.ServerInterface#updateUser(myhomeaudio.server.interfaces.UserInterface)
	 */
	@Override
	public boolean updateUser(UserInterface u) {
		// TODO Auto-generated method stub
		return false;
	}

}
