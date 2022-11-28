package prodcons.utils;

public interface IProdConsBuffer {

	// put the message in the buffer
	public void put(Message message) throws InterruptedException;

	// get the message from the buffer
	public Message get() throws InterruptedException;

	// return the number of messages in the buffer
	public int nmsg();

	// return the total number of put operations
	public int totmsg();

	/**
	 * Retrieve n consecutive messages from the prodcons buffer
	 **/
	public Message[] get(int k) throws InterruptedException;

	// return a boolean indicating if the buffer has occurred an error
	public boolean noErrors();

	// return the buffer size
	public int getSize();
}