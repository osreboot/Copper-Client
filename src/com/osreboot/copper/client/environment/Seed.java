package com.osreboot.copper.client.environment;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.ridhvl2.HvlMath;

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
				CTile tile = new CTile(x, y, FTileMaterial.PATHWAY);
				tile.material = 
						HvlMath.distance(tile.vertices[0].x + 0.25f, tile.vertices[0].y, 0, 0) <= EWorld.DIAMETER / 2.1f ||
						HvlMath.distance(tile.vertices[1].x + 0.25f, tile.vertices[1].y, 0, 0) <= EWorld.DIAMETER / 2.1f ||
						HvlMath.distance(tile.vertices[2].x + 0.25f, tile.vertices[2].y, 0, 0) <= EWorld.DIAMETER / 2.1f ?
								FTileMaterial.ASTEROID : FTileMaterial.PATHWAY;
				world.tiles[x][y] = tile;
			}
		}

		// TODO this technically violates ECS structure
		for(int x = 0; x < EWorld.SIZE_X; x++){
			for(int y = 0; y < EWorld.SIZE_Y; y++){
				world.tiles[x][y].bakeFaces(world.tiles);
			}
		}

		new EPlayer(environment);
	}

}
