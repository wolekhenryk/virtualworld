import javax.swing.*;
import java.awt.*;

public class Fox extends Animal {
  private static final int FOX_STRENGTH = 3;
  private static final int FOX_INITIATIVE = 7;

  public Fox(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(FOX_INITIATIVE);
    this.setStrength(FOX_STRENGTH);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\uD83E\uDD8A";
    panel.setBackground(new Color(255, 170, 0));
    label.setForeground(new Color(119, 70, 34));
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y, Polygon p) {
    g.setColor(new Color(255, 170, 0));
    g.fillPolygon(p);
    g.setColor(new Color(119, 70, 34));
    g.drawPolygon(p);
    g.drawString("\uD83E\uDD8A", x, y);
  }

  private boolean safe_movement(int newI, int newJ) {
    var livings = getWorld().getOrganisms();
    var posAsPair = new Pair<>(newI, newJ);

    var foundOrganism = livings.stream().filter(o -> o.getPosition().equals(posAsPair)).findFirst();

    if (foundOrganism.isPresent()) {
      var organism = foundOrganism.get();
      return organism.getStrength() < this.getStrength();
    }

    return true;
  }

  @Override
  public void takeTurn() {
    var world = getWorld();
    var worldWidth = world.getWidth();
    var worldHeight = world.getHeight();

    var currentPosition = getPosition();

    var currentI = currentPosition.first();
    var currentJ = currentPosition.second();

    var randomNumber = randomShift();

    var deltaI = dy[randomNumber];
    var deltaJ = dx[randomNumber];

    var newI = currentI + deltaI;
    var newJ = currentJ + deltaJ;

    while (invalidCoords(newI, newJ)) {
      randomNumber = randomShift();

      deltaI = dy[randomNumber];
      deltaJ = dx[randomNumber];

      newI = currentI + deltaI;
      newJ = currentJ + deltaJ;
    }

    if (!safe_movement(newI, newJ)) return;

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
}
