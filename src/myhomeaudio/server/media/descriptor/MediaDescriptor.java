package myhomeaudio.server.media.descriptor;


public class MediaDescriptor {
	
	private final int id;
	private final String title;
	private final String artist;
	private final String album;
	private final String genre;
	
	public MediaDescriptor(int id, String title, String artist, String album, String genre) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
	}
	
	public MediaDescriptor(int id) {
		this.id = id;
		this.title = null;
		this.artist = null;
		this.album = null;
		this.genre = null;
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
	
}
