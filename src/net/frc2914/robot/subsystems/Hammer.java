package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.joystick.Keybind;
import net.frc2914.joystick.Keybind.keybindTriggerType;
import net.frc2914.robot.IO;
import net.frc2914.robot.Robot;
import net.frc2914.services.KeybindService;
import net.frc2914.services.ServiceManager;

public class Hammer extends Subsystem {

	@Command("knockbin")
	public static void knockBin() {
		CommandManager.call("swingout;wait 1;swingin");// TODO find right time
	}

	@Command("swingout")
	public static void swingOut() {
		//if (!IO.armNotRetracted.get());
			Robot.hammer.set(-.8);// TODO find right speed
	}

	@Command("swingIn")
	public static void swingIn() {
		double timeout = System.currentTimeMillis() + 1500;
		Robot.hammer.set(.3); // TODO find right speed
		while (IO.armNotRetracted.get() && timeout > System.currentTimeMillis());
		Robot.hammer.set(0);
	}
	@Command("swingstop")
	public static void swingStop(){
		Robot.hammer.set(0);
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		KeybindService keybindService = (KeybindService) ServiceManager
				.getService(KeybindService.class);
		keybindService.addKeybind(new Keybind(2, "swingout",
				keybindTriggerType.WHILE_PRESSED));
		keybindService.addKeybind(new Keybind(2, "swingin",
				keybindTriggerType.WHEN_RELEASED));
	}

}
