package prodcons.semaphores;

import prodcons.utils.Consumer;
import prodcons.utils.Producer;

import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;
import static prodcons.utils.Print.print;

public class Main {
	static Properties properties = new Properties();
	static boolean print = false;

	public static void getProperties(String fileName) throws IOException {
		Properties properties = new Properties();
		properties.loadFromXML(Main.class.getResourceAsStream("../utils/" + fileName + ".xml"));
		Main.properties = properties;
	}

	public static void displayProperties() {
		// display the properties
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			print(key + ": " + value, print);
		}
	}

	public static int main(String[] args, String fileName, Boolean print) throws IOException, InterruptedException {
		Main.print = print;
		// load the properties
		getProperties(fileName);
		// display the properties
		displayProperties();


		// create the buffer
		print("Creating the buffer", print);
		ProdConsBuffer buffer = new ProdConsBuffer(Integer.parseInt(properties.getProperty("bufSz")));

		// create the producers
		int nProducers = Integer.parseInt(properties.getProperty("nProd"));
		int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
		print("Creating the producers", print);

		Producer[] producers = new Producer[nProducers];
		for (int i = 0; i < nProducers; i++) {
			producers[i] = new Producer(buffer, i);
		}

		Producer.setProdTime(prodTime);
		Producer.setMinProd(Integer.parseInt(properties.getProperty("minProd")));
		Producer.setMaxProd(Integer.parseInt(properties.getProperty("maxProd")));
		Producer.setPrint(print);

		// create the consumers
		int nConsumers = Integer.parseInt(properties.getProperty("nCons"));
		int consTime = Integer.parseInt(properties.getProperty("consTime"));
		print("Creating the consumers", print);

		Consumer[] consumers = new Consumer[nConsumers];
		for (int i = 0; i < nConsumers; i++) {
			consumers[i] = new Consumer(buffer, i + nProducers);
		}

		Consumer.setConsTime(consTime);
		Consumer.setPrint(print);

		// wait for the producers to finish
		for (Producer producer : producers) {
			producer.join();
		}
		print("All producers have finished", print);

		// verify that the buffer is empty and stop the consumers if it is not empty yet re ask at 50 ms intervals
		print("Verifying that the buffer is empty", print);
		while (buffer.nmsg() != 0) {
			print("Buffer is not empty, waiting 50 ms", print);
			print("Buffer size: " + buffer.nmsg(), print);
			sleep(50);
		}

		// stop the consumers
		print("Stopping the consumers", print);
		for (Consumer consumer : consumers) {
			consumer.stopRunning();
		}
		print("All consumers have finished", print);

		// verify if no message was lost
		print("Verifying that no message was lost", print);
		if (buffer.noErrors()) {
			print("No message was lost", print);
		} else {
			print("Some messages were lost", print);
		}
		return buffer.nmsg();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(main(args, "options-short", false));
	}
}