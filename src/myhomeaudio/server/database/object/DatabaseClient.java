package myhomeaudio.server.database.object;

import myhomeaudio.server.client.Client;
import myhomeaudio.server.user.User;

public class DatabaseClient extends DatabaseObject<Client> {

	public DatabaseClient(int id, Client client) {
		super(id, new Client(client));
	}

	public DatabaseClient(DatabaseClient dbClient) {
		super(dbClient.getId(), new Client(dbClient.getCurrentUser(), dbClient.getMacAddress(),
				dbClient.getIpAddress(), dbClient.getBluetoothName()));
	}

	public String getIpAddress() {
		return this.object.getIpAddress();
	}

	public String getMacAddress() {
		return this.object.getMacAddress();
	}

	public String getBluetoothName() {
		return this.object.getBluetoothName();
	}

	public User getCurrentUser() {
		return this.object.getCurrentUser();
	}

	public void setCurrentUser(User user) {
		this.object.setCurrentUser(user);
	}

}
