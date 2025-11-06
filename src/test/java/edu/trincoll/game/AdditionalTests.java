package edu.trincoll.game;

import edu.trincoll.game.command.AttackCommand;
import edu.trincoll.game.command.CommandInvoker;
import edu.trincoll.game.command.HealCommand;
import edu.trincoll.game.factory.CharacterFactory;
import edu.trincoll.game.model.Character;
import edu.trincoll.game.strategy.MagicAttackStrategy;
import edu.trincoll.game.strategy.RangedAttackStrategy;
import edu.trincoll.game.template.PowerAttackSequence;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Additional tests demonstrating deep understanding of design patterns.
 * These tests go beyond basic requirements to verify edge cases,
 * integration points, and boundary conditions.
 *
 * @author Gabriela Scavenius
 * @author Noella Uwayisenga
 * @author Chris Burns
 */
class AdditionalTests {

    // ============================================
    // EDGE CASE TESTS
    // ============================================

    @Test
    void testMagicAttackThrowsExceptionWhenInsufficientMana() {
        Character mage = CharacterFactory.createMage("LowManaMage");
        Character enemy = CharacterFactory.createWarrior("Tank");

        // Use up all but 5 mana
        while (mage.getStats().mana() > 5) {
            mage.useMana(10);
        }

        // Should throw exception when trying to attack with < 10 mana
        assertThatThrownBy(() -> mage.attack(enemy))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Not enough mana");
    }

    @Test
    void testRangedAttackCriticalHitAtExact30Percent() {
        Character archer = CharacterFactory.createArcher("PrecisionArcher");
        Character target = CharacterFactory.createWarrior("AlmostDeadWarrior");

        // Set target to exactly 30% health (45 out of 150)
        target.setHealth(45);

        // Should NOT trigger critical (needs to be < 30%, not <=)
        int damage = archer.attack(target);
        int expectedNormalDamage = (int) (50 * 0.8); // 40 damage

        assertThat(damage).isEqualTo(expectedNormalDamage);
    }

    @Test
    void testDefenseStrategyNeverProducesNegativeDamage() {
        Character warrior = CharacterFactory.createWarrior("SuperTank");

        // Attack with very low damage (less than defense reduction)
        int veryLowDamage = 5;
        int actualDamage = warrior.defend(veryLowDamage);

        // Should never be negative
        assertThat(actualDamage).isGreaterThanOrEqualTo(0);
    }

    // ============================================
    // INTEGRATION TESTS
    // ============================================

    @Test
    void testFactoryProducesCharactersWithCorrectStrategyTypes() {
        Character warrior = CharacterFactory.createWarrior("TestWarrior");
        Character mage = CharacterFactory.createMage("TestMage");

        // Verify strategies are correctly assigned
        assertThat(warrior.getAttackStrategy())
                .isInstanceOf(edu.trincoll.game.strategy.MeleeAttackStrategy.class);
        assertThat(mage.getAttackStrategy())
                .isInstanceOf(MagicAttackStrategy.class);
    }

    @Test
    void testMultipleCommandUndosRestoreCorrectState() {
        Character attacker = CharacterFactory.createWarrior("Attacker");
        Character target = CharacterFactory.createMage("Target");
        CommandInvoker invoker = new CommandInvoker();

        int initialTargetHealth = target.getStats().health();

        // Execute multiple commands
        invoker.executeCommand(new AttackCommand(attacker, target));
        invoker.executeCommand(new AttackCommand(attacker, target));
        invoker.executeCommand(new HealCommand(target, 20));

        // Undo all three
        invoker.undoLastCommand();
        invoker.undoLastCommand();
        invoker.undoLastCommand();

        // Should be back to initial state
        assertThat(target.getStats().health()).isEqualTo(initialTargetHealth);
    }

    @Test
    void testStrategyCanBeChangedAtRuntime() {
        Character versatileWarrior = CharacterFactory.createWarrior("Versatile");
        Character enemy = CharacterFactory.createMage("Enemy");

        int initialEnemyHealth = enemy.getStats().health();

        // Attack with melee strategy
        int meleeDamage = versatileWarrior.attack(enemy);
        enemy.takeDamage(meleeDamage);

        // Switch to ranged strategy
        versatileWarrior.setAttackStrategy(new RangedAttackStrategy());

        int healthAfterMelee = enemy.getStats().health();
        int rangedDamage = versatileWarrior.attack(enemy);
        enemy.takeDamage(rangedDamage);

        // Ranged damage should be different from melee (80% vs 120%)
        assertThat(rangedDamage).isNotEqualTo(meleeDamage);
        assertThat(enemy.getStats().health()).isLessThan(healthAfterMelee);
    }

    // ============================================
    // BOUNDARY CONDITION TESTS
    // ============================================

    @Test
    void testHealingCannotExceedMaxHealth() {
        Character character = CharacterFactory.createWarrior("OverhealTest");

        // Damage character first
        character.takeDamage(50);
        int healthAfterDamage = character.getStats().health();

        // Try to heal for more than damage taken
        character.heal(1000);

        // Should cap at max health
        assertThat(character.getStats().health())
                .isEqualTo(character.getStats().maxHealth());
    }

    @Test
    void testPowerAttackRecoilCannotKillAttacker() {
        Character warrior = CharacterFactory.createWarrior("SuicidalWarrior");
        Character dummy = CharacterFactory.createMage("Dummy");

        // Reduce warrior to very low health
        warrior.setHealth(5);

        PowerAttackSequence powerAttack = new PowerAttackSequence(warrior, dummy);
        powerAttack.executeTurn();

        // Warrior should still be alive (health might be 0 but not negative)
        assertThat(warrior.getStats().health()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void testHeavyArmorAlwaysAllowsMinimum25PercentDamage() {
        Character superTank = CharacterFactory.createWarrior("SuperTank");

        // Test with very high incoming damage
        int massiveDamage = 1000;
        int actualDamage = superTank.defend(massiveDamage);

        // Should allow at least 25% through
        int minimumDamage = (int) (massiveDamage * 0.25);
        assertThat(actualDamage).isGreaterThanOrEqualTo(minimumDamage);
    }

    @Test
    void testCommandHistoryMaintainsCorrectSize() {
        CommandInvoker invoker = new CommandInvoker();
        Character attacker = CharacterFactory.createWarrior("Attacker");
        Character target = CharacterFactory.createMage("Target");

        // Execute 5 commands
        for (int i = 0; i < 5; i++) {
            invoker.executeCommand(new AttackCommand(attacker, target));
        }

        assertThat(invoker.getCommandHistory()).hasSize(5);

        // Undo 2 commands
        invoker.undoLastCommand();
        invoker.undoLastCommand();

        assertThat(invoker.getCommandHistory()).hasSize(3);
    }

    // ============================================
    // PATTERN INTERACTION TESTS
    // ============================================

    @Test
    void testBuilderValidatesAllRequiredFields() {
        // Test that builder properly validates each field
        assertThatThrownBy(() ->
                Character.builder()
                        .type(edu.trincoll.game.model.CharacterType.WARRIOR)
                        .build()
        ).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("name is required");
    }

    @Test
    void testFactoryAndBuilderProduceSameResult() {
        Character factoryWarrior = CharacterFactory.createWarrior("Factory");
        Character builderWarrior = Character.builder()
                .name("Builder")
                .type(edu.trincoll.game.model.CharacterType.WARRIOR)
                .stats(edu.trincoll.game.model.CharacterStats.create(150, 40, 30, 0))
                .attackStrategy(new edu.trincoll.game.strategy.MeleeAttackStrategy())
                .defenseStrategy(new edu.trincoll.game.strategy.HeavyArmorDefenseStrategy())
                .build();

        // Should have same stats
        assertThat(factoryWarrior.getStats().maxHealth())
                .isEqualTo(builderWarrior.getStats().maxHealth());
        assertThat(factoryWarrior.getStats().attackPower())
                .isEqualTo(builderWarrior.getStats().attackPower());
    }
}