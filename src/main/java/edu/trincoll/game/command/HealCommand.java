package edu.trincoll.game.command;

import edu.trincoll.game.model.Character;

/**
 * Command to heal a character.
 *
 * TODO 4b: Implement execute() and undo()
 *
 * Requirements for execute():
 * 1. Store the target's current health before healing
 * 2. Heal the target: target.heal(amount)
 * 3. Store the target's health after healing
 * 4. Calculate actual healing done (after - before)
 *
 * Requirements for undo():
 * 1. Damage the target for the amount that was actually healed
 * 2. Use target.takeDamage(actualHealingDone)
 * 3. This will bypass defense and restore exact previous health
 *
 * Note: Need to track actual healing because you can't heal above max health.
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
        // TODO 4b: Implement heal execution
        throw new UnsupportedOperationException("TODO 4b: Implement HealCommand.execute()");
    }

    @Override
    public void undo() {
        // TODO 4b: Implement heal undo
        throw new UnsupportedOperationException("TODO 4b: Implement HealCommand.undo()");
    }

    @Override
    public String getDescription() {
        return String.format("Heal %s for %d HP", target.getName(), amount);
    }
}
