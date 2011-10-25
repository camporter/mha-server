/**
 * 
 */
package myhomeaudio.server.implement;

import myhomeaudio.server.interfaces.PrefsInterface;
import myhomeaudio.server.interfaces.UserInterface;

/**
 * @author Ryan Brown
 *
 */
public class UserImpl implements UserInterface {

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.UserInterface#getPrefs()
	 */
	@Override
	public PrefsInterface getPrefs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.UserInterface#getUsername()
	 */
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.UserInterface#isSuperUser()
	 */
	@Override
	public boolean isSuperUser() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see myhomeaudio.server.interfaces.UserInterface#setPrefs(myhomeaudio.server.interfaces.PrefsInterface)
	 */
	@Override
	public boolean setPrefs(PrefsInterface userPrefs) {
		// TODO Auto-generated method stub
		return false;
	}

}
