package br.odb.giovanni.game

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import br.odb.giovanni.R
import br.odb.giovanni.engine.Constants

class Monster(resources: Resources, private val kill: MediaPlayer) : Actor() {

    var actorTarget: Actor? = null
    private var timeToMove: Long = 0

    init {
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.monster_3))
        animation.addFrame(BitmapFactory.decodeResource(resources, R.drawable.monster_4))
        direction = 3
    }

    override fun tick(timeInMS: Long) {
        super.tick(timeInMS)
        timeToMove -= timeInMS
        timeToMove = if (timeToMove < 0) {
            300
        } else {
            return
        }
        val currentX = position.x / Constants.BASE_TILE_WIDTH
        val currentY = position.y / Constants.BASE_TILE_HEIGHT
        val targetX = actorTarget!!.position.x / Constants.BASE_TILE_WIDTH
        val targetY = actorTarget!!.position.y / Constants.BASE_TILE_HEIGHT
        val dirX: Float = when {
            targetX < currentX -> {
                -0.5f
            }
            targetX > currentX -> {
                0.5f
            }
            else -> {
                0f
            }
        }
        val dirY: Float = when {
            targetY < currentY -> {
                -0.5f
            }
            targetY > currentY -> {
                0.5f
            }
            else -> {
                0f
            }
        }
        when {
            level!!.mayMoveTo(currentX + dirX, currentY + dirY) -> {
                move(
                    Constants.BASE_TILE_WIDTH * dirX,
                    Constants.BASE_TILE_HEIGHT * dirY
                )

                //try to move around obstacle
            }
            level!!.mayMoveTo(currentX, currentY + 0.5f) -> {
                move(0f, Constants.BASE_TILE_HEIGHT * 0.5f)
            }
            level!!.mayMoveTo(currentX + 0.5f, currentY) -> {
                move(Constants.BASE_TILE_WIDTH * 0.5f, 0.0f)
            }
            level!!.mayMoveTo(currentX - 0.5f, currentY) -> {
                move(Constants.BASE_TILE_WIDTH * -0.5f, 0.0f)
            }
            level!!.mayMoveTo(currentX, currentY - 0.5f) -> {
                move(0f, Constants.BASE_TILE_HEIGHT * 0.5f)
            }
        }
    }

    override fun touched(actor: Actor?) {
        if (actor is Monster) {
            kill.start()
            kill()
        }
    }

    public override fun didMove() {}
}