package net.frc2914.services;

import java.util.ArrayList;
import java.util.List;

import net.frc2914.joystick.Keybind;
import edu.wpi.first.wpilibj.Joystick;

public class KeybindService extends Service {
	private Joystick joystick = new Joystick(0);
	private List<Keybind> keybinds = new ArrayList<Keybind>();
	@Override
	public void update() {
		keybinds.forEach(k -> k.update(joystick));
	}
	
	/**
	 * adds keybinding
	 * @param keybind keybind for joystick
	 */
	public void addKeybind(Keybind keybind){
		keybinds.add(keybind);
	}

	@Override
	public void init() {
		System.out.println("Initializing keybind service");
		
	}

}
