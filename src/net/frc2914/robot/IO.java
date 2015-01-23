package net.frc2914.robot;

import edu.wpi.first.wpilibj.Joystick;

public class IO {
	
	public static final Joystick[] joysticks = new Joystick[4];
	static{
		for(int i =0; i < joysticks.length; i++){
			joysticks[i] = new Joystick(i);
		}
	}

}
