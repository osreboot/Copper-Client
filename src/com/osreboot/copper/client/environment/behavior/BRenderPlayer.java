package com.osreboot.copper.client.environment.behavior;

import static com.osreboot.ridhvl2.HvlStatics.hvlCirclec;
import static com.osreboot.ridhvl2.HvlStatics.hvlDraw;

import org.newdawn.slick.Color;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.feature.FRenderChannel;

public class BRenderPlayer extends BRender<EPlayer>{
	private static final long serialVersionUID = 1L;

	public BRenderPlayer(Environment environmentArg, EPlayer parentArg){
		super(environmentArg, parentArg);
	}

	@Override
	public void render(Environment environment, float delta, FRenderChannel channel){
		if(channel == FRenderChannel.BASE_ENTITY){
			hvlDraw(hvlCirclec(parent.location.x, parent.location.y, EPlayer.RADIUS, 20), Color.blue);
		}
	}

}
