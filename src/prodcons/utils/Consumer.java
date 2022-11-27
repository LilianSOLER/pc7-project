package prodcons.utils;

import static prodcons.utils.Print.print;

public class Consumer extends Thread {

	private int id;
	private IProdConsBuffer buffer;

	private static int consTime;

	private boolean running = true;

	private static boolean print = false;

	public Consumer(IProdConsBuffer buffer, int id) {
		this.id = id;
		this.buffer = buffer;
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
		while (this.running) {
			try {
				consume();
				sleep(consTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public void consume() throws InterruptedException {
		// get the message from the buffer
		String message = buffer.get();
		// display the message
		print("Consumer " + id + " consumed :" + message, print);
	}

	public void stopRunning() {
		this.running = false;
	}
}
