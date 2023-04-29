import mdlaf.MaterialLookAndFeel;
import mdlaf.utils.*;
import mdlaf.utils.icons.MaterialIconFont;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
public class World {
    private ArrayList<Organism> livings;
    private int width = 0;
    private int height = 0;
    public World(int width, int height) {
        this.height = height;
        this.width = width;
    }
    public World() {}
    public void make_turn() {

    }
    public void display_world() throws UnsupportedLookAndFeelException, IOException, FontFormatException {
        UIManager.setLookAndFeel(new MaterialLookAndFeel());

        var frame = new JFrame("Hustlers University");

        var panel = new JPanel();
        panel.setLayout(new FlowLayout());

        var font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Kanit-Regular.ttf"));

        var header = new JLabel("Escaping the matrix...");
        header.setForeground(MaterialColors.BLUE_500);
        header.setFont(font.deriveFont(Font.PLAIN, 24f));

        panel.add(header, BorderLayout.CENTER);
        frame.add(panel);

        frame.setSize(width * 50, height * 50);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(MaterialColors.LIGHT_BLUE_50);
        frame.setVisible(true);
    }



    public ArrayList<Organism> getOrganisms() {return livings;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public void addOrganism(Organism newOrganism) {livings.add(newOrganism);}
}
