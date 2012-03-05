package myhomeaudio.server.media.descriptor;


public class MediaDescriptor {
	
	private final int id;
	private final String title;
	private final String artist;
	private final String album;
	private final String genre;
	private final String location;
	private final byte[] mediaData; 
	
	
	public MediaDescriptor(int id, String title, String artist, String album, String genre, String location) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.location = location;
		this.mediaData = null;
	}
	
	public MediaDescriptor(int id, String title, String artist, String album, String genre, byte[] mediaData) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.mediaData = mediaData;
		this.location = null;
	}
	
	public MediaDescriptor(int id) {
		this.id = id;
		this.title = null;
		this.artist = null;
		this.album = null;
		this.genre = null;
		this.location = null;
		this.mediaData = null;
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
	
	public String location(){
		return location;
	}
	
	public byte[] mediaData(){
		return mediaData;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediaDescriptor [id=" + id + ", title=" + title + ", artist="
				+ artist + ", album=" + album + ", genre=" + genre
				+ ", location=" + location + "]";
	}
}
