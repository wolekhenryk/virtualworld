import javax.swing.*;
import java.awt.*;

public final class Wolf extends Animal {
    private static final int wolfStrength = 9;
    private static final int wolfInitiative = 5;
    public Wolf(Pair<Integer, Integer> position, World world) {
        super(position, world);
        this.setInitiative(wolfInitiative);
        this.setStrength(wolfStrength);
    }

    @Override
    public void display(String content, JPanel panel, JLabel label) {
        content = "\uD83D\uDC3A";
        panel.setBackground(new Color(30, 30, 30)); // very dark gray
        label.setForeground(Color.WHITE);
        label.setText(content);
    }
}
