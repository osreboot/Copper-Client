package com.osreboot.copper.client.environment;

import java.io.Serializable;

public abstract class Entity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private transient Environment environment;
	
	private Class<? extends Entity> type;
	
	// Default constructor for serialization
	protected Entity(){}

	@SuppressWarnings("unchecked")
	public <E extends Entity> Entity(Environment environmentArg, Class<E> typeArg){
		environment = environmentArg;
		type = typeArg;
		environment.addEntity(typeArg, (E)this);
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entity> void destroy(){
		Class<E> typeArg = (Class<E>)type;
		environment.removeEntity(typeArg, typeArg.cast(this));
	}
	
}
