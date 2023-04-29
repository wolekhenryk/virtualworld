import javax.swing.*;
import java.awt.*;

public final class Sheep extends Animal {
  private static final int sheepStrength = 4;
  private static final int sheepInitiative = 4;

  public Sheep(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(sheepInitiative);
    this.setStrength(sheepStrength);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83D\uDC11"; // sheep emoji
    panel.setBackground(Color.LIGHT_GRAY);
    label.setForeground(Color.DARK_GRAY);
    label.setText(content);
  }
}
