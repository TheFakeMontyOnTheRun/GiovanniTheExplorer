package br.odb.game;

import android.graphics.Rect;

import br.odb.engine.Animation;
import br.odb.engine.Bitmap;
import br.odb.engine.Constants;
import br.odb.menus.ItCameView;

public abstract class Actor {

	public Level level;
	public boolean killed;
	private boolean visible = true;
	Animation animation;
	Bitmap currentFrame;
	private Vec2 position;
	private int direction;
	private Rect bounds;
	private states state;

	Actor() {
		bounds = new Rect();
		setDirection(0);
		setPosition(new Vec2(0, 0));
		setState(states.STILL);
	}

	public void tick(long timeInMS) {
		animation.tick(timeInMS);
		currentFrame = animation.getCurrentFrameReference().getBitmap();
	}

	private Vec2 getScreenPosition() {
		Vec2 toReturn = new Vec2();

		toReturn.x = (-Level.camera.x + (ItCameView.viewport.right / 2.0f) + getPosition().x - currentFrame.getAndroidBitmap().getWidth() / 2.0f);
		toReturn.y = (-Level.camera.y + (ItCameView.viewport.bottom / 2.0f) + getPosition().y - currentFrame.getAndroidBitmap().getHeight() + (Constants.BASETILEHEIGHT / 2.0f));

		return toReturn;

	}

	public void draw(android.graphics.Canvas canvas,
					 android.graphics.Paint paint) {

		if (!visible) {
			return;
		}

		try {
			Vec2 screenPos = getScreenPosition();
			currentFrame.setX(screenPos.x);
			currentFrame.setY(screenPos.y);
			// currentFrame.setX(-Level.camera.X + super.getPosition().X);
			// currentFrame.setY(-Level.camera.Y + super.getPosition().Y);
			currentFrame.draw(canvas, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void move(float x, float y) {
		move((int) x, (int) y);
		didMove();
	}

	public void moveTo(float x, float y) {
		moveTo((int) x, (int) y);
		didMove();
	}

	public void move(int x, int y) {
		position.x += x;
		position.y += y;
		didMove();
	}

	private void moveTo(int x, int y) {
		position.x = x;
		position.y = y;
		didMove();
	}

	/**
	 * @return the direction
	 */
	int getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the position
	 */
	public Vec2 getPosition() {
		return position;
	}

	/**
	 */
	public void setPosition(Vec2 Position) {
		position = Position;
	}

	public Rect getBounds() {
		// TODO Auto-generated method stub
		return bounds;
	}

	/**
	 * @return the state
	 */
	states getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	void setState(states state) {
		this.state = state;
	}

	void kill() {
//		position = null;
//		bounds = null;
//		state = null;
//		currentFrame = null;
//		animation = null;
//		level = null;
		killed = true;
		visible = false;
	}

	public abstract void touched(Actor actor);

	protected abstract void didMove();

	public void prepareForGC() {
		position = null;
		bounds = null;
		state = null;
		currentFrame.prepareForGC();
		currentFrame = null;
		animation.prepareForGC();
		animation = null;
		level = null;
	}

	public enum states {
		STILL, MOVING, DYING
	}
}
