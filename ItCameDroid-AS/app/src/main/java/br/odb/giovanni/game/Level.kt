package br.odb.giovanni.game

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import br.odb.giovanni.engine.TileArea
import br.odb.giovanni.engine.Vec2
import br.odb.giovanni.menus.ItCameView

class Level(
    i: Int,
    j: Int,
    resources: Resources?,
    tilePaletteIndexes: IntArray?,
    wallTiles: IntArray?
) : TileArea(
    i, j, resources,
    tilePaletteIndexes!!, wallTiles!!
) {
    var dynamite: Dynamite? = null
    var gameShouldEnd = false
    var dead = 0
    var miner: Miner? = null
    lateinit var motherMonster: Array<MonsterMother?>
    private val recycler = ArrayList<Actor>()
    override fun draw(canvas: Canvas?, paint: Paint?) {
        super.move(
            camera!!.x - ItCameView.viewport.right / 2.0f, camera!!.y
                    - ItCameView.viewport.bottom / 2.0f
        )
        super.draw(canvas, paint)
        super.move(
            -camera!!.x + ItCameView.viewport.right / 2.0f, -camera!!.y
                    + ItCameView.viewport.bottom / 2.0f
        )
    }

    fun setCurrentCamera(Camera: Vec2?) {
        camera = Camera
    }

    fun mayMoveTo(x: Float, y: Float): Boolean {
        return mayMoveTo(x.toInt(), y.toInt())
    }

    fun mayMoveTo(x: Int, y: Int): Boolean {
        return if (x < 0 || y < 0 || x >= Constants.SIZE_X || y >= Constants.SIZE_Y) false else !super.getTileAt(
            x,
            y
        )!!.blocker
    }

    override fun tick(timeInMS: Long) {
        super.tick(timeInMS)
        recycler.clear()
        var alive = 0
        for (a in actors) {
            if (a is Monster) {
                if (a.killed) {
                    ++dead
                    recycler.add(a)
                } else {
                    ++alive
                }
            }
        }
        for (mm in motherMonster) {
            val generated = mm!!.generate(timeInMS / 10)
            if (generated != null && alive < LIMIT_MONSTERS_ALIVE) {
                val x = mm.position.x / br.odb.giovanni.engine.Constants.BASE_TILE_WIDTH
                val y = mm.position.y / br.odb.giovanni.engine.Constants.BASE_TILE_HEIGHT
                addActor(x, y, generated)
                generated.actorTarget = miner
            }
        }
        if (miner!!.killed || outsideMap(miner!!) && dynamite!!.killed) {
            gameShouldEnd = true
        }
        for (a in recycler) {
            actors.remove(a)
            a.prepareForGC()
        }
    }

    override fun addActor(i: Float, j: Float, actor: Actor) {
        super.addActor(i, j, actor)
        actor.level = this
    }

    companion object {
        private const val LIMIT_MONSTERS_ALIVE = 16
        var camera: Vec2? = null
    }
}