package com.osreboot.copper.client.forge;

import java.util.ArrayList;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class GeneratorSurfaceProbability {

	private GeneratorSurfaceProbability(){}

	public static final float
	SEED_RADIUS_BUFFER = 2f,
	SEED_RADIUS_BUFFER_RANDOM = 20f,
	SEED_RADIUS_MIN = 0.4f,
	SEED_RADIUS_MAX = 50f,
	SPAWN_RADIUS = 2f;

	public static final int
	SEED_ATTEMPTS = 16;

	public static Mask<Float> run(TokenMetadata metadata){
		Random random = new Random(metadata.seedTerrain.hashCode());
		
		Mask<Float> maskCellProbability = new Mask<>(0f);

		// Select seeds based on Poisson disc sampling
		ArrayList<SeedAsteroid> seedsActive = new ArrayList<>();
		ArrayList<SeedAsteroid> seeds = new ArrayList<>();

		SeedAsteroid seedInitial = new SeedAsteroid(new HvlCoord(
				random.nextFloat() * EWorld.DIAMETER - EWorld.DIAMETER / 2f,
				random.nextFloat() * EWorld.DIAMETER - EWorld.DIAMETER / 2f), getRandomRadius(random));
		seedsActive.add(seedInitial);
		seeds.add(seedInitial);

		while(seedsActive.size() > 0){
			SeedAsteroid seedCurrent = seedsActive.get(random.nextInt(seedsActive.size()));
			for(int i = 0; i < SEED_ATTEMPTS; i++){
				float testAngle = random.nextFloat() * 2f * (float)Math.PI;
				float testRadius = getRandomRadius(random);
				float testDistance = getRandomBuffer(random) + seedCurrent.radius + testRadius;
				HvlCoord testLocation = new HvlCoord((float)Math.cos(testAngle) * testDistance, (float)Math.sin(testAngle) * testDistance).add(seedCurrent.location);
				SeedAsteroid seedTest = new SeedAsteroid(testLocation, testRadius);
				if(Math.abs(testLocation.x) + testRadius < EWorld.DIAMETER / 2f && Math.abs(testLocation.y) + testRadius < EWorld.DIAMETER / 2f){
					boolean valid = true;
					for(SeedAsteroid seed : seeds){
						if(HvlMath.distance(testLocation, seed.location) < testRadius + seed.radius + getRandomBuffer(random) ||
								HvlMath.distance(testLocation, new HvlCoord()) < testRadius + SPAWN_RADIUS){
							valid = false;
							break;
						}
					}
					if(valid){
						seedsActive.add(seedTest);
						seeds.add(seedTest);
					}
				}
			}
			seedsActive.remove(seedCurrent);
		}

		ForgeUtil.forWorld((x, y) -> {
			HvlCoord tLocation = WorldUtil.toEntitySpace(new HvlCoord(x, y));
			for(SeedAsteroid seed : seeds){
				maskCellProbability.set(x, y, Math.min(maskCellProbability.get(x, y) + getProbabilityFromDistance(seed, HvlMath.distance(seed.location, tLocation)), 1f));
			}
		});

		return maskCellProbability;
	}

	private static float getProbabilityFromDistance(SeedAsteroid seed, float distance){
		return HvlMath.limit(HvlMath.map(distance, seed.radius * 0.8f, seed.radius * 1.2f, 1f, 0f), 0f, 1f);
	}
	
	private static float getRandomRadius(Random random){
		return (float)Math.pow(0.0005f, random.nextFloat()) * (SEED_RADIUS_MAX - SEED_RADIUS_MIN) + SEED_RADIUS_MIN;
	}

	private static float getRandomBuffer(Random random){
		return random.nextFloat() * SEED_RADIUS_BUFFER_RANDOM + SEED_RADIUS_BUFFER;
	}

	private static class SeedAsteroid{

		private HvlCoord location;
		private float radius;

		private SeedAsteroid(HvlCoord locationArg, float radiusArg){
			location = locationArg;
			radius = radiusArg;
		}

	}

}
