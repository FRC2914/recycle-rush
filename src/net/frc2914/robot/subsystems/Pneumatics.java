package net.frc2914.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.joystick.Keybind;
import net.frc2914.services.KeybindService;
import net.frc2914.services.ServiceManager;

public class Pneumatics extends Subsystem{

	@Command("toggleSolenoid")
	public static void toggleSolenoid(int channel){
		System.out.println("toggling solenoid " + channel);
		Solenoid solenoid = new Solenoid(channel);
		solenoid.set(!solenoid.get());
	}
	
	@Command("togglePiston")
	public static void togglePiston(int channel1, int channel2){
		Solenoid solenoid = new Solenoid(channel1);
		setSolenoid(channel2, solenoid.get());
		solenoid.set(!solenoid.get());
	}
	
	@Command("setSolenoid")
	public static void setSolenoid(int channel, boolean on){
		new Solenoid(channel).set(on);
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		KeybindService keybindService= (KeybindService) ServiceManager.getService(KeybindService.class);
		keybindService.addKeybind(new Keybind(1, "togglePiston 0 1"));
	}
}
