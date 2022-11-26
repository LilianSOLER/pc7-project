package prodcons.utils;

public class Message {
	private String msg;
	private int id;

	public Message(String msg, int id) {
		this.msg = msg;
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public int getId() {
		return id;
	}
}