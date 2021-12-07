package com.osreboot.copper.client.environment.component;

import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlCoord;

public class CTile {

	public FTileMaterial material;

	public final int x, y;
	public final HvlCoord origin;

	public final FTileOrientation orientation;
	public boolean faceVert, faceWest, faceEast;

	public final HvlCoord[] vertices;

	public CTile(int xArg, int yArg, FTileMaterial materialArg){
		material = materialArg;

		x = xArg;
		y = yArg;

		origin = new HvlCoord();
		origin.x = (x - ((float)EWorld.SIZE_X / 2f)) * 0.5f + 0.25f;
		origin.y = (y - ((float)EWorld.SIZE_Y / 2f) + 0.5f) * EWorld.SCALE_Y;
		
		orientation = (x + y) % 2 == 0 ? FTileOrientation.UP_ARROW : FTileOrientation.DOWN_ARROW;

		faceVert = false;
		faceWest = false;
		faceEast = false;
		
		vertices = new HvlCoord[3];
		if(orientation == FTileOrientation.UP_ARROW){
			vertices[0] = new HvlCoord(origin.x, origin.y - EWorld.SCALE_Y / 2f);
			vertices[1] = new HvlCoord(origin.x + 0.5f, origin.y + EWorld.SCALE_Y / 2f);
			vertices[2] = new HvlCoord(origin.x - 0.5f, origin.y + EWorld.SCALE_Y / 2f);
		}else{
			vertices[0] = new HvlCoord(origin.x, origin.y + EWorld.SCALE_Y / 2f);
			vertices[1] = new HvlCoord(origin.x + 0.5f, origin.y - EWorld.SCALE_Y / 2f);
			vertices[2] = new HvlCoord(origin.x - 0.5f, origin.y - EWorld.SCALE_Y / 2f);
		}
	}

	// TODO this technically violates ECS structure
	public void bakeFaces(CTile[][] tiles){
		if(material.solid){
			if(orientation == FTileOrientation.UP_ARROW){
				faceVert = y + 1 < tiles[x].length && !tiles[x][y + 1].material.solid;
			}else{
				faceVert = y - 1 > 0 && !tiles[x][y - 1].material.solid;
			}
			faceWest = x - 1 > 0 && !tiles[x - 1][y].material.solid;
			faceEast = x + 1 < tiles.length && !tiles[x + 1][y].material.solid;
		}
	}

}
