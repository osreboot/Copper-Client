package com.osreboot.copper.client.forge.old;

import java.util.concurrent.atomic.AtomicInteger;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.old.OldForgeUtil.Mask;

public final class GeneratorSurfaceDepth {

	private GeneratorSurfaceDepth(){}

	public static Mask<Integer> run(TokenMetadata metadata, Mask<Boolean> maskSurface){
		final Mask<Integer> maskSurfaceDepth = new Mask<>(0);

		ForgeUtil.forWorld((x, y) -> {
			if(maskSurface.get(x, y)) maskSurfaceDepth.set(x, y, 100);
			else maskSurfaceDepth.set(x, y, -100);
		});

		for(int i = 0; i < 20; i++){
			final Mask<Integer> maskSurfaceDepthNew = new Mask<>(0);
			ForgeUtil.forWorld((x, y) -> {
				if(maskSurface.get(x, y)){
					AtomicInteger surfaceDepthNew = new AtomicInteger(maskSurfaceDepth.get(x, y));
					ForgeUtil.forDirectNeighbors(x, y, (nx, ny) -> {
						surfaceDepthNew.set(Math.min(surfaceDepthNew.get(), !maskSurface.get(nx, ny) ? 1 : (maskSurfaceDepth.get(nx, ny) + 1)));
					});
					maskSurfaceDepthNew.set(x, y, surfaceDepthNew.get());
				}else{
					AtomicInteger surfaceDepthNew = new AtomicInteger(maskSurfaceDepth.get(x, y));
					ForgeUtil.forDirectNeighbors(x, y, (nx, ny) -> {
						surfaceDepthNew.set(Math.max(surfaceDepthNew.get(), maskSurface.get(nx, ny) ? -1 : (maskSurfaceDepth.get(nx, ny) - 1)));
					});
					maskSurfaceDepthNew.set(x, y, surfaceDepthNew.get());
				}
			});
			maskSurfaceDepth.set(maskSurfaceDepthNew);
		}

		return maskSurfaceDepth;
	}

}
