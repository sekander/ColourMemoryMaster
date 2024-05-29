package com.mygdx.util.config;

public class GameConfig {
    //public static final float WIDTH = 400.0f; //pixels
    public static final float WIDTH = 1080.0f; //pixels
    public static final float HEIGHT = 1920.0f; //pixels
    //public static final float HEIGHT = 800.0f; //pixels
    //public static final float WIDTH = 1920.0f; //pixels
    //public static final float HEIGHT = 1080.0f; //pixels

    //public static final float WORLD_WIDTH = 19.2f; //WORLD UNITS
    //public static final float WORLD_HEIGHT = 10.8f; //WORLD UNITS
    public static final float WORLD_WIDTH = 6.0f; //WORLD UNITS
    public static final float WORLD_HEIGHT = 10.8f; //WORLD UNITS

    public static final float WORLD_CENTER_X = WORLD_WIDTH / 2.0f; //WORLD UNITS
    public static final float WORLD_CENTER_Y = WORLD_HEIGHT / 2.0f; //WORLD UNITS


    public static final float HUD_WIDTH = 480f; // world units
    public static final float HUD_HEIGHT = 800f; // world units

    public static final float MAX_PLAYER_X_SPEED = 0.0025f; // max player speed
    public static final float OBSTACLE_SPAWN_TIME = 0.25f; // spawn obstacle every interval
    public static final float SCORE_MAX_TIME = 1.25f; // add score every interval

    public static final int LIVES_START = 3; // lives on start

    public static final float EASY_OBSTACLE_SPEED = 0.1f;
    public static final float MEDIUM_OBSTACLE_SPEED = 0.15f;
    public static final float HARD_OBSTACLE_SPEED = 0.18f;

    public static final float PLAYER_BOUNDS_RADIUS = 0.4f; // world units
    public static final float PLAYER_SIZE = 2 * PLAYER_BOUNDS_RADIUS; // world units

    public static final float OBSTACLE_BOUNDS_RADIUS = 0.3f; // world units
    public static final float OBSTACLE_SIZE = 2 * OBSTACLE_BOUNDS_RADIUS; // world units

    private GameConfig() {}

}
