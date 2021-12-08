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
				world.tiles[x][y] = new CTile(x, y, FTileMaterial.PATHWAY);;
			}
		}
		
		createAsteroid(world.tiles, -100, -200, 120);
		createAsteroid(world.tiles, 40, 40, 40);
		createAsteroid(world.tiles, -50, 120, 60);
		createAsteroid(world.tiles, 90, -60, 20);
		createAsteroid(world.tiles, -30, -10, 25);
		createAsteroid(world.tiles, 10, -80, 10);
		
		for(int x = 0; x < EWorld.SIZE_X; x++){
			for(int y = 0; y < EWorld.SIZE_Y; y++){
				if(x <= 1 || x >= EWorld.SIZE_X - 2 || y <= 0 || y >= EWorld.SIZE_Y - 1)
					world.tiles[x][y].material = FTileMaterial.WORLD_BORDER;
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
	
	private void createAsteroid(CTile[][] tiles, float xAsteroid, float yAsteroid, float diameter){
		for(int x = 0; x < EWorld.SIZE_X; x++){
			for(int y = 0; y < EWorld.SIZE_Y; y++){
				CTile tile = tiles[x][y];
				if(HvlMath.distance(tile.vertices[0].x, tile.vertices[0].y, xAsteroid, yAsteroid) <= diameter ||
						HvlMath.distance(tile.vertices[1].x, tile.vertices[1].y, xAsteroid, yAsteroid) <= diameter ||
						HvlMath.distance(tile.vertices[2].x, tile.vertices[2].y, xAsteroid, yAsteroid) <= diameter){
					tile.material = FTileMaterial.ASTEROID;
				}
			}
		}
	}

}
