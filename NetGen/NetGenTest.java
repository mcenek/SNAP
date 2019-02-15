//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package netgen;

import static org.junit.Assert.*;

import org.junit.Test;

public class NetGenTest {

	Filter f = new Filter();
	
	@Test
	public void test() {
		Filter.getStopWords("stopword.txt");
	}

}
