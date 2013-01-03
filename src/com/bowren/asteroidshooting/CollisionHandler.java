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

import java.util.Iterator;
import java.util.LinkedList;

public class CollisionHandler 
{
	private final static float OFFSET = 0.15f;
	private final static float SHIP_SPAWN_DIMENSIONS = 1.5f;
	
	public int Score = 0;
	public int highScore = 0;
	public Rectangle shipSpawn;
	private AsteroidsActivity context;
	private Ship ship;
	private LinkedList<Asteroid> asteroids;
	private LinkedList<Bullet> bullets;
	private AsteroidPool astPool;
	private BulletPool bulletPool;
	
	public CollisionHandler(AsteroidsActivity c, Ship s, LinkedList<Asteroid> a, LinkedList<Bullet> b, AsteroidPool ap, BulletPool bp)
	{
		context = c;
		ship = s;
		asteroids = a;
		bullets = b;
		astPool = ap;
		bulletPool = bp;
		shipSpawn = new Rectangle();
		shipSpawn.setInitial(new Vector2D(0 - (SHIP_SPAWN_DIMENSIONS / 2), 0 - (SHIP_SPAWN_DIMENSIONS / 2)), SHIP_SPAWN_DIMENSIONS, SHIP_SPAWN_DIMENSIONS);
		if (context.highScoreString != null)
			highScore =  Integer.parseInt(context.highScoreString);
	}
	
	public void checkCollisions()
	{
		for (Iterator<Asteroid> iterator = asteroids.iterator(); iterator.hasNext();)
		{
			Asteroid ast = iterator.next();
			if (checkLooseCollision(ship.rect, ast.rect) && ship.state != Ship.DEAD)
			{
				if (ship.state == Ship.STATIONARY || ship.state == Ship.THRUSTING)
				{
					//cannot use a switch on floats...
					if (ast.width == AsteroidManager.LARGE_ASTEROID_SIZE)
						Score += AsteroidManager.LARGE_ASTEROID_POINT;
					else if (ast.width == AsteroidManager.MEDIUM_ASTEROID_SIZE)
						Score += AsteroidManager.MEDIUM_ASTEROID_POINT;
					else if (ast.width == AsteroidManager.SMALL_ASTEROID_SIZE)
						Score += AsteroidManager.SMALL_ASTEROID_POINT;
					
					Utils.playSound(context, Utils.EXPLOSION_SOUND);
					ship.state = Ship.EXPLODING;
					ast.inValidate();
					asteroids.remove(ast);
					astPool.free(ast);
					iterator = asteroids.iterator();
				}
			}
		}
		for (Iterator<Bullet> bi = bullets.iterator(); bi.hasNext();)
		{
			Bullet b = bi.next();
			for (Iterator<Asteroid> asti = asteroids.iterator(); asti.hasNext();)
			{
				Asteroid a = asti.next();
				if (checkAccurateCollision(b.rect, a.rect))
				{
					if (a.width > AsteroidManager.SMALL_ASTEROID_SIZE)
					{
						Asteroid a1 = astPool.alloc();
						Asteroid a2 = astPool.alloc();
						float factor = 1;
						if (a.velocityX > a.velocityY)
							factor = a.velocityX * 10;
						else
							factor = a.velocityY * 10;
						a1.Validate(a.vecPos.x - (a.velocityX / factor), a.vecPos.y + (a.velocityY / factor), -a.velocityX, a.velocityY, a.rotateSpeed, a.width / 2, a.height / 2);
						a2.Validate(a.vecPos.x + (a.velocityX / factor), a.vecPos.y - (a.velocityY / factor), a.velocityX, -a.velocityY, a.rotateSpeed, a.width / 2, a.height / 2);
						asteroids.add(a1);
						asteroids.add(a2);
					}
					//cannot use a switch on floats...
					if (a.width == AsteroidManager.LARGE_ASTEROID_SIZE)
						Score += AsteroidManager.LARGE_ASTEROID_POINT;
					else if (a.width == AsteroidManager.MEDIUM_ASTEROID_SIZE)
						Score += AsteroidManager.MEDIUM_ASTEROID_POINT;
					else if (a.width == AsteroidManager.SMALL_ASTEROID_SIZE)
						Score += AsteroidManager.SMALL_ASTEROID_POINT;
					
					a.inValidate();
					asteroids.remove(a);
					astPool.free(a);
					b.inValidate();
					bullets.remove(b);
					bulletPool.free(b);
					bi = bullets.iterator();
					break;
				}
			}
		}
		if (ship.state == Ship.DEAD)
		{
			boolean astInBounds = false;
			for (Asteroid ast : asteroids)
			{
				if (checkAccurateCollision(shipSpawn, ast.rect))
					astInBounds = true;
			}
			if (astInBounds)
				ship.spawnSafe = false;
			else
				ship.spawnSafe = true;
		}
		if (highScore < Score)
			highScore = Score;
	}
	
	/* Loose and Accurate are relative
	   to the bounding boxes, not the sprites. */
	private boolean checkLooseCollision(Rectangle first, Rectangle second)
	{
		if ((first.left + OFFSET) < (second.right - OFFSET) && (first.right - OFFSET) > (second.left + OFFSET))
		{
			if ((first.top - OFFSET) > (second.bottom + OFFSET) && (first.bottom + OFFSET) < (second.top - OFFSET))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean checkAccurateCollision(Rectangle first, Rectangle second)
	{
		if (first.left < second.right && first.right > second.left)
		{
			if (first.top > second.bottom && first.bottom < second.top)
			{
				return true;
			}
		}
		return false;
	}
}
