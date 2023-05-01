import javax.swing.*;

public class Plant extends Organism {
  public Plant(Pair<Integer, Integer> position, World world) {
    this.setPosition(position);
    this.setWorld(world);
  }

  public void spread(int chance) {
    var prob = randomProbability();
    if (prob < chance) {
      var currentI = getPosition().first();
      var currentJ = getPosition().second();

      var posShift = randomShift();

      var newI = currentI + dy[posShift];
      var newJ = currentJ + dx[posShift];

      if (invalidCoords(newI, newJ)) return;

      var livings = getWorld().getOrganisms();
      var newPos = new Pair<>(newI, newJ);

      var foundOrganism = livings.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();
      foundOrganism.ifPresentOrElse(
          o -> {},
          () ->
              getWorld()
                  .addOrganism(
                      Factory.create(this.getClass().getSimpleName(), newPos, getWorld())));
    }
  }

  void takeEffect(Animal other) {}

  @Override
  public void takeTurn() {}

  @Override
  public void collision(Organism other) {}

  @Override
  public void display(String content, JPanel panel, JLabel label) {}
}
