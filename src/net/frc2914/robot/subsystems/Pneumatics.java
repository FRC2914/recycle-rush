package net.frc2914.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.joystick.Keybind;
import net.frc2914.services.KeybindService;
import net.frc2914.services.ServiceManager;

public class Pneumatics extends Subsystem{
	/**\
	 * command to toggle a solenoid
	 * @param channel solenoid port
	 */
	@Command("toggleSolenoid")
	public static void toggleSolenoid(int channel){
		System.out.println("toggling solenoid " + channel);
		Solenoid solenoid = new Solenoid(channel);
		solenoid.set(!solenoid.get());
	}
	
	/**
	 * toggles a piston consisting of two solenoids
	 * @param channel1 first solenoid
	 * @param channel2 second solenoid
	 */
	@Command("togglePiston")
	public static void togglePiston(int channel1, int channel2){
		Solenoid solenoid = new Solenoid(channel1);
		setSolenoid(channel2, solenoid.get());
		solenoid.set(!solenoid.get());
	}
	
	/**
	 * sets position of a solenoid
	 * @param channel solenoid port
	 * @param on is the solenoid on or off
	 */
	@Command("setSolenoid")
	public static void setSolenoid(int channel, boolean on){
		new Solenoid(channel).set(on);
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		KeybindService keybindService= (KeybindService) ServiceManager.getService(KeybindService.class);
		//bind command "togglePiston 0 1" to joystick button 1
		keybindService.addKeybind(new Keybind(1, "togglePiston 0 1"));
	}
}
