package edu.trincoll.game.template;

import edu.trincoll.game.model.Character;

/**
 * Template Method pattern for battle sequences.
 * Defines the skeleton of a battle turn, with steps that can be customized.
 *
 * The template method (executeTurn) defines the sequence:
 * 1. Begin turn
 * 2. Perform pre-attack actions
 * 3. Execute attack
 * 4. Perform post-attack actions
 * 5. End turn
 *
 * Subclasses can override hook methods to customize behavior.
 */
public abstract class BattleSequence {
    protected final Character attacker;
    protected final Character defender;

    public BattleSequence(Character attacker, Character defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    /**
     * Template Method pattern for battle turn sequences.
     * Defines turn structure: begin → prepare → attack → follow-up → end.
     *
     * <p>Subclasses customize specific steps without changing the sequence.</p>
     *
     * @author Chris Burns
     */
    public final void executeTurn() {
        beginTurn();
        preAttackAction();
        performAttack();
        postAttackAction();
        endTurn();
    }

    /**
     * Hook method - called at the start of turn.
     * Default implementation does nothing.
     * Subclasses can override to add behavior.
     */
    protected void beginTurn() {
        // Default: do nothing
    }

    /**
     * Hook method - called before attack.
     * Default implementation does nothing.
     */
    protected void preAttackAction() {
        // Default: do nothing
    }

    /**
     * Abstract method - must be implemented by subclasses.
     * This is where the actual attack logic goes.
     */
    protected abstract void performAttack();

    /**
     * Hook method - called after attack.
     * Default implementation does nothing.
     */
    protected void postAttackAction() {
        // Default: do nothing
    }

    /**
     * Hook method - called at the end of turn.
     * Default implementation does nothing.
     */
    protected void endTurn() {
        // Default: do nothing
    }
}