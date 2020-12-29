package br.com.josedev.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import br.com.josedev.main.Game;

public class UI {
	public void render(Graphics g) {
		renderLife(g);
		renderAmmo(g);
	}

	private void renderLife(Graphics g) {
		int sizeOfLifeBar = 120;
		int lifePercentage = (int) ((Game.player.life / Game.player.maxLife) * sizeOfLifeBar);

		g.setColor(Color.red);
		g.fillRect(10, 4, sizeOfLifeBar, 20);

		g.setColor(Color.green);
		g.fillRect(10, 4, lifePercentage, 20);

		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 20));

		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxLife, 45, 21);
	}

	private void renderAmmo(Graphics g) {
		int renderXPosition = (Game.WIDTH * Game.SCALE) - 200;
		int renderYPosition = 20;

		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);

		g.drawString("Balas na agulha: " + Game.player.ammo, renderXPosition, renderYPosition);
	}

	public void renderGameOver(Graphics g, boolean toggleGameOver) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 00, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		g.setColor(Color.white);

		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("GAME OVER", (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 20);

		if (toggleGameOver) {
			g.setFont(new Font("arial", Font.BOLD, 25));
			g.drawString("> Pressione Enter para reiniciar <", (Game.WIDTH * Game.SCALE) / 2 - 180,
					(Game.HEIGHT * Game.SCALE) / 2 + 20);
		}
	}

	public void renderLoading(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 00, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		g.setColor(Color.white);

		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString("Carregando...", (Game.WIDTH * Game.SCALE) / 2 - 50, (Game.HEIGHT * Game.SCALE) / 2 - 20);
	}
}
