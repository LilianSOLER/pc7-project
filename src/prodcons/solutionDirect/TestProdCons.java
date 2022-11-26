package prodcons.solutionDirect;

import java.io.IOException;
import java.util.Properties;

public class TestProdCons {
	public static Properties getProperties() throws IOException {
		Properties properties = new Properties();
		properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("utils/options.xml"));
		return properties;
	}

	public static void displayProperties(Properties properties) {
		// display the properties
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			System.out.println(key + ": " + value);
		}
	}
	public static void main(String[] args) throws IOException {
		displayProperties(getProperties());
	}
}
