package br.odb.giovanni.engine

class Animation : Runnable {
    @JvmField
    var play: Boolean
    private var frames: ArrayList<Bitmap>?
    private var currentFrame: Int
    private var loop: Boolean
    override fun run() {
        while (play) {
            tick()
        }
    }

    fun setCurrentFrame(currentFrame: Int) {
        this.currentFrame = currentFrame
    }

    fun setLoop(loop: Boolean) {
        this.loop = loop
    }

    fun addFrame(bitmap: Bitmap) {
        frames!!.add(bitmap)
    }

    val currentFrameReference: Bitmap
        get() = getFrameReference(currentFrame)

    fun getFrameReference(i: Int): Bitmap {
        return frames!![i]
    }

    fun tick() {
        if (play) {
            currentFrame++
        }
        if (currentFrame == frames!!.size) {
            currentFrame = 0
            if (!loop) {
                play = false
            }
        }
    }

    fun prepareForGC() {
        play = false
        Thread.yield()
        for (f in frames!!) {
            f.prepareForGC()
        }
        frames!!.clear()
        frames = null
    }

    init {
        frames = ArrayList()
        currentFrame = 0
        loop = true
        play = true
    }
}