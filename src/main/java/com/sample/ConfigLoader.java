package com.sample;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
public class ConfigLoader {
	private InputStream input;
	private Properties prop;
	private HashMap<String,String> hashmap = new HashMap<String,String>();
	
	public ConfigLoader() {
		this.prop = new Properties();
		this.input = null;
	}
	public void loadConfig() {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			hashmap.put("dbhost", prop.getProperty("dbhost"));
			hashmap.put("dbuser", prop.getProperty("dbuser"));
			hashmap.put("dbpassword", prop.getProperty("dbpassword"));
			hashmap.put("dbname", prop.getProperty("dbname"));
			hashmap.put("dbport", prop.getProperty("dbport"));
			// get the property value and print it out
//			System.out.println(prop.getProperty("dbhost"));
//			System.out.println(prop.getProperty("dbuser"));
//			System.out.println(prop.getProperty("dbpassword"));
//			System.out.println(prop.getProperty("dbname"));
//			System.out.println(prop.getProperty("dbport"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getConfig( String name ){
		return hashmap.get(name);
	}
}