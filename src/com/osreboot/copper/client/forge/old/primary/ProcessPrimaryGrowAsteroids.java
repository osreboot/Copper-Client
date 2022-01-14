package com.osreboot.copper.client.forge.old.primary;

import java.util.ArrayList;
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
import com.osreboot.copper.client.forge.old.ForgeUtil.Ellipse;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class ProcessPrimaryGrowAsteroids {

	private ProcessPrimaryGrowAsteroids(){}

	public static void run(TokenMetadata metadata, CTile[][] world, Map<ForgeTag, Double>[][] worldTags){
		Random random = new Random(metadata.seedTerrain.hashCode() + 1);

		WorldUtil.loop2D(world, (ax, ay, at) -> {
			if(worldTags[ax][ay].containsKey(ForgeTag.SEED_ASTEROID)){
				HvlCoord aLocation = WorldUtil.toEntitySpace(new HvlCoord(ax, ay));
				float aRadius = worldTags[ax][ay].get(ForgeTag.SEED_ASTEROID).floatValue();

				HvlCoord aabbMin = WorldUtil.toTileSpace(new HvlCoord(aLocation).subtract(aRadius + ForgeUtil.BUFFER_RADIUS_AABB, aRadius + ForgeUtil.BUFFER_RADIUS_AABB));
				HvlCoord aabbMax = WorldUtil.toTileSpace(new HvlCoord(aLocation).add(aRadius + ForgeUtil.BUFFER_RADIUS_AABB, aRadius + ForgeUtil.BUFFER_RADIUS_AABB));
				AsteroidAABB aabb = new AsteroidAABB((int)Math.floor(aabbMin.x), (int)Math.floor(aabbMin.y), (int)Math.ceil(aabbMax.x), (int)Math.ceil(aabbMax.y));

				if(aRadius <= 0.8f){
					handleFillSolid(world, random, aabb, aLocation, aRadius);
				}else if(aRadius < 3f){
					handleFillSolid(world, random, aabb, aLocation, aRadius);
					handleDeformMinor(world, random, aabb, aLocation, aRadius);
				}else{
					// aabb.loop(world, (x, y, t) -> { world[x][y] = new CTile(x, y, FTileMaterial.PATHWAY); });
					handleFillSubtractive(world, random, aabb, aLocation, aRadius);
					ForgeUtil.smartSmooth(world, aabb.xMin, aabb.yMin, aabb.xMax, aabb.yMax);

					//					handleFillCA(world, random, aabb, aLocation, aRadius);
					//					ForgeUtil.smartSmooth(world, aabb.xMin, aabb.yMin, aabb.xMax, aabb.yMax);
				}
			}
		});
	}

	// Raw set by radius, no smooth
	private static void handleFillSolid(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		aabb.loop(world, (x, y, t) -> {
			if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius)
				world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});
	}

	private static void handleFillSubtractive(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		int ellipseCount = Math.max(5, Math.round(aRadius * (float)Math.PI * 2f / 5f));

		ArrayList<Ellipse> ellipses = new ArrayList<>();
		for(int i = 0; i < ellipseCount; i++){
			float eRadius = random.nextFloat() * aRadius * (0.7f * (float)Math.pow(1f - 0.02f, aRadius) + 0.025f);
			// float eRadius = random.nextFloat() * aRadius * 0.6f;
			// float eOffsetAngle = random.nextFloat() * 360f;
			float eOffsetAngle = HvlMath.map(i, 0f, ellipseCount, 0, 360f);
			HvlCoord eOffset = new HvlCoord(
					(float)Math.cos(HvlMath.toRadians(eOffsetAngle)) * aRadius,
					(float)Math.sin(HvlMath.toRadians(eOffsetAngle)) * aRadius);
			Ellipse ellipse = new Ellipse(new HvlCoord(aLocation).subtract(eOffset), eRadius, random.nextFloat() * 0.5f + 0.5f, random.nextFloat() * 360f);
			ellipses.add(ellipse);
		}

		aabb.loop(world, (x, y, t) -> {
			if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius){
				HvlCoord loc = WorldUtil.toEntitySpace(new HvlCoord(x, y));
				float value = 0f;
				int samples = 0;
				for(float xs = loc.x - 1f; xs <= loc.x + 1f; xs += 1f){
					for(float ys = loc.y - 1f; ys <= loc.y + 1f; ys += 1f){
						samples++;
						boolean filled = true;
						for(Ellipse ellipse : ellipses){
							if(ellipse.isInside(new HvlCoord(xs, ys))){
								filled = false;
								break;
							}
						}
						if(filled) value += 1f;
					}
				}
				value /= (float)samples;

				if(value >= 0.5f) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
			}
		});
	}

	private static void handleFillCA(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		// Initialize with random values
		aabb.loop(world, (x, y, t) -> {
			if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius){
				HvlCoord loc = WorldUtil.toEntitySpace(new HvlCoord(x, y));
				float fillChance = 0f;
				if(HvlMath.distance(loc, aLocation) < aRadius * 0.7f) fillChance = 1f;
				else if(HvlMath.distance(loc, aLocation) < aRadius) fillChance = 0.5f;
				//				float fillChance = HvlMath.limit(HvlMath.map(HvlMath.distance(loc, aLocation), aRadius * 0.2f, aRadius, 1f, 0f), 0f, 1f);
				if(random.nextFloat() < fillChance) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
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
	}

	// Randomly delete border tiles then smooth
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
