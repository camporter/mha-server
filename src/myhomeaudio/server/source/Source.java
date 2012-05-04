package myhomeaudio.server.source;

import java.util.ArrayList;

import myhomeaudio.server.media.descriptor.MediaDescriptor;


public interface Source {
	
	public int getId();
	public String getName();
	public ArrayList<MediaDescriptor> getMediaList();
	public MediaDescriptor getMedia(MediaDescriptor descriptor);
	public byte[] getData(int descriptorId);
	
}
