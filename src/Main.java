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
    var world = new World();
    UIManager.setLookAndFeel(new MaterialLookAndFeel());

    var frame = new JFrame("Henryk Wolek 193399");

    var panel = new JPanel();
    panel.setLayout(new FlowLayout());

    var font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

    var widthField = new JTextField(10);
    var heightField = new JTextField(10);
    var submitButton = new JButton("Submit");

    submitButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            world.setHeight(height);
            world.setWidth(width);

            frame.dispose();

            try {
              world.setGUI();
              world.display_world();
            } catch (UnsupportedLookAndFeelException | IOException | FontFormatException ex1) {
              return;
            }
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

    world.addOrganism(Factory.create(Wolf.class, new Pair<>(2, 2), world));
    world.addOrganism(Factory.create(Gazelle.class, new Pair<>(4, 4), world));

    var livings = world.getOrganisms();

    for (int i = 0; i < 2; i++) {
      var rand = new Random();
      var newI = rand.nextInt(10);
      var newJ = rand.nextInt(10);

      var newPos = new Pair<>(newI, newJ);
      var foundOrganism = livings.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();

      foundOrganism.ifPresentOrElse(
          o -> {}, () -> world.addOrganism(Factory.create(Sheep.class, newPos, world)));
    }
  }
}
