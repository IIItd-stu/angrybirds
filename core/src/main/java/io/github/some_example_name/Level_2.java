package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector3;

public class Level_2 {
    private Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private static final float PPM = 100f; // Pixels per meter
    private final float WORLD_WIDTH = Gdx.graphics.getWidth() / PPM;
    private final float WORLD_HEIGHT = Gdx.graphics.getHeight() / PPM;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Array<Body> bodiesToDestroy;

    private Texture backgroundTexture;
    private Texture catapultTexture;
    private Texture birdTexture;
    private Texture bird2Texture;
    private Texture blockTexture;
    private Texture blockHorizontalTexture;

    private Texture helmetPigTexture;

    private Texture pauseButtonTexture;
    private Texture quitButtonTexture;
    private Rectangle pauseButtonBounds;
    private Rectangle quitButtonBounds;
    private boolean isPaused;
    private boolean showQuitButton;

    private Texture homeButtonTexture;
    private Texture playAgainButtonTexture;
    private Rectangle homeButtonBounds;
    private Rectangle playAgainButtonBounds;

    private Body groundBody;
    // Modify birdBodies array to hold four birds
    private Body[] birdBodies = new Body[4];
    private Body[] blockBodies = new Body[6];
    // Modify pigBody to hold two pigs
    private Body[] pigBodies = new Body[2];

    private int currentBirdIndex = 0;
    private boolean isDragging = false;
    private Vector2 rubberEnd = new Vector2();

    private final float GROUND_Y = 65f / PPM;
    private final Vector2 catapultPosition = new Vector2(100f / PPM, GROUND_Y + 40f / PPM);
    private final float DRAG_RADIUS = 40f / PPM;
    private final float TIME_STEP = 1/60f;
    private final int VELOCITY_ITERATIONS = 6;
    private final int POSITION_ITERATIONS = 3;

    private boolean birdLaunched = false;
    private float launchTimer = 0;
    private static final float RESET_TIME = 2f;

    // Pig related
    private Body pigBody;
    private Texture pigTexture;
    private boolean showEndScreen = false;

    private boolean showWinningScreen = false;
    private boolean showLosingScreen = false;
    private Texture winningScreenTexture;
    private Texture losingScreenTexture;

    private BitmapFont scoreFont;
    private int score = 0;

    private float winningScreenDelayTimer = 0;
    private static final float WINNING_SCREEN_DELAY = 2f;

    public Level_2(Main game) {
        this.game = game;
    }

    public void create() {

        System.out.println("hello from Level_2");
        if (batch == null) {
            batch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();

            // Load textures only if they haven't been loaded
            // Load textures
            backgroundTexture = new Texture("insidelvle2.jpg");
            catapultTexture = new Texture("catapult2.png");
            birdTexture = new Texture("bird.png");
            bird2Texture = new Texture("bird2.png");
            blockTexture = new Texture("wb1.png");
            blockHorizontalTexture = new Texture("wbh.png");
            pauseButtonTexture = new Texture("pse.png");
            quitButtonTexture = new Texture("quit1.png");
            pigTexture = new Texture("pig.png");
            helmetPigTexture = new Texture("helmetpig.png"); // New texture// New texture
            winningScreenTexture = new Texture("winning.png");
            losingScreenTexture = new Texture("losing.png");
            homeButtonTexture = new Texture("home.png");
            playAgainButtonTexture = new Texture("retry.png");
        }



        bodiesToDestroy = new Array<>();

        // Position buttons
        pauseButtonBounds = new Rectangle(10, Gdx.graphics.getHeight() - 60, 50, 50);
        quitButtonBounds = new Rectangle(10, Gdx.graphics.getHeight() - 120, 50, 50);
        homeButtonBounds = new Rectangle(200, 100, 100, 100);
        playAgainButtonBounds = new Rectangle(350, 100, 100, 100);

        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(2f);
        scoreFont.setColor(Color.WHITE);

        // Create physics world
        world = new World(new Vector2(0, -10f), true);
        debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(createContactListener());

        // Create game objects
        createGround();
        createBirds();
        createPigs();
        createBlocks();

        // Reset game state
        isPaused = false;
        showQuitButton = false;
        currentBirdIndex = 0;
        isDragging = false;
        birdLaunched = false;
        launchTimer = 0;


    }




    private ContactListener createContactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                Object userDataA = contact.getFixtureA().getUserData();
                Object userDataB = contact.getFixtureB().getUserData();

                float relativeVelocity = contact.getWorldManifold().getNormal().dot(
                    bodyA.getLinearVelocity().cpy().sub(bodyB.getLinearVelocity())
                );

                // Always check birds first


                // Handle pigs and blocks with health reduction
                if (Math.abs(relativeVelocity) > 5f) {

                    if (userDataA != null && userDataA instanceof Bird) {
                        // Always destroy the bird
                        if (!bodiesToDestroy.contains(bodyA, true)) {
                            bodiesToDestroy.add(bodyA);
                        }
                    }
                    if (userDataB != null && userDataB instanceof Bird) {
                        // Always destroy the bird
                        if (!bodiesToDestroy.contains(bodyB, true)) {
                            bodiesToDestroy.add(bodyB);
                        }
                    }

                    // Check and reduce health for pigs
                    if (userDataA instanceof Pig) {
                        Pig pig = (Pig)userDataA;
                        Bird bird = (Bird)userDataB;
                        pig.setHealth(pig.getHealth() - bird.getHitPoint());
                        if (pig.getHealth() <= 0 && !bodiesToDestroy.contains(bodyA, true)) {
                            bodiesToDestroy.add(bodyA);
                        }
                    }
                    if (userDataB instanceof Pig) {
                        Pig pig = (Pig)userDataB;
                        Bird bird = (Bird)userDataA;
                        pig.setHealth(pig.getHealth() - bird.getHitPoint());
                        if (pig.getHealth() <= 0 && !bodiesToDestroy.contains(bodyB, true)) {
                            bodiesToDestroy.add(bodyB);
                        }
                    }

                    // Check and reduce health for blocks
                    if (userDataA instanceof Block) {
                        Block block = (Block)userDataA;
                        Bird    bird = (Bird)userDataB;
                        block.setHealth(block.getHealth() - bird.getHitPoint());
                        if (block.getHealth() <= 0 && !bodiesToDestroy.contains(bodyA, true)) {
                            bodiesToDestroy.add(bodyA);
                        }
                    }
                    if (userDataB instanceof Block) {
                        Block block = (Block)userDataB;
                        Bird bird = (Bird)userDataA;
                        block.setHealth(block.getHealth() - bird.getHitPoint());
                        if (block.getHealth() <= 0 && !bodiesToDestroy.contains(bodyB, true)) {
                            bodiesToDestroy.add(bodyB);
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        };
    }


    private void handleRemainingBirdsAndScore() {

        boolean allPigsDestroyed = true;
        for (Body pigBody : pigBodies) {
            if (pigBody != null) {
                allPigsDestroyed = false;
                break;
            }
        }

        if (allPigsDestroyed) {
            showWinningScreen = true;
        } else {
            showLosingScreen = true;
        }

        isPaused = true; // Stop the game
    }

    private void createGround() {
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(WORLD_WIDTH / 2, GROUND_Y / 2);

        groundBody = world.createBody(groundDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(WORLD_WIDTH / 2, GROUND_Y / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.1f;

        groundBody.createFixture(fixtureDef);
        groundShape.dispose();
    }





    private void createBirds() {
        float birdSpacing = 40f / PPM;

        Bird yellow = new Bird(world, catapultPosition, "bird.png",2);
        Body birdBody1 = yellow.createBody(new Vector2(0, 0), catapultPosition, 0, birdSpacing);
        birdBodies[0] = birdBody1;
        birdBodies[0].getFixtureList().first().setUserData(yellow);

        Bird red1 = new Bird(world, catapultPosition, "bird.png",1);
        Body birdBody2 = red1.createBody(new Vector2(0, 0), catapultPosition, 1, birdSpacing);
        birdBodies[1] = birdBody2;
        birdBodies[1].getFixtureList().first().setUserData(red1);

        Bird red2 = new Bird(world, catapultPosition, "bird.png",1);
        Body birdBody3 = red2.createBody(new Vector2(0, 0), catapultPosition, 2, birdSpacing);
        birdBodies[2] = birdBody3;
        birdBodies[2].getFixtureList().first().setUserData(red2);

        Bird red3 = new Bird(world, catapultPosition, "bird.png",1);
        Body birdBody4 = red3.createBody(new Vector2(0, 0), catapultPosition, 3, birdSpacing);
        birdBodies[3] = birdBody4;
        birdBodies[3].getFixtureList().first().setUserData(red3);

    }


    private void createBlocks() {

        Block block1 = new Block(world,  "wb1.png", 2);
        blockBodies[0] = block1.createBodyVertical(new Vector2(350f / PPM, GROUND_Y + 40f / PPM));
        blockBodies[0].getFixtureList().first().setUserData(block1);

        Block block2 = new Block(world,  "wb1.png", 2);
        blockBodies[1] = block2.createBodyVertical(new Vector2(390f / PPM, GROUND_Y + 40f / PPM));
        blockBodies[1].getFixtureList().first().setUserData(block2);

        Block block3 = new Block(world,  "wbh.png", 2);
        blockBodies[2] = block3.createBodyHorizontal(new Vector2(370f / PPM, GROUND_Y + 80f / PPM));
        blockBodies[2].getFixtureList().first().setUserData(block3);

        Block block4 = new Block(world,  "wb1.png", 2);
        blockBodies[3] = block4.createBodyVertical(new Vector2(450f / PPM, GROUND_Y + 40f / PPM));
        blockBodies[3].getFixtureList().first().setUserData(block4);

        Block block5 = new Block(world,  "wb1.png", 2);
        blockBodies[4] = block5.createBodyVertical(new Vector2(490f / PPM, GROUND_Y + 40f / PPM));
        blockBodies[4].getFixtureList().first().setUserData(block5);

        Block block6 = new Block(world,  "wbh.png", 2);
        blockBodies[5] = block6.createBodyHorizontal(new Vector2(470f / PPM, GROUND_Y + 80f / PPM));
        blockBodies[5].getFixtureList().first().setUserData(block6);

    }

    private void createPigs() {

        Pig pig1 = new Pig(world, "pig.png", 1);
        pigBodies[0] = pig1.createBody(new Vector2(370f / PPM, GROUND_Y + 90f / PPM));
        pigBodies[0].getFixtureList().first().setUserData(pig1);

        Pig pig2 = new Pig(world, "helmetpig.png", 1);
        pigBodies[1] = pig2.createBody(new Vector2(470f / PPM, GROUND_Y + 90f / PPM));
        pigBodies[1].getFixtureList().first().setUserData(pig2);

        // Set initial angular velocity to zero to prevent rolling
        for (Body pigBody : pigBodies) {
            if (pigBody != null) {
                pigBody.setAngularVelocity(0);
            }
        }
    }






    public void render() {
        handlePauseInput();

        if (!isPaused) {
            updatePhysics();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(game.camera.combined);
        batch.begin();

        if (showWinningScreen) {
            batch.draw(winningScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(homeButtonTexture, homeButtonBounds.x, homeButtonBounds.y, homeButtonBounds.width, homeButtonBounds.height);
            batch.draw(playAgainButtonTexture, playAgainButtonBounds.x, playAgainButtonBounds.y, playAgainButtonBounds.width, playAgainButtonBounds.height);
            batch.end();
            handleEndScreenInput();
            return;
        }

        if (showLosingScreen) {
            batch.draw(losingScreenTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(homeButtonTexture, homeButtonBounds.x, homeButtonBounds.y, homeButtonBounds.width, homeButtonBounds.height);
            batch.draw(playAgainButtonTexture, playAgainButtonBounds.x, playAgainButtonBounds.y, playAgainButtonBounds.width, playAgainButtonBounds.height);
            batch.end();
            handleEndScreenInput();
            return;
        }

        // Existing rendering logic for the game
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(catapultTexture, catapultPosition.x * PPM - 40, catapultPosition.y * PPM - 40, 80, 80);

        // Draw birds
        for (int i = 0; i < birdBodies.length; i++) {
            if (birdBodies[i] != null && !bodiesToDestroy.contains(birdBodies[i], true)) {
                Texture birdTex = (i == 0) ? birdTexture : bird2Texture;
                Body birdBody = birdBodies[i];

                float birdX = birdBody.getPosition().x * PPM - 12.5f;
                float birdY = birdBody.getPosition().y * PPM - 12.5f;
                float rotation = (float) Math.toDegrees(birdBody.getAngle());

                batch.draw(birdTex,
                    birdX, birdY,
                    12.5f, 12.5f,
                    25, 25,
                    1, 1,
                    rotation,
                    0, 0,
                    birdTex.getWidth(), birdTex.getHeight(),
                    false, false);
            }
        }

//         Draw blocks
        for (Body blockBody : blockBodies) {
            if (blockBody != null && !bodiesToDestroy.contains(blockBody, true)) {
                // Select the appropriate texture based on block orientation
                Texture blockTex = (blockBody == blockBodies[2] || blockBody == blockBodies[5]) ? blockHorizontalTexture : blockTexture;

                // Set block dimensions based on orientation
                float blockWidth = (blockBody == blockBodies[2] || blockBody == blockBodies[5]) ? 40 : 10;
                float blockHeight = (blockBody == blockBodies[2] || blockBody == blockBodies[5]) ? 20 : 80;

                // Calculate block position in pixels
                float blockX = blockBody.getPosition().x * PPM - blockWidth / 2;
                float blockY = blockBody.getPosition().y * PPM - blockHeight / 2;

                // Calculate block rotation in degrees
                float rotation = (float) Math.toDegrees(blockBody.getAngle());

                // Draw the block
                batch.draw(blockTex,
                    blockX, blockY,
                    blockWidth / 2, blockHeight / 2,
                    blockWidth, blockHeight,
                    1, 1,
                    rotation,
                    0, 0,
                    blockTex.getWidth(), blockTex.getHeight(),
                    false, false);
            }
        }


        // Draw pigs
        for (int i = 0; i < pigBodies.length; i++) {
            if (pigBodies[i] != null && !bodiesToDestroy.contains(pigBodies[i], true)) {
                Texture pigTex = (i == 0) ? pigTexture : helmetPigTexture;
                Body pigBody = pigBodies[i];

                float pigX = pigBody.getPosition().x * PPM - 15f;
                float pigY = pigBody.getPosition().y * PPM - 15f;
                float rotation = (float) Math.toDegrees(pigBody.getAngle());

                batch.draw(pigTex,
                    pigX, pigY,
                    15f, 15f,
                    30, 30,
                    1, 1,
                    rotation,
                    0, 0,
                    pigTex.getWidth(), pigTex.getHeight(),
                    false, false);
            }
        }

        batch.draw(pauseButtonTexture,
            pauseButtonBounds.x,
            pauseButtonBounds.y,
            pauseButtonBounds.width,
            pauseButtonBounds.height
        );

        if (showQuitButton) {
            batch.draw(quitButtonTexture,
                quitButtonBounds.x,
                quitButtonBounds.y,
                quitButtonBounds.width,
                quitButtonBounds.height
            );
        }

        // Draw score
        scoreFont.draw(batch, "Score: " + score, Gdx.graphics.getWidth() - 170, Gdx.graphics.getHeight() - 10);

        batch.end();

        if (isDragging && !showEndScreen) {
            shapeRenderer.setProjectionMatrix(game.camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BROWN);

            // Calculate rubber band start position (0.25f above catapult)
            Vector2 rubberStart = new Vector2(
                catapultPosition.x,
                catapultPosition.y + 0.25f
            );

            // Calculate the rubber band stretch factor based on distance from the raised position
            Vector2 dragVector = new Vector2(
                rubberEnd.x - rubberStart.x,
                rubberEnd.y - rubberStart.y
            );
            float stretchDistance = dragVector.len();
            float stretchFactor = 1 + (stretchDistance / 100f);

            // Calculate the width of the rubber band based on stretch
            float rubberWidth = 5 * stretchFactor;

            // Draw the rubber band with variable width from the raised position
            shapeRenderer.rectLine(
                rubberStart.x * PPM,
                rubberStart.y * PPM,
                rubberEnd.x * PPM,
                rubberEnd.y * PPM,
                rubberWidth
            );
            shapeRenderer.end();
        }

        if (isDragging && !showEndScreen) {
            drawTrajectoryPrediction();
        }

        if (!isPaused && !showEndScreen) {
            handleInput();
        }
    }

    private void handleEndScreenInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (homeButtonBounds.contains(touch.x, touch.y)) {
                // Properly dispose of current level resources
                dispose();

                // Reset game state
                game.currentLevel = 0;
                game.showLevelDisplayScreen = true;

                // Create a new Level_1 instance if needed
                if (game.level_1 != null) {
                    game.level_1 = new Level_1(game);
                }
            } else if (playAgainButtonBounds.contains(touch.x, touch.y)) {
                // Reset the level fully
                resetLevel(); // Reset the level state and reinitialize resources
            }
        }
    }


    private void updatePhysics() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

        // Clean up destroyed bodies
        Array<Body> bodiesToRemove = new Array<>();
        for (Body body : bodiesToDestroy) {
            if (body != null) {
                bodiesToRemove.add(body);

                // Update references
                for (int i = 0; i < pigBodies.length; i++) {
                    if (body == pigBodies[i]) {
                        pigBodies[i] = null;
                    }
                }

                for (int i = 0; i < blockBodies.length; i++) {
                    if (body == blockBodies[i]) {
                        blockBodies[i] = null;
                    }
                }

                for (int i = 0; i < birdBodies.length; i++) {
                    if (body == birdBodies[i]) {
                        birdBodies[i] = null;
                    }
                }
            }
        }

        // Actually destroy bodies
        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }

        bodiesToDestroy.clear();

        // Handle bird reset timer
        if (birdLaunched) {
            launchTimer += TIME_STEP;
            if (launchTimer >= RESET_TIME) {
                birdLaunched = false;
                launchTimer = 0;
                if (currentBirdIndex < birdBodies.length) {
                    resetBirdPosition();
                }
            }
        }

        // Check for game over due to all birds used
        if (currentBirdIndex >= birdBodies.length && !birdLaunched) {
            handleRemainingBirdsAndScore();
        }

        // Handle winning screen delay timer
        boolean allPigsDead = true;
        for (Body pigBody : pigBodies) {
            if (pigBody != null) {
                allPigsDead = false;
                break;
            }
        }

        if (allPigsDead && !showWinningScreen) {
            if (winningScreenDelayTimer <= 0) {
                winningScreenDelayTimer = WINNING_SCREEN_DELAY;
            } else {
                winningScreenDelayTimer -= TIME_STEP;
                if (winningScreenDelayTimer <= 0) {
                    showWinningScreen = true;
                    isPaused = true; // Stop the game
                }
            }
        }
    }

    private void resetBirdPosition() {
        if (currentBirdIndex < birdBodies.length && birdBodies[currentBirdIndex] != null) {
            // Reset to the raised position (0.25f above catapult)
            birdBodies[currentBirdIndex].setTransform(
                catapultPosition.x,
                catapultPosition.y + 0.25f,
                0
            );
            birdBodies[currentBirdIndex].setLinearVelocity(0, 0);
            birdBodies[currentBirdIndex].setAngularVelocity(0);
        }
    }

    private void handleInput() {
        if (currentBirdIndex >= birdBodies.length || birdBodies[currentBirdIndex] == null) return;

        Vector2 dragOrigin = catapultPosition.cpy().add(0, 0.25f);

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
            Vector2 touchPoint = new Vector2(touchX / PPM, touchY / PPM);

            if (!isDragging && !birdLaunched &&
                birdBodies[currentBirdIndex].getPosition().dst(touchPoint) < DRAG_RADIUS) {
                isDragging = true;
            }

            if (isDragging) {
                Vector2 displacement = touchPoint.cpy().sub(dragOrigin);
                float maxLength = DRAG_RADIUS;
                if (displacement.len() > maxLength) {
                    displacement.setLength(maxLength);
                }

                rubberEnd.set(dragOrigin.cpy().add(displacement));

                birdBodies[currentBirdIndex].setTransform(rubberEnd.x, rubberEnd.y, 0);
                birdBodies[currentBirdIndex].setLinearVelocity(0, 0);
            }
        } else if (isDragging) {
            launchBird();
        }
    }



    private void drawTrajectoryPrediction() {
        if (isDragging && !birdLaunched && currentBirdIndex < birdBodies.length) {
            Vector2 dragOrigin = catapultPosition.cpy().add(0, 0.25f);
            Vector2 releaseVector = dragOrigin.cpy().sub(rubberEnd);

            // Calculate launch parameters
            float pullDistance = releaseVector.len();
            float maxPullDistance = DRAG_RADIUS;
            float launchForce = Math.min(10f, pullDistance * 2f);

            // Normalize and scale the release vector
            releaseVector.nor().scl(launchForce);

            shapeRenderer.setProjectionMatrix(game.camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);

            // Initial conditions
            Vector2 currentPos = rubberEnd.cpy();
            Vector2 velocity = releaseVector.cpy();
            Vector2 gravity = world.getGravity().cpy();

            // Prediction parameters
            float timeStep = 1 / 60f;  // Smaller time step for smoother prediction
            float totalSimTime = 2f;  // Limit prediction time
            int maxPoints = 60;  // Limit number of trajectory points
            float pointSpacing = totalSimTime / maxPoints;

            // Store and draw trajectory points
            for (int i = 0; i < maxPoints; i++) {
                // Simulate physics for the next position
                Vector2 nextPos = new Vector2(
                    currentPos.x + velocity.x * timeStep,
                    currentPos.y + velocity.y * timeStep + 0.5f * gravity.y * timeStep * timeStep
                );

                // Update velocity for the next step
                velocity.add(gravity.scl(timeStep));

                // Draw a small circle at each point
                shapeRenderer.circle(nextPos.x * PPM, nextPos.y * PPM, 2); // Small dot radius: 2

                currentPos = nextPos;
            }

            shapeRenderer.end();
        }
    }




    private void launchBird() {
        Vector2 dragOrigin = catapultPosition.cpy().add(0, 0.25f);
        Vector2 releaseVector = dragOrigin.cpy().sub(rubberEnd);

        float pullDistance = releaseVector.len();
        float launchForce = Math.min(4f, pullDistance * 2f);

        releaseVector.nor().scl(launchForce);

        birdBodies[currentBirdIndex].setLinearVelocity(0, 0);
        birdBodies[currentBirdIndex].setAngularVelocity(0);
        birdBodies[currentBirdIndex].applyLinearImpulse(
            releaseVector,
            birdBodies[currentBirdIndex].getWorldCenter(),
            true
        );

        isDragging = false;
        birdLaunched = true;
        launchTimer = 0;

        if (currentBirdIndex < birdBodies.length - 1) {
            currentBirdIndex++;
        } else if (currentBirdIndex == birdBodies.length - 1) {
            currentBirdIndex++; // Move past the last bird to trigger exhaustion
        }
    }




    public void handlePauseInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseButtonBounds.contains(touch.x, touch.y)) {
                isPaused = !isPaused;
                showQuitButton = isPaused;
            } else if (showQuitButton && quitButtonBounds.contains(touch.x, touch.y)) {
                // Navigate to the level selection screen
                resetLevel();
                game.currentLevel = 0;
                game.showLevelDisplayScreen = true;
            }
        }
    }






    public void resetLevel() {
        // Dispose of existing textures
        disposeTextures();

        // Clear physics bodies
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            if (body != null) {
                world.destroyBody(body);
            }
        }

        // Clear arrays
        bodiesToDestroy.clear();

        // Reset game state
        currentBirdIndex = 0;
        isDragging = false;
        birdLaunched = false;
        launchTimer = 0;
        isPaused = false;
        score = 0;
        showQuitButton = false;
        showEndScreen = false;
        showWinningScreen = false;
        showLosingScreen = false;

        // Recreate game objects and textures
        world.dispose();
        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(createContactListener());
        createGround();
        createBirds();
        createBlocks();
        createPigs();

        // Reinitialize textures
        // Load textures
        backgroundTexture = new Texture("insidelvle2.jpg");
        catapultTexture = new Texture("catapult2.png");
        birdTexture = new Texture("bird.png");
        bird2Texture = new Texture("bird2.png");
        blockTexture = new Texture("wb1.png");
        blockHorizontalTexture = new Texture("wbh.png");
        pauseButtonTexture = new Texture("pse.png");
        quitButtonTexture = new Texture("quit1.png");
        pigTexture = new Texture("pig.png");
        helmetPigTexture = new Texture("helmetpig.png"); // New texture
