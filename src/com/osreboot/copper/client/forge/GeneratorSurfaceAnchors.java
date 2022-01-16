package com.osreboot.copper.client.forge;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public class GeneratorSurfaceAnchors {

	private GeneratorSurfaceAnchors(){}
	
	public static Mask<Boolean> run(TokenMetadata metadata, Mask<Float> maskSurfaceProbability){
		final Mask<Boolean> maskSurfaceAnchors = new Mask<>(false);
		
		ForgeUtil.forWorld((x, y) -> {
			if(maskSurfaceProbability.get(x, y) >= 1f) maskSurfaceAnchors.set(x, y, true);
		});
		
		return maskSurfaceAnchors;
	}
	
}
