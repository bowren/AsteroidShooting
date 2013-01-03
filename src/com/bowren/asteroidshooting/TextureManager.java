/* Copyright (C) 2012 Joshua Bowren

This file is part of AsteroidShooting.

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;

public class TextureManager 
{
	public final static int SHIP = 0;
	public final static int ASTEROID = 1;
	public final static int JOYPAD = 2;
	public final static int JOYBALL = 3;
	public final static int BUTTON = 4;
	public final static int PLASMA = 5;
	public final static int SPACE = 6;
	public final static int EXPLOSION = 7;
	public final static int CHARACTERS = 8;
	public final static int SHIP_THRUST = 9;
	
	public boolean texturesEnabled = false;
	public int currentTexture;
	private int[] textures = new int[10];
	private Bitmap imgBuffer;
	private ByteBuffer pixBuffer;
	
	public TextureManager(Activity context)
	{
		GLES11.glEnable(GLES11.GL_TEXTURE_2D);
		texturesEnabled = true;
		
		GLES11.glGenTextures(10, textures, 0);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[0]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid1);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[1]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.joypad);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[2]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.joyball);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[3]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.button);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[4]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.plasma);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[5]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[6]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[7]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.freemonochars);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[8]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		imgBuffer = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceshipthrust);
		pixBuffer = ByteBuffer.allocateDirect(imgBuffer.getWidth() * imgBuffer.getHeight() * 4);
		pixBuffer.order(ByteOrder.nativeOrder());
		imgBuffer.copyPixelsToBuffer(pixBuffer);
		pixBuffer.position(0);
		
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[9]);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MAG_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_S, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexParameterf(GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_WRAP_T, GLES11.GL_CLAMP_TO_EDGE);
		GLES11.glTexImage2D(GLES11.GL_TEXTURE_2D, 0, GLES11.GL_RGBA, imgBuffer.getWidth(), imgBuffer.getHeight(), 0, GLES11.GL_RGBA, GLES11.GL_UNSIGNED_BYTE, pixBuffer);
		
		currentTexture = SHIP_THRUST;
	}
	
	public void ChangeTextureMode(boolean enable)
	{
	    if (!texturesEnabled)
	    {
	        if (enable)
	        {
	            GLES11.glEnable(GLES11.GL_TEXTURE_2D);
	            texturesEnabled = true;
	        }
	        else
	            return;
	    }
	    else
	    {
	        if (enable)
	            return;
	        else
	        {
	            GLES11.glDisable(GLES11.GL_TEXTURE_2D);
	            texturesEnabled = false;
	        }
	    }

	}
	
	public void setTexture(int texture)
	{
		if (texturesEnabled == false)
		{
			GLES11.glEnable(GLES11.GL_TEXTURE_2D);
			texturesEnabled = true;
		}
		GLES11.glBindTexture(GLES11.GL_TEXTURE_2D, textures[texture]);
		currentTexture = texture;
	}
	
	public boolean TexturesEnabled()
	{
		return texturesEnabled;
	}
	
	public void FreeTextures()
	{
		GLES11.glDeleteTextures(10, textures, 0);
	}
}