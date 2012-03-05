package myhomeaudio.server.media.descriptor;

/**
 * Describes media that is byte-represented (and stored in this class).
 * 
 * TODO: We eventually need to care about what the data's format is.
 * 
 * @author Cameron
 * 
 */
public class ByteMediaDescriptor extends MediaDescriptor {

	private final byte[] mediaData;

	public ByteMediaDescriptor(int id, byte[] data) {
		super(id);
		this.mediaData = data;
	}

	public ByteMediaDescriptor(int id, byte[] data, String title,
			String artist, String album, String genre) {
		super(id, title, artist, album, genre);
		this.mediaData = data;
	}

	/**
	 * Returns the byte array of the media's data.
	 * 
	 * @return Data as a byte array.
	 */
	public byte[] getData() {
		return this.mediaData;
	}
}
