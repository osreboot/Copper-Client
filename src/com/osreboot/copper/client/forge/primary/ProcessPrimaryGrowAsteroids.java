package com.osreboot.copper.client.forge.primary;

import java.util.Map;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.copper.client.forge.ForgeTag;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ProcessPrimaryGrowAsteroids {

	private ProcessPrimaryGrowAsteroids(){}

	public static final float
	BUFFER_RADIUS_AABB = 2f;

	public static void run(TokenMetadata metadata, CTile[][] world, Map<ForgeTag, Double>[][] worldTags){
		Random random = new Random(metadata.seedTerrain.hashCode() + 1);

		WorldUtil.loop2D(world, (ax, ay, at) -> {
			if(worldTags[ax][ay].containsKey(ForgeTag.SEED_ASTEROID)){
				HvlCoord aLocation = WorldUtil.toEntitySpace(new HvlCoord(ax, ay));
				float aRadius = worldTags[ax][ay].get(ForgeTag.SEED_ASTEROID).floatValue();

				HvlCoord aabbMin = WorldUtil.toTileSpace(new HvlCoord(aLocation).subtract(aRadius + BUFFER_RADIUS_AABB, aRadius + BUFFER_RADIUS_AABB));
				HvlCoord aabbMax = WorldUtil.toTileSpace(new HvlCoord(aLocation).add(aRadius + BUFFER_RADIUS_AABB, aRadius + BUFFER_RADIUS_AABB));
				AsteroidAABB aabb = new AsteroidAABB((int)Math.floor(aabbMin.x), (int)Math.floor(aabbMin.y), (int)Math.ceil(aabbMax.x), (int)Math.ceil(aabbMax.y));

				if(aRadius <= 0.8f){
					handleFillSolid(world, random, aabb, aLocation, aRadius);
				}else if(aRadius < 3f){
					handleFillSolid(world, random, aabb, aLocation, aRadius);
					handleDeformMinor(world, random, aabb, aLocation, aRadius);
				}else{
					handleFillSolid(world, random, aabb, aLocation, aRadius);
					ForgeUtil.smartSmooth(world, aabb.xMin, aabb.yMin, aabb.xMax, aabb.yMax);
				}
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

	private static void handleFillSolid(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		aabb.loop(world, (x, y, t) -> {
			if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius)
				world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});
	}

	private static void handleDeformMinor(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		aabb.loop(world, (x, y, t) -> {
			if(world[x][y] != null){
				int emptySides = 0;
				if(world[x][y].orientation == FTileOrientation.UP_ARROW){
					if(!ForgeUtil.isInBounds(world, x, y + 1) || world[x][y + 1] == null || !world[x][y + 1].material.solid) emptySides++;
				}else{
					if(!ForgeUtil.isInBounds(world, x, y - 1) || world[x][y - 1] == null || !world[x][y - 1].material.solid) emptySides++;
				}
				if(!ForgeUtil.isInBounds(world, x - 1, y) || world[x - 1][y] == null || !world[x - 1][y].material.solid) emptySides++;
				if(!ForgeUtil.isInBounds(world, x + 1, y) || world[x + 1][y] == null || !world[x + 1][y].material.solid) emptySides++;
				if(emptySides >= 1 && random.nextFloat() < 0.3f) world[x][y] = null;
			}
		});

		ForgeUtil.smartSmooth(world, aabb.xMin, aabb.yMin, aabb.xMax, aabb.yMax);
	}

}
