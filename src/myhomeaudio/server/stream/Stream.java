package myhomeaudio.server.stream;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Abstract basis for
 * 
 * @author Cameron
 * 
 */
public class Stream implements JSONAware {

	protected String name;

	public Stream(String name) {
		this.name = name;
	}

	public Stream(Stream s) {
		this.name = s.name();
	}

	public String name() {
		return name;
	}

	@Override
	public String toJSONString() {
		JSONObject result = new JSONObject();
		result.put("name", name);
		return result.toString();
	}

}
