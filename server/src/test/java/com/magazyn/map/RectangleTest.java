package com.magazyn.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RectangleTest {
	private double eps = 0.00001;

	@Test
	public void RectangleConstructorTest() {
		Rectangle rect1 = new Rectangle();
		Rectangle rect2 = new Rectangle(0.0, 0.0, 0.0, 0.0, 0.0);

		assertEquals(rect1, rect2);
		assertTrue(Math.abs(rect1.getAngle() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getWidth() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getHeight() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getTop() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getLeft() - 0.0) < eps);

		rect2 = new Rectangle(1.0, 0.0, 0.0, 0.0, 0.0);
		assertNotEquals(rect1, rect2);

		rect2 = new Rectangle(0.0, 1.0, 0.0, 0.0, 0.0);
		assertNotEquals(rect1, rect2);

		rect2 = new Rectangle(0.0, 0.0, 1.0, 0.0, 0.0);
		assertNotEquals(rect1, rect2);

		rect2 = new Rectangle(0.0, 0.0, 0.0, 1.0, 0.0);
		assertNotEquals(rect1, rect2);

		rect2 = new Rectangle(0.0, 0.0, 0.0, 0.0, 1.0);
		assertNotEquals(rect1, rect2);
	}

	@Test
	public void RectangleSetTest() {
		Rectangle rect1 = new Rectangle();
		Rectangle rect2 = new Rectangle();
		Rectangle rect3 = new Rectangle();

		assertEquals(rect1, rect2);
		assertTrue(Math.abs(rect1.getAngle() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getWidth() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getHeight() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getTop() - 0.0) < eps);
		assertTrue(Math.abs(rect1.getLeft() - 0.0) < eps);

		rect2 = new Rectangle(1.0, 0.0, 0.0, 0.0, 0.0);
		rect3.setLeft(1.0);
		assertNotEquals(rect1, rect3);
		assertEquals(rect2, rect3);
		rect3.setLeft(0.0);

		rect2 = new Rectangle(0.0, 1.0, 0.0, 0.0, 0.0);
		rect3.setTop(1.0);
		assertNotEquals(rect1, rect3);
		assertEquals(rect2, rect3);
		rect3.setTop(0.0);

		rect2 = new Rectangle(0.0, 0.0, 1.0, 0.0, 0.0);
		rect3.setWidth(1.0);
		assertNotEquals(rect1, rect3);
		assertEquals(rect2, rect3);
		rect3.setWidth(0.0);

		rect2 = new Rectangle(0.0, 0.0, 0.0, 1.0, 0.0);
		rect3.setHeight(1.0);
		assertNotEquals(rect1, rect3);
		assertEquals(rect2, rect3);
		rect3.setHeight(0.0);

		rect2 = new Rectangle(0.0, 0.0, 0.0, 0.0, 1.0);
		rect3.setAngle(1.0);
		assertNotEquals(rect1, rect3);
		assertEquals(rect2, rect3);
		rect3.setAngle(0.0);
	}

	@Test
	public void RectangleAreaTest() {
		Rectangle rect1 = new Rectangle(0.0, 0.0, 5.0, 4.0, 0.0);
		Rectangle rect2 = new Rectangle(0.0, 0.0, -5.0, 4.0, 0.0);
		Rectangle rect3 = new Rectangle(0.0, 0.0, 5.0, -4.0, 0.0);
		Rectangle rect4 = new Rectangle(0.0, 0.0, -5.0, -4.0, 0.0);

		assertTrue(Math.abs(rect1.calculateArea() - (4.0 * 5.0)) < eps);
		assertTrue(Math.abs(rect2.calculateArea() - (-1.0)) < eps);
		assertTrue(Math.abs(rect3.calculateArea() - (-1.0)) < eps);
		assertTrue(Math.abs(rect4.calculateArea() - (-1.0)) < eps);
	}

	@Test
	public void AngleTest() {
		Rectangle rect1 = new Rectangle(0.0, 0.0, 5.0, 4.0, -1.0);
		Rectangle rect2 = new Rectangle(0.0, 0.0, -5.0, 4.0, 100.0);
		Rectangle rect3 = new Rectangle(0.0, 0.0, 5.0, -4.0, -500.0);
		Rectangle rect4 = new Rectangle(0.0, 0.0, -5.0, -4.0, 2.0);

		assertTrue(rect1.getAngle() <= 2 * Math.PI);
		assertTrue(rect2.getAngle() <= 2 * Math.PI);
		assertTrue(rect3.getAngle() <= 2 * Math.PI);
		assertTrue(rect4.getAngle() <= 2 * Math.PI);

		assertTrue(rect1.getAngle() >= 0);
		assertTrue(rect2.getAngle() >= 0);
		assertTrue(rect3.getAngle() >= 0);
		assertTrue(rect4.getAngle() >= 0);
	}

	@Test
	public void ArrayTest() {
		Rectangle rect1 = new Rectangle(1.0, 2.0, 3.0, 4.0, 1.0);
		Rectangle rect2 = new Rectangle(10.0, -50.0, -5.54, 0.0, 0.0);

		double[] array = rect1.toArray();

		Rectangle rect = new Rectangle();
		rect.setLeft(array[0]);
		rect.setTop(array[1]);
		rect.setWidth(array[2]);
		rect.setHeight(array[3]);
		rect.setAngle(array[4]);

		assertEquals(rect1, rect);

		array = rect2.toArray();

		rect = new Rectangle();
		rect.setLeft(array[0]);
		rect.setTop(array[1]);
		rect.setWidth(array[2]);
		rect.setHeight(array[3]);
		rect.setAngle(array[4]);

		assertEquals(rect2, rect);
	}

	@Test
	public void angleTest() {
		Rectangle rect = new Rectangle(1.0, 2.0, 3.0, 4.0, 1.0);

		rect.setAngleFromDegree(180);

		assertTrue(Math.abs(Math.PI - rect.getAngle()) < 0.001);

		rect.setAngleFromDegree(90);

		assertTrue(Math.abs(Math.PI / 2.0 - rect.getAngle()) < 0.001);
	}
}
