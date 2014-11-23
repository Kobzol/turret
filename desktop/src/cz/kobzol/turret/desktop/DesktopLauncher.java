package cz.kobzol.turret.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import cz.kobzol.turret.GameLoop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.foregroundFPS = 60;
        config.backgroundFPS = 60;
        config.width = 1200;
        config.height = 600;

		new LwjglApplication(new GameLoop(), config);
	}
}
