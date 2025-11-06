# AI Collaboration Report
## Assignment 5: Design Patterns - Game Character System

**Team Members:** Noella Uwayisenga, Gabriela Scavenius, Chris Burns  
**AI Tool Used:** Claude (Anthropic)  
**Date:** November 5, 2025

---

## Executive Summary

Our team used Claude AI as a learning companion and debugging assistant for this assignment. We maintained a collaborative approach where AI helped clarify concepts and validate our implementations, but all design decisions and final code were team-driven. This report details our strategic use of AI while demonstrating our deep understanding of design patterns.

---

## What AI Helped With

### 1. **Clarifying the Heavy Armor Defense Cap Logic (TODO 1e)**
**Context:** The 75% damage reduction cap in HeavyArmorDefenseStrategy was conceptually tricky.

**AI Assistance:** We asked Claude to explain the mathematical relationship between:
- Maximum damage reduction (75%)
- Minimum damage that must get through (25%)
- How to implement this with Math.max()

**Our Learning:** Understanding that `Math.max(incomingDamage * 0.25, actualDamage)` ensures that even with extremely high defense, at least 25% of incoming damage always penetrates was crucial. This prevents game-breaking invulnerability.

### 2. **Understanding Critical Hit Calculation in Ranged Attack (TODO 1c)**
**Context:** The critical hit mechanic needed to check if target health was below 30% threshold.

**AI Assistance:** We discussed with Claude the proper way to calculate percentage thresholds:
```java
// Our initial attempt (wrong):
if (targetHealth < 30) // This checks absolute value, not percentage

// AI suggested clarification led us to:
if (targetHealth < targetMaxHealth * 0.3) // Correct percentage check
```

**Our Learning:** Learned the distinction between absolute values and percentage-based calculations, and why percentage thresholds scale better across different character types.

### 3. **Builder Pattern Validation Strategy (TODO 3)**
**Context:** Understanding when and how to throw exceptions in the Builder pattern.

**AI Assistance:** Claude explained that:
- Each required field should have a specific error message
- IllegalStateException is appropriate for builder validation
- Validation should happen in build(), not in individual setters

**Our Improvement:** We went beyond AI's suggestion by ensuring our error messages were very specific (e.g., "name is required" vs "attackStrategy is required") to make debugging easier.

### 4. **Command Pattern Undo Complexity (TODO 4b)**
**Context:** HealCommand undo was tricky because healing caps at max health.

**AI Assistance:** We asked Claude why we need to track `actualHealingDone` instead of just using the `amount` parameter. AI explained:
- If a character has 90/100 HP and you heal for 20
- Actual healing done is only 10 (capped at max health)
- Undo must only subtract 10, not 20

**Our Learning:** Commands must store actual state changes, not intended changes. This was a key insight about the Command pattern's undo mechanism.

### 5. **Template Method's Final Keyword (TODO 5a)**
**Context:** Understanding why executeTurn() must be marked final.

**AI Assistance:** Claude explained that making the template method final prevents subclasses from changing the algorithm's structure, which is the core principle of the Template Method pattern. Subclasses should only customize specific steps (hooks), not the sequence itself.

**Our Learning:** The final keyword is not just a technicality—it's a design constraint that enforces the pattern's intent.

---

## What We Had to Modify/Fix

### 1. **Integer Division and Casting**
**AI Initial Suggestion:** Used floating-point arithmetic for damage calculations.

**Our Fix:** Recognized that game mechanics require integer arithmetic to avoid precision issues. Changed all calculations to use explicit casting:
```java
// AI suggested:
return baseDamage * 1.2; // Returns double

// We implemented:
return (int) (baseDamage * 1.2); // Returns int, truncates
```

**Why This Matters:** Games need deterministic, reproducible calculations. Floating-point precision issues could cause different damage values in identical scenarios.

### 2. **Defense Strategy Return Values**
**AI Oversight:** Initial suggestions didn't emphasize the importance of preventing negative damage.

**Our Addition:** We ensured all defense strategies use `Math.max(0, actualDamage)` to guarantee non-negative values. This prevents bizarre scenarios like healing from attacks.

### 3. **Mana Consumption Timing in MagicAttackStrategy**
**Initial Confusion:** Uncertain whether to consume mana before or after calculating damage.

**Our Decision:** We consume mana after calculating the mana bonus but before returning damage. This ensures:
- Bonus is based on current mana
- Mana is consumed for the attack
- If not enough mana, exception prevents attack entirely

### 4. **Power Attack Recoil Implementation**
**AI Suggestion:** Use takeDamage() for recoil in PowerAttackSequence.

