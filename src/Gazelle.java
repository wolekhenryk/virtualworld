import javax.swing.*;
import java.awt.*;
import java.util.Random;

public final class Gazelle extends Animal {
  private static final int GAZELLE_STRENGTH = 4;
  private static final int GAZELLE_INITIATIVE = 4;

  public Gazelle(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(GAZELLE_INITIATIVE);
    this.setStrength(GAZELLE_STRENGTH);
  }

  @Override
  public void takeTurn() {
    var world = getWorld();
    var currentPosition = getPosition();

    var currentI = currentPosition.first();
    var currentJ = currentPosition.second();

    var randomNumber = randomShift();

    var deltaI = 2 * dy[randomNumber];
    var deltaJ = 2 * dx[randomNumber];

    var newI = currentI + deltaI;
    var newJ = currentJ + deltaJ;

    while (invalidCoords(newI, newJ)) {
      randomNumber = randomShift();

      deltaI = 2 * dy[randomNumber];
      deltaJ = 2 * dx[randomNumber];

      newI = currentI + deltaI;
      newJ = currentJ + deltaJ;
    }

    var newPosition = new Pair<>(newI, newJ);
    var livings = world.getOrganisms();

    var foundOrganism =
        livings.stream().filter(o -> o.getPosition().equals(newPosition)).findFirst();

    foundOrganism.ifPresentOrElse(
        this::collision,
        () -> {
          setPosition(newPosition);
        });
  }

  @Override
  public void collision(Organism other) {
    if (other instanceof Plant) {
      ((Plant) other).takeEffect(this);
    } else {
      var rand = new Random();
      var escapeChance = rand.nextInt(2);

      if (escapeChance == 1) {
        var newSafePos = findFreeSpot();
        if (!newSafePos.equals(new Pair<>(-1, -1))) {
          getWorld().message("Gazela escaped the fight!");
          setPosition(newSafePos);
          return;
        }
      }
      super.collision(other);
    }
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83E\uDD8C"; // antelope emoji
    panel.setBackground(new Color(160, 82, 45)); // brownish color for antelope
    label.setForeground(Color.WHITE);
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y, Polygon p) {
    g.setColor(new Color(160, 82, 45));
    g.fillPolygon(p);
    g.setColor(Color.WHITE);
    g.drawPolygon(p);
    g.drawString("\uD83E\uDD8C", x, y);
  }
}
