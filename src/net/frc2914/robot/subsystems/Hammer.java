package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.robot.Robot;
import net.frc2914.robot.Sensors;

public class Hammer extends Subsystem {

	@Command("swingOut")
	public static void swingOut() {
	
		long timeout = System.currentTimeMillis() + 1000; //@TODO right time
		
		if (!Sensors.armNotRetracted.get())
			Robot.hammer.set(1); // @TODO find right speed
		while (System.currentTimeMillis() < timeout ){}
		
		Robot.hammer.set(0); 
		CommandManager.call("swingIn");
		
	}
	@Command("swingIn")
	public static void swingIn() {
		if (Sensors.armNotRetracted.get())
			do{
			Robot.hammer.set(-.25); // @TODO find right speed
			}while (!Sensors.armNotRetracted.get());
		
	}
	
	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
	}

}
