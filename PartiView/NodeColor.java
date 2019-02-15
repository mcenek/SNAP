//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

public class NodeColor {
	private int r;
	private int g;
	private int b;

	public NodeColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public NodeColor(String hexValue) {
		int index;

		// Remove quotes.
		index = hexValue.indexOf("\"");
		if (index >= 0) {
			hexValue = hexValue.substring(index + 1, hexValue.lastIndexOf("\""));
		}

		index = hexValue.indexOf("#");
		if (index < 0) {
			r = 0;
			g = 0;
			b = 0;
		} else {
			hexValue = hexValue.substring(index + 1, hexValue.length());
			if (hexValue.length() < 6) {
				hexValue = "" + hexValue.charAt(0) + hexValue.charAt(0) + hexValue.charAt(1) + hexValue.charAt(1)
						+ hexValue.charAt(2) + hexValue.charAt(2);
			}

			r = decodeHexStr(hexValue.substring(0, 2));
			g = decodeHexStr(hexValue.substring(2, 4));
			b = decodeHexStr(hexValue.substring(4, 6));
		}
	}

	// r g b getters
	public int getR() {
		return r;
	}
	public int getG() {
		return g;
	}
	public int getB() {
		return b;
	}

	// Returns the value of two hex characters in a String.
	private int decodeHexStr(String hex) {
		int value = 0;
		value += decodeHexChar(hex.charAt(0));
		value <<= 4;
		value += decodeHexChar(hex.charAt(1));
		return value;
	}

	// Returns the value of a hex char.
	private int decodeHexChar(char c) {
		c |= 32; // Make c lower-case.

		switch (c) {
		case '0': return 0;
		case '1': return 1;
		case '2': return 2;
		case '3': return 3;
		case '4': return 4;
		case '5': return 5;
		case '6': return 6;
		case '7': return 7;
		case '8': return 8;
		case '9': return 9;
		case 'a': return 10;
		case 'b': return 11;
		case 'c': return 12;
		case 'd': return 13;
		case 'e': return 14;
		case 'f': return 15;
		default: return 0;
		}
	}
}
