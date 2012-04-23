package myhomeaudio.server.source;

import java.util.ArrayList;

import myhomeaudio.server.media.descriptor.MediaDescriptor;


public interface Source {
	
	public ArrayList<MediaDescriptor> getMediaList();
	public MediaDescriptor getMedia(MediaDescriptor descriptor);
	
}
