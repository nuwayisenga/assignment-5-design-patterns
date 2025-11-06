package edu.trincoll.game.template;

import edu.trincoll.game.model.Character;

/**
 * Power attack sequence - charges up before attack, exhausted after.
 *
 * TODO 5c: Implement preAttackAction(), performAttack(), and postAttackAction()
 *
 * This demonstrates how hook methods can customize the template.
 */
public class PowerAttackSequence extends BattleSequence {
    private int damageBonus = 0;

    public PowerAttackSequence(Character attacker, Character defender) {
        super(attacker, defender);
    }

    /**
     * TODO 5c: Implement preAttackAction()
     *
     * Requirements:
     * 1. Calculate bonus: attacker's attack power / 4
     * 2. Store in damageBonus field
     * 3. This will be added during performAttack()
     */
    @Override
    protected void preAttackAction() {
        // Calculate bonus: attacker's attack power / 4
        damageBonus = attacker.getStats().attackPower() / 4;
    }

    /**
     * TODO 5c: Implement performAttack()
     *
     * Requirements:
     * 1. Calculate base damage: attacker.attack(defender)
     * 2. Add the damage bonus calculated in preAttackAction
     * 3. Apply total damage: defender.takeDamage(baseDamage + damageBonus)
     */
    @Override
    protected void performAttack() {
        // Calculate base damage: attacker.attack(defender)
        int baseDamage = attacker.attack(defender);

        // Add the damage bonus calculated in preAttackAction
        int totalDamage = baseDamage + damageBonus;

        // Apply total damage: defender.takeDamage(baseDamage + damageBonus)
        defender.takeDamage(totalDamage);
    }

    /**
     * TODO 5c: Implement postAttackAction()
     *
     * Requirements:
     * 1. Attacker is exhausted from power attack
     * 2. Take 10% of max health as recoil damage
     * 3. Use attacker.setHealth() to apply recoil directly
     *    (Can't use takeDamage as it applies defense)
     */
    @Override
    protected void postAttackAction() {
        // Calculate recoil damage: 10% of max health
        int recoilDamage = (int) (attacker.getStats().maxHealth() * 0.1);

        // Get current health and subtract recoil
        int currentHealth = attacker.getStats().health();
        int newHealth = currentHealth - recoilDamage;

        // Apply recoil damage directly using setHealth
        attacker.setHealth(newHealth);
    }
}