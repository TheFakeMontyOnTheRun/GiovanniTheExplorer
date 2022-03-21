package br.odb.giovanni.game

import kotlin.math.abs

class Vec2 {
    @JvmField
	var x: Float
    @JvmField
	var y: Float

    constructor(aX: Float, aY: Float) {
        x = aX
        y = aY
    }

    constructor() {
        x = 0f
        y = 0f
    }

    override fun equals(o: Any?): Boolean {
        return if (o is Vec2) {
            o.x == x && o.y == y
        } else {
            false
        }
    }

    fun isCloseEnoughToConsiderEqualTo(position: Vec2): Boolean {
        return abs(x - position.x) < 1.0f && abs(y - position.y) < 1.0f
    }
}