package net.frc2914.services;

import java.util.HashMap;
import java.util.Map;
import net.frc2914.configuration.Configuration;

public class ServiceManager extends Thread{
	private static Map<String, Service> services = new HashMap<String, Service>();
	static{
		services.put("keybind", new KeybindService());
		services.put("compressor", new CompressorService());
	}
	
	@Override
	public void run() {
		long statusTimer = Long.parseLong(Configuration.getProperty("service_status_interval"));
		
	}
	

}
