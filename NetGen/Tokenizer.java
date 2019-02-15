//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
// 
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from: 
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package netgen;

import java.util.ArrayList;

/**
 *
 * @author Neal
 */
public class Tokenizer {
    
    //Splits text on regex, returns as ArrayList
    public static ArrayList<String> split(String text, String regex) {
        ArrayList<String> output = new ArrayList<>();
        String[] split = text.split(regex);
        for(String string : split) {
            output.add(string.trim());
        }
        return output;       
    }

    //Splits text on the default regex, whitespace; returns as ArrayList
    public static ArrayList<String> split(String text) {
        return Tokenizer.split(text, "\\s+");    
    }

    
    
}
