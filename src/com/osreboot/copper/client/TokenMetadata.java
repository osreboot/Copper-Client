package com.osreboot.copper.client;

import java.io.Serializable;

public final class TokenMetadata implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final TokenMetadata DEV = new TokenMetadata("dev",
			false, "dev", 1f);
	
	public String seedTerrain;
	
	public boolean hasVoids;
	public String seedVoids;
	public float scaleVoids;
	// TODO offsetVoids
	
	public TokenMetadata(String seedTerrainArg,
			boolean hasVoidsArg, String seedVoidsArg, float scaleVoidsArg){
		seedTerrain = seedTerrainArg;
		
		hasVoids = hasVoidsArg;
		seedVoids = seedVoidsArg;
		scaleVoids = scaleVoidsArg;
	}
	
}
