package com.osreboot.copper.client.environment.feature;

import org.newdawn.slick.Color;

public enum FTileMaterial {

	ASTEROID(Color.gray);
	
	// TODO
	public final Color color;
	
	private FTileMaterial(Color colorArg){
		color = colorArg;
	}
	
}
