package myhomeaudio.server.media.descriptor;

/**
 * Describes media that is at a certain location (such as a URL).
 * 
 * TODO: We need to use a more specific class for the location instead of a
 * String.
 * 
 * @author Cameron
 * 
 */
public class LocationMediaDescriptor extends MediaDescriptor {

	private final String location;

	public LocationMediaDescriptor(int id, String location) {
		super(id);
		this.location = location;
	}

	public LocationMediaDescriptor(int id, String location, String title, String artist,
			String album, String genre) {
		super(id, title, artist, album, genre);
		this.location = location;
	}
	
	/**
	 * Returns the media's location.
	 * 
	 * @return URL as a string.
	 */
	public String getLocation() {
		return this.location;
	}

}
