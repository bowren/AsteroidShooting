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
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameRenderer implements Renderer
{
	//a float is 4 bytes
	private final static int BYTES_PER_FLOAT = 4;
	//a short is 2 bytes
	private final static int BYTES_PER_SHORT = 2;
	
	private AsteroidsActivity context;
	private DisplayMetrics dm;
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer textureBuffer;
	private TextureManager texman;
	private InputManager inman;
	private CollisionHandler cHandler;
	private Ship player;
	private AsteroidManager astman;
	private TextRenderer textRend;
	private TextManager textManager;
	private float ratio = 0;
	private float lastTime = 0;
	private float curtime = 0;
	private float deltaTime = 0;
	private int[] viewport = new int[16];
	private float[] modelview = new float[16];
	private float[] project = new float[16];
	
	public GameRenderer(AsteroidsActivity a)
	{
		context = a;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		initGraphics();
		dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		ratio = (float) dm.widthPixels / dm.heightPixels;
		
		textRend = new TextRenderer(indexBuffer, texman);
		player = new Ship(indexBuffer, texman, textRend, context, ratio);
		astman = new AsteroidManager(indexBuffer, texman);
		cHandler = new CollisionHandler(context, player, astman.getAsteroids(), player.getBullets(), astman.getAsteroidPool(), player.getBulletPool());
		inman = new InputManager(indexBuffer, texman, dm.widthPixels, dm.heightPixels, context);
		textManager = new TextManager(textRend, ratio);
		
		lastTime = SystemClock.uptimeMillis();
	}
	
	private void initGraphics()
	{
		/* sets the clear value for the color buffer. when glClear() 
		 * is called and GL_COLOR_BUFFER_BIT is passed in, the screen clears */
	    GLES11.glClearColor(0, 0, 0, 0);
	    /* tells OpenGL a render method for the passed in target. in this case GL_PERSPECTIVE_CORRECTION_HINT for color 
	     * and texture quality and fog interpolation. Passing GL_NICEST allows for the best quality. GL_FASTEST for best performance.*/
	    GLES11.glHint(GLES11.GL_PERSPECTIVE_CORRECTION_HINT, GLES11.GL_NICEST);
	    /* enables blending for, it is does not simply
	     * discard the alpha pixels like alpha test, so
	     * it can achieve semitransparency. */
	    GLES11.glEnable(GLES11.GL_BLEND);
	    /* specifies the source and destination blending factors in the equation 
	     * (by default) Cf = (Cs * S) + (Cd * D) where S and D are the blending
	     * factors. S will be the source alpha value and D will be the destination
	     * alpha value. */
	    GLES11.glBlendFunc(GLES11.GL_SRC_ALPHA, GLES11.GL_ONE_MINUS_SRC_ALPHA);
	    
	    float vertices[] =
    	{
    		0.0f, 0.0f, 0.0f,//0 (0,0) origin
    		1.0f, 1.0f, 0.0f,//1 (1,1)
    		0.0f, 1.0f, 0.0f,//2 (0,1)
    		1.0f, 0.0f, 0.0f //3 (1,0)
    	};

    	short indices[] =
    	{
    	    2, 1, 0,
    	    0, 3, 1
    	};
    	
    	float textureCoords[] = 
		{
			0.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
			1.0f, 0.0f
		};
	    
	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * BYTES_PER_SHORT);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureCoords.length * BYTES_PER_FLOAT);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoords);
		textureBuffer.position(0);
		
		texman = new TextureManager(context);
		
		GLES11.glEnableClientState(GLES11.GL_TEXTURE_COORD_ARRAY);
		GLES11.glTexCoordPointer(2, GLES11.GL_FLOAT, 0, textureBuffer);
		
		GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);
        GLES11.glVertexPointer(3, GLES11.GL_FLOAT, 0, vertexBuffer);
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		GLES11.glViewport(0, 0, width, height);
		
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		
		GLES11.glLoadIdentity();
		GLES11.glFrustumf(-ratio, ratio, -1, 1, 3, 10);
		
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);

		GLES11.glLoadIdentity();
		
		GLES11.glGetIntegerv(GLES11.GL_VIEWPORT, viewport, 0);
    	GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, modelview, 0);
    	GLES11.glGetFloatv(GLES11.GL_PROJECTION_MATRIX, project, 0);
	}
	
	public void onDrawFrame(GL10 gl) 
	{
		//Logic
		curtime = SystemClock.uptimeMillis();
		deltaTime = (curtime - lastTime) / 1000;
		lastTime = SystemClock.uptimeMillis();
		
		cHandler.checkCollisions();
		player.Update(deltaTime, inman);
		player.updateParticles(deltaTime);
		astman.updateParticles(deltaTime);
		textManager.Update(cHandler.Score, astman.Level, player.Lives, cHandler.highScore);
		
		GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT);
		
		GLES11.glViewport(0, 0, dm.widthPixels, dm.heightPixels);
		
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		
		GLES11.glLoadIdentity();
		GLES11.glFrustumf(-ratio, ratio, -1, 1, 3, 10);
		
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		
		//Draw Calls
		BackGround.Draw(indexBuffer, texman);
		player.Draw();
		player.drawParticles();
		astman.drawParticles();
		textManager.Draw();
		player.drawLives();
		inman.Draw();
	}
	
	public void passMotionEvent(MotionEvent e)
	{
		if (inman != null)
			inman.updateTouch(e, viewport, modelview, project);
	}
	
	public void passKeyDownEvent(int keyCode, KeyEvent event)
	{
		if (inman != null)
			inman.updateKeyDown(keyCode, cHandler.highScore, event);
	}
	
	public void passKeyUpEvent(int keyCode, KeyEvent event)
	{
		if (inman != null)
			inman.updateKeyUp(keyCode, event);
	}
}
