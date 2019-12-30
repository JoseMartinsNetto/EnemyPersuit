package br.com.josedev.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	public String[] options = {"Novo jogo", "Carregar jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		

		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 30));
		g.drawString("> ENEMY PERSUIT <",(Game.WIDTH*Game.SCALE) / 2 -140, (Game.HEIGHT*Game.SCALE) / 2 - 200);
		

		g.setFont(new Font("arial", Font.BOLD, 20));
		g.drawString(options[0],(Game.WIDTH*Game.SCALE) / 2 -50, (Game.HEIGHT*Game.SCALE) / 2 - 100);
		g.drawString(options[1],(Game.WIDTH*Game.SCALE) / 2 -65, (Game.HEIGHT*Game.SCALE) / 2 - 60);
		g.drawString(options[2],(Game.WIDTH*Game.SCALE) / 2 -20, (Game.HEIGHT*Game.SCALE) / 2 - 20);
		
		if(options[currentOption] == options[0]) {
			g.drawString("> ",(Game.WIDTH*Game.SCALE) / 2 -80, (Game.HEIGHT*Game.SCALE) / 2 - 100);
		} else if (options[currentOption] == options[1]) {
			g.drawString("> ",(Game.WIDTH*Game.SCALE) / 2 -90, (Game.HEIGHT*Game.SCALE) / 2 - 60);
		} else {
			g.drawString("> ",(Game.WIDTH*Game.SCALE) / 2 -40, (Game.HEIGHT*Game.SCALE) / 2 - 20);
		}
	}
}
