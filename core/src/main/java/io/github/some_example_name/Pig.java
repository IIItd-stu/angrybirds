package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Pig {
    private World world;
    private Body body;
    private Texture texture;
    private static final float PPM = 100f;
    private int health;

    public Pig(World world,  String texturePath,int health) {
        this.world = world;
        this.texture = new Texture(texturePath);
        this.health = health;
    }

    public Body createBody(Vector2 position) {
        BodyDef pigDef = new BodyDef();
        pigDef.type = BodyDef.BodyType.DynamicBody;
        pigDef.position.set(position);

        body = world.createBody(pigDef);

        CircleShape pigShape = new CircleShape();
        pigShape.setRadius(15f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = pigShape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef).setUserData("pig");
        pigShape.dispose();

        return body;
    }

    public void setUserData(Object data) {
        body.getFixtureList().first().setUserData(data);
    }

    public void getUserData() {
        body.getFixtureList().first().getUserData();
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


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
