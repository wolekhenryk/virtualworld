import javax.swing.*;
import java.awt.*;

public final class Sheep extends Animal {
  private static final int SHEEP_STRENGTH = 4;
  private static final int SHEEP_INITIATIVE = 4;

  public Sheep(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(SHEEP_INITIATIVE);
    this.setStrength(SHEEP_STRENGTH);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83D\uDC11"; // sheep emoji
    panel.setBackground(Color.LIGHT_GRAY);
    label.setForeground(Color.DARK_GRAY);
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y) {
    g.drawString("\uD83D\uDC11", x, y);
  }
}
