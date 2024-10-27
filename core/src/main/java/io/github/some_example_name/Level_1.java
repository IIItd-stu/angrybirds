package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture levelBackground;
    private Texture catapult;
    private Texture scr2Texture;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    private Stage stage;
    private ImageButton pauseButton;
    private ImageButton quitButton;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);
        viewport.apply();

        levelBackground = new Texture("insidelvle2.jpg");
        catapult = new Texture("catapult2.png");
        scr2Texture = new Texture("scr1.png");

        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);

        // Set up the stage for UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load button textures
        Texture pauseTexture = new Texture("pause2.png");
        Texture quitTexture = new Texture("quit1.png");

        // Create and position the pause button at the top-left
        pauseButton = createImageButton(pauseTexture, 10, 430, 50, 50);
        quitButton = createImageButton(quitTexture, 10, 370, 50, 50);
        quitButton.setVisible(false);

        // Set up listener for pause button click
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isPaused = !isPaused;
                quitButton.setVisible(isPaused); // Toggle quit button visibility on pause
                return true;
            }
        });

        // Set up listener for quit button click
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isPaused) {
                    mainInstance.showLevelDisplayScreen = true;
                    mainInstance.showLevel1 = false;
                }
                return true;
            }
        });

        // Add buttons to the stage
        stage.addActor(pauseButton);
        stage.addActor(quitButton);
    }

    // Helper method to create ImageButton with specified size
    private ImageButton createImageButton(Texture texture, float x, float y, float width, float height) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(drawable);
        button.setSize(width, height); // Set button size
        button.setPosition(x, y); // Set button position
        return button;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480);
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);

        float catapultX = bird.getX() + bird.getWidth() + 10;
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);

        float scr2X = 640 - 60; // X position for the image (adjust width as necessary)
        float scr2Y = 480 - 60; // Y position for the image (adjust height as necessary)
        batch.draw(scr2Texture, scr2X, scr2Y, 50, 40); // Draw scr2.png with size 50x50
        batch.end();

        // Draw the stage with buttons
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update(); // Update camera after resizing
        stage.getViewport().update(width, height, true); // Ensure stage viewport is also updated

        // Reset the input processor to ensure it receives inputs correctly
        Gdx.input.setInputProcessor(stage);

        // Update button positions dynamically based on the new height
        pauseButton.setPosition(10, height - 60); // Adjust Y position based on height
        quitButton.setPosition(10, height - 120); // Adjust Y position based on height
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
        stage.dispose();
    }
}








/*package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;    // Camera for world coordinates
    private Viewport viewport;            // Viewport to handle resizing
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult;             // New catapult texture
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Set up camera and viewport for a 640x480 world size
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);  // Center the camera at (320, 240)
        viewport.apply();

        // Load textures
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pse.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png");

        // Set button bounds in world coordinates
        pauseButtonBounds = new Rectangle(50, 430, 50, 50);   // Position near the top-left corner
        quitButtonBounds = new Rectangle(50, 370, 50, 50);    // Position below pause button
        settingsButtonBounds = new Rectangle(50, 310, 50, 50); // Position below quit button

        isPaused = false;

        // Initialize game objects with world coordinates
        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480); // Draw background to match viewport size

        // Draw pause button
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        // Render game objects
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);

        // Draw the catapult next to the bird
        float catapultX = bird.getX() + bird.getWidth() + 10; // Position catapult near bird
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);

        if (isPaused) {
            // Draw quit and settings buttons if paused
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            // Convert screen coordinates to world coordinates
            float touchX = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x;
            float touchY = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y;

            if (pauseButtonBounds.contains(touchX, touchY)) {
                isPaused = !isPaused;  // Toggle pause state
            }

            if (isPaused && quitButtonBounds.contains(touchX, touchY)) {
                mainInstance.showLevelDisplayScreen = true; // Go back to level select
                mainInstance.showLevel1 = false; // Exit Level_1
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height); // Update viewport to handle resizing
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
    }
}





/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture levelBackground;
    private Texture catapult;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    private Stage stage;
    private ImageButton pauseButton;
    private ImageButton quitButton;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);
        viewport.apply();

        levelBackground = new Texture("insidelvle2.jpg");
        catapult = new Texture("catapult2.png");

        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);

        // Set up the stage for UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load button textures
        Texture pauseTexture = new Texture("pause2.png");
        Texture quitTexture = new Texture("quit1.png");

        // Create and position the pause button at the top-left
        pauseButton = createImageButton(pauseTexture, 10, 430, 50, 50);
        quitButton = createImageButton(quitTexture, 10, 370, 50, 50);
        quitButton.setVisible(false);

        // Set up listener for pause button click
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isPaused = !isPaused;
                quitButton.setVisible(isPaused); // Toggle quit button visibility on pause
                return true;
            }
        });

        // Set up listener for quit button click
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isPaused) {
                    mainInstance.showLevelDisplayScreen = true;
                    mainInstance.showLevel1 = false;
                }
                return true;
            }
        });

        // Add buttons to the stage
        stage.addActor(pauseButton);
        stage.addActor(quitButton);
    }

    // Helper method to create ImageButton with specified size
    private ImageButton createImageButton(Texture texture, float x, float y, float width, float height) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(drawable);
        button.setSize(width, height); // Set button size
        button.setPosition(x, y); // Set button position
        return button;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480);
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);

        float catapultX = bird.getX() + bird.getWidth() + 10;
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);
        batch.end();

        // Draw the stage with buttons
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true); // Ensure stage updates correctly

        // Reset input processor to ensure it receives inputs correctly
        Gdx.input.setInputProcessor(stage);

        // Update button positions dynamically based on height
        pauseButton.setPosition(10, height - 60); // Adjust Y position based on height
        quitButton.setPosition(10, height - 120); // Adjust Y position based on height
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
        stage.dispose();
    }
}




/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture levelBackground;
    private Texture catapult;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    private Stage stage;
    private ImageButton pauseButton;
    private ImageButton quitButton;
    private ImageButton settingsButton;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);
        viewport.apply();

        levelBackground = new Texture("insidelvle2.jpg");
        catapult = new Texture("catapult2.png");

        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);

        // Set up the stage for UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set input processor to the stage

        // Load button textures
        Texture pauseTexture = new Texture("pause2.png");
        Texture quitTexture = new Texture("quit1.png");
        Texture settingsTexture = new Texture("settings.png");

        // Create ImageButtons
        pauseButton = createImageButton(pauseTexture, 50, 430);
        quitButton = createImageButton(quitTexture, 50, 370);
        settingsButton = createImageButton(settingsTexture,  50, 310);

        // Add listeners to handle clicks
        pauseButton.addListener(event -> {
            isPaused = !isPaused;
            return true;
        });

        quitButton.addListener(event -> {
            if (isPaused) {
                mainInstance.showLevelDisplayScreen = true;
                mainInstance.showLevel1 = false;
            }
            return true;
        });

        // Add buttons to stage
        stage.addActor(pauseButton);
        stage.addActor(quitButton);
        stage.addActor(settingsButton);
    }

    // Helper method to create ImageButton
    private ImageButton createImageButton(Texture texture, float x, float y) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(drawable);
        button.setPosition(x,y);
        button.setSize(50,50);
        return button;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480);
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);

        float catapultX = bird.getX() + bird.getWidth() + 10;
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);
        batch.end();

        // Draw the stage with buttons
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
        stage.dispose();
    }
}




/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector2;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;    // Camera for world coordinates
    private Viewport viewport;            // Viewport to handle resizing
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult;             // New catapult texture
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Set up camera and viewport for a 640x480 world size
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);  // Center the camera at (320, 240)
        viewport.apply();

        // Load textures
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause2.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png");

        // Set button bounds in world coordinates
        pauseButtonBounds = new Rectangle(50, 430, 50, 50);   // Position near the top-left corner
        quitButtonBounds = new Rectangle(50, 370, 50, 50);    // Position below pause button
        settingsButtonBounds = new Rectangle(50, 310, 50, 50); // Position below quit button

        isPaused = false;

        // Initialize game objects with world coordinates
        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480); // Draw background to match viewport size

        // Draw pause button
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        // Render game objects
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);

        // Draw the catapult next to the bird
        float catapultX = bird.getX() + bird.getWidth() + 10; // Position catapult near bird
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);

        if (isPaused) {
            // Draw quit and settings buttons if paused
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        }

        batch.end();

        // Check if the screen was just touched
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos); // Convert screen coordinates to world coordinates

            // Check if the touch is within the pause button bounds
            if (pauseButtonBounds.contains(touchPos.x, touchPos.y)) {
                isPaused = !isPaused;  // Toggle pause state
                System.out.println("Pause button clicked!");
            }

            // If paused, check quit button for level selection screen
            if (isPaused && quitButtonBounds.contains(touchPos.x, touchPos.y)) {
                mainInstance.showLevelDisplayScreen = true; // Go back to level select
                mainInstance.showLevel1 = false; // Exit Level_1
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
    }
}





/*package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult;
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    private Vector3 touchPoint;  // Store touch coordinates

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Set up camera and viewport for a 640x480 world size
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);  // Center the camera at (320, 240)
        viewport.apply();

        // Load textures
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause2.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png");

        // Set button bounds in world coordinates
        pauseButtonBounds = new Rectangle(50, 430, 50, 50);   // Position near the top-left corner
        quitButtonBounds = new Rectangle(50, 370, 50, 50);    // Position below pause button
        settingsButtonBounds = new Rectangle(50, 310, 50, 50); // Position below quit button

        isPaused = false;

        // Initialize game objects with world coordinates
        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);

        touchPoint = new Vector3();  // Initialize touchPoint for reuse
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480); // Draw background to match viewport size
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);
        // Draw pause button
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);
        float catapultX = bird.getX() + bird.getWidth() + 10; // Position catapult near bird
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);
        if (isPaused) {
            // Draw quit and settings buttons if paused
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        } else {
            // Render game objects

        }

        batch.end();

        // Handle touch input with justTouched to register single clicks
        if (Gdx.input.justTouched()) {
            // Convert screen coordinates to world coordinates once
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            // Check if the touch is within the pause button's bounds
            if (pauseButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                isPaused = !isPaused;  // Toggle pause state
            }

            // Check if quit button is clicked while paused
            if (isPaused && quitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                mainInstance.showLevelDisplayScreen = true; // Go back to level select
                mainInstance.showLevel1 = false; // Exit Level_1
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height); // Update viewport to handle resizing
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
}
}







/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;    // Camera for world coordinates
    private Viewport viewport;            // Viewport to handle resizing
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult;             // New catapult texture
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Set up camera and viewport for a 640x480 world size
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);  // Center the camera at (320, 240)
        viewport.apply();

        // Load textures
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause2.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png");

        // Set button bounds in world coordinates
        pauseButtonBounds = new Rectangle(50, 430, 50, 50);   // Position near the top-left corner
        quitButtonBounds = new Rectangle(50, 370, 50, 50);    // Position below pause button
        settingsButtonBounds = new Rectangle(50, 310, 50, 50); // Position below quit button

        isPaused = false;

        // Initialize game objects with world coordinates
        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480); // Draw background to match viewport size
        bird.render(batch);
        bird2.render(batch);
        pig.render(batch);
        woodBlock.render(batch);
        // Draw pause button
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);
        // Draw the catapult next to the bird
        float catapultX = bird.getX() + bird.getWidth() + 10; // Position catapult near bird
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);
        if (isPaused) {
            // Draw quit and settings buttons if paused
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        } else {
           //
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            // Convert screen coordinates to world coordinates
            float touchX = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x;
            float touchY = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y;

            if (pauseButtonBounds.contains(touchX, touchY)) {
                isPaused = !isPaused;  // Toggle pause state
            }

            if (isPaused && quitButtonBounds.contains(touchX, touchY)) {
                mainInstance.showLevelDisplayScreen = true; // Go back to level select
                mainInstance.showLevel1 = false; // Exit Level_1
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height); // Update viewport to handle resizing
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
}
}




/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult;
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    private Vector3 touchPoint;  // Store the unprojected touch point

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Set up camera and viewport for a 640x480 world size
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        camera.position.set(320, 240, 0);
        viewport.apply();

        // Load textures
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause2.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png");

        // Set button bounds in world coordinates
        pauseButtonBounds = new Rectangle(50, 430, 50, 50);
        quitButtonBounds = new Rectangle(50, 370, 50, 50);
        settingsButtonBounds = new Rectangle(50, 310, 50, 50);

        isPaused = false;

        // Initialize game objects with world coordinates
        bird = new Bird("bird.png", 70, 70, 25, 25);
        bird2 = new Bird("bird2.png", 120, 127, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);

        touchPoint = new Vector3();  // Initialize the touchPoint for reuse
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(levelBackground, 0, 0, 640, 480);

        // Draw pause button
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        if (isPaused) {
            // Draw quit and settings buttons if paused
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        } else {
            // Render game objects
            bird.render(batch);
            bird2.render(batch);
            pig.render(batch);
            woodBlock.render(batch);

            // Draw the catapult next to the bird
            float catapultX = bird.getX() + bird.getWidth() + 10;
            float catapultY = bird.getY();
            batch.draw(catapult, catapultX, catapultY, 60, 70);
        }

        batch.end();

        // Handle touch input
        if (Gdx.input.justTouched()) { // Use justTouched to ensure only one input is registered
            // Convert screen coordinates to world coordinates once per touch
            touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            // Check if the touch is within the pause button's bounds
            if (pauseButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                isPaused = !isPaused;  // Toggle pause state
            }

            // Check if quit button is clicked while paused
            if (isPaused && quitButtonBounds.contains(touchPoint.x, touchPoint.y)) {
                mainInstance.showLevelDisplayScreen = true;
                mainInstance.showLevel1 = false;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose();
        bird.dispose();
        bird2.dispose();
        pig.dispose();
        woodBlock.dispose();
}
}

 */





/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult; // New catapult texture
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause2.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png"); // Load catapult texture

        pauseButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 100, 50, 50);
        quitButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 150, 50, 50);
        settingsButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 200, 50, 50);

        isPaused = false;

        bird = new Bird("bird.png", 70, 70, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);
        bird2= new Bird("bird2.png",120, 127, 25 , 25 );
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        bird.render(batch);
        pig.render(batch);
        woodBlock.render(batch);
        bird2.render(batch);
        // Draw the catapult to the right of the bird
        float catapultX = bird.getX() + bird.getWidth() + 10; // Adjust positioning
        float catapultY = bird.getY();
        batch.draw(catapult, catapultX, catapultY, 60, 70);

        if (isPaused) {
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        } else {


          // Adjust size as needed
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (pauseButtonBounds.contains(touchX, touchY)) {
                isPaused = !isPaused;
            }

            if (isPaused && quitButtonBounds.contains(touchX, touchY)) {
                mainInstance.showLevelDisplayScreen = true;
                mainInstance.showLevel1 = false;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose(); // Dispose catapult texture
        bird.dispose();
        pig.dispose();
        woodBlock.dispose();
    }
}








/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Texture catapult; // New catapult texture
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Bird bird2;
    private Pig pig;
    private Object woodBlock;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause1.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");
        catapult = new Texture("catapult2.png"); // Load catapult texture

        pauseButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 100, 50, 50);
        quitButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 150, 50, 50);
        settingsButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 200, 50, 50);

        isPaused = false;

        bird = new Bird("bird.png", 70, 70, 25, 25);
        pig = new Pig("pig.png", 510, 70, 30, 40);
        woodBlock = new Object("wood2.png", 480, 60, 80, 70);
        bird2= new Bird("bird2.png",120, 127, 25 , 25 );
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        if (isPaused) {
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        } else {
            bird.render(batch);
            pig.render(batch);
            woodBlock.render(batch);
            bird2.render(batch);

            // Draw the catapult to the right of the bird
            float catapultX = bird.getX() + bird.getWidth() + 10; // Adjust positioning
            float catapultY = bird.getY();
            batch.draw(catapult, catapultX, catapultY, 60, 70); // Adjust size as needed
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (pauseButtonBounds.contains(touchX, touchY)) {
                isPaused = !isPaused;
            }

            if (isPaused && quitButtonBounds.contains(touchX, touchY)) {
                mainInstance.showLevelDisplayScreen = true;
                mainInstance.showLevel1 = false;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        catapult.dispose(); // Dispose catapult texture
        bird.dispose();
        pig.dispose();
        woodBlock.dispose();
    }
}