// New texture
        winningScreenTexture = new Texture("winning.png");
        losingScreenTexture = new Texture("losing.png");
        homeButtonTexture = new Texture("home.png");
        playAgainButtonTexture = new Texture("retry.png");
    }


    public void disposeTextures() {
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (catapultTexture != null) catapultTexture.dispose();
        if (birdTexture != null) birdTexture.dispose();
        if (bird2Texture != null) bird2Texture.dispose();
        if (blockTexture != null) blockTexture.dispose();
        if (pauseButtonTexture != null) pauseButtonTexture.dispose();
        if (quitButtonTexture != null) quitButtonTexture.dispose();
        if (pigTexture != null) pigTexture.dispose();
        if (winningScreenTexture != null) winningScreenTexture.dispose();
        if (losingScreenTexture != null) losingScreenTexture.dispose();
        if (homeButtonTexture != null) homeButtonTexture.dispose();
        if (playAgainButtonTexture != null) playAgainButtonTexture.dispose();
    }



    public void dispose() {
        // Dispose of physics world and renderers
        if (world != null) world.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (scoreFont != null) scoreFont.dispose();

        // Dispose of textures
        disposeTextures();
    }

    public int getCurrentBirdIndex() {
        return currentBirdIndex;
    }

    public boolean isBirdLaunched() {
        return birdLaunched;
    }

    public double getLaunchTimer() {
        return launchTimer;
    }

    public int getScore() {
        return score;
    }


    public boolean isShowQuitButton() {
        return showQuitButton;
    }

    public boolean isShowEndScreen()
    {
        return showEndScreen;
    }


    public boolean isShowWinningScreen() {
        return showWinningScreen;
    }


    public boolean isShowLosingScreen() {
        return showLosingScreen;
    }

    public Object getBackgroundTexture() {
        return backgroundTexture;
    }

    public Object getCatapultTexture() {
        return catapultTexture;
    }

    public Object getBirdTexture() {
        return birdTexture;
    }

    public Object getBird2Texture() {
        return bird2Texture;
    }

    public Object getBlockTexture() {
        return blockTexture;
    }

    public Object getPauseButtonTexture() {
        return pauseButtonTexture;
    }

    public Object getQuitButtonTexture() {
        return quitButtonTexture;
    }

    public Object getPigTexture() {
        return pigTexture;
    }

    public Object getPauseButtonBounds() {
        return pauseButtonBounds;
    }

    public Object getQuitButtonBounds() {
        return quitButtonBounds;
    }

    public Object getGame() {
        return game;
    }

    public int getCurrentLevel() {
        return game.currentLevel;
    }

    public boolean isShowLevelDisplayScreen() {
        return game.showLevelDisplayScreen;
    }

    public boolean isdragging() {
        return isDragging;
    }

    public boolean ispaused() {
        return isPaused;
    }
}
