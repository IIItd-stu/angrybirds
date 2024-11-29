package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

public class BirdState {
    private Vector2 position;
    private int hitPoints;

    // Constructor
    public BirdState(Vector2 position, int hitPoints) {
        this.position = position;
        this.hitPoints = hitPoints;
    }

    // Getters and setters
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}
