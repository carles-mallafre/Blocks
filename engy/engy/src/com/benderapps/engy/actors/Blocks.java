package com.benderapps.engy.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.benderapps.engy.data.BlockItem;
import com.benderapps.engy.world.World;

public class Blocks extends Group {

	public Blocks(TextureAtlas textureAtlas, int level) {
		FileHandle file = Gdx.files.internal("levels/level" + level + ".json");
		Json json = new Json();
		@SuppressWarnings("unchecked")
		Array<BlockItem> items = json.fromJson(Array.class, BlockItem.class,
				file);
		
		for (BlockItem item : items) {
			Block platform = new Block(textureAtlas, item.type,
					World.BLOCK_WEIGHT * item.x, World.BLOCK_WEIGHT * item.y);
			for (int i = 0; i < item.fields.size; i++) {
				platform.setFieldPosition(new int[] { item.fields.get(i).x,
						item.fields.get(i).y });
			}
			addActor(platform);
		}

	}

}
