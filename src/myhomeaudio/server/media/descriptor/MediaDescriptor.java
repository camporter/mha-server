package myhomeaudio.server.media.descriptor;

/**
 * Acts as a container for all the media included on the system. Typically, a
 * Source will generate them and then they will be passed off to the
 * ClientHandler and NodeManager.
 * 
 * @author cameron
 * 
 */
public class MediaDescriptor {

	private final int id;
	private final String title;
	private final String artist;
	private final String album;
	private final String genre;
	private final String location;
	private final byte[] byteData;

	public MediaDescriptor(int id, String title, String artist, String album,
			String genre, String location) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.location = location;
		this.byteData = null;
	}

	public MediaDescriptor(int id, String title, String artist, String album,
			String genre, byte[] byteData) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.byteData = byteData;
		this.location = null;
	}

	public MediaDescriptor(int id) {
		this.id = id;
		this.title = null;
		this.artist = null;
		this.album = null;
		this.genre = null;
		this.location = null;
		this.byteData = null;
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

	public byte[] byteData() {
		return byteData;
	}

	public boolean isLocationDescriptor() {
		return (location != null) && (!location.trim().equals(""));
	}

	public boolean isByteDescriptor() {
		return (byteData != null);
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
}
