public final class Factory {
  private static final String[] CLASSES = {
    "Sheep",
    "Wolf",
    "Fox",
    "Gazelle",
    "Tortoise",
    "Grass",
    "Mlecz",
    "Guarana",
    "Nightshade",
    "Barszcz"
  };

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
    } else if (className.equals(Tortoise.class.getSimpleName())) {
      return new Tortoise(position, world);
    } else if (className.equals(Grass.class.getSimpleName())) {
      return new Grass(position, world);
    } else if (className.equals(Mlecz.class.getSimpleName())) {
      return new Mlecz(position, world);
    } else if (className.equals(Guarana.class.getSimpleName())) {
      return new Guarana(position, world);
    } else if (className.equals(Nightshade.class.getSimpleName())) {
      return new Nightshade(position, world);
    } else if (className.equals(Barszcz.class.getSimpleName())) {
      return new Barszcz(position, world);
    } else if (className.equals(Human.class.getSimpleName())) {
      return new Human(position, world);
    }

    throw new IllegalArgumentException("Wrong arguments provided to function.");
  }

  public static Organism create(
      Class<? extends Organism> classType, Pair<Integer, Integer> position, World world) {
    return create(classType.getSimpleName(), position, world);
  }

  public static String[] getClasses() {
    return CLASSES;
  }
}
