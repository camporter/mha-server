package myhomeaudio.node;

import java.net.InetAddress;

import myhomeaudio.server.discovery.DiscoverySearch;
import myhomeaudio.server.discovery.DiscoverySearchListener;
import myhomeaudio.server.discovery.DiscoveryDescription;


public class ServerDiscovery implements DiscoverySearchListener {
	
	DiscoverySearch browser;
	DiscoveryDescription descriptor;
	
	
	public ServerDiscovery() {
		browser = null;//new DiscoverySearch();
		browser.addServiceBrowserListener(this);
		browser.setServiceName("myhomeaudio");
	}
	
	public boolean doDiscovery() {
		browser.startListener();
		browser.startLookup();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Nothing
		}
		
		browser.stopLookup();
		browser.stopListener();
		
		if (descriptor == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public int getNodePort() {
		if (descriptor != null)
			return descriptor.getNodePort();
		else
			return -1;
	}

	@Override
	public void serviceReply(DiscoveryDescription descriptor) {
		this.descriptor = descriptor;
		
	}

}
