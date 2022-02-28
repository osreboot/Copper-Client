package com.osreboot.copper.client.forge.old;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.old.GeneratorTerrainProbability.GeneratorTerrainProbabilityOutput;
import com.osreboot.copper.client.forge.old.OldForgeUtil.Mask;

public final class OldForge {

	private OldForge(){}
	
	public static void run(TokenMetadata metadata, CTile[][] world){
		GeneratorTerrainProbabilityOutput generatorTerrainProbabilityOutput = GeneratorTerrainProbability.run(metadata);
		Mask<Float> maskSurfaceProbability = generatorTerrainProbabilityOutput.maskSurfaceProbability;
		Mask<Float> maskCaveProbability = generatorTerrainProbabilityOutput.maskCaveProbability;
		
		Mask<Boolean> maskSurfaceAnchors = GeneratorSurfaceAnchors.run(metadata, maskSurfaceProbability);
		Mask<Boolean> maskSurface = GeneratorSurfaceMask.run(metadata, maskSurfaceProbability, maskSurfaceAnchors);
		Mask<Integer> maskSurfaceDepth = GeneratorSurfaceDepth.run(metadata, maskSurface);
		
		Mask<Boolean> maskCaves = GeneratorCaveMask.run(metadata, maskSurface, maskSurfaceDepth, maskCaveProbability);
		
		OldForgeUtil.smartSmooth(maskSurface);
		
		ForgeUtil.forWorld((x, y) -> {
			if(maskSurface.get(x, y) && maskCaves.get(x, y)) world[x][y] = new CTile(x, y, FTileMaterial.PATHWAY);
			else if(maskSurface.get(x, y)) world[x][y] = new CTile(x, y, FTileMaterial.ASTEROID);
		});
	}
	
}
