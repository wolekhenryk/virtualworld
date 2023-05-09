import javax.swing.*;
import java.awt.*;

public final class Wolf extends Animal {
    private static final int WOLF_STRENGTH = 9;
    private static final int WOLF_INITIATIVE = 5;
    public Wolf(Pair<Integer, Integer> position, World world) {
        super(position, world);
        this.setInitiative(WOLF_INITIATIVE);
        this.setStrength(WOLF_STRENGTH);
    }

    @Override
    public void display(String content, JPanel panel, JLabel label) {
        content = "\uD83D\uDC3A";
        panel.setBackground(new Color(30, 30, 30)); // very dark gray
        label.setForeground(Color.WHITE);
        label.setText(content);
    }

    @Override
    public void display(Graphics g, int x, int y) {
        g.drawString("\ud83d\udc3a", x, y);
    }
}
