package com.osreboot.copper.client.forge.old.primary;

import java.util.Map;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.old.ForgeTag;
import com.osreboot.copper.client.forge.old.ForgeUtil;
import com.osreboot.copper.client.forge.old.util.Noise2DPerlin;
import com.osreboot.ridhvl2.HvlCoord;

public final class ProcessPrimaryTerrain {

	private ProcessPrimaryTerrain(){}

	public static void run(TokenMetadata metadata, CTile[][] world, Map<ForgeTag, Double>[][] worldTags){
		Random random = new Random(metadata.seedTerrain.hashCode());

		HvlCoord w0 = new HvlCoord(-EWorld.DIAMETER / 2f, -EWorld.DIAMETER / 2f);
		HvlCoord w1 = new HvlCoord(EWorld.DIAMETER / 2f, EWorld.DIAMETER / 2f);

		Noise2DPerlin nP1 = new Noise2DPerlin(random, 8, w0, w1);
		Noise2DPerlin nP2 = new Noise2DPerlin(random, 13, w0, w1);
		Noise2DPerlin nP3 = new Noise2DPerlin(random, 24, w0, w1);

		WorldUtil.loop2D(world, (x, y, t) -> {
			HvlCoord c = WorldUtil.toEntitySpace(new HvlCoord(x, y));
			float v = nP3.map(c) + nP2.map(c) / 2f + nP1.map(c) / 4f;

			//			world[x][y].debugColor = new Color(v, v, v);
			world[x][y].material = v > 0.3f ? FTileMaterial.ASTEROID : FTileMaterial.PATHWAY;
		});

		ForgeUtil.dilate(world, FTileMaterial.ASTEROID, FTileMaterial.PATHWAY);
		ForgeUtil.dilate(world, FTileMaterial.ASTEROID, FTileMaterial.PATHWAY);
		ForgeUtil.dilate(world, FTileMaterial.PATHWAY, FTileMaterial.ASTEROID);
		ForgeUtil.dilate(world, FTileMaterial.PATHWAY, FTileMaterial.ASTEROID);
		
		WorldUtil.loop2D(world, (x, y, t) -> {
			if(t.material == FTileMaterial.PATHWAY) world[x][y] = null;
		});
	}

}
