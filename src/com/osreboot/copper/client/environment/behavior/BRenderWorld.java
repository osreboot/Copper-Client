package com.osreboot.copper.client.environment.behavior;

import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

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
	}

}
