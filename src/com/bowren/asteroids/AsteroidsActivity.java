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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class AsteroidsActivity extends Activity 
{
	private final static File storageDir = new File(Environment.getExternalStorageDirectory() + "/Asteroids");
	private final static File prefLoc = new File(storageDir, "AsteroidsPrefs.txt");
	private final static File highScoreLoc = new File(storageDir, "AsteroidsHighScore.txt");
	
	public SoundPool soundPool;
	public AudioManager audioManager;
	public int[] sounds = new int[3];
	public boolean usesKeyboard = false;
	public String fileContents = null;
	public String highScoreString = null;
	private GameRenderer gr;
	private GLSurfaceView glv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        readFiles();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //makes activity full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        sounds[0] = soundPool.load(this, R.raw.shot, 1);
        sounds[1] = soundPool.load(this, R.raw.explosion, 1);
        sounds[2] = soundPool.load(this, R.raw.rocketthrusters, 1);
        glv = new GLSurfaceView(this);
        gr = new GameRenderer(this);
        glv.setRenderer(gr);
        setContentView(glv);
    }
    
    /*events handled here so the 
      rendering is not disturbed */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	if (!usesKeyboard)
    	{
	    	if (glv != null && gr != null)
	    		gr.passMotionEvent(event);
    	}
    	return true;
    }
    
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	//onKeyDown is still needed even if they do not use it for controls.
    	if (glv != null && gr != null)
    		gr.passKeyDownEvent(keyCode, event);
    	return false;
    }
    
    @Override 
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	if (usesKeyboard)
    	{
	    	if (glv != null && gr != null)
	    		gr.passKeyUpEvent(keyCode, event);
    	}
    	return false;
    }
    
    private void readFiles()
    {
    	//read preferences
        try 
        {
            final BufferedReader in = new BufferedReader(new FileReader(prefLoc));
            String str;
            while ((str = in.readLine()) != null) 
            {
            	fileContents = str;
            }
            in.close();
        }
        catch (Exception e) 
        {
        	
        }
        try 
        {
            final BufferedReader b = new BufferedReader(new FileReader(highScoreLoc));
            String s;
            while ((s = b.readLine()) != null) 
            {
            	highScoreString = s;
            }
            b.close();
        }
        catch (Exception e) 
        {
        	
        }
        if (fileContents != null)
        {
        	if (fileContents.charAt(0) == '0')
        		usesKeyboard = true;
        }
    }
}