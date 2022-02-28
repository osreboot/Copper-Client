package com.osreboot.copper.client.forge;

import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public abstract class Cell {
	
	public abstract Cell advance(int phase, Mask<Cell> worldCells);
	
	public abstract FTileMaterial evaluate();
	
}
