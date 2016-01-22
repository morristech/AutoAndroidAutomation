package Utilities;

import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
	/**
	 * 
	 * @param propFile: The absolute path to a properties file (must be validated)
	 * @return
	 */
	public static Properties loadProperties(String propFile) throws Exception{
		Properties loadedProps = new Properties();
		InputStream in = null;
		try{
			in = LoadProperties.class.getClassLoader().getResourceAsStream(propFile);
			loadedProps.load(in);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			in.close();
		}
		return loadedProps;
	}
}
