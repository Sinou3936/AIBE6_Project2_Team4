package org.project;

import java.io.FileNotFoundException;

public class Main {
    static void main() {
        App app = new App();
        try {
            app.run();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
