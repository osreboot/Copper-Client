package com.osreboot.copper.client.environment.behavior;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlLine;

import org.newdawn.slick.Color;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FRenderChannel;
import com.osreboot.ridhvl2.painter.HvlPolygon;

public class BRenderWorld extends BRender{
	private static final long serialVersionUID = 1L;

	public BRenderWorld(Environment environmentArg){
		super(environmentArg);
	}

	@Override
	public void render(Environment environment, float delta, FRenderChannel channel){
		if(channel == FRenderChannel.BASE_TERRAIN){
			EWorld world = environment.getEntitySingleton(EWorld.class);
			for(int x = 0; x < EWorld.SIZE_X; x++){
				for(int y = 0; y < EWorld.SIZE_Y; y++){
					CTile tile = world.tiles[x][y];
					if(tile != null){
						hvlDraw(new HvlPolygon(tile.vertices, tile.vertices), tile.material.color);
					}
				}
			}
		}
		if(channel == FRenderChannel.BASE_ENTITY){
			EWorld world = environment.getEntitySingleton(EWorld.class);
			for(int x = 0; x < EWorld.SIZE_X; x++){
				for(int y = 0; y < EWorld.SIZE_Y; y++){
					CTile tile = world.tiles[x][y];
					if(tile != null){
						if(tile.faceVert) hvlDraw(hvlLine(tile.vertices[1], tile.vertices[2], 0.05f), Color.red);
						if(tile.faceEast) hvlDraw(hvlLine(tile.vertices[0], tile.vertices[1], 0.05f), Color.red);
						if(tile.faceWest) hvlDraw(hvlLine(tile.vertices[2], tile.vertices[0], 0.05f), Color.red);
					}
				}
			}
		}
	}

}
