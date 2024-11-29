package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Block {
    private World world;
    private Body body;
    private Texture texture;
    private static final float PPM = 100f;

    int strength;

    public Block(World world,  String texturePath, int strength) {
        this.world = world;
        this.texture = new Texture(texturePath);
        this.strength = strength;
    }

    public Body createBodyVertical(Vector2 position) {
        BodyDef blockDef = new BodyDef();
        blockDef.type = BodyDef.BodyType.DynamicBody;
        blockDef.position.set(position);

        body = world.createBody(blockDef);

        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox(10f / PPM, 40f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = blockShape;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        body.setUserData("block");
        blockShape.dispose();

        return body;
    }

    public Body createBodyHorizontal(Vector2 position) {
        BodyDef blockDef = new BodyDef();
        blockDef.type = BodyDef.BodyType.DynamicBody;
        blockDef.position.set(position);

        body = world.createBody(blockDef);

        PolygonShape blockShape = new PolygonShape();
        blockShape.setAsBox(40f / PPM, 10f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = blockShape;
        fixtureDef.density = 0.8f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        blockShape.dispose();

        return body;
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

    public void setHealth(int health) {
        this.strength = health;
    }

    public int getHealth() {
        return strength;
    }
}
