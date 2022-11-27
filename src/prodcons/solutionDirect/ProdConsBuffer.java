package prodcons.solutionDirect;

import prodcons.utils.IProdConsBuffer;
import prodcons.utils.Message;

public class ProdConsBuffer implements IProdConsBuffer {

	private Message[] buffer;
	private int in;
	private int out;
	private int count;

	private int totalMessages;

	public ProdConsBuffer() {
		buffer = new Message[10];
		in = 0;
		out = 0;
		count = 0;
	}

	public ProdConsBuffer(int size) {
		buffer = new Message[size];
		in = 0;
		out = 0;
		count = 0;
	}

	@Override
	public void put(Message message) {
		synchronized (this) {
			while (count == buffer.length) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			buffer[in] = message;
			in = (in + 1) % buffer.length;
			count++;
			totalMessages++;
			printBufferState();
			notifyAll();
		}
	}

	@Override
	public String get() throws InterruptedException {
		synchronized (this) {
			while (count == 0) {
				wait();
			}
			String message = buffer[out].getMsg();
			out = (out + 1) % buffer.length;
			count--;
			printBufferState();
			notifyAll();
			return message;
		}
	}

	@Override
	public int nmsg() {
		return in - out;
	}

	@Override
	public int totmsg() {
		return totalMessages;
	}

	public boolean noErrors() {
		return count == 0 && in == out;
	}

	public void printBuffer() {
		System.out.println("Buffer: ");
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] != null) {
				System.out.println(buffer[i].getMsg());
			}
		}
	}

	public void printBufferState() {
		System.out.println("Buffer state: ");
		System.out.println("in: " + in);
		System.out.println("out: " + out);
		System.out.println("count: " + count);
		System.out.println("totalMessages: " + totalMessages);
	}

	public void printBufferStateAndBuffer() {
		printBufferState();
		printBuffer();
	}
}
