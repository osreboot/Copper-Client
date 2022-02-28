package com.osreboot.copper.client.forge.planner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.forge.BlueprintAsteroid;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class PlannerAsteroids {
	
	private PlannerAsteroids(){}
	
	public static final float
	SEED_RADIUS_BUFFER = 2f,
	SEED_RADIUS_BUFFER_RANDOM = 10f,
	SEED_RADIUS_MIN = 3f,
	SEED_RADIUS_MAX = 50f,
	SPAWN_RADIUS = 2f;

	public static final int
	SEED_ATTEMPTS = 16;

	public static List<BlueprintAsteroid> run(TokenMetadata metadata){
		Random random = new Random(metadata.seedAsteroids.hashCode());

		// Select seeds based on Poisson disc sampling
		ArrayList<BlueprintAsteroid> blueprintsActive = new ArrayList<>();
		ArrayList<BlueprintAsteroid> blueprints = new ArrayList<>();

		BlueprintAsteroid blueprintInitial = new BlueprintAsteroid(new HvlCoord(
				random.nextFloat() * EWorld.DIAMETER - EWorld.DIAMETER / 2f,
				random.nextFloat() * EWorld.DIAMETER - EWorld.DIAMETER / 2f), getRandomRadius(random));
		blueprintsActive.add(blueprintInitial);
		blueprints.add(blueprintInitial);

		while(blueprintsActive.size() > 0){
			BlueprintAsteroid seedCurrent = blueprintsActive.get(random.nextInt(blueprintsActive.size()));
			for(int i = 0; i < SEED_ATTEMPTS; i++){
				float testAngle = random.nextFloat() * 2f * (float)Math.PI;
				float testRadius = getRandomRadius(random);
				float testDistance = getRandomBuffer(random) + seedCurrent.radius + testRadius;
				HvlCoord testLocation = new HvlCoord((float)Math.cos(testAngle) * testDistance, (float)Math.sin(testAngle) * testDistance).add(seedCurrent.location);
				BlueprintAsteroid blueprintTest = new BlueprintAsteroid(testLocation, testRadius);
				if(Math.abs(testLocation.x) + testRadius < EWorld.DIAMETER / 2f && Math.abs(testLocation.y) + testRadius < EWorld.DIAMETER / 2f){
					boolean valid = true;

					for(BlueprintAsteroid blueprint : blueprints){
						if(HvlMath.distance(testLocation, blueprint.location) < testRadius + blueprint.radius + getRandomBuffer(random) ||
								HvlMath.distance(testLocation, new HvlCoord()) < testRadius + SPAWN_RADIUS){
							valid = false;
							break;
						}
					}
					if(valid){
						blueprintsActive.add(blueprintTest);
						blueprints.add(blueprintTest);
					}
				}
			}
			blueprintsActive.remove(seedCurrent);
		}

		return blueprints;
	}

	private static float getRandomRadius(Random random){
		return (float)Math.pow(0.02f, random.nextFloat()) * (SEED_RADIUS_MAX - SEED_RADIUS_MIN) + SEED_RADIUS_MIN;
	}

	private static float getRandomBuffer(Random random){
		return random.nextFloat() * SEED_RADIUS_BUFFER_RANDOM + SEED_RADIUS_BUFFER;
	}

}
