package com.osreboot.copper.client.environment;

import com.osreboot.copper.client.environment.system.SPhysics;
import com.osreboot.copper.client.environment.system.SPlayer;
import com.osreboot.copper.client.environment.system.SRender;

public class EnvironmentSinglePlayer extends Environment{

	public EnvironmentSinglePlayer(Seed seedArg){
		super(seedArg);
		
		SPlayer.initialize(this);
		SPhysics.initialize(this);
		SRender.initialize(this);
	}

	@Override
	public void update(float delta){
		SPlayer.update(this, delta);
		SPhysics.update(this, delta);
		SRender.update(this, delta);
	}

}
