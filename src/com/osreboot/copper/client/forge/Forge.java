package com.osreboot.copper.client.forge;

import java.util.List;

import com.osreboot.copper.client.TokenMetadata;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;
import com.osreboot.copper.client.forge.planner.PlannerAsteroids;
import com.osreboot.copper.client.forge.planner.PlannerCells;
import com.osreboot.copper.client.forge.planner.PlannerVoids;

public final class Forge {

	private Forge(){}
	
	public static void run(TokenMetadata metadata, CTile[][] world){
		List<BlueprintAsteroid> blueprints = PlannerAsteroids.run(metadata);

		PlannerVoids.run(metadata, blueprints);
		
		Mask<Cell> worldCells = PlannerCells.run(metadata, blueprints);
		
		for(int i = 0; i < 3; i++){
			Mask<Cell> worldCellsNext = new Mask<>();
			ForgeUtil.forWorld((x, y) -> {
				worldCellsNext.set(x, y, worldCells.get(x, y).advance(0, worldCells));
			});
			worldCells.set(worldCellsNext);
		}
		
		ForgeUtil.forWorld((x, y) -> {
			FTileMaterial material = worldCells.get(x, y).evaluate();
			if(material != null) world[x][y] = new CTile(x, y, material);
		});
	}
	
}
