package com.osreboot.copper.client.environment.entity;

import com.osreboot.copper.client.environment.Entity;
import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.behavior.BRenderPlayer;
import com.osreboot.ridhvl2.HvlCoord;

public class EPlayer extends Entity{
	private static final long serialVersionUID = 1L;

	public static final float
	RADIUS = 0.8f;
	
	public HvlCoord location, speed;
	
	public BRenderPlayer render;
	
	public EPlayer(Environment environmentArg){
		super(environmentArg, EPlayer.class);
		location = new HvlCoord();
		speed = new HvlCoord();
		
		render = new BRenderPlayer(environmentArg, this);
	}
	
	@Override
	public void destroy(){
		super.destroy();
		render.destroy();
	}
	
}
