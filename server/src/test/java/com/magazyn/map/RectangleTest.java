package com.magazyn.map;

import org.junit.Assert;
import org.junit.Test;

public class RectangleTest {
    private double eps = 0.00001;

    @Test
    public void RectangleConstructorTest() {
        Rectangle rect1 = new Rectangle();
        Rectangle rect2 = new Rectangle(0.0, 0.0, 0.0, 0.0, 0.0);

        Assert.assertEquals(rect1, rect2);
        Assert.assertTrue(Math.abs(rect1.getAngle() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getWidth() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getHeight() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getTop() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getLeft() - 0.0) < eps);

        rect2 = new Rectangle(1.0, 0.0, 0.0, 0.0, 0.0);
        Assert.assertNotEquals(rect1, rect2);

        rect2 = new Rectangle(0.0, 1.0, 0.0, 0.0, 0.0);
        Assert.assertNotEquals(rect1, rect2);

        rect2 = new Rectangle(0.0, 0.0, 1.0, 0.0, 0.0);
        Assert.assertNotEquals(rect1, rect2);

        rect2 = new Rectangle(0.0, 0.0, 0.0, 1.0, 0.0);
        Assert.assertNotEquals(rect1, rect2);

        rect2 = new Rectangle(0.0, 0.0, 0.0, 0.0, 1.0);
        Assert.assertNotEquals(rect1, rect2);
    }

    @Test
    public void RectangleSetTest() {
        Rectangle rect1 = new Rectangle();
        Rectangle rect2 = new Rectangle();
        Rectangle rect3 = new Rectangle();

        Assert.assertEquals(rect1, rect2);
        Assert.assertTrue(Math.abs(rect1.getAngle() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getWidth() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getHeight() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getTop() - 0.0) < eps);
        Assert.assertTrue(Math.abs(rect1.getLeft() - 0.0) < eps);

        rect2 = new Rectangle(1.0, 0.0, 0.0, 0.0, 0.0);
        rect3.setLeft(1.0);
        Assert.assertNotEquals(rect1, rect3);
        Assert.assertEquals(rect2, rect3);
        rect3.setLeft(0.0);

        rect2 = new Rectangle(0.0, 1.0, 0.0, 0.0, 0.0);
        rect3.setTop(1.0);
        Assert.assertNotEquals(rect1, rect3);
        Assert.assertEquals(rect2, rect3);
        rect3.setTop(0.0);

        rect2 = new Rectangle(0.0, 0.0, 1.0, 0.0, 0.0);
        rect3.setWidth(1.0);
        Assert.assertNotEquals(rect1, rect3);
        Assert.assertEquals(rect2, rect3);
        rect3.setWidth(0.0);

        rect2 = new Rectangle(0.0, 0.0, 0.0, 1.0, 0.0);
        rect3.setHeight(1.0);
        Assert.assertNotEquals(rect1, rect3);
        Assert.assertEquals(rect2, rect3);
        rect3.setHeight(0.0);

        rect2 = new Rectangle(0.0, 0.0, 0.0, 0.0, 1.0);
        rect3.setAngle(1.0);
        Assert.assertNotEquals(rect1, rect3);
        Assert.assertEquals(rect2, rect3);
        rect3.setAngle(0.0);
    }

    @Test
    public void RectangleAreaTest() {
        Rectangle rect1 = new Rectangle(0.0, 0.0, 5.0, 4.0, 0.0);
        Rectangle rect2 = new Rectangle(0.0, 0.0, -5.0, 4.0, 0.0);
        Rectangle rect3 = new Rectangle(0.0, 0.0, 5.0, -4.0, 0.0);
        Rectangle rect4 = new Rectangle(0.0, 0.0, -5.0, -4.0, 0.0);

        Assert.assertTrue(Math.abs(rect1.calculateArea() - (4.0 * 5.0)) < eps);
        Assert.assertTrue(Math.abs(rect2.calculateArea() - (-1.0)) < eps);
        Assert.assertTrue(Math.abs(rect3.calculateArea() - (-1.0)) < eps);
        Assert.assertTrue(Math.abs(rect4.calculateArea() - (-1.0)) < eps);
    }
}
