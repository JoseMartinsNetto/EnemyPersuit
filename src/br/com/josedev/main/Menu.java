package br.com.josedev.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	public String[] options = {
			Constants.Strings.UI.NewGame,
			Constants.Strings.UI.LoadGame,
			Constants.Strings.UI.Exit
	};

	public int currentOption = 0;
	public int maxOption = options.length - 1;

	public boolean up, down, enter, pause;

	public void update() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}

		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}

		if (enter) {
			enter = false;
			if (options[currentOption].equals(options[0])) {
				Game.gameState = GameState.Normal;
				pause = false;

				if (Sound.musicBackground != null) {
					Sound.musicBackground.resume();
				}
			} else if (options[currentOption].equals(options[2])) {
				System.exit(1);
			}
		}

	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString(Constants.Strings.UI.GameName, (Game.WIDTH * Game.SCALE) / 2 - 140, (Game.HEIGHT * Game.SCALE) / 2 - 200);

		g.setFont(new Font("arial", Font.BOLD, 20));

		if (!pause) {
			g.drawString(options[0], (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		} else {
			g.drawString(Constants.Strings.UI.Continue, (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		}

		g.drawString(options[1], (Game.WIDTH * Game.SCALE) / 2 - 65, (Game.HEIGHT * Game.SCALE) / 2 - 60);
		g.drawString(options[2], (Game.WIDTH * Game.SCALE) / 2 - 20, (Game.HEIGHT * Game.SCALE) / 2 - 20);

		if (options[currentOption].equals(options[0])) {
			g.drawString("> ", (Game.WIDTH * Game.SCALE) / 2 - 80, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		} else if (options[currentOption].equals(options[1])) {
			g.drawString("> ", (Game.WIDTH * Game.SCALE) / 2 - 90, (Game.HEIGHT * Game.SCALE) / 2 - 60);
		} else {
			g.drawString("> ", (Game.WIDTH * Game.SCALE) / 2 - 40, (Game.HEIGHT * Game.SCALE) / 2 - 20);
		}
	}
}
