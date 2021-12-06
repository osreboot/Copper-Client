package com.osreboot.copper.client.environment.entity;

import com.osreboot.copper.client.environment.Entity;
import com.osreboot.copper.client.environment.Environment;
import com.osreboot.ridhvl2.HvlCoord;

public class EPlayer extends Entity{
	private static final long serialVersionUID = 1L;

	public HvlCoord location, speed;
	
	public EPlayer(Environment environmentArg){
		super(environmentArg, EPlayer.class);
		location = new HvlCoord();
		speed = new HvlCoord();
	}
	
}
