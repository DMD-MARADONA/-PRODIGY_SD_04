package org.example;


import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            sodukuView soduku = new sodukuView();
            soduku.setContentPane(soduku.mainPanel);
            soduku.setTitle("Chitengu-Danai_Task-04");
            soduku.setSize(900, 450);
            soduku.setVisible(true);
            soduku.setLocationRelativeTo(null);
            soduku.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }
}