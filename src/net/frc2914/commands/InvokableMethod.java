package net.frc2914.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokableMethod {
	private Method method;
	private Object invoker;
	
	public InvokableMethod(Method method, Object invoker){
		this.method = method;
		this.invoker = invoker;
	}
	
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
