package myhomeaudio.server.helper;

import java.io.File;
//import org.json.simple.JSONObject;

public class SongHelper extends Helper implements HelperInterface {
	public String getOutput() {
		File files = new File("/music");
		String[] array = files.list();
		//String output = array.toJSONstring();
		String output = array[0]+array[1];
		return output;
	}

}
