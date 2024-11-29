//package io.github.some_example_name;
//import package com.mygdx.game;
//import com.mygdx.game.GameScore;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class GameScoreTest {
//    private GameScore gameScore;
//
//    @Before
//    public void setUp() {
//        gameScore = new GameScore();
//    }
//
//    @Test
//    public void testInitialScore() {
//        assertEquals(0, gameScore.getScore());
//    }
//
//    @Test
//    public void testIncrementScore() {
//        gameScore.incrementScore(10);
//        assertEquals(10, gameScore.getScore());
//
//        gameScore.incrementScore(5);
//        assertEquals(15, gameScore.getScore());
//    }
//
//    @Test
//    public void testResetScore() {
//        gameScore.incrementScore(10);
//        gameScore.resetScore();
//        assertEquals(0, gameScore.getScore());
//    }
//}
