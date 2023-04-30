public final class Factory {
  public static Organism create(String className, Pair<Integer, Integer> position, World world)
      throws IllegalArgumentException {
    if (className.equals(Sheep.class.getSimpleName())) {
      return new Sheep(position, world);
    } else if (className.equals(Wolf.class.getSimpleName())) {
      return new Wolf(position, world);
    } else if (className.equals(Fox.class.getSimpleName())) {
      return new Fox(position, world);
    } else if (className.equals(Gazelle.class.getSimpleName())) {
      return new Gazelle(position, world);
    }

    throw new IllegalArgumentException("Wrong arguments provided to function.");
  }

  public static Organism create(
      Class<? extends Organism> classType, Pair<Integer, Integer> position, World world) {
    return create(classType.getSimpleName(), position, world);
  }
}
