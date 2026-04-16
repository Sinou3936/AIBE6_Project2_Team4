package org.project;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        try {
            app.run();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
