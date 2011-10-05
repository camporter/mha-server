package myhomeaudio.server;

/**
 * The Room object represents a room that a User may be in.
 * Nodes are located in a Room.
 * @author Cameron
 *
 */
public class Room {
	int id;
	String name;
	
	public Room(int roomId, String roomName)
	{
		this.id = roomId;
		this.name = roomName;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getId()
	{
		return this.id;
	}
}
