import javax.swing.*;
import java.awt.*;

public final class Grass extends Plant {
  private static final int GRASS_SPREAD_CHANCE = 5; // chance of 5%

  public Grass(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setStrength(0);
  }

  @Override
  public void takeTurn() {
    spread(GRASS_SPREAD_CHANCE);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83C\uDF3A"; // herb emoji
    panel.setBackground(new Color(34, 139, 34)); // forest green
    label.setForeground(new Color(255, 255, 255)); // white
    label.setText(content);
  }

  @Override
  void takeEffect(Animal other) {
    getWorld().message("Grass was eaten");
    other.setPosition(this.getPosition());
    this.die();
  }
}
