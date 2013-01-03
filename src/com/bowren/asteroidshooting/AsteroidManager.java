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

import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import android.os.SystemClock;

public class AsteroidManager implements ParticleEmitter
{
	public final static float MIN_SPEED = 0.1f;
	public final static float LARGE_ASTEROID_SIZE = 1.0f;
	public final static float MEDIUM_ASTEROID_SIZE = 0.5f;
	public final static float SMALL_ASTEROID_SIZE = 0.25f;
	public final static int LARGE_ASTEROID_POINT = 1;
	public final static int MEDIUM_ASTEROID_POINT = 2;
	public final static int SMALL_ASTEROID_POINT = 3;
	private final static int LEVEL_DELAY = 3000;
	private final static int MAX_ASTEROIDS = 50;
	private final static int INITIAL_ASTEROIDS = 5;
	private final static float INTIAL_WIDTH = 1.0f;
	private final static float INITAL_HEIGHT = 1.0f;
	private final static float SPEED_INCREMENT = 0.5f;
	
	public int Level = 1;
	private LinkedList<Asteroid> asteroids;
	private AsteroidPool asteroidPool;
	private Random r;
	private float curTime = 0;
	private float lastTime = 0;
	public float maxSpeed = 1.5f;
	private boolean hasLastTime = false;
	
	public AsteroidManager(ShortBuffer ib, TextureManager tm)
	{
		asteroids = new LinkedList<Asteroid>();
		asteroidPool = new AsteroidPool(ib, tm, MAX_ASTEROIDS);
		r = new Random();
		for (int i = 0; i < INITIAL_ASTEROIDS; i++)
		{
			Asteroid a = asteroidPool.alloc();
			a.Validate(r, INTIAL_WIDTH, INITAL_HEIGHT, maxSpeed);
			asteroids.add(a);
		}
	}

	public void updateParticles(float deltaTime) 
	{
		for (Iterator<Asteroid> i = asteroids.iterator(); i.hasNext();)
		{
			Asteroid a = i.next();
			a.Update(deltaTime);
		}
		if (asteroids.size() == 0)
		{
			if (!hasLastTime)
			{
				updateLevel();
				lastTime = SystemClock.uptimeMillis();
				hasLastTime = true;
			}
			curTime = SystemClock.uptimeMillis();
			if (curTime - lastTime > LEVEL_DELAY)
			{
				for (int i = 0; i < INITIAL_ASTEROIDS; i++)
				{
					Asteroid a = asteroidPool.alloc();
					a.Validate(r, INTIAL_WIDTH, INITAL_HEIGHT, maxSpeed);
					asteroids.add(a);
				}
				hasLastTime = false;
			}
		}
	}

	public void drawParticles() 
	{
		for (Asteroid a : asteroids)
		{
			a.Draw();
		}
	}
	
	private void updateLevel()
	{
		Level++;
		maxSpeed += SPEED_INCREMENT;
	}
	
	public LinkedList<Asteroid> getAsteroids()
	{
		return asteroids;
	}
	
	public AsteroidPool getAsteroidPool()
	{
		return asteroidPool;
	}
}
