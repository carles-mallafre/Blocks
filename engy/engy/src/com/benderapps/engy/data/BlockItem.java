package com.benderapps.engy.data;

import com.badlogic.gdx.utils.Array;

public class BlockItem {
	public int type;
	public int x;
	public int y;
	public Array<BlockField> fields;

	public BlockItem() {
		fields = new Array<BlockField>();
	}
}
