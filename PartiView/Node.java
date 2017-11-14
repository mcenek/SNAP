//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

public class Node {
	private String id;
	private String label;
	private double size;
	private double x;
	private double y;
	private int z;
	private int modClass;
	private NodeColor color;

	// id getter/setter
	public String getId(){
		return id;
	}
	public void setId(String idIn){
		id = idIn;
	}

	// label getter/setter
	public String getLabel(){
		return label;
	}
	public void setLabel(String labelIn){
		label = labelIn;
	}

	// size getter/setter
	public double getSize(){
		return size;
	}
	public void setSize(double d){
		size = d;
	}
	
	// x getter/setter
	public double getX(){
		return x;
	}
	public void setX(double xIn){
		x = xIn;
	}
	
	// y getter/setter
	public double getY(){
		return y;
	}
	public void setY(double yIn){
		y = yIn;
	}
	
	// z getter/setter
	public int getZ(){
		return z;
	}
	public void setZ(int zIn){
		z = zIn;
	}

	// modClass getter/setter
	public int getModClass(){
		return modClass;
	}
	public void setModClass(int modClassIn){
		modClass = modClassIn;
	}

	// color getter/setter
	public NodeColor getColor() {
		return color;
	}
	public void setColor(NodeColor colorIn) {
		color = colorIn;
	}
}
