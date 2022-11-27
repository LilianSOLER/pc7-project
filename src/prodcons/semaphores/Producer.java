package prodcons.semaphores;

import prodcons.utils.Message;

import java.util.Random;

public class Producer extends Thread {
	private int id;
	private ProdConsBuffer buffer;

	private static int prodTime = 10;

	private static int maxProd = 500;
	private static int minProd = 5;

	private boolean running = true;

	public Producer(ProdConsBuffer buffer, int id) {
		this.id = id;
		this.buffer = buffer;
		this.start();
	}

	public static void setProdTime(int prodTime) {
		Producer.prodTime = prodTime;
	}

	public static void setMinProd(int minProd) {
		Producer.minProd = minProd;
	}

	public static void setMaxProd(int maxProd) {
		Producer.maxProd = maxProd;
	}

	public void run() {
		System.out.println("Producer " + id + " started");

		//generate a random number of messages to produce
		Random random = new Random();
		int nMessages = random.nextInt(maxProd - minProd) + minProd;

		System.out.println("Producer " + id + " will produce " + nMessages + " messages");

		while (this.running && nMessages > 0) {
			try {
				produce(new Message("message " + id + " - " + nMessages , id));
				nMessages--;
				sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.interrupt();
			}
		}
		System.out.println("Producer " + id + " finished");
	}

	public void produce(Message message) throws InterruptedException {
		// put the message in the buffer
		buffer.put(message);
		System.out.println("Producer " + id + " produced");
	}

	public void stopRunning() {
		this.running = false;
	}
}
