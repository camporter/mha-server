package myhomeaudio.server.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import myhomeaudio.server.media.descriptor.MediaDescriptor;
import myhomeaudio.server.mp3.ID3v1;
import myhomeaudio.server.mp3.ID3v2;
import myhomeaudio.server.mp3.InvalidDataException;
import myhomeaudio.server.mp3.Mp3File;
import myhomeaudio.server.mp3.UnsupportedTagException;

/**
 * A media source that provides various media files from a specified folder.
 * 
 * @author Cameron
 * 
 */
public class FolderSource implements Source {

	protected String folderLocation;
	protected File folder;

	private String nextLocation;

	protected ArrayList<MediaDescriptor> mediaList;

	/**
	 * Create the FolderSource.
	 * 
	 * @param folderLocation
	 *            Which folder to get media from.
	 * @throws NullPointerException
	 *             folderLocation cannot be null.
	 */
	public FolderSource(String folderLocation) throws NullPointerException {
		if (folderLocation == null) {
			throw new NullPointerException("Folder location cannot be null.");
		} else {
			this.folderLocation = folderLocation;
			this.folder = new File(folderLocation);
		}

		mediaList = new ArrayList<MediaDescriptor>();

		// Find all of the media we can
		getFiles(this.folder);
	}

	private void getFiles(File directory) {
		System.out.println("Folder: " + directory);
		File[] files = directory.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i] == null) {
					// Skip null files
				} else if (files[i].isDirectory() && !files[i].isHidden()) {
					// Directory found, check it for more media files
					getFiles(files[i]);
				} else {
					// File found, see if it's an mp3 that can be in our media
					// list.
					if (files[i].length() < 100000000 && files[i].isFile()
							&& !files[i].isHidden()
							&& files[i].getName().contains(".mp3")) {
						try {
							MediaDescriptor media = parseMediaFile(files[i]
									.getCanonicalPath());
							if (media != null) {
								mediaList.add(media);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Too large!");
					}

				}
			}
		}
		System.out.flush();
		System.gc();
	}

	public ArrayList<MediaDescriptor> getMediaList() {

		ArrayList<MediaDescriptor> mediaList = new ArrayList<MediaDescriptor>();

		for (String mediaFileName : folder.list()) {
			// mediaList.add(mediaFileName);
		}
		return null;
	}

	/**
	 * Loads media file data into a byte array
	 * 
	 * @param mediaFileName
	 *            File of media file to open
	 * @return Byte array of the media data
	 */
	public byte[] getMediaData(String mediaFileName) {
		try {
			File mediaFile = new File(mediaFileName);
			InputStream fileInput = new FileInputStream(mediaFile);
			long length = mediaFile.length();

			byte[] bytes = new byte[(int) length];

			int offset = 0;
			int numRead = 0;
			// Read each individual byte from the file
			while (offset < bytes.length
					&& (numRead = fileInput.read(bytes, offset, bytes.length
							- offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				// Didn't completely read the file.
				// Do nothing for now..
			}

			fileInput.close();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	/**
	 * 
	 * @return The folder location represented as a String.
	 */
	public String getFolderLocation() {
		return folderLocation;
	}

	/**
	 * Generates a MediaDescriptor for a file at the specified location.
	 * 
	 * @param mediaLocation
	 *            The path to the file.
	 * @return A MediaDescriptor is parsing the media file was successful.
	 *         Otherwise, it will return null.
	 */
	public MediaDescriptor parseMediaFile(String mediaLocation) {
		MediaDescriptor descriptor = null;

		// Let's try parsing the file as an mp3
		try {
			Mp3File mp3 = new Mp3File(mediaLocation);

			if (mp3.hasId3v2Tag()) {
				// An ID3v2 tag was found, use it
				ID3v2 tag = mp3.getId3v2Tag();
				descriptor = new MediaDescriptor(-1, tag.getTitle(),
						tag.getArtist(), tag.getAlbum(),
						tag.getGenreDescription(), new byte[0]/*getMediaData(mediaLocation)*/,
						mp3.getLengthInSeconds());
				System.out.println("Title: " + tag.getTitle() + ", Artist: "
						+ tag.getArtist() + ", Genre: "
						+ tag.getGenreDescription() + ", Length: "
						+ mp3.getLengthInSeconds());

			} else if (mp3.hasId3v1Tag()) {
				System.out.println("\t has id3v1!");
				// An ID3v1 tag was found, use it
				ID3v1 tag = mp3.getId3v1Tag();
				descriptor = new MediaDescriptor(-1, tag.getTitle(),
						tag.getArtist(), tag.getAlbum(),
						tag.getGenreDescription(), new byte[0]/*getMediaData(mediaLocation)*/,
						mp3.getLengthInSeconds());
				System.out.println("Title: " + tag.getTitle() + ", Artist: "
						+ tag.getArtist() + ", Genre: "
						+ tag.getGenreDescription() + ", Length: "
						+ mp3.getLengthInSeconds());
			}
		} catch (UnsupportedTagException e) {
			// e.printStackTrace();
		} catch (InvalidDataException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		// TODO: parse the file as other media types here.

		return descriptor;
	}

	@Override
	public MediaDescriptor getMedia(MediaDescriptor descriptor) {
		// TODO Auto-generated method stub
		return null;
	}
}
