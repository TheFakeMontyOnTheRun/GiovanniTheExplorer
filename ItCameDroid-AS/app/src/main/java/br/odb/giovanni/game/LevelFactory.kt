package br.odb.giovanni.game

import android.content.Context
import android.content.res.Resources
import br.odb.giovanni.R
import kotlin.math.abs

object LevelFactory {
    @JvmStatic
    fun createRandomLevel(i: Int, j: Int, resources: Resources?, context: Context?): Level {
        val tilePaletteIndexes = intArrayOf(R.drawable.cave_floor2, R.drawable.cave_floor)
        val wallPaletteIndexes = intArrayOf(R.drawable.cave2, R.drawable.cave)
        val level = Level(i, j, resources, tilePaletteIndexes, wallPaletteIndexes)
        for (x in 0 until i) {
            level.setTileType(x, 0, Constants.WALLRESID)
            level.setTileType(x, j - 1, Constants.WALLRESID)
        }
        for (y in 0 until j) {
            level.setTileType(0, y, Constants.WALLRESID)
            level.setTileType(i - 1, y, Constants.WALLRESID)
        }
        level.miner = Miner(resources, context)
        level.motherMonster = arrayOfNulls(3)
        level.motherMonster[0] = MonsterMother(resources!!, context)
        level.motherMonster[1] = MonsterMother(resources, context)
        level.motherMonster[2] = MonsterMother(resources, context)
        level.dynamite = Dynamite(resources, context)
        var x: Int
        var y: Int
        for (c in 0..9) {
            x = abs(Math.random() * i).toInt()
            y = abs(Math.random() * j).toInt()
            positPillar(x, y, level, c)
        }
        for (c in 0..9) {
            x = abs(Math.random() * (i - 2)).toInt() + 1
            y = abs(Math.random() * (j - 2)).toInt() + 1
            level.setTileType(x, y, Constants.WALLRESID)
        }
        for (c in 0..6) {
            x = abs(Math.random() * (i - 2)).toInt() + 1
            y = abs(Math.random() * (j - 2)).toInt() + 1
            level.setTileType(x, y, Constants.BASETERRAINRESID)
        }
        x = i / 2
        y = j - 3 - 2
        placeMonsterNest(5, 5, level, level.motherMonster[0]!!)
        placeMonsterNest(i - 5, j - 5, level, level.motherMonster[1]!!)
        placeMonsterNest(x, j / 2, level, level.motherMonster[2]!!)
        placeExit(x, y, level)
        return level
    }

    private fun placeExit(i: Int, j: Int, level: Level) {
        for (a in i - 4 until i + 4) {
            for (b in j - 4 until j + 4) {
                level.setTileType(a, b, Constants.BASETERRAINRESID)
            }
        }
        level.setTileType(i - 1, j - 2, Constants.WALLRESID)
        level.setTileType(i, j - 2, Constants.WALLRESID)
        level.setTileType(i + 1, j - 2, Constants.WALLRESID)
        level.setTileType(i + 2, j - 2, Constants.WALLRESID)
        level.addActor(i.toFloat(), (j + 1).toFloat(), level.miner!!)
        level.setTileType(i, j + 4, Constants.BASETERRAINRESID)
        level.addActor(i.toFloat(), (j + 4).toFloat(), level.dynamite!!)
    }

    private fun placeMonsterNest(i: Int, j: Int, level: Level, mm: MonsterMother) {
        for (a in i - 4 until i + 4) {
            for (b in j - 4 until j + 4) {
                level.setTileType(a, b, Constants.BASETERRAINRESID)
            }
        }
        for (y in 1 until level.height - 1) {
            level.setTileType(i, y, Constants.BASETERRAINRESID)
        }
        level.addActor(i.toFloat(), j.toFloat(), mm)
    }

    private fun positPillar(x: Int, y: Int, level: Level, radius: Int) {
        for (i in -radius / 2 until radius / 2) {
            for (j in -radius / 2 until radius / 2) {
                level.setTileType(x + i, y + j, Constants.WALLRESID)
            }
        }
    }
}