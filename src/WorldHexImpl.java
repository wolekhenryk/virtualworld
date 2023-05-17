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

public class WorldHexImpl extends World {
  private int numRows;
  private int numCols;
  private final int SIZE = CELL_SIZE / 2;
  private final int HEX_SPACING = 10;
  private final int[] DELTA_X_ODD_HEX = {0, 1, 1, 1, 0, -1};
  private final int[] DELTA_Y_ODD_HEX = {-1, -1, 0, 1, 1, 0};
  private final int[] DELTA_X_EVEN_HEX = {-1, 0, 1, 0, -1, -1};
  private final int[] DELTA_Y_EVEN_HEX = {-1, -1, 0, 1, 1, 0};

  @Override
  public void setGUI() throws IOException, FontFormatException {
    frame = new JFrame("University of sparkling water");

    font = new Font("Segoe UI Emoji", Font.PLAIN, 72);
    regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

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

    if (organisms.stream().noneMatch(o -> o.getPosition().equals(new Pair<>(newI, newJ))))
      addOrganism(Factory.create(classType, new Pair<>(newI, newJ), this));
  }

  @Override
  public Pair<Integer, Integer> nextMove(Pair<Integer, Integer> currentPosition) {
    var random = new Random();
    var shift = random.nextInt(6);

    var currentI = currentPosition.first();
    var currentJ = currentPosition.second();

    if (currentI % 2 == 0) {
      return new Pair<>(currentI + DELTA_Y_EVEN_HEX[shift], currentJ + DELTA_X_EVEN_HEX[shift]);
    } else {
      return new Pair<>(currentI + DELTA_Y_ODD_HEX[shift], currentJ + DELTA_X_ODD_HEX[shift]);
    }
  }

  @Override
  public boolean isFree(int i, int j) {
    return organisms.stream().anyMatch(o -> o.getPosition().equals(new Pair<>(i, j)));
  }

  @Override
  public void display_world()
      throws UnsupportedLookAndFeelException, IOException, FontFormatException {
    UIManager.setLookAndFeel(new MaterialLookAndFeel());

    frame = new JFrame("University of sparkling water");

    font = new Font("Segoe UI Emoji", Font.PLAIN, 72);
    regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

    gridPanel =
        new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < height; i++) {
              for (int j = 0; j < width; j++) {
                int x =
                    SIZE
                        + j * (SIZE * 2 - HEX_SPACING)
                        + (i % 2 == 0 ? HEX_SPACING : SIZE + HEX_SPACING / 2);
                int y = SIZE + i * (SIZE * 2 - HEX_SPACING * 2) + SIZE / 2;

                Polygon hex = new Polygon();
                for (int k = 0; k < 6; k++) {
                  int angleDeg = 30 + 60 * k;
                  double angleRad = Math.PI / 180 * angleDeg;
                  int x_i = (int) (x + SIZE * Math.cos(angleRad));
                  int y_i = (int) (y + SIZE * Math.sin(angleRad));
                  hex.addPoint(x_i, y_i);
                }

                g.setFont(font.deriveFont(Font.PLAIN, 36));

                var newPos = new Pair<>(i, j);
                var foundOrganism =
                    organisms.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();

                var cellPanel = new JPanel();
                cellPanel.setPreferredSize(new Dimension(SIZE, SIZE));
                cellPanel.setBounds(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
                cellPanel.setOpaque(false);
                cellPanel.setBackground(new Color(0, 0, 0, 0));

                var organism = foundOrganism.orElse(null);
                if (organism != null) {
                  g.drawPolygon(hex);
                  organism.display(g, x - SIZE / 2 - SIZE / 6, y + SIZE / 2 - SIZE / 4, hex);
                } else {
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
                                  Factory.create(selectedOrganism, newPos, WorldHexImpl.this);
                              newOrganism.display(
                                  g, x - SIZE / 2 - SIZE / 6, y + SIZE / 2 - SIZE / 4, hex);
                              addOrganism(newOrganism);
                              message("A new " + selectedOrganism + " was just added by a player!");
                            }
                          }
                        }
                      });
                  g.setColor(Color.WHITE);
                  g.fillPolygon(hex);
                  g.setColor(Color.BLACK);
                  g.drawPolygon(hex);
                }
                add(cellPanel);
              }
            }
          }
        };

    gridPanel.setLayout(null);
    gridPanel.setPreferredSize(new Dimension(width * CELL_SIZE + SIZE / 4, height * CELL_SIZE));
    frame.add(gridPanel);

    userInterface = new JPanel();
    userInterface.setPreferredSize(new Dimension(UI_WIDTH, height * CELL_SIZE));
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
            gridPanel.repaint();
            update_logs();
          } catch (IOException ex1) {
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
            gridPanel.repaint();
            update_logs();
          } catch (IOException ex1) {
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

    update_logs();

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
              organisms.removeIf(Organism::isDead);

              final int size = organisms.size();
              for (int i = 0; i < size; i++) {
                organisms.get(i).takeTurn();
              }
              update_logs();
              gridPanel.repaint();
            }
          }

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }

  private void update_logs() {
    logArea.setText("================\n* WORLD LOGS *\n================\n");
    for (var message : logs) {
      logArea.append(message + "\n");
    }
  }
}
