# Assignment 5: Design Patterns - Game Character System

- **Due:** Thursday, November 6 at 11:59 PM
- **Points:** 100
- **Submission:** Via GitHub (one per team)
- **Build Requirements:** Gradle 9.1.0, Java 21 or higher (including Java 25)

## Overview

This assignment brings together **five essential design patterns** from Weeks 7-8 in one cohesive game character system. You'll implement behavioral patterns (Strategy, Command, Template Method) and creational patterns (Factory Method, Builder) while understanding how patterns work together in real applications.

## Learning Objectives

- Master the Strategy pattern for interchangeable algorithms
- Use Factory Method for object creation with appropriate defaults
- Build complex objects with the Builder pattern
- Implement Command pattern for undoable actions
- Apply Template Method for customizable algorithms
- Understand how patterns complement each other
- Write comprehensive tests for pattern implementations

## What You're Given

A game character system with complete domain models but missing pattern implementations:

- ✅ **Domain Models**: `Character`, `CharacterStats`, `CharacterType` (all complete)
- ✅ **Test Suite**: 60+ comprehensive tests (many will fail until you implement TODOs)
- ❌ **Strategy Pattern**: Attack and defense strategy implementations (5 TODOs)
- ❌ **Factory Method**: Character creation factory (5 TODOs)
- ❌ **Builder Pattern**: Character builder implementation (1 TODO)
- ❌ **Command Pattern**: Game commands and invoker (3 TODOs)
- ❌ **Template Method**: Battle sequence templates (3 TODOs)

Your task: **Implement all TODOs to make the tests pass**.

## Pattern Overview

### 1. Strategy Pattern (Week 7) - 30 points
- **Problem**: Different characters need different attack and defense behaviors.
- **Solution**: Encapsulate algorithms in strategy classes that can be swapped at runtime.

**You'll implement**:
- `MeleeAttackStrategy` - Physical damage with bonus
- `MagicAttackStrategy` - Mana-based damage
- `RangedAttackStrategy` - Accuracy-based with critical hits
- `StandardDefenseStrategy` - Basic damage reduction
- `HeavyArmorDefenseStrategy` - Enhanced protection with cap

### 2. Factory Method (Week 8) - 20 points
- **Problem**: Creating characters with appropriate stats and strategies is complex.
- **Solution**: Factory methods that create pre-configured character types.

**You'll implement**:
- `createWarrior()` - High health, melee attack, heavy defense
- `createMage()` - High mana, magic attack, standard defense
- `createArcher()` - Balanced stats, ranged attack
- `createRogue()` - Agile stats, melee attack
- `createCharacter()` - Generic factory using switch expression

### 3. Builder Pattern (Week 8) - 15 points
- **Problem**: Characters have many required fields - constructor gets unwieldy.
- **Solution**: Fluent builder API for constructing characters step-by-step.

**You'll implement**:
- `Builder.build()` - Validates all required fields and constructs character

### 4. Command Pattern (Week 8) - 20 points
- **Problem**: Need to execute and undo game actions.
- **Solution**: Encapsulate actions as command objects with execute/undo methods.

**You'll implement**:
- `AttackCommand` - Execute attack and undo damage
- `HealCommand` - Execute healing and undo
- `CommandInvoker` - Execute commands and maintain history for undo

### 5. Template Method (Week 8) - 15 points
- **Problem**: Battle sequences share common structure but vary in details.
- **Solution**: Define algorithm skeleton with customizable steps.

**You'll implement**:
- `BattleSequence.executeTurn()` - Template method defining the sequence
- `StandardBattleSequence` - Simple attack
- `PowerAttackSequence` - Charge-up, powered attack, recoil

## Assignment Structure (17 TODOs)

### Part 1: Strategy Pattern (5 TODOs - 30 points)

#### TODO 1a: Melee Attack Strategy (6 points)
**File**: `src/main/java/edu/trincoll/game/strategy/MeleeAttackStrategy.java`

```java
@Override
public int calculateDamage(Character attacker, Character target) {
    // Base damage = attacker's attack power
    // Add 20% bonus (multiply by 1.2)
    // Return as integer
}
```

**Tests**: `StrategyPatternTest.testMeleeAttack()`

#### TODO 1b: Magic Attack Strategy (6 points)
**File**: `src/main/java/edu/trincoll/game/strategy/MagicAttackStrategy.java`

```java
@Override
public int calculateDamage(Character attacker, Character target) {
    // Base damage = attacker's attack power
    // Mana bonus = current mana / 10
    // Total = base + mana bonus
    // Consume 10 mana: attacker.useMana(10)
    // Return total damage
}
```

**Tests**: `StrategyPatternTest.testMagicAttack()`

#### TODO 1c: Ranged Attack Strategy (6 points)
**File**: `src/main/java/edu/trincoll/game/strategy/RangedAttackStrategy.java`

```java
@Override
public int calculateDamage(Character attacker, Character target) {
    // Base damage = attack power * 0.8 (accuracy)
    // If target health < 30% of max: critical hit (damage * 1.5)
    // Return total as integer
}
```

**Tests**: `StrategyPatternTest.testRangedAttackNoCritical()`, `testRangedAttackCritical()`

#### TODO 1d: Standard Defense Strategy (6 points)
**File**: `src/main/java/edu/trincoll/game/strategy/StandardDefenseStrategy.java`

```java
@Override
public int calculateDamageReduction(Character defender, int incomingDamage) {
    // Damage reduction = defense / 2
    // Actual damage = incoming - reduction
    // Return max(0, actualDamage) // Never negative
}
```

**Tests**: `StrategyPatternTest.testStandardDefense()`, `testStandardDefenseFloor()`

#### TODO 1e: Heavy Armor Defense Strategy (6 points)
**File**: `src/main/java/edu/trincoll/game/strategy/HeavyArmorDefenseStrategy.java`

```java
@Override
public int calculateDamageReduction(Character defender, int incomingDamage) {
    // Damage reduction = full defense value
    // Actual damage = incoming - reduction
    // Cap: max 75% reduction (min 25% damage gets through)
    // Return max(incomingDamage * 0.25, actualDamage)
}
```

**Tests**: `StrategyPatternTest.testHeavyArmorDefense()`, `testHeavyArmorCap()`

---

### Part 2: Factory Method Pattern (5 TODOs - 20 points)

#### TODO 2a: Create Warrior (3 points)
**File**: `src/main/java/edu/trincoll/game/factory/CharacterFactory.java`

```java
public static Character createWarrior(String name) {
    return Character.builder()
        .name(name)
        .type(CharacterType.WARRIOR)
        .stats(CharacterStats.create(150, 40, 30, 0))
        .attackStrategy(new MeleeAttackStrategy())
        .defenseStrategy(new HeavyArmorDefenseStrategy())
        .build();
}
```

**Tests**: `FactoryPatternTest.testCreateWarrior()`

#### TODO 2b: Create Mage (3 points)
```java
// Stats: 80 HP, 60 ATK, 10 DEF, 100 MANA
// Attack: MagicAttackStrategy
// Defense: StandardDefenseStrategy
```

**Tests**: `FactoryPatternTest.testCreateMage()`

#### TODO 2c: Create Archer (3 points)
```java
// Stats: 100 HP, 50 ATK, 15 DEF, 20 MANA
// Attack: RangedAttackStrategy
// Defense: StandardDefenseStrategy
```

**Tests**: `FactoryPatternTest.testCreateArcher()`

#### TODO 2d: Create Rogue (3 points)
```java
// Stats: 90 HP, 55 ATK, 20 DEF, 30 MANA
// Attack: MeleeAttackStrategy
// Defense: StandardDefenseStrategy
```

**Tests**: `FactoryPatternTest.testCreateRogue()`

#### TODO 2e: Generic Factory Method (8 points)
```java
public static Character createCharacter(String name, CharacterType type) {
    // Use switch expression (Java 21!) to delegate:
    return switch(type) {
        case WARRIOR -> createWarrior(name);
        case MAGE -> createMage(name);
        case ARCHER -> createArcher(name);
        case ROGUE -> createRogue(name);
    };
}
```

**Tests**: `FactoryPatternTest.testGenericFactory*()`

---

### Part 3: Builder Pattern (1 TODO - 15 points)

#### TODO 3: Implement Builder.build() (15 points)
**File**: `src/main/java/edu/trincoll/game/model/Character.java`

```java
public Character build() {
    // Validate all required fields are non-null
    if (name == null) throw new IllegalStateException("name is required");
    if (type == null) throw new IllegalStateException("type is required");
    if (stats == null) throw new IllegalStateException("stats is required");
    if (attackStrategy == null) throw new IllegalStateException("attackStrategy is required");
    if (defenseStrategy == null) throw new IllegalStateException("defenseStrategy is required");

    // Construct and return character
    return new Character(name, type, stats, attackStrategy, defenseStrategy);
}
```

**Tests**: `BuilderPatternTest.testBuilderComplete()`, `testBuilderMissing*()`

---

### Part 4: Command Pattern (3 TODOs - 20 points)

#### TODO 4a: Attack Command (6 points)
**File**: `src/main/java/edu/trincoll/game/command/AttackCommand.java`

```java
@Override
public void execute() {
    damageDealt = attacker.attack(target);
    target.takeDamage(damageDealt);
}

@Override
public void undo() {
    target.heal(damageDealt);
}
```

**Tests**: `CommandPatternTest.testAttackCommandExecute()`, `testAttackCommandUndo()`

#### TODO 4b: Heal Command (6 points)
**File**: `src/main/java/edu/trincoll/game/command/HealCommand.java`

```java
@Override
public void execute() {
    int healthBefore = target.getStats().health();
    target.heal(amount);
    int healthAfter = target.getStats().health();
    actualHealingDone = healthAfter - healthBefore;
}

@Override
public void undo() {
    target.takeDamage(actualHealingDone);
}
```

**Tests**: `CommandPatternTest.testHealCommandExecute()`, `testHealCommandUndo()`

#### TODO 4c: Command Invoker (8 points)
**File**: `src/main/java/edu/trincoll/game/command/CommandInvoker.java`

```java
public void executeCommand(GameCommand command) {
    command.execute();
    commandHistory.push(command);
}

public void undoLastCommand() {
    if (commandHistory.isEmpty()) return;
    GameCommand command = commandHistory.pop();
    command.undo();
}
```

**Tests**: `CommandPatternTest.testInvokerExecuteCommand()`, `testInvokerUndoLastCommand()`

---

### Part 5: Template Method Pattern (3 TODOs - 15 points)

#### TODO 5a: Template Method (5 points)
**File**: `src/main/java/edu/trincoll/game/template/BattleSequence.java`

```java
public final void executeTurn() {
    beginTurn();
    preAttackAction();
    performAttack();
    postAttackAction();
    endTurn();
}
```

**Tests**: `TemplateMethodTest.testTemplateMethodSequence()`

#### TODO 5b: Standard Battle Sequence (5 points)
**File**: `src/main/java/edu/trincoll/game/template/StandardBattleSequence.java`

```java
@Override
protected void performAttack() {
    int damage = attacker.attack(defender);
    defender.takeDamage(damage);
}
```

**Tests**: `TemplateMethodTest.testStandardBattleSequence()`

#### TODO 5c: Power Attack Sequence (5 points)
**File**: `src/main/java/edu/trincoll/game/template/PowerAttackSequence.java`

```java
@Override
protected void preAttackAction() {
    damageBonus = attacker.getStats().attackPower() / 4;
}

@Override
protected void performAttack() {
    int baseDamage = attacker.attack(defender);
    defender.takeDamage(baseDamage + damageBonus);
}

@Override
protected void postAttackAction() {
    int recoil = (int) (attacker.getStats().maxHealth() * 0.1);
    attacker.takeDamage(recoil);
}
```

**Tests**: `TemplateMethodTest.testPowerAttackDamage()`, `testPowerAttackRecoil()`

---

## How Patterns Work Together

```
User wants to create a warrior and execute an attack:

1. FACTORY METHOD creates the warrior with appropriate defaults
   CharacterFactory.createWarrior("Conan")

2. BUILDER validates all required fields during construction
   Character.builder()...build()

3. STRATEGY PATTERN determines attack behavior
   warrior uses MeleeAttackStrategy

4. COMMAND PATTERN encapsulates the action
   AttackCommand(warrior, enemy).execute()

5. TEMPLATE METHOD defines attack sequence
   BattleSequence.executeTurn() orchestrates the flow
```

## Getting Started

### 1. Fork and Clone
```bash
git clone [your-forked-repo-url]
cd assignment-5-design-patterns
```

### 2. Verify Setup
```bash
./gradlew test
# Many tests will fail - this is expected!
# You'll see which TODOs need implementation
```

### 3. Implementation Strategy

**Recommended order**:

1. **Start with Strategy (TODO 1a-1e)** - Foundation for everything else
   - Implement attack strategies first
   - Then defense strategies
   - Run `./gradlew test --tests StrategyPatternTest`

2. **Builder Pattern (TODO 3)** - Needed for Factory
   - Simple validation logic
   - Run `./gradlew test --tests BuilderPatternTest`

3. **Factory Method (TODO 2a-2e)** - Uses Builder and Strategies
   - Implement specific factories
   - Then generic factory with switch
   - Run `./gradlew test --tests FactoryPatternTest`

4. **Command Pattern (TODO 4a-4c)** - Uses completed characters
   - Implement commands
   - Then invoker
   - Run `./gradlew test --tests CommandPatternTest`

5. **Template Method (TODO 5a-5c)** - Final integration
   - Implement template method
   - Then concrete sequences
   - Run `./gradlew test --tests TemplateMethodTest`

### 4. Run All Tests
```bash
./gradlew test
./gradlew jacocoTestReport
# Check build/reports/jacoco/test/html/index.html
```

## Testing Requirements

### Minimum Coverage: 80%
Your implementation must achieve at least 80% code coverage.

```bash
./gradlew test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

### Test Organization
Tests are organized by pattern:
- `StrategyPatternTest` - 12 tests for attack/defense strategies
- `FactoryPatternTest` - 10 tests for character creation
- `BuilderPatternTest` - 8 tests for builder validation
- `CommandPatternTest` - 12 tests for commands and invoker
- `TemplateMethodTest` - 6 tests for battle sequences

### Running Specific Tests
```bash
# Run tests for one pattern
./gradlew test --tests StrategyPatternTest

# Run one specific test
./gradlew test --tests "StrategyPatternTest.testMeleeAttack"