/*



package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;
    private Main mainInstance;

    private Bird bird;
    private Pig pig;
    private Object woodBlock;

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelBackground = new Texture("insidelvle2.jpg");
        pauseButton = new Texture("pause1.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");

        pauseButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 100, 50, 50);
        quitButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 150, 50, 50);
        settingsButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 200, 50, 50);

        isPaused = false;

        bird = new Bird("bird.png", 70, 70, 30, 30);
        pig = new Pig("pig.png", 500, 110, 30, 30);
        woodBlock = new Object("wood.jpg", 480, 60, 70, 50);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        if (isPaused) {
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        } else {
            bird.render(batch);
            pig.render(batch);
            woodBlock.render(batch);
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (pauseButtonBounds.contains(touchX, touchY)) {
                isPaused = !isPaused;
            }

            if (isPaused && quitButtonBounds.contains(touchX, touchY)) {
                mainInstance.showLevelDisplayScreen = true;
                mainInstance.showLevel1 = false;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
        bird.dispose();
        pig.dispose();
        woodBlock.dispose();
    }
}


 */




/*package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Level_1 extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture levelBackground;
    private Texture pauseButton;
    private Texture quitButton;
    private Texture settingsButton;
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private Rectangle settingsButtonBounds;
    private boolean isPaused;

    private Main mainInstance; // Reference to Main class for screen transitions

    public Level_1(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        levelBackground = new Texture("insidelvle.png");
        pauseButton = new Texture("pause1.png");
        quitButton = new Texture("quit1.png");
        settingsButton = new Texture("settings.png");

        pauseButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 100, 50, 50);
        quitButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 150, 50, 50);
        settingsButtonBounds = new Rectangle(50, Gdx.graphics.getHeight() - 200, 50, 50);

        isPaused = false;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(pauseButton, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height);

        if (isPaused) {
            batch.draw(quitButton, quitButtonBounds.x, quitButtonBounds.y, quitButtonBounds.width, quitButtonBounds.height);
            batch.draw(settingsButton, settingsButtonBounds.x, settingsButtonBounds.y, settingsButtonBounds.width, settingsButtonBounds.height);
        }

        batch.end();

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (pauseButtonBounds.contains(touchX, touchY)) {
                isPaused = !isPaused;
            }

            if (isPaused && quitButtonBounds.contains(touchX, touchY)) {
                mainInstance.showLevelDisplayScreen = true; // Return to level display screen
                mainInstance.showLevel1 = false; // Exit Level_1
            }
        }


    }

    @Override
    public void dispose() {
        batch.dispose();
        levelBackground.dispose();
        pauseButton.dispose();
        quitButton.dispose();
        settingsButton.dispose();
    }
}
*/
