package net.frc2914.services;

public abstract class Service {
	
	public abstract void update();
	public String getStatus(){
		return "nominal";
	}
}
