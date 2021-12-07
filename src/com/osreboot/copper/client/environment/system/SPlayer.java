package com.osreboot.copper.client.environment.system;

import org.lwjgl.input.Keyboard;

import com.osreboot.copper.client.environment.Environment;
import com.osreboot.copper.client.environment.entity.EPlayer;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public final class SPlayer {

	private SPlayer(){}
	
	public static final float
	ACCELERATION = 7f;
	
	public static void initialize(Environment environment){
		
	}
	
	public static void update(Environment environment, float delta){
		EPlayer player = environment.getEntitySingleton(EPlayer.class);
		
		HvlCoord playerImpulse = new HvlCoord();
		playerImpulse.x -= Keyboard.isKeyDown(Keyboard.KEY_A) ? 1f : 0f;
		playerImpulse.x += Keyboard.isKeyDown(Keyboard.KEY_D) ? 1f : 0f;
		playerImpulse.y -= Keyboard.isKeyDown(Keyboard.KEY_W) ? 1f : 0f;
		playerImpulse.y += Keyboard.isKeyDown(Keyboard.KEY_S) ? 1f : 0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_X)) playerImpulse = new HvlCoord(player.speed).multiply(-1f);
		playerImpulse.x = HvlMath.limit(playerImpulse.x, 0f, new HvlCoord(playerImpulse).normalize().x);
		playerImpulse.y = HvlMath.limit(playerImpulse.y, 0f, new HvlCoord(playerImpulse).normalize().y);
		player.speed.add(playerImpulse.multiply(delta * ACCELERATION));
		player.location.add(new HvlCoord(player.speed).multiply(delta));
	}
	
}
