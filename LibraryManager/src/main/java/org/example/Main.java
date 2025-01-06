package org.example;

import org.example.Modules.CLI;

public class Main {
    public static void main(String[] args) {
        CLI albumManager = new CLI();

        try{
            albumManager.Run();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}