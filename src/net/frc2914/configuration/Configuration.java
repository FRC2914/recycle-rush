package net.frc2914.configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Configuration{
	private static Properties  properties;
	/**
	 * load properties from file for use in program
	 * @param propertiesFile file to load properties from
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void loadProperties(File propertiesFile) throws FileNotFoundException, IOException{
		properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
	}
	/**
	 * get a property from the properties file
	 * @param key property name
	 * @return property value
	 */
	public static String getProperty(String key){
		return properties.getProperty(key);
	}
}
