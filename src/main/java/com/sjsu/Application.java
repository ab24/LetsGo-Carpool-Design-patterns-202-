package com.sjsu;


import com.sjsu.client.MainMenu;

import java.io.FileNotFoundException;

import static com.sjsu.client.MainMenu.getMainMenu;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {
        MainMenu mainMenu = getMainMenu();
        mainMenu.start();
        System.out.println("Test");

    }
}
