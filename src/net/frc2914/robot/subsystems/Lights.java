package net.frc2914.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.SerialPort;
import net.frc2914.commands.CommandManager;
import net.frc2914.commands.CommandManager.Command;

public class Lights extends Subsystem {
	private static Map<String, Byte> modes = new HashMap<String, Byte>();
	static{
		//Modes:
		modes.put("freedom", (byte) 110);
		modes.put("liberty", (byte) 120);
		modes.put("redbeam", (byte) 130);
		modes.put("candy", 	 (byte) 140);
		modes.put("rainbow", (byte) 150);
		modes.put("patriot", (byte) 160);
		modes.put("deplete", (byte) 170);
		//Animations:
		modes.put("cylonup",    (byte) 1);
		modes.put("cylondown",  (byte) 2);
		modes.put("flashred",   (byte) 3);
		modes.put("flashgreen", (byte) 4);

	}
	public static final int FREEDOM = 110;
	public static final int LIBERTY = 120;
	public static final int REDBEAM = 130;
	public static final int CANDY   = 140;
	public static final int RAINBOW = 150;
	public static final int PATRIOT = 160;
	public static final int DEPLETE = 170;
	
	//Animations:
	public static final int CYLONUP    = 1;
	public static final int CYLONDOWN  = 2;
	public static final int FLASHRED   = 3;
	public static final int FLASHGREEN = 4;
	
	private static SerialPort serialPort;
	
	@Command("setLights")
	public static void setLights(String mode){
		serialPort.write(new byte[]{modes.get(mode.toLowerCase())},1);
		serialPort.flush();
	}
	
	@Override
	public void init() {
		CommandManager.loadCommandsFromClass(getClass());
		serialPort = new SerialPort(9600,SerialPort.Port.kMXP);
	}
}
