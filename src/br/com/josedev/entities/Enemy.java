package br.com.josedev.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import br.com.josedev.main.Game;
import br.com.josedev.main.Sound;
import br.com.josedev.world.*;

public class Enemy extends Entity {
	private double speed = 1;
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 1;
	private BufferedImage[] animmationSprites;
	
	private int life = 30;
	private boolean isDamage = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		animmationSprites = new BufferedImage[2];
		setAnimationSprites();
	}
	
	public void tick() {
		animate();
		verifyBulletCollision();
		
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		if(isColiddingWithPlayer()) {
			makeDamageInPlayer();
		} else {
			persuitPlayer();
		}
	}
	
	public void render(Graphics g) {
		if(isDamage) {
			g.drawImage(Entity.ENEMY_EN_DAMAGE, this.getX() - Camera.x, this.getY() - Camera.y,  null);
		} else {
			g.drawImage(animmationSprites[index], this.getX() - Camera.x, this.getY() - Camera.y,  null);
		}
	}
	
	private void setAnimationSprites() {
		int initalXPosition = 112;
		int incrementsXPosition = 0;
		
		for(int i = 0; i < animmationSprites.length; i++) {
			animmationSprites[i] = Game.spritesheet.getSprite(initalXPosition + incrementsXPosition, 16, 16, 16);
			incrementsXPosition = 16;
		}
	}
	
	private void animate() {
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
		
		if(isDamage) {
			damageCurrent++;
			if(damageCurrent == damageFrames) {
				damageCurrent = 0;
				isDamage = false;
			}
		}
	}
	
	private void makeDamageInPlayer() {
		int probabilityOfDamage = Game.rand.nextInt(100);
		boolean shouldMakeDamage = probabilityOfDamage < 10;
		
		if(shouldMakeDamage) {
			Game.player.life -= Game.rand.nextInt(5);
			Game.player.isDamaged = true;
			Sound.hurtEffect.play();
		}
	}
	
	private void persuitPlayer() {
		int probabilityOfPersuit = Game.rand.nextInt(100);
		boolean shouldPersuit = probabilityOfPersuit < 50;
		
		if(shouldPersuit) {
			if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
					&& !isColiddingWithFriends((int)(x+speed), this.getY())) {
				x+=speed;
			} else if ((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
					&& !isColiddingWithFriends((int)(x-speed), this.getY())) {
				x-=speed;
			} 
			
			if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
					&& !isColiddingWithFriends(this.getX(), (int)(y+speed))) {
				y+=speed;
			} else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
					&& !isColiddingWithFriends(this.getX(), (int)(y-speed))) {
				y-=speed;
			}
		}
	}
	
	private void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	private void verifyBulletCollision() {
		for (int i = 0; i < Game.bullets.size(); i++ ) {
			Bullet bs = Game.bullets.get(i);
			if(Entity.isColliding(this, bs)) {
				isDamage = true;
				life -= Bullet.DAMAGE;
				Game.bullets.remove(i);
				Sound.enemyHit.play();
				return;
			}
		}
	}
	
	private boolean isColiddingWithFriends(int xnext, int ynext) {
		Rectangle currentEnemy = new Rectangle(xnext, ynext, World.TILE_SIZE, World.TILE_SIZE);
		
		for(int i = 0; i< Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			
			Rectangle targetEmeny = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
			
			if(currentEnemy.intersects(targetEmeny)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isColiddingWithPlayer() {		
		return Entity.isColliding(this, Game.player);
	}

}
