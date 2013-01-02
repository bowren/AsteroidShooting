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

public class Rectangle 
{
	public float bottom = 0;
	public float top = 0;
	public float left = 0;
	public float right = 0;
	
	public Rectangle()
	{
		
	}
	
	public void setInitial(Vector2D ePos, float Width, float Height)
	{
		bottom = ePos.y;
		top = ePos.y + Height;
		left = ePos.x;
		right = ePos.x + Width;
	}
}
