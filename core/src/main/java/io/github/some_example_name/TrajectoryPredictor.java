// TrajectoryPredictor.java
package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class TrajectoryPredictor {
    private static final float PPM = 100f; // Pixels per meter
    private ShapeRenderer shapeRenderer;
    private World world;
    private Vector2 catapultPosition;
    private Vector2 rubberEnd;
    private boolean isDragging;
    private boolean birdLaunched;
    private int currentBirdIndex;
    private Body[] birdBodies;

    public TrajectoryPredictor(ShapeRenderer shapeRenderer, World world, Vector2 catapultPosition, Vector2 rubberEnd, boolean isDragging, boolean birdLaunched, int currentBirdIndex, Body[] birdBodies) {
        this.shapeRenderer = shapeRenderer;
        this.world = world;
        this.catapultPosition = catapultPosition;
        this.rubberEnd = rubberEnd;
        this.isDragging = isDragging;
        this.birdLaunched = birdLaunched;
        this.currentBirdIndex = currentBirdIndex;
        this.birdBodies = birdBodies;
    }

    public void drawTrajectoryPrediction() {
        if (isDragging && !birdLaunched && currentBirdIndex < birdBodies.length) {
            Vector2 dragOrigin = catapultPosition.cpy().add(0, 0.25f);
            Vector2 releaseVector = dragOrigin.cpy().sub(rubberEnd);

            // Calculate launch parameters
            float pullDistance = releaseVector.len();
            float maxPullDistance = 40f / PPM; // DRAG_RADIUS
            float launchForce = Math.min(10f, pullDistance * 2f);

            // Normalize and scale the release vector
            releaseVector.nor().scl(launchForce);

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
}
