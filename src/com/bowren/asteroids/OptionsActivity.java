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
import java.io.FileWriter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends Activity
{
	private final static String[] KEYS = { "SOFT LEFT", "SOFT RIGHT", "HOME", "BACK", "CALL", "ENDCALL",
										   "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "STAR", "POUND",
										   "DPAD UP", "DPAD DOWN", "DPAD LEFT", "DPAD RIGHT", "DPAD CENTER",
										   "VOLUME UP", "VOLUME DOWN", "POWER", "CAMERA", "CLEAR", "A", "B",
										   "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
										   "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "COMMA", "PERIOD", "ALT LEFT",
										   "ALT RIGHT", "SHIFT LEFT", "SHIFT RIGHT", "TAB", "SPACE", "SYM", 
										   "EXPLORER", "ENVELOPE", "ENTER", "DEL", "GRAVE", "MINUS", "EQUALS",
										   "LEFT BRACKET", "RIGHT BRACKET", "BACKSLASH", "SEMICOLON",
										   "APOSTROPHE", "SLASH", "AT", "NUM", "HEADSETHOOK", "FOCUS", "PLUS",
										   "MENU", "NOTIFICATION", "SEARCH" };
	private final static File storageDir = new File(Environment.getExternalStorageDirectory() + "/Asteroids");
	private final static File prefLoc = new File(storageDir, "AsteroidsPrefs.txt");
	private final static File highScoreLoc = new File(storageDir, "AsteroidsHighScore.txt");
	
	private String fileContents = null;
	private String highScoreString = null;
	private String upkey = " W";
	private String downkey = " S";
	private String leftkey = " A";
	private String rightkey = " D";
	private String firekey = " Space";
	private RadioButton touchScreen;
	private RadioButton keyBoard;
	private Button changeup;
	private Button changedown;
	private Button changeleft;
	private Button changeright;
	private Button changefire;
	private TextView up;
	private TextView down;
	private TextView left;
	private TextView right;
	private TextView fire;
	private TextView highScoreView;
	private boolean upDown = false;
	private boolean downDown = false;
	private boolean leftDown = false;
	private boolean rightDown = false;
	private boolean fireDown = false;
	private int upkeycode = KeyEvent.KEYCODE_W;
	private int downkeycode = KeyEvent.KEYCODE_S;
	private int leftkeycode = KeyEvent.KEYCODE_A;
	private int rightkeycode = KeyEvent.KEYCODE_D;
	private int firekeycode = KeyEvent.KEYCODE_SPACE;
	private int currentKeycode = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		TabHost tabs = (TabHost) findViewById(R.id.tabhost);
        
        tabs.setup();
        
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        
        spec.setContent(R.id.tab1);
        spec.setIndicator("Controls");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("How to play");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("High Score");
        tabs.addTab(spec);
        
        spec = tabs.newTabSpec("tag4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Credits");
        tabs.addTab(spec);
        
        changeup = (Button)findViewById(R.id.buttonup);
        changedown = (Button)findViewById(R.id.buttondown);
        changeleft = (Button)findViewById(R.id.buttonleft);
        changeright = (Button)findViewById(R.id.buttonright);
        changefire = (Button)findViewById(R.id.buttonfire);
        
        touchScreen = (RadioButton)findViewById(R.id.rbtouchscreen);
        keyBoard = (RadioButton)findViewById(R.id.rbkeyboard);
        
        up = (TextView)findViewById(R.id.textup);
        down = (TextView)findViewById(R.id.textdown);
        left = (TextView)findViewById(R.id.textleft);
        right = (TextView)findViewById(R.id.textright);
        fire = (TextView)findViewById(R.id.textfire);
        
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
			//grab the keycodes from the file and set the strings to them.
			int[] spaces = new int[5];
			int spaceIndex = 0;
			for (int i = 1; i < fileContents.length(); i++)
			{
				if (fileContents.charAt(i) == ' ')
				{
					spaces[spaceIndex] = i;
					spaceIndex++;
				}
			}
			try
			{
				if (fileContents.charAt(0) == '0')
					keyBoard.setChecked(true);
				else
					touchScreen.setChecked(true);
				upkeycode = Integer.parseInt(fileContents.substring(1, spaces[0]));
				downkeycode = Integer.parseInt(fileContents.substring(spaces[0] + 1, spaces[1]));
				leftkeycode = Integer.parseInt(fileContents.substring(spaces[1] + 1, spaces[2]));
				rightkeycode = Integer.parseInt(fileContents.substring(spaces[2] + 1, spaces[3]));
				firekeycode = Integer.parseInt(fileContents.substring(spaces[3] + 1, spaces[4]));
			}
			catch (Exception e)
			{
				Toast.makeText(this, "Keyboard preference error.", Toast.LENGTH_LONG).show();
			}
			upkey = " " + KEYS[upkeycode - 1];
			downkey = " " + KEYS[downkeycode - 1];
			leftkey = " " + KEYS[leftkeycode - 1];
			rightkey = " " + KEYS[rightkeycode - 1];
			firekey = " " + KEYS[firekeycode - 1];
		}
		
		highScoreView = (TextView)findViewById(R.id.highscore);
		
		if (highScoreString != null)
			highScoreView.setText("High Score:" + highScoreString);
		else
			highScoreView.setText("No High Score Available Yet");
        
        changeup.setOnClickListener(uplistener);
        changedown.setOnClickListener(downlistener);
        changeleft.setOnClickListener(leftlistener);
        changeright.setOnClickListener(rightlistener);
        changefire.setOnClickListener(firelistener);
        
        up.append(upkey);
        down.append(downkey);
        left.append(leftkey);
        right.append(rightkey);
        fire.append(firekey);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
    	try
		{
    		if (!storageDir.exists())
    			storageDir.mkdir();
			final FileWriter fw = new FileWriter(prefLoc);
			final StringBuilder sb = new StringBuilder(64);
			if (keyBoard.isChecked())
				sb.append(0);
			else
				sb.append(1);
			sb.append(upkeycode);
			sb.append(' ');
			sb.append(downkeycode);
			sb.append(' ');
			sb.append(leftkeycode);
			sb.append(' ');
			sb.append(rightkeycode);
			sb.append(' ');
			sb.append(firekeycode);
			//the last space is for knowing the length of the last code.
			sb.append(' ');
			fw.write(sb.toString());
			fw.flush();
		    fw.close();
		}
		catch (Exception e) 
		{
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void openAlertDialog()
	{
		AlertDialog keydialog = new AlertDialog.Builder(this).create();
		keydialog.setOnKeyListener(keylistener);
		keydialog.setTitle("New Key");
		keydialog.setMessage("Press a key: ");
		keydialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", oklistener);
		keydialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", cancellistener);
		keydialog.show();
		keydialog.takeKeyEvents(true);
	}
	
	OnClickListener uplistener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			upDown = true;
			openAlertDialog();
		}
	};
	
	OnClickListener downlistener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			downDown = true;
			openAlertDialog();
		}
	};
	
	OnClickListener leftlistener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			leftDown = true;
			openAlertDialog();
		}
	};
	
	OnClickListener rightlistener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			rightDown = true;
			openAlertDialog();
		}
	};
	
	OnClickListener firelistener = new OnClickListener()
	{
		public void onClick(View v) 
		{
			fireDown = true;
			openAlertDialog();
		}
	};
	
	DialogInterface.OnClickListener oklistener = new DialogInterface.OnClickListener() 
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			if (upDown)
			{
				upkeycode = currentKeycode;
				upkey = "Up: " + KEYS[currentKeycode - 1];
				up.setText(upkey);
				upDown = false;
			}
			else if (downDown)
			{
				downkeycode = currentKeycode;
				downkey = "Down: " + KEYS[currentKeycode - 1];
				down.setText(downkey);
				downDown = false;
			}
			else if (leftDown)
			{
				leftkeycode = currentKeycode;
				leftkey = "Left: " + KEYS[currentKeycode - 1];
				left.setText(leftkey);
				leftDown = false;
			}
			else if (rightDown)
			{
				rightkeycode = currentKeycode;
				rightkey = "Right: " + KEYS[currentKeycode - 1];
				right.setText(rightkey);
				rightDown = false;
			}
			else if (fireDown)
			{
				firekeycode = currentKeycode;
				firekey = "Fire: " + KEYS[currentKeycode - 1];
				fire.setText(firekey);
				fireDown = false;
			}
		}
	};
	
	DialogInterface.OnClickListener cancellistener = new DialogInterface.OnClickListener() 
	{
		public void onClick(DialogInterface dialog, int which) 
		{
			upDown = false;
			downDown = false;
			leftDown = false;
			rightDown = false;
			fireDown = false;
		}
	};
	
	DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() 
	{
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) 
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN)
			{
				currentKeycode = keyCode;
				((AlertDialog)dialog).setMessage("Press a key: " + KEYS[currentKeycode - 1]);
			}
			return false;
		}
	};
}