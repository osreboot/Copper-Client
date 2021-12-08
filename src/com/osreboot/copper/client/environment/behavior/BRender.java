package com.osreboot.copper.client.environment.behavior;

import com.osreboot.copper.client.environment.Entity;
import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.feature.FRenderChannel;

public abstract class BRender<T> extends Entity{
	private static final long serialVersionUID = 1L;

	protected final T parent;

	public BRender(Environment environmentArg, T parentArg){
		super(environmentArg, BRender.class);
		parent = parentArg;
	}

	public abstract void render(Environment environment, float delta, FRenderChannel channel);

}
