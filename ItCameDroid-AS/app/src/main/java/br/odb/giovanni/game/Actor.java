package br.odb.giovanni.game;

import android.graphics.Rect;

import br.odb.giovanni.engine.Animation;
import br.odb.giovanni.engine.Bitmap;
import br.odb.giovanni.menus.ItCameView;

public abstract class Actor {

    public Level level;
    public boolean killed;
    private boolean visible = true;
    Animation animation;
    Bitmap currentFrame;
    private Vec2 position;
    private int direction;
    private Rect bounds;
    private states state;

    Actor() {
        bounds = new Rect();
        setDirection(0);
        setPosition(new Vec2(0, 0));
        setState(states.STILL);
    }

    public void tick(long timeInMS) {
        animation.tick(timeInMS);
        currentFrame = animation.getCurrentFrameReference();
    }

    private Vec2 getScreenPosition() {
        Vec2 toReturn = new Vec2();

        toReturn.x = (-Level.camera.x + (ItCameView.viewport.right / 2.0f) + getPosition().x - currentFrame.getAndroidBitmap().getWidth() / 2.0f);
        toReturn.y = (-Level.camera.y + (ItCameView.viewport.bottom / 2.0f) + getPosition().y - currentFrame.getAndroidBitmap().getHeight() + (br.odb.giovanni.engine.Constants.BASETILEHEIGHT / 2.0f));

        return toReturn;

    }

    public void draw(android.graphics.Canvas canvas,
                     android.graphics.Paint paint) {

        if (!visible) {
            return;
        }

        try {
            Vec2 screenPos = getScreenPosition();
            currentFrame.setX(screenPos.x);
            currentFrame.setY(screenPos.y);

            currentFrame.draw(canvas, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move(float x, float y) {
        position.x += x;
        position.y += y;
        didMove();
    }

    int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 Position) {
        position = Position;
    }

    public Rect getBounds() {
        return bounds;
    }

    states getState() {
        return state;
    }

    void setState(states state) {
        this.state = state;
    }

    void kill() {
        killed = true;
        visible = false;
    }

    public abstract void touched(Actor actor);

    protected abstract void didMove();

    public void prepareForGC() {
        position = null;
        bounds = null;
        state = null;
        currentFrame.prepareForGC();
        currentFrame = null;
        animation.prepareForGC();
        animation = null;
        level = null;
    }

    public enum states {
        STILL,
        MOVING,
    }
}
