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
import java.util.LinkedList;

public class BulletPool extends ObjectPool<Bullet>
{
	protected BulletPool(ShortBuffer ib, TextureManager t) 
	{
		super(ib, t);
		objectArray = new Bullet[DEFAULT_SIZE];
		objects = new LinkedList<Bullet>();
		for (int i = 0; i < DEFAULT_SIZE; i++)
		{
			objectArray[i] = new Bullet(ib, t);
			objects.add(objectArray[i]);
		}
	}
	
	protected BulletPool(ShortBuffer ib, TextureManager t, int size)
	{
		super(ib, t);
		objectArray = new Bullet[size];
		objects = new LinkedList<Bullet>();
		for (int i = 0; i < size; i++)
		{
			objectArray[i] = new Bullet(ib, t);
			objects.add(objectArray[i]);
		}
	}
}
