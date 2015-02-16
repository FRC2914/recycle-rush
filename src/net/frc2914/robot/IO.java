package net.frc2914.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class IO {
	
	public static final Joystick[] joysticks = new Joystick[4];
	
	public static final DigitalInput lifterBottom = new DigitalInput(0);//@TODO fix pot vals
	public static final DigitalInput lifterTop  = new DigitalInput(1);//@TODO fix pot vals
	
	public static final DigitalInput armNotRetracted = new DigitalInput(3);//@TODO fix pot vals

	public static final DigitalInput toteNotInPlace = new DigitalInput(4);//@TODO fix pot
	
	static{
		for(int i =0; i < joysticks.length; i++){
			joysticks[i] = new Joystick(i);
		}
	}

}
