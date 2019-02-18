//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

public class Node {
	private String id;
	private String label;
	private double size;
	private double x;
	private double y;
	private int community;
	private NodeColor color;

	// id getter/setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// label getter/setter
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	// size getter/setter
	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	// x getter/setter
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	// y getter/setter
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	// community getter/setter
	public int getCommunity() {
		return community;
	}

	public void setCommunity(int communityIn ) {
		this.community = communityIn;
	}

	// color getter/setter
	public NodeColor getColor() {
		return color;
	}

	public void setColor(NodeColor color) {
		this.color = color;
	}
}
