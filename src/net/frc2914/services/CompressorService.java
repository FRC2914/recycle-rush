package net.frc2914.services;

import edu.wpi.first.wpilibj.Compressor;

public class CompressorService extends Service {

	private Compressor compressor = new Compressor(0);
	@Override
	public void update() {
		if(!compressor.getCompressorNotConnectedFault() && !compressor.getPressureSwitchValue()){
			if(compressor.enabled()){
					compressor.stop();
				}
		}else{
				compressor.start();
				
		}
		
	}
	
	@Override
	public void init() {
		System.out.println("initializing compressor service");
	}

}
