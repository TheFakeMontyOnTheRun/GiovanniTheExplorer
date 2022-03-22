package br.odb.giovanni.engine

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import br.odb.giovanni.menus.VirtualPadClient

class VirtualPad(private val listener: VirtualPadClient) : Drawable() {

    val keyMap: BooleanArray = BooleanArray(5)
    private val vKeys: Array<Rect> = arrayOf(Rect(), Rect(), Rect(), Rect())
    private val lastTouch1 = Rect()
    private val paint: Paint = Paint()

    override fun setBounds(bounds: Rect) {
        setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        val width = right - left
        val height = bottom - left
        vKeys[0][(width * 15L / 100L).toInt(), (height * 30L / 100L).toInt(), (width * 20L / 100L).toInt()] =
            (height * 40L / 100L).toInt()
        vKeys[1][(width * 25L / 100L).toInt(), (height * 40L / 100L).toInt(), (width * 30L / 100L).toInt()] =
            (height * 50L / 100L).toInt()
        vKeys[2][(width * 15L / 100L).toInt(), (height * 50L / 100L).toInt(), (width * 20L / 100L).toInt()] =
            (height * 60L / 100L).toInt()
        vKeys[3][(width * 5L / 100L).toInt(), (height * 40L / 100L).toInt(), (width * 10L / 100L).toInt()] =
            (height * 50L / 100L).toInt()
    }

    override fun draw(canvas: Canvas) {
        for (c in vKeys.indices) {
            if (keyMap[c]) paint.setARGB(64, 0, 0, 255) else paint.setARGB(64, 255, 0, 0)
            canvas.drawCircle(
                vKeys[c].exactCenterX(),
                vKeys[c].exactCenterY(),
                vKeys[c].width().toFloat(),
                paint
            )
        }

        synchronized(listener) {
            val overlay = listener.bitmapOverlay
            if (overlay != null) {
                paint.setARGB(128, 0, 0, 0)
                val rect = Rect()
                rect.top = vKeys[0].exactCenterY().toInt()
                rect.bottom = vKeys[2].exactCenterY().toInt()
                rect.left = vKeys[3].exactCenterX().toInt()
                rect.right = vKeys[1].exactCenterX().toInt()
                canvas.drawBitmap(overlay, null, rect, paint)
            }
        }
        paint.setARGB(255, 0, 0, 0)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setAlpha(arg0: Int) {}

    override fun setColorFilter(arg0: ColorFilter?) {}

    fun onTouchEvent(event: MotionEvent): Boolean {
        lastTouch1[event.x.toInt() - 25, event.y.toInt() - 25, (event.x + 25).toInt()] =
            (event.y + 25).toInt()
        return updateTouch(event.action != MotionEvent.ACTION_UP)
    }

    private fun updateTouch(down: Boolean): Boolean {
        var returnValue = false
        for (c in vKeys.indices) {
            if (Rect.intersects(vKeys[c], lastTouch1)) {
                keyMap[c] = down
                returnValue = keyMap[c]
            } else {
                keyMap[c] = false
            }
        }
        return returnValue
    }
}