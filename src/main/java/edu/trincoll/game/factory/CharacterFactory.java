package edu.trincoll.game.factory;

import edu.trincoll.game.model.Character;
import edu.trincoll.game.model.CharacterStats;
import edu.trincoll.game.model.CharacterType;
import edu.trincoll.game.strategy.*;

/**
 * Factory for creating pre-configured game characters using the Factory Method pattern.
 *
 * <p>This class provides static factory methods that encapsulate the complex process
 * of creating characters with appropriate stats, attack strategies, and defense strategies
 * based on their character type. This centralization ensures consistency and reduces
 * code duplication throughout the game system.</p>
 *
 * <h2>Factory Method Pattern Benefits</h2>
 * <ul>
 *   <li><b>Consistency:</b> All Warriors have identical base configuration</li>
 *   <li><b>Maintainability:</b> Character balance changes happen in one place</li>
 *   <li><b>Extensibility:</b> New character types can be added without modifying client code</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * // Create specific character types
 * Character warrior = CharacterFactory.createWarrior("Conan");
 * Character mage = CharacterFactory.createMage("Gandalf");
 *
 * // Or use generic factory
 * Character rogue = CharacterFactory.createCharacter("Shadow", CharacterType.ROGUE);
 * }</pre>
 *
 * @author Noella Uwayisenga
 * @author Gabriela Scavenius
 * @author Chris Burns
 * @version 1.0
 * @since Assignment 5
 */
public class CharacterFactory {

    /**
     * Creates a Warrior character optimized for melee combat and tanking.
     *
     * <p><b>Warrior Characteristics:</b> High HP (150), heavy armor, strong melee damage.
     * Warriors are frontline tanks who absorb damage and protect allies.</p>
     *
     * <p><b>Strategy Composition:</b></p>
     * <ul>
     *   <li>Attack: MeleeAttackStrategy (20% bonus damage)</li>
     *   <li>Defense: HeavyArmorDefenseStrategy (max 75% reduction)</li>
     * </ul>
     *
     * @param name the warrior's name (must not be null or empty)
     * @return a fully configured Warrior character ready for combat
     * @throws NullPointerException if name is null
     */
    public static Character createWarrior(String name) {
        return Character.builder()
                .name(name)
                .type(CharacterType.WARRIOR)
                .stats(CharacterStats.create(150, 40, 30, 0))
                .attackStrategy(new MeleeAttackStrategy())
                .defenseStrategy(new HeavyArmorDefenseStrategy())
                .build();
    }

    /**
     * Creates a Mage character specialized in magical damage and mana manipulation.
     *
     * <p><b>Mage Characteristics:</b> Highest attack power (60), large mana pool (100),
     * but low HP (80) and defense (10). Glass cannon that devastates from range.</p>
     *
     * <p><b>Mana Management:</b> Gains bonus damage based on current mana (mana/10)
     * but consumes 10 mana per attack. Strategic mana conservation is crucial.</p>
     *
     * @param name the mage's name (must not be null or empty)
     * @return a fully configured Mage character ready for combat
     * @throws NullPointerException if name is null
     */
    public static Character createMage(String name) {
        return Character.builder()
                .name(name)
                .type(CharacterType.MAGE)
                .stats(CharacterStats.create(80, 60, 10, 100))
                .attackStrategy(new MagicAttackStrategy())
                .defenseStrategy(new StandardDefenseStrategy())
                .build();
    }

    /**
     * Creates an Archer character balanced between damage and survivability.
     *
     * <p><b>Archer Characteristics:</b> Balanced stats with ranged attacks.
     * Critical hit mechanic: 50% bonus damage when target HP < 30%.</p>
     *
     * @param name the archer's name (must not be null or empty)
     * @return a fully configured Archer character ready for combat
     */
    public static Character createArcher(String name) {
        return Character.builder()
                .name(name)
                .type(CharacterType.ARCHER)
                .stats(CharacterStats.create(100, 50, 15, 20))
                .attackStrategy(new RangedAttackStrategy())
                .defenseStrategy(new StandardDefenseStrategy())
                .build();
    }

    /**
     * Creates a Rogue character focused on high sustained melee damage.
     *
     * <p><b>Rogue Characteristics:</b> High attack (55), moderate defense (20),
     * aggressive melee fighter with decent survivability.</p>
     *
     * @param name the rogue's name (must not be null or empty)
     * @return a fully configured Rogue character ready for combat
     */
    public static Character createRogue(String name) {
        return Character.builder()
                .name(name)
                .type(CharacterType.ROGUE)
                .stats(CharacterStats.create(90, 55, 20, 30))
                .attackStrategy(new MeleeAttackStrategy())
                .defenseStrategy(new StandardDefenseStrategy())
                .build();
    }

    /**
     * Generic factory method that creates a character of any type.
     *
     * <p>Demonstrates the Factory Method pattern's delegation using Java 21
     * switch expressions. The compiler ensures all enum values are handled.</p>
     *
     * <h3>Usage Example</h3>
     * <pre>{@code
     * CharacterType playerChoice = getUserSelection();
     * Character hero = CharacterFactory.createCharacter("Hero", playerChoice);
     * }</pre>
     *
     * @param name the character's name (must not be null)
     * @param type the type of character to create (must not be null)
     * @return a fully configured character of the specified type
     * @throws IllegalArgumentException if type is null
     */
    public static Character createCharacter(String name, CharacterType type) {
        if (type == null) {
            throw new IllegalArgumentException("Character type cannot be null");
        }
        return switch(type) {
            case WARRIOR -> createWarrior(name);
            case MAGE -> createMage(name);
            case ARCHER -> createArcher(name);
            case ROGUE -> createRogue(name);
        };
    }
}