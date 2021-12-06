package com.osreboot.copper.client.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Environment {
	
	private HashMap<Class<? extends Entity>, List<Entity>> entities;
	
	public Environment(Seed seedArg){
		entities = new HashMap<>();
		
		if(seedArg != null) seedArg.generate(this);
	}
	
	public abstract void update(float delta);
	
	public <E extends Entity> void addEntity(Class<E> typeArg, E entityArg){
		entities.putIfAbsent(typeArg, new ArrayList<>());
		entities.get(typeArg).add(entityArg);
	}
	
	public <E extends Entity> void removeEntity(Class<E> typeArg, E entityArg){
		if(entities.containsKey(typeArg)){
			entities.get(typeArg).remove(entityArg);
			if(entities.get(typeArg).size() == 0) entities.remove(typeArg);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entity> E getEntitySingleton(Class<E> typeArg){
		if(entities.containsKey(typeArg) && entities.get(typeArg).size() == 1)
			return (E)entities.get(typeArg).get(0);
		else throw new RuntimeException();
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entity> List<E> getEntities(Class<E> typeArg){
		if(entities.containsKey(typeArg))
			return (List<E>)entities.get(typeArg);
		else throw new RuntimeException();
	}
	
}
