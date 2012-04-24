package myhomeaudio.server.media.descriptor;

import java.io.File;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Acts as a container for all the media included on the system. Typically, a
 * Source will generate them and then they will be passed off to the
 * ClientHandler and NodeManager.
 * 
 * @author cameron
 * 
 */
public class MediaDescriptor implements JSONAware {

	private final int id;
	private final String title;
	private final String artist;
	private final String album;
	private final String genre;
	private final String location;
	private final boolean isInternetLocation;
	private final long durationInSeconds;

	public MediaDescriptor(int id, String title, String artist, String album,
			String genre, String location, boolean isInternetLocation, long durationInSeconds) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.location = location;
		this.isInternetLocation = isInternetLocation;
		this.durationInSeconds = durationInSeconds;
	}

	public MediaDescriptor(int id) {
		this.id = id;
		this.title = null;
		this.artist = null;
		this.album = null;
		this.genre = null;
		this.location = null;
		this.isInternetLocation = false;
		this.durationInSeconds = -1;
	}

	public MediaDescriptor(MediaDescriptor descriptor) {
		this.id = descriptor.id();
		this.title = descriptor.title();
		this.artist = descriptor.artist();
		this.album = descriptor.album();
		this.genre = descriptor.genre();
		this.location = descriptor.location();
		this.isInternetLocation = descriptor.isInternetLocation();
		this.durationInSeconds = descriptor.getDuration();
	}

	public int id() {
		return id;
	}

	public String title() {
		return title;
	}

	public String artist() {
		return artist;
	}

	public String album() {
		return album;
	}

	public String genre() {
		return genre;
	}

	public String location() {
		return location;
	}
	
	public boolean isInternetLocation() {
		return isInternetLocation;
	}
	
	public long getDuration() {
		return durationInSeconds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediaDescriptor [id=" + id + ", title=" + title + ", artist="
				+ artist + ", album=" + album + ", genre=" + genre
				+ ", location=" + location + "]";
	}

	@Override
	public String toJSONString() {		
		JSONObject obj = new JSONObject();
		obj.put("title", title);
		obj.put("artist", artist);
		obj.put("album", album);
		obj.put("genre", genre);
		obj.put("location", location);
		obj.put("duration", durationInSeconds);
		return obj.toString();
	}
}
