package com.osreboot.copper.client.environment;

import com.osreboot.copper.client.environment.system.SPlayer;
import com.osreboot.copper.client.environment.system.SRenderTemp;

public class EnvironmentSinglePlayer extends Environment{

	public EnvironmentSinglePlayer(Seed seedArg){
		super(seedArg);
		
		SPlayer.initialize(this);
		SRenderTemp.initialize(this);
	}

	@Override
	public void update(float delta){
		SPlayer.update(this, delta);
		SRenderTemp.update(this, delta);
	}

}
