/**
 * 
 */
package myhomeaudio.server.implement;

import myhomeaudio.server.interfaces.PrefsInterface;
import myhomeaudio.server.interfaces.StreamInterface;

/**
 * @author Ryan Brown
 * 
 */
public class PrefsImpl implements PrefsInterface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see myhomeaudio.server.interfaces.PrefsInterface#getActiveStream()
	 */
	@Override
	public StreamInterface getActiveStream() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see myhomeaudio.server.interfaces.PrefsInterface#getDefaultStream()
	 */
	@Override
	public StreamInterface getDefaultStream() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.PrefsInterface#setActiveStream(myhomeaudio
	 * .server.interfaces.StreamInterface)
	 */
	@Override
	public boolean setActiveStream(StreamInterface s) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * myhomeaudio.server.interfaces.PrefsInterface#setDefaultStream(myhomeaudio
	 * .server.interfaces.StreamInterface)
	 */
	@Override
	public boolean setDefaultStream(StreamInterface s) {
		// TODO Auto-generated method stub
		return false;
	}

}
