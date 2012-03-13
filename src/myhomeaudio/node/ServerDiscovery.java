package myhomeaudio.node;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import myhomeaudio.server.discovery.DiscoverySearch;
import myhomeaudio.server.discovery.DiscoverySearchListener;
import myhomeaudio.server.discovery.DiscoveryDescription;


public class ServerDiscovery implements DiscoverySearchListener {
	
	DiscoverySearch search;
	DiscoveryDescription descriptor;
	
	
	public ServerDiscovery() {
		search = new DiscoverySearch("myhomeaudio");
		search.addServiceBrowserListener(this);
	}
	
	public boolean doDiscovery() {
		search.startListener();
		search.startLookup();
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// Nothing
		}
		
		search.stopLookup();
		search.stopListener();
		
		if (descriptor == null) {
			return false;
		}
		else {
			try {
				Socket s = new Socket(descriptor.getAddress(), descriptor.getNodePort());
				s.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
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
		System.out.println("RECEIVED!");
		this.descriptor = descriptor;
		
	}

}
