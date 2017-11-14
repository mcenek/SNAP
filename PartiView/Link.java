//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

import java.util.ArrayList;

public class Link {
	private int modClass;
	private ArrayList<Integer> linkedClass;
	
	// Constructor
	public Link(int modClassIn, int linkedClassIn){
		modClass = modClassIn;
		linkedClass = new ArrayList<Integer>();
		linkedClass.add(linkedClassIn);
	}
	
	// modClass getter/setter
	public int getModClass(){
		return modClass;
	}
	public void setModClass(int modClassIn){
		modClass = modClassIn;
	}
	
	// linkedClass getter/setter/add
	public ArrayList<Integer> getLinkedClass(){
		return linkedClass;
	}
	public void setLinkedClass(ArrayList<Integer> linkedClassIn){
		linkedClass = linkedClassIn;
	}
	public void addLink(int link){
		linkedClass.add(link);
	}
}
