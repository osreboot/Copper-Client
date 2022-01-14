package com.osreboot.copper.client.forge.old;

import java.util.HashMap;
import java.util.Map;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.forge.old.primary.ProcessPrimaryDamageAsteroids;
import com.osreboot.copper.client.forge.old.primary.ProcessPrimaryGrowAsteroids;
import com.osreboot.copper.client.forge.old.primary.ProcessPrimarySeedAsteroids;

public final class OldForge {

	private OldForge(){}
	
	public static void run(TokenMetadata metadata, CTile[][] world){
		@SuppressWarnings("unchecked")
		Map<ForgeTag, Double>[][] worldTags = new Map[world.length][world[0].length];
		WorldUtil.loop2D(world, (x, y, t) -> {
			worldTags[x][y] = new HashMap<>();
		});
		
		ProcessPrimarySeedAsteroids.run(metadata, world, worldTags);
		ProcessPrimaryGrowAsteroids.run(metadata, world, worldTags);
		ProcessPrimaryDamageAsteroids.run(metadata, world, worldTags);
	}
	
}
