//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
package netgen;

//Immutable type
//Could break some methods if fields are modifiable
public class SemanticPair {

    private final String a;
    private final String b;

    /**
     * @return the a
     */
    public String getA() {
        return a;
    }

    /**
     * @return the b
     */
    public String getB() {
        return b;
    }

    //Standardizing the order of tokens
    //For use in undirected semantic networks
    public SemanticPair(String a, String b) {
        if (a.compareTo(b) > 0) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    @Override
    public boolean equals(Object other) {

        if (!other.getClass().equals(this.getClass())) {
            return false;
        } else if (!this.a.equals(((SemanticPair)other).a)) {
            return false;
        } else if (!this.b.equals(((SemanticPair) other).b)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    
    public int hashCode() {
        return a.hashCode() / 2 + b.hashCode() % 3593; //3593 is allegedly prime
    }

}
