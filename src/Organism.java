import javax.swing.*;
import java.awt.*;
import java.util.Random;

public abstract class Organism {
  private int strength = 0;
  private int initiative = 0;
  private int age = 0;
  private boolean isDead = false;
  private Pair<Integer, Integer> position;
  private World world;
  public static int[] dx = {0, 1, 0, -1};
  public static int[] dy = {1, 0, -1, 0};

  public abstract void takeTurn();

  public abstract void collision(Organism other);

  public abstract void display(String content, JPanel panel, JLabel label);

  public abstract void display(Graphics g, int x, int y);

  public void setStrength(int strength) {
    this.strength = strength;
  }

  public void setInitiative(int initiative) {
    this.initiative = initiative;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  public int getStrength() {
    return strength;
  }

  public int getInitiative() {
    return initiative;
  }

  public int getAge() {
    return age;
  }

  public Pair<Integer, Integer> getPosition() {
    return position;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setPosition(Pair<Integer, Integer> position) {
    this.position = position;
  }

  public World getWorld() {
    return world;
  }

  public void incrementAge() {
    age++;
  }

  public void die() {
    isDead = true;
  }

  public boolean isDead() {
    return isDead;
  }

  public int randomProbability() {
    var rand = new Random();
    return rand.nextInt(101);
  }

  public int randomShift() {
    var rand = new Random();
    return rand.nextInt(4);
  }

  public void empower() {}

  public boolean invalidCoords(int newI, int newJ) {
    return newI < 0 || newJ < 0 || newI >= world.getHeight() || newJ >= world.getWidth();
  }
}
