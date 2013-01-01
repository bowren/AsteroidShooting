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

public abstract class Entity 
{
	public Vector2D vecPos;
	public Rectangle rect;
	protected ShortBuffer indexBuffer;
	protected TextureManager tm;
	protected Vector2D center;
	protected float width = 0;
	protected float height = 0;
	protected float angle = 0;
	protected float velocityX = 0;
	protected float velocityY = 0;
	
	public Entity(ShortBuffer ib, TextureManager t)
	{
		indexBuffer = ib;
		tm = t;
		vecPos = new Vector2D(0, 0);
		center = new Vector2D(0, 0);
		rect = new Rectangle();
	}
	
	/*updates rectangle center
	  and velocity information */
	protected void Update(float deltaTime)
	{
		rect.bottom = vecPos.y;
		rect.top = vecPos.y + height;
		rect.left = vecPos.x;
		rect.right = vecPos.x + width;
		center.x = vecPos.x + (width / 2);
		center.y = vecPos.y + (height / 2);
	}
	
	public abstract void Draw();
}
