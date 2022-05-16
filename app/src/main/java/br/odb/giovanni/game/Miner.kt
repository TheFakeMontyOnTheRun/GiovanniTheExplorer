package br.odb.giovanni.game

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import br.odb.giovanni.R
import br.odb.giovanni.engine.Animation
import br.odb.giovanni.menus.ItCameView

class Miner(resources: Resources, context: Context) : Actor() {

	//WTF - what was I smoking when I created this?!
	private val minerLocalAnimation: Array<Animation?> = arrayOfNulls(4)

	private var stepSound: MediaPlayer? = null

	init {
		minerLocalAnimation[0] = Animation()
		minerLocalAnimation[0]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero0_1
			)
		)
		minerLocalAnimation[0]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero0_2
			)
		)
		minerLocalAnimation[0]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero0_3
			)
		)
		minerLocalAnimation[0]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero0_4
			)
		)
		minerLocalAnimation[1] = Animation()
		minerLocalAnimation[1]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero1_1
			)
		)
		minerLocalAnimation[1]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero1_2
			)
		)
		minerLocalAnimation[1]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero1_3
			)
		)
		minerLocalAnimation[1]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero1_4
			)
		)
		minerLocalAnimation[2] = Animation()
		minerLocalAnimation[2]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero2_1
			)
		)
		minerLocalAnimation[2]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero2_2
			)
		)
		minerLocalAnimation[2]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero2_3
			)
		)
		minerLocalAnimation[2]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero2_4
			)
		)
		minerLocalAnimation[3] = Animation()
		minerLocalAnimation[3]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero3_1
			)
		)
		minerLocalAnimation[3]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero3_2
			)
		)
		minerLocalAnimation[3]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero3_3
			)
		)
		minerLocalAnimation[3]!!.addFrame(
			BitmapFactory.decodeResource(
				resources,
				R.drawable.hero3_4
			)
		)

		if (ItCameView.playSounds) {
			stepSound = MediaPlayer.create(context, R.raw.steps)
		}
	}

	public override fun didMove() {
		if (stepSound != null) {
			stepSound!!.start()
		}
	}

	override var state: ActorStates
		get() = super.state
		set(state) {
			super.state = state
			if (state === ActorStates.STILL) try {
				minerLocalAnimation[super.direction]!!.currentFrame = 0
				currentFrame = minerLocalAnimation[super.direction]!!.currentFrameReference
			} catch (ignored: Exception) {
			}
		}

	override fun tick(timeInMS: Long) {
		if (state === ActorStates.MOVING) {
			minerLocalAnimation[super.direction]!!.tick()
			currentFrame = minerLocalAnimation[super.direction]!!.currentFrameReference
		}
	}

	override fun touched(actor: Actor?) {
		if (actor is Monster) {
			kill()
		}
	}
}