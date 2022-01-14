package com.osreboot.copper.client.forge;

import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlAction;

public final class ForgeUtil {
	
	private ForgeUtil(){}
	
	public static boolean isInWorld(int x, int y){
		return x >= 0 && x < EWorld.SIZE_X && y >= 0 && y < EWorld.SIZE_Y;
	}
	
	public static void forWorld(HvlAction.A2<Integer, Integer> action){
		for(int x = 0; x < EWorld.SIZE_X; x++){
			for(int y = 0; y < EWorld.SIZE_Y; y++){
				action.run(x, y);
			}
		}
	}
	
	public static void forDirectNeighbors(int x, int y, HvlAction.A2<Integer, Integer> action){
		if(WorldUtil.getOrientation(x, y) == FTileOrientation.UP_ARROW){
			if(isInWorld(x, y + 1)) action.run(x, y + 1);
		}else{
			if(isInWorld(x, y - 1)) action.run(x, y - 1);
		}
		if(isInWorld(x - 1, y)) action.run(x - 1, y);
		if(isInWorld(x + 1, y)) action.run(x + 1, y);
	}
	
	public static void forIndirectNeighbors(int x, int y, HvlAction.A2<Integer, Integer> action){
		if(WorldUtil.getOrientation(x, y) == FTileOrientation.UP_ARROW){
			if(isInWorld(x - 1, y - 1)) 	action.run(x - 1, 	y - 1);
			if(isInWorld(x, 	y - 1)) 	action.run(x, 		y - 1);
			if(isInWorld(x + 1, y - 1)) 	action.run(x + 1, 	y - 1);
			if(isInWorld(x - 2, y + 1)) 	action.run(x - 2, 	y + 1);
			if(isInWorld(x - 1, y + 1)) 	action.run(x - 1, 	y + 1);
			if(isInWorld(x, 	y + 1)) 	action.run(x, 		y + 1);
			if(isInWorld(x + 1, y + 1)) 	action.run(x + 1, 	y + 1);
			if(isInWorld(x + 2, y + 1)) 	action.run(x + 2, 	y + 1);
		}else{
			if(isInWorld(x - 1, y + 1)) 	action.run(x - 1, 	y + 1);
			if(isInWorld(x, 	y + 1)) 	action.run(x, 		y + 1);
			if(isInWorld(x + 1, y + 1)) 	action.run(x + 1, 	y + 1);
			if(isInWorld(x - 2, y - 1)) 	action.run(x - 2, 	y - 1);
			if(isInWorld(x - 1, y - 1)) 	action.run(x - 1, 	y - 1);
			if(isInWorld(x, 	y - 1)) 	action.run(x, 		y - 1);
			if(isInWorld(x + 1, y - 1)) 	action.run(x + 1, 	y - 1);
			if(isInWorld(x + 2, y - 1)) 	action.run(x + 2, 	y - 1);
		}
		if(isInWorld(x - 2, y)) action.run(x - 2, y);
		if(isInWorld(x - 1, y)) action.run(x - 1, y);
		if(isInWorld(x + 1, y)) action.run(x + 1, y);
		if(isInWorld(x + 2, y)) action.run(x + 2, y);
	}
	
	public static class Mask<T>{
		
		private T[][] values;
		
		@SuppressWarnings("unchecked")
		public Mask(T initialValue){
			values = (T[][])new Object[EWorld.SIZE_X][EWorld.SIZE_Y];
			forWorld((x, y) -> {
				values[x][y] = initialValue;
			});
		}
		
		public Mask(){
			this(null);
		}
		
		public void set(int x, int y, T value){
			values[x][y] = value;
		}
		
		public T get(int x, int y){
			return values[x][y];
		}
		
		public void set(Mask<T> mask){
			values = mask.values;
		}
		
	}

}
