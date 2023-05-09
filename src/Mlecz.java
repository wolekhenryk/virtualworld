import javax.swing.*;
import java.awt.*;

public final class Mlecz extends Plant {
  private static final int MLECZ_SPREAD_CHANCE = 2;
  private static final int ATTEMPTS = 3;

  public Mlecz(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setStrength(0);
  }

  @Override
  public void takeTurn() {
    for (int i = 0; i < ATTEMPTS; i++) spread(MLECZ_SPREAD_CHANCE);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83C\uDF3B";
    panel.setBackground(new Color(255, 255, 0)); // white
    label.setForeground(new Color(0, 0, 0)); // orange
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y) {
    g.drawString("\uD83C\uDF3B", x, y);
  }

  @Override
  void takeEffect(Animal other) {
    getWorld().message("Mlecz was eaten");
    other.setPosition(this.getPosition());
    this.die();
  }
}
