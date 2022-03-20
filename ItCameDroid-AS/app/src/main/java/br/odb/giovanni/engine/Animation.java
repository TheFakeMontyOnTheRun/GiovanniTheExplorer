package br.odb.giovanni.engine;

import java.util.ArrayList;

public class Animation implements Runnable {
    public boolean play;
    private final long interval;
    private ArrayList<Bitmap> frames;
    private int currentFrame;
    private boolean loop;

    public Animation() {
        frames = new ArrayList<>();
        currentFrame = 0;
        interval = 50;
        loop = true;
        play = true;
    }

    @Override
    public void run() {

        while (play) {
            tick(interval);
        }
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void addFrame(br.odb.giovanni.engine.Bitmap bitmap) {
        frames.add((bitmap));

    }

    public Bitmap getCurrentFrameReference() {
        return getFrameReference(currentFrame);
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
