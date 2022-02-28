package com.osreboot.copper.client.forge.old;

import java.util.ArrayList;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.Noise2DPerlin;
import com.osreboot.copper.client.forge.old.OldForgeUtil.Mask;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class GeneratorTerrainProbability {

	private GeneratorTerrainProbability(){}

	public static final float
	SEED_RADIUS_BUFFER = 2f,
	SEED_RADIUS_BUFFER_RANDOM = 10f,
	SEED_RADIUS_MIN = 3f,
	SEED_RADIUS_MAX = 50f,
	SPAWN_RADIUS = 2f,
	FIELD_SIZE_LIMIT = 16f;

	public static final int
	SEED_ATTEMPTS = 16;

	public static GeneratorTerrainProbabilityOutput run(TokenMetadata metadata){
		// (Voids) Initialize noise
		Random randomVoids = new Random(metadata.seedVoids.hashCode());
		Noise2DPerlin noiseVoids = new Noise2DPerlin(randomVoids, 5,
				new HvlCoord(EWorld.DIAMETER * metadata.scaleVoids / -2f, EWorld.DIAMETER * metadata.scaleVoids / -2f),
				new HvlCoord(EWorld.DIAMETER * metadata.scaleVoids / 2f, EWorld.DIAMETER * metadata.scaleVoids / 2f));

		Random randomTerrain = new Random(metadata.seedAsteroids.hashCode());

		// Select seeds based on Poisson disc sampling
		ArrayList<SeedAsteroid> seedsActive = new ArrayList<>();
		ArrayList<SeedAsteroid> seeds = new ArrayList<>();

		SeedAsteroid seedInitial = new SeedAsteroid(new HvlCoord(
				randomTerrain.nextFloat() * EWorld.DIAMETER - EWorld.DIAMETER / 2f,
				randomTerrain.nextFloat() * EWorld.DIAMETER - EWorld.DIAMETER / 2f), getRandomRadius(randomTerrain));
		seedsActive.add(seedInitial);
		seeds.add(seedInitial);

		while(seedsActive.size() > 0){
			SeedAsteroid seedCurrent = seedsActive.get(randomTerrain.nextInt(seedsActive.size()));
			for(int i = 0; i < SEED_ATTEMPTS; i++){
				float testAngle = randomTerrain.nextFloat() * 2f * (float)Math.PI;
				float testRadius = getRandomRadius(randomTerrain);
				float testDistance = getRandomBuffer(randomTerrain) + seedCurrent.radius + testRadius;
				HvlCoord testLocation = new HvlCoord((float)Math.cos(testAngle) * testDistance, (float)Math.sin(testAngle) * testDistance).add(seedCurrent.location);
				SeedAsteroid seedTest = new SeedAsteroid(testLocation, testRadius);
				if(Math.abs(testLocation.x) + testRadius < EWorld.DIAMETER / 2f && Math.abs(testLocation.y) + testRadius < EWorld.DIAMETER / 2f){
					boolean valid = true;

					for(SeedAsteroid seed : seeds){
						if(HvlMath.distance(testLocation, seed.location) < testRadius + seed.radius + getRandomBuffer(randomTerrain) ||
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
		
		// (Voids) Remove asteroids based on noise mask
		seeds.removeIf(seed -> {
			return metadata.hasVoids && (randomTerrain.nextFloat() - 0.5f) * 2f/* - (seed.radius / 30f)*/ < noiseVoids.map(seed.location) * 4f;
		});

		// Assign output mask probability values
		Mask<Float> maskCellProbability = new Mask<>(0f);
		Mask<Float> maskCaveProbability = new Mask<>(1f);
		ForgeUtil.forWorld((x, y) -> {
			HvlCoord tLocation = WorldUtil.toEntitySpace(new HvlCoord(x, y));
			for(SeedAsteroid seed : seeds){
				if(HvlMath.distance(seed.location, tLocation) < seed.radius && seed.radius < 3f) maskCaveProbability.set(x, y, 0f);
				maskCellProbability.set(x, y, Math.min(maskCellProbability.get(x, y) + getProbabilityFromDistance(seed, HvlMath.distance(seed.location, tLocation)), 1f));
			}
		});

		GeneratorTerrainProbabilityOutput output = new GeneratorTerrainProbabilityOutput();
		output.maskSurfaceProbability = maskCellProbability;
		output.maskCaveProbability = maskCaveProbability;
		return output;
	}

	public static class GeneratorTerrainProbabilityOutput{
		public Mask<Float> maskSurfaceProbability;
		public Mask<Float> maskCaveProbability;
	}
	
	private static float getProbabilityFromDistance(SeedAsteroid seed, float distance){
		return HvlMath.limit(HvlMath.map(distance, 
				Math.max(seed.radius * 0.5f, seed.radius - FIELD_SIZE_LIMIT), 
				Math.min(seed.radius * 1.5f, seed.radius + FIELD_SIZE_LIMIT),
				1f, 0f), 0f, 1f);
	}

	private static float getRandomRadius(Random random){
		return (float)Math.pow(0.02f, random.nextFloat()) * (SEED_RADIUS_MAX - SEED_RADIUS_MIN) + SEED_RADIUS_MIN;
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
