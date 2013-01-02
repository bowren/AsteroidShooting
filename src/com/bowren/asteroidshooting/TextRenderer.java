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

public class TextRenderer 
{
	//Precomputed values for the width and height of a char in the texture.
	private final static float TEXTURE_CHAR_WIDTH = 0.166666667f;
	private final static float TEXTURE_CHAR_HEIGHT = 0.142857143f;
	private final static int LETTER_ASCII_OFFSET = 65;
	private final static int NUMBER_ASCII_OFFSET = 48;
	
	private final static Vector2D[] letterPositions = {new Vector2D(0, 0), new Vector2D(1, 0), new Vector2D(2, 0), new Vector2D(3, 0),
													   new Vector2D(4, 0), new Vector2D(5, 0), new Vector2D(0, 1), new Vector2D(1, 1),
													   new Vector2D(2, 1), new Vector2D(3, 1), new Vector2D(4, 1), new Vector2D(5, 1),
													   new Vector2D(0, 2), new Vector2D(1, 2), new Vector2D(2, 2), new Vector2D(3, 2),
													   new Vector2D(4, 2), new Vector2D(0, 3), new Vector2D(1, 3), new Vector2D(2, 3),
													   new Vector2D(3, 3), new Vector2D(0, 4), new Vector2D(1, 4), new Vector2D(2, 4),
													   new Vector2D(3, 4), new Vector2D(4, 4)};
	private final static Vector2D[] numberPositions = {new Vector2D(5, 4), new Vector2D(0, 5), new Vector2D(1, 5), new Vector2D(2, 5), new Vector2D(3, 5),
										 	 		   new Vector2D(4, 5), new Vector2D(5, 5), new Vector2D(0, 6), new Vector2D(1, 6), new Vector2D(2, 6)};
	private ShortBuffer indexBuffer;
	private TextureManager tm;
	
	public TextRenderer(ShortBuffer ib, TextureManager t)
	{
		indexBuffer = ib;
		tm = t;
	}
	
	public void DrawText(Text t)
	{
		if (tm.currentTexture != TextureManager.CHARACTERS)
			tm.setTexture(TextureManager.CHARACTERS);
		
		for (int i = 0; i < t.text.length(); i++)
		{
			char currentChar = t.text.charAt(i);
			
			//is it a letter or number or space
			if (currentChar >= 65 && currentChar <= 90)
			{
				t.charTexPos.x = letterPositions[currentChar - LETTER_ASCII_OFFSET].x;
				t.charTexPos.y = letterPositions[currentChar - LETTER_ASCII_OFFSET].y;
			}
			else if (currentChar >= 48 && currentChar <= 57)
			{
				t.charTexPos.x = numberPositions[currentChar - NUMBER_ASCII_OFFSET].x;
				t.charTexPos.y = numberPositions[currentChar - NUMBER_ASCII_OFFSET].y;
			}
			else if (currentChar == 32)
			{
				t.charTexPos.x = 3;
				t.charTexPos.y = 6;
			}
			
			t.center.x = (t.textPos.x + (i * t.charWidth)) + (t.charWidth / 2);
			t.center.y = t.textPos.y + (t.charHeight / 2);
			
			GLES11.glMatrixMode(GLES11.GL_TEXTURE);
			
			GLES11.glLoadIdentity();
			GLES11.glTranslatef((t.charTexPos.x * TEXTURE_CHAR_WIDTH), (t.charTexPos.y * TEXTURE_CHAR_HEIGHT), 1);
			GLES11.glScalef(TEXTURE_CHAR_WIDTH, TEXTURE_CHAR_HEIGHT, 1);
			
			GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
			
			GLES11.glLoadIdentity();
			
			GLES11.glTranslatef(t.center.x, t.center.y, Utils.DISCARD_Z);
			GLES11.glRotatef(180, 1, 0, 0);
			GLES11.glTranslatef(-t.center.x, -t.center.y, -Utils.DISCARD_Z);
			
			GLES11.glTranslatef(t.textPos.x + (i * t.Spacing), t.textPos.y, Utils.DISCARD_Z);
			GLES11.glScalef(t.charWidth, t.charHeight, Utils.DISCARD_Z_SCALE);
			GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
		}
		
		GLES11.glMatrixMode(GLES11.GL_TEXTURE);
		
		GLES11.glLoadIdentity();
		
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
	}
}
