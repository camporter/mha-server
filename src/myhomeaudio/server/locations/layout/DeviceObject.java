package myhomeaudio.server.locations.layout;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class DeviceObject implements JSONAware {
		public final int id;
		public final int rssi;

		public DeviceObject(int id, int rssi) {
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
