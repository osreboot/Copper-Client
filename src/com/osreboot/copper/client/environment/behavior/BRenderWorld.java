package com.osreboot.copper.client.environment.behavior;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import org.lwjgl.opengl.Display;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FRenderChannel;
import com.osreboot.copper.client.environment.system.SRender;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;
import com.osreboot.ridhvl2.painter.HvlPolygon;

public class BRenderWorld extends BRender<EWorld>{
	private static final long serialVersionUID = 1L;

	public BRenderWorld(Environment environmentArg, EWorld parentArg){
		super(environmentArg, parentArg);
	}

	@Override
	public void render(Environment environment, float delta, FRenderChannel channel){
		if(channel == FRenderChannel.BASE_TERRAIN){
			EPlayer player = environment.getEntitySingleton(EPlayer.class);
			HvlCoord pMin = CTile.toTileSpace(new HvlCoord(player.location)
					.subtract((Display.getWidth() / 2f) / SRender.SCALE_WORLD + 2f, (Display.getHeight() / 2f) / SRender.SCALE_WORLD + 2f));
			HvlCoord pMax = CTile.toTileSpace(new HvlCoord(player.location)
					.add((Display.getWidth() / 2f) / SRender.SCALE_WORLD + 2f, (Display.getHeight() / 2f) / SRender.SCALE_WORLD + 2f));
			int xMin = (int)HvlMath.limit(Math.round(pMin.x), 0, parent.tiles.length - 1);
			int xMax = (int)HvlMath.limit(Math.round(pMax.x), 0, parent.tiles.length - 1);
			int yMin = (int)HvlMath.limit(Math.round(pMin.y), 0, parent.tiles[0].length - 1);
			int yMax = (int)HvlMath.limit(Math.round(pMax.y), 0, parent.tiles[0].length - 1);
			for(int x = xMin; x <= xMax; x++){
				for(int y = yMin; y <= yMax; y++){
					CTile tile = parent.tiles[x][y];
					if(tile != null){
						hvlDraw(new HvlPolygon(tile.vertices, tile.vertices), tile.material.color);
					}
				}
			}
		}
//		if(channel == FRenderChannel.BASE_ENTITY){
//			EWorld world = environment.getEntitySingleton(EWorld.class);
//			for(int x = 0; x < EWorld.SIZE_X; x++){
//				for(int y = 0; y < EWorld.SIZE_Y; y++){
//					CTile tile = world.tiles[x][y];
//					if(tile != null){
//						if(tile.faceVert) hvlDraw(hvlLine(tile.vertices[1], tile.vertices[2], 0.05f), Color.red);
//						if(tile.faceEast) hvlDraw(hvlLine(tile.vertices[0], tile.vertices[1], 0.05f), Color.red);
//						if(tile.faceWest) hvlDraw(hvlLine(tile.vertices[2], tile.vertices[0], 0.05f), Color.red);
//					}
//				}
//			}
//		}
	}

}
