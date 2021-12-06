package com.osreboot.copper.client;

import java.io.Serializable;

public final class TokenMetadata implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final TokenMetadata DEV = new TokenMetadata("dev");
	
	public String seedTerrain;
	
	public TokenMetadata(String seedTerrainArg){
		seedTerrain = seedTerrainArg;
	}
	
}
