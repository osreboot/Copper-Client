package com.osreboot.copper.client.environment;

import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.HvlCoord;

public final class WorldUtil {

	private WorldUtil(){}
	
	public static HvlCoord toTileSpace(HvlCoord entitySpaceArg){
		HvlCoord output = new HvlCoord();
		output.x = (entitySpaceArg.x - 0.25f) * 2f + ((float)EWorld.SIZE_X / 2f);
		output.y = (entitySpaceArg.y / EWorld.SCALE_Y) - 0.5f + ((float)EWorld.SIZE_Y / 2f);
		return output;
	}

	public static HvlCoord toEntitySpace(HvlCoord tileSpaceArg){
		HvlCoord output = new HvlCoord();
		output.x = (tileSpaceArg.x - ((float)EWorld.SIZE_X / 2f)) * 0.5f + 0.25f;
		output.y = (tileSpaceArg.y - ((float)EWorld.SIZE_Y / 2f) + 0.5f) * EWorld.SCALE_Y;
		return output;
	}
	
	public static <T> void loop2D(T[][] objects, HvlAction.A3<Integer, Integer, T> action){
		for(int x = 0; x < objects.length; x++){
			for(int y = 0; y < objects[0].length; y++){
				action.run(x, y, objects[x][y]);
			}
		}
	}
	
	public static FTileOrientation getOrientation(int x, int y){
		return (x + y) % 2 == 0 ? FTileOrientation.UP_ARROW : FTileOrientation.DOWN_ARROW;
	}
	
}
