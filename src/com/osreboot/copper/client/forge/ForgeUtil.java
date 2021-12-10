package com.osreboot.copper.client.forge;

import java.util.ArrayList;

import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlCoord;

public final class ForgeUtil {

	private ForgeUtil(){}

	// TODO support dilation to null
	public static void dilate(CTile[][] world, FTileMaterial mFrom, FTileMaterial mTo){
		ArrayList<HvlCoord> toChange = new ArrayList<>();
		WorldUtil.loop2D(world, (x, y, t) -> {
			if(t != null){
				if(t.material == mTo){
					if(t.orientation == FTileOrientation.UP_ARROW){
						if(isInBounds(world, x, y - 1)) toChange.add(new HvlCoord(x, y - 1));
						if(isInBounds(world, x - 1, y - 1)) toChange.add(new HvlCoord(x - 1, y - 1));
						if(isInBounds(world, x + 1, y - 1)) toChange.add(new HvlCoord(x + 1, y - 1));
						if(isInBounds(world, x - 1, y)) toChange.add(new HvlCoord(x - 1, y));
						if(isInBounds(world, x - 2, y)) toChange.add(new HvlCoord(x - 2, y));
						if(isInBounds(world, x + 1, y)) toChange.add(new HvlCoord(x + 1, y));
						if(isInBounds(world, x + 2, y)) toChange.add(new HvlCoord(x + 2, y));
						if(isInBounds(world, x, y + 1)) toChange.add(new HvlCoord(x, y + 1));
						if(isInBounds(world, x - 1, y + 1)) toChange.add(new HvlCoord(x - 1, y + 1));
						if(isInBounds(world, x - 2, y + 1)) toChange.add(new HvlCoord(x - 2, y + 1));
						if(isInBounds(world, x + 1, y + 1)) toChange.add(new HvlCoord(x + 1, y + 1));
						if(isInBounds(world, x + 2, y + 1)) toChange.add(new HvlCoord(x + 2, y + 1));
					}else{
						if(isInBounds(world, x, y + 1)) toChange.add(new HvlCoord(x, y + 1));
						if(isInBounds(world, x - 1, y + 1)) toChange.add(new HvlCoord(x - 1, y + 1));
						if(isInBounds(world, x + 1, y + 1)) toChange.add(new HvlCoord(x + 1, y + 1));
						if(isInBounds(world, x - 1, y)) toChange.add(new HvlCoord(x - 1, y));
						if(isInBounds(world, x - 2, y)) toChange.add(new HvlCoord(x - 2, y));
						if(isInBounds(world, x + 1, y)) toChange.add(new HvlCoord(x + 1, y));
						if(isInBounds(world, x + 2, y)) toChange.add(new HvlCoord(x + 2, y));
						if(isInBounds(world, x, y - 1)) toChange.add(new HvlCoord(x, y - 1));
						if(isInBounds(world, x - 1, y - 1)) toChange.add(new HvlCoord(x - 1, y - 1));
						if(isInBounds(world, x - 2, y - 1)) toChange.add(new HvlCoord(x - 2, y - 1));
						if(isInBounds(world, x + 1, y - 1)) toChange.add(new HvlCoord(x + 1, y - 1));
						if(isInBounds(world, x + 2, y - 1)) toChange.add(new HvlCoord(x + 2, y - 1));
					}
				}
			}
		});
		for(HvlCoord c : toChange){
			if(world[(int)c.x][(int)c.y] != null){
				if(world[(int)c.x][(int)c.y].material == mFrom) world[(int)c.x][(int)c.y].material = mTo;
			}else if(mFrom == null){
				world[(int)c.x][(int)c.y] = new CTile((int)c.x, (int)c.y, mTo);
			}
		}
	}

	private static boolean isInBounds(CTile[][] world, int x, int y){
		return x >= 0 && x <= world.length - 1 && y >= 0 && y <= world[0].length - 1;
	}

}
