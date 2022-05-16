package br.odb.giovanni.engine

import kotlin.math.abs

class Vec2(var x: Float = 0.0f, var y: Float = 0.0f) {

	fun isCloseEnoughToConsiderEqualTo(position: Vec2): Boolean {
		return abs(x - position.x) < 1.0f && abs(y - position.y) < 1.0f
	}
}