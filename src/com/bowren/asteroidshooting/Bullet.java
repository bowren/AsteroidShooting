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
import android.opengl.GLES11;

public class Bullet extends Particle
{
	private final static int BULLET_SPEED = 3;
	private final static int BULLET_ROTATE_SPEED = 1440;
	
	public Bullet(ShortBuffer ib, TextureManager t) 
	{
		super(ib, t);
		width = 0.5f;
		height = 0.5f;
		rect.setInitial(vecPos, width, height);
	}
	
	@Override
	public void Update(float deltaTime)
	{
		vecPos.x += velocityX * BULLET_SPEED * deltaTime;
		vecPos.y += velocityY * BULLET_SPEED * deltaTime;
		angle += BULLET_ROTATE_SPEED * deltaTime;
		super.Update(deltaTime);
	}

	public void Validate(Vector2D shipCenter, float shipVelocityX, float shipVelocityY, float Angle) 
	{
		velocityX = shipVelocityX;
		velocityY = shipVelocityY;
		angle = Angle;
		vecPos.x = (shipCenter.x + (velocityX / 2)) - (width / 2);
		vecPos.y = (shipCenter.y + (velocityY / 2)) - (height / 2);
		super.Validate();
	}

	@Override
	public void Draw() 
	{
		if (tm.currentTexture != TextureManager.PLASMA)
			tm.setTexture(TextureManager.PLASMA);
		
		GLES11.glLoadIdentity();
		
		GLES11.glTranslatef(center.x, center.y, Utils.DISCARD_Z);
		GLES11.glRotatef(angle, 0, 0, 1);
		GLES11.glTranslatef(-center.x, -center.y, -Utils.DISCARD_Z);
		
		GLES11.glTranslatef(vecPos.x, vecPos.y, Utils.DISCARD_Z);
		GLES11.glScalef(width, height, Utils.DISCARD_Z_SCALE);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
