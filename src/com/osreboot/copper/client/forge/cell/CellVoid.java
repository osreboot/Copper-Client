package com.osreboot.copper.client.forge.cell;

import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.forge.Cell;
import com.osreboot.copper.client.forge.ForgeUtil.Mask;

public class CellVoid extends Cell{

	@Override
	public Cell advance(int phase, Mask<Cell> worldCells){
		return null;
	}

	@Override
	public FTileMaterial evaluate(){
		return null;
	}

}
