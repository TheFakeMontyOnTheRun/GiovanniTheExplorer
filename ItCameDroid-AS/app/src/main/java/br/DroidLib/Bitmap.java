package br.DroidLib;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bitmap {
	private android.graphics.Bitmap sprite;
	private float x;
	private float y;

	public Bitmap(Resources resources, int baseTypeId) {
		/*
		Resources r = context.getResources();
		Drawable sprite= r.getDrawable(resId);
		int width = sprite.getIntrinsicWidth();
		int height = sprite.getIntrinsicHeight();
		bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		sprite.setBounds(0, 0, width, height);
		sprite.draw(canvas);
	   */

		sprite = BitmapFactory.decodeResource(resources, baseTypeId);
	}

	Bitmap(android.graphics.Bitmap androidBitmap) {
		// TODO Auto-generated constructor stub
		sprite = androidBitmap;
	}

	public android.graphics.Bitmap getAndroidBitmap() {
		return sprite;
	}

	void setAndroidBitmap(android.graphics.Bitmap tile) {
		// TODO Auto-generated method stub
		sprite = tile;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		//	return sprite.getBounds().left;
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
//		Rect rect=sprite.getBounds();
//		rect.left=(int) x;
//		sprite.setBounds(rect);
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
//		return sprite.getBounds().top;
		return this.y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
//		Rect rect=sprite.getBounds();
//		rect.top=(int) y;
//		sprite.setBounds(rect);
		this.y = y;
	}

	public void draw(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub

		canvas.drawBitmap(sprite, x, y, paint);
	}

	public void prepareForGC() {
		sprite = null;
	}
}
