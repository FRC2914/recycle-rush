package net.frc2914.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.frc2914.configuration.Configuration;

public class ServiceManager extends Thread {
	private static List<Service> services = new ArrayList<Service>();
	static {
		services.add(new KeybindService());
		services.add(new CompressorService());
		services.add(new DriveService());
		services.add(new VisionService());
	}

	@Override
	public void run() {
		services.parallelStream().forEach((service) -> service.init());
		long frameLength = Long.parseLong(Configuration
				.getProperty("frame_length"));
		while (true) {
			long frameStart = System.currentTimeMillis();
			services.parallelStream().forEach((service) -> service.update());
			try {
				if (frameLength - (System.currentTimeMillis() - frameStart) > 0) {
					sleep(frameLength
							- (System.currentTimeMillis() - frameStart));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * gets instance of running service
	 * @param service type of service to get
	 * @return instance of requested service
	 */
	public static Service getService(Class<? extends Service> service) {
		for (Service s : services) {
			if (s.getClass().equals(service)) {
				return s;
			}
		}
		return null;
	}

}
