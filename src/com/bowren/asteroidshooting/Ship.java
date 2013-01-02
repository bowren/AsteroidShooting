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
import java.util.Iterator;
import java.util.LinkedList;
import android.opengl.GLES11;
import android.os.SystemClock;
import android.util.FloatMath;

public class Ship extends Entity implements ParticleEmitter
{
	public final static int STATIONARY = 0;
	public final static int THRUSTING = 1;
	public final static int EXPLODING = 2;
	public final static int DEAD = 3;
	
	private final static int SHIP_ROTATE_SPEED = 180;
	private final static int SHIP_ACCELERATION = 1;
	private final static int SHOOT_DELAY = 200;
	private final static int MAX_BULLETS = 50;
	private final static int MAX_SPEED = 4;
	private final static float LIVE_WIDTH = 0.4f;
	private final static float LIVE_HEIGHT = 0.4f;
	
	public int state = STATIONARY;
	public int Lives = 3;
	public boolean spawnSafe = true;
	private float lastTime = 0;
	private float curTime = 0;
	private boolean thrustPlaying = false;
	private AsteroidsActivity context;
	private LinkedList<Bullet> bullets;
	private BulletPool bulletPool;
	private Animation explosion;
	private Animation thrust;
	private TextRenderer textRend;
	private Text wait;
	private Vector2D Accel;
	private Vector2D livesPos;
	
	public Ship(ShortBuffer ib, TextureManager t, TextRenderer tr, AsteroidsActivity c, float aspectRatio) 
	{
		super(ib, t);
		bullets = new LinkedList<Bullet>();
		bulletPool = new BulletPool(indexBuffer, tm, MAX_BULLETS);
		explosion = new Animation(Animation.EXPLOSION, tm);
		thrust = new Animation(Animation.THRUST, tm);
		Accel = new Vector2D(0, 0);
		livesPos = new Vector2D(aspectRatio * 1.25f, 1.65f);
		wait = new Text("WAITING FOR ASTEROIDS TO CLEAR", 0 - ((24 * 0.25f) / 2), 0.25f, 0.25f, 0.25f);
		context = c;
		textRend = tr;
		width = 0.8f;
		height = 0.8f;
		vecPos.x = 0 - (width / 2);
		vecPos.y = 0 - (height / 2);
		rect.setInitial(vecPos, width, height);
		angle = 180;
	}
	
	public void Update(float deltaTime, InputManager inputStates)
	{
		if (state == DEAD || state == EXPLODING)
			return;
		velocityX = FloatMath.cos((angle + 270) * Utils.DEG_TO_RAD);//r*cos(theta)
		velocityY = FloatMath.sin((angle + 270) * Utils.DEG_TO_RAD);//r*sin(theta)

		if (inputStates.moveLeft)
			angle += SHIP_ROTATE_SPEED * deltaTime;
		else if (inputStates.moveRight)
			angle -= SHIP_ROTATE_SPEED * deltaTime;
		if (inputStates.moveUp)
		{
			if (Math.abs(Accel.x) < MAX_SPEED)
				Accel.x += velocityX * SHIP_ACCELERATION * deltaTime;
			if (Math.abs(Accel.y) < MAX_SPEED)
				Accel.y += velocityY * SHIP_ACCELERATION * deltaTime;
		}
		else if (inputStates.moveDown)
		{
			if (Math.abs(Accel.x) < MAX_SPEED)
				Accel.x -= velocityX * SHIP_ACCELERATION * deltaTime;
			if (Math.abs(Accel.y) < MAX_SPEED)
				Accel.y -= velocityY * SHIP_ACCELERATION * deltaTime;
		}
		else
		{
			if (Accel.x > 0.1)
				Accel.x -= Math.abs(velocityX) * SHIP_ACCELERATION * deltaTime;
			else if (Accel.x < -0.1)
				Accel.x += Math.abs(velocityX) * SHIP_ACCELERATION * deltaTime;
			else
				Accel.x = 0;
			
			if (Accel.y > 0.1)
				Accel.y -= Math.abs(velocityY) * SHIP_ACCELERATION * deltaTime;
			else if (Accel.y < -0.1)
				Accel.y += Math.abs(velocityY) * SHIP_ACCELERATION * deltaTime;
			else
				Accel.y = 0;
		}
		vecPos.x += Accel.x * deltaTime;
		vecPos.y += Accel.y * deltaTime;
		
		if (vecPos.x > 3)
			vecPos.x = -4;
		if (vecPos.x < -4)
			vecPos.x = 3;
		if (vecPos.y > 1.5f)
			vecPos.y = -2.5f;
		if (vecPos.y < -2.5f)
			vecPos.y = 1.5f;
		
		if (inputStates.fireDown)
		{
			if (!(vecPos.x > 4 || vecPos.x < -4 || vecPos.y > 3 || vecPos.y < -3))
				Fire();
		}
		
		if (state != EXPLODING && state != DEAD)
		{
			if (Math.abs(Accel.x) > 0 || Math.abs(Accel.y) > 0)
				state = THRUSTING;
			else
				state = STATIONARY;
		}
			
		super.Update(deltaTime);
	}

