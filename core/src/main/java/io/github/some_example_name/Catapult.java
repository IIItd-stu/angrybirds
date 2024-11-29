package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Catapult {
    private World world;
    private Body catapultBody;
    private Vector2 position;
    private float width;
    private float height;

    public Catapult(World world, Vector2 position, float width, float height) {
        this.world = world;
        this.position = position;
        this.width = width;
        this.height = height;
        createCatapult();
    }

    private void createCatapult() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        catapultBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        catapultBody.createFixture(shape, 0.0f);
        shape.dispose();
    }

    public Vector2 getPosition() {
        return catapultBody.getPosition();
    }

    public void setPosition(Vector2 position) {
        catapultBody.setTransform(position, catapultBody.getAngle());
    }

    public void dispose() {
        if (catapultBody != null) {
            world.destroyBody(catapultBody);
        }
    }
}
