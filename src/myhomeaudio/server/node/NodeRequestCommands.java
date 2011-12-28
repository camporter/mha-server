package myhomeaudio.server.node;

public interface NodeRequestCommands {

	final static int INIT = 0;
	final static int PLAY = 1;
	final static int SWITCH = 2;
	final static int STOP = 3;
	final static int DISCONNECT = 4;
	final static int RECEIVED = 5;
}
