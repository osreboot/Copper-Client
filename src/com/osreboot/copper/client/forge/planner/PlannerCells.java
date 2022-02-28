package com.osreboot.copper.client.forge.planner;

import java.util.List;
import java.util.Random;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.forge.BlueprintAsteroid;
import com.osreboot.copper.client.forge.Cell;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;
import com.osreboot.copper.client.forge.cell.CellAsteroid;
import com.osreboot.copper.client.forge.cell.CellVoid;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class PlannerCells {

	private PlannerCells(){}
	
	public static Mask<Cell> run(TokenMetadata metadata, List<BlueprintAsteroid> blueprints){
		Random random = new Random(metadata.seedAsteroids.hashCode());
		
		Mask<Cell> worldCells = new Mask<>();
		
		ForgeUtil.forWorld((x, y) -> {
			HvlCoord cLocation = WorldUtil.toEntitySpace(new HvlCoord(x, y));
			
			float maxProbability = 0f;
			for(BlueprintAsteroid blueprint : blueprints)
				maxProbability = Math.max(maxProbability, mapToProbability(cLocation, blueprint));
			
			if(random.nextFloat() < maxProbability) worldCells.set(x, y, new CellAsteroid());
			else worldCells.set(x, y, new CellVoid());
		});
		
		return worldCells;
	}
	
	private static float mapToProbability(HvlCoord location, BlueprintAsteroid blueprint){
		return HvlMath.distance(location, blueprint.location) < blueprint.radius ? 1f : 0f;
	}
	
}