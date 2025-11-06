package edu.trincoll.game.command;

import edu.trincoll.game.model.Character;

/**
 * Command to execute and undo an attack between characters.
 * Stores damage dealt for accurate undo operation.
 *
 * @author Chris Burns
 * @see GameCommand
 */
public class AttackCommand implements GameCommand {
    private final Character attacker;
    private final Character target;
    private int damageDealt;

    public AttackCommand(Character attacker, Character target) {
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void execute() {
        // Calculate damage: attacker.attack(target)
        damageDealt = attacker.attack(target);

        // Apply damage: target.takeDamage(calculatedDamage)
        target.takeDamage(damageDealt);
    }

    @Override
    public void undo() {
        // Heal the target for the amount of damage that was dealt
        target.heal(damageDealt);
    }

    @Override
    public String getDescription() {
        return String.format("%s attacks %s", attacker.getName(), target.getName());
    }
}