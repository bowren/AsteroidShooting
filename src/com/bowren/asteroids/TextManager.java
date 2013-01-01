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

import android.opengl.GLES11;

public class TextManager 
{
	private TextRenderer textRend;
	private Text Score;
	private Text Level;
	private Text Lose;
	private Text highScore;
	
	public TextManager(TextRenderer tr, float ratio)
	{
		textRend = tr;
		Score = new Text("SCORE 0", ratio * -0.55f, 1.65f, 0.35f, 0.35f);
		Level = new Text("LEVEL 1", ratio * -2, 1.65f, 0.35f, 0.35f);
		Lose = new Text("", 0 - ((7.5f * 0.35f) / 2), 0 - (0.35f / 2), 0.35f, 0.35f);
		highScore = new Text("HIGH ", ratio * -0.55f, 1.3f, 0.35f, 0.35f);
	}
	
	public void Update(int points, int level, float lives, int hScore)
	{
		Score.text = "SCORE " + String.valueOf(points);
		Level.text = "LEVEL " + String.valueOf(level);
		highScore.text = "HIGH " + String.valueOf(hScore);
		if (lives <= 0)
			Lose.setText("GAME OVER");
		else
			Lose.setText("");
	}
	
	public void Draw()
	{
		textRend.DrawText(Score);
		textRend.DrawText(Level);
		GLES11.glColor4f(0, 1, 0, 1);
		textRend.DrawText(highScore);
		GLES11.glColor4f(1, 1, 1, 1);
		if (Lose.text != "")
			textRend.DrawText(Lose);
	}
}
