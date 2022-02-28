package com.osreboot.copper.client.forge.old;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.old.OldForgeUtil.Mask;

public final class GeneratorSurfaceAnchors {

	private GeneratorSurfaceAnchors(){}
	
	public static Mask<Boolean> run(TokenMetadata metadata, Mask<Float> maskSurfaceProbability){
		final Mask<Boolean> maskSurfaceAnchors = new Mask<>(false);
		
		ForgeUtil.forWorld((x, y) -> {
			if(maskSurfaceProbability.get(x, y) >= 1f) maskSurfaceAnchors.set(x, y, true);
		});
		
		return maskSurfaceAnchors;
	}
	
}
