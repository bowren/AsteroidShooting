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

public class ObjectPool<T> 
{
	protected final static int DEFAULT_SIZE = 25;
	
	protected ShortBuffer indexBuffer;
	protected TextureManager tm;
	protected T[] objectArray;
	protected LinkedList<T> objects;
	
	protected ObjectPool(ShortBuffer ib, TextureManager t)
	{
		indexBuffer = ib;
		tm = t;
	}
	
	public T alloc()
	{
		if (objects.size() == 0)
			return null;//out of objects
		T first = objects.getFirst();
		objects.removeFirst();
		return first;
	}
	
	public void free(T object)
	{
		objects.add(object);
	}
}
