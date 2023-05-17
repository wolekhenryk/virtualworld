import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class Human extends Animal {
  private static final int HUMAN_STRENGTH = 5;
  private static final int HUMAN_INITIATIVE = 4;
  boolean hasActivatedKeyListener = false;
  KeyListener listener;
  private int specialAbilityDuration;
  private int specialAbilityCooldown;
  private boolean specialAbilityActive = false;

  public Human(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(HUMAN_INITIATIVE);
    this.setStrength(HUMAN_STRENGTH);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\ud83e\uddd9";
    panel.setBackground(new Color(255, 255, 255));
    label.setForeground(new Color(34, 45, 119));
    label.setText(content);
  }

  @Override
  public void display(Graphics g, int x, int y, Polygon p) {
    g.setColor(new Color(255, 255, 255));
    g.fillPolygon(p);
    g.setColor(new Color(34, 45, 119));
    g.drawPolygon(p);
    g.drawString("\ud83e\uddd9", x, y);
  }

  @Override
  public void takeTurn() {
    if (this.isDead()) {
      getWorld().frame.removeKeyListener(listener);
    }
    listener =
        new KeyListener() {

          @Override
          public void keyTyped(KeyEvent e) {}

          @Override
          public void keyPressed(KeyEvent e) {
            final int currI = getPosition().first();
            final int currJ = getPosition().second();

            var newI = currI;
            var newJ = currJ;

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
              newJ--;
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
              newI--;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
              newJ++;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
              newI++;
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE
                && !specialAbilityActive
                && specialAbilityCooldown == 0) {
              activateSpecialAbility();
              getWorld().message("Human activated ability. It will last for 5 rounds");
            }

            if (!invalidCoords(newI, newJ)) {
              var newPos = new Pair<>(newI, newJ);
              var livings = getWorld().getOrganisms();
              var foundOrganism =
                  livings.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();
              if (foundOrganism.isPresent()) {
                collision(foundOrganism.get());
              } else {
                setPosition(newPos);
              }
            }

            hasActivatedKeyListener = false;
            getWorld().getFrame().removeKeyListener(listener);
          }

          @Override
          public void keyReleased(KeyEvent e) {}
        };

    displayStats();

    if (!hasActivatedKeyListener) getWorld().getFrame().addKeyListener(listener);

    if (this.specialAbilityActive) castSpecialAbility();

    if (this.specialAbilityDuration == 0 && this.specialAbilityActive) deactivateSpecialAbility();

    if (!this.specialAbilityActive && this.specialAbilityCooldown != 0)
      this.specialAbilityCooldown--;
  }

  private void castSpecialAbility() {
    this.specialAbilityDuration--;

    var livings = getWorld().getOrganisms();
    var currI = getPosition().first();
    var currJ = getPosition().second();

    for (int i = 0; i < 4; i++) {
      var newI = currI + dy[i];
      var newJ = currJ + dx[i];

      if (invalidCoords(newI, newJ)) continue;

      var newPos = new Pair<>(newI, newJ);

      var foundOrganism = livings.stream().filter(o -> o.getPosition().equals(newPos)).findFirst();

      foundOrganism.ifPresent(Organism::die);
    }
  }

  public void displayStats() {
    getWorld()
        .humanStats
        .setText(
            "Is special ability active? "
                + (this.specialAbilityActive ? "YES" : "NO")
                + "\n"
                + "Special ability duration left: "
                + (this.specialAbilityActive ? this.specialAbilityDuration : "N/A")
                + "\n"
                + "Special ability cooldown left: "
                + ((!this.specialAbilityActive && this.specialAbilityCooldown != 0)
                    ? this.specialAbilityCooldown
                    : "N/A")
                + "\n");
  }

  public void activateSpecialAbility() {
    this.specialAbilityActive = true;
    this.specialAbilityDuration = 5;
  }

  private void deactivateSpecialAbility() {
    this.specialAbilityActive = false;
    this.specialAbilityCooldown = 5;
    getWorld().message("Ability deactivated, cooldown is now 5 rounds");
  }

  public void removeHumanListener() {
    getWorld().frame.removeKeyListener(listener);
  }

  public boolean isAbilityActive() {
    return this.specialAbilityActive;
  }

  public void setSpecialAbilityDuration(int duration) {
    this.specialAbilityDuration = duration;
  }

  public void setSpecialAbilityCooldown(int cooldown) {
    this.specialAbilityCooldown = cooldown;
  }

  public int getSpecialAbilityDuration() {
    return this.specialAbilityDuration;
  }

  public int getSpecialAbilityCooldown() {
    return this.specialAbilityCooldown;
  }
}
