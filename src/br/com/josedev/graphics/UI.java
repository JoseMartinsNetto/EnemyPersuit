package br.com.josedev.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import br.com.josedev.main.Constants;
import br.com.josedev.main.Game;
import br.com.josedev.main.GameState;

public class UI {
	public void render(Graphics g, boolean shouldToggleEnemyCount) {
		if (Game.gameState == GameState.Normal) {
			renderLife(g);
			renderEnemyCount(g, shouldToggleEnemyCount);
			renderAmmo(g);
		}
	}

	private void renderLife(Graphics g) {
		int sizeOfLifeBar = 120;
		int lifePercentage = (int) ((Game.player.getLife() / Game.player.getMaxLife()) * sizeOfLifeBar);

		g.setColor(Color.red);
		g.fillRect(10, 4, sizeOfLifeBar, 20);

		g.setColor(Color.green);
		g.fillRect(10, 4, lifePercentage, 20);

		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 20));

		g.drawString((int) Game.player.getLife()  + "/" + (int) Game.player.getMaxLife(), 45, 21);
	}

	private void renderAmmo(Graphics g) {
		int renderXPosition = 10;
		int renderYPosition = 40;
		int ammo = Game.player.ammo;

		g.setFont(new Font("arial", Font.BOLD, 15));

		if (ammo > 5 && ammo < 10) {
			g.setColor(Color.white);
		}

		if (ammo < 5) {
			g.setColor(Color.RED);
		}

		if (ammo > 10) {
			g.setColor(Color.cyan);
		}

		if(Game.player.hasGun()) {
			g.drawString(Constants.Strings.UI.BulletsOnNeedle + ammo, renderXPosition, renderYPosition);
		} else {
			g.drawString(Constants.Strings.UI.BulletsInPocket + ammo, renderXPosition, renderYPosition);
		}
	}

	private void renderEnemyCount(Graphics g, boolean toggleable) {
		int renderXPosition = (Game.WIDTH * Game.SCALE) - 180;
		int renderYPosition = 20;

		g.setFont(new Font("arial", Font.BOLD, 15));
		g.setColor(Color.red);

		if(toggleable) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.white);
		}

		g.drawString(Constants.Strings.UI.AmountOfEnemies + Game.enemies.size(), renderXPosition, renderYPosition);

	}

	public void renderGameOver(Graphics g, boolean toggleGameOver) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		g.setColor(Color.white);

		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString(Constants.Strings.UI.GameOver, (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 20);

		if (toggleGameOver) {
			g.setFont(new Font("arial", Font.BOLD, 25));
			g.drawString(Constants.Strings.UI.RestartGame, (Game.WIDTH * Game.SCALE) / 2 - 180,
					(Game.HEIGHT * Game.SCALE) / 2 + 20);
		}
	}

	public void renderLoading(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		g.setColor(Color.white);

		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString(Constants.Strings.UI.Loading, (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 20);
	}
}
