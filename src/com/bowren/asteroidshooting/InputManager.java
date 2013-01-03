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

import java.io.File;
import java.io.FileWriter;
import java.nio.ShortBuffer;
import android.opengl.GLES11;
import android.opengl.GLU;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class InputManager 
{
	private final static int INVALID_POINTER_ID = -1;
	private final static float OFFSET_UP = 0.15f;
	private final static float OFFSET_DOWN = 0;
	private final static float OFFSET_HORIZONTAL = 0.2f;
	
	public float jpbX = 0;
	public float jpbY = 0;
	public float jpbstartX = 0;
	public float jpbstartY = 0;
	public boolean fireDown = false;
	public boolean moveLeft = false;
	public boolean moveRight = false;
	public boolean moveUp = false;
	public boolean moveDown = false;
	private ShortBuffer indexBuffer;
	private TextureManager tm;
	private AsteroidsActivity context;
	private int upkeycode = KeyEvent.KEYCODE_W;
	private int downkeycode = KeyEvent.KEYCODE_S;
	private int leftkeycode = KeyEvent.KEYCODE_A;
	private int rightkeycode = KeyEvent.KEYCODE_D;
	private int firekeycode = KeyEvent.KEYCODE_SPACE;
	private int joystickPointer = INVALID_POINTER_ID;
	private int buttonPointer = INVALID_POINTER_ID;
	private float jpdWidth = 1.75f;
	private float jpdHeight = 1.75f;
	private float jpbWidth = 1;
	private float jpbHeight = 1;
	private float sWidth = 0;
	private float sHeight = 0;
	private float buttonWidth = 1.5f;
	private float buttonHeight = 1.5f;
	private float ratio = 0;
	//NDC - normalized device coordinate(the coordinates OpenGL uses)
	private float[] ndcs = new float[16];
	private float joystick_range_x = 0;
	private float joystick_range_y = 0;
	private float button_range_x = 0;
	private float button_range_y = 0;
	
	public InputManager(ShortBuffer ib, TextureManager t, float sw, float sh, AsteroidsActivity gamec)
	{
		indexBuffer = ib;
		tm = t;
		sWidth = sw;
		sHeight = sh;
		context = gamec;
		ratio = sWidth / sHeight;
		jpbstartX = (ratio + (jpbWidth * 0.5f)) - 0.1f;
		jpbstartY = (-2 + (jpbHeight * 0.5f)) - 0.15f;
		jpbX = jpbstartX;
		jpbY = jpbstartY;
		joystick_range_x = sWidth * 0.75f;
		joystick_range_y = sHeight * 0.5f;
		button_range_x = sWidth * 0.2f;
		button_range_y = sHeight * 0.65f;
		for (int i = 0; i < 3; i++)
		{
			ndcs[i] = 0;
		}
		if (context.fileContents != null)
		{
			//grab the keycodes from the file and set the strings to them.
			int[] spaces = new int[5];
			int spaceIndex = 0;
			for (int i = 1; i < context.fileContents.length(); i++)
			{
				if (context.fileContents.charAt(i) == ' ')
				{
					spaces[spaceIndex] = i;
					spaceIndex++;
				}
			}
			upkeycode = Integer.parseInt(context.fileContents.substring(1, spaces[0]));
			downkeycode = Integer.parseInt(context.fileContents.substring(spaces[0] + 1, spaces[1]));
			leftkeycode = Integer.parseInt(context.fileContents.substring(spaces[1] + 1, spaces[2]));
			rightkeycode = Integer.parseInt(context.fileContents.substring(spaces[2] + 1, spaces[3]));
			firekeycode = Integer.parseInt(context.fileContents.substring(spaces[3] + 1, spaces[4]));
		}
	}
	
	public void updateTouch(MotionEvent mevent, int[] viewport, float[] modelview, float[] project)
	{
		float[] touchX = {0.0f, 0.0f};
    	float[] touchY = {0.0f, 0.0f};
		int actionType = mevent.getAction() & MotionEvent.ACTION_MASK;
    	switch (actionType)
    	{
    		case MotionEvent.ACTION_DOWN:
    		{
    			//the first finger went down
    			touchX[0] = mevent.getX(0);
    			touchY[0] = mevent.getY(0);
    	    	
    			if (joystickPointer == INVALID_POINTER_ID)
    			{
    				//check in joystick range.
	    	    	if (touchX[0] > joystick_range_x && touchY[0] > joystick_range_y)
	    	    	{
	    	    		joystickPointer = mevent.getPointerId(0);
	    	    		
		    	    	GLU.gluUnProject(touchX[0], touchY[0], 0, modelview, 0, project, 0, viewport, 0, ndcs, 0);
		    	    	
		    	    	jpbX = ndcs[0] * 5.0f;
		    	    	jpbY = (ndcs[1] * -1.0f) - 1.4f;
	    	    	}
	    	    	else
	    	    	{
	    	    		jpbX = jpbstartX;
	        			jpbY = jpbstartY;
	    	    	}
    			}
    			if (buttonPointer == INVALID_POINTER_ID)
    			{
    				//check in button range.
    				if (touchX[0] < button_range_x && touchY[0] > button_range_y)
    				{
    					buttonPointer = mevent.getPointerId(0);
    					
    					fireDown = true;
    				}
    			}
    	    	
    			break;
    		}
    		
    		case MotionEvent.ACTION_UP:
    		{
    			//only one finger was down and it just
    			//went up, invalidate the active pointer.
    			joystickPointer = INVALID_POINTER_ID;
    			buttonPointer = INVALID_POINTER_ID;
    			fireDown = false;
    			jpbX = jpbstartX;
    			jpbY = jpbstartY;
    			break;
    		}
    		
    		case MotionEvent.ACTION_POINTER_DOWN:
    		{
    			//one finger is already down,
    			//and another just went down.
    			int pointerIndex = mevent.getActionIndex();
    			
    			if (pointerIndex > 1)
    				break;
    			
    	        touchX[pointerIndex] = mevent.getX(pointerIndex);
    			touchY[pointerIndex] = mevent.getY(pointerIndex);
    	    	
    			if (joystickPointer == INVALID_POINTER_ID)
    			{
    				//check in joystick range.
	    	    	if (touchX[pointerIndex] > joystick_range_x && touchY[pointerIndex] > joystick_range_y)
	    	    	{
	    	    		joystickPointer = mevent.getPointerId(pointerIndex);
	    	    		
		    	    	GLU.gluUnProject(touchX[pointerIndex], touchY[pointerIndex], 0, modelview, 0, project, 0, viewport, 0, ndcs, 0);
		    	    	
		    	    	jpbX = ndcs[0] * 5.0f;
		    	    	jpbY = (ndcs[1] * -1.0f) - 1.4f;
	    	    	}
	    	    	else
	    	    	{
	    	    		jpbX = jpbstartX;
	        			jpbY = jpbstartY;
	    	    	}
    			}
    			if (buttonPointer == INVALID_POINTER_ID)
    			{
    				//check in button range.
    				if (touchX[pointerIndex] < button_range_x && touchY[pointerIndex] > button_range_y)
    				{
    					buttonPointer = mevent.getPointerId(pointerIndex);
    					fireDown = true;
    				}
    			}
    			break;
    		}
    		
    		case MotionEvent.ACTION_POINTER_UP:
    		{
    			//two fingers were down but one went up.
    			//find the index of the one that went up and use it to find its ID
    			int pointerIndex = mevent.getActionIndex();
    			if (pointerIndex > 1)
    				break;
    	        int pointerId = mevent.getPointerId(pointerIndex);
    	        if (pointerId == joystickPointer) 
    	        {
    	        	joystickPointer = INVALID_POINTER_ID;
    	        	jpbX = jpbstartX;
        			jpbY = jpbstartY;
    	        }
    	        else if (pointerId == buttonPointer)
    	        {
    	        	buttonPointer = INVALID_POINTER_ID;
    	        	fireDown = false;
    	        }
    			break;
    		}
    		
    		case MotionEvent.ACTION_MOVE:
    		{
    			//no telling how many are down or which moved,
    			//we only know that a pointer moved. in most cases
    			//getActionIndex() is enough but for some reason it always
    			//returns 0 here. we must iterate through all that are down.
    			if (mevent.getPointerCount() == 1)
    			{
    				int pointerid = mevent.getPointerId(0);
    				
    				touchX[0] = mevent.getX(0);
					touchY[0] = mevent.getY(0);
    				
    				if (pointerid == joystickPointer)
    				{
    					//check in joystick range.
    					if (touchX[0] > joystick_range_x && touchY[0] > joystick_range_y)
		    	    	{
			    	    	GLU.gluUnProject(touchX[0], touchY[0], 0, modelview, 0, project, 0, viewport, 0, ndcs, 0);
			    	    	
			    	    	jpbX = ndcs[0] * 5.0f;
			    	    	jpbY = (ndcs[1] * -1.0f) - 1.4f;
		    	    	}
		    	    	else
		    	    	{
		    	    		jpbX = jpbstartX;
		        			jpbY = jpbstartY;
		    	    	}
    				}
    				else if (pointerid == buttonPointer)
    				{
    					if (!(touchX[0] < button_range_x) || !(touchY[0] > button_range_y))
    					{
    						buttonPointer = INVALID_POINTER_ID;
    						fireDown = false;
    					}
    				}
    			}
    			else if (mevent.getPointerCount() > 1)
    			{
    				for (int i = 0; i < 2; i++)
    				{
    					int pointerid = mevent.getPointerId(i);
    					touchX[i] = mevent.getX(i);
    					touchY[i] = mevent.getY(i);
    					
    					if (pointerid == joystickPointer)
        				{
        					if (touchX[i] > joystick_range_x && touchY[i] > joystick_range_y)
    		    	    	{
    			    	    	GLU.gluUnProject(touchX[i], touchY[i], 0, modelview, 0, project, 0, viewport, 0, ndcs, 0);
    			    	    	
    			    	    	jpbX = ndcs[0] * 5.0f;
    			    	    	jpbY = (ndcs[1] * -1.0f) - 1.4f;
    		    	    	}
    		    	    	else
    		    	    	{
    		    	    		jpbX = jpbstartX;
    		        			jpbY = jpbstartY;
    		    	    	}
        					
        				}
    					else if (pointerid == buttonPointer)
        				{
    						if (!(touchX[i] < button_range_x) || !(touchY[i] > button_range_y))
    						{
    							buttonPointer = INVALID_POINTER_ID;
        						fireDown = false;
    						}
        				}
    				}
    			}
    	    	
    			break;
    		}
    	}
		
    	if (jpbX > jpbstartX + OFFSET_HORIZONTAL)
    	{
    		moveRight = true;
			moveLeft = false;
    	}
		else if (jpbX < jpbstartX - OFFSET_HORIZONTAL)
		{
			moveLeft = true;
			moveRight = false;
		}
		else
		{
			moveLeft = false;
			moveRight = false;
		}
    	
		if (jpbY > jpbstartY + OFFSET_UP)
		{
			moveUp = true;
			moveDown = false;
		}
		else if (jpbY < jpbstartY - OFFSET_DOWN)
		{
			moveDown = true;
			moveUp = false;
		}
		else
		{
			moveUp = false;
			moveDown = false;
		}
    	
	}
	
	public void updateKeyDown(int keycode, int hscore, KeyEvent keyevent)
	{
		if (context.usesKeyboard)
		{
			if (keycode == upkeycode)
				moveUp = true;
			if (keycode == downkeycode)
				moveDown = true;
			if (keycode == rightkeycode)
				moveRight = true;
			if (keycode == leftkeycode)
				moveLeft = true;
			if (keycode == firekeycode)
				fireDown = true;
		}
		if (keycode == KeyEvent.KEYCODE_BACK)
		{
			try
			{
				final File storageDir = new File(Environment.getExternalStorageDirectory() + "/Asteroids");
				if (!storageDir.exists())
					storageDir.mkdir();
				final File file = new File(storageDir, "AsteroidsHighScore.txt");
				final FileWriter fw = new FileWriter(file);
				fw.write(String.valueOf(hscore));
				fw.flush();
			    fw.close();
			}
			catch (Exception e) 
			{
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			tm.FreeTextures();
			context.soundPool.release();
			context.soundPool = null;
			context.audioManager.unloadSoundEffects();
			context.finish();
		}
	}
	
	public void updateKeyUp(int keycode, KeyEvent keyevent)
	{
		if (keycode == upkeycode)
			moveUp = false;
		if (keycode == downkeycode)
			moveDown = false;
		if (keycode == rightkeycode)
			moveRight = false;
		if (keycode == leftkeycode)
			moveLeft = false;
		if (keycode == firekeycode)
			fireDown = false;
	}
	
	public void Draw()
	{
		if (context.usesKeyboard)
			return;
		
		if (tm.currentTexture != TextureManager.JOYPAD)
			tm.setTexture(TextureManager.JOYPAD);
		
		GLES11.glLoadIdentity();
		GLES11.glTranslatef(ratio * 1, ratio * -1.17f, -6);
		GLES11.glScalef(jpdWidth, jpdHeight, 1);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
		
		tm.setTexture(TextureManager.JOYBALL);
		
		GLES11.glLoadIdentity();
		GLES11.glTranslatef(jpbX, jpbY, -6);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
		
		tm.setTexture(TextureManager.BUTTON);
		
		GLES11.glLoadIdentity();
		
		if (fireDown)
			GLES11.glColor4f(1, 1, 1, 0.5f);
		GLES11.glTranslatef(ratio * -2.0f, ratio * -1.17f, -6);
		GLES11.glScalef(buttonWidth, buttonHeight, -6);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
		
		GLES11.glColor4f(1, 1, 1, 1);
	}
}