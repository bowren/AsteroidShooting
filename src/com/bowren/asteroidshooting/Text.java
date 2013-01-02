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

public class Text 
{
	public String text;
	public Vector2D textPos;
	public Vector2D center;
	public Vector2D charTexPos;
	public float charWidth;
	public float charHeight;
	public float Spacing;
	
	public Text(String textString, float x, float y, float cwidth, float cheight)
	{
		textPos = new Vector2D(x, y);
		center = new Vector2D(x + (charWidth / 2), y + (charHeight / 2));
		charTexPos = new Vector2D(0, 0);
		text = textString;
		charWidth = cwidth;
		charHeight = cheight;
		Spacing = charWidth / 1.25f;
	}
	
	public void setText(String newText)
	{
		text = newText;
	}
}
