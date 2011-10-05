package myhomeaudio.server;

import javax.sound.sampled.AudioFormat;

public class Packet {
	// Audio file information
	// TODO Put audio file information in header of packet
	// private AudioFileFormat aformat;
	private AudioFormat format;
	private Boolean endian;
	private int channel = 1;
	// 1,2
	private AudioFormat.Encoding encode;
	private float frameRate;
	private int frameSize;
	private float sampleRate;
	// 8000,11025,16000,22050,44100
	private int sampleSizeInBits;
	// 8,16

	// Header just contains sequence number, 2 bytes
	static int HEADER_SIZE = 2;
	// Fields that compose the RTP header
	public int sequenceNumber;
	public byte[] header;
	public int payloadSize;
	public byte[] payload;

	// Constructor of an RTPpacket object from header fields and payload
	// bitstream
	public Packet(int Framenb, byte[] data, int dataLen) {

		// fill changing header fields:
		sequenceNumber = Framenb;

		// build the header bitstream:
		// --------------------------
		header = new byte[HEADER_SIZE];

		header[0] = new Integer(sequenceNumber >> 8).byteValue();
		header[1] = new Integer(sequenceNumber).byteValue();

		// fill the payload bitstream:
		payloadSize = dataLen;
		payload = new byte[dataLen];

		// fill payload array of byte from data
		for (int i = 0; i < dataLen; i++) {
			payload[i] = data[i];
		}

	}

	// Constructor of an RTPpacket object from the packet bitstream
	public Packet(byte[] packet, int packet_size) {
		// check if total packet size is lower than the header size
		if (packet_size >= HEADER_SIZE) {
			// get the header bitsream:
			header = new byte[HEADER_SIZE];
			for (int i = 0; i < HEADER_SIZE; i++) {
				header[i] = packet[i];
			}

			// get the payload bitstream
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
	 * Get the payload bitstream of the packet.
	 * @return
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
	 * @return
	 */
	public int getPayloadLength() {
		return payloadSize;
	}

	/**
	 * Get the total length of the RTP packet.
	 * 
	 * @return
	 */
	public int getLength() {
		return (this.payloadSize + HEADER_SIZE);
	}

	// getpacket: returns the packet bitstream and its length
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

	// getsequencenumber
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	// return the unsigned value of 8-bit integer nb
	static int unsigned_int(int nb) {
		if (nb >= 0) {
			return (nb);
		} else {
			return (256 + nb);
		}
	}

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

	public AudioFormat getFormat() {
		return format;
	}

	public void setEndian(Boolean endian) {
		this.endian = endian;
	}

	public Boolean getEndian() {
		return endian;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getChannel() {
		return channel;
	}

	public void setEncode(AudioFormat.Encoding encode) {
		this.encode = encode;
	}

	public AudioFormat.Encoding getEncode() {
		return encode;
	}

	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}

	public float getFrameRate() {
		return frameRate;
	}

	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	public int getFrameSize() {
		return frameSize;
	}

	public void setSampleRate(float sampleRate) {
		this.sampleRate = sampleRate;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public void setSampleSizeInBits(int sampleSizeInBits) {
		this.sampleSizeInBits = sampleSizeInBits;
	}

	public int getSampleSizeInBits() {
		return sampleSizeInBits;
	}
}
