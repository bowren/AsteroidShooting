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

public class BackGround 
{
	private final static float BACKGROUND_WIDTH = 9.5f;
	private final static float BACKGROUND_HEIGHT = 5.25f;
	private static float background_pos_x = -(BACKGROUND_WIDTH / 2);
	private static float background_pos_y = -(BACKGROUND_HEIGHT / 2);
	private static float background_center_x = background_pos_x + (BACKGROUND_WIDTH / 2);
	private static float background_center_y = background_pos_y + (BACKGROUND_HEIGHT / 2);
	
	public static void Draw(ShortBuffer indexBuffer, TextureManager texman)
	{
		if (texman.currentTexture != TextureManager.SPACE)
			texman.setTexture(TextureManager.SPACE);
		
		GLES11.glLoadIdentity();
		
		GLES11.glTranslatef(background_center_x, background_center_y, Utils.DISCARD_Z);
		GLES11.glRotatef(180, 1, 0, 0);
		GLES11.glTranslatef(-background_center_x, -background_center_y, -Utils.DISCARD_Z);
		
		GLES11.glTranslatef(background_pos_x, background_pos_y, -6);
		GLES11.glScalef(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 1);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
