package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.robot.Robot;
import net.frc2914.robot.Sensors;

public class Lifter extends Subsystem {

	@Command("liftUp")
	public static void liftUp() {
		if (Sensors.lifterNotTooHigh.get())
			Robot.lifter.set(.5); // @TODO find right speed
	}

	@Command("liftDown")
	public static void liftDown() {
		if (Sensors.lifterNotTooLow.get())
			Robot.lifter.set(-.5); // @TODO find right speed
	}
	
	@Command("clamp")
	public static void clamp() {
		CommandManager.call("togglePiston 0 1");
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
	}

}
