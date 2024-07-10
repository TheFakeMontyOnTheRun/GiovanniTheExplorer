package br.odb.giovanni.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import br.odb.giovanni.engine.Animation
import br.odb.giovanni.engine.Constants
import br.odb.giovanni.engine.Vec2
import br.odb.giovanni.menus.ItCameView

abstract class Actor {

    enum class ActorStates {
        STILL, MOVING
    }

    var level: Level? = null
    var killed = false
    private var visible = true
    var animation: Animation = Animation()
    var currentFrame: Bitmap? = null
    var position: Vec2 = Vec2()
    var direction = 0
    var bounds: Rect = Rect()
        private set

    open var state: ActorStates = ActorStates.STILL

    open fun tick(timeInMS: Long) {
        animation.tick()
        currentFrame = animation.currentFrameReference
    }

    private val screenPosition: Vec2
        get() {
            val toReturn = Vec2()
            toReturn.x =
                -level!!.camera.x + ItCameView.viewport.right / 2.0f + position.x - currentFrame!!.width / 2.0f
            toReturn.y =
                -level!!.camera.y + ItCameView.viewport.bottom / 2.0f + position.y - currentFrame!!.height + Constants.BASE_TILE_HEIGHT / 2.0f
            return toReturn
        }

    fun draw(
        canvas: Canvas?,
        paint: Paint?
    ) {
        if (!visible) {
            return
        }
        try {
            if (currentFrame != null && canvas != null && paint != null) {
                canvas.drawBitmap(currentFrame!!, screenPosition.x, screenPosition.y, paint)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun move(x: Float, y: Float) {
        position.x += x
        position.y += y
        didMove()
    }

    fun kill() {
        killed = true
        visible = false
    }

    fun prepareForGC() {
        currentFrame = null
        animation.prepareForGC()
        level = null
    }

    abstract fun touched(actor: Actor?)

    protected abstract fun didMove()
}