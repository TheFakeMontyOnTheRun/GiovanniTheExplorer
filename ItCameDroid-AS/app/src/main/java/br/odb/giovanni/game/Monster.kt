package br.odb.giovanni.game

import android.content.res.Resources
import android.media.MediaPlayer
import br.odb.giovanni.R
import br.odb.giovanni.engine.Animation
import br.odb.giovanni.engine.Bitmap
import br.odb.giovanni.engine.Constants

class Monster(resources: Resources?, kill: MediaPlayer?) : Actor() {
    @JvmField
    var actorTarget: Actor? = null
    private var timeToMove: Long = 0
    private val kill: MediaPlayer?
    override fun tick(timeInMS: Long) {
        super.tick(timeInMS)
        timeToMove -= timeInMS
        timeToMove = if (timeToMove < 0) {
            300
        } else {
            return
        }
        val currentX = position!!.x / Constants.BASETILEWIDTH
        val currentY = position!!.y / Constants.BASETILEHEIGHT
        val targetX = actorTarget!!.position!!.x / Constants.BASETILEWIDTH
        val targetY = actorTarget!!.position!!.y / Constants.BASETILEHEIGHT
        val dirX: Float
        val dirY: Float
        dirX = if (targetX < currentX) {
            -0.5f
        } else if (targetX > currentX) {
            0.5f
        } else {
            0f
        }
        dirY = if (targetY < currentY) {
            -0.5f
        } else if (targetY > currentY) {
            0.5f
        } else {
            0f
        }
        if (level!!.mayMoveTo(currentX + dirX, currentY + dirY)) {
            move(
                Constants.BASETILEWIDTH * dirX,
                Constants.BASETILEHEIGHT * dirY
            )

            //try to move around obstacle
        } else if (level!!.mayMoveTo(currentX, currentY + 0.5f)) {
            move(0f, Constants.BASETILEHEIGHT * 0.5f)
        } else if (level!!.mayMoveTo(currentX + 0.5f, currentY)) {
            move(Constants.BASETILEWIDTH * 0.5f, 0.0f)
        } else if (level!!.mayMoveTo(currentX - 0.5f, currentY)) {
            move(Constants.BASETILEWIDTH * -0.5f, 0.0f)
        } else if (level!!.mayMoveTo(currentX, currentY - 0.5f)) {
            move(0f, Constants.BASETILEHEIGHT * 0.5f)
        }
    }

    override fun touched(actor: Actor?) {
        if (actor is Monster) {
            kill?.start()
            kill()
        }
    }

    public override fun didMove() {}

    init {
        animation = Animation()
        animation!!.addFrame(Bitmap(resources, R.drawable.monster_3))
        animation!!.addFrame(Bitmap(resources, R.drawable.monster_4))
        currentFrame = animation!!.getFrameReference(0)
        this.kill = kill
        direction = 3
    }
}