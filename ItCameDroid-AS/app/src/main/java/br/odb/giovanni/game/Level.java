package br.odb.giovanni.game;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import br.odb.giovanni.engine.TileArea;
import br.odb.giovanni.menus.ItCameView;

public class Level extends TileArea {
    private static final int LIMIT_MONSTERS_ALIVE = 16;
    public static Vec2 camera;
    public Dynamite dynamite;
    public boolean gameShouldEnd;
    public int dead;
    Miner miner;
    MonsterMother[] motherMonster;
    private final ArrayList<Actor> recycler = new ArrayList<>();

    public Level(int i, int j, Resources resources, int[] tilePaletteIndexes, int chancesTile, int[] wallTiles, int chancesWall) {
        super(i, j, br.odb.giovanni.game.Constants.BASETERRAINRESID, resources,
                tilePaletteIndexes, chancesTile, wallTiles, chancesWall);
    }

    public void draw(Canvas canvas, Paint paint) {

        super.move(camera.x - (ItCameView.viewport.right / 2.0f), camera.y
                - (ItCameView.viewport.bottom / 2.0f));

        super.draw(canvas, paint);

        super.move(-camera.x + (ItCameView.viewport.right / 2.0f), -camera.y
                + (ItCameView.viewport.bottom / 2.0f));

    }

    public void setCurrentCamera(Vec2 Camera) {
        camera = Camera;
    }

    public boolean mayMoveTo(float x, float y) {
        return mayMoveTo((int) x, (int) y);
    }

    public boolean mayMoveTo(int x, int y) {

        if (x < 0 || y < 0 || x >= br.odb.giovanni.game.Constants.SIZEX
                || y >= br.odb.giovanni.game.Constants.SIZEY)
            return false;

        return super.getTileAt(x, y).getType() != br.odb.giovanni.game.Constants.WALLRESID;
    }

    @Override
    public void tick(long timeInMS) {

        super.tick(timeInMS);

        recycler.clear();

        int alive = 0;

        for (Actor a : getActors()) {
            if (a instanceof Monster) {
                if (a.killed) {
                    ++dead;
                    recycler.add(a);
                } else {
                    ++alive;
                }
            }
        }

        for (MonsterMother mm : motherMonster) {

            Monster generated = mm.generate(timeInMS / 10);

            if (generated != null && alive < LIMIT_MONSTERS_ALIVE) {
                float x = (mm.getPosition().x / br.odb.giovanni.engine.Constants.BASETILEWIDTH);
                float y = (mm.getPosition().y / br.odb.giovanni.engine.Constants.BASETILEHEIGHT);
                addActor(x, y, generated);
                generated.target = miner;
            }
        }

        if (miner.killed || (outsideMap(miner) && dynamite.killed)) {
            gameShouldEnd = true;
        }

        for (Actor a : recycler) {
            actors.remove(a);
            a.prepareForGC();
        }
    }


    @Override
    public void addActor(float i, float j, Actor actor) {
        super.addActor(i, j, actor);
        actor.level = this;
    }

    public Miner getMiner() {

        return miner;
    }
}
