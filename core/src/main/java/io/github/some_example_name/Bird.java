// Bird.java
package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Bird {
    private World world;
    private Body body;
    private Texture texture;
    private static final float PPM = 100f;
    private int hitpoints;

    public Bird(World world, Vector2 position, String texturePath, int hitpoints) {
        this.world = world;
        this.texture = new Texture(texturePath);
        this.hitpoints = hitpoints;
    }

    public Body createBody(Vector2 position, Vector2 catapultPosition, int i, float birdSpacing) {
        BodyDef birdDef = new BodyDef();
        birdDef.type = BodyDef.BodyType.DynamicBody;
        birdDef.position.set(
            catapultPosition.x + (i + 1) * birdSpacing, // Adjust position to be in front of the catapult
            catapultPosition.y + 0.25f  // Adjust initial position to match drag origin
        );
        birdDef.bullet = true;
        birdDef.fixedRotation = true;

        body = world.createBody(birdDef);

        CircleShape birdShape = new CircleShape();
        birdShape.setRadius(12.5f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = birdShape;
        fixtureDef.density = 1.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef).setUserData("bird");
        birdShape.dispose();

        return body;
    }


    public void renderBirds(Body[] birdBodies, SpriteBatch batch, Array<Body> bodiesToDestroy, int i) {

            if (birdBodies[i] != null && !bodiesToDestroy.contains(birdBodies[i], true)) {
                Bird bird = (Bird) birdBodies[i].getFixtureList().first().getUserData();
                Texture birdTex = bird.getTexture();
                Body birdBody = bird.getBody();

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

    public void launch(float impulseX, float impulseY) {
        body.applyLinearImpulse(
            new Vector2(impulseX, impulseY),
            body.getWorldCenter(),
            true
        );
    }

    public void reset(Vector2 position) {
        body.setTransform(position, 0);
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
    }

    public void setUserData(Object data) {
        body.getFixtureList().first().setUserData(data);
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }

    public int getHitPoint()
    {
        return hitpoints;
    }

    public void setHitPoints(int hitpoint)
    {

        this.hitpoints=hitpoint;
    }


}
