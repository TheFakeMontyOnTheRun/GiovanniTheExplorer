package br.odb.engine;

public class Frame {
	private Sound sound;
	private br.odb.engine.Bitmap bitmap;

	public Frame(br.odb.engine.Bitmap bitmap2) {
		sound = null;
		bitmap = bitmap2;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public br.odb.engine.Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(br.odb.engine.Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void prepareForGC() {
		bitmap.prepareForGC();
		sound = null;
	}
}
