package tank029;

import java.io.IOException;
import java.util.Properties;

public class PropertyM {
	
	private PropertyM() {
		
	}
	
	static Properties p=new Properties();
	
	static {
		try {
			p.load(PropertyM.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return p.getProperty(key);
	}
}
