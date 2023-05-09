import javax.swing.*;
import java.awt.*;

public class Nightshade extends Plant {
    private static final int NIGHTSHADE_SPREAD_CHANCE = 3;
  public Nightshade(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setStrength(99);
  }

    @Override
    public void takeTurn() {
        spread(NIGHTSHADE_SPREAD_CHANCE);
    }

    @Override
    public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83C\uDF53"; // Nightshade emoji
        panel.setBackground(new Color(138, 43, 226)); // orange
        label.setForeground(new Color(167, 225, 43)); // dark yellow
        label.setText(content);
    }

    @Override
    void takeEffect(Animal other) {
      getWorld().message("Nightshade just poisoned " + other.getClass().getSimpleName() + " to death");
      other.die();
      this.die();
    }
}
