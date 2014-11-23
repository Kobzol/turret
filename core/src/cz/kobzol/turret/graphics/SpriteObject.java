package cz.kobzol.turret.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.DrawableShape;
import cz.kobzol.turret.graphics.ICollidable;
import cz.kobzol.turret.graphics.IMovable;
import cz.kobzol.turret.graphics.IRotable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.model.GameObject;

import java.awt.Dimension;

/**
 * Represents Bulanek.
 */
public abstract class SpriteObject extends DrawableShape implements IMovable, IRotable, ICollidable, IUpdatable {
    protected Sprite sprite;
    protected Vector2 direction;
    protected float speed;

    public SpriteObject() {
        this.sprite = new Sprite();
        this.direction = new Vector2(0, -1);
    }

    public float getSpeed() {
        return this.speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void setTexture(Texture texture) {
        super.setTexture(texture);

        this.sprite.setTexture(texture);
        this.sprite.setRegion(texture);
        this.setDimension(new Dimension(texture.getWidth(), texture.getHeight()));
    }

    @Override
    public void setDimension(Dimension dimension) {
        super.setDimension(dimension);

        this.sprite.setBounds(this.sprite.getX(), this.sprite.getY(), (float) dimension.getWidth(), (float) dimension.getHeight());
        this.sprite.setOrigin((float) dimension.getWidth() / 2.0f, (float) dimension.getHeight() / 2.0f);
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        this.sprite.setPosition(position.x, position.y);
    }

    @Override
    public void draw(Batch batch) {
            batch.draw(
                    this.sprite, this.sprite.getX() - this.sprite.getOriginX(), this.sprite.getY()  - this.sprite.getOriginY(),
                    this.sprite.getOriginX(), this.sprite.getOriginY(),
                    this.sprite.getWidth(), this.sprite.getHeight(),
                    this.sprite.getScaleX(), this.sprite.getScaleY(), this.sprite.getRotation());
    }

    public void drawShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(this.getBoundingBox().x, this.getBoundingBox().y, this.getBoundingBox().width, this.getBoundingBox().height);
    }

    @Override
    public void move() {
        float speed = this.speed;

        Vector2 move_vector = new Vector2(this.direction);

        this.setPosition(this.getPosition().add(move_vector.scl(speed)));
    }

    @Override
    public float getRotation() {
        return this.direction.angle();
    }

    @Override
    public void setRotation(float angle) {
        this.direction.setAngle(angle);
        this.sprite.setRotation(this.direction.angle() - 90);
    }

    @Override
    public Vector2 getDirection() {
        return new Vector2(this.direction);
    }

    @Override
    public void setDirection(Vector2 direction) {
        this.direction.set(direction);
        this.setRotation(this.direction.angle());
    }

    @Override
    public void rotate(float angle) {
        this.setRotation(this.getRotation() + angle);
    }

    @Override
    public boolean collidesWith(ICollidable collidable) {
        return this.getBoundingBox().overlaps(collidable.getBoundingBox());
    }

    @Override
    public Rectangle getBoundingBox() {
        Rectangle rect = this.sprite.getBoundingRectangle();
        rect.x -= this.getDimension().getWidth() / 2.0f;
        rect.y -= this.getDimension().getHeight() / 2.0f;

        return rect;
    }

    @Override
    public void update() {

    }

    @Override
    public Object clone() {
        SpriteObject obj = (SpriteObject) super.clone();
        obj.sprite = new Sprite();
        obj.setTexture(new Texture(this.getTexture().getTextureData()));
        obj.position = new Vector2(this.getPosition());
        obj.direction = new Vector2(this.getDirection());
        obj.dimension = (Dimension) this.getDimension().clone();

        return obj;
    }
}