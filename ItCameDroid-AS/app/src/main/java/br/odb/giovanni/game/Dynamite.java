package br.odb.giovanni.game;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import br.odb.giovanni.R;
import br.odb.giovanni.engine.Animation;
import br.odb.giovanni.engine.Bitmap;
import br.odb.giovanni.menus.ItCameView;


public class Dynamite extends Actor {

	public long timeToBlow;
	private final Animation blowAnimation;
	private MediaPlayer boomSound = null;

	public Dynamite(Resources resources, Context context) {
		super();

		timeToBlow = 100 * 1000;

		animation = new Animation();
		animation.addFrame(new Bitmap(resources, R.drawable.bomba1));
		animation.addFrame(new Bitmap(resources, R.drawable.bomba2));
		animation.addFrame(new Bitmap(resources, R.drawable.bomba3));
		animation.addFrame(new Bitmap(resources, R.drawable.bomba4));
		animation.addFrame(new Bitmap(resources, R.drawable.bomba5));

		blowAnimation = new Animation();
		blowAnimation.addFrame(new Bitmap(resources, R.drawable.boom_03));
		blowAnimation.addFrame(new Bitmap(resources, R.drawable.boom_02));
		blowAnimation.addFrame(new Bitmap(resources, R.drawable.boom_01));
		blowAnimation.setLoop(false);

		currentFrame = animation.getFrameReference(0);

		if (ItCameView.playSounds) {

			boomSound = MediaPlayer.create(context, R.raw.boom);
		}

		this.setDirection(3);
	}

	@Override
	public void tick(long timeInMS) {

		super.tick(timeInMS);

		timeToBlow -= timeInMS;

		if (timeToBlow < 0 && animation != blowAnimation) {
			animation = blowAnimation;

			if (boomSound != null) {

				boomSound.start();
			}
		}

		if (!blowAnimation.play) {
			kill();
		}
	}

	@Override
	public void touched(Actor actor) {
		if (!killed) {
			actor.kill();
		}
	}

	@Override
	public void didMove() {
	}
}
