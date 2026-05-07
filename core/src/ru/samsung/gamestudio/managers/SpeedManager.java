package ru.samsung.gamestudio.managers;

import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.screens.SettingsScreen;

import static ru.samsung.gamestudio.GameSettings.*;

public class SpeedManager {

    public static int shipForceRatio= GameSettings.SHIP_FORCE_RATIO;


    public static void speedUp(){
         shipForceRatio += 5;

    }
    public static void speedDown(){
        shipForceRatio -= 5;
    }
}
