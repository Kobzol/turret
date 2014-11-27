package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.input.drag.IDraggable;
import cz.kobzol.turret.input.drag.IDroppable;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Field with many slots.
 */
public final class Field extends GameObject implements IDrawable, IDroppable, IUpdatable {
    private final Screen screen;

    private final Vector2 startPosition;
    private final Dimension size;
    private final Slot templateSlot;

    private final List<List<Slot>> slots = new ArrayList<List<Slot>>();

    public Field(Screen screen, Vector2 startPosition, Dimension size, Slot templateSlot) {
        this.screen = screen;

        this.startPosition = startPosition;
        this.size = size;
        this.templateSlot = templateSlot;

        this.createSlots();
    }

    private void createSlots() {
        Vector2 movedStart = new Vector2(this.startPosition.x + this.templateSlot.getDimension().width / 2, this.startPosition.y + this.templateSlot.getDimension().height / 2);

        for (int height = 0; height < size.height; height++) {
            this.slots.add(new ArrayList<Slot>());

            for (int width = 0; width < size.width; width++) {
                Slot currentSlot = (Slot) this.templateSlot.clone();
                currentSlot.setPosition(new Vector2(movedStart.x + (width * this.templateSlot.getDimension().width), movedStart.y + (height * this.templateSlot.getDimension().height)));

                this.slots.get(height).add(currentSlot);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        for (List<Slot> slotRow : this.slots) {
            for (Slot slot : slotRow) {
                slot.draw(batch);
            }
        }
    }

    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
        for (List<Slot> slotRow : this.slots) {
            for (Slot slot : slotRow) {
                slot.drawShape(shapeRenderer);
            }
        }
    }

    @Override
    public void update() {
        for (List<Slot> slotRow : this.slots) {
            for (Slot slot : slotRow) {
                slot.update();
            }
        }
    }

    @Override
    public boolean receiveObject(IDraggable draggableObject) {
        for (List<Slot> slotRow : this.slots) {
            for (Slot slot : slotRow) {
                if (slot.getBoundingBox().contains(draggableObject.getPosition())) {
                    return slot.receiveObject(draggableObject);
                }
            }
        }

        return false;
    }
}
