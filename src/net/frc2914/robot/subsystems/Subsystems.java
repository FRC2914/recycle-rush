package net.frc2914.robot.subsystems;

import java.util.ArrayList;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;

public class Subsystems {
	private static ArrayList<Class<? extends Subsystem>> subsystems = new ArrayList<Class<? extends Subsystem>>();
	static {
		subsystems.add(Hammer.class);
		subsystems.add(Drive.class);
		subsystems.add(Lifter.class);
		subsystems.add(Lights.class);
		subsystems.add(Pneumatics.class);
		subsystems.add(Timing.class);
		subsystems.add(Vision.class);
	}

	public static void init() {
		subsystems.parallelStream().forEach((subsystem) -> {
			try {
				subsystem.newInstance().init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		CommandManager.loadCommandsFromClass(Subsystems.class);
	}
	
	@Command("print")
	public static void print(String s){
		System.out.println(s);
	}
}
