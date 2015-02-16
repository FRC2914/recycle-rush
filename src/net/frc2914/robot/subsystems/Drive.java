package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.robot.IO;
import net.frc2914.services.DriveService;
import net.frc2914.services.ServiceManager;

public class Drive extends Subsystem {
	private static DriveService driveService;
	
	@Command("drive")
	public static void drive(double magnitude) {
		driveService.drive(magnitude);
	}
	@Command("driveToTote")
	public static void drivetoTote() {
		driveService.drive(.5); //TODO Placeholder val
		while (IO.toteNotInPlace.get()){}
		driveService.drive(0);
			
	}
	
	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		driveService = (DriveService)ServiceManager.getService(DriveService.class);
	}

}
