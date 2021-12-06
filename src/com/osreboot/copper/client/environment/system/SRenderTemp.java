package com.osreboot.copper.client.environment.system;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;
import static com.osreboot.ridhvl2.HvlStatics.hvlScale;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.ridhvl2.painter.HvlPolygon;

public final class SRenderTemp {

	private SRenderTemp(){}

	public static final float
	SCALE_WORLD = 32f;

	public static void initialize(Environment environment){

	}

	public static void update(Environment environment, float delta){
		hvlTranslate(Display.getWidth()/2, Display.getHeight()/2, () -> {
			hvlScale(0f, 0f, SCALE_WORLD, () -> {
				EPlayer player = environment.getEntitySingleton(EPlayer.class);
				hvlTranslate(-player.location.x, -player.location.y, () -> {
					EWorld world = environment.getEntitySingleton(EWorld.class);
					for(int x = 0; x < EWorld.SIZE_X; x++){
						for(int y = 0; y < EWorld.SIZE_Y; y++){
							CTile tile = world.tiles[x][y];
							if(tile != null){
								hvlDraw(new HvlPolygon(tile.vertices, tile.vertices), tile.material.color);
							}
						}
					}

					hvlDraw(hvlCirclec(player.location.x, player.location.y, 0.8f), Color.blue);
				});
			});
		});
	}

}
