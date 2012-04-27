package myhomeaudio.server.source;

import java.util.ArrayList;

import myhomeaudio.server.media.descriptor.MediaDescriptor;

/**
 * Base source that other sources should extend.
 * 
 * @author Cameron
 *
 */
public class SourceBase implements Source {

	protected int id;
	protected String name;
	
	public SourceBase(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public ArrayList<MediaDescriptor> getMediaList() {
		return null;
	}

	@Override
	public MediaDescriptor getMedia(MediaDescriptor descriptor) {
		return null;
	}

}
