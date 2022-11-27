package prodcons.semaphores;

import prodcons.utils.IProdConsBuffer;
import prodcons.utils.Message;

import java.util.concurrent.Semaphore;

import static prodcons.utils.Print.print;

public class ProdConsBuffer implements IProdConsBuffer {

	private Message[] buffer;
	private int in;
	private int out;

	private Semaphore notFull = new Semaphore(10);
	private Semaphore notEmpty = new Semaphore(0);
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
		notFull = new Semaphore(size);
	}

	@Override
	public void put(Message message) throws InterruptedException {
		notFull.acquire();
		synchronized (this) {
			buffer[in] = message;
			in = (in + 1) % buffer.length;
			count++;
			totalMessages++;
			printBufferState();
			notEmpty.release();
		}

	}

	@Override
	public String get() throws InterruptedException {
		notEmpty.acquire();
		synchronized (this) {
			String message = buffer[out].getMsg();
			out = (out + 1) % buffer.length;
			count--;
			printBufferState();
			notifyAll();
			notFull.release();
			return message;
		}
	}

	@Override
	public int nmsg() {
		return (in - out) % buffer.length;
	}

	@Override
	public int totmsg() {
		return totalMessages;
	}

	public boolean noErrors() {
		return count == 0 && in == out;
	}

	public void printBuffer() {
		print("Buffer: ", print);
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] != null) {
				print(buffer[i].getMsg(), print);
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
