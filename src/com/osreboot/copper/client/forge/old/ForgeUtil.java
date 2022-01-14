package com.osreboot.copper.client.forge.old;

import java.util.ArrayList;

import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.feature.FTileMaterial;
import com.osreboot.copper.client.environment.feature.FTileOrientation;
import com.osreboot.ridhvl2.HvlAction;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class ForgeUtil {

	private ForgeUtil(){}
	
	public static final float
	BUFFER_RADIUS_AABB = 2f;

	// TODO support dilation to null
	public static void dilate(CTile[][] world, FTileMaterial mFrom, FTileMaterial mTo){
		ArrayList<HvlCoord> toChange = new ArrayList<>();
		WorldUtil.loop2D(world, (x, y, t) -> {
			if(t != null){
				if(t.material == mTo){
					if(t.orientation == FTileOrientation.UP_ARROW){
						if(isInBounds(world, x, y - 1)) toChange.add(new HvlCoord(x, y - 1));
						if(isInBounds(world, x - 1, y - 1)) toChange.add(new HvlCoord(x - 1, y - 1));
						if(isInBounds(world, x + 1, y - 1)) toChange.add(new HvlCoord(x + 1, y - 1));
						if(isInBounds(world, x - 1, y)) toChange.add(new HvlCoord(x - 1, y));
						if(isInBounds(world, x - 2, y)) toChange.add(new HvlCoord(x - 2, y));
						if(isInBounds(world, x + 1, y)) toChange.add(new HvlCoord(x + 1, y));
						if(isInBounds(world, x + 2, y)) toChange.add(new HvlCoord(x + 2, y));
						if(isInBounds(world, x, y + 1)) toChange.add(new HvlCoord(x, y + 1));
						if(isInBounds(world, x - 1, y + 1)) toChange.add(new HvlCoord(x - 1, y + 1));
						if(isInBounds(world, x - 2, y + 1)) toChange.add(new HvlCoord(x - 2, y + 1));
						if(isInBounds(world, x + 1, y + 1)) toChange.add(new HvlCoord(x + 1, y + 1));
						if(isInBounds(world, x + 2, y + 1)) toChange.add(new HvlCoord(x + 2, y + 1));
					}else{
						if(isInBounds(world, x, y + 1)) toChange.add(new HvlCoord(x, y + 1));
						if(isInBounds(world, x - 1, y + 1)) toChange.add(new HvlCoord(x - 1, y + 1));
						if(isInBounds(world, x + 1, y + 1)) toChange.add(new HvlCoord(x + 1, y + 1));
						if(isInBounds(world, x - 1, y)) toChange.add(new HvlCoord(x - 1, y));
						if(isInBounds(world, x - 2, y)) toChange.add(new HvlCoord(x - 2, y));
						if(isInBounds(world, x + 1, y)) toChange.add(new HvlCoord(x + 1, y));
						if(isInBounds(world, x + 2, y)) toChange.add(new HvlCoord(x + 2, y));
						if(isInBounds(world, x, y - 1)) toChange.add(new HvlCoord(x, y - 1));
						if(isInBounds(world, x - 1, y - 1)) toChange.add(new HvlCoord(x - 1, y - 1));
						if(isInBounds(world, x - 2, y - 1)) toChange.add(new HvlCoord(x - 2, y - 1));
						if(isInBounds(world, x + 1, y - 1)) toChange.add(new HvlCoord(x + 1, y - 1));
						if(isInBounds(world, x + 2, y - 1)) toChange.add(new HvlCoord(x + 2, y - 1));
					}
				}
			}
		});
		for(HvlCoord c : toChange){
			if(world[(int)c.x][(int)c.y] != null){
				if(world[(int)c.x][(int)c.y].material == mFrom) world[(int)c.x][(int)c.y].material = mTo;
			}else if(mFrom == null){
				world[(int)c.x][(int)c.y] = new CTile((int)c.x, (int)c.y, mTo);
			}
		}
	}

	public static boolean isInBounds(CTile[][] world, int x, int y){
		return x >= 0 && x <= world.length - 1 && y >= 0 && y <= world[0].length - 1;
	}
	
	public static boolean isEmpty(CTile[][] world, int x, int y){
		return !ForgeUtil.isInBounds(world, x, y) || world[x][y] == null || !world[x][y].material.solid;
	}

	public static void smartSmooth(CTile[][] world, int xStart, int yStart, int xEnd, int yEnd, FTileMaterial materialTo){
		for(int i = 0; i < 3; i++){
			int[][] worldDelta = new int[world.length][world[0].length];
			for(int x = xStart; x <= xEnd; x++){
				for(int y = yStart; y <= yEnd; y++){
					if(isInBounds(world, x, y) && world[x][y] != null){
						int emptySides = 0;
						if(world[x][y].orientation == FTileOrientation.UP_ARROW){
							if(isEmpty(world, x, y + 1)) emptySides++;
						}else{
							if(isEmpty(world, x, y - 1)) emptySides++;
						}
						if(isEmpty(world, x - 1, y)) emptySides++;
						if(isEmpty(world, x + 1, y)) emptySides++;
						if(emptySides >= 2) worldDelta[x][y] = -1;
					}
				}
			}
			
			for(int x = xStart; x <= xEnd; x++){
				for(int y = yStart; y <= yEnd; y++){
					if(isInBounds(world, x, y) && worldDelta[x][y] == -1){
						if(materialTo == null) world[x][y] = null;
						else world[x][y] = new CTile(x, y, materialTo);
					}
				}
			}
		}
	}
	
	public static void smartSmooth(CTile[][] world, int xStart, int yStart, int xEnd, int yEnd){
		smartSmooth(world, xStart, yStart, xEnd, yEnd, null);
	}

	// Algorithm source: https://en.wikipedia.org/wiki/Smoothstep | https://eev.ee/blog/2016/05/29/perlin-noise/ | http://adrianb.io/2014/08/09/perlinnoise.html
	public static float smoothstep(float x){
		return 6f * (float)Math.pow(x, 5f) - 15f * (float)Math.pow(x, 4f) + 10f * (float)Math.pow(x, 3f);
	}

	public static class Ellipse{

		public HvlCoord location;
		public float radius, ratio, rotation;

		public Ellipse(HvlCoord locationArg, float radiusArg, float ratioArg, float rotationArg){
			location = locationArg;
			radius = radiusArg;
			ratio = ratioArg;
			rotation = rotationArg;
		}

		public float getRadius(float angle){
			float a = radius;
			float b = ratio * radius;
			return (a * b) / (float)Math.sqrt(
					a * a * (float)Math.sin(angle) * (float)Math.sin(angle) +
					b * b * (float)Math.cos(angle) * (float)Math.cos(angle));
		}

		public boolean isInside(HvlCoord locationArg){
			float localAngle = HvlMath.toRadians(new HvlCoord(locationArg).subtract(location).angle() - rotation);
			return HvlMath.distance(location, locationArg) < getRadius(localAngle);
		}

	}
	
	public static class AsteroidAABB{

		public final int xMin, yMin, xMax, yMax;

		public AsteroidAABB(int xMinArg, int yMinArg, int xMaxArg, int yMaxArg){
			xMin = xMinArg;
			yMin = yMinArg;
			xMax = xMaxArg;
			yMax = yMaxArg;
		}

		public void loop(CTile[][] world, HvlAction.A3<Integer, Integer, CTile> action){
			for(int x = xMin; x <= xMax; x++){
				for(int y = yMin; y <= yMax; y++){
					if(ForgeUtil.isInBounds(world, x, y)) action.run(x, y, world[x][y]);
				}
			}
		}

	}

}
