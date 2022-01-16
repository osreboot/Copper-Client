package com.osreboot.copper.client.forge;

import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public final class GeneratorSurfaceMask {

	private GeneratorSurfaceMask(){}
	
	public static Mask<Boolean> run(TokenMetadata metadata, Mask<Float> maskSurfaceProbability, Mask<Boolean> maskSurfaceAnchors){
		Random random = new Random(metadata.seedTerrain.hashCode() + 1);
		
		final Mask<Boolean> maskSurface = new Mask<>(false);
		
		ForgeUtil.forWorld((x, y) -> {
			maskSurface.set(x, y, random.nextFloat() < maskSurfaceProbability.get(x, y));
		});
		
		for(int i = 0; i < 8; i++){
			Mask<Integer> maskNumberNeighbors = new Mask<>(0);
			
			ForgeUtil.forWorld((x, y) -> {
				if(maskSurface.get(x, y)){
					ForgeUtil.forIndirectNeighbors(x, y, (nx, ny) -> {
						maskNumberNeighbors.set(nx, ny, maskNumberNeighbors.get(nx, ny) + 1);
					});
				}
			});
			
			Mask<Boolean> maskSurfaceNew = new Mask<>(false);
			ForgeUtil.forWorld((x, y) -> {
				if(maskNumberNeighbors.get(x, y) > 7) maskSurfaceNew.set(x, y, true);
				if(maskNumberNeighbors.get(x, y) < 6) maskSurfaceNew.set(x, y, false);
				if(maskSurfaceAnchors.get(x, y)) maskSurfaceNew.set(x, y, true);
			});
			maskSurface.set(maskSurfaceNew);
		}
		
		return maskSurface;
	}
	
}
