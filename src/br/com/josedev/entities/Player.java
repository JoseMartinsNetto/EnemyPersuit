package br.com.josedev.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import br.com.josedev.main.Game;
import br.com.josedev.world.*;

public class Player extends Entity{
	public boolean up, down, left, rigth = false;
	
	public double speed = 1.4;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private boolean moved = false;
	
	public static double life = 100, maxLife = 100;
	
	public int ammo = 0;
	
	public Player (int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		
		for (int i = 0; i< 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		
		for (int i = 0; i< 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
	}
	
	public void tick() {
		moved = false;
		
		if (up && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int)(y+speed))) {
			moved = true;
			y += speed;
		}
		
		if (rigth && World.isFree((int)(x+speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		checkColisionLifePack();
		checkColisionLifeAmmo();
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkColisionLifeAmmo() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);
			
			if(Entity.isColliding(this, bullet)) {
				ammo += Bullet.ammoSize;
				
				if(ammo > Bullet.maxAmmo) {
					ammo = Bullet.maxAmmo;
				}
				
				Game.bullets.remove(bullet);
				Game.entities.remove(bullet);
					
			}
		}
	}
	
	public void checkColisionLifePack() {
		for (int i = 0; i < Game.lifepacks.size(); i++) {
			Lifepack lifepack = Game.lifepacks.get(i);
			
			if(Entity.isColliding(this, lifepack)) {
				life += Lifepack.lifepackSize;
				
				if(Player.life > Player.maxLife) {
					Player.life = Player.maxLife;
				}
				
				Game.lifepacks.remove(lifepack);
				Game.entities.remove(lifepack);
					
			}
		}
		
	}
	
	public void render(Graphics g) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
