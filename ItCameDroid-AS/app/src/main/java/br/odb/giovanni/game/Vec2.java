package br.odb.giovanni.game;

public class Vec2 {
	public float x;
	public float y;

	public Vec2(float aX, float aY) {
		x = aX;
		y = aY;
	}

	public Vec2() {
		x = 0;
		y = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vec2) {
			Vec2 v = (Vec2) o;
			return v.x == x && v.y == y;
		} else {
			return false;
		}
	}

	public boolean isCloseEnoughToConsiderEqualTo(Vec2 position) {
		return (Math.abs(x - position.x) < 1.0f) && (Math.abs(y - position.y) < 1.0f);
	}
}