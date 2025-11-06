package edu.trincoll.game.model;

import edu.trincoll.game.strategy.AttackStrategy;
import edu.trincoll.game.strategy.DefenseStrategy;

import java.util.Objects;

/**
 * Represents a game character with stats and behavior strategies.
 * This class will be constructed using the Builder pattern.
 */
public class Character {
    private final String name;
    private final CharacterType type;
    private CharacterStats stats;
    private AttackStrategy attackStrategy;
    private DefenseStrategy defenseStrategy;

    // Public constructor for testing - prefer Builder for production use
    public Character(String name, CharacterType type, CharacterStats stats,
                     AttackStrategy attackStrategy, DefenseStrategy defenseStrategy) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.type = Objects.requireNonNull(type, "Type cannot be null");
        this.stats = Objects.requireNonNull(stats, "Stats cannot be null");
        this.attackStrategy = Objects.requireNonNull(attackStrategy, "Attack strategy cannot be null");
        this.defenseStrategy = Objects.requireNonNull(defenseStrategy, "Defense strategy cannot be null");
    }

    // Getters
    public String getName() {
        return name;
    }

    public CharacterType getType() {
        return type;
    }

    public CharacterStats getStats() {
        return stats;
    }

    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }

    public DefenseStrategy getDefenseStrategy() {
        return defenseStrategy;
    }

    // Strategy setters (allow runtime strategy changes - Strategy pattern)
    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = Objects.requireNonNull(attackStrategy, "Attack strategy cannot be null");
    }

    public void setDefenseStrategy(DefenseStrategy defenseStrategy) {
        this.defenseStrategy = Objects.requireNonNull(defenseStrategy, "Defense strategy cannot be null");
    }

    // Combat methods that delegate to strategies
    public int attack(Character target) {
        return attackStrategy.calculateDamage(this, target);
    }

    public int defend(int incomingDamage) {
        return defenseStrategy.calculateDamageReduction(this, incomingDamage);
    }

    // Health management
    public void takeDamage(int damage) {
        int actualDamage = defend(damage);
        int netDamage = Math.max(0, actualDamage);
        stats = stats.withHealth(stats.health() - netDamage);
    }

    public void heal(int amount) {
        stats = stats.withHealth(stats.health() + amount);
    }

    /**
     * Set health directly (used for command undo operations and testing).
     * Use with caution - bypasses defense calculations.
     */
    public void setHealth(int health) {
        stats = stats.withHealth(health);
    }

    // Mana management
    public void useMana(int amount) {
        if (stats.mana() < amount) {
            throw new IllegalStateException("Not enough mana");
        }
        stats = stats.withMana(stats.mana() - amount);
    }

    public void restoreMana(int amount) {
        stats = stats.withMana(stats.mana() + amount);
    }

    // Status checks
    public boolean isAlive() {
        return stats.isAlive();
    }

    public boolean isDead() {
        return stats.isDead();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - HP: %d/%d, ATK: %d, DEF: %d",
                name, type, stats.health(), stats.maxHealth(),
                stats.attackPower(), stats.defense());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(name, character.name) &&
                type == character.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    /**
     * Builder for creating Character instances using the Builder pattern.
     *
     * <p><b>Builder Pattern Benefits:</b></p>
     * <ul>
     *   <li>Constructs complex objects step-by-step</li>
     *   <li>Validates all required fields before construction</li>
     *   <li>Provides fluent API for readable object creation</li>
     *   <li>Prevents incomplete or invalid Character objects</li>
     * </ul>
     *
     * <h2>Usage Example</h2>
     * <pre>{@code
     * Character hero = Character.builder()
     *     .name("Aragorn")
     *     .type(CharacterType.WARRIOR)
     *     .stats(CharacterStats.create(150, 40, 30, 0))
     *     .attackStrategy(new MeleeAttackStrategy())
     *     .defenseStrategy(new HeavyArmorDefenseStrategy())
     *     .build(); // Validates all fields are set
     * }</pre>
     *
     * @author Gabriela Scavenius
     * @author Noella Uwayisenga
     * @author Chris Burns
     */
    public static class Builder {
        private String name;
        private CharacterType type;
        private CharacterStats stats;
        private AttackStrategy attackStrategy;
        private DefenseStrategy defenseStrategy;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(CharacterType type) {
            this.type = type;
            return this;
        }

        public Builder stats(CharacterStats stats) {
            this.stats = stats;
            return this;
        }

        public Builder attackStrategy(AttackStrategy attackStrategy) {
            this.attackStrategy = attackStrategy;
            return this;
        }

        public Builder defenseStrategy(DefenseStrategy defenseStrategy) {
            this.defenseStrategy = defenseStrategy;
            return this;
        }

        /**
         * Validates all required fields and constructs the Character.
         *
         * <p><b>Validation:</b> Ensures name, type, stats, attackStrategy, and
         * defenseStrategy are all non-null before construction. This prevents
         * creation of invalid Character objects.</p>
         *
         * <p><b>Design Rationale:</b> Fail-fast validation at build time is better
         * than runtime errors during gameplay. This catches configuration errors
         * immediately during development.</p>
         *
         * @return a new fully-configured Character instance
         * @throws IllegalStateException if any required field is null
         */
        public Character build() {
            // Validate all required fields are non-null
            if (name == null) {
                throw new IllegalStateException("name is required");
            }
            if (type == null) {
                throw new IllegalStateException("type is required");
            }
            if (stats == null) {
                throw new IllegalStateException("stats is required");
            }
            if (attackStrategy == null) {
                throw new IllegalStateException("attackStrategy is required");
            }
            if (defenseStrategy == null) {
                throw new IllegalStateException("defenseStrategy is required");
            }

            // Construct and return character with all validated fields
            return new Character(name, type, stats, attackStrategy, defenseStrategy);
        }
    }

    /**
     * Static factory method to create a Builder.
     * This is the entry point for using the Builder pattern.
     */
    public static Builder builder() {
        return new Builder();
    }
}