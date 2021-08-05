package br.com.josedev.main;

import br.com.josedev.entities.*;
import br.com.josedev.graphics.Spritesheet;
import br.com.josedev.graphics.UI;
import br.com.josedev.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener {	
	public static JFrame frame;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	public static String curLevelName;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Lifepack> lifepacks;
	public static List<Bullet> bullets;
	public static List<Weapon> weapons;
	public static List<Ammunition> ammunition;
	
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;

	public Menu menu;
	public UI ui;

	private static GameState gameState = GameState.Loading;
	private static final long serialVersionUID = 1L;
	private static double waitTime = 0;
	private static final double waitTimeLimit = 300.0;
	
	private int CUR_LEVEL = 1;
	private final int MAX_LEVEL = 2;
	private int framesOfToggleable = 0;
	private boolean toggleable = true;
	private boolean isRunning = false;
	private boolean restartGame = false;
	
	private Thread thread;
	private final BufferedImage image;

	public static GameState getState() { return gameState; }

	public static void setStateForPlayerLife(double life) {
		if (life <= 0 ) {
			gameState = GameState.GameOver;
		}
	}

	public static void setPlayingState() {
		gameState = GameState.Normal;
	}

	public Game() {
		rand = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		ui = new UI();
		menu = new Menu();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		initGameLevel("level1.png");
	}

	public static void main(String[] args) {
		new Game().start();
	}

	public static void initGameLevel(String level) {
		curLevelName = level;
		
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		lifepacks = new ArrayList<>();
		bullets = new ArrayList<>();
		weapons = new ArrayList<>();
		ammunition = new ArrayList<>();

		spritesheet = new Spritesheet("/spritesheet.png");
		
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		
		world = new World("/" + level);
		player.updateCamera();
		waitTime = 0.0;
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void initFrame() {
		frame = new JFrame("Enemy Pursuit");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void update() {

		if (gameState == GameState.Normal) {
			restartGame = false;

			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).update();
			}

			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).update();
			}

			if (enemies.size() == 0) {
				CUR_LEVEL++;
				
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
					
					gameState = GameState.Menu;
				}

				String newWorld = "level" + CUR_LEVEL + ".png";
				
				curLevelName = newWorld;

				initGameLevel(newWorld);
			}
		} else if (gameState == GameState.GameOver) {

			if (restartGame) {
				gameState = GameState.Normal;
				
				initGameLevel(curLevelName);
			}
			
		} else if (gameState == GameState.Menu) {
			menu.update();
		}

		framesOfToggleable++;

		if (framesOfToggleable == 30) {
			framesOfToggleable = 0;
			toggleable = !toggleable;
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Rendering the items of the game
		world.render(g);

		for (Entity entity : entities) {
			entity.render(g);
		}

		for (Bullet bullet : bullets) {
			bullet.render(g);
		}

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		ui.render(g, toggleable);

		if (gameState == GameState.Loading) {
			ui.renderLoading(g);
		} else if (gameState == GameState.GameOver) {
			ui.renderGameOver(g, toggleable);
		} else if (gameState == GameState.Menu) {
			menu.render(g);
		}

		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		int frames = 0;
		
		double amountUpdates = 60.0;
		double minFrameUpdates = 55.0;
		double ns = 1000000000 / amountUpdates;
		double timer = System.currentTimeMillis();
		double delta = 0;

		boolean firstRun = true;
		
		requestFocus();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				update();
				render();
				frames++;
				delta--;
				waitTime ++;
				
				if(waitTime >= waitTimeLimit) {
					waitTime = waitTimeLimit;
				}
				
			}
			

			if ((frames >= minFrameUpdates && frames <= amountUpdates) && firstRun && waitTime >= waitTimeLimit) {
				gameState = GameState.Menu;
				
				firstRun = false;
				if (Sound.musicBackground != null) {
					Sound.musicBackground.loop(0.8f);
				}
			}

			if (System.currentTimeMillis() - timer >= 1000 || frames >= amountUpdates) {
				Debug.log("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}

		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Input.setPressed(e.getKeyCode());

		if (Input.Enter()) {
			restartGame = true;

			if (gameState == GameState.Menu) {
				menu.enter = true;
			}
		}

		if (Input.Escape()) {
			if (gameState == GameState.Normal) {
				gameState = GameState.Menu;
				menu.pause = true;
				if (Sound.musicBackground != null) {
					Sound.musicBackground.pause();
				}
			}
		}

		if (Input.Space()) {
			player.isShooting = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		Input.setReleased(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) { }

}
