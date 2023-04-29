import javax.swing.*;
import java.util.Optional;

public class Animal extends Organism {
  public Animal(Pair<Integer, Integer> position, World world) {
    this.setPosition(position);
    this.setWorld(world);
  }
  @Override
  public void make_turn() {
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

    while (!validCoords(newI, newJ)) {
      randomNumber = randomShift();

      deltaI = dy[randomNumber];
      deltaJ = dx[randomNumber];

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
  public void collision(Organism other) {}

  @Override
  public void display(String content, JPanel panel, JLabel label) {}
}
