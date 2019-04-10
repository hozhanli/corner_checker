package com.cin_damasi.app;

import com.cin_damasi.app.chinese_checker_package.Game;

import javax.swing.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Game game = new Game();
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
