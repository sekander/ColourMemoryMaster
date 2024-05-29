package com.mygdx.util.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.game.LibGdxFramework;
import com.mygdx.util.config.DifficultyLevel;
import com.mygdx.util.config.GameConfig;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();

    private static final String HIGH_SCORE_KEY = "highscore";
    private static final String DIFFICULTY_KEY = "difficulty";

    private Preferences PREFS;
    private int highscore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.EASY;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private double gameTime;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(LibGdxFramework.class.getSimpleName());
        highscore = PREFS.getInteger(HIGH_SCORE_KEY, 0);
        String difficultyName = PREFS.getString(DIFFICULTY_KEY, DifficultyLevel.
                MEDIUM.name());
        difficultyLevel = DifficultyLevel.valueOf(difficultyName);
        score = 0;
        gameTime = 0;
    }

    public void setGameTime(double time){
        gameTime = time;
    }

    public void updateScore(int amount){
        score += amount;
    }

    public void updateHighScore() {
        //public void updateHighScore(int score) {
        if(score < highscore) {
            return;
        }

        highscore = score;
        PREFS.putInteger(HIGH_SCORE_KEY, highscore);
        PREFS.flush();
    }

    public String getHighScoreString() {
        return String.valueOf(highscore);
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void reset()
    {
        lives = GameConfig.LIVES_START;
        score = 0;
    }

    public void decrementLives(){
        lives--;
    }

    public boolean isGameOver(){
        return lives <= 0;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public double getGameTime(){
        return gameTime;
    }

    public void updateDifficulty(DifficultyLevel newDifficultyLevel) {
        if(difficultyLevel == newDifficultyLevel) {
            return;
        }

        difficultyLevel = newDifficultyLevel;
        PREFS.putString(DIFFICULTY_KEY, difficultyLevel.name());
        PREFS.flush();
    }

}
