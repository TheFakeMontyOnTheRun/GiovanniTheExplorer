package br.odb.giovanni.game

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import br.odb.giovanni.R
import br.odb.giovanni.engine.Animation
import br.odb.giovanni.menus.ItCameView

class Dynamite(resources: Resources?, context: Context?) : Actor() {

    var timeToBlow: Long = (100 * 1000).toLong()
    private val blowAnimation: Animation = Animation()
    private var boomSound: MediaPlayer? = null

    override fun tick(timeInMS: Long) {
        super.tick(timeInMS)
        timeToBlow -= timeInMS
        if (timeToBlow < 0 && animation != blowAnimation) {
            animation = blowAnimation
            if (boomSound != null) {
                boomSound!!.start()
            }
        }
        if (!blowAnimation.playing) {
            kill()
        }
    }

    override fun touched(actor: Actor?) {
        if (!killed) {
            actor!!.kill()
        }
    }

    public override fun didMove() {}

    init {
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.bomba1))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.bomba2))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.bomba3))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.bomba4))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.bomba5))
        blowAnimation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.boom_03))
        blowAnimation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.boom_02))
        blowAnimation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.boom_01))
        blowAnimation.loop = false

        currentFrame = animation.getFrameReference(0)
        if (ItCameView.playSounds) {
            boomSound = MediaPlayer.create(context, R.raw.boom)
        }
        direction = 3
    }
}