package net.frc2914.joystick;

import net.frc2914.commands.CommandManager;

import edu.wpi.first.wpilibj.Joystick;

public class Keybind {
	private int button;
	private String command;
	private boolean lastState;
	/**
	 * create a new keybinding
	 * @param button joystick button
	 * @param command command to be run when button is pushed
	 */
	public Keybind(int button, String command) {
		this.button = button;
		this.command = command;
	}
	/**
	 * called every tick to check if button has been pushed and if so call command
	 * @param joystick joystick to check button with
	 */
	public void update(Joystick joystick) {
		if (!lastState && joystick.getRawButton(button)) {
			CommandManager.call(command);
		}
		lastState = joystick.getRawButton(button);
	}
}
