package com.osreboot.copper.client.forge.primary;

import java.util.Map;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeTag;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ProcessPrimaryGrowAsteroids {

	private ProcessPrimaryGrowAsteroids(){}

	public static void run(TokenMetadata metadata, CTile[][] world, Map<ForgeTag, Double>[][] worldTags){
		WorldUtil.loop2D(world, (x, y, t) -> {
			if(worldTags[x][y].containsKey(ForgeTag.SEED_ASTEROID)){
				HvlCoord location = WorldUtil.toEntitySpace(new HvlCoord(x, y));
				float radius = worldTags[x][y].get(ForgeTag.SEED_ASTEROID).floatValue();
				for(int x2 = (int)(x - radius * 2f) - 2; x2 <= (int)(x + radius * 2f) + 2; x2++){
					for(int y2 = (int)(y - radius) - 2; y2 <= (int)(y + radius) + 2; y2++){
						if(HvlMath.distance(location, WorldUtil.toEntitySpace(new HvlCoord(x2, y2))) <= radius){
							world[x2][y2] = new CTile(x2, y2, FTileMaterial.ASTEROID);
						}
					}
				}
			}
		});
		
		ForgeUtil.dilate(world, null, FTileMaterial.ASTEROID);
	}

}
