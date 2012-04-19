package myhomeaudio.node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads in the configuration for the node.
 * 
 * @author Cameron
 * 
 */
public class Configuration {

	private static Configuration instance;

	private Properties configFile;

	public static final String CONFIG_FILE = "node.conf";

	private Configuration() {
		configFile = new Properties();
	}

	public static synchronized Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	/**
	 * Reads the configuration from the file.
	 * 
	 * @return Whether the configuration read succeeded.
	 */
	public boolean readConfig() {
		try {
			FileInputStream fis = new FileInputStream(CONFIG_FILE);
			configFile.load(fis);
			
			// Make sure that certain keys are in the config file.
			if (configFile.containsKey("bluetoothName")
					&& configFile.containsKey("bluetoothAddress")) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the configured bluetooth name for this node.
	 * 
	 * @return The name of the node for bluetooth, or null if the property
	 *         doesn't exist
	 */
	public String getBluetoothName() {
		return configFile.getProperty("bluetoothName");
	}

	public String getBluetoothAddress() {
		return configFile.getProperty("bluetoothAddress");
	}
}
