package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;

public class Timing extends Subsystem {

	@Command("wait")
	public static void wait(double waitTime) {
		try {
			Thread.sleep((long) (waitTime*1000));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
	}

}
