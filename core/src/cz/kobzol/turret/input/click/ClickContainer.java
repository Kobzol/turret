package cz.kobzol.turret.input.click;

/**
 * Represents the draggable state of an object.
 */
public class ClickContainer {
    public enum ClickState {
        WAITING,
        READY_TO_CLICK,
        MOUSE_HOLD
    }

    private final IClickable clickableObject;
    private ClickState clickState;

    public ClickContainer(IClickable draggedObject) {
        this.clickableObject = draggedObject;
        this.clickState = ClickState.WAITING;
    }

    public void performClick() {
        this.clickableObject.onClick();
        this.setReadyToClick();
    }

    public ClickState getClickState() {
        return this.clickState;
    }

    public void setWaiting() {
        this.clickState = ClickState.WAITING;
    }
    public void setReadyToClick() {
        this.clickState = ClickState.READY_TO_CLICK;
    }
    public void setMouseHold() {
        this.clickState = ClickState.MOUSE_HOLD;
    }
}
