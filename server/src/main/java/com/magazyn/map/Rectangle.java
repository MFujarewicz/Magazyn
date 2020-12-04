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
		setLeft(left);
		setTop(top);
		setWidth(width);
		setHeight(height);
		setAngle(angle);
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
		//Angle should be in [0, 2* PI]
		while (angle > 2 * Math.PI) {
			angle -= 2 * Math.PI;
		}

		while (angle < 0) {
			angle += 2 * Math.PI;
		}

		this.angle = angle;
	}

	void setAngleFromDegree(double angle) {
		setAngle(angle / 180.0 * Math.PI);
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

	double[] toArray() {
		return new double[] {
			getLeft(),
			getTop(),
			getWidth(),
			getHeight(),
			getAngle()
		};
	}
}
