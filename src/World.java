import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class World {
  private ArrayList<Organism> organisms;
  private int width = 0;
  private int height = 0;
  private JFrame frame;
  private JPanel gridPanel;
  private JPanel userInterface;
  private JSplitPane splitPane;
  private Font font;
  private Font regularFont;

  public World() {
    organisms = new ArrayList<>();
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setGUI() throws IOException, FontFormatException {
    frame = new JFrame("University of sparkling water");

    font = new Font("Segoe UI Emoji", Font.PLAIN, 72);
    regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

    gridPanel = new JPanel(new GridLayout(height, width));

    userInterface = new JPanel();
    userInterface.setPreferredSize(new Dimension(width * 75, height * 75));
    userInterface.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    userInterface.setBackground(MaterialColors.LIGHT_BLUE_50);
    frame.add(gridPanel);

    var mainLabel = new JLabel("Henryk Wołek 193399");
    mainLabel.setFont(regularFont.deriveFont(Font.PLAIN, 24));

    userInterface.add(mainLabel);

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

  public void make_turn() {
    for (var organism : organisms) {
      organism.takeTurn();
    }
  }

  public void display_world()
      throws UnsupportedLookAndFeelException, IOException, FontFormatException {
    UIManager.setLookAndFeel(new MaterialLookAndFeel());
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        var cellPanel = new JPanel();
        cellPanel.setPreferredSize(new Dimension(75, 75));
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
    frame.setDefaultCloseOperation(
        JFrame.DISPOSE_ON_CLOSE); // close current frame when opening next
    frame.setVisible(true);
  }

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
}
