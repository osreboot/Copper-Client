package com.osreboot.copper.client.environment.entity;

import com.osreboot.copper.client.environment.Entity;
import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.behavior.BRenderWorld;
import com.osreboot.copper.client.environment.component.CTile;

public class EWorld extends Entity{
	private static final long serialVersionUID = 1L;

	public static final float
	SCALE_Y = 0.866025403784f;
	
	public static final int
	DIAMETER = 512,
	SIZE_X = DIAMETER * 2,
	SIZE_Y = Math.round((float)DIAMETER / SCALE_Y);

	public CTile[][] tiles;
	
	public BRenderWorld render;

	// Default constructor for serialization
	protected EWorld(){}

	public EWorld(Environment environmentArg){
		super(environmentArg, EWorld.class);
		tiles = new CTile[SIZE_X][SIZE_Y];
		
		render = new BRenderWorld(environmentArg, this);
	}
	
	@Override
	public void destroy(){
		super.destroy();
		render.destroy();
	}

}
