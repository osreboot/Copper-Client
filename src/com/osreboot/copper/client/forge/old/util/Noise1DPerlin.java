package com.osreboot.copper.client.forge.old.util;

import java.util.Random;

import com.osreboot.copper.client.forge.old.ForgeUtil;
import com.osreboot.ridhvl2.HvlMath;

public class Noise1DPerlin {

	private float m0, m1;
	
	private float[] vectors;

	public Noise1DPerlin(Random random, int gridSizeArg, float m0Arg, float m1Arg){
		vectors = new float[gridSizeArg];
		for(int i = 0; i < gridSizeArg; i++) vectors[i] = (random.nextFloat() - 0.5f) * 2f;
		m0 = m0Arg;
		m1 = m1Arg;
	}

	public float map(float x){
		x = HvlMath.map(x, m0, m1, 0, vectors.length - 1);
		
		int v0Index = (int)Math.floor(x);
		int v1Index = (int)Math.ceil(x);
		
		float l = x - (float)v0Index;
		float v0 = vectors[v0Index] * l;
		float v1 = vectors[v1Index] * (l - 1f);

		return HvlMath.lerp(v0, v1, ForgeUtil.smoothstep(l));
	}

}
