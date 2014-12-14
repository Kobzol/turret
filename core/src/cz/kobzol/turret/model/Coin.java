package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.ClickContainer;
import cz.kobzol.turret.input.click.IClickable;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.gold.IValuable;
import cz.kobzol.turret.model.screen.GameScreen;
import cz.kobzol.turret.util.Cooldown;
import cz.kobzol.turret.util.IObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents coin with gold value.
 */
public class Coin extends SpriteObject implements IValuable, IObservable, IClickable {
    private enum CoinState {
        RISING,
        ROTATING
    }

    private final ClickContainer clickContainer;
    private final List<ObservableListener> listeners = new ArrayList<ObservableListener>();
    private final GameScreen gameScreen;

    private int value;
    private int direction = 1;
    private final float baseScale;
    private CoinState state;
    private Cooldown riseCooldown = new Cooldown(500);

    public Coin(GameScreen gameScreen, int value) {
        this.clickContainer = new ClickContainer(this);
        this.gameScreen = gameScreen;

        this.setGoldValue(value);
        this.baseScale = value / 500.0f;
        this.sprite.setScale(this.baseScale);

        this.setSpeed(100.0f);

        this.state = CoinState.RISING;
    }

    private void notifyRemove() {
        for (ObservableListener listener : this.listeners) {
            listener.onRemove(this);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        this.riseCooldown.update(delta);

        if (this.state.equals(CoinState.RISING)) {
            this.move(delta);

            if (this.riseCooldown.isReady()) {
                this.state = CoinState.ROTATING;
            }
        }
        else {
            if (this.sprite.getScaleX() <= -this.baseScale || this.sprite.getScaleX() >= this.baseScale) {
                this.direction *= -1;
            }

            this.sprite.setScale(this.sprite.getScaleX() + 0.01f * this.direction, this.baseScale);
        }
    }

    @Override
    public void addListener(ObservableListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public int getGoldValue() {
        return this.value;
    }

    @Override
    public void setGoldValue(int value) {
        this.value = value;
    }

    @Override
    public ClickContainer getClickContainer() {
        return this.clickContainer;
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void onClick(MouseState mouseState) {
        this.gameScreen.deposit(this);
        this.notifyRemove();
    }
}
