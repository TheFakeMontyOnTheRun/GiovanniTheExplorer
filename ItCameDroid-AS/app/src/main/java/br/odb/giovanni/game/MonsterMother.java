package br.odb.giovanni.game;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import br.odb.giovanni.R;
import br.odb.giovanni.engine.Animation;
import br.odb.giovanni.engine.Bitmap;
import br.odb.giovanni.menus.ItCameView;


public class MonsterMother extends Actor {

    private long timeEllapsed;
    private final Resources resources;
    private MediaPlayer spawnSound = null;
    private MediaPlayer kill = null;
    private MediaPlayer walk1 = null;

    public MonsterMother(Resources resources, Context context) {
        super();

        this.resources = resources;
        animation = new Animation();
        animation.addFrame(new Bitmap(resources, R.drawable.vulcan_01));
        animation.addFrame(new Bitmap(resources, R.drawable.vulcan_02));
        animation.addFrame(new Bitmap(resources, R.drawable.vulcan_03));
        currentFrame = animation.getFrameReference(0);

        if (ItCameView.playSounds) {

            spawnSound = MediaPlayer.create(context, R.raw.spawn);
            kill = MediaPlayer.create(context, R.raw.monsterkill);
            walk1 = MediaPlayer.create(context, R.raw.walk1);
        }
    }

    public Monster generate(long timeEllapsed) {

        this.timeEllapsed += timeEllapsed;

        Monster generated = null;

        if (this.timeEllapsed > 400) {

            this.timeEllapsed -= 400;

            generated = new Monster(resources, walk1, null, kill);

            if (spawnSound != null && !spawnSound.isPlaying()) {

                spawnSound.start();
            }
            generated.setPosition(getPosition());
        }
        return generated;
    }

    @Override
    public void touched(Actor actor) {
    }

    @Override
    public void didMove() {
    }
}
