package com.osreboot.copper.client.forge;

import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public final class Forge {

	private Forge(){}
	
	public static void run(TokenMetadata metadata, CTile[][] world){
		Random random = new Random(metadata.seedTerrain.hashCode());
		
		Mask<Float> maskSurfaceProbability = GeneratorSurfaceProbability.run(metadata);
		Mask<Boolean> maskSurface = GeneratorSurfaceMask.run(metadata, maskSurfaceProbability);
		
		ForgeUtil.forWorld((x, y) -> {
			if(maskSurface.get(x, y)) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});
	}
	
}
