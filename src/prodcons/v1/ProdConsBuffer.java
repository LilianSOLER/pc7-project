package prodcons.v1;

import prodcons.utils.IProdConsBuffer;
import prodcons.utils.Message;

import static prodcons.utils.Print.print;

public class ProdConsBuffer implements IProdConsBuffer {

	private Message[] buffer;
	private int in;
	private int out;
	private int count;

	private int totalMessages;

	private static boolean print = false;

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
	public Message get() throws InterruptedException {
		synchronized (this) {
			while (count == 0) {
				wait();
			}
			Message message = buffer[out];
			out = (out + 1) % buffer.length;
			count--;
			printBufferState();
			notifyAll();
			return message;
		}
	}

	@Override
	public Message[] get(int k) throws InterruptedException {
		synchronized (this) {
			while (count < k) {
				wait();
			}
			Message[] messages = new Message[k];
			for (int i = 0; i < k; i++) {
				messages[i] = buffer[out];
				out = (out + 1) % buffer.length;
				count--;
			}
			printBufferState();
			notifyAll();
			return messages;
		}
	}

	@Override
	public int nmsg() {
		return count;
	}


	@Override
	public int totmsg() {
		return totalMessages;
	}

	public boolean noErrors() {
		return count == 0 && in == out;
	}

	@Override
	public int getSize() {
		return buffer.length;
	}

	public void printBuffer() {
		print("Buffer: ", print);
		for (Message message : buffer) {
			if (message != null) {
				print(message.getMsg(), print);
			}
		}
	}

	public void printBufferState() {
		print("Buffer state: ", print);
		print("in: " + in, print);
		print("out: " + out, print);
		print("count: " + count, print);
		print("totalMessages: " + totalMessages, print);
	}

	public void printBufferStateAndBuffer() {
		printBufferState();
		printBuffer();
	}
}
