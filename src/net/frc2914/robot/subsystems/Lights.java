package net.frc2914.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;

public class Lights extends Subsystem {
	public static final int FREEDOM = 110;
	public static final int LIBERTY = 120;
	public static final int REDBEAM = 130;
	public static final int CANDY = 140;
	public static final int RAINBOW = 150;
	public static final int PATRIOT = 160;
	
	private static SerialPort serialPort;
	
	@Command("setLights")
	public static void setLights(int type){
		serialPort.write(new byte[]{(byte)type},1);
		serialPort.flush();
	}
	
	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		serialPort = new SerialPort(9600,SerialPort.Port.kMXP);
	}
}
