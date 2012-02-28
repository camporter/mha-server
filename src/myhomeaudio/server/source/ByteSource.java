package myhomeaudio.server.source;

import java.util.ArrayList;

import myhomeaudio.server.media.descriptor.ByteMediaDescriptor;
import myhomeaudio.server.media.descriptor.MediaDescriptor;

/**
 * A source that provides 
 * @author Cameron
 *
 */
public class ByteSource extends SourceBase {
	
	public MediaDescriptor getMedia(MediaDescriptor descriptor) {
		return new ByteMediaDescriptor(0, new byte[0]);
}
	
	public ArrayList<MediaDescriptor> searchMedia(String search) {
		return new ArrayList<MediaDescriptor>();
	}
	
}
