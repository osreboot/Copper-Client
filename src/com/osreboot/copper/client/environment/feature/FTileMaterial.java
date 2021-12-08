package com.osreboot.copper.client.environment.feature;

import org.newdawn.slick.Color;

public enum FTileMaterial {

	PATHWAY(false, Color.darkGray),
	ASTEROID(true, Color.gray),
	WORLD_BORDER(true, Color.blue);
	
	public final boolean solid;
	public final Color color;
	
	private FTileMaterial(boolean solidArg, Color colorArg){
		solid = solidArg;
		color = colorArg;
	}
	
}
