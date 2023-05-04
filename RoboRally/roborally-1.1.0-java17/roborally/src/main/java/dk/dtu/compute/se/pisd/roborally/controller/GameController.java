/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.PlayerView;
import org.jetbrains.annotations.NotNull;

/**
 * This is the controller class for the game. It is responsible for
 * the game logic.
 */

public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    public void nextTurn() {
        int currenPlayerIndex = board.getPlayerNumber(board.getCurrentPlayer());
        board.setCurrentPlayer(board.getPlayer((currenPlayerIndex + 1) % board.getPlayersNumber()));
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

        // Checks whether the space is free
        if (space.getPlayer() != null) {
            return;
        }
        // Moves player to chosen space
        board.getCurrentPlayer().setSpace(space);
    }
    // XXX: V2


    /**
     * It sets the phase to programming, the current player to the first player and the step to 0
     * It also sets the cards in the program field to null and the cards in the card field to random cards
     * It is used in executeCommandOptionAndContinue and executeNextStep
     */

    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2

    /**
     * This method stops the programming phase and activates the activation phase
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }


    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }


    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * This method executes all steps of the current player
     */
    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * This method executes the next step of the current player
     */
    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }


    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * This method executes the next step of the current player
     * and changes the phase to PLAYER_INTERACTION if the command
     * is interactive
     *
     * This method is used in continuePrograms
     *
     */
    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                            return;
                    }

                    executeCommand(currentPlayer, command);
                }

                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * This method executes the given command for the given player
     *
     * This method is used in executeNextStep
     *
     * @param player
     * @param command
     */
    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case FAST_FAST_FORWARD:
                    this.fastFastForward(player);
                    break;
                case U_TURN:
                this.uTurn(player);
                    break;
                case BACK_UP:
                    this.backUp(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }


    // TODO Assignment V2
    /**
     * This method moves the player one space forward
     *
     * @param player
     */
    public void moveForward(@NotNull Player player) {
        Space newSpace = board.getNeighbour(player.getSpace(), player.getHeading());

        //check if sapce is wall
            if (newSpace.getPlayer() == null && newSpace.getIsWall() == false) {
                player.getSpace().setPlayer(null);
                player.setSpace(newSpace);
                newSpace.setPlayer(player);

        }
    }

    // TODO Assignment V2
    /**
     * This method moves the player two spaces forward
     *
     * @param player
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * This method moves the player three spaces forward
     *
     * @param player
     */
    public void fastFastForward(@NotNull Player player) {
        fastForward(player);
        moveForward(player);
    }


    // TODO Assignment V2
    /**
     * This method turns the player to the right
     *
     * @param player
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
        }



    // TODO Assignment V2
    /**
     * This method turns the player to the left
     *
     * @param player
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * This method turns the player around
     * (makes a u-turn)
     *
     * @param player
     */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev().prev());
    }

    /**
     * This method moves the player one space backwards
     * without changing the heading
     *
     * @param player
     */
    public void backUp(@NotNull Player player) {
        Space newSpace = board.getSpaceBehind(player.getSpace(), player.getHeading());

        if (newSpace.getPlayer() == null) {
            player.getSpace().setPlayer(null);
            player.setSpace(newSpace);
            newSpace.setPlayer(player);

        }
    }


    /**
     * This method checks if a card can be moved from the source field to the target field.
     * If it can, the card is moved and the method returns true.
     * If it can't, the method returns false.
     *
     * @param source
     * @param target
     * @return
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method executes the chosen option for the current player
     * and continues with the next step
     * (or the next player if the current player has no more steps)
     * or starts the programming phase if all players have finished their steps.
     * @param command
     */
    public void executeCommandOptionAndContinue(Command command) {
        Phase phase = board.getPhase();
        Player currentPlayer = board.getCurrentPlayer();

        // Setting the phase to ACTIVATION when and INTERACTIVE card is put
        if (phase == Phase.PLAYER_INTERACTION && currentPlayer != null) {
            board.setPhase(Phase.ACTIVATION);

            // We have made a switch cases?
            switch (command) {
                case LEFT:
                    executeCommand(currentPlayer, Command.LEFT);
                    break;

                case RIGHT:
                    executeCommand(currentPlayer, Command.RIGHT);
                    break;
                // execute selected option for current player
            }
            int step = board.getStep();
            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                step++;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                    executeNextStep();
                } else {
                    startProgrammingPhase();
                }
            }

            CommandCard card = currentPlayer.getProgramField(step).getCard();
            if (card != null) {
                continuePrograms();
            }

            /*
            if (phase == Phase.ACTIVATION && currentPlayer != null) {
                executeNextStep();
                board.setPhase(Phase.PROGRAMMING);

                //executeNextStep(); // move to next program card
            }*/
        }
    }


    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }
}
