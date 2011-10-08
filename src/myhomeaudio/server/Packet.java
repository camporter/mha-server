package myhomeaudio.server;

import javax.sound.sampled.AudioFormat;

public class Packet {
	// Audio file information
	private AudioFormat format;
	private Boolean endian;
	private int channel = 1; // 1,2
	private AudioFormat.Encoding encode;
	private float frameRate;
	private int frameSize;
	private float sampleRate; // 8000,11025,16000,22050,44100
	private int sampleSizeInBits; // 8,16

	// Header just contains sequence number, 2 bytes
	static int HEADER_SIZE = 2;
	// Fields that compose the header
	public int sequenceNumber;
	public byte[] header;
	public int payloadSize;
	public byte[] payload;

	/**
	 *  Constructor of an packet object from header fields and payload bytestream
	 * @param Framenb
	 * 		Starting frame number first frame in packet
	 * @param data
	 * 		Data byte stream
	 * @param dataLen
	 * 		Length in bytes of data stream
	 */
	public Packet(int Framenb, byte[] data, int dataLen) {

		// fill changing header fields:
		sequenceNumber = Framenb;

		// build the header bytestream:
		// --------------------------
		header = new byte[HEADER_SIZE];

		header[0] = new Integer(sequenceNumber >> 8).byteValue();
		header[1] = new Integer(sequenceNumber).byteValue();

		// fill the payload bytestream:
		payloadSize = dataLen;
		payload = new byte[dataLen];

		// fill payload array of byte from data
		for (int i = 0; i < dataLen; i++) {
			payload[i] = data[i];
		}

	}
	
	/**
	 * Constructor of an packet object from the packet bytestream
	 * 
	 * @param packet
	 * 		Packet byte stream
	 * @param packet_size
	 * 		Size in bytes of packet
	 * 
	 */
	public Packet(byte[] packet, int packet_size) {
		// check if total packet size is lower than the header size
		if (packet_size >= HEADER_SIZE) {
			// get the header bytestream:
			header = new byte[HEADER_SIZE];
			for (int i = 0; i < HEADER_SIZE; i++) {
				header[i] = packet[i];
			}

			// get the payload bytestream
			payloadSize = packet_size - HEADER_SIZE;
			payload = new byte[payloadSize];
			for (int i = HEADER_SIZE; i < packet_size; i++) {
				payload[i - HEADER_SIZE] = packet[i];
			}

			// interpret the changing fields of the header
			sequenceNumber = unsigned_int(header[1]) + 256 * unsigned_int(header[0]);
		}
	}

	/**
	 * Get the payload bytestream of the packet.
	 * @return data
	 * 		Data byte stream of audio data stored in payload.
	 */
	public byte[] getPayload() {
		
		byte[] data = this.payload.clone();
		
		//for (int i = 0; i < payloadSize; i++) {
		//	data[i] = this.payload[i];
		//}

		return data;
	}

	/**
	 * Get the length of the payload.
	 * 
	 * @return payloadSize
	 * 		Size in bytes of payload
	 */
	public int getPayloadLength() {
		return payloadSize;
	}

	/**
	 * Get the total length of the packet.
	 * 
	 * @return length
	 * 		Size of packet, payload+header
	 */
	public int getLength() {
		return (this.payloadSize + HEADER_SIZE);
	}

	/**
	 * Constructs packet
	 * 
	 * @param packet
	 * 		Byte stream for storing packet data
	 * @return length
	 * 		Size of packet, payload+header
	 */
	public int getPacket(byte[] packet) {
		// construct the packet = header + payload
		for (int i = 0; i < HEADER_SIZE; i++) {
			packet[i] = header[i];
		}
		for (int i = 0; i < payloadSize; i++) {
			packet[i + HEADER_SIZE] = payload[i];
		}

		// return total size of the packet
		return (this.payloadSize + HEADER_SIZE);
	}

	/**
	 * SequenceNumber Getter
	 * 
	 * @return sequenceNumber
	 * 		Sequence of current frame to be sent
	 */
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	/**
	 * Turns 8bit integer to number
	 * 
	 * @param nb
	 * 		Number to convert
	 * 
	 * @return number
	 * 		unsigned value of number
	 */
	static int unsigned_int(int nb) {
		if (nb >= 0) {
			return (nb);
		} else {
			return (256 + nb);
		}
	}

	/**
	 * Format Setter
	 * 
	 * @param format
	 * 		AudioFormat of audio file
	 * 
	 * @return
	 */
	public void setFormat(AudioFormat format) {
		this.format = format;
		this.endian = format.isBigEndian();
		this.channel = format.getChannels();
		this.encode = format.getEncoding();
		this.frameRate = format.getFrameRate();
		this.frameSize = format.getFrameSize();
		this.sampleRate = format.getSampleRate();
		this.sampleSizeInBits = format.getSampleSizeInBits();
	}

	/**
	 * Format Getter
	 * 
	 * @param
	 * 
	 * @return format
	 * 		AudioFormat of audio file
	 */
	public AudioFormat getFormat() {
		return format;
	}

	/**
	 * Endian Setter
	 * 
	 * @param endian
	 * 		Endian value of audio file
	 * 
	 * @return
	 */
	public void setEndian(Boolean endian) {
		this.endian = endian;
	}

	/**
	 * Endian Getter
	 * 
	 * @param
	 * 
	 * @return endian
	 * 		Endian value of audio file
	 */
	public Boolean getEndian() {
		return endian;
	}
	
	/**
	 * Channel Setter
	 * 
	 * @param channel
	 * 		Channel value of audio file
	 * 
	 * @return
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * Channel Getter
	 * 
	 * @param
	 * 
	 * @return channel
	 * 		Channel value of audio file
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * Encode Setter
	 * 
	 * @param encode
	 * 		Encode value of audio file
	 * 
	 * @return
	 */
	public void setEncode(AudioFormat.Encoding encode) {
		this.encode = encode;
	}

	/**
	 * Encode Getter
	 * 
	 * @param
	 * 
	 * @return encode
	 * 		Encode value of audio file
	 */
	public AudioFormat.Encoding getEncode() {
		return encode;
	}

	/**
	 * FrameRate Setter
	 * 
	 * @param frameRate
	 * 		FrameRate value of audio file
	 * 
	 * @return
	 */
	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}

	/**
	 * FrameRate Getter
	 * 
	 * @param
	 * 
	 * @return frameRate
	 * 		FrameRate value of audio file
	 */
	public float getFrameRate() {
		return frameRate;
	}

	/**
	 * FrameSize Setter
	 * 
	 * @param frameSize
	 * 		FrameSize value of audio file
	 * 
	 * @return
	 */
	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	/**
	 * FrameSize Getter
	 * 
	 * @param
	 * 
	 * @return frameSize
	 * 		FrameSize value of audio file
	 */
	public int getFrameSize() {
		return frameSize;
	}

	/**
	 * SampleRate Setter
	 * 
	 * @param sampleRate
	 * 		SampleRate value of audio file
	 * 
	 * @return
	 */
	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * SampleRate Getter
	 * 
	 * @param
	 * 
	 * @return sampleRate
	 * 		SampleRate value of audio file
	 * 
	 */
	public float getSampleRate() {
		return sampleRate;
	}

	/**
	 * SampleSizeInBits Setter
	 * 
	 * @param sampleSizeInBits
	 * 		SampleSizeInBits value of audio file
	 * 
	 * @return
	 */
	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}

	/**
	 * SampleSizeInBits Getter
	 * 
	 * @param
	 * 
	 * @return sampleSizeInBits
	 * 		SampleSizeInBits value of audio file
	 *
	 */
	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}
}
