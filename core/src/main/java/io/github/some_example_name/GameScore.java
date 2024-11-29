package io.github.some_example_name;

public class GameScore {
    private int score;

    public GameScore() {
        this.score = 0;
    }

    public void incrementScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        this.score = 0;
    }
}
