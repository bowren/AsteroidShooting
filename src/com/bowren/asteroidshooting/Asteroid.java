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

package com.bowren.asteroidshooting;

import java.nio.ShortBuffer;
import java.util.Random;
import android.opengl.GLES11;

public class Asteroid extends Particle
{	
	public Asteroid(ShortBuffer ib, TextureManager t) 
	{
		super(ib, t);
		width = 1.0f;
		height = 1.0f;
		rect.setInitial(vecPos, width, height);
	}

	@Override
	public void Update(float deltaTime)
	{
		angle += rotateSpeed * deltaTime;
		vecPos.x += velocityX * deltaTime;
		vecPos.y += velocityY * deltaTime;
		if (vecPos.x > 3)
			vecPos.x = -4;
		else if (vecPos.x < -4)
			vecPos.x = 3;
		if (vecPos.y > 3)
			vecPos.y = -3;
		else if (vecPos.y < -3)
			vecPos.y = 3;
		super.Update(deltaTime);
	}
	
	public void Validate(Random r, float Width, float Height, float maxSpd) 
	{
		int posorneg = 0;
		posorneg = r.nextInt(9);
		if (posorneg <= 5)
		{
			vecPos.x = r.nextFloat() * (4.0f - 2.0f) + 2.0f;
			vecPos.y = r.nextFloat() * (4.0f - 2.0f) + 2.0f;
		}
		else
		{
			vecPos.x = r.nextFloat() * (-4.0f - (-2.0f)) + (-2.0f);
			vecPos.y = r.nextFloat() * (-4.0f - (-2.0f)) + (-2.0f);
		}
		do
		{
			velocityX = r.nextFloat() * (maxSpd - (-maxSpd)) + (-maxSpd);
		}
		while (Math.abs(velocityX) < AsteroidManager.MIN_SPEED);//make sure speed is not too slow
		do
		{
			velocityY = r.nextFloat() * (maxSpd - (-maxSpd)) + (-maxSpd);
		}
		while (Math.abs(velocityY) < AsteroidManager.MIN_SPEED);
		angle = 90;
		rotateSpeed = (float)(r.nextInt(120 - 80 + 1) + 80);
		width = Width;
		height = Height;
		super.Validate();
	}
	
	public void Validate(float x, float y, float velX, float velY, float rotatespd, float Width, float Height) 
	{
		vecPos.x = x;
		vecPos.y = y;
		velocityX = velX;
		velocityY = velY;
		angle = 90;
		rotateSpeed = rotatespd;
		width = Width;
		height = Height;
		super.Validate();
	}

	@Override
	public void Draw() 
	{
		if (tm.currentTexture != TextureManager.ASTEROID)
			tm.setTexture(TextureManager.ASTEROID);
		
		GLES11.glLoadIdentity();
		
		GLES11.glTranslatef(center.x, center.y, Utils.DISCARD_Z);
		GLES11.glRotatef(angle, 0, 0, 1);
		GLES11.glTranslatef(-center.x, -center.y, -Utils.DISCARD_Z);
		
		GLES11.glTranslatef(vecPos.x, vecPos.y, Utils.DISCARD_Z);
		GLES11.glScalef(width, height, Utils.DISCARD_Z_SCALE);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
