package ru.samsung.gamestudio;

import com.badlogic.gdx.utils.TimeUtils;
import ru.samsung.gamestudio.screens.GameState;

public class GameSession {
    public GameState state;
    long nextTrashSpawnTime;
    long sessionStartTime;
    long pauseStartTime;

    public GameSession(){

    }

    public void startGame(){
        state = GameState.PLAYING;
        sessionStartTime = TimeUtils.millis();
        nextTrashSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN * getTrashPeriodCoolDown());

    }
    public boolean shouldSpawnTrash(){

        if (nextTrashSpawnTime <= TimeUtils.millis()){
            nextTrashSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN * getTrashPeriodCoolDown());
            return true;
        }
        return false;

    }
    private float getTrashPeriodCoolDown(){
        return (float) Math.exp(- 0.001 * (TimeUtils.millis() - sessionStartTime)/ 1000);
    }
    public void resumeGame(){
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }
    public void pauseGame(){
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();

    }
}
