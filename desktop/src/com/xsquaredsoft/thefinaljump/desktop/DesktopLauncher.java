package com.xsquaredsoft.thefinaljump.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xsquaredsoft.thefinaljump.GameMain;
import com.xsquaredsoft.thefinaljump.PlayServices;

import helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = GameInfo.WIDTH;
		config.height = GameInfo.HEIGHT;

		new LwjglApplication(new GameMain(new PlayServices() {
			@Override
			public void signIn() {

			}

			@Override
			public void signOut() {

			}

			@Override
			public void rateGame() {

			}

			@Override
			public void unlockAchievement(String id) {

			}

			@Override
			public void submitScore(int highScore) {

			}

			@Override
			public void showAchievement() {

			}

			@Override
			public void showScore() {

			}

			@Override
			public boolean isSignedIn() {
				return false;
			}
		}), config);
	}
}
