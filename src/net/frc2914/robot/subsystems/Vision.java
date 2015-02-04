package net.frc2914.robot.subsystems;

import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;
import net.frc2914.services.DriveService;
import net.frc2914.services.ServiceManager;
import net.frc2914.services.VisionService;

public class Vision extends Subsystem {

	@Command("alignWithTote")
	public static void alignWithTote() {
		VisionService visionService = (VisionService) ServiceManager
				.getService(VisionService.class);
		if (visionService.isTrackingObject()) {
			DriveService driveService = (DriveService) ServiceManager
					.getService(DriveService.class);
			int totePosition = visionService.getObjectOffset() - 160;
			while (Math
					.abs((totePosition = visionService.getObjectOffset() - 160)) > 10) {
				driveService.rotate((float) Math.pow(
						((double) totePosition) / 160.0, 2.0));
			}
		}
	}

	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());

	}

}
