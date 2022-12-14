package prodcons.utils;


import java.util.Random;

import static prodcons.utils.Print.print;

public class Producer extends Thread {
	private int id;
	private IProdConsBuffer buffer;

	private static int prodTime = 10;

	private static int maxProd = 500;
	private static int minProd = 5;

	private boolean running = true;

	private static boolean print = false;

	public Producer(IProdConsBuffer buffer, int id) {
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

	public static void setPrint(Boolean print) {
		Producer.print = print;
	}

	public void run() {
		print("Producer " + id + " started", print);

		//generate a random number of messages to produce
		Random random = new Random();
		int maxMinDiff = maxProd - minProd;
		if (maxMinDiff < 0) {
			maxMinDiff = -maxMinDiff;
		}
		int nMessages = random.nextInt(maxMinDiff) + minProd;
		if (nMessages < 0) {
			nMessages = -nMessages;
		}

		print("Producer " + id + " will produce " + nMessages + " messages", print);

		while (this.running && nMessages > 0) {
			try {
				produce(new Message("message " + id + " - " + nMessages, id));
				nMessages--;
				sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.interrupt();
			}
		}
		print("Producer " + id + " finished", print);
	}

	public void produce(Message message) throws InterruptedException {
		// put the message in the buffer
		buffer.put(message);
		print("Producer " + id + " produced : " + message.getMsg(), print);
	}

	public void stopRunning() {
		this.running = false;
	}
}
