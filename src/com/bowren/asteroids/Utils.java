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

import android.media.AudioManager;

public class Utils 
{
	public final static float DEG_TO_RAD = (float)Math.PI / 180.0f;
	public final static int DISCARD_Z = -6;
	public final static int DISCARD_Z_SCALE = 1;
	public final static int SHOOT_SOUND = 0;
	public final static int EXPLOSION_SOUND = 1;
	public final static int THRUST_SOUND = 2;
	
	private static int streamId = 0;
	
	public static void playSound(AsteroidsActivity context, int soundIndex)
	{
		float actualVolume = (float) context.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) context.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		context.soundPool.play(context.sounds[soundIndex], volume, volume, 1, 0, 1f);
	}
	
	public static void playSoundContinuous(AsteroidsActivity context, int soundIndex)
	{
		float actualVolume = (float) context.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) context.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		streamId = context.soundPool.play(context.sounds[soundIndex], volume, volume, 1, -1, 1f);
	}
	
	public static void stopSound(AsteroidsActivity context, int soundIndex)
	{
		context.soundPool.stop(streamId);
	}
}
