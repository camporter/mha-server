package myhomeaudio.server;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Allows audio files to be converted into frames of data for streaming.
 * 
 * @author Ryan Brown
 * 
 */
public class AudioStream {
	File fileName;
	AudioInputStream audioInputStream;
	int frameNumber; // current frame nb

	private AudioFormat format; //format info for audio file
	private long numFrames; //number of frames in audio file
	private int frameSize;//size of frames in bytes

	// constructor
	/* 
	 * @param file Audio file to be placed in frames and streamed
	 * 
	 * @return
	 * 
	 */
	public AudioStream(File file) {

		this.fileName = file;
		this.frameNumber = 0;

		try {
			audioInputStream = AudioSystem.getAudioInputStream(fileName);
		} catch (UnsupportedAudioFileException e) {
			System.out.println("The audio file " + fileName.getName()
					+ " is not supported.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setFormat(audioInputStream.getFormat());
		setNumFrames(audioInputStream.getFrameLength());
		setFrameSize(audioInputStream.getFormat().getFrameSize());
	}

	/* NextFrame Getter
	 * @param frame Byte stream to hold frames
	 * 
	 * @return nBytesRead Number of bytes read from audio input stream
	 * @return frame Byte stream holding audio frame data
	 * 
	 * 
	 */
	public int getNextFrame(byte[] frame) {
		int nBytesRead = 0;
		// byte[] data = new byte[frame.length];
		try {
			nBytesRead = audioInputStream.read(frame, 0, frame.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nBytesRead;
	}

	/* Format Setter
	 * 
	 * @param format Set AudioFormat variable of instance
	 * 
	 * @return
	 * 
	 */
	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	/* Format Getter
	 * 
	 * @param 
	 * 
	 * @return format AudioFormat variable of instance
	 * 
	 */
	public AudioFormat getFormat() {
		return format;
	}

	/* NumFrames Setter
	 * 
	 * @param numFrames Number of frames of audio file
	 * 
	 * @return
	 * 
	 */
	public void setNumFrames(long numFrames) {
		this.numFrames = numFrames;
	}

	/* NumFrames Getter
	 * 
	 * @param 
	 * 
	 * @return numFrames Number of frames of audio file
	 * 
	 */
	public long getNumFrames() {
		return numFrames;
	}
	
	/* FrameSize Setter
	 * 
	 * @param frameSize Size of audio frame
	 * 
	 * @return
	 * 
	 */
	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	/* FrameSize Getter
	 * 
	 * @param 
	 * 
	 * @return frameSize Frame of frame in bytes
	 * 
	 */
	public int getFrameSize() {
		return frameSize;
	}
}
