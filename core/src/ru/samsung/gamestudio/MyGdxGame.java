package ru.samsung.gamestudio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.samsung.gamestudio.managers.AudioManager;
import ru.samsung.gamestudio.screens.GameScreen;
import ru.samsung.gamestudio.screens.MenuScreen;
import ru.samsung.gamestudio.screens.SettingsScreen;

import static java.awt.Color.WHITE;
import static ru.samsung.gamestudio.GameResources.FONT_PATH;
import static ru.samsung.gamestudio.GameSettings.*;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public GameScreen gameScreen;
	public World world;
	float accumulator = 0;
	public Vector3 touch;
	public BitmapFont commonWhiteFont;
	public BitmapFont largeWhiteFont;
	public BitmapFont commonBlackFont;
	public MenuScreen menuScreen;
	public AudioManager audioManager;
	public SettingsScreen settingsScreen;


	
	@Override
	public void create () {
		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);
		commonBlackFont = FontBuilder.generate(24, Color.BLACK, GameResources.FONT_PATH);
		largeWhiteFont = FontBuilder.generate(48, Color.WHITE, GameResources.FONT_PATH);

		audioManager = new AudioManager();

		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		settingsScreen = new SettingsScreen(this);
		setScreen(menuScreen);


	}

	public void stepWorld(){
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += delta;
		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}



	@Override
	public void dispose () {
		batch.dispose();

	}
}
