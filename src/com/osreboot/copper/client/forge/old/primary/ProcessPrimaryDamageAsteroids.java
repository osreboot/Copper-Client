package com.osreboot.copper.client.forge.old.primary;

import java.util.Map;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.copper.client.forge.old.ForgeTag;
import com.osreboot.copper.client.forge.old.ForgeUtil;
import com.osreboot.copper.client.forge.old.ForgeUtil.AsteroidAABB;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ProcessPrimaryDamageAsteroids {

	private ProcessPrimaryDamageAsteroids(){}

	public static void run(TokenMetadata metadata, CTile[][] world, Map<ForgeTag, Double>[][] worldTags){
		Random random = new Random(metadata.seedTerrain.hashCode() + 2);

		boolean[][] worldHistory = new boolean[world.length][world[0].length];
		WorldUtil.loop2D(world, (x, y, t) -> {
			if(t != null && t.material == FTileMaterial.ASTEROID) worldHistory[x][y] = true;
		});
		WorldUtil.loop2D(world, (ax, ay, at) -> {
			if(worldTags[ax][ay].containsKey(ForgeTag.SEED_ASTEROID)){
				HvlCoord aLocation = WorldUtil.toEntitySpace(new HvlCoord(ax, ay));
				float aRadius = worldTags[ax][ay].get(ForgeTag.SEED_ASTEROID).floatValue();

				HvlCoord aabbMin = WorldUtil.toTileSpace(new HvlCoord(aLocation).subtract(aRadius + ForgeUtil.BUFFER_RADIUS_AABB, aRadius + ForgeUtil.BUFFER_RADIUS_AABB));
				HvlCoord aabbMax = WorldUtil.toTileSpace(new HvlCoord(aLocation).add(aRadius + ForgeUtil.BUFFER_RADIUS_AABB, aRadius + ForgeUtil.BUFFER_RADIUS_AABB));
				AsteroidAABB aabb = new AsteroidAABB((int)Math.floor(aabbMin.x), (int)Math.floor(aabbMin.y), (int)Math.ceil(aabbMax.x), (int)Math.ceil(aabbMax.y));

				if(aRadius > 10f){
					aabb.loop(world, (x, y, t) -> {
						if(world[x][y] != null){
							if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius){
								HvlCoord loc = WorldUtil.toEntitySpace(new HvlCoord(x, y));
								float carveChance = HvlMath.limit(HvlMath.map(HvlMath.distance(loc, aLocation), aRadius * 0.9f, 0f, 0.2f, 0.9f), 0.2f, 0.9f);
								if(random.nextFloat() < carveChance) world[x][y] = null;
							}
						}
					});
					
					// Iterate based on cellular automata
					for(int i = 0; i < 20; i++){
						int[][] worldDelta = new int[world.length][world[0].length];
						aabb.loop(world, (x, y, t) -> {
							int emptySides = 0;
							if(WorldUtil.getOrientation(x, y) == FTileOrientation.UP_ARROW){
								if(ForgeUtil.isEmpty(world, x, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 1, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 1, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 2, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 2, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 1, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 2, y + 1)) emptySides++;
							}else{
								if(ForgeUtil.isEmpty(world, x, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 1, y + 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 1, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 2, y)) emptySides++;
								if(ForgeUtil.isEmpty(world, x, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 1, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x - 2, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 1, y - 1)) emptySides++;
								if(ForgeUtil.isEmpty(world, x + 2, y - 1)) emptySides++;
							}

							if(emptySides > 7) worldDelta[x][y] = -1;
							if(emptySides < 6) worldDelta[x][y] = 1;
						});
						aabb.loop(world, (x, y, t) -> {
							if(worldDelta[x][y] > 0) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
							else if(worldDelta[x][y] < 0) world[x][y] = null;
						});
					}
					ForgeUtil.smartSmooth(world, aabb.xMin, aabb.yMin, aabb.xMax, aabb.yMax, FTileMaterial.PATHWAY);
				}
			}
		});
		WorldUtil.loop2D(world, (x, y, t) -> {
			if(ForgeUtil.isEmpty(world, x, y) && worldHistory[x][y]) world[x][y] = new CTile(x, y, FTileMaterial.PATHWAY);
		});
	}

}
