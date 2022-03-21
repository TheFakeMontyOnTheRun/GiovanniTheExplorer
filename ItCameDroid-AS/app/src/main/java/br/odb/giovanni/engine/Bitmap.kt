package br.odb.giovanni.engine

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

open class Bitmap {
    var androidBitmap: Bitmap?
    var x = 0f
    var y = 0f

    constructor(resources: Resources?, baseTypeId: Int) {
        androidBitmap = BitmapFactory.decodeResource(resources, baseTypeId)
    }

    internal constructor(androidBitmap: Bitmap?) {
        this.androidBitmap = androidBitmap
    }

    fun draw(canvas: Canvas, paint: Paint?) {
        canvas.drawBitmap(androidBitmap!!, x, y, paint)
    }

    fun prepareForGC() {
        androidBitmap = null
    }
}