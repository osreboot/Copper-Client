package com.osreboot.copper.client.forge.planner;

import java.util.List;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.forge.BlueprintAsteroid;
import com.osreboot.copper.client.forge.Noise2DPerlin;
import com.osreboot.ridhvl2.HvlCoord;

public final class PlannerVoids {

	private PlannerVoids(){}
	
	public static void run(TokenMetadata metadata, List<BlueprintAsteroid> blueprints){
		Random randomVoids = new Random(metadata.seedVoids.hashCode());
		Noise2DPerlin noiseVoids = new Noise2DPerlin(randomVoids, 5,
				new HvlCoord(EWorld.DIAMETER * metadata.scaleVoids / -2f, EWorld.DIAMETER * metadata.scaleVoids / -2f),
				new HvlCoord(EWorld.DIAMETER * metadata.scaleVoids / 2f, EWorld.DIAMETER * metadata.scaleVoids / 2f));

		blueprints.removeIf(seed -> {
			return metadata.hasVoids && (randomVoids.nextFloat() - 0.5f) * 2f/* - (seed.radius / 30f)*/ < noiseVoids.map(seed.location) * 4f;
		});
	}
	
}
