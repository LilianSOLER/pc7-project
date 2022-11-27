package prodcons.solutionDirect;

import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;

public class Main {
	static Properties properties = new Properties();
	public static void getProperties(String fileName) throws IOException {
		Properties properties = new Properties();
		properties.loadFromXML(Main.class.getResourceAsStream("../utils/" + fileName + ".xml"));
		Main.properties = properties;
	}

	public static void displayProperties() {
		// display the properties
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			System.out.println(key + ": " + value);
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		// load the properties
		getProperties("options");
		// display the properties
		displayProperties();


		// create the buffer
		System.out.println("Creating the buffer");
		ProdConsBuffer buffer = new ProdConsBuffer();

		// create the producers
		int nProducers = Integer.parseInt(properties.getProperty("nProd"));
		int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
		System.out.println("Creating " + nProducers + " producers");

		Producer[] producers = new Producer[nProducers];
		for (int i = 0; i < nProducers; i++) {
			producers[i] = new Producer(buffer, i);
		}

		Producer.setProdTime(prodTime);
		Producer.setMinProd(Integer.parseInt(properties.getProperty("minProd")));
		Producer.setMaxProd(Integer.parseInt(properties.getProperty("maxProd")));

		// create the consumers
		int nConsumers = Integer.parseInt(properties.getProperty("nCons"));
		int consTime = Integer.parseInt(properties.getProperty("consTime"));
		System.out.println("Creating " + nConsumers + " consumers");

		Consumer[] consumers = new Consumer[nConsumers];
		for (int i = 0; i < nConsumers; i++) {
			consumers[i] = new Consumer(buffer, i + nProducers);
		}

		Consumer.setConsTime(consTime);

		// wait for the producers to finish
		for (Producer producer : producers) {
			producer.join();
		}
		System.out.println("All producers finished");

		// verify that the buffer is empty and stop the consumers if it is not empty yet re ask at 50 ms intervals
		System.out.println("Waiting for the buffer to be empty");
		while (buffer.nmsg() != 0) {
			System.out.println("Buffer is not empty yet, waiting for consumers to finish");
			System.out.println("Buffer size: " + buffer.nmsg());
			sleep(50);
		}

		// stop the consumers
		System.out.println("Stopping the consumers");
		for (Consumer consumer : consumers) {
			consumer.stopRunning();
		}
		System.out.println("All consumers finished");

		// verify if no message was lost
		System.out.println("Verifying if no message was lost");
		if(buffer.noErrors()) {
			System.out.println("No message was lost");
		} else {
			System.out.println("Some messages were lost");
		}
	}
}