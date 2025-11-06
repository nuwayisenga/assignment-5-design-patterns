package edu.trincoll.game.strategy;

import edu.trincoll.game.model.Character;

/**
 * Magic attack strategy that uses mana to amplify damage.
 * Used by Mages. Consumes 10 mana per attack.
 *
 * <p><b>Damage Formula:</b> {@code baseDamage + (currentMana / 10)}</p>
 *
 * @author Noella Uwayisenga
 * @see AttackStrategy
 */
public class MagicAttackStrategy implements AttackStrategy {
    @Override
    public int calculateDamage(Character attacker, Character target) {
        // Base damage = attacker's attack power
        int baseDamage = attacker.getStats().attackPower();

        // Mana bonus = current mana / 10 (integer division)
        int manaBonus = attacker.getStats().mana() / 10;

        // Total damage = base + mana bonus
        int totalDamage = baseDamage + manaBonus;

        // Reduce attacker's mana by 10
        attacker.useMana(10);

        // Return total damage
        return totalDamage;
    }
}