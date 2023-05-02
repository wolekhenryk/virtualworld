import javax.swing.*;
import java.awt.*;

public final class Guarana extends Plant {
  private static final int guaranaSpreadChance = 6;
  private static final int guaranaPower = 3;

  public Guarana(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setStrength(0);
  }

  @Override
  public void takeTurn() {
    spread(guaranaSpreadChance);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83C\uDF31"; // Guarana emoji
    panel.setBackground(new Color(139, 69, 19)); // orange
    label.setForeground(new Color(255, 140, 0)); // dark yellow
    label.setText(content);
  }

  @Override
  void takeEffect(Animal other) {
    other.setStrength(other.getStrength() + guaranaPower);
    getWorld()
        .message(
            other.getClass().getSimpleName()
                + " was empowered, now has strength of "
                + other.getStrength());
    other.setPosition(this.getPosition());
    this.die();
  }
}
