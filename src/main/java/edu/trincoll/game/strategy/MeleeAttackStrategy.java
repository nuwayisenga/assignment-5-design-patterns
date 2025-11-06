package edu.trincoll.game.strategy;

import edu.trincoll.game.model.Character;

/**
 * Melee attack - pure physical damage with bonus.
 * Used by Warriors and Rogues.
 *
 * TODO 1a: Implement calculateDamage()
 *
 * Requirements:
 * - Base damage = attacker's attack power
 * - Add 20% bonus (multiply by 1.2)
 * - Return total as integer
 *
 * Example: If attacker has 40 attack power:
 *   Base: 40
 *   With bonus: 40 * 1.2 = 48
 *   Return: 48
 */
public class MeleeAttackStrategy implements AttackStrategy {
    @Override
    public int calculateDamage(Character attacker, Character target) {
        // Base damage = attacker's attack power
        int baseDamage = attacker.getStats().attackPower();

        // Add 20% bonus (multiply by 1.2)
        int totalDamage = (int) (baseDamage * 1.2);

        // Return total as integer
        return totalDamage;
    }
}