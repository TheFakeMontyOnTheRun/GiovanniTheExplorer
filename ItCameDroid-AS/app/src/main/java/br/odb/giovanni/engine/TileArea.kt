package br.odb.giovanni.engine

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import br.odb.giovanni.game.Actor
import br.odb.giovanni.game.Vec2
import java.util.*
import kotlin.collections.ArrayList

open class TileArea protected constructor(
    i: Int,
    j: Int,
    resources: Resources?,
    tilePaletteIndex: IntArray,
    wallTiles: IntArray
) {
    private val map: Array<Array<Tile?>>
    open var actors = ArrayList<Actor>()

    private val tilePalette: Array<Tile?>
    private val wallPalette: Array<Tile?>
    val width: Int
    val height: Int
    fun getTileAt(x: Int, y: Int): Tile? {
        return map[x][y]
    }

    open fun draw(canvas: Canvas?, paint: Paint?) {
        val actorMap = makeSnapshot()
        for (x in 0 until width) {
            for (y in 0 until height) {
                map[x][y]!!.draw(canvas!!, paint)
                if (actorMap[x]!![y] != null) {
                    actorMap[x]!![y]!!.draw(canvas, paint)
                }
            }
        }
    }

    private fun makeSnapshot(): Array<Array<Actor?>?> {
        val toReturn: Array<Array<Actor?>?> = arrayOfNulls(
            width
        )
        var actor: Actor
        for (x in toReturn.indices) {
            toReturn[x] = arrayOfNulls(height)
            Arrays.fill(toReturn[x], null)
        }
        for (c in actors!!.indices) {
            actor = actors!![c]
            if (actor.killed) {
                continue
            }
            val currentX = (actor.position!!.x / Constants.BASETILEWIDTH).toInt()
            val currentY = (actor.position!!.y / Constants.BASETILEHEIGHT).toInt()
            toReturn[currentX]!![currentY] = actor
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
        tile!!.x = (tile.x
                + (tile.androidBitmap!!.width - Constants.BASETILEWIDTH))
        tile.y = (tile.y
                + (tile.androidBitmap!!.height - Constants.BASETILEHEIGHT))
        if (type != 0) {
            tile.setTile(
                type,
                wallPalette[if (Math.round(Math.random() * 6) != 0L) 0 else 1]!!.androidBitmap
            )
        } else {
            tile.setTile(type, tilePalette[if (Math.random() * 3 == 0.0) 0 else 1]!!.androidBitmap)
        }
        tile.x = (tile.x
                - (tile.androidBitmap!!.width - Constants.BASETILEWIDTH))
        tile.y = (tile.y
                - (tile.androidBitmap!!.height - Constants.BASETILEHEIGHT))
    }

    fun move(x: Float, y: Float) {
        move(x.toInt(), y.toInt())
    }

    private fun move(x: Int, y: Int) {
        for (i in 0 until width) for (j in 0 until height) {
            val tile = map[i][j]
            tile!!.x = tile.x - x
            tile.y = tile.y - y
        }
    }

    open fun addActor(i: Float, j: Float, actor: Actor) {
        if (i < 0 || i >= map.size) {
            return
        }
        if (j < 0 || j >= map[i.toInt()].size) {
            return
        }
        actors!!.add(actor)
        val adjustY = -(Constants.BASETILEHEIGHT - actor.bounds!!.height()) / 2
        val adjustX = (Constants.BASETILEWIDTH - actor.bounds!!.width()) / 2
        val vec2 = Vec2(
            i * Constants.BASETILEWIDTH + adjustX, j
                    * Constants.BASETILEHEIGHT + adjustY
        )
        actor.position = vec2
    }

    open fun tick(timeInMS: Long) {
        for (a in actors!!) {
            if (a.killed) {
                continue
            }
            assert(a.position != null)
            a.tick(timeInMS)
            for (b in actors!!) {
                if (b.killed) {
                    continue
                }
                if (a === b) {
                    continue
                }
                assert(b.position != null)
                if (a.position!!.isCloseEnoughToConsiderEqualTo(
                        b.position!!
                    )
                ) {
                    a.touched(b)
                    b.touched(a)
                }
            }
        }
    }

    protected fun outsideMap(actor: Actor): Boolean {
        val pos = actor.position
        val x = (pos!!.y / Constants.BASETILEHEIGHT).toInt()
        val y = (pos!!.x / Constants.BASETILEWIDTH).toInt()
        return x <= 0 || y <= 0 || y >= map.size - 1 || x >= map[x].size - 1
    }

    init {
        var lastx = 0
        var lasty = 0
        var tile: Tile
        map = Array(i) { arrayOfNulls(j) }
        width = i
        height = j
        var biggestY = 0
        tilePalette = arrayOfNulls(2)
        wallPalette = arrayOfNulls(2)
        for (c in tilePaletteIndex.indices) tilePalette[c] = Tile(resources, tilePaletteIndex[c])
        for (c in wallTiles.indices) wallPalette[c] = Tile(resources, wallTiles[c])
        for (y in 0 until j) {
            for (x in 0 until i) {
                tile = Tile(
                    br.odb.giovanni.game.Constants.BASETERRAINRESID,
                    tilePalette[if (Math.round(Math.random() * 45) == 0L) 0 else 1]
                        ?.androidBitmap
                )
                map[x][y] = tile
                tile.x = lastx.toFloat()
                tile.y = (lasty
                        - (tile.androidBitmap!!.height - Constants.BASETILEHEIGHT)).toFloat()
                tile.y = lasty.toFloat()
                lastx += tile.androidBitmap!!.width
                if (tile.androidBitmap!!.height > biggestY) {
                    biggestY = tile.androidBitmap!!.height
                }
            }
            lastx = 0
            lasty += biggestY
        }
    }
}