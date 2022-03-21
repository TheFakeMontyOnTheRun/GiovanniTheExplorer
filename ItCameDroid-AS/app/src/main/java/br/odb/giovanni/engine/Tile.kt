package br.odb.giovanni.engine

import android.graphics.Bitmap

class Tile(var bitmap: Bitmap, var blocker: Boolean = false, val position: Vec2 = Vec2())