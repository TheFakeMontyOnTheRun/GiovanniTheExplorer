package br.DroidLib;

public class Frame {
	private Sound sound;
	private br.DroidLib.Bitmap bitmap;

	public Frame(br.DroidLib.Bitmap bitmap2) {
		sound = null;
		bitmap = bitmap2;
	}

	/**
	 * @return the sound
	 */
	public Sound getSound() {
		return sound;
	}

	/**
	 * @param sound the sound to set
	 */
	public void setSound(Sound sound) {
		this.sound = sound;
	}

	/**
	 * @return the bitmap
	 */
	public br.DroidLib.Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(br.DroidLib.Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void prepareForGC() {
		bitmap.prepareForGC();
		sound = null;
	}
}
