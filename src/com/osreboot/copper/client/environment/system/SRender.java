package com.osreboot.copper.client.environment.system;

import static com.osreboot.ridhvl2.HvlStatics.hvlScale;
import static com.osreboot.ridhvl2.HvlStatics.hvlTranslate;

import org.lwjgl.opengl.Display;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.behavior.BRender;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.feature.FRenderChannel;

public final class SRender {

	private SRender(){}

	public static final float
	SCALE_WORLD = 16f;
//	SCALE_WORLD = 3f;

	public static void initialize(Environment environment){

	}

	public static void update(Environment environment, float delta){
		hvlTranslate(Display.getWidth()/2, Display.getHeight()/2, () -> {
			hvlScale(0f, 0f, SCALE_WORLD, () -> {
				EPlayer player = environment.getEntitySingleton(EPlayer.class);
				hvlTranslate(-player.location.x, -player.location.y, () -> {
					environment.getEntities(BRender.class).forEach(r -> r.render(environment, delta, FRenderChannel.BASE_TERRAIN));
					
					environment.getEntities(BRender.class).forEach(r -> r.render(environment, delta, FRenderChannel.BASE_ENTITY));
				});
			});
		});
	}

}
