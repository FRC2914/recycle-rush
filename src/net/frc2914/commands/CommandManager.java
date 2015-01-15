package net.frc2914.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CommandManager {
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Command {
		public String value();
	}

	private static Map<String, InvokableMethod> commands = new HashMap<String, InvokableMethod>();

	private static void loadCommandsFromClass(Class clazz, Object invoker) {
		Stream.of(clazz.getMethods())
				.filter(m -> m.getAnnotationsByType(Command.class).length > 0)
				.forEach(
						m -> {
							Stream.of(m.getAnnotationsByType(Command.class))
									.map(a -> (Command)a)
									.forEach(
											a -> commands.put(a.value().toUpperCase(), new InvokableMethod(m, invoker)));
						});
		
	}
	/**
	 * load commands from non static class (only use for singletons)
	 * @param container instance of class with methods
	 */
	public static void loadCommandsFromClass(Object container){
		loadCommandsFromClass(container.getClass(), container);
	}
	
	/**
	 * load commands from static class
	 * @param clazz class with methods
	 */
	public static void loadCommandsFromClass(Class clazz){
		loadCommandsFromClass(clazz, null);
	}
	
	/** 
	 * calls a given command 
	 * @param command command with arguments seperated by spaces
	 */
	public static void call(String command){
		String commandName = command.substring(0, command.indexOf(' ')).toUpperCase();
		command = command.replaceFirst(commandName, "");
		command = command.trim();
		try {
			System.out.println("command: " + commandName);
			commands.get(commandName).invoke(command.split(" "));
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}