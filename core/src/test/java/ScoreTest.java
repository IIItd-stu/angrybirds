package io.github.some_example_name;

import org.junit.Assert;
import org.junit.Test;

public class ScoreTest {

    @Test
    public void testCalculateScoreForRemainingBirds() {
        int initialScore = 0;
        int birdsRemaining = 2;
        int birdScore = 500;
        int expectedScore = initialScore + (birdsRemaining * birdScore);

        Assert.assertEquals("Score calculation is incorrect", expectedScore, calculateScore(birdsRemaining, birdScore));
    }

    private int calculateScore(int birds, int birdScore) {
        return birds * birdScore;
    }
}
