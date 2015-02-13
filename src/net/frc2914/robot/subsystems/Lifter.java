package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.robot.IO;
import net.frc2914.robot.Robot;

public class Lifter extends Subsystem { 

	/**
	 * High level command that will lift a tote, regardless of how many are already stacked
	 */
	@Command("liftTote")
	public static void liftTote(){
		if (!IO.toteNotInPlace.get()){
			if (IO.lifterNotHigh.get())
				CommandManager.call("liftDown");
			CommandManager.call("clamp;liftUp");
		}
		
	}
	
	/**
	 * Sends instructions to raise the arms until the top limit switch is depressed
	 */
	@Command("liftUp")
	public static void liftUp() {
		long start = System.currentTimeMillis();
		Robot.lifter.set(.5); // @TODO find right speed
		while (IO.lifterNotHigh.get() || start + 5000 < System.currentTimeMillis()){}
		Robot.lifter.set(0); 
	}

	@Command("liftDown")
	public static void liftDown() {
		long start = System.currentTimeMillis();
		CommandManager.call("unclamp");
		Robot.lifter.set(-.5); // @TODO find right speed
		while (IO.lifterNotLow.get() || start + 5000 < System.currentTimeMillis()){}
		Robot.lifter.set(0);
	}
	
	@Command("clamp")
	public static void clamp() {
		CommandManager.call("togglePiston 0 1");
	}
	
	@Command("unclamp")
	public static void unclamp() {
		CommandManager.call("togglePiston 0 1");
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
	}

}
