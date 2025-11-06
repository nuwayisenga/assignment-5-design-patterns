package edu.trincoll.game.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Manages execution and undo operations for game commands using the Command pattern.
 *
 * <p>The CommandInvoker acts as the central controller for all game actions, maintaining
 * a history of executed commands to enable undo functionality. Essential for implementing
 * turn reversal, replay systems, and save states in games.</p>
 *
 * <h2>Command Pattern Implementation</h2>
 * <ul>
 *   <li><b>Executes commands</b> without knowing their internal implementation</li>
 *   <li><b>Maintains history</b> using a stack (LIFO order)</li>
 *   <li><b>Supports undo</b> by popping and reversing the last command</li>
 *   <li><b>Decouples</b> action requests from action performers</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * CommandInvoker invoker = new CommandInvoker();
 *
 * // Execute actions
 * invoker.executeCommand(new AttackCommand(warrior, enemy));
 * invoker.executeCommand(new HealCommand(warrior, 20));
 *
 * // Undo last action
 * invoker.undoLastCommand(); // Heal is reversed
 * }</pre>
 *
 * @author Chris Burns
 * @author Gabriela Scavenius
 * @author Noella Uwayisenga
 * @see GameCommand
 */
public class CommandInvoker {
    private final Stack<GameCommand> commandHistory = new Stack<>();

    /**
     * Executes a command and adds it to history for potential undo.
     *
     * <p>If command execution throws an exception, the command will NOT be
     * added to history, preventing undo of failed actions.</p>
     *
     * @param command the command to execute (must not be null)
     * @throws NullPointerException if command is null
     * @see GameCommand#execute()
     */
    public void executeCommand(GameCommand command) {
        // Execute the command
        command.execute();

        // Add the command to history
        commandHistory.push(command);
    }

    /**
     * Undoes the most recently executed command and removes it from history.
     *
     * <p>Safely handles empty history by returning without error. Command is
     * removed from history whether undo succeeds or fails.</p>
     *
     * <p><b>Example:</b> If history contains [Attack, Heal], this undoes Heal
     * and leaves [Attack] in history for next undo.</p>
     *
     * @see GameCommand#undo()
     */
    public void undoLastCommand() {
        // Check if history is empty - if so, return
        if (commandHistory.isEmpty()) {
            return;
        }

        // Pop the last command from history
        GameCommand command = commandHistory.pop();

        // Call undo() on that command
        command.undo();
    }

    /**
     * Get the command history (for testing and logging).
     */
    public List<GameCommand> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

    /**
     * Clear all command history.
     */
    public void clearHistory() {
        commandHistory.clear();
    }

    /**
     * Check if there are commands to undo.
     */
    public boolean hasCommandsToUndo() {
        return !commandHistory.isEmpty();
    }
}