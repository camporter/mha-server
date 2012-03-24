package myhomeaudio.server.locations;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class DeviceObject implements JSONAware {
		public String id;
		public int rssi;

		public DeviceObject(String id, int rssi) {
			this.id = id;
			this.rssi = rssi;
		}

		public String toJSONString() {
			JSONObject device = new JSONObject();

			device.put("id", id);
			device.put("rssi", rssi);
			return device.toJSONString();
		}
}
