package com.osreboot.copper.client.forge.primary;

import java.util.Map;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeTag;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ProcessPrimaryGrowAsteroids {

	private ProcessPrimaryGrowAsteroids(){}

	public static final float
	BUFFER_LOOP_RADIUS = 2f;
	
	public static void run(TokenMetadata metadata, CTile[][] world, Map<ForgeTag, Double>[][] worldTags){
		Random random = new Random(metadata.seedTerrain.hashCode() + 1);

		WorldUtil.loop2D(world, (ax, ay, at) -> {
			if(worldTags[ax][ay].containsKey(ForgeTag.SEED_ASTEROID)){
				HvlCoord aLocation = WorldUtil.toEntitySpace(new HvlCoord(ax, ay));
				float aRadius = worldTags[ax][ay].get(ForgeTag.SEED_ASTEROID).floatValue();
				
				HvlCoord aabbMin = WorldUtil.toTileSpace(new HvlCoord(aLocation).subtract(aRadius + BUFFER_LOOP_RADIUS, aRadius + BUFFER_LOOP_RADIUS));
				HvlCoord aabbMax = WorldUtil.toTileSpace(new HvlCoord(aLocation).add(aRadius + BUFFER_LOOP_RADIUS, aRadius + BUFFER_LOOP_RADIUS));
				AsteroidAABB aabb = new AsteroidAABB((int)Math.floor(aabbMin.x), (int)Math.floor(aabbMin.y), (int)Math.ceil(aabbMax.x), (int)Math.ceil(aabbMax.y));
				
				aabb.loop(world, (x, y, t) -> {
					if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius)
						world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
				});
				
				ForgeUtil.smartSmooth(world, aabb.xMin, aabb.yMin, aabb.xMax, aabb.yMax);
			}
		});
	}
	
	private static class AsteroidAABB{
		
		private final int xMin, yMin, xMax, yMax;
		
		private AsteroidAABB(int xMinArg, int yMinArg, int xMaxArg, int yMaxArg){
			xMin = xMinArg;
			yMin = yMinArg;
			xMax = xMaxArg;
			yMax = yMaxArg;
		}
		
		private void loop(CTile[][] world, HvlAction.A3<Integer, Integer, CTile> action){
			for(int x = xMin; x <= xMax; x++){
				for(int y = yMin; y <= yMax; y++){
					if(ForgeUtil.isInBounds(world, x, y)) action.run(x, y, world[x][y]);
				}
			}
		}
		
	}

}
