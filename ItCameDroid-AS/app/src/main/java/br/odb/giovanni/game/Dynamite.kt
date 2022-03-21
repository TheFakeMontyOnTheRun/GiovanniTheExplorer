package br.odb.giovanni.game

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import br.odb.giovanni.R
import br.odb.giovanni.engine.Animation
import br.odb.giovanni.engine.Bitmap
import br.odb.giovanni.menus.ItCameView

class Dynamite(resources: Resources?, context: Context?) : Actor() {
    @JvmField
    var timeToBlow: Long
    private val blowAnimation: Animation
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
        if (!blowAnimation.play) {
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
        timeToBlow = (100 * 1000).toLong()
        animation = Animation()
        animation!!.addFrame(Bitmap(resources, R.drawable.bomba1))
        animation!!.addFrame(Bitmap(resources, R.drawable.bomba2))
        animation!!.addFrame(Bitmap(resources, R.drawable.bomba3))
        animation!!.addFrame(Bitmap(resources, R.drawable.bomba4))
        animation!!.addFrame(Bitmap(resources, R.drawable.bomba5))
        blowAnimation = Animation()
        blowAnimation.addFrame(Bitmap(resources, R.drawable.boom_03))
        blowAnimation.addFrame(Bitmap(resources, R.drawable.boom_02))
        blowAnimation.addFrame(Bitmap(resources, R.drawable.boom_01))
        blowAnimation.setLoop(false)
        currentFrame = animation!!.getFrameReference(0)
        if (ItCameView.playSounds) {
            boomSound = MediaPlayer.create(context, R.raw.boom)
        }
        direction = 3
    }
}