package edu.trincoll.game.strategy;

import edu.trincoll.game.model.Character;

/**
 * Ranged attack with accuracy penalty and critical hit potential.
 * Used by Archers. Deals 150% damage when target HP < 30%.
 *
 * <p><b>Critical Hit:</b> Finishes weakened enemies efficiently.</p>
 *
 * @author Noella Uwayisenga
 * @see AttackStrategy
 */
public class RangedAttackStrategy implements AttackStrategy {
    @Override
    public int calculateDamage(Character attacker, Character target) {
        // Base damage = attacker's attack power
        int baseDamage = attacker.getStats().attackPower();

        // Apply 80% accuracy (multiply by 0.8)
        int damageWithAccuracy = (int) (baseDamage * 0.8);

        // Check if target's health < 30% of max for critical hit
        int targetHealth = target.getStats().health();
        int targetMaxHealth = target.getStats().maxHealth();

        // Add critical hit bonus: if target's health < 30% of max, add 50% bonus
        if (targetHealth < targetMaxHealth * 0.3) {
            // Critical bonus: multiply by 1.5
            damageWithAccuracy = (int) (damageWithAccuracy * 1.5);
        }

        // Return total as integer
        return damageWithAccuracy;
    }
}