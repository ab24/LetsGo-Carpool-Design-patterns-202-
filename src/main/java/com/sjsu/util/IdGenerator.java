package com.sjsu.util;

import java.util.Random;


public class IdGenerator {

    public static String generateId(String prefix) {
        return prefix + (new Random().nextInt((2000 - 1000)) + 1000);
    }

    public static void main(String arg[]) {
        for (int i = 0; i < 90; i++) {
            System.out.println(IdGenerator.generateId("M"));
        }
    }

}
