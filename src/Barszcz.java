import javax.swing.*;
import java.awt.*;

public class Barszcz extends Plant {
  private static final int BARSZCZ_SPREAD_CHANCE = 2;

  public Barszcz(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setStrength(10);
  }

  @Override
  public void takeTurn() {
    var currentI = getPosition().first();
    var currentJ = getPosition().second();

    var livings = getWorld().getOrganisms();

    for (int i = 0; i < 4; i++) {
      var newI = currentI + dy[i];
      var newJ = currentJ + dx[i];

      if (invalidCoords(newI, newJ)) continue;

      var foundAnimal =
          livings.stream()
              .filter(a -> a.getPosition().equals(new Pair<>(newI, newJ)))
              .filter(a -> a instanceof Animal)
              .findFirst();
      if (foundAnimal.isPresent()) {
        var animal = (Animal) foundAnimal.get();
        animal.die();
        getWorld().message("Barszcz killed some animals");
      }
    }
    spread(BARSZCZ_SPREAD_CHANCE);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83C\uDF35"; // cactus emoji
    panel.setBackground(Color.WHITE);
    label.setForeground(Color.BLACK);
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y, Polygon p) {
    g.setColor(Color.WHITE);
    g.fillPolygon(p);
    g.setColor(Color.BLACK);
    g.drawPolygon(p);
    g.drawString("\uD83C\uDF35", x, y);
  }

  @Override
  void takeEffect(Animal other) {
    getWorld().message("Barszcz just poisoned " + other.getClass().getSimpleName() + " to death");
    other.die();
    this.die();
  }
}
