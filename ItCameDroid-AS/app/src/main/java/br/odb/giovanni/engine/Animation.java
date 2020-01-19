package br.odb.giovanni.engine;

import java.util.ArrayList;

public class Animation implements Runnable {
	public boolean play;
	private final long interval;
	private int framesPerSecond;
	private ArrayList<Bitmap> frames;
	private int currentFrame;
	private boolean loop;
	private Thread controller;

	public Animation() {
		frames = new ArrayList<>();
		currentFrame = 0;
		interval = 50;
		loop = true;
		play = true;
		framesPerSecond = 24;
		controller = null;
	}

	@Override
	public void run() {

		while (play) {
			// if (currentFrame==frames.size()-1)
			// parar=true;
			tick(interval);
		}
	}

	/**
	 * @return the framesPerSeconds
	 */
	public int getFramesPerSecond() {
		return framesPerSecond;
	}

	/**
	 * @param framesPerSeconds the framesPerSeconds to set
	 */
	public void setFramesPerSecond(int framesPerSeconds) {
		this.framesPerSecond = framesPerSeconds;
	}

	/**
	 * @return the frames
	 */
	public ArrayList<Bitmap> getFrames() {
		return frames;
	}

	/**
	 * @param frames the frames to set
	 */
	public void setFrames(ArrayList<Bitmap> frames) {
		this.frames = frames;
	}

	/**
	 * @return the currentFrame
	 */
	public int getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * @param currentFrame the currentFrame to set
	 */
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	/**
	 * @return the loop
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * @param loop the loop to set
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void addFrame(br.odb.giovanni.engine.Bitmap bitmap) {
		frames.add((bitmap));

	}

	public Bitmap getCurrentFrameReference() {
		return getFrameReference(currentFrame);
	}

	public void start() {
		controller = new Thread(this, "animation ticker");
		controller.start();
	}

	public Bitmap getFrameReference(int i) {
		return frames.get(i);
	}

	public void tick(long timeInMS) {

		if (play) {
			currentFrame++;
		}

		if (currentFrame == frames.size()) {

			currentFrame = 0;

			if (!loop) {

				play = false;
			}
		}
	}

	public void prepareForGC() {
		play = false;
		Thread.yield();

		for (Bitmap f : frames) {
			f.prepareForGC();
		}

		frames.clear();
		frames = null;
	}
}
