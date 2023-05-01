import javax.swing.*;

public class Animal extends Organism {
  public Animal(Pair<Integer, Integer> position, World world) {
    this.setPosition(position);
    this.setWorld(world);
  }

  Pair<Integer, Integer> findFreeSpot() {
    var currentI = getPosition().first();
    var currentJ = getPosition().second();

    var livings = getWorld().getOrganisms();

    for (int i = 0; i < 4; i++) {
      var newI = currentI + dy[i];
      var newJ = currentJ + dx[i];

      if (invalidCoords(newI, newJ)) continue;

      var newPos = new Pair<>(newI, newJ);
      var foundOrganism = livings.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();

      if (foundOrganism.isEmpty()) return newPos;
    }
    return new Pair<>(-1, -1);
  }

  @Override
  public void takeTurn() {
    var world = getWorld();
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

  private boolean isSameSpecies(Animal first, Animal second) {
    return first.getClass().getSimpleName().equals(second.getClass().getSimpleName());
  }

  private void reproduce() {
    getWorld().message("Reproducing...");
    var newPos = findFreeSpot();
    if (newPos.equals(new Pair<>(-1, -1))) return;
    getWorld().addOrganism(Factory.create(this.getClass().getSimpleName(), newPos, getWorld()));
  }

  @Override
  public void collision(Organism other) {
    if (other instanceof Animal) {
      if (isSameSpecies(this, (Animal) other)) {
        reproduce();
        return;
      }
      if (other instanceof Tortoise && this.getStrength() < 5) {
        getWorld().message("Tortoise deflected attack!");
        return;
      }
      if (other.getStrength() > this.getStrength()) {
        this.die();
        var prevPos = this.getPosition();
        other.setPosition(prevPos);
        getWorld()
            .message(other.getClass().getSimpleName() + " ate " + this.getClass().getSimpleName());
      } else if (other.getStrength() < this.getStrength()) {
        other.die();
        var prevPos = other.getPosition();
        this.setPosition(prevPos);
        getWorld()
            .message(this.getClass().getSimpleName() + " ate " + other.getClass().getSimpleName());
      }
    } else if (other instanceof Plant) {
      ((Plant) other).takeEffect(this);
    }
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {}
}
