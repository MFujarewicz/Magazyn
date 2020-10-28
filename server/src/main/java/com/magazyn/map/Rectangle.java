package com.magazyn.map;

public class Rectangle {
	private double left;
	private double top;
	private double width;
	private double height;
	private double angle;

	public Rectangle() {
		left = 0;
		top = 0;
		width = 0;
		height = 0;
		angle = 0;
	}

	public Rectangle(double left, double top, double width, double height, double angle) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.angle = angle;
	}

	double getLeft() {
		return left;
	}

	double getTop() {
		return top;
	}

	double getWidth() {
		return width;
	}

	double getHeight() {
		return height;
	}

	double getAngle() {
		return angle;
	}

	void setLeft(double left) {
		this.left = left;
	}

	void setTop(double top) {
		this.top = top;
	}

	void setWidth(double width) {
		this.width = width;
	}

	void setHeight(double height) {
		this.height = height;
	}

	void setAngle(double angle) {
		this.angle = angle;
	}

	double calculateArea() {
		if (height < 0 || width < 0) {
			return -1.0;
		}

		return height * width;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (object.getClass() != this.getClass()) {
            return false;
		}
		
		final Rectangle other_rectangle = (Rectangle)object;

		return other_rectangle.top == top &&
				other_rectangle.left == left &&
				other_rectangle.width == width &&
				other_rectangle.height == height &&
				other_rectangle.angle == angle;
	}
}
