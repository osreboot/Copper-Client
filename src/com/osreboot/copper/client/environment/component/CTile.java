package com.osreboot.copper.client.environment.component;

import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlCoord;

public class CTile {

	public final FTileMaterial material;
	
	public final FTileOrientation orientation;
	public final boolean faceVert, faceWest, faceEast;
	
	public final HvlCoord[] vertices;
	
	public CTile(int xArg, int yArg, FTileMaterial materialArg, boolean faceVertArg, boolean faceWestArg, boolean faceEastArg){
		material = materialArg;
		
		orientation = (xArg + yArg) % 2 == 0 ? FTileOrientation.UP_ARROW : FTileOrientation.DOWN_ARROW;
		faceVert = faceVertArg;
		faceWest = faceWestArg;
		faceEast = faceEastArg;
		
		HvlCoord origin = new HvlCoord();
		origin.x = (xArg - ((float)EWorld.SIZE_X / 2f)) * 0.5f + 0.25f;
		origin.y = (yArg - ((float)EWorld.SIZE_Y / 2f) + 0.5f) * EWorld.SCALE_Y;
		
		vertices = new HvlCoord[3];
		if(orientation == FTileOrientation.UP_ARROW){
			vertices[0] = new HvlCoord(origin.x, origin.y - EWorld.SCALE_Y / 2f);
			vertices[1] = new HvlCoord(origin.x + 0.5f, origin.y + EWorld.SCALE_Y / 2f);
			vertices[2] = new HvlCoord(origin.x - 0.5f, origin.y + EWorld.SCALE_Y / 2f);
		}else{
			vertices[0] = new HvlCoord(origin.x - 0.5f, origin.y - EWorld.SCALE_Y / 2f);
			vertices[1] = new HvlCoord(origin.x + 0.5f, origin.y - EWorld.SCALE_Y / 2f);
			vertices[2] = new HvlCoord(origin.x, origin.y + EWorld.SCALE_Y / 2f);
		}
	}
	
}
