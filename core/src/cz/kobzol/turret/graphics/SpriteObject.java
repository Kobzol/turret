package cz.kobzol.turret.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.Dimension;

/**
 * Represents Bulanek.
 */
public abstract class SpriteObject extends DrawableShape implements IMovable, IRotable, ICollidable, IUpdatable {
    protected Sprite sprite;
    protected Vector2 direction;
    protected Vector2 moveDirection;
    protected float speed;
    protected Color color;

    public SpriteObject() {
        this.sprite = new Sprite();
        this.color = new Color(Color.WHITE);
        this.direction = new Vector2(0, 1);
        this.moveDirection = this.direction.cpy();
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
    public void render(Batch batch, Camera camera) {
        Color color = batch.getColor();

        batch.setColor(this.color);

        batch.draw(
                this.sprite, this.sprite.getX() - this.sprite.getWidth() / 2, this.sprite.getY() - this.sprite.getHeight() / 2,
                this.sprite.getOriginX(), this.sprite.getOriginY(),
                this.sprite.getWidth(), this.sprite.getHeight(),
                this.sprite.getScaleX(), this.sprite.getScaleY(), this.sprite.getRotation());

        batch.setColor(color);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }
    public Color getColor() {
        return new Color(this.color);
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {
        //shapeRenderer.rect(this.getBoundingBox().x, this.getBoundingBox().y, this.getBoundingBox().width, this.getBoundingBox().height);
    }

    @Override
    public void move(float delta) {
        float speed = this.getSpeed() * delta;

        Vector2 move_vector = this.getMoveDirection();

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
    public Vector2 getMoveDirection() {
        return this.moveDirection.cpy();
    }

    @Override
    public void setMoveDirection(Vector2 moveDirection) {
        this.moveDirection.set(moveDirection);
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

    public void setOrigin(int x, int y) {
        this.sprite.setOrigin(x, y);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public Object clone() {
        SpriteObject obj = (SpriteObject) super.clone();
        obj.sprite = new Sprite();

        Vector2 origin = new Vector2(this.sprite.getOriginX(), this.sprite.getOriginY());
        Dimension originalDimension = this.getDimension();

        if (this.texture != null) {
            obj.setTexture(new Texture(this.getTexture().getTextureData()));
        }

        obj.position = this.getPosition().cpy();
        obj.direction = this.getDirection().cpy();
        obj.moveDirection = this.moveDirection.cpy();
        obj.dimension = originalDimension;
        obj.setDimension(obj.dimension);

        this.setDimension(originalDimension);

        obj.setOrigin((int) origin.x, (int) origin.y);

        return obj;
    }
}
