package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.GameResources;
import ru.samsung.gamestudio.GameSession;
import ru.samsung.gamestudio.GameSettings;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.objects.BulletObject;
import ru.samsung.gamestudio.objects.ShipObject;
import ru.samsung.gamestudio.objects.TrashObject;

import java.util.ArrayList;
import java.util.Vector;

import static com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable.draw;

public class GameScreen extends ScreenAdapter {
    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;


    MyGdxGame myGdxGame;
    ShipObject shipObject;
    GameSession gameSession;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH/ 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );
    }

    public void show(){
        gameSession.startGame();

    }

    @Override
    public void render(float delta) {


            myGdxGame.stepWorld();
            handleInput();

        if (gameSession.shouldSpawnTrash()) {
            TrashObject trashObject = new TrashObject(
                    GameResources.TRASH_IMG_PATH,
                    GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                    myGdxGame.world
            );
            trashArray.add(trashObject);
        }

        if (shipObject.needToShoot()) {
            BulletObject laserBullet = new BulletObject(
                    shipObject.getX(), shipObject.getY() + shipObject.height / 2,
                    GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                    GameResources.BULLET_IMG_PATH,
                    myGdxGame.world
            );
            bulletArray.add(laserBullet);
        }

        updateTrash();

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            shipObject.move(myGdxGame.touch);
        }
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        shipObject.draw(myGdxGame.batch);
        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }
        private void updateTrash() {
            for (int i = 0; i < trashArray.size(); i++) {
                if (!trashArray.get(i).isInFrame()) {
                    myGdxGame.world.destroyBody(trashArray.get(i).body);
                    trashArray.remove(i--);
                }
            }
        }
        private void updateBullets(){
        for (int i = 0; i < bulletArray.size(); i ++){
            if (!bulletArray.get(i).hasToBeDestroyed()){
                myGdxGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i --);
            }
        }
        }
}
