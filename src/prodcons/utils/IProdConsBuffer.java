package prodcons.utils;

public interface IProdConsBuffer {
	// put the message in the buffer
	public void put(Message message) throws InterruptedException;

	// get the message from the buffer
	public String get() throws InterruptedException;

	// return the number of messages in the buffer
	public int nmsg();

	// return the total number of put operations
	public int totmsg();

	boolean noErrors();
}