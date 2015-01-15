package net.frc2914.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokableMethod {
	private Method method;
	private Object invoker;
	
	/**
	 * 
	 * @param method method to be invoked
	 * @param invoker object from which the method will be invoked from, if null invoker is set to this
	 */
	public InvokableMethod(Method method, Object invoker){
		this.method = method;
		if(invoker != null){
			this.invoker = invoker;
		}else{
			this.invoker = this;
		}
	}
	
	/** 
	 * invokes command with given arguments casting each argument into the given type supplied by the method
	 * @param args arguments in string form to be parsed
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void invoke(String... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class[] parameterTypes = method.getParameterTypes();
		Object[] parameters = new Object[parameterTypes.length];
		for(int i = 0; i < args.length && i < parameterTypes.length; i++){
			Class parameterType = parameterTypes[i];
			if(parameterType.equals(int.class)){
				parameters[i] = Integer.parseInt(args[i]);
				System.out.println("parsing " + args[i] + " as integer");
			}else if(parameterType.equals(Double.class)){
				parameters[i] = Double.parseDouble(args[i]);
			}else{
				parameters[i] = args[i];
			}
		}
		if(invoker == null){
			invoker = this;
		}
		method.invoke(invoker, parameters);
	}
}
