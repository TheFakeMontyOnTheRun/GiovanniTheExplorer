package br.odb.giovanni.game

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import br.odb.giovanni.engine.Animation
import br.odb.giovanni.engine.Bitmap
import br.odb.giovanni.engine.Constants
import br.odb.giovanni.menus.ItCameView

abstract class Actor internal constructor() {
    @JvmField
    var level: Level? = null
    @JvmField
    var killed = false
    private var visible = true
    @JvmField
    var animation: Animation? = null
    @JvmField
    var currentFrame: Bitmap? = null
    var position: Vec2? = null
    var direction = 0
    var bounds: Rect?
        private set
    open var state: ActorStates? = null
    open fun tick(timeInMS: Long) {
        animation!!.tick()
        currentFrame = animation!!.currentFrameReference
    }

    private val screenPosition: Vec2
        private get() {
            val toReturn = Vec2()
            toReturn.x =
                -Level.camera!!.x + ItCameView.viewport.right / 2.0f + position!!.x - currentFrame!!.androidBitmap!!.width / 2.0f
            toReturn.y =
                -Level.camera!!.y + ItCameView.viewport.bottom / 2.0f + position!!.y - currentFrame!!.androidBitmap!!.height + Constants.BASETILEHEIGHT / 2.0f
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
            val screenPos = screenPosition
            currentFrame!!.x = screenPos.x
            currentFrame!!.y = screenPos.y
            currentFrame!!.draw(canvas!!, paint)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun move(x: Float, y: Float) {
        position!!.x += x
        position!!.y += y
        didMove()
    }

    fun kill() {
        killed = true
        visible = false
    }

    abstract fun touched(actor: Actor?)
    protected abstract fun didMove()
    fun prepareForGC() {
        position = null
        bounds = null
        state = null
        currentFrame!!.prepareForGC()
        currentFrame = null
        animation!!.prepareForGC()
        animation = null
        level = null
    }

    enum class ActorStates {
        STILL, MOVING
    }

    init {
        bounds = Rect()
        direction = 0
        position = Vec2(0.0f, 0.0f)
        state = ActorStates.STILL
    }
}