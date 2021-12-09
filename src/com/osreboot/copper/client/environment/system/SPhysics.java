package com.osreboot.copper.client.environment.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.environment.component.CTile;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.copper.client.environment.entity.EWorld;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class SPhysics {

	private SPhysics(){}

	public static final float
	CALC_RANGE = 5f,
	VELOCITY_COEF = 0.5f;

	public static void initialize(Environment environment){

	}

	public static void update(Environment environment, float delta){
		EWorld world = environment.getEntitySingleton(EWorld.class);
		for(EPlayer player : environment.getEntities(EPlayer.class)){
			ArrayList<Collider> colliders = new ArrayList<>();

			// Find all colliders in range
			HvlCoord pMin = WorldUtil.toTileSpace(new HvlCoord(player.location).subtract(CALC_RANGE, CALC_RANGE));
			HvlCoord pMax = WorldUtil.toTileSpace(new HvlCoord(player.location).add(CALC_RANGE, CALC_RANGE));
			int xMin = (int)HvlMath.limit(Math.round(pMin.x), 0, world.tiles.length - 1);
			int xMax = (int)HvlMath.limit(Math.round(pMax.x), 0, world.tiles.length - 1);
			int yMin = (int)HvlMath.limit(Math.round(pMin.y), 0, world.tiles[0].length - 1);
			int yMax = (int)HvlMath.limit(Math.round(pMax.y), 0, world.tiles[0].length - 1);
			for(int x = xMin; x <= xMax; x++){
				for(int y = yMin; y <= yMax; y++){
					CTile tile = world.tiles[x][y];
					if(tile != null){
						if(tile.faceVert) colliders.add(new Collider(tile.vertices[1], tile.vertices[2]));
						if(tile.faceEast) colliders.add(new Collider(tile.vertices[0], tile.vertices[1]));
						if(tile.faceWest) colliders.add(new Collider(tile.vertices[2], tile.vertices[0]));
					}
				}
			}

			// Sort colliders by distance
			for(Collider collider : colliders){
				HvlCoord c0 = collider.c0;
				HvlCoord c1 = collider.c1;

				float t = dot(new HvlCoord(player.location).subtract(c0), new HvlCoord(c1).subtract(c0)) / (float)Math.pow(HvlMath.distance(c0, c1), 2);
				t = HvlMath.limit(t, 0f, 1f);
				HvlCoord projection = new HvlCoord(c1).subtract(c0).multiply(t).add(c0);

				collider.pDistance = HvlMath.distance(player.location, projection);
			}

			Collections.sort(colliders, new Comparator<Collider>(){
				@Override
				public int compare(Collider c0, Collider c1){
					return (int)Math.copySign(1f, c0.pDistance - c1.pDistance);
				}
			});

			// Handle collision for all colliders
			for(Collider collider : colliders){
				HvlCoord c0 = collider.c0;
				HvlCoord c1 = collider.c1;

				float t = dot(new HvlCoord(player.location).subtract(c0), new HvlCoord(c1).subtract(c0)) / (float)Math.pow(HvlMath.distance(c0, c1), 2);
				t = HvlMath.limit(t, 0f, 1f);
				HvlCoord projection = new HvlCoord(c1).subtract(c0).multiply(t).add(c0);

				// Detect collision
				if(HvlMath.distance(player.location, projection) < EPlayer.RADIUS){
					// Resolve overlap
					HvlCoord contactNormal = new HvlCoord(projection).subtract(player.location).normalize().fixNaN();
					float contactDistanceC0 = HvlMath.distance(player.location, projection) * (EPlayer.RADIUS / (EPlayer.RADIUS));
					HvlCoord contactPos = new HvlCoord(contactNormal).multiply(contactDistanceC0).add(player.location);
					player.location = new HvlCoord(contactNormal).multiply(-1f * EPlayer.RADIUS).add(contactPos);

					// Adjust speed
					float p = (1f + VELOCITY_COEF) * (player.speed.x * contactNormal.x + player.speed.y * contactNormal.y);
					player.speed.x = player.speed.x - p * contactNormal.x;
					player.speed.y = player.speed.y - p * contactNormal.y;
				}
			}

			// Adjust location
			player.location.add(new HvlCoord(player.speed).multiply(delta));
		}
	}

	//TODO add this to Ridhvl2
	private static float dot(HvlCoord c0, HvlCoord c1){
		return c0.x * c1.x + c0.y * c1.y;
	}

	private static class Collider{

		private HvlCoord c0, c1;
		private float pDistance;

		private Collider(HvlCoord c0Arg, HvlCoord c1Arg){
			c0 = c0Arg;
			c1 = c1Arg;
		}

	}

}
