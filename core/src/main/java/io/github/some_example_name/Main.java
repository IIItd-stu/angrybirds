package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;    // Camera for world coordinates
    Viewport viewport;            // Viewport to handle resizing
    Texture background;
    Texture levelDisplayBackground;
    Texture nextButton;
    Texture levelButton;
    Texture levelButton2;
    Texture levelButton3;
    Texture backButton;
    Circle nextButtonBounds;
    Rectangle levelButtonBounds;
    Rectangle levelButton2Bounds;
    Rectangle levelButton3Bounds;
    Rectangle backButtonBounds;
    boolean showLevelDisplayScreen;
    boolean showLevel1;
    boolean isLoaded;
    float loadingProgress;
    BitmapFont font;

    Level_1 level_1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);  // Ensure a 640x480 world size
        camera.position.set(320, 240, 0);              // Center the camera at (320, 240)
        viewport.apply();

        // Load textures
        background = new Texture("homesc.png");
        levelDisplayBackground = new Texture("leveldisplayscreen.png");
        nextButton = new Texture("playbutton1.png");
        levelButton = new Texture("lvle.png");
        levelButton2 = new Texture("lvle2.png");
        levelButton3 = new Texture("lvle3.png");
        backButton = new Texture("back.png");

        // Set button bounds with world coordinates
        nextButtonBounds = new Circle(320, 225, 50);  // Centered in the screen
        levelButtonBounds = new Rectangle(200, 240, 60, 60);  // Level 1 button
        levelButton2Bounds = new Rectangle(280, 240, 60, 60); // Level 2 button, next to Level 1
        levelButton3Bounds = new Rectangle(360, 240, 60, 60); // Level 3 button, next to Level 2
        backButtonBounds = new Rectangle(20, 420, 60, 60);    // Back button at top left corner

        showLevelDisplayScreen = false;
        showLevel1 = false;
        isLoaded = false;
        loadingProgress = 0;

        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);

        level_1 = new Level_1(this);
        level_1.create();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();          // Update camera before rendering
        batch.setProjectionMatrix(camera.combined); // Apply camera to batch
        shapeRenderer.setProjectionMatrix(camera.combined); // Apply camera to shape renderer

        if (!isLoaded) {
            loadingProgress += Gdx.graphics.getDeltaTime() * 60;
            if (loadingProgress >= 100) {
                isLoaded = true;
            }

            batch.begin();
            batch.draw(background, 0, 0, 640, 480); // Draw background with viewport dimensions
            font.draw(batch, "LOADING", 245, 100);  // Centered loading text
            batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 1, 0, 1);
            float barWidth = 300;
            float barHeight = 20;
            float barX = (640 - barWidth) / 2;
            float barY = 50;
            shapeRenderer.rect(barX, barY, loadingProgress / 100 * barWidth, barHeight);
            shapeRenderer.end();

        } else if(showLevel1) {
            level_1.render();
        } else {
            batch.begin();

            if (!showLevelDisplayScreen) {
                batch.draw(background, 0, 0, 640, 480);
                batch.draw(nextButton, nextButtonBounds.x - nextButtonBounds.radius * 0.7f,
                    nextButtonBounds.y - nextButtonBounds.radius * 0.7f,
                    nextButtonBounds.radius * 2 * 0.7f, nextButtonBounds.radius * 2 * 0.7f);
            } else {
                batch.draw(levelDisplayBackground, 0, 0, 640, 480);
                batch.draw(levelButton, levelButtonBounds.x, levelButtonBounds.y,
                    levelButtonBounds.width, levelButtonBounds.height);
                batch.draw(levelButton2, levelButton2Bounds.x, levelButton2Bounds.y,
                    levelButton2Bounds.width, levelButton2Bounds.height);
                batch.draw(levelButton3, levelButton3Bounds.x, levelButton3Bounds.y,
                    levelButton3Bounds.width, levelButton3Bounds.height);
                batch.draw(backButton, backButtonBounds.x, backButtonBounds.y,
                    backButtonBounds.width, backButtonBounds.height);
            }

            batch.end();

            if (Gdx.input.isTouched()) {
                // Convert screen coordinates to world coordinates
                float touchX = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x;
                float touchY = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y;

                if (nextButtonBounds.contains(touchX, touchY) && !showLevelDisplayScreen) {
                    showLevelDisplayScreen = true;
                }

                if (levelButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevel1 = true;
                }

                if (backButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevelDisplayScreen = false;
                }
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
        shapeRenderer.dispose();
        background.dispose();
        levelDisplayBackground.dispose();
        nextButton.dispose();
        levelButton.dispose();
        levelButton2.dispose();
        levelButton3.dispose();
        backButton.dispose();
        font.dispose();
        level_1.dispose();
    }
}




