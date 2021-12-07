package com.osreboot.copper.client.environment.behavior;

import com.osreboot.copper.client.environment.Entity;
import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.feature.FRenderChannel;

public abstract class BRender extends Entity{
	private static final long serialVersionUID = 1L;

	 public BRender(Environment environmentArg){
		 super(environmentArg, BRender.class);
	 }
	
	public abstract void render(Environment environment, float delta, FRenderChannel channel);

}