**Our Fix:** Changed to setHealth() because takeDamage() applies defense reduction, which doesn't make sense for self-inflicted recoil damage. The attacker should take the full 10% recoil, not a reduced amount.

---

## Key Concepts Learned

### 1. Strategy Pattern: Runtime Algorithm Selection
**Core Principle:** Encapsulate algorithms in separate classes that implement a common interface.

**Real-World Application:** In our game system, a Warrior can dynamically change from melee to ranged combat without changing its character class. This demonstrates the Open-Closed Principle—we can add new attack strategies without modifying existing character code.

**Specific Example:**
```java
Character warrior = CharacterFactory.createWarrior("Conan");
// Starts with MeleeAttackStrategy
warrior.setAttackStrategy(new RangedAttackStrategy()); 
// Now attacks at range - no character modification needed!
```

**Why It Matters:** In real games, characters might pick up new weapons, learn new skills, or be affected by status effects that change their combat behavior. The Strategy pattern makes this flexible and maintainable.

### 2. Factory Method Pattern: Centralized Object Creation
**Core Principle:** Encapsulate object creation logic to ensure consistency and reduce duplication.

**Real-World Application:** Instead of repeating builder code every time we need a Warrior, the factory guarantees all Warriors have correct stats, strategies, and configuration.

**Specific Example:**
```java
// Without factory (error-prone, repeated everywhere):
Character warrior = Character.builder()
    .name("Hero")
    .type(CharacterType.WARRIOR)
    .stats(CharacterStats.create(150, 40, 30, 0))
    .attackStrategy(new MeleeAttackStrategy())
    .defenseStrategy(new HeavyArmorDefenseStrategy())
    .build();

// With factory (consistent, maintainable):
Character warrior = CharacterFactory.createWarrior("Hero");
```

**Why It Matters:** As games evolve, character balance changes. With a factory, we update stats in ONE place. Without it, we'd need to find and update every place warriors are created.

### 3. Builder Pattern: Complex Object Construction
**Core Principle:** Separate construction from representation, building objects step-by-step with validation.

**Real-World Application:** Characters have many required fields. The Builder ensures we can't accidentally create invalid characters (e.g., a character with no name or missing strategies).

**Specific Example:**
```java
// This will throw IllegalStateException at build() time:
Character invalid = Character.builder()
    .name("Test")
    .type(CharacterType.MAGE)
    // Missing stats, attackStrategy, defenseStrategy
    .build(); // Throws: "stats is required"
```

**Why It Matters:** Failing fast during construction prevents bugs that would only appear later during gameplay. Better to crash during development than ship a game with invisible characters!

### 4. Command Pattern: Undoable Actions and History
**Core Principle:** Encapsulate actions as objects with execute() and undo() methods.

**Real-World Application:** Enables undo/redo, macro commands (combo attacks), action replay, and save states.

**Specific Example:**
```java
CommandInvoker invoker = new CommandInvoker();

// Execute attacks
invoker.executeCommand(new AttackCommand(warrior, enemy));
invoker.executeCommand(new AttackCommand(mage, enemy));

// Undo last action
invoker.undoLastCommand(); // Mage's attack is reversed
```

**Why It Matters:** Modern games need undo for turn-based strategy, replay systems for analysis, and save states. The Command pattern provides the foundation for all of these features.

### 5. Template Method Pattern: Customizable Algorithm Structure
**Core Principle:** Define the skeleton of an algorithm in a base class, letting subclasses override specific steps.

**Real-World Application:** All battle sequences follow the same flow (begin → prepare → attack → follow-up → end), but different attack types customize specific steps.

**Specific Example:**
```java
// Standard attack: just attacks
StandardBattleSequence standard = new StandardBattleSequence(attacker, defender);
standard.executeTurn(); // Simple attack

// Power attack: charges up, attacks with bonus, takes recoil
PowerAttackSequence power = new PowerAttackSequence(attacker, defender);
power.executeTurn(); // Same sequence, but preAttack/postAttack customized
```

**Why It Matters:** Ensures consistency in combat flow while allowing variety. All attacks follow the same timing and event structure, which is crucial for animations, sound effects, and game feel.

---

## How Patterns Work Together

### Pattern Synergy in Our System

1. **Factory + Strategy + Builder:**
    - Factory uses Builder to create characters
    - Factory assigns appropriate Strategies based on character type
    - Result: Consistent character creation with correct behaviors

2. **Command + Strategy:**
    - Commands execute actions that use character Strategies
    - AttackCommand calls `attacker.attack()`, which delegates to AttackStrategy
    - Result: Undoable actions with flexible combat behaviors

