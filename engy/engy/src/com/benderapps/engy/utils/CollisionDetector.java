package com.benderapps.engy.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.benderapps.engy.world.World;
import com.benderapps.engy.world.World.CollisionResult;

public class CollisionDetector {

	public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
		if (r1.x < r2.x + r2.width && r1.x + r1.width > r2.x
				&& r1.y < r2.y + r2.height && r1.y + r1.height > r2.y)
			return true;
		else
			return false;
	}

	public static boolean pointInRectangle(Rectangle r, Vector2 p) {
		return r.x <= p.x && r.x + r.width >= p.x && r.y <= p.y
				&& r.y + r.height >= p.y;
	}

	public static boolean pointInRectangle(Rectangle r, float x, float y) {
		return r.x <= x && r.x + r.width >= x && r.y <= y
				&& r.y + r.height >= y;
	}

	public static CollisionResult rectangleOverlapWorld(Rectangle r, int i, int j,
			int exitTop, int exitBottom) {
		// check exit
		if (r.x + r.width > i && r.y > exitTop && r.y <= exitBottom) {
			return CollisionResult.EXIT;
		}
		// check walls
		if (r.x < 0 + World.marginX || r.x + r.width > i
				|| r.y < 0 + World.marginY || r.y + r.height > j)
			return CollisionResult.COLLISION;
		else
			return CollisionResult.NO_COLLISION;
	}

}
