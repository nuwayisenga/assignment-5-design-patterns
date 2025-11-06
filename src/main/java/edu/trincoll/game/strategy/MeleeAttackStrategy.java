package edu.trincoll.game.strategy;

import edu.trincoll.game.model.Character;

/**
 * Implements melee attack strategy with 20% damage bonus for close-range combat.
 * Used by Warriors and Rogues who engage enemies in hand-to-hand combat.
 *
 * <p><b>Strategy Pattern:</b> This class encapsulates the melee attack algorithm,
 * allowing it to be interchanged with other attack strategies at runtime.</p>
 *
 * <h2>Damage Calculation</h2>
 * <p>Formula: {@code damage = floor(attackPower × 1.2)}</p>
 * <p>Uses integer arithmetic for deterministic, reproducible results.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * Character warrior = CharacterFactory.createWarrior("Conan");
 * int damage = warrior.attack(enemy); // Applies 1.2x multiplier
 * }</pre>
 *
 * @author Noella Uwayisenga
 * @author Gabriela Scavenius
 * @author Chris Burns
 * @see AttackStrategy
 * @see Character#attack(Character)
 */
public class MeleeAttackStrategy implements AttackStrategy {
    /**
     * Calculates damage for a melee attack with 20% bonus.
     *
     * <p><b>Example:</b> If attacker has 40 attack power:
     * <ul>
     *   <li>Base damage: 40</li>
     *   <li>With 20% bonus: 40 × 1.2 = 48</li>
     * </ul>
     *
     * @param attacker the character performing the melee attack
     * @param target the character being attacked (not used in calculation)
     * @return the calculated melee damage as a positive integer
     */
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