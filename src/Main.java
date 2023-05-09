import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.MaterialColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {

  public static void main(String[] args)
      throws UnsupportedLookAndFeelException, IOException, FontFormatException {
    var world = new WorldHexImpl();
    UIManager.setLookAndFeel(new MaterialLookAndFeel());

    var frame = new JFrame("Henryk Wolek 193399");

    var panel = new JPanel();
    panel.setLayout(new FlowLayout());

    var font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

    var widthField = new JTextField(10);
    var heightField = new JTextField(10);
    var submitButton = new JButton("Submit");

    submitButton.addActionListener(
        e -> {
          int width = Integer.parseInt(widthField.getText());
          int height = Integer.parseInt(heightField.getText());

          world.setHeight(height);
          world.setWidth(width);

          frame.dispose();

          try {
            world.setGUI();

            world.placeOrganism(Barszcz.class);
            world.placeOrganism(Fox.class);
            world.placeOrganism(Gazelle.class);
            world.placeOrganism(Grass.class);
            world.placeOrganism(Grass.class);
            world.placeOrganism(Guarana.class);
            world.placeOrganism(Guarana.class);
            world.placeOrganism(Mlecz.class);
            world.placeOrganism(Nightshade.class);
            world.placeOrganism(Sheep.class);
            world.placeOrganism(Tortoise.class);
            world.placeOrganism(Wolf.class);
            world.placeOrganism(Human.class);

            world.display_world();
          } catch (UnsupportedLookAndFeelException | IOException | FontFormatException ex1) {
            return;
          }
        });

    panel.add(new JLabel("Width:"));
    panel.add(widthField);
    panel.add(new JLabel("Height:"));
    panel.add(heightField);
    panel.add(submitButton);

    frame.add(panel);

    frame.setSize(400, 150);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setBackground(MaterialColors.LIGHT_BLUE_50);
    frame.setVisible(true);
  }
}
