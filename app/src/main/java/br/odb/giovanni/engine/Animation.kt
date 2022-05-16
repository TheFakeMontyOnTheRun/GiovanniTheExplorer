package br.odb.giovanni.engine

import android.graphics.Bitmap

class Animation {
	var playing = true
	var loop = true
	var currentFrame: Int = 0

	val currentFrameReference: Bitmap
		get() = getFrameReference(currentFrame)

	private val frames = ArrayList<Bitmap>()

	fun addFrame(bitmap: Bitmap) {
		frames.add(bitmap)
	}

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
}