package com.osreboot.copper.client.environment;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.Forge;

public final class Seed {

	public static final Seed DEV = new Seed(TokenMetadata.DEV);

	public TokenMetadata tokenMetadata;

	public Seed(TokenMetadata tokenMetadataArg){
		tokenMetadata = tokenMetadataArg;
	}

	public void generate(Environment environment){
		EWorld world = new EWorld(environment);
		
		Forge.run(tokenMetadata, world.tiles);
		//OldForge.run(tokenMetadata, world.tiles);
		
		WorldUtil.loop2D(world.tiles, (x, y, t) -> {
			if(x <= 1 || x >= EWorld.SIZE_X - 2 || y <= 0 || y >= EWorld.SIZE_Y - 1)
				world.tiles[x][y] = new CTile(x, y, FTileMaterial.WORLD_BORDER);
		});

		// TODO this technically violates ECS structure
		WorldUtil.loop2D(world.tiles, (x, y, t) -> {
			if(world.tiles[x][y] != null)
				world.tiles[x][y].bakeFaces(world.tiles);
		});

		new EPlayer(environment);
	}

}
