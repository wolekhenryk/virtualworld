import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Semaphore;

public final class Human extends Animal {
  private static final int humanStrength = 5;
  private static final int humanInitiative = 4;
  boolean hasActivatedKeyListener = false;
  KeyListener listener;
  private int specialAbilityDuration;
  private int specialAbilityCooldown;
  private boolean specialAbilityActive = false;

  public Human(Pair<Integer, Integer> position, World world) {
    super(position, world);
    this.setInitiative(humanInitiative);
    this.setStrength(humanStrength);
  }

  @Override
  public void display(String content, JPanel panel, JLabel label) {
    content = "\ud83e\uddd9";
    panel.setBackground(new Color(255, 255, 255));
    label.setForeground(new Color(34, 45, 119));
    label.setText(content);
  }

  @Override
  public void takeTurn() {
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

    if (!hasActivatedKeyListener) getWorld().getFrame().addKeyListener(listener);

    if (this.specialAbilityActive) castSpecialAbility();

    if (this.specialAbilityDuration == 0 && this.specialAbilityActive) deactivateSpecialAbility();
  }

  private void castSpecialAbility() {
    this.specialAbilityDuration--;
    getWorld().message("Ability will last for " + specialAbilityDuration + " more rounds");
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
    return this.specialAbilityDuration;
  }
}
