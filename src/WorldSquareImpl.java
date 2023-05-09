import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mdlaf.MaterialLookAndFeel;
import mdlaf.components.button.MaterialButtonUI;
import mdlaf.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class WorldSquareImpl extends World {
  @Override
  public void setGUI() throws IOException, FontFormatException {
    frame = new JFrame("University of sparkling water");

    font = new Font("Segoe UI Emoji", Font.PLAIN, 72);
    regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

    gridPanel = new JPanel(new GridLayout(height, width));
    frame.add(gridPanel);

    userInterface = new JPanel();
    userInterface.setPreferredSize(new Dimension(width * CELL_SIZE, height * CELL_SIZE));
    userInterface.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    userInterface.setBackground(MaterialColors.LIGHT_BLUE_50);
    userInterface.setLayout(new BoxLayout(userInterface, BoxLayout.Y_AXIS));
    userInterface.setAlignmentX(Component.CENTER_ALIGNMENT);

    var mainLabel = new JLabel("Henryk Wołek 193399");
    mainLabel.setFont(regularFont.deriveFont(Font.PLAIN, 36));
    mainLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userInterface.add(mainLabel);

    userInterface.add(Box.createRigidArea(new Dimension(0, 10)));

    var saveButton = new JButton("Save game");
    saveButton.setUI(new MaterialButtonUI());
    saveButton.setFocusable(false);
    saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    userInterface.add(saveButton);

    saveButton.addActionListener(
        e -> {
          try (var writer = new FileWriter(FILENAME, false)) {
            saveGameToFile(writer);
            writer.close();
            message("Game state has been successfully saved to file " + FILENAME);
            gridPanel.removeAll();
            display_world();
          } catch (UnsupportedLookAndFeelException | IOException | FontFormatException ex1) {
            return;
          }
        });

    userInterface.add(Box.createRigidArea(new Dimension(0, 10)));

    var loadButton = new JButton("Load game");
    loadButton.setUI(new MaterialButtonUI());
    loadButton.setFocusable(false);
    loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    userInterface.add(loadButton);

    loadButton.addActionListener(
        e -> {
          try {
            var reader = new File(FILENAME);
            readFromFile(reader);
            gridPanel.removeAll();
            display_world();
          } catch (UnsupportedLookAndFeelException | IOException | FontFormatException ex1) {
            return;
          }
        });

    userInterface.add(Box.createRigidArea(new Dimension(0, 10)));

    logArea = new JTextArea();
    logArea.setEditable(false);
    logArea.setFocusable(false);
    logArea.setLineWrap(true);
    logArea.setPreferredSize(new Dimension(width * 75, height * 30));
    logArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    userInterface.add(logArea);

    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gridPanel, userInterface);
    splitPane.setResizeWeight(0);
    splitPane.setDividerSize(0);
    frame.add(splitPane);

    frame.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {}

          @Override
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              try {
                gridPanel.removeAll();
                make_turn();
                display_world();
              } catch (UnsupportedLookAndFeelException | IOException | FontFormatException ex1) {
                return;
              }
            }
          }

          @Override
          public void keyReleased(KeyEvent e) {}
        });
  }

  @Override
  public void placeOrganism(Class<? extends Organism> classType) {
    var rand = new Random();

    var newI = rand.nextInt(getHeight());
    var newJ = rand.nextInt(getWidth());
    var attemptCounter = 0;

    while (!isFree(newI, newJ) && attemptCounter++ < ATTEMPTS) {
      newI = rand.nextInt(getHeight());
      newJ = rand.nextInt(getWidth());
    }

    if (attemptCounter == ATTEMPTS) return;

    addOrganism(Factory.create(classType, new Pair<>(newI, newJ), this));
  }

  @Override
  public boolean isFree(int i, int j) {
    return organisms.stream().anyMatch(o -> o.getPosition().equals(new Pair<>(i, j)));
  }

  @Override
  public void display_world()
      throws UnsupportedLookAndFeelException, IOException, FontFormatException {
    UIManager.setLookAndFeel(new MaterialLookAndFeel());

    logArea.setText("================\n* WORLD LOGS *\n================\n");
    for (var message : logs) {
      logArea.append(message + "\n");
    }

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        var cellPanel = new JPanel();
        cellPanel.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cellPanel.setBackground(MaterialColors.LIGHT_BLUE_50);

        var newPos = new Pair<>(i, j);

        var cellContent = "";
        var foundOrganism =
            organisms.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();

        var organism = foundOrganism.orElse(null);
        var cellLabel = new JLabel();

        if (organism != null) {
          organism.display(cellContent, cellPanel, cellLabel);
        } else {
          cellLabel.setText(cellContent);
          cellPanel.addMouseListener(
              new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                  var options = Factory.getClasses();
                  var comboBox = new JComboBox<>(options);
                  comboBox.setPreferredSize(new Dimension(100, 30));

                  int option =
                      JOptionPane.showOptionDialog(
                          null,
                          comboBox,
                          "Select organism to place at this field",
                          JOptionPane.DEFAULT_OPTION,
                          JOptionPane.PLAIN_MESSAGE,
                          null,
                          null,
                          null);

                  if (option == JOptionPane.OK_OPTION) {
                    var selectedOrganism = (String) comboBox.getSelectedItem();
                    if (selectedOrganism != null) {
                      var newOrganism =
                          Factory.create(selectedOrganism, newPos, WorldSquareImpl.this);
                      newOrganism.display(cellContent, cellPanel, cellLabel);
                      addOrganism(newOrganism);
                      message("A new " + selectedOrganism + " was just added by a player!");
                    }
                  }
                }
              });
        }

        cellLabel.setPreferredSize(new Dimension(75, 75));
        cellLabel.setVerticalAlignment(SwingConstants.CENTER);
        cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cellLabel.setFont(font.deriveFont(Font.PLAIN, 36));
        cellPanel.add(cellLabel, BorderLayout.CENTER);

        gridPanel.add(cellPanel);
      }
    }

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

  @Override
  public void saveGameToFile(FileWriter writer) throws IOException {
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

  @Override
  public void readFromFile(File reader) throws IOException {
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
