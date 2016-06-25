package com.benderapps.engy.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.benderapps.engy.world.World;

public class Block extends Image {
	public static final int MASTER = 0;
	public static final int SMALL_HORITZONTAL = 1;
	public static final int SMALL_VERTICAL = 2;
	public static final int BIG_HORITZONTAL = 3;
	public static final int BIG_VERTICAL = 4;

	private int type;
	private List<int[]> fieldPos;
//	public boolean can_move = true;

	public Block(TextureAtlas textureAtlas, int type, float x, float y) {
		super(getRegion(textureAtlas, type));
		fieldPos = new ArrayList<int[]>();
		this.type = type;

		setPosition(x + World.marginX, y + World.marginY);
	}

	private static TextureRegion getRegion(TextureAtlas textureAtlas, int type) {
		TextureRegion textureRegion = null;

		switch (type) {
		case MASTER:
			textureRegion = textureAtlas.findRegion("red_block");
			break;
		case SMALL_HORITZONTAL:
			textureRegion = textureAtlas
					.findRegion("small_horizontal");
			break;
		case SMALL_VERTICAL:
			textureRegion = textureAtlas
					.findRegion("small_vertical");
			break;
		case BIG_HORITZONTAL:
			textureRegion = textureAtlas
					.findRegion("big_horizontal");
			break;
		case BIG_VERTICAL:
			textureRegion = textureAtlas
					.findRegion("big_vertical");
			break;
		}

		return textureRegion;
	}

	public int getType() {
		return type;
	}

	public void setFieldPosition(int[] field) {
		fieldPos.add(field);
	}

	public List<int[]> getFieldPosition() {
		return fieldPos;
	}

	public void clearFieldPosition() {
		fieldPos.clear();
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public void moveTo(float oldX, float oldY, float x, float y) {
		if (type == MASTER || type == SMALL_HORITZONTAL
				|| type == BIG_HORITZONTAL) {
			setPosition(getX() + x - oldX, getY());
		} else {
			setPosition(getX(), getY() + y - oldY);
		}
	}
}
