package br.com.josedev.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	
	private AudioClip clip;
	public static final Sound musicBackGround = new Sound("/hadouken-theme.wav");
	public static final Sound hurtEffect = new Sound("/hurt.wav");
	public static final Sound playerShoot = new Sound("/shot.wav");
	public static final Sound lifePackRecovered = new Sound("/lifepack.wav");
	public static final Sound gunLoad = new Sound("/gun_load.wav");
	public static final Sound weaponRecovered = new Sound("/weapon-recovered.wav");
	public static final Sound enemyHit = new Sound("/enemy-hit.wav");
	public static final Sound playerWalk = new Sound("/player-walk.wav");
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		
		try {
			new Thread () {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	public void loop() {
			
			try {
				new Thread () {
					public void run() {
						clip.loop();
					}
				}.start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
		}

}
