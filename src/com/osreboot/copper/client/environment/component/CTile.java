package com.osreboot.copper.client.environment.component;

import org.newdawn.slick.Color;

import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.painter.HvlPolygon;

public class CTile {

	public FTileMaterial material;

	public final int x, y;
	public final HvlCoord origin;

	public final FTileOrientation orientation;
	public boolean faceVert, faceWest, faceEast;

	public final HvlCoord[] vertices;
	public final HvlPolygon polygon;

	public Color debugColor;
	
	public CTile(int xArg, int yArg, FTileMaterial materialArg){
		material = materialArg;

		x = xArg;
		y = yArg;

		origin = WorldUtil.toEntitySpace(new HvlCoord(x, y));

		orientation = WorldUtil.getOrientation(x, y);

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
		
		polygon = new HvlPolygon(vertices, vertices);
	}

	// TODO this technically violates ECS structure
	public void bakeFaces(CTile[][] tiles){
		if(material.solid){
			if(orientation == FTileOrientation.UP_ARROW){
				faceVert = y + 1 < tiles[x].length && (tiles[x][y + 1] == null || !tiles[x][y + 1].material.solid);
			}else{
				faceVert = y - 1 > 0 && (tiles[x][y - 1] == null || !tiles[x][y - 1].material.solid);
			}
			faceWest = x - 1 > 0 && (tiles[x - 1][y] == null || !tiles[x - 1][y].material.solid);
			faceEast = x + 1 < tiles.length && (tiles[x + 1][y] == null || !tiles[x + 1][y].material.solid);
		}
	}

}
