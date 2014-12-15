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
import java.util.EnumSet;
import java.util.List;

/**
 * Represents coin with gold value.
 */
public class Coin extends SpriteObject implements IValuable, IObservable, IClickable {
    private enum CoinState {
        RISING,
        ROTATING,
        DISAPPEARING
    }

    private final ClickContainer clickContainer;
    private final List<ObservableListener> listeners = new ArrayList<ObservableListener>();
    private final GameScreen gameScreen;

    private int value;
    private int direction = 1;
    private final float baseScale;
    private EnumSet<CoinState> state;
    private Cooldown riseCooldown = new Cooldown(500);
    private Cooldown disappearCooldown = new Cooldown(5000);

    public Coin(GameScreen gameScreen, int value) {
        this.clickContainer = new ClickContainer(this);
        this.gameScreen = gameScreen;

        this.setGoldValue(value);
        this.baseScale = value / 500.0f;
        this.sprite.setScale(this.baseScale);

        this.setSpeed(100.0f);

        this.state = EnumSet.of(CoinState.RISING);
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
        this.disappearCooldown.update(delta);

        if (this.state.contains(CoinState.RISING)) {
            this.move(delta);

            if (this.riseCooldown.isReady()) {
                this.state.remove(CoinState.RISING);
                this.state.add(CoinState.ROTATING);
                this.disappearCooldown.reset();
            }
        }

        if (this.state.contains(CoinState.ROTATING)){
            if (this.sprite.getScaleX() <= -this.baseScale || this.sprite.getScaleX() >= this.baseScale) {
                this.direction *= -1;
            }

            this.sprite.setScale(this.sprite.getScaleX() + 0.01f * this.direction, this.baseScale);

            if (this.disappearCooldown.isReady()) {
                this.state.add(CoinState.DISAPPEARING);
            }
        }

        if (this.state.contains(CoinState.DISAPPEARING)) {
            this.setColor(this.getColor().mul(0.95f));

            if (this.getColor().a < 0.1f) {
                this.notifyRemove();
            }
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
