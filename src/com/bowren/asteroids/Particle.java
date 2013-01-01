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

import java.nio.ShortBuffer;

public abstract class Particle extends Entity
{
	protected float rotateSpeed = 0;
	
	public Particle(ShortBuffer ib, TextureManager t)
	{
		super(ib, t);
	}
	
	protected void Validate()
	{
		rect.bottom = vecPos.y;
		rect.top = vecPos.y + height;
		rect.left = vecPos.x;
		rect.right = vecPos.x + width;
		center.x = vecPos.x + (width / 2);
		center.y = vecPos.y + (height / 2);
	}
	
	public void inValidate()
	{
		vecPos.x = 0;
		vecPos.y = 0;
		velocityX = 0;
		velocityY = 0;
		angle = 0;
		center.x = 0;
		center.y = 0;
	}
}
