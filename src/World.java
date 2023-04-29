import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class World {
  private ArrayList<Organism> organisms;
  private int width = 0;
  private int height = 0;

  public World() {
    organisms = new ArrayList<>();
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void make_turn() {}

  public void display_world()
      throws UnsupportedLookAndFeelException, IOException, FontFormatException {
    UIManager.setLookAndFeel(new MaterialLookAndFeel());

    var frame = new JFrame("Hustlers University");

    var gridPanel = new JPanel(new GridLayout(height, width));
    Font font = new Font("Segoe UI Emoji", Font.PLAIN, 72);
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

    frame.add(gridPanel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
