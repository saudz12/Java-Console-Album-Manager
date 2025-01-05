package org.example;

import org.example.Modules.CLI;


class Person {
    public String name;
    public int age;
    public String email;

    // Default constructor is needed for Jackson
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}

public class Main {
    public static void main(String[] args) {
        CLI albumManager = new CLI();

        //albumManager.Run();

        albumManager.Run();

    }
}