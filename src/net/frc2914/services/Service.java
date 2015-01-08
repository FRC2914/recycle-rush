package net.frc2914.services;

public abstract class Service {
	/**
	 * called once every frame
	 */
	public abstract void update();
	
	/**
	 * called when the robot boots
	 */
	public abstract void init();
}
