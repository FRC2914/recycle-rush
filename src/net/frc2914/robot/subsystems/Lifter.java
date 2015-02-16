package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.joystick.Keybind;
import net.frc2914.joystick.Keybind.keybindTriggerType;
import net.frc2914.robot.IO;
import net.frc2914.robot.Robot;
import net.frc2914.services.KeybindService;
import net.frc2914.services.ServiceManager;

public class Lifter extends Subsystem { 

	/**
	 * High level command that will lift a tote, regardless of how many are already stacked
	 */
	@Command("liftTote")
	public static void liftTote(){
		if (!IO.toteNotInPlace.get()){
			if (IO.lifterBottom.get())
				CommandManager.call("liftDown");
			CommandManager.call("clamp;liftUp");
		}
		
	}
	
	/**
	 * Sends instructions to raise the arms until the top limit switch is depressed
	 */
//	@Command("liftUp")
//	public static void liftUp() {
//		long start = System.currentTimeMillis();
//		lift(.5); // @TODO find right speed
//		CommandManager.call("setLights CYLONUP");
//		while (IO.lifterNotHigh.get() || start + 5000 < System.currentTimeMillis()){}
//		lift(0); 
//	}

	@Command("liftUp")
	public static void liftUp(){
		if(IO.lifterTop.get()){
			lift(-.5);
		}else{
			lift(0);
		}
	}
	
	@Command("liftStop")
	public static void liftStop(){
		lift(0);
	}
	@Command("liftDown")
	public static void liftDown(){
		if(IO.lifterBottom.get()){
			lift(.5);
		}else{
			lift(0);
		}
	}
	
	public static final void lift(double magnitude){
		Robot.lifterLeft.set(magnitude * 1.1);
		Robot.lifterRight.set(-magnitude);
	}
	
//	@Command("liftDown")
//	public static void liftDown() {
//		long start = System.currentTimeMillis();
//		CommandManager.call("unclamp");
//		lift(-.5); // @TODO find right speed
//		CommandManager.call("setLights CYLONDOWN");
//		while (IO.lifterNotLow.get() || start + 5000 < System.currentTimeMillis()){}
//		lift(0);
//	}
	
	@Command("clamp")
	public static void clamp() {
		CommandManager.call("setLights FLASHGREEN");
		CommandManager.call("togglePiston 0 1");
	}
	
	@Command("unclamp")
	public static void unclamp() {
		CommandManager.call("togglePiston 0 1");
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		KeybindService keybindService = (KeybindService) ServiceManager.getService(KeybindService.class);
		//bind command "togglePiston 0 1" to joystick button 1
		keybindService.addKeybind(new Keybind(5, "liftup", keybindTriggerType.WHILE_PRESSED));
		keybindService.addKeybind(new Keybind(3, "liftdown", keybindTriggerType.WHILE_PRESSED));
		keybindService.addKeybind(new Keybind(5, "liftstop", keybindTriggerType.WHEN_RELEASED));
		keybindService.addKeybind(new Keybind(3, "liftstop", keybindTriggerType.WHEN_RELEASED));
	}

}
