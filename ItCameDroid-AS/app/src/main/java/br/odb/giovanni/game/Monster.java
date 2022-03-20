package br.odb.giovanni.game;

import android.content.res.Resources;
import android.media.MediaPlayer;

import br.odb.giovanni.R;
import br.odb.giovanni.engine.Animation;
import br.odb.giovanni.engine.Bitmap;

public class Monster extends Actor {
    Actor target;
    private long timeToMove;
    private final MediaPlayer kill;

    public Monster(Resources resources, MediaPlayer walk1, MediaPlayer walk2, MediaPlayer kill) {
        super();

        animation = new Animation();
        animation.addFrame(new Bitmap(resources, R.drawable.monster_3));
        animation.addFrame(new Bitmap(resources, R.drawable.monster_4));

        currentFrame = animation.getFrameReference(0);
        this.kill = kill;

        this.setDirection(3);
    }


    @Override
    public void tick(long timeInMS) {
        super.tick(timeInMS);

        timeToMove -= timeInMS;

        if (timeToMove < 0) {
            timeToMove = 300;
        } else {
            return;
        }

        float currentX = (getPosition().x / br.odb.giovanni.engine.Constants.BASETILEWIDTH);
        float currentY = (getPosition().y / br.odb.giovanni.engine.Constants.BASETILEHEIGHT);
        float targetX = (target.getPosition().x / br.odb.giovanni.engine.Constants.BASETILEWIDTH);
        float targetY = (target.getPosition().y / br.odb.giovanni.engine.Constants.BASETILEHEIGHT);
        float dirX;
        float dirY;

        if (targetX < currentX) {
            dirX = -0.5f;
        } else if (targetX > currentX) {
            dirX = 0.5f;
        } else {
            dirX = 0;
        }

        if (targetY < currentY) {
            dirY = -0.5f;
        } else if (targetY > currentY) {
            dirY = 0.5f;
        } else {
            dirY = 0;
        }

        if (level.mayMoveTo(currentX + dirX, currentY + dirY)) {

            this.move(br.odb.giovanni.engine.Constants.BASETILEWIDTH * (dirX),
                    br.odb.giovanni.engine.Constants.BASETILEHEIGHT * (dirY));

            //try to move around obstacle
        } else if (level.mayMoveTo(currentX, currentY + 0.5f)) {
            this.move(0, br.odb.giovanni.engine.Constants.BASETILEHEIGHT * (0.5f));
        } else if (level.mayMoveTo(currentX + 0.5f, currentY)) {
            this.move(br.odb.giovanni.engine.Constants.BASETILEWIDTH * (0.5f), 0.0f);
        } else if (level.mayMoveTo(currentX - 0.5f, currentY)) {
            this.move(br.odb.giovanni.engine.Constants.BASETILEWIDTH * (-0.5f), 0.0f);
        } else if (level.mayMoveTo(currentX, currentY - 0.5f)) {
            this.move(0, br.odb.giovanni.engine.Constants.BASETILEHEIGHT * (0.5f));
        }
    }

    @Override
    public void touched(Actor actor) {
        if (actor instanceof Monster) {

            if (kill != null) {
                kill.start();
            }
            kill();
        }
    }

    @Override
    public void didMove() {
    }
}
