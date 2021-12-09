package com.osreboot.copper.client.forge;

import java.util.HashMap;
import java.util.Map;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.forge.ambient.ProcessAmbientPlanet;
import com.osreboot.copper.client.forge.primary.ProcessPrimaryTerrain;

public final class Forge {

	private Forge(){}
	
	public static void run(TokenMetadata metadata, CTile[][] world){
		@SuppressWarnings("unchecked")
		Map<Tag, Double>[][] worldTags = new Map[world.length][world[0].length];
		WorldUtil.loop2D(world, (x, y, t) -> {
			worldTags[x][y] = new HashMap<>();
		});
		
		ProcessAmbientPlanet.run(metadata, world, worldTags);
		
		ProcessPrimaryTerrain.run(metadata, world, worldTags);
	}
	
}
