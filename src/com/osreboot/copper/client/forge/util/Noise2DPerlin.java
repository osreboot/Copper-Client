package com.osreboot.copper.client.forge.util;

import java.util.Random;

import com.osreboot.copper.client.environment.WorldUtil;
import com.osreboot.copper.client.forge.ForgeUtil;
import com.osreboot.ridhvl2.HvlCoord;
import com.osreboot.ridhvl2.HvlMath;

public class Noise2DPerlin {

	private HvlCoord m0, m1;

	private HvlCoord[][] vectors;

	public Noise2DPerlin(Random random, int gridSizeArg, HvlCoord m0Arg, HvlCoord m1Arg){
		m0 = m0Arg;
		m1 = m1Arg;

		vectors = new HvlCoord[gridSizeArg][gridSizeArg];
		WorldUtil.loop2D(vectors, (x, y, c) -> {
			vectors[x][y] = new HvlCoord(random.nextFloat() * 2f - 1f, random.nextFloat() * 2f - 1f).normalize().fixNaN();
		});
	}

	public float map(HvlCoord c){
		c = new HvlCoord(c);
		c.x = HvlMath.map(c.x, m0.x, m1.x, 0, vectors.length - 1);
		c.y = HvlMath.map(c.y, m0.y, m1.y, 0, vectors[0].length - 1);
		
		HvlCoord c0Offset = new HvlCoord(c.x - (float)Math.floor(c.x), c.y - (float)Math.floor(c.y));
		float c0Value = dot(c0Offset, vectors[(int)Math.floor(c.x)][(int)Math.floor(c.y)]);

		HvlCoord c1Offset = new HvlCoord(c.x - (float)Math.ceil(c.x), c.y - (float)Math.floor(c.y));
		float c1Value = dot(c1Offset, vectors[(int)Math.ceil(c.x)][(int)Math.floor(c.y)]);

		HvlCoord c2Offset = new HvlCoord(c.x - (float)Math.ceil(c.x), c.y - (float)Math.ceil(c.y));
		float c2Value = dot(c2Offset, vectors[(int)Math.ceil(c.x)][(int)Math.ceil(c.y)]);

		HvlCoord c3Offset = new HvlCoord(c.x - (float)Math.floor(c.x), c.y - (float)Math.ceil(c.y));
		float c3Value = dot(c3Offset, vectors[(int)Math.floor(c.x)][(int)Math.ceil(c.y)]);

		float c4Value = HvlMath.lerp(c0Value, c1Value, ForgeUtil.smoothstep(c0Offset.x));
		float c5Value = HvlMath.lerp(c3Value, c2Value, ForgeUtil.smoothstep(c0Offset.x));
		float c6Value = HvlMath.lerp(c4Value, c5Value, ForgeUtil.smoothstep(c0Offset.y));

		return c6Value;
	}

	public static float dot(HvlCoord c0, HvlCoord c1){
		return c0.x * c1.x + c0.y * c1.y;
	}

}
