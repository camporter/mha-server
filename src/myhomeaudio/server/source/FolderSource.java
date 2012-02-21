package myhomeaudio.server.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A media source used to
 * 
 * @author Cameron
 * 
 */
public class FolderSource extends SourceBase implements Source {

	protected String folderLocation;
	protected File folder;

	/**
	 * 
	 * Create the FolderSource.
	 * 
	 * @param folderLocation Which folder to get media from.
	 * @throws NullPointerException folderLocation cannot be null.
	 */
	public FolderSource(String folderLocation) throws NullPointerException {
		if (folderLocation == null) {
			throw new NullPointerException("Folder location cannot be null.");
		} else {
			this.folderLocation = folderLocation;
			this.folder = new File(folderLocation);
		}
	}
	
	@Override
	public ArrayList<String> getMediaList() {

		ArrayList<String> mediaList = new ArrayList<String>();

		for (String mediaFileName : folder.list()) {
			mediaList.add(mediaFileName);
		}
		return null;
	}
	
	/**
	 * Loads media file data into a byte array
	 * 
	 * @param mediaFileName File of media file to open
	 * @return Byte array of the media data
	 */
	public byte[] getMediaData(String mediaFileName) {
		try {
			File mediaFile = new File(folderLocation + mediaFileName);
			InputStream fileInput = new FileInputStream(mediaFile);
			long length = mediaFile.length();
			
			byte[] bytes = new byte[(int) length];
			
			int offset = 0;
			int numRead = 0;
			// Read each individual byte from the file
			while(offset < bytes.length && (numRead = fileInput.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			
			if (offset < bytes.length) {
				// Didn't completely read the file.
				// Do nothing for now..
			}
			
			fileInput.close();
			return bytes;
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
}
