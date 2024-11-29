package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class GameState {
    private List<BirdState> birdStates;
    private List<PigState> pigStates;
    private List<BlockState> blockStates;
    private boolean isPaused;
    private int score;

    // Getters and setters
    public List<BirdState> getBirdStates() {
        return birdStates;
    }

    public void setBirdStates(List<BirdState> birdStates) {
        this.birdStates = birdStates;
    }

    public List<PigState> getPigStates() {
        return pigStates;
    }

    public void setPigStates(List<PigState> pigStates) {
        this.pigStates = pigStates;
    }

    public List<BlockState> getBlockStates() {
        return blockStates;
    }

    public void setBlockStates(List<BlockState> blockStates) {
        this.blockStates = blockStates;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