3. **Template Method + Command:**
    - Template Method orchestrates combat sequences
    - Each step can execute Commands
    - Result: Structured battle flow with reversible actions

### Real-World Example: Complete Combat Turn
```java
// Factory creates characters with appropriate strategies
Character warrior = CharacterFactory.createWarrior("Conan");
Character mage = CharacterFactory.createMage("Gandalf");

// Command pattern for undoable actions
CommandInvoker invoker = new CommandInvoker();

// Template Method orchestrates the attack sequence
BattleSequence battle = new PowerAttackSequence(warrior, mage);
battle.executeTurn(); // Uses warrior's strategy, structured sequence

// Can undo if needed
invoker.undoLastCommand();
```

This demonstrates how all five patterns collaborate to create a flexible, maintainable combat system.

---

## Team Contributions

### Division of Labor

**Noella Uwayisenga:**
- Implemented Strategy Pattern TODOs (1a-1e)
- Researched and implemented damage calculation formulas
- Created test scenarios for critical hits and defense caps
- Led discussion on integer arithmetic vs floating-point

**Gabriela Scavenius:**
- Implemented Factory Method Pattern (2a-2e)
- Implemented Builder Pattern validation (TODO 3)
- Configured JaCoCo to exclude demo package from coverage
- Managed project submission and documentation compilation
- Ensured code quality and coverage requirements were met

**Chris Burns:**
- Implemented Command Pattern TODOs (4a-4c)
- Implemented Template Method Pattern (5a-5c)
- Debugged undo logic for heal commands
- Created additional test cases for edge scenarios
- Documented pattern interactions and synergies

### Collaboration Approach

All team members:
- Pair programmed on complex logic (critical hits, command undo)
- Reviewed each other's code before committing
- Participated in AI-assisted learning sessions
- Contributed to documentation and Javadoc

---

## Additional Enhancements Beyond Requirements

### 1. Comprehensive Javadoc Documentation
Added detailed Javadoc to all public methods explaining:
- Design pattern rationale
- Parameter constraints
- Return value semantics
- Usage examples
- Edge cases and error conditions

### 2. Enhanced Builder Validation
Beyond basic null checks, we ensured:
- Descriptive error messages for each field
- Clear indication of which field is missing
- Validation happens atomically in build()

### 3. Integer Arithmetic Consistency
Ensured all damage calculations use integer arithmetic:
- Explicit casting for all multiplications
- Consistent truncation behavior
- No floating-point precision issues

### 4. Coverage Configuration
Properly configured JaCoCo to:
- Exclude demo package (not part of graded work)
- Focus on actual pattern implementations
- Achieve 82% coverage (exceeding 80% requirement)

### 5. Java 21 Feature Usage
Leveraged modern Java features:
- Records for immutable data (CharacterStats)
- Switch expressions with exhaustive matching
- Functional interfaces for strategies
- Pattern matching in tests

---

## Reflection: What We'd Do Differently

### If We Started Over

1. **Start with Tests:** We implemented TODOs first, then ran tests. Next time, we'd read tests first to understand exact requirements.

2. **Document as We Go:** We added Javadoc at the end. Better to document while implementing, when decisions are fresh.

3. **More Pair Programming:** Solo implementation was faster, but pair programming on complex parts (critical hits, undo logic) caught bugs earlier.

### Key Takeaways

- **Design patterns solve real problems:** Each pattern addressed a specific game development challenge
- **Composition over inheritance:** Strategies are far better than having WarriorAttack, MageAttack subclasses
- **Test-driven validation:** Tests were our specification—they told us exactly what to implement
- **Modern Java features help:** Records, switch expressions, and functional interfaces made code cleaner
- **AI as a learning tool:** Claude helped clarify concepts but didn't replace our critical thinking

---

## Conclusion

This assignment demonstrated how design patterns work together to create flexible, maintainable systems. By strategically using AI as a learning companion while maintaining ownership of our design decisions, we achieved:

- ✅ All 17 TODOs implemented correctly
- ✅ 44/44 tests passing (100% success rate)
- ✅ 82% code coverage (exceeding 80% requirement)
- ✅ Clean, well-documented code using Java 21 features
- ✅ Deep understanding of how patterns collaborate

We're confident this work demonstrates not just completion of requirements, but mastery of design pattern principles and their real-world applications.

---

**Total Implementation Time:** ~8 hours  
**AI Consultation Time:** ~1 hour  
**Documentation Time:** ~1.5 hours

**Final Status:** ✅ Ready for Submission