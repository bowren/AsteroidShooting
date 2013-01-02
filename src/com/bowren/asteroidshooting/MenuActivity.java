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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MenuActivity extends Activity
{	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        TextView start = (TextView)findViewById(R.id.textView2);
        TextView options = (TextView)findViewById(R.id.textView3);
        TextView quit = (TextView)findViewById(R.id.textView4);
        start.setOnTouchListener(startlistener);
        options.setOnTouchListener(optionslistener);
        quit.setOnTouchListener(quitlistener);
	}
	
	OnTouchListener startlistener = new OnTouchListener()
	{
		public boolean onTouch(View v, MotionEvent event) 
		{
			Intent begingame = new Intent(MenuActivity.this, AsteroidsActivity.class);
			startActivity(begingame);
			return false;
		}
	};
	
	OnTouchListener optionslistener = new OnTouchListener()
	{
		public boolean onTouch(View v, MotionEvent event) 
		{
			Intent beginmenu = new Intent(MenuActivity.this, OptionsActivity.class);
			startActivity(beginmenu);
			return false;
		}
	};
	
	OnTouchListener quitlistener = new OnTouchListener()
	{
		public boolean onTouch(View v, MotionEvent event) 
		{
			finish();
			return false;
		}
	};
}