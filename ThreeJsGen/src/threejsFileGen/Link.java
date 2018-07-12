//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package threejsFileGen;

public class Link {
	// source and target layer dates
	public int sourceLayer;
	public int targetLayer;
	// source and target community Ids
	public int sourceCommunity;
	public int targetCommunity;
	// weight is currently the number of concepts these communities share
	public int weight;

	public Link(int sourceLayer, int targetLayer, int sourceCommunity, int targetCommunity) {
		this.sourceLayer = sourceLayer;
		this.targetLayer = targetLayer;
		this.sourceCommunity = sourceCommunity;
		this.targetCommunity = targetCommunity;
		weight = 1;
	}

	public void addToWeight() {
		weight += 1;
	}
}
