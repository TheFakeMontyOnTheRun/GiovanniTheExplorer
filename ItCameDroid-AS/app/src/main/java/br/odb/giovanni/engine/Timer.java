package br.odb.giovanni.engine;

public class Timer implements Runnable {

	private int secsToWait;
	private Thread callback;

	/**
	 * @return the callback
	 */
	public Thread getCallback() {
		return callback;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(Thread callback) {
		this.callback = callback;
	}

	/**
	 * @return the secsToWait
	 */
	public int getSecsToWait() {
		return secsToWait;
	}

	/**
	 * @param secsToWait the secsToWait to set
	 */
	public void setSecsToWait(int secsToWait) {
		this.secsToWait = secsToWait;
	}

	public void start() {
		Thread waitTimer = new Thread(this, "timer ticker");
		waitTimer.start();
	}


	@Override
	public void run() {
		try {
			Thread.sleep(this.secsToWait);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (callback != null)
			callback.start();
	}
}
