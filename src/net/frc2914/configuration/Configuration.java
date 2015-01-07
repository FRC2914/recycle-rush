package net.frc2914.configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Configuration{
	private static Properties  properties;
	
	public static void loadProperties(File propertiesFile) throws FileNotFoundException, IOException{
		properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
	}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}
}
