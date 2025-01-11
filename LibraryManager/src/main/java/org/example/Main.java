package org.example;

import org.example.Modules.View;

public class Main {
    public static void main(String[] args) {
        View albumManager = new View();

        try{
            albumManager.Run();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}