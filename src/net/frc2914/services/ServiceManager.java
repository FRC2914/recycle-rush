package net.frc2914.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.frc2914.configuration.Configuration;

public class ServiceManager extends Thread{
	private static List<Service> services = new ArrayList<Service>();
	static{
		services.add(new KeybindService());
		services.add(new CompressorService());
	}
	
	@Override
	public void run() {
		services.parallelStream().forEach((service) -> service.init());
		long frameLength = Long.parseLong(Configuration.getProperty("frame_length"));
		while(true){
			long frameStart = System.currentTimeMillis();
			services.parallelStream().forEach((service) -> service.update());
			try {
				if(frameLength - (System.currentTimeMillis() - frameStart) > 0){
					sleep(frameLength - (System.currentTimeMillis() - frameStart));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	

}
