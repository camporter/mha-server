package myhomeaudio.server.database;

import java.sql.Connection;
import java.util.ArrayList;

import myhomeaudio.server.user.User;
import myhomeaudio.server.user.UserManager;

public class DBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database db = Database.getInstance();
			
		UserManager um = new UserManager();
		if (um.registerUser(new User("pappy", "password")) == UserManager.REGISTER_OK) {
			System.out.println("user registered!");
		}
		
		System.out.println(um.getUser(1).getUsername());
		System.out.println(um.getUser("pappy").getId());
		System.out.println(um.getUser(1).getPassword());
			
	}
}
