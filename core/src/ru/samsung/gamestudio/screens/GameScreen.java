package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.*;
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
    ContactManager contactManager;
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
        contactManager = new ContactManager(myGdxGame.world);
        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_PATH);

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
        backgroundView.move();
        if (!shipObject.isAlive()){
            System.out.println("Game over");
        }


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

        liveView.setLeftLives(shipObject.getLiveLeft());
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
        backgroundView.draw(myGdxGame.batch);
        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }
        private void updateTrash() {
            for (int i = 0; i < trashArray.size(); i++) {
                if (!trashArray.get(i).isInFrame() || !trashArray.get(i).isAlive()){
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
