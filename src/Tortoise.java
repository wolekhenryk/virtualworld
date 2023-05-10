import javax.swing.*;
import java.awt.*;

public final class Tortoise extends Animal {
  private static final int TORTOISE_STRENGTH = 2;
  private static final int TORTOISE_INITIATIVE = 1;
  private static final int MOVEMENT_CHANCE = 3;

  public Tortoise(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(TORTOISE_INITIATIVE);
    this.setStrength(TORTOISE_STRENGTH);
  }

  @Override
  public void takeTurn() {
    if (randomShift() >= MOVEMENT_CHANCE) super.takeTurn();
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\ud83d\udc22"; // Tortoise emoji
    panel.setBackground(new Color(155, 204, 102)); // Light green
    label.setForeground(new Color(51, 51, 51)); // Dark gray
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y, Polygon p) {
    g.setColor(new Color(155, 204, 102));
    g.fillPolygon(p);
    g.setColor(new Color(51, 51, 51));
    g.drawPolygon(p);
    g.drawString("\ud83d\udc22", x, y);
  }
}
