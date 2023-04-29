import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException, FontFormatException {
        var world = new World(10, 10);
        world.display_world();
    }
}