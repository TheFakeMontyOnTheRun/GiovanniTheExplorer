package br.odb.giovanni.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Objects;

import br.odb.giovanni.R;
import br.odb.giovanni.engine.Constants;
import br.odb.giovanni.engine.VirtualPad;
import br.odb.giovanni.game.Actor;
import br.odb.giovanni.game.Level;
import br.odb.giovanni.game.LevelFactory;
import br.odb.giovanni.game.Miner;
import br.odb.giovanni.game.MiniMapWidget;
import br.odb.giovanni.game.Vec2;

public class ItCameView extends View implements Runnable, VirtualPadClient,
		OnTouchListener {

	private static final long INTERVAL = 75;
	private static final int KB_UP = 0;
	private static final int KB_RIGHT = 1;
	private static final int KB_DOWN = 2;
	private static final int KB_LEFT = 3;
	public static boolean playSounds = true;
	public static final Rect viewport = new Rect();
	private static Level level;
	public boolean playing = false;
	private long timeSinceAcquiredFocus = 0;
	private boolean drawOnScreenController;
	private final VirtualPad vPad;
	private final boolean[] keyMap;
	private boolean running = true;
	private final Paint paint;
	private final Vec2 camera;
	private final Miner actor;
	private final Bitmap controlPadOverlay;

	public ItCameView(Context context, boolean enableSounds) {
		super(context);

		setFocusable(true);
		setClickable(true);
		setLongClickable(true);


		playSounds = enableSounds;

		controlPadOverlay = BitmapFactory.decodeResource(getResources(), R.drawable.control_brown);

		vPad = new VirtualPad(this);

		this.requestFocus();
		this.setFocusableInTouchMode(true);
		keyMap = vPad.getKeyMap();
		camera = new Vec2(0, 0);

		if (MainMenuActivity.needsReset) {

			level = LevelFactory.createRandomLevel(
					br.odb.giovanni.game.Constants.SIZEX,
					br.odb.giovanni.game.Constants.SIZEY, getResources(), context);

			MainMenuActivity.needsReset = false;
		}

		MiniMapWidget map = new MiniMapWidget(level);

		actor = level.getMiner();


		paint = new Paint();
		setBackgroundColor(Color.BLACK);
		Thread monitorThread = new Thread(this, "main game ticker");
		monitorThread.setPriority(Thread.MIN_PRIORITY);
		monitorThread.start();

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) this.getContext()
		).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int screenWidth = displaymetrics.widthPixels;
		int screenHeight = displaymetrics.heightPixels;
		viewport.set(0, 0, screenWidth, screenHeight);

		setOnTouchListener(this);


	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean handled = false;

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			keyMap[KB_UP] = false;
			handled = true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			keyMap[KB_DOWN] = false;
			handled = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			keyMap[KB_LEFT] = false;
			handled = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			keyMap[KB_RIGHT] = false;
			handled = true;
		}
		return handled;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean handled = false;

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			keyMap[KB_UP] = true;
			handled = true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			keyMap[KB_DOWN] = true;
			handled = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			keyMap[KB_LEFT] = true;
			handled = true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			keyMap[KB_RIGHT] = true;
			handled = true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK)
			System.exit(0);

		return handled;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		int screenWidth = getWidth();
		int screenHeight = getHeight();

		if (hasFocus) {
			timeSinceAcquiredFocus = 5000;
		}

		viewport.set(0, 0, screenWidth, screenHeight);
	}

	private ArrayList getGameControllerIds() {
		ArrayList gameControllerDeviceIds = new ArrayList();

		int[] deviceIds = InputDevice.getDeviceIds();
		for (int deviceId : deviceIds) {
			InputDevice dev = InputDevice.getDevice(deviceId);
			int sources = dev.getSources();

			// Verify that the device has gamepad buttons, control sticks, or both.
			if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
					|| ((sources & InputDevice.SOURCE_JOYSTICK)
					== InputDevice.SOURCE_JOYSTICK)) {
				// This device is a game controller. Store its device ID.
				if (!gameControllerDeviceIds.contains(deviceId)) {
					gameControllerDeviceIds.add(deviceId);
				}
			}
		}

		return gameControllerDeviceIds;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		synchronized (actor) {

			vPad.setBounds(0, 0, getWidth(), getHeight());

			level.setCurrentCamera(camera);

			if (level != null && paint != null) {
				level.setCurrentCamera(actor.getPosition());
				level.draw(canvas, paint);
			}

			Objects.requireNonNull(paint).setARGB(255, 0, 0, 0);

			if (drawOnScreenController) {

				vPad.draw(canvas);
			}

			drawMap(canvas);
			paint.setColor(Color.YELLOW);

			paint.setFakeBoldText(true);

			canvas.drawText("Você derrotou " + level.dead
							+ " monstros; Tempo para a detonação: "
							+ (level.dynamite.timeToBlow / 1000) + "s", 0,
					getHeight() - 50, paint);

			paint.setFakeBoldText(false);
		}


		if (timeSinceAcquiredFocus > 0) {
			String text = "Jogo começando em " + (timeSinceAcquiredFocus / 1000);
			paint.getTextBounds(text, 0, text.length(), bounds);
			float prevSize = paint.getTextSize();
			paint.setTextSize(30);
			canvas.drawText(text, (getWidth()) / 2 - bounds.width(), getHeight() / 2, paint);
			paint.setTextSize(prevSize);
		}
	}

	final Rect bounds = new Rect();

	private void drawMap(Canvas canvas) {

		int x2;
		int y2;

//		paint.setAntiAlias(true);
//		paint.setDither(true);
//		paint.setFilterBitmap(true);

		paint.setColor(Color.YELLOW);
		paint.setAlpha(128);

		for (int x = 0; x < level.getWidth(); ++x) {
			for (int y = 0; y < level.getHeight(); ++y) {

				if (!level.mayMoveTo(x, y)) {

					canvas.drawRect(x * 5, y * 5, (x + 1) * 5, (y + 1) * 5,
							paint);
				}
			}
		}

		paint.setColor(Color.BLUE);
		paint.setAlpha(128);

		for (Actor a : level.getActors()) {

			if (a.killed) {
				continue;
			}

			x2 = (int) (a.getPosition().x / Constants.BASETILEWIDTH);
			y2 = (int) (a.getPosition().y / Constants.BASETILEHEIGHT);

			canvas.drawRect(x2 * 5, y2 * 5, (x2 + 1) * 5,
					(y2 + 1) * 5, paint);
		}

		paint.setAlpha(255);
	}

	@Override
	public void run() {

		running = true;

		while (running) {


			try {
				drawOnScreenController = getGameControllerIds().size() == 0;
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
				running = false;
			}

			if (!playing) {
				continue;
			}

			if (timeSinceAcquiredFocus > 0) {
				timeSinceAcquiredFocus -= INTERVAL;
				postInvalidate();
				continue;
			}


			handleKeys(keyMap);

			level.tick(INTERVAL);

			if (level.gameShouldEnd) {
				running = false;
				Intent intent = ((ItCameFromTheCaveActivity) this.getContext())
						.getIntent();
				intent.putExtra("result", level.getMiner().killed ? "failure"
						: "victory");
				((ItCameFromTheCaveActivity) this.getContext()).setResult(
						Activity.RESULT_OK, intent);
				((ItCameFromTheCaveActivity) this.getContext()).finish();
				return;
			}

			if (level.dynamite.killed
					&& level.dynamite.getPosition()
					.isCloseEnoughToConsiderEqualTo(
							level.getMiner().getPosition())) {
				Intent intent = ((ItCameFromTheCaveActivity) this.getContext())
						.getIntent();

				intent.putExtra("result", level.getMiner().killed ? "failure"
						: "victory");
				((ItCameFromTheCaveActivity) this.getContext()).setResult(
						Activity.RESULT_OK, intent);

				running = false;
				((ItCameFromTheCaveActivity) this.getContext()).finish();
			}

			postInvalidate();
		}
	}

	public void pause() {
		running = false;
	}

	@Override
	public void handleKeys(boolean[] keymap) {

		if (timeSinceAcquiredFocus > 0) {
			return;
		}

		synchronized (actor) {

			boolean handled = false;
			int x = 0;
			int y = 0;
			int currentX = (int) (actor.getPosition().x / Constants.BASETILEWIDTH);
			int currentY = (int) (actor.getPosition().y / Constants.BASETILEHEIGHT);

			if (keymap[KB_UP]) {
				y -= 1.25f;
				actor.setDirection(0);
				handled = true;
			} else if (keymap[KB_DOWN]) {
				y += 1.25f;
				actor.setDirection(2);
				handled = true;
			} else if (keymap[KB_LEFT]) {
				x -= 1.25f;
				actor.setDirection(3);
				handled = true;
			} else if (keymap[KB_RIGHT]) {
				actor.setDirection(1);
				x += 1.25f;
				handled = true;
			}

			if (handled) {
				if (level.mayMoveTo(currentX + x, currentY + y)) {
					actor.move(x * Constants.BASETILEWIDTH, y
							* Constants.BASETILEHEIGHT);
					actor.setState(Actor.states.MOVING);
				}
			} else
				actor.setState(Actor.states.STILL);

			if (actor.getPosition().x < 0)
				actor.getPosition().x = 0;

			if (actor.getPosition().y < 0)
				actor.getPosition().y = 0;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return vPad.onTouchEvent(event);
	}

	@Override
	public Bitmap getBitmapOverlay() {
		return controlPadOverlay;
	}
}
