package com.example.yatra.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringChunker {
    public static String[] chunk(String s) {
        //https://javarevisited.blogspot.com/2016/10/how-to-split-string-in-java-by-whitespace-or-tabs.html#axzz6sPBVhIR3
        return s.split("\\s+");
    }

    public static String arrayDifference(String[] arr1, String[] arr2) {
        String finalSeats = "";

        //https://www.geeksforgeeks.org/conversion-of-array-to-arraylist-in-java/
        List<String> a1 = new ArrayList<String>(Arrays.asList(arr1));
        List<String> a2 = new ArrayList<String>(Arrays.asList(arr2));

        //https://www.geeksforgeeks.org/remove-one-array-from-another-array-in-java/
        a1.removeAll(a2);

        //https://stackoverflow.com/questions/16863255/trying-to-convert-an-arraylist-to-string
        for (String s : a1) {
            finalSeats += s + " ";
        }
        return finalSeats.trim();
    }

    public int getSeatCount(String s){
        String[] strings = chunk(s);
        return strings.length + 1;
    }

}
