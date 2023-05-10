import mdlaf.MaterialLookAndFeel;
import mdlaf.components.button.MaterialButtonUI;
import mdlaf.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public abstract class World {
  protected ArrayList<Organism> organisms;
  protected int width = 0;
  protected int height = 0;
  protected JFrame frame;
  protected JPanel gridPanel;
  protected JPanel userInterface;
  protected JSplitPane splitPane;
  protected JTextArea logArea;
  protected Font font;
  protected Font regularFont;
  protected KeyListener arrowListener;
  Deque<String> logs;
  protected static final int ATTEMPTS = 10;
  protected static final int CELL_SIZE = 75;
  protected static final int HEX_SIDES = 6;
  protected static final String FILENAME = "file_java_game.txt";

  public World() {
    organisms = new ArrayList<>();
    logs = new ArrayDeque<>();
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public abstract void setGUI() throws IOException, FontFormatException;

  public void make_turn() {
    organisms.removeIf(Organism::isDead);

    final int size = organisms.size();
    for (int i = 0; i < size; i++) {
      organisms.get(i).takeTurn();
    }
  }

  public void message(String msg) {
    var worldMessage = "[WORLD] " + msg;

    this.logs.addLast(worldMessage);
    if (this.logs.size() > 10) {
      this.logs.removeFirst();
    }
  }

  public abstract void placeOrganism(Class<? extends Organism> classType);

  public abstract boolean isFree(int i, int j);

  public abstract void display_world()
      throws UnsupportedLookAndFeelException, IOException, FontFormatException;

  public ArrayList<Organism> getOrganisms() {
    return organisms;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void addOrganism(Organism newOrganism) {
    organisms.add(newOrganism);
  }

  public JFrame getFrame() {
    return frame;
  }

  protected void saveGameToFile(FileWriter writer) throws IOException {
    writer.write(getWidth() + " " + getHeight() + "\n");
    for (var o : organisms) {
      if (o instanceof Human human) {
        writer.write(
            human.getClass().getSimpleName()
                + " "
                + human.getStrength()
                + " "
                + human.getInitiative()
                + " "
                + human.getAge()
                + " "
                + human.getPosition().first()
                + " "
                + human.getPosition().second()
                + " "
                + human.getSpecialAbilityCooldown()
                + " "
                + human.isAbilityActive()
                + " "
                + human.getSpecialAbilityDuration()
                + "\n");
      } else {
        writer.write(
            o.getClass().getSimpleName()
                + " "
                + o.getStrength()
                + " "
                + o.getInitiative()
                + " "
                + o.getAge()
                + " "
                + o.getPosition().first()
                + " "
                + o.getPosition().second()
                + "\n");
      }
    }
  }

  protected void readFromFile(File reader) throws IOException {
    organisms.clear();
    var scanner = new Scanner(reader);

    String dimensions = scanner.nextLine();
    var dimensionScanner = new Scanner(dimensions);

    int worldWidth = dimensionScanner.nextInt();
    int worldHeight = dimensionScanner.nextInt();

    setWidth(worldWidth);
    setHeight(worldHeight);

    dimensionScanner.close();

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      var lineScanner = new Scanner(line);

      String className = lineScanner.next();
      int strength = lineScanner.nextInt();
      int initiative = lineScanner.nextInt();
      int age = lineScanner.nextInt();
      int iPos = lineScanner.nextInt();
      int jPos = lineScanner.nextInt();

      var organism = Factory.create(className, new Pair<>(iPos, jPos), this);
      organism.setStrength(strength);
      organism.setInitiative(initiative);
      organism.setAge(age);

      if (organism instanceof Human) {
        int cooldown = lineScanner.nextInt();
        boolean isActive = lineScanner.nextBoolean();
        int duration = lineScanner.nextInt();

        ((Human) organism).setSpecialAbilityCooldown(cooldown);
        if (isActive) ((Human) organism).activateSpecialAbility();
        ((Human) organism).setSpecialAbilityDuration(duration);
      }

      organisms.add(organism);
    }

    scanner.close();
  }
}
