import javax.swing.*;
import java.awt.*;

public final class Tortoise extends Animal {
  private static final int tortoiseStrength = 2;
  private static final int tortoiseInitiative = 1;
  private static final int movementChance = 3;

  public Tortoise(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(tortoiseInitiative);
    this.setStrength(tortoiseStrength);
  }

  @Override
  public void takeTurn() {
    if (randomShift() >= movementChance) super.takeTurn();
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\ud83d\udc22"; // Tortoise emoji
    panel.setBackground(new Color(155, 204, 102)); // Light green
    label.setForeground(new Color(51, 51, 51)); // Dark gray
    label.setText(content);
  }
}
