package cz.kobzol.turret;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class GameLoop extends ApplicationAdapter {
    Game game;
	
	@Override
	public void create () {
        this.game = new Game();
        this.game.start();
	}

	@Override
	public void render() {
        this.game.update(Gdx.graphics.getDeltaTime());
	}

    @Override
    public void dispose() {
        this.game.dispose();
    }
}