	@Override
	public void Draw() 
	{
		if (state != THRUSTING && thrustPlaying)
		{
			Utils.stopSound(context, Utils.THRUST_SOUND);
			thrustPlaying = false;
		}
		
		switch (state)
		{
			case STATIONARY:
			{
				if (tm.currentTexture != TextureManager.SHIP)
					tm.setTexture(TextureManager.SHIP);
				break;
			}
			case THRUSTING:
			{
				if (!thrustPlaying)
				{
					Utils.playSoundContinuous(context, Utils.THRUST_SOUND);
					thrustPlaying = true;
				}
				thrust.Animate();
				break;
			}
			case EXPLODING:
			{
				if (explosion.Animate())
				{
					state = DEAD;
					Accel.x = 0;
					Accel.y = 0;
					return;
				}
				break;
			}
			case DEAD:
			{
				if (spawnSafe)
				{
					Lives--;
					vecPos.x = 0 - (width / 2);
					vecPos.y = 0 - (height / 2);
					if (Lives > 0)
						state = STATIONARY;
				}
				else if (Lives > 0)
					textRend.DrawText(wait);
				return;
			}
		}
		
		GLES11.glLoadIdentity();
		
		GLES11.glTranslatef(center.x, center.y, Utils.DISCARD_Z);
		GLES11.glRotatef(angle, 0, 0, 1);
		GLES11.glTranslatef(-center.x, -center.y, -Utils.DISCARD_Z);
		
		GLES11.glTranslatef(vecPos.x, vecPos.y, Utils.DISCARD_Z);
		GLES11.glScalef(width, height, Utils.DISCARD_Z_SCALE);
		GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
		
		if (state != STATIONARY || state != DEAD)
			Animation.clearMatrix();
	}

	public void updateParticles(float deltaTime) 
	{
		for (Iterator<Bullet> i = bullets.iterator(); i.hasNext();)
		{
			Bullet b = i.next();
			b.Update(deltaTime);
			if (b.vecPos.x > 4 || b.vecPos.x < -4 || b.vecPos.y > 3 || b.vecPos.y < -3)
			{
				b.inValidate();
				bullets.remove(b);
				bulletPool.free(b);
				i = bullets.iterator();
			}
		}
	}

	public void drawParticles() 
	{
		for (Bullet b : bullets)
		{
			b.Draw();
		}
	}
	
	public void drawLives()
	{
		if (tm.currentTexture != TextureManager.SHIP)
			tm.setTexture(TextureManager.SHIP);
		for (int i = 0; i < Lives; i++)
		{
			GLES11.glLoadIdentity();
			
			GLES11.glTranslatef(livesPos.x + (i * LIVE_WIDTH) + (LIVE_WIDTH / 2), livesPos.y + (LIVE_HEIGHT / 2), Utils.DISCARD_Z);
			GLES11.glRotatef(180, 1, 0, 0);
			GLES11.glTranslatef(-(livesPos.x + (i * LIVE_WIDTH) + (LIVE_WIDTH / 2)), -(livesPos.y + (LIVE_HEIGHT / 2)), -Utils.DISCARD_Z);
			
			GLES11.glTranslatef(livesPos.x + (i * LIVE_WIDTH), livesPos.y, Utils.DISCARD_Z);
			GLES11.glScalef(LIVE_WIDTH, LIVE_HEIGHT, Utils.DISCARD_Z_SCALE);
			GLES11.glDrawElements(GLES11.GL_TRIANGLES, 6, GLES11.GL_UNSIGNED_SHORT, indexBuffer);
		}
	}
	
	private void Fire()
	{
		//No boolean for firing because the first shot has no delay.
		curTime = SystemClock.uptimeMillis();
		if (curTime - lastTime > SHOOT_DELAY)
		{
			Bullet b = bulletPool.alloc();
			b.Validate(center, velocityX, velocityY, angle);
			bullets.add(b);
			Utils.playSound(context, Utils.SHOOT_SOUND);
			lastTime = SystemClock.uptimeMillis();
		}
	}
	
	public LinkedList<Bullet> getBullets()
	{
		return bullets;
	}
	
	public BulletPool getBulletPool()
	{
		return bulletPool;
	}
}
