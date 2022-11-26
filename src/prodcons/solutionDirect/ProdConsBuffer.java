package prodcons.solutionDirect;

import prodcons.utils.IProdConsBuffer;
import prodcons.utils.Message;

public class ProdConsBuffer implements IProdConsBuffer {

	@Override
	public void put(Message message) throws InterruptedException {

	}

	@Override
	public String get() throws InterruptedException {
		return null;
	}

	@Override
	public int nmsg() {
		return 0;
	}

	@Override
	public int totmsg() {
		return 0;
	}
}
