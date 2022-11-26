package prodcons.solutionDirect;

import prodcons.utils.Message;

public class Producer extends Thread {
	private int id;
	private ProdConsBuffer buffer;

	private int prodTime;

	private boolean running = true;

	public Producer(ProdConsBuffer buffer, int id, int prodTime) {
		this.id = id;
		this.buffer = buffer;
		this.prodTime = prodTime;
		this.start();
	}

	public void run() {
		System.out.println("Producer " + id + " started");
		while (this.running) {
			try {
				produce(new Message("message " + id, id));
				sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.interrupt();
			}
		}
	}

	public void produce(Message message) throws InterruptedException {
		// put the message in the buffer

		System.out.println("Producer " + id + " produced");
	}

	public void stopRunning() {
		this.running = false;
	}
}
