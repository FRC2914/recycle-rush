package net.frc2914.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.frc2914.services.DriveService;
import net.frc2914.services.ServiceManager;

/**
 * Main class, allowing for the command structure.
 * 
 * Loads commands (into existance) and runs them as they're called.
 * 
 * @author toby
 */
public class CommandManager {
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Command {
		public String value();
	}

	private static Map<String, InvokableMethod> commands = new HashMap<String, InvokableMethod>();
	private static boolean acceptDriveCommands = true;

	static {
		loadCommandsFromClass(CommandManager.class);
	}

	/**
	 * Uses reflection to read a class file and load the commands.
	 * 
	 * @param clazz
	 *            - class containing commands to be loaded
	 * @param invoker
	 *            - in case of a dynamic class, where to call from (the object
	 *            of the dynamic class)
	 */
	private static void loadCommandsFromClass(Class clazz, Object invoker) {
		Stream.of(clazz.getMethods())
				.filter(m -> m.getAnnotationsByType(Command.class).length > 0)
				.forEach(
						m -> {
							Stream.of(m.getAnnotationsByType(Command.class))
									.map(a -> (Command) a)
									.forEach(
											a -> commands.put(a.value()
													.toUpperCase(),
													new InvokableMethod(m,
															invoker)));
						});
	}

	/**
	 * Uses reflection to read a class file and load the commands. Only from non
	 * static classes, use where there only exists on instance of the object
	 * 
	 * @param container
	 *            - instance of class with methods
	 */
	public static void loadCommandsFromClass(Object container) {
		loadCommandsFromClass(container.getClass(), container);
	}

	/**
	 * Uses reflection to read a class file and load the commands. Always from a
	 * static class.
	 * 
	 * @param clazz
	 *            class with methods
	 */
	public static void loadCommandsFromClass(Class clazz) {
		loadCommandsFromClass(clazz, null);
	}

	/**
	 * Calls a given command String, searching through the list of loaded
	 * invokables and runs the one with the matching name.
	 * 
	 * @param command
	 *            - String with name and arguments seperated by spaces
	 */
	public static void call(String command) {
		String[] blocks = splitToBlocks(command);
		for(String block : blocks){
			String[] simultaneousBlocks = splitToSimultaneousBlocks(block);
			if(simultaneousBlocks.length > 1){
				Stream.of(simultaneousBlocks).parallel().forEach((simultaneousCommand) ->{
					simultaneousCommand = simultaneousCommand.trim();
					call(simultaneousCommand);
				});
			}else{
				String[] parsedCommand = simultaneousBlocks[0].trim().split(" ", 2);
				try {
					if(parsedCommand.length > 1){
						parsedCommand[0] = parsedCommand[0].toUpperCase();
						commands.get(parsedCommand[0]).invoke(parsedCommand[1].trim().split(" "));
					}else{
						parsedCommand[0] = parsedCommand[0].toUpperCase();
						commands.get(parsedCommand[0]).invoke();
					}
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	private static String[] splitToBlocks(String command){
		command = command.trim();
		ArrayList<String> blocks = new ArrayList<String>();
		int unclosedParens = 0;
		String currentBlock = "";
		boolean simultaneousBlock = false;
		for(int i = 0; i < command.length(); i++){
			char curr = command.charAt(i);
			switch(curr){
			case '(':
				unclosedParens++;
				if(unclosedParens == 1 && !simultaneousBlock){
					continue;
				}
			break;
			case ')':
				unclosedParens--;
				if(unclosedParens == 0 && !simultaneousBlock){
					continue;
				}
			break;
			case ';':
				if(unclosedParens == 0){
					blocks.add(currentBlock.trim());
					currentBlock = "";
					simultaneousBlock = false;
					continue;
				}
			break;
			case '&':
				if(command.charAt(i+1) == '&' && unclosedParens == 0){
					simultaneousBlock = true;
				}
			}
			currentBlock += curr;
		}
		if(command.charAt(command.length() - 1) != ';'){
			blocks.add(currentBlock);
		}
		return (String[])blocks.toArray(new String[blocks.size()]);
	}
	
	private static String[] splitToSimultaneousBlocks(String command){
		command = command.trim();
		ArrayList<String> blocks = new ArrayList<String>();
		int unclosedParens = 0;
		String currentBlock = "";
		for(int i = 0; i < command.length(); i++){
			char curr = command.charAt(i);
			switch(curr){
			case '(':
				unclosedParens++;
				if(unclosedParens == 1){
					continue;
				}
			break;
			case ')':
				unclosedParens--;
				if(unclosedParens == 0){
					continue;
				}
			break;
			case '&':
				if(command.charAt(i+1) == '&' && unclosedParens == 0){
					i++;
					blocks.add(currentBlock.trim());
					currentBlock = "";
					continue;
				}
			}
			currentBlock += curr;
		}
		if(command.charAt(command.length() - 1) != ';'){
			blocks.add(currentBlock);
		}
		return (String[])blocks.toArray(new String[blocks.size()]);
	}

	/**
	 * Calls a given command from a String. Specifically calls a command
	 * pertaining to driving the robot, adding it to the drive queue (the list
	 * of commands the robot will execute in the interrupt of the drive loop).
	 * 
	 * @param command
	 *            - String with name and arguments seperated by spaces
	 */
	public static void callDriveCommand(String command) {
		if (acceptDriveCommands)
			((DriveService) ServiceManager.getService(DriveService.class))
					.addToQueue(command);
	}

	@Command("acceptDriveCommands")
	public static void acceptDriveCommands(boolean setAcceptDriveCommands) {
		acceptDriveCommands = setAcceptDriveCommands;
		if (acceptDriveCommands)
			((DriveService) ServiceManager.getService(DriveService.class))
					.clearQueue();
	}
}