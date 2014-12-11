package cz.kobzol.turret.model.screen;

/**
 * Represents state of game.
 */
public class BuildState {
    private enum State {
        BUILDING,
        DEFENDING
    }

    private State state;

    public BuildState() {
        this.state = State.BUILDING;
    }

    public boolean isBuilding() {
        return this.state == State.BUILDING;
    }

    public void setBuilding() {
        this.state = State.BUILDING;
    }
    public void setDefending() {
        this.state = State.DEFENDING;
    }
}
