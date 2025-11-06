package edu.trincoll.game.strategy;

import edu.trincoll.game.model.Character;

/**
 * Heavy armor defense with maximum 75% damage reduction cap.
 * Used by Warriors. Always allows at least 25% damage through.
 *
 * <p><b>Cap Rationale:</b> Prevents game-breaking invulnerability.</p>
 *
 * @author Noella Uwayisenga
 * @see DefenseStrategy
 */
public class HeavyArmorDefenseStrategy implements DefenseStrategy {
    @Override
    public int calculateDamageReduction(Character defender, int incomingDamage) {
        // Calculate damage reduction: defense (full defense value)
        int damageReduction = defender.getStats().defense();

        // Actual damage = incoming damage - damage reduction
        int actualDamage = incomingDamage - damageReduction;

        // Maximum 75% damage reduction (even if defense is very high)
        // This means minimum 25% damage must get through
        int minimumDamage = (int) (incomingDamage * 0.25);

        // Return the greater of: minimum damage (25%) or actual damage
        // This ensures we never reduce more than 75%
        return Math.max(minimumDamage, actualDamage);
    }
}