package com.osreboot.copper.client.forge;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public final class Forge {

	private Forge(){}
	
	public static void run(TokenMetadata metadata, CTile[][] world){
		Mask<Float> maskSurfaceProbability = GeneratorSurfaceProbability.run(metadata);
		Mask<Boolean> maskSurfaceAnchors = GeneratorSurfaceAnchors.run(metadata, maskSurfaceProbability);
		Mask<Boolean> maskSurface = GeneratorSurfaceMask.run(metadata, maskSurfaceProbability, maskSurfaceAnchors);
		
		ForgeUtil.smartSmooth(maskSurface);
		
		ForgeUtil.forWorld((x, y) -> {
			if(maskSurface.get(x, y)) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});
	}
	
}
