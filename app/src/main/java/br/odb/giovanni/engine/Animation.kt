package br.odb.giovanni.engine

import android.graphics.Bitmap

class Animation {
    var playing: Boolean
    var loop = true

    private val frames = ArrayList<Bitmap>()
    private var currentFrame: Int

    fun setCurrentFrame(currentFrame: Int) {
        this.currentFrame = currentFrame
    }

    fun addFrame(bitmap: Bitmap) {
        frames.add(bitmap)
    }

    val currentFrameReference: Bitmap
        get() = getFrameReference(currentFrame)

    fun getFrameReference(i: Int): Bitmap {
        return frames[i]
    }

    fun tick() {
        if (playing) {
            currentFrame++
        }
        if (currentFrame == frames.size) {
            currentFrame = 0
            if (!loop) {
                playing = false
            }
        }
    }

    fun prepareForGC() {
        playing = false
        frames.clear()
    }

    init {
        currentFrame = 0
        loop = true
        playing = true
    }
}