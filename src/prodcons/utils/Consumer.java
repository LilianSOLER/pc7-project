package prodcons.utils;

import java.util.Random;

import static prodcons.utils.Print.print;

public class Consumer extends Thread {

	private int id;
	private IProdConsBuffer buffer;

	private static int consTime;

	private boolean running = true;

	private static boolean print = false;

	private boolean isMulti = false;

	public Consumer(IProdConsBuffer buffer, int id, boolean isMulti) {
		this.id = id;
		this.buffer = buffer;
		this.isMulti = isMulti;
		this.start();
	}

	public static void setPrint(Boolean print) {
		Consumer.print = print;
	}

	public int getID() {
		return id;
	}

	public static void setConsTime(int consTime) {
		Consumer.consTime = consTime;
	}

	public void run() {
		print("Consumer " + id + " started", print);
		int k = 0;
		while (this.running) {
			try {
				if (isMulti) {
					Random random = new Random();
					k = random.nextInt(buffer.getSize() - 1) + 1;
				} else {
					k = 1;
				}
				consume(k);
				sleep(consTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void consume(int k) throws InterruptedException {
		print("Consumer " + id + " will consume " + k + " messages", print);
		Message[] messages = buffer.get(k);
		print("Consumer " + id + " consumed " + messages.length + " messages", print);
		// display the messages
		for (Message message : messages) {
			print("Consumer " + id + " consumed " + message.getMsg(), print);
		}
	}

	public void stopRunning() {
		this.running = false;
	}
}
