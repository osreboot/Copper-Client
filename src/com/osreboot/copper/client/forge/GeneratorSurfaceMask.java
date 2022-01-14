package com.osreboot.copper.client.forge;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public final class GeneratorSurfaceMask {

	private GeneratorSurfaceMask(){}
	
	public static Mask<Boolean> run(TokenMetadata metadata, Mask<Float> maskSurfaceProbability){
		Random random = new Random(metadata.seedTerrain.hashCode() + 1);
		
		final Mask<Boolean> maskSurface = new Mask<>(false);
		
		ForgeUtil.forWorld((x, y) -> {
			maskSurface.set(x, y, random.nextFloat() < maskSurfaceProbability.get(x, y));
		});
		
		for(int i = 0; i < 3; i++){
			Mask<Boolean> maskSurfaceNew = new Mask<>(false);
			
			ForgeUtil.forWorld((x, y) -> {
				AtomicInteger neighbors = new AtomicInteger();
				ForgeUtil.forIndirectNeighbors(x, y, (nx, ny) -> {
					if(maskSurface.get(nx, ny)) neighbors.getAndIncrement();
				});
				if(neighbors.get() > 7) maskSurfaceNew.set(x, y, true);
				if(neighbors.get() < 6) maskSurfaceNew.set(x, y, false);
			});
			
			maskSurface.set(maskSurfaceNew);
		}
		
		return maskSurface;
	}
	
}
