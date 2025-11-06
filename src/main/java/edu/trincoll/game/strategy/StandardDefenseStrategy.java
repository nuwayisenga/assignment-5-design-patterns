package edu.trincoll.game.strategy;

import edu.trincoll.game.model.Character;

/**
 * Standard defense that reduces damage by half the defense value.
 * Used by most character types for basic damage mitigation.
 *
 * <p><b>Reduction Formula:</b> {@code max(0, damage - defense/2)}</p>
 *
 * @author Noella Uwayisenga
 * @see DefenseStrategy
 */
public class StandardDefenseStrategy implements DefenseStrategy {
    @Override
    public int calculateDamageReduction(Character defender, int incomingDamage) {
        // Calculate damage reduction: defense / 2
        int damageReduction = defender.getStats().defense() / 2;

        // Actual damage = incoming damage - damage reduction
        int actualDamage = incomingDamage - damageReduction;

        // Ensure actual damage is never negative (minimum 0)
        return Math.max(0, actualDamage);
    }
}