package edu.trincoll.game.command;

import edu.trincoll.game.model.Character;

/**
 * Command to execute and undo healing on a character.
 * Tracks actual healing done (capped at max HP) for proper undo.
 *
 * @author Chris Burns
 * @see GameCommand
 */
public class HealCommand implements GameCommand {
    private final Character target;
    private final int amount;
    private int actualHealingDone;

    public HealCommand(Character target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void execute() {
        // Store the target's current health before healing
        int healthBefore = target.getStats().health();

        // Heal the target
        target.heal(amount);

        // Store the target's health after healing
        int healthAfter = target.getStats().health();

        // Calculate actual healing done (after - before)
        actualHealingDone = healthAfter - healthBefore;
    }

    @Override
    public void undo() {
        // Restore health to before healing by removing the actual healing done
        // Get current health and subtract the actual healing that was done
        int currentHealth = target.getStats().health();
        int healthBeforeHeal = currentHealth - actualHealingDone;

        // Use setHealth() to set health directly (bypasses defense calculations)
        target.setHealth(healthBeforeHeal);
    }

    @Override
    public String getDescription() {
        return String.format("Heal %s for %d HP", target.getName(), amount);
    }
}