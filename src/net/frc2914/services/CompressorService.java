package net.frc2914.services;

import net.frc2914.configuration.Configuration;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotState;

public class CompressorService extends Service {

	private Compressor compressor = new Compressor();
	private long compressorTimeout;
	private long compressorStartTime;
	//set to true if compressor runs for longer than compressorTimeout without stopping
	private boolean pneumaticProblemDetected;
	@Override
	public void update() {
		if(!compressor.getPressureSwitchValue() && RobotState.isEnabled() && !pneumaticProblemDetected){
			if(compressor.enabled()){
				if(System.currentTimeMillis() - compressorStartTime > compressorTimeout){
					compressor.setClosedLoopControl(false);
					pneumaticProblemDetected = true;
				}
			}else{
				compressorStartTime = System.currentTimeMillis();
				compressor.setClosedLoopControl(true);
				
			}
		}
	}

	@Override
	public void init() {
		compressorTimeout = Long.parseLong(Configuration.getProperty("compressor_timeout"));
		System.out.println("initializing compressor service");
	}

}
