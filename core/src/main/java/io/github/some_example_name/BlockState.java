package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

public class BlockState {
    private Vector2 position;
    private int health;

    // Constructor
    public BlockState(Vector2 position, int health) {
        this.position = position;
        this.health = health;
    }

    // Getters and setters
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
