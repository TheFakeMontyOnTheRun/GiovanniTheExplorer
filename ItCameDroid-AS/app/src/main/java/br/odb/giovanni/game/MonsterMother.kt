package br.odb.giovanni.game

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import br.odb.giovanni.R
import br.odb.giovanni.menus.ItCameView

class MonsterMother(private val resources: Resources, context: Context?) : Actor() {

    private var timeElapsed: Long = 0
    private var spawnSound: MediaPlayer? = null
    private var kill: MediaPlayer? = null

    fun generate(timeElapsed: Long): Monster? {

        this.timeElapsed += timeElapsed

        var generated: Monster? = null

        if (this.timeElapsed > 400) {

            this.timeElapsed -= 400

            generated = Monster(resources, kill)

            if (spawnSound != null && !spawnSound!!.isPlaying) {
                spawnSound!!.start()
            }

            generated.position = position
        }
        return generated
    }

    override fun touched(actor: Actor?) {}
    public override fun didMove() {}

    init {
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.vulcan_01))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.vulcan_02))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.vulcan_03))
        currentFrame = animation.getFrameReference(0)
        if (ItCameView.playSounds) {
            spawnSound = MediaPlayer.create(context, R.raw.spawn)
            kill = MediaPlayer.create(context, R.raw.monsterkill)
        }
    }
}