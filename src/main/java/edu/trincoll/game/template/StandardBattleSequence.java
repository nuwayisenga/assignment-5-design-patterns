package edu.trincoll.game.template;

import edu.trincoll.game.model.Character;

/**
 * Standard battle sequence with simple direct attack.
 * No special preparation or follow-up actions.
 *
 * @author Chris Burns
 */
public class StandardBattleSequence extends BattleSequence {

    public StandardBattleSequence(Character attacker, Character defender) {
        super(attacker, defender);
    }

    /**
     * TODO 5b: Implement performAttack()
     *
     * Requirements:
     * 1. Calculate damage: attacker.attack(defender)
     * 2. Apply damage: defender.takeDamage(calculatedDamage)
     */
    @Override
    protected void performAttack() {
        // Calculate damage: attacker.attack(defender)
        int damage = attacker.attack(defender);

        // Apply damage: defender.takeDamage(calculatedDamage)
        defender.takeDamage(damage);
    }
}