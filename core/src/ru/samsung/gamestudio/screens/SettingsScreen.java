package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.*;
import ru.samsung.gamestudio.managers.MemoryManager;
import ru.samsung.gamestudio.managers.SpeedManager;

import java.util.ArrayList;

public class SettingsScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    MovingBackgroundView backgroundView;
    TextView titleTextView;
    ImageView blackoutImageView;
    ButtonView returnButton;
    TextView musicSettingView;
    TextView soundSettingView;
    TextView clearSettingView;
    TextView speedSettingView;
    TextView downSpeedSettingView;

    private String translateStateToText(boolean state) {
        return state ? "ON" : "OFF";
    }
    public SettingsScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;
        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);


        speedSettingView = new TextView(myGdxGame.commonWhiteFont,173, 776, "Add ship force" );
        downSpeedSettingView = new TextView(myGdxGame.commonWhiteFont,173,717, "Lower ship force");
        titleTextView = new TextView(myGdxGame.largeWhiteFont, 256, 956, "Settings");
        blackoutImageView = new ImageView(85, 365, GameResources.BLACKOUT_MIDDLE_IMG_PATH);
        clearSettingView = new TextView(myGdxGame.commonWhiteFont, 173, 540, "clear records");
        musicSettingView = new TextView(
                myGdxGame.commonWhiteFont,
                173, 658,
                "music: " + translateStateToText(MemoryManager.loadIsMusicOn())
        );

        soundSettingView = new TextView(
                myGdxGame.commonWhiteFont,
                173, 599,
                "sound: " + translateStateToText(MemoryManager.loadIsSoundOn())
        );


        returnButton = new ButtonView(
                280, 447,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "return"
        );
    }
    public void render(float delta) {
        handleInput();
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleTextView.draw(myGdxGame.batch);
        blackoutImageView.draw(myGdxGame.batch);
        returnButton.draw(myGdxGame.batch);
        musicSettingView.draw(myGdxGame.batch);
        soundSettingView.draw(myGdxGame.batch);
        downSpeedSettingView.draw(myGdxGame.batch);

        speedSettingView.draw(myGdxGame.batch);
        clearSettingView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }
    void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if (musicSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("music: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                myGdxGame.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("sound: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                myGdxGame.audioManager.updateSoundFlag();
            }
            if (clearSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveTableOfRecords(new ArrayList<>());
                clearSettingView.setText("clear records (cleared)");

        }
            if(speedSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                speedSettingView.setText("Add ship force(+5)");
                SpeedManager.speedUp();

            }
            if(downSpeedSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                downSpeedSettingView.setText("Lower ship force(-5)");

                SpeedManager.speedDown();
            }
    } }

    @Override
    public void dispose() {
        super.dispose();
    }
}
