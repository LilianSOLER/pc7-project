package prodcons.semaphores;

public class Consumer extends Thread {

		private int id;
		private ProdConsBuffer buffer;

		private static int consTime;

		private boolean running = true;

		public Consumer(ProdConsBuffer buffer, int id) {
			this.id = id;
			this.buffer = buffer;
			this.start();
		}

		public int getID() {
			return id;
		}

	public static void setConsTime(int consTime) {
		Consumer.consTime = consTime;
	}

	public void run() {
			System.out.println("Consumer " + id + " started");
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
			System.out.println("Consumer " + id + " consumed message: " + message);
		}

		public void stopRunning() {
			this.running = false;
		}
}
