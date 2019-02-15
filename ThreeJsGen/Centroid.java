//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

public class Centroid {
	private double x;
	private double y;

	Centroid(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	// x getter/setter
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	// y getter/setter
	public Double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	// add x / add y
	public void addX(double x) {
		this.x += x;
	}

	public void addY(double y) {
		this.y += y;
	}
}
