package net.frc2914.joystick;

import net.frc2914.commands.CommandManager;

import edu.wpi.first.wpilibj.Joystick;

public class Keybind {
	private int button;
	private String command;
	private boolean lastState;

	public Keybind(int button, String command) {
		this.button = button;
		this.command = command;
	}

	public void update(Joystick joystick) {
		if (!lastState && joystick.getRawButton(button)) {
			CommandManager.call(command);
		}
		lastState = joystick.getRawButton(button);
	}
}
