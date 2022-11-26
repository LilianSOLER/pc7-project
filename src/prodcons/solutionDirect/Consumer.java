package prodcons.solutionDirect;

public class Consumer extends Thread {

		private int id;
		private ProdConsBuffer buffer;

		private int consTime;

		private boolean running = true;

		public Consumer(ProdConsBuffer buffer, int id, int consTime) {
			this.id = id;
			this.buffer = buffer;
			this.consTime = consTime;
			this.start();
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

			// display the message
			System.out.println("Consumer " + id + " consumed");
		}

		public void stopRunning() {
			this.running = false;
		}
}