# Run with detailed output
./gradlew test --info
```

## Grading Rubric

| Component            | TODOs | Points  | Tests                          |
|----------------------|-------|---------|--------------------------------|
| **Strategy Pattern** | 1a-1e | 30      | StrategyPatternTest (12 tests) |
| **Factory Method**   | 2a-2e | 20      | FactoryPatternTest (10 tests)  |
| **Builder Pattern**  | 3     | 15      | BuilderPatternTest (8 tests)   |
| **Command Pattern**  | 4a-4c | 20      | CommandPatternTest (12 tests)  |
| **Template Method**  | 5a-5c | 15      | TemplateMethodTest (6 tests)   |
| **Total**            | 17    | **100** | **48 tests**                   |

### Quality Criteria
- All tests pass ✓
- 80%+ code coverage ✓
- Clean code (no warnings) ✓
- Proper use of Java 21 features ✓
- Clear commit messages ✓

## Modern Java Features Used

This assignment uses **Java 21 LTS** features:

### Records (Java 16+)
```java
public record CharacterStats(int health, int maxHealth, ...) {
    // Immutable data with automatic equals/hashCode/toString
}
```

### Switch Expressions (Java 14+)
```java
return switch(type) {
    case WARRIOR -> createWarrior(name);
    case MAGE -> createMage(name);
    // ...
};
```

### Pattern Matching (Java 16+)
Used in tests for type checking

### Functional Interfaces
```java
@FunctionalInterface
public interface AttackStrategy {
    int calculateDamage(Character attacker, Character target);
}
```

Allows lambda implementations:
```java
character.setAttackStrategy((attacker, target) -> 100);
```

## AI Collaboration Requirements

Document at the top of any file where you used significant AI assistance:

```java
/**
 * AI Collaboration Summary:
 * Tool: [ChatGPT/Claude/Copilot/Gemini]
 *
 * What AI Helped With:
 * 1. [e.g., "Suggested calculation formula for critical hits"]
 * 2. [e.g., "Helped debug command undo logic"]
 *
 * What I Had to Fix:
 * 1. [e.g., "AI suggested mutable stats, changed to immutable"]
 * 2. [e.g., "AI missed the 75% damage reduction cap"]
 *
 * What I Learned:
 * - [Key insights about design patterns]
 * - [Understanding of when to use each pattern]
 *
 * Team: [Member names and contributions]
 */
```

## Common Pitfalls

❌ **DON'T:**
- Implement patterns without understanding why
- Skip the tests - they guide your implementation
- Make defensive checks nullable when builder enforces non-null
- Forget to update command state in undo()
- Implement template method as non-final

✅ **DO:**
- Read the test names - they explain requirements
- Run tests after each TODO completion
- Use integer arithmetic (avoid floating point precision issues)
- Remember strategies can be lambdas for custom behavior
- Follow the Java 21 switch expression syntax

## Debugging Tips

### Test Failures
```bash
# See detailed test output
./gradlew test --info

# Run one failing test
./gradlew test --tests "StrategyPatternTest.testMeleeAttack"
```

### Common Issues

**"UnsupportedOperationException: TODO X"**
- You haven't implemented that TODO yet

**"IllegalStateException: X is required"**
- Builder validation working correctly
- Ensure all fields are set before calling build()

**Damage calculation off by a few points**
- Use integer arithmetic: `(int) (value * 1.2)`
- Don't round - truncate with cast

**Undo doesn't restore exact state**
- Store actual changes, not intended changes
- Heal amount may hit max health cap

## Design Pattern Resources

### From Class
- Week 7 Slides: Strategy, Command, Template Method
- Week 8 Slides: Factory Method, Builder
- `examples/design-patterns/` - Working SOLID examples

## Submission Requirements

1. **All TODOs implemented** - Tests pass
2. **80%+ coverage** - Run jacocoTestReport
3. **AI documentation** - Where you used AI assistance
4. **Clean commit history** - Meaningful commit messages
5. **Team collaboration** - All members have commits

### Submit to Moodle
- Repository URL
- Team member names
- Coverage percentage achieved

## Extra Challenges (Optional)

Want to go further? Try these:

1. **Add a new character type** (demonstrates OCP)
   - Add to CharacterType enum
   - Implement factory method
   - No changes to existing code!

2. **Create combo attacks** (pattern composition)
   - Combine multiple attack strategies
   - Use Function composition

3. **Implement a MacroCommand**
   - Execute multiple commands as one
   - Undo all in reverse order

4. **Add state verification to template method**
   - Check preconditions in beginTurn()
   - Validate postconditions in endTurn()

5. **Create a character loadout builder**
   - Builder for equipment, skills, etc.
   - Nested builders pattern

## Questions?

- Office hours: Wednesdays 1:30-3:00 PM
- Email: kkousen@trincoll.edu
- Post on course discussion board

---

**Remember**: Design patterns are about **solving recurring problems** with proven solutions. Don't just make tests pass - understand **why** each pattern is used and **when** you'd choose one over another.

Good luck! 🎮⚔️
