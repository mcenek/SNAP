//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

// An Edge represents a link between two nodes in one layer -
// two connected concepts within one piece of text
public class Edge {
	private Node source;
	private Node target;
	private double weight;
	// TODO what exactly are start and end used for?
	private int start;
	private int end;

	// constructors
	public Edge() {
	}
	public Edge(Node target, Node source, int start, int end) {
		this.target = target;
		this.source = source;
		this.start = start;
		this.end = end;
	}

	// source getter/setter
	public Node getSource() {
		return source;
	}
	public void setSource(Node source) {
		this.source = source;
	}

	// target getter/setter
	public Node getTarget() {
		return target;
	}
	public void setTarget(Node target) {
		this.target = target;
	}

	// weight getter/setter
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}

	// start getter/setter
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}

	// end getter/setter
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
}
