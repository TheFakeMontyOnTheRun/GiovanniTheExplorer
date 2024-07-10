package br.odb.giovanni.engine

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import br.odb.giovanni.game.Actor
import kotlin.math.roundToLong

open class TileArea(
    val width: Int,
    val height: Int,
    resources: Resources,
    tilePaletteIndex: IntArray,
    wallTiles: IntArray
) {

    private val map: Array<Array<Tile?>> = Array(height) { arrayOfNulls(width) }
    open var actors = ArrayList<Actor>()
    private val tilePalette: Array<Tile?> = arrayOfNulls(2)
    private val wallPalette: Array<Tile?> = arrayOfNulls(2)

    init {
        var lastX = 0
        var lastY = 0
        var biggestY = 0

        for (c in tilePaletteIndex.indices) tilePalette[c] =
            Tile(BitmapFactory.decodeResource(resources, tilePaletteIndex[c]))

        for (c in wallTiles.indices) wallPalette[c] =
            Tile(BitmapFactory.decodeResource(resources, wallTiles[c]))

        for (y in 0 until height) {
            for (x in 0 until width) {

                val tile = Tile(
                    tilePalette[if ((Math.random() * 45).roundToLong() == 0L) 0 else 1]!!.bitmap,
                    false
                )

                map[x][y] = tile

                tile.position.x = lastX.toFloat()
                tile.position.y = lastY.toFloat()

                lastX += tile.bitmap.width

                if (tile.bitmap.height > biggestY) {
                    biggestY = tile.bitmap.height
                }
            }

            lastX = 0
            lastY += biggestY
        }
    }

    fun getTileAt(x: Int, y: Int): Tile? {
        return map[x][y]
    }

    open fun draw(canvas: Canvas?, paint: Paint?) {

        if (canvas == null) {
            return
        }

        val actorMap = makeSnapshot()

        for (y in 0 until height) {
            for (x in 0 until width) {

                val tile = map[y][x]

                if (tile != null) {
                    canvas.drawBitmap(tile.bitmap, tile.position.x, tile.position.y, paint)
                }

                if (actorMap[y][x] != null) {
                    actorMap[y][x]!!.draw(canvas, paint)
                }
            }
        }
    }

    private fun makeSnapshot(): Array<Array<Actor?>> {

        val toReturn: Array<Array<Actor?>> = Array(height) { arrayOfNulls(width) }

        for (c in actors.indices) {
            val actor = actors[c]
            if (actor.killed) {
                continue
            }
            val currentX = (actor.position.x / Constants.BASE_TILE_WIDTH).toInt()
            val currentY = (actor.position.y / Constants.BASE_TILE_HEIGHT).toInt()

            toReturn[currentX][currentY] = actor
        }
        return toReturn
    }

    fun setTileType(i: Int, j: Int, type: Int) {

        if (i < 0 || i >= map.size) {
            return
        }
        if (j < 0 || j >= map[i].size) {
            return
        }

        val tile = map[i][j]

        if (tile != null) {
            tile.position.x += (tile.bitmap.width - Constants.BASE_TILE_WIDTH)
            tile.position.y += (tile.bitmap.height - Constants.BASE_TILE_HEIGHT)

            if (type != 0) {
                tile.bitmap =
                    wallPalette[if ((Math.random() * 6).roundToLong() != 0L) 0 else 1]!!.bitmap
            } else {
                tile.bitmap = tilePalette[if (Math.random() * 3 == 0.0) 0 else 1]!!.bitmap
            }

            tile.blocker = (type == br.odb.giovanni.game.Constants.BLOCKING_TILE)

            tile.position.x -= (tile.bitmap.width - Constants.BASE_TILE_WIDTH)
            tile.position.y -= (tile.bitmap.height - Constants.BASE_TILE_HEIGHT)
        }
    }

    fun move(x: Float, y: Float) {
        move(x.toInt(), y.toInt())
    }

    private fun move(x: Int, y: Int) {
        for (i in 0 until width) for (j in 0 until height) {
            val tile = map[i][j]
            if (tile != null) {
                tile.position.x -= x
                tile.position.y -= y
            }
        }
    }

    open fun addActor(i: Float, j: Float, actor: Actor) {

        if (i < 0 || i >= map.size) {
            return
        }

        if (j < 0 || j >= map[i.toInt()].size) {
            return
        }

        actors.add(actor)

        val adjustY = -(Constants.BASE_TILE_HEIGHT - actor.bounds.height()) / 2
        val adjustX = (Constants.BASE_TILE_WIDTH - actor.bounds.width()) / 2

        val vec2 = Vec2(
            i * Constants.BASE_TILE_WIDTH + adjustX, j * Constants.BASE_TILE_HEIGHT + adjustY
        )

        actor.position = vec2
    }

    open fun tick(timeInMS: Long) {

        for (a in actors) {

            if (a.killed) {
                continue
            }

            a.tick(timeInMS)

            for (b in actors) {

                if (b.killed) {
                    continue
                }

                if (a === b) {
                    continue
                }

                if (a.position.isCloseEnoughToConsiderEqualTo(b.position)) {
                    a.touched(b)
                    b.touched(a)
                }
            }
        }
    }

    protected fun outsideMap(actor: Actor): Boolean {
        val pos = actor.position
        val x = (pos.y / Constants.BASE_TILE_HEIGHT).toInt()
        val y = (pos.x / Constants.BASE_TILE_WIDTH).toInt()

        return x <= 0 || y <= 0 || y >= map.size - 1 || x >= map[x].size - 1
    }
}