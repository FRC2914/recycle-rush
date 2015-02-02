package net.frc2914.services;

import java.util.ArrayList;

import net.frc2914.commands.CommandManager;
import net.frc2914.configuration.Configuration;
import net.frc2914.robot.IO;
import net.frc2914.robot.Robot;

public class DriveService extends Service{
	private ArrayList<String> queue = new ArrayList<String>();
	private DriveType driveType;

	private enum DriveType{
		/**
		 * Determines tank drive style
		 * Implies: One Joystick
		 *			Right = 0
		 *			Left  = 1
		 * Allows:  Independent control of treads 
		 */
		tank,
		/**
		 * Determines arcade drive style
		 * Implies: One Joystick
		 *			Right = 0
		 * Allows: 	Longitudinal Movement
		 * 			Directional Rotation
		 */
		arcade, 
		/**
		 * Determines meccanum drive style
		 * Implies: One Joystick
		 *			Right = 0
		 * Allows: 	Longitudinal Movement
		 * 			Lateral Movement
		 * 			Rotation around the vertical axis
		 */
		meccanum;
	}
	@Override
	/**
	 * called every frame
	 * 
	 * run drive protocols from Joystick
	 * 
	 * runs and empties command queue
	 */
	public void update() {
		while(queue.size() > 0){
			CommandManager.call(queue.remove(0));
		}
		
		
		switch(driveType){
			case tank:
				Robot.drive.tankDrive(IO.joysticks[1].getY(), IO.joysticks[0].getY());
				break;
			case arcade:
				Robot.drive.arcadeDrive(IO.joysticks[0].getY(), -IO.joysticks[0].getTwist());
				break;
			case meccanum:
				double magnitude = IO.joysticks[0].getMagnitude();
				double direction = Math.toDegrees(Math.atan(IO.joysticks[0].getY()/IO.joysticks[0].getX()));
				double rotation  = IO.joysticks[0].getTwist();
				Robot.drive.mecanumDrive_Polar(magnitude, direction, rotation);
				break;
		}
	}
	/**
	 * initiates the variables based on the properties file
	 * 
	 */
	@Override
	public void init() {
		
		String driveType = Configuration.getProperty("drive_type");
		switch(driveType){
		case "tank":
			this.driveType = DriveType.tank;
			break;
		case "arcade":
			this.driveType = DriveType.arcade;
			break;
		case "meccanum":
			this.driveType = DriveType.meccanum;
			break;
		default:
			this.driveType = DriveType.arcade;
		}
		
	}
	
	/**
	 * Add new command to perform
	 * @param command - string to be parsed in command system
	 */
	public void addToQueue(String command) {
		queue.add(command);
	}
	
	/**
	 * clear queue perform
	 * called on toggle command acceptance
	 */
	public void clearQueue() {
		queue.clear();
	}
	

}
