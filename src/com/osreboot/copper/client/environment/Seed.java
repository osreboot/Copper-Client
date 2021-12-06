package com.osreboot.copper.client.environment;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;

public final class Seed {

	public static final Seed DEV = new Seed(TokenMetadata.DEV);

	public TokenMetadata tokenMetadata;

	public Seed(TokenMetadata tokenMetadataArg){
		tokenMetadata = tokenMetadataArg;
	}

	public void generate(Environment environment){
		EWorld world = new EWorld(environment);
		for(int x = 0; x < EWorld.SIZE_X; x++){
			for(int y = 0; y < EWorld.SIZE_Y; y++){
				world.tiles[x][y] = new CTile(x, y, FTileMaterial.ASTEROID, false, false, false);
			}
		}

		new EPlayer(environment);
	}

}