/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;  // For rendering text
import com.badlogic.gdx.graphics.Color;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Texture background;
    Texture levelDisplayBackground;  // Background for level display screen
    Texture nextButton;
    Texture levelButton;
    Texture levelButton2;            // Texture for Level 2 (lvle2.png)
    Texture levelButton3;            // Texture for Level 3 (lvle3.png)
    Texture backButton;              // Texture for the back button
    Circle nextButtonBounds;
    Rectangle levelButtonBounds;
    Rectangle levelButton2Bounds;    // Bounds for Level 2 button
    Rectangle levelButton3Bounds;    // Bounds for Level 3 button
    Rectangle backButtonBounds;      // Bounds for the back button
    boolean showLevelDisplayScreen; // Controls showing level display screen
    boolean showLevel1;             // Controls showing Level_1 class
    boolean isLoaded;               // Indicates whether loading is complete
    float loadingProgress;          // Tracks the loading progress
    BitmapFont font;                // Font for rendering "LOADING" text

    Level_1 level_1; // Instance of Level_1 class

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("homesc.png");
        levelDisplayBackground = new Texture("leveldisplayscreen.png");
        nextButton = new Texture("playbutton1.png");
        levelButton = new Texture("lvle.png");
        levelButton2 = new Texture("lvle2.png");  // Load Level 2 texture
        levelButton3 = new Texture("lvle3.png");  // Load Level 3 texture
        backButton = new Texture("back.png"); // Load the back button texture

        nextButtonBounds = new Circle(Gdx.graphics.getWidth() / 2, 225, 50);
        levelButtonBounds = new Rectangle((Gdx.graphics.getWidth() - 100) / 2 - 150, (Gdx.graphics.getHeight() - 100) / 2, 60, 60);
        levelButton2Bounds = new Rectangle(levelButtonBounds.x + 140, levelButtonBounds.y, 60, 60); // Position Level 2 next to Level 1
        levelButton3Bounds = new Rectangle(levelButton2Bounds.x + 140, levelButton2Bounds.y, 60, 60); // Position Level 3 next to Level 2
        backButtonBounds = new Rectangle(20, Gdx.graphics.getHeight() - 80, 60, 60); // Position the back button at the top left corner

        showLevelDisplayScreen = false; // Initially on home screen
        showLevel1 = false; // Initially Level_1 is not shown
        isLoaded = false;   // Initially, loading is not complete
        loadingProgress = 0; // Start loading progress at 0

        font = new BitmapFont();  // Load default font
        font.getData().setScale(2);  // Scale up the font size to make it appear bold
        font.setColor(Color.WHITE);  // Set font color to white

        level_1 = new Level_1(this); // Initialize Level_1 instance
        level_1.create(); // Call create method of Level_1 to set up its resources
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isLoaded) {
            // Simulate loading process
            loadingProgress += Gdx.graphics.getDeltaTime() * 60; // Adjust speed as necessary
            if (loadingProgress >= 100) {
                isLoaded = true; // Mark as loaded when progress reaches 100%
            }

            // Draw the background and loading bar
            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Draw the "LOADING" text above the loading bar
            font.draw(batch, "LOADING", (Gdx.graphics.getWidth() - 150) / 2, 100); // Adjust x and y positions

            batch.end();

            // Draw the loading bar
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 1, 0, 1); // Green color for the loading bar
            float barWidth = 300;
            float barHeight = 20;
            float barX = (Gdx.graphics.getWidth() - barWidth) / 2;
            float barY = 50; // Positioned 50 pixels from the bottom
            shapeRenderer.rect(barX, barY, loadingProgress / 100 * barWidth, barHeight);
            shapeRenderer.end();

        } else if (showLevel1) {
            // Render Level_1 if we are in that screen
            level_1.render();
        } else {
            batch.begin();

            if (!showLevelDisplayScreen) {
                // Home screen with next button
                batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(nextButton, nextButtonBounds.x - nextButtonBounds.radius * 0.7f, nextButtonBounds.y - nextButtonBounds.radius * 0.7f, nextButtonBounds.radius * 2 * 0.7f, nextButtonBounds.radius * 2 * 0.7f);
            } else {
                // Level display screen with level buttons and back button
                batch.draw(levelDisplayBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(levelButton, levelButtonBounds.x, levelButtonBounds.y, levelButtonBounds.width, levelButtonBounds.height); // Level 1 button
                batch.draw(levelButton2, levelButton2Bounds.x, levelButton2Bounds.y, levelButton2Bounds.width, levelButton2Bounds.height); // Level 2 button
                batch.draw(levelButton3, levelButton3Bounds.x, levelButton3Bounds.y, levelButton3Bounds.width, levelButton3Bounds.height); // Level 3 button
                batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height); // Draw the back button
            }

            batch.end();

            if (Gdx.input.isTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert y-axis

                if (nextButtonBounds.contains(touchX, touchY) && !showLevelDisplayScreen) {
                    showLevelDisplayScreen = true; // Transition to level display screen
                }

                if (levelButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevel1 = true; // Transition to Level_1
                }

                if (backButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevelDisplayScreen = false; // Go back to the home screen
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();
        levelDisplayBackground.dispose();
        nextButton.dispose();
        levelButton.dispose();
        levelButton2.dispose();  // Dispose Level 2 resources
        levelButton3.dispose();  // Dispose Level 3 resources
        backButton.dispose();  // Dispose of back button resources
        font.dispose();        // Dispose of font resources
        level_1.dispose(); // Dispose resources of Level_1
    }
}



/*
package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;  // For rendering text
import com.badlogic.gdx.graphics.Color;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Texture background;
    Texture levelDisplayBackground;  // Background for level display screen
    Texture nextButton;
    Texture levelButton;
    Texture levelButton2;            // Texture for Level 2 (lvle2.png)
    Texture levelButton3;            // Texture for Level 3 (lvle3.png)
    Texture backButton;              // Texture for the back button
    Circle nextButtonBounds;
    Rectangle levelButtonBounds;
    Rectangle levelButton2Bounds;    // Bounds for Level 2 button
    Rectangle levelButton3Bounds;    // Bounds for Level 3 button
    Rectangle backButtonBounds;      // Bounds for the back button
    boolean showLevelDisplayScreen; // Controls showing level display screen
    boolean showLevel1;             // Controls showing Level_1 class
    boolean isLoaded;               // Indicates whether loading is complete
    float loadingProgress;          // Tracks the loading progress
    BitmapFont font;                // Font for rendering "LOADING" text

    Level_1 level_1; // Instance of Level_1 class

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("homesc.png");
        levelDisplayBackground = new Texture("leveldisplayscreen.png");
        nextButton = new Texture("playbutton1.png");
        levelButton = new Texture("lvle.png");
        levelButton2 = new Texture("lvle2.png");  // Load Level 2 texture
        levelButton3 = new Texture("lvle3.png");  // Load Level 3 texture
        backButton = new Texture("back.png"); // Load the back button texture

        nextButtonBounds = new Circle(Gdx.graphics.getWidth() / 2, 225, 50);
        levelButtonBounds = new Rectangle((Gdx.graphics.getWidth() - 100) / 2 - 150, (Gdx.graphics.getHeight() - 100) / 2, 60, 60);
        levelButton2Bounds = new Rectangle(levelButtonBounds.x + 140, levelButtonBounds.y, 60, 60); // Position Level 2 next to Level 1
        levelButton3Bounds = new Rectangle(levelButton2Bounds.x + 140, levelButton2Bounds.y, 60, 60); // Position Level 3 next to Level 2
        backButtonBounds = new Rectangle(20, Gdx.graphics.getHeight() - 80, 60, 60); // Position the back button at the top left corner

        showLevelDisplayScreen = false; // Initially on home screen
        showLevel1 = false; // Initially Level_1 is not shown
        isLoaded = false;   // Initially, loading is not complete
        loadingProgress = 0; // Start loading progress at 0

        font = new BitmapFont();  // Load default font
        font.getData().setScale(2);  // Scale up the font size to make it appear bold
        font.setColor(Color.WHITE);  // Set font color to white

        level_1 = new Level_1(this); // Initialize Level_1 instance
        level_1.create(); // Call create method of Level_1 to set up its resources
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isLoaded) {
            // Simulate loading process
            loadingProgress += Gdx.graphics.getDeltaTime() * 60; // Adjust speed as necessary
            if (loadingProgress >= 100) {
                isLoaded = true; // Mark as loaded when progress reaches 100%
            }

            // Draw the background and loading bar
            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Draw the "LOADING" text above the loading bar
            font.draw(batch, "LOADING", (Gdx.graphics.getWidth() - 150) / 2, 100); // Adjust x and y positions

            batch.end();

            // Draw the loading bar
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 1, 0, 1); // Green color for the loading bar
            float barWidth = 300;
            float barHeight = 20;
            float barX = (Gdx.graphics.getWidth() - barWidth) / 2;
            float barY = 50; // Positioned 50 pixels from the bottom
            shapeRenderer.rect(barX, barY, loadingProgress / 100 * barWidth, barHeight);
            shapeRenderer.end();

        } else if (showLevel1) {
            // Render Level_1 if we are in that screen
            level_1.render();
        } else {
            batch.begin();

            if (!showLevelDisplayScreen) {
                // Home screen with next button
                batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(nextButton, nextButtonBounds.x - nextButtonBounds.radius * 0.7f, nextButtonBounds.y - nextButtonBounds.radius * 0.7f, nextButtonBounds.radius * 2 * 0.7f, nextButtonBounds.radius * 2 * 0.7f);
            } else {
                // Level display screen with level buttons and back button
                batch.draw(levelDisplayBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(levelButton, levelButtonBounds.x, levelButtonBounds.y, levelButtonBounds.width, levelButtonBounds.height); // Level 1 button
                batch.draw(levelButton2, levelButton2Bounds.x, levelButton2Bounds.y, levelButton2Bounds.width, levelButton2Bounds.height); // Level 2 button
                batch.draw(levelButton3, levelButton3Bounds.x, levelButton3Bounds.y, levelButton3Bounds.width, levelButton3Bounds.height); // Level 3 button
                batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height); // Draw the back button
            }

            batch.end();

            if (Gdx.input.isTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert y-axis

                if (nextButtonBounds.contains(touchX, touchY) && !showLevelDisplayScreen) {
                    showLevelDisplayScreen = true; // Transition to level display screen
                }

                if (levelButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevel1 = true; // Transition to Level_1
                }

                if (backButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevelDisplayScreen = false; // Go back to the home screen
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();
        levelDisplayBackground.dispose();
        nextButton.dispose();
        levelButton.dispose();
        levelButton2.dispose();  // Dispose Level 2 resources
        levelButton3.dispose();  // Dispose Level 3 resources
        backButton.dispose();  // Dispose of back button resources
        font.dispose();        // Dispose of font resources
        level_1.dispose(); // Dispose resources of Level_1
    }
}



/*

package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;  // For rendering text
import com.badlogic.gdx.graphics.Color;


public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Texture background;
    Texture levelDisplayBackground;  // Background for level display screen
    Texture nextButton;
    Texture levelButton;
    Texture backButton;              // Texture for the back button
    Circle nextButtonBounds;
    Rectangle levelButtonBounds;
    Rectangle backButtonBounds;      // Bounds for the back button
    boolean showLevelDisplayScreen; // Controls showing level display screen
    boolean showLevel1;             // Controls showing Level_1 class
    boolean isLoaded;               // Indicates whether loading is complete
    float loadingProgress;          // Tracks the loading progress
    BitmapFont font;                // Font for rendering "LOADING" text

    Level_1 level_1; // Instance of Level_1 class

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("homesc.png");
        levelDisplayBackground = new Texture("leveldisplayscreen.png");
        nextButton = new Texture("playbutton1.png");
        levelButton = new Texture("lvle.png");
        backButton = new Texture("back.png"); // Load the back button texture

        nextButtonBounds = new Circle(Gdx.graphics.getWidth() / 2, 225, 50);
        levelButtonBounds = new Rectangle((Gdx.graphics.getWidth() - 100) / 2 - 50, (Gdx.graphics.getHeight() - 100) / 2, 60, 60);
        backButtonBounds = new Rectangle(20, Gdx.graphics.getHeight() - 80, 60, 60); // Position the back button at the top left corner

        showLevelDisplayScreen = false; // Initially on home screen
        showLevel1 = false; // Initially Level_1 is not shown
        isLoaded = false;   // Initially, loading is not complete
        loadingProgress = 0; // Start loading progress at 0

        font = new BitmapFont();  // Load default font
        font.getData().setScale(2);  // Scale up the font size to make it appear bold
        font.setColor(Color.WHITE);  // Set font color to white

        level_1 = new Level_1(this); // Initialize Level_1 instance
        level_1.create(); // Call create method of Level_1 to set up its resources
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isLoaded) {
            // Simulate loading process
            loadingProgress += Gdx.graphics.getDeltaTime() * 60; // Adjust speed as necessary
            if (loadingProgress >= 100) {
                isLoaded = true; // Mark as loaded when progress reaches 100%
            }

            // Draw the background and loading bar
            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Draw the "LOADING" text above the loading bar
            font.draw(batch, "LOADING", (Gdx.graphics.getWidth() - 150) / 2, 100); // Adjust x and y positions

            batch.end();

            // Draw the loading bar
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 1, 0, 1); // Green color for the loading bar
            float barWidth = 300;
            float barHeight = 20;
            float barX = (Gdx.graphics.getWidth() - barWidth) / 2;
            float barY = 50; // Positioned 50 pixels from the bottom
            shapeRenderer.rect(barX, barY, loadingProgress / 100 * barWidth, barHeight);
            shapeRenderer.end();

        } else if (showLevel1) {
            // Render Level_1 if we are in that screen
            level_1.render();
        } else {
            batch.begin();

            if (!showLevelDisplayScreen) {
                // Home screen with next button
                batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(nextButton, nextButtonBounds.x - nextButtonBounds.radius * 0.7f, nextButtonBounds.y - nextButtonBounds.radius * 0.7f, nextButtonBounds.radius * 2 * 0.7f, nextButtonBounds.radius * 2 * 0.7f);
            } else {
                // Level display screen with level button and back button
                batch.draw(levelDisplayBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                batch.draw(levelButton, levelButtonBounds.x, levelButtonBounds.y, levelButtonBounds.width, levelButtonBounds.height);
                batch.draw(backButton, backButtonBounds.x, backButtonBounds.y, backButtonBounds.width, backButtonBounds.height); // Draw the back button
            }

            batch.end();

            if (Gdx.input.isTouched()) {
                float touchX = Gdx.input.getX();
                float touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert y-axis

                if (nextButtonBounds.contains(touchX, touchY) && !showLevelDisplayScreen) {
                    showLevelDisplayScreen = true; // Transition to level display screen
                }

                if (levelButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevel1 = true; // Transition to Level_1
                }

                if (backButtonBounds.contains(touchX, touchY) && showLevelDisplayScreen) {
                    showLevelDisplayScreen = false; // Go back to the home screen
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();
        levelDisplayBackground.dispose();
        nextButton.dispose();
        levelButton.dispose();
        backButton.dispose();  // Dispose of back button resources
        font.dispose();        // Dispose of font resources
        level_1.dispose(); // Dispose resources of Level_1
    }
}
*/




