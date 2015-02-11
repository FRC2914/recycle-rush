package net.frc2914.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
		for (String specificCommand : command.split(";")) {
			String commandName = specificCommand.substring(0, specificCommand.indexOf(' '))
					.toUpperCase();
			specificCommand = specificCommand.replaceFirst(commandName, "");
			specificCommand = specificCommand.trim();
			try {
				System.out.println("command: " + commandName);
				commands.get(commandName).invoke(specificCommand.split(" "));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}

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