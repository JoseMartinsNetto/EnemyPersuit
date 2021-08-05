package br.com.josedev.entities;

import br.com.josedev.main.*;
import br.com.josedev.world.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
	public boolean isDamaged = false;
	public boolean isShooting = false;

	public double speed;
	private final double normalSpeed = 0.8;
	private final double runningSpeed = 1.2;

	public int rightDir = 0, leftDir = 1;
	public int dir = rightDir;
	public int ammo = 0;
	
	private int animationFrames = 0;
	private final int maxAnimationFrames = 5;
	private int indexAnimation = 0;
	private final int maxIndexAnimation = 3;
	private int walkCounter = 0;
	private final int walkCounterLimit = 20;
	private int damagedFrames = 0;
	private double life = 100;
	private final double maxLife = 100;
	
	private boolean moved = false;
	private boolean hasGun = false;

	private BufferedImage[] rightAnimationPlayer;
	private BufferedImage[] leftAnimationPlayer;
	private BufferedImage damagedPlayer;


	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		setupAnimations();
		setMask(3,0, 10,16);
	}
	
	public double getLife() {
		return life;
	}

	public boolean hasGun() {
		return hasGun;
	}
	
	public double getMaxLife() {
		return maxLife;
	}

	public void update() {
		animateMove();
		animateDamage();
		shootingCheck();
		checkCollisionWithLifePack();
		checkCollisionLifeWithAmmunition();
		checkCollisionWithWeapon();
		Game.setStateForPlayerLife(life);
		updateCamera();
	}
	
	public void render(Graphics g) {

		if (isDamaged) {
			g.drawImage(damagedPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
			if (hasGun) {
				if (dir == rightDir) {
					g.drawImage(Entity.WEAPON_DAMAGE_RIGHT, this.getX() + 6 - Camera.x, this.getY() - Camera.y, null);
				} else {
					g.drawImage(Entity.WEAPON_DAMAGE_LEFT, this.getX() - 6 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		} else {
			if (dir == rightDir) {
				g.drawImage(rightAnimationPlayer[indexAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.WEAPON_RIGHT, this.getX() + 6 - Camera.x, this.getY() - Camera.y, null);
				}
			} else if (dir == leftDir) {
				g.drawImage(leftAnimationPlayer[indexAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.WEAPON_LEFT, this.getX() - 6 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
	}
	
	public void receiveDamage(int damage) {
		life -= damage;
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	private void setupAnimations() {
		rightAnimationPlayer = new BufferedImage[4];
		leftAnimationPlayer = new BufferedImage[4];

		damagedPlayer = Game.spritesheet.getSprite(0, 16, 16, 16);

		for (int i = 0; i < 4; i++) {
			rightAnimationPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftAnimationPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}
	
	private void animateMove() {
		moved = false;

		if (Input.Shift()) {
			speed = runningSpeed;
		} else {
			speed = normalSpeed;
		}

		if (Input.Up() && World.isFree(this.getX(), (int) (y - normalSpeed))) {
			moved = true;
			y -= speed;
		} else if (Input.Down() && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
		}

		if (Input.Right() && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = rightDir;
			x += speed;
		} else if (Input.Left() && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = leftDir;
			x -= speed;
		}

		if (moved) {

			walkCounter++;
			if (walkCounter == walkCounterLimit / 2) {
				if (Sound.playerWalk != null) {
					Sound.playerWalk.play();
				}
			}
			if (walkCounter == walkCounterLimit) {
				walkCounter = 0;
			}

			animationFrames++;
			if (animationFrames == maxAnimationFrames) {
				animationFrames = 0;
				indexAnimation++;
				if (indexAnimation > maxIndexAnimation) {
					indexAnimation = 0;
				}
			}
		}
	}
	
	private void animateDamage() {
		if (isDamaged) {
			damagedFrames++;
			if (damagedFrames == 3) {
				damagedFrames = 0;
				isDamaged = false;
			}
		}
	}
	
	private void shootingCheck() {

		if (isShooting) {
			isShooting = false;
			if (hasGun && ammo > 0) {
				ammo--;

				int dx;
				int px;
				int py = 6;

				if (dir == rightDir) {
					px = 18;
					dx = 1;
				} else {
					px = -8;
					dx = -1;
				}

				Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);

				Game.bullets.add(bullet);
				if (Sound.playerShoot != null) {
					Sound.playerShoot.play();
				}

			}
		}
	}

	private void checkCollisionLifeWithAmmunition() {
		for (int i = 0; i < Game.ammunition.size(); i++) {
			Ammunition currentAmmo = Game.ammunition.get(i);

			if (Entity.isColliding(this, currentAmmo)) {
				ammo += Ammunition.ammunitionSize;

				/*
				 * if(ammo > Ammunition.maxAmmunition) { ammo = Ammunition.maxAmmunition; }
				 */
				if (Sound.gunLoad != null) {
					Sound.gunLoad.play();
				}

				Game.ammunition.remove(currentAmmo);
				Game.entities.remove(currentAmmo);

			}
		}
	}

	private void checkCollisionWithLifePack() {
		for (int i = 0; i < Game.lifepacks.size(); i++) {
			Lifepack lifepack = Game.lifepacks.get(i);

			if (Entity.isColliding(this, lifepack)) {
				life += Lifepack.lifepackSize;

				if (Game.player.life > Game.player.maxLife) {
					Game.player.life = Game.player.maxLife;
				}

				if (Sound.lifePackRecovered != null) {
					Sound.lifePackRecovered.play();
				}

				Game.lifepacks.remove(lifepack);
				Game.entities.remove(lifepack);

			}
		}

	}

	private void checkCollisionWithWeapon() {
		for (int i = 0; i < Game.weapons.size(); i++) {
			Weapon weapon = Game.weapons.get(i);

			if (Entity.isColliding(this, weapon)) {
				hasGun = true;
				if (Sound.weaponRecovered != null) {
					Sound.weaponRecovered.play();
				}

				Game.weapons.remove(weapon);
				Game.entities.remove(weapon);

			}
		}

	}

}
