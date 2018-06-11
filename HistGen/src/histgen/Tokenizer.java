/*
//Neal Logan
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package histgen;

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
