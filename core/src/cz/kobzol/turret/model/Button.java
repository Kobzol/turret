package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.ClickContainer;
import cz.kobzol.turret.input.click.IClickable;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
    Represents button.
 */
public class Button extends SpriteObject implements IClickable {
    private final ClickContainer clickContainer;

    private final BitmapFont font;
    private final OnClickListener listener;
    private String text;

    public Button(String text, Color color, OnClickListener listener) {
        this.clickContainer = new ClickContainer(this);

        this.text = text;

        this.font = new BitmapFont(Locator.getAssetContainer().getAssetManager().get(AssetContainer.FONT_ARIAL, BitmapFont.class).getData(), (TextureRegion) null, true);
        this.font.setColor(color);
        BitmapFont.TextBounds bounds =  this.font.getBounds(text);
        this.setDimension(new Dimension((int) bounds.width, (int) bounds.height));

        this.listener = listener;
    }

    @Override
    public void render(Batch batch, Camera camera) {
        this.font.draw(batch, this.text, this.getPosition().x - this.getDimension().width / 2, this.getPosition().y + this.getDimension().height / 2);
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
        this.listener.onClick();
    }

    public static interface OnClickListener {
        void onClick();
    }
}
