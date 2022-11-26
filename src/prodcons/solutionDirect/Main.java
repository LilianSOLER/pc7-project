package prodcons.solutionDirect;

import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;

public class Main {
	public static Properties getProperties() throws IOException {
		Properties properties = new Properties();
		properties.loadFromXML(Main.class.getResourceAsStream("../utils/options.xml"));
		return properties;
	}

	public static void displayProperties(Properties properties) {
		// display the properties
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			System.out.println(key + ": " + value);
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		//displayProperties(getProperties());


		// create the buffer
		System.out.println("Creating the buffer");
		ProdConsBuffer buffer = new ProdConsBuffer();

		// create the producers
		int nProducers = Integer.parseInt(getProperties().getProperty("nProd"));
		int prodTime = Integer.parseInt(getProperties().getProperty("prodTime"));
		System.out.println("Creating " + nProducers + " producers");

		Producer[] producers = new Producer[nProducers];
		for (int i = 0; i < nProducers; i++) {
			producers[i] = new Producer(buffer, i, prodTime);
		}

		// create the consumers
		int nConsumers = Integer.parseInt(getProperties().getProperty("nCons"));
		int consTime = Integer.parseInt(getProperties().getProperty("consTime"));
		System.out.println("Creating " + nConsumers + " consumers");

		Consumer[] consumers = new Consumer[nConsumers];
		for (int i = 0; i < nConsumers; i++) {
			consumers[i] = new Consumer(buffer, i + nProducers , consTime);
		}


		// interrupt the consumers and producers after 5 seconds
		System.out.println("Interrupting the consumers and producers after 5 seconds");
		sleep(5000);
		System.out.println("Interrupting producers and consumers");


		for (int i = 0; i < nProducers; i++) {
			producers[i].stopRunning();
		}
		for (int i = 0; i < nConsumers; i++) {
			consumers[i].stopRunning();
		}
	}
}