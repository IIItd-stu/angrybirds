package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Game;
import java.io.Serializable;


public class Main extends Game {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    Viewport viewport;
    Texture background;
    Texture levelDisplayBackground;
    Texture nextButton;
    Texture levelButton;
    Texture levelButton2;
    Texture levelButton3;
    Texture backButton;
    Rectangle nextButtonBounds;
    Rectangle levelButtonBounds;
    Rectangle levelButton2Bounds;
    Rectangle levelButton3Bounds;
    Rectangle backButtonBounds;
    boolean showLevelDisplayScreen;
    boolean isLoaded;
    float loadingProgress;
    BitmapFont font;
    Level_1 level_1;
    Level_2 level_2;
    Level_3 level_3; // Declare Level_3 instance
    int currentLevel;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);
        viewport.apply();

        background = new Texture("homesc.png");
        levelDisplayBackground = new Texture("leveldisplayscreen.png");
        nextButton = new Texture("playbutton1.png");
        levelButton = new Texture("lvle.png");
        levelButton2 = new Texture("lvle2.png");
        levelButton3 = new Texture("lvle3.png");
        backButton = new Texture("back.png");

        nextButtonBounds = new Rectangle(270, 350, 100, 100);
        levelButtonBounds = new Rectangle(80, 170, 150, 150);
        levelButton2Bounds = new Rectangle(260, 170, 150, 150);
        levelButton3Bounds = new Rectangle(440, 170, 150, 150);
        backButtonBounds = new Rectangle(20, 400, 70, 60);

        showLevelDisplayScreen = false;
        isLoaded = false;
        loadingProgress = 0;

        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);

        level_1 = new Level_1(this);
        level_1.create();

        level_2 = new Level_2(this);
        level_2.create();

        level_3 = new Level_3(this); // Initialize Level_3
        level_3.create();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (!isLoaded) {
            loadingProgress += Gdx.graphics.getDeltaTime() * 60;
            if (loadingProgress >= 100) {
                isLoaded = true;
            }
            drawLoadingScreen();
        } else if (currentLevel == 1) {
            level_1.render();
        } else if (currentLevel == 2) {
            level_2.render();
        } else if (currentLevel == 3) { // Add condition to render Level_3
            level_3.render();
        } else {
            drawHomeScreen();
        }
    }

    private void drawLoadingScreen() {
        batch.begin();
        batch.draw(background, 0, 0, 640, 480);
        font.draw(batch, "LOADING", 245, 100);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
        float barWidth = 300;
        shapeRenderer.rect((640 - barWidth) / 2, 50, loadingProgress / 100 * barWidth, 20);
        shapeRenderer.end();
    }

    private void drawHomeScreen() {
        batch.begin();
        batch.draw(showLevelDisplayScreen ? levelDisplayBackground : background, 0, 0, 640, 480);

        if (showLevelDisplayScreen) {
            batch.draw(levelButton, levelButtonBounds.x, levelButtonBounds.y, levelButtonBounds.width, levelButtonBounds.height);
            batch.draw(levelButton2, levelButton2Bounds.x, levelButton2Bounds.y, levelButton2Bounds.width, levelButton2Bounds.height);
            batch.draw(levelButton3, levelButton3Bounds.x, levelButton3Bounds.y, levelButton3Bounds.width, levelButton3Bounds.height);
            batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height);
        } else {
            batch.draw(nextButton, nextButtonBounds.x, nextButtonBounds.y, nextButtonBounds.width, nextButtonBounds.height);
        }
        batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touch = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            handleTouch(touch.x, touch.y);
        }
    }

    private void handleTouch(float touchX, float touchY) {
        if (showLevelDisplayScreen) {
            if (backButtonBounds.contains(touchX, touchY)) {
                showLevelDisplayScreen = false;
            } else if (levelButtonBounds.contains(touchX, touchY)) {
                currentLevel = 1;
                level_1.create(); // Reinitialize level 1
            } else if (levelButton2Bounds.contains(touchX, touchY)) {
                currentLevel = 2;
                level_2.create(); // Reinitialize level 2
            } else if (levelButton3Bounds.contains(touchX, touchY)) {
                currentLevel = 3;
                level_3.create(); // Reinitialize level 3
            }
        } else if (nextButtonBounds.contains(touchX, touchY)) {
            showLevelDisplayScreen = true; // Always show level selection screen when play is clicked
            currentLevel = 0; // Ensure no level starts yet
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(320, 240, 0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();
        levelDisplayBackground.dispose();
        nextButton.dispose();
        levelButton.dispose();
        levelButton2.dispose();
        levelButton3.dispose();
        backButton.dispose();
        font.dispose();
        if (level_1 != null) level_1.dispose();
        if (level_2 != null) level_2.dispose();
        if (level_3 != null) level_3.dispose(); // Dispose Level_3
    }
}
