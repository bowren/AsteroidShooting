/* This file is part of AsteroidShooting.

AsteroidShooting is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

AsteroidShooting is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with AsteroidShooting.  If not, see <http://www.gnu.org/licenses/>. */

package com.bowren.asteroids;

import android.opengl.GLES11;
import android.os.SystemClock;

public class Animation 
{
	public final static int EXPLOSION = 0;
	public final static int THRUST = 1;
	
	public final static float SHIP_THRUST_SCALE = 0.5f;
	private final static float EXPLOSION_WIDTH = 1.0f / 6.0f;
	private final static float EXPLOSION_HEIGHT = 1.0f / 3.0f;
	private final static int TIME_PER_FRAME = 25;
	
	private Vector2D texPos;
	private Vector2D texBounds;
	private TextureManager tm;
	private int animationTexture = 0;
	private float animationWidth = 0;
	private float animationHeight = 0;
	private float lastTime = 0;
	private float curTime = 0;
	private boolean continuous = false;
	private boolean hasLastTime = false;
	
	public Animation(int animation, TextureManager t)
	{
		tm = t;
		texPos = new Vector2D(0, 0);
		texBounds = new Vector2D();
		if (animation == EXPLOSION)
		{
			animationTexture = TextureManager.EXPLOSION;
			animationWidth = EXPLOSION_WIDTH;
			animationHeight = EXPLOSION_HEIGHT;
			texBounds.x = 5;
			texBounds.y = 2;
			continuous = false;
		}
		else if (animation == THRUST)
		{
			animationTexture = TextureManager.SHIP_THRUST;
			animationWidth = SHIP_THRUST_SCALE;
			animationHeight = SHIP_THRUST_SCALE;
			texBounds.x = 1;
			texBounds.y = 1;
			continuous = true;
		}
	}
	/* Returns true if the animation is complete
	   and false if not, if the animation is
	   continuous it always returns false */
	public boolean Animate()
	{
		if (!hasLastTime)
		{
			lastTime = SystemClock.uptimeMillis();
			hasLastTime = true;
		}
		curTime = SystemClock.uptimeMillis();
		if (curTime - lastTime > TIME_PER_FRAME)
		{
			if (texPos.x == texBounds.x && texPos.y == texBounds.y)
			{
				texPos.x = 0;
				texPos.y = 0;
				if (!continuous)
					return true;
			}
			if (texPos.x < texBounds.x)
			{
				texPos.x++;
			}
			else
			{
				texPos.y++;
				texPos.x = 0;
			}
			hasLastTime = false;
		}
		
		if (tm.currentTexture != animationTexture)
			tm.setTexture(animationTexture);
		
		GLES11.glMatrixMode(GLES11.GL_TEXTURE);
		
		GLES11.glLoadIdentity();
		GLES11.glTranslatef((texPos.x * animationWidth), (texPos.y * animationHeight), 1);
		GLES11.glScalef(animationWidth, animationHeight, 1);
		
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		
		return false;
	}
	
	public static void clearMatrix()
	{
		GLES11.glMatrixMode(GLES11.GL_TEXTURE);
		
		GLES11.glLoadIdentity();
		
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
	}
}
