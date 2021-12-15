package com.osreboot.copper.client.forge.primary;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.copper.client.forge.ForgeTag;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.ForgeUtil.Ellipse;
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
//										aabb.loop(world, (x, y, t) -> { world[x][y] = new CTile(x, y, FTileMaterial.PATHWAY); });
					handleFillSubtractive(world, random, aabb, aLocation, aRadius);
					//					handleFillSolid(world, random, aabb, aLocation, aRadius);
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

	// Raw set by radius, no smooth
	private static void handleFillSolid(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		aabb.loop(world, (x, y, t) -> {
			if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius)
				world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});
	}

	private static void handleFillAdditive(CTile[][] world, Random random, AsteroidAABB aabb, HvlCoord aLocation, float aRadius){
		// Randomly distribute ellipses around center point
		ArrayList<Ellipse> ellipses = new ArrayList<>();
		for(int i = 0; i < random.nextInt(3) + 1; i++){
			float eAngle = random.nextFloat() * 360f;
			float eRadius = aRadius / 2f;
			HvlCoord eOffset = new HvlCoord(
					(float)Math.cos(HvlMath.toRadians(eAngle)) * eRadius * 0.8f,
					(float)Math.sin(HvlMath.toRadians(eAngle)) * eRadius * 0.8f);
			Ellipse ellipse = new Ellipse(new HvlCoord(aLocation).subtract(eOffset), eRadius, random.nextFloat() * 0.8f + 0.2f, eAngle);
			ellipses.add(ellipse);
		}

		// Center ellipses based on collective center-of-mass
		HvlCoord ellipsesCenterOfMass = new HvlCoord();
		float ellipsesTotalMass = 0f;
		for(Ellipse ellipse : ellipses){
			float eMass = (float)Math.PI * ellipse.radius * ellipse.radius * ellipse.ratio;
			ellipsesTotalMass += eMass;
			ellipsesCenterOfMass.add(new HvlCoord(ellipse.location).subtract(aLocation).multiply(eMass));
		}
		ellipsesCenterOfMass.divide(ellipsesTotalMass);
		for(Ellipse ellipse : ellipses) ellipse.location.subtract(ellipsesCenterOfMass);

		aabb.loop(world, (x, y, t) -> {
			for(Ellipse ellipse : ellipses){
				if(ellipse.isInside(WorldUtil.toEntitySpace(new HvlCoord(x, y)))){
					world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
					break;
				}
			}
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

		/*
		aabb.loop(world, (x, y, t) -> {
			boolean filled = false;
			if(HvlMath.distance(aLocation, WorldUtil.toEntitySpace(new HvlCoord(x, y))) < aRadius) filled = true;
			for(Ellipse ellipse : ellipses){
				if(ellipse.isInside(WorldUtil.toEntitySpace(new HvlCoord(x, y)))){
					filled = false;
					break;
				}
			}
			if(filled) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});*/
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
