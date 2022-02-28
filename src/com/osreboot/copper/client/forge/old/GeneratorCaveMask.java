package com.osreboot.copper.client.forge.old;

import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.Noise2DPerlin;
import com.osreboot.copper.client.forge.old.OldForgeUtil.Mask;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class GeneratorCaveMask {

	private GeneratorCaveMask(){}

	public static Mask<Boolean> run(TokenMetadata metadata, Mask<Boolean> maskSurface, Mask<Integer> maskSurfaceDepth, Mask<Float> maskCaveProbability){
		Random random = new Random(metadata.seedAsteroids.hashCode() + 3);

		final Mask<Boolean> maskCaves = new Mask<>(false);

		Noise2DPerlin noise = new Noise2DPerlin(random, 40,
				new HvlCoord(EWorld.DIAMETER * metadata.scaleVoids / -2f, EWorld.DIAMETER * metadata.scaleVoids / -2f),
				new HvlCoord(EWorld.DIAMETER * metadata.scaleVoids / 2f, EWorld.DIAMETER * metadata.scaleVoids / 2f));

		ForgeUtil.forWorld((x, y) -> {
			if(maskSurface.get(x, y)){
				float probability = noise.map(WorldUtil.toEntitySpace(new HvlCoord(x, y))) + 0.4f;
				probability += HvlMath.map(HvlMath.limit(maskSurfaceDepth.get(x, y), 1, 20), 1f, 20f, 0f, 0.6f);
				probability *= maskCaveProbability.get(x, y);
				maskCaves.set(x, y, random.nextFloat() < probability);
			}
		});

		for(int i = 0; i < 4; i++){
			Mask<Float> maskNumberNeighbors = new Mask<>(12f);

			ForgeUtil.forWorld((x, y) -> {
				if(!maskCaves.get(x, y)){
					if(!maskSurface.get(x, y)){
						ForgeUtil.forIndirectNeighbors(x, y, (nx, ny) -> {
//							maskNumberNeighbors.set(nx, ny, maskNumberNeighbors.get(nx, ny) - 0.5f);
						});
					}else{
						ForgeUtil.forIndirectNeighbors(x, y, (nx, ny) -> {
							maskNumberNeighbors.set(nx, ny, maskNumberNeighbors.get(nx, ny) - 1f);
						});
					}
				}
			});

			Mask<Boolean> maskCavesNew = new Mask<>(false);
			ForgeUtil.forWorld((x, y) -> {
				if(maskNumberNeighbors.get(x, y) > 8.5f) maskCavesNew.set(x, y, true);
				if(maskNumberNeighbors.get(x, y) < 3.5f) maskCavesNew.set(x, y, false);
			});
			maskCaves.set(maskCavesNew);
		}

		return maskCaves;
	}

}
