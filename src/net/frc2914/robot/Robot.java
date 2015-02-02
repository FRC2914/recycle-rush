
package net.frc2914.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.frc2914.configuration.Configuration;
import net.frc2914.robot.subsystems.Pneumatics;
import net.frc2914.services.ServiceManager;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static final RobotDrive drive = new RobotDrive(1, 0);
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	try {
			Configuration.loadProperties(new File("/home/lvuser/robot.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	System.out.println(Configuration.getProperty("greeting_message"));
    	new ServiceManager().start();
    	new Pneumatics().init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
