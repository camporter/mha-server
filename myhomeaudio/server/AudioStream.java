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
class AudioStream {
	File fileName;
	AudioInputStream audioInputStream;
	int frameNumber; // current frame nb

	private AudioFormat format;
	private long numFrames;
	private int frameSize;

	// constructor
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

	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public void setNumFrames(long numFrames) {
		this.numFrames = numFrames;
	}

	public long getNumFrames() {
		return numFrames;
	}

	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	public int getFrameSize() {
		return frameSize;
	}
}
