package com.benderapps.engy.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.benderapps.engy.actors.Block;
import com.benderapps.engy.actors.Blocks;
import com.benderapps.engy.utils.CollisionDetector;

public class World {
	public enum CollisionResult {
		NO_COLLISION, COLLISION, EXIT
	}

	public static final int WORLD_WIDTH = 6;
	public static final int WORLD_HEIGHT = 6;
	public static final int BLOCK_WEIGHT = 70;
	public static final int marginX = 190;
	public static final int marginY = 30;

	public boolean level_complete = false;
	public int score;
	public Blocks blocks;
	public Block current;
	public int current_level;

	public World(TextureAtlas textureAtlas, int level) {
		blocks = new Blocks(textureAtlas, level);
		current_level = level;
		score = 0;
	}

	private CollisionResult checkCollisions() {
		if (current != null) {
			SnapshotArray<Actor> actors = blocks.getChildren();
			for (int i = 0; i < actors.size; i++) {
				Block block = (Block) actors.get(i);
				if (!block.equals(current)
						&& CollisionDetector.overlapRectangles(
								block.getBounds(), current.getBounds())) {
					// moveToNonCollisionPosition();
					// current.can_move = false;
					return CollisionResult.COLLISION;
				}
			}
			// check collision with walls
			return CollisionDetector.rectangleOverlapWorld(current.getBounds(),
					WORLD_WIDTH * BLOCK_WEIGHT + marginX, WORLD_HEIGHT
							* BLOCK_WEIGHT + marginY, 2 * BLOCK_WEIGHT
							+ marginY, 3 * BLOCK_WEIGHT + marginY);
		}
		return CollisionResult.NO_COLLISION;
	}

	public void moveToNonCollisionPosition() {
		if (current != null && current.getFieldPosition().size() > 0) {
			int[] aux = current.getFieldPosition().get(0);
			current.setPosition(aux[0] * BLOCK_WEIGHT + marginX, aux[1]
					* BLOCK_WEIGHT + marginY);
		}
	}

	public Block blockInXY(float x, float y) {
		SnapshotArray<Actor> actors = blocks.getChildren();
		for (int i = 0; i < actors.size; i++) {
			Block block = (Block) actors.get(i);
			if (CollisionDetector.pointInRectangle(block.getBounds(), x, y)) {
				return block;
			}
		}
		return null;
	}

	public void moveCurrentTo(float oldX, float oldY, float x, float y) {
		int start_fieldX = (int) ((current.getX() - marginX) / BLOCK_WEIGHT);
		int start_fieldY = (int) ((current.getY() - marginY) / BLOCK_WEIGHT);
		current.moveTo(oldX, oldY, x, y);
		switch (checkCollisions()) {
		case COLLISION:
			moveToNonCollisionPosition();
			break;
		case NO_COLLISION:
			float modX = (current.getX() - marginX) % BLOCK_WEIGHT;
			float modY = (current.getY() - marginY) % BLOCK_WEIGHT;
			int end_fieldX = (int) ((current.getX() - marginX) / BLOCK_WEIGHT);
			int end_fieldY = (int) ((current.getY() - marginY) / BLOCK_WEIGHT);

			switch (current.getType()) {
			case Block.MASTER:
				current.clearFieldPosition();
				if (modX > BLOCK_WEIGHT / 2 && start_fieldX == end_fieldX) {
					current.setFieldPosition(new int[] { start_fieldX + 1,
							start_fieldY });
					current.setFieldPosition(new int[] { start_fieldX + 2,
							start_fieldY });
				} else {
					current.setFieldPosition(new int[] { end_fieldX,
							start_fieldY });
					current.setFieldPosition(new int[] { end_fieldX + 1,
							start_fieldY });
				}
				if (current.getFieldPosition().get(0)[0] == 4
						&& current.getFieldPosition().get(0)[1] == 3) {
					level_complete = true;
				}
				break;
			case Block.SMALL_HORITZONTAL:
				current.clearFieldPosition();
				if (modX > BLOCK_WEIGHT / 2 && start_fieldX == end_fieldX) {
					current.setFieldPosition(new int[] { start_fieldX + 1,
							start_fieldY });
					current.setFieldPosition(new int[] { start_fieldX + 2,
							start_fieldY });
				} else {
					current.setFieldPosition(new int[] { end_fieldX,
							start_fieldY });
					current.setFieldPosition(new int[] { end_fieldX + 1,
							start_fieldY });
				}
				break;
			case Block.SMALL_VERTICAL:
				current.clearFieldPosition();
				if (modY > BLOCK_WEIGHT / 2 && start_fieldY == end_fieldY) {
					current.setFieldPosition(new int[] { start_fieldX,
							start_fieldY + 1 });
					current.setFieldPosition(new int[] { start_fieldX,
							start_fieldY + 2 });
				} else {
					current.setFieldPosition(new int[] { end_fieldX, end_fieldY });
					current.setFieldPosition(new int[] { end_fieldX,
							end_fieldY + 1 });
				}
				break;
			case Block.BIG_HORITZONTAL:
				current.clearFieldPosition();
				if (modX > BLOCK_WEIGHT / 2 && start_fieldX == end_fieldX) {
					current.setFieldPosition(new int[] { start_fieldX + 1,
							start_fieldY });
					current.setFieldPosition(new int[] { start_fieldX + 2,
							start_fieldY });
					current.setFieldPosition(new int[] { start_fieldX + 3,
							start_fieldY });
				} else {
					current.setFieldPosition(new int[] { end_fieldX,
							start_fieldY });
					current.setFieldPosition(new int[] { end_fieldX + 1,
							start_fieldY });
					current.setFieldPosition(new int[] { end_fieldX + 2,
							start_fieldY });
				}
				break;
			case Block.BIG_VERTICAL:
				current.clearFieldPosition();
				if (modY > BLOCK_WEIGHT / 2 && start_fieldY == end_fieldY) {
					current.setFieldPosition(new int[] { start_fieldX,
							start_fieldY + 1 });
					current.setFieldPosition(new int[] { start_fieldX,
							start_fieldY + 2 });
					current.setFieldPosition(new int[] { start_fieldX,
							start_fieldY + 3 });
				} else {
					current.setFieldPosition(new int[] { end_fieldX, end_fieldY });
					current.setFieldPosition(new int[] { end_fieldX,
							end_fieldY + 1 });
					current.setFieldPosition(new int[] { end_fieldX,
							end_fieldY + 2 });
				}
				break;
			}
			break;
		case EXIT:
			level_complete = true;
			break;
		}
	}
}
