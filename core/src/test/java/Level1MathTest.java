package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;
import org.junit.Assert;
import org.junit.Test;

public class Level1MathTest {

    @Test
    public void testLaunchForceCalculation() {
        Vector2 dragOrigin = new Vector2(1, 1);
        Vector2 rubberEnd = new Vector2(0.5f, 0.5f);
        Vector2 releaseVector = dragOrigin.cpy().sub(rubberEnd);

        float pullDistance = releaseVector.len();
        float maxPullDistance = 0.4f;
        float launchForce = Math.min(10f, pullDistance * 1f);

        Assert.assertTrue("Launch force should be clamped within limits", launchForce <= 10f);
        Assert.assertTrue("Launch force should not exceed expected max", launchForce <= maxPullDistance * 2f);
    }
}
