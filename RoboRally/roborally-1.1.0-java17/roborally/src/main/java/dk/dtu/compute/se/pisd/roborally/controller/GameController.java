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
import dk.dtu.compute.se.pisd.roborally.model.boardElements.EnergySpace;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.CreateUpgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.ModularChassis;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.Upgrade;
import dk.dtu.compute.se.pisd.roborally.model.upgrade.UpgradeResponsibility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the controller class for the game. It is responsible for
 * the game logic.
 */

public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }
    // XXX: V2

    /**
     * @author Daniel, Ismail and Zainab.
     * Changes the current player to the next one.
     */
    public void nextTurn() {
        int currenPlayerIndex = board.getPlayerNumber(board.getCurrentPlayer());
        board.setCurrentPlayer(board.getPlayer((currenPlayerIndex + 1) % board.getPlayersNumber()));
    }

    /**
     * @param space the space to which the current player should move
     * @author Daniel, Ismail and Zainab.
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
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

    public void moveToSpace(@NotNull Space space, @NotNull Player player, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space;
        Player other = space.getPlayer();
        Space target = board.getNeighbour(space, heading);
        if (other != null) {
            if (target != null) {
                //dealPushDamage(player, other);
                for (Upgrade upg : player.getUpgrades()) {
                    if (upg.responsible(UpgradeResponsibility.MODULARCHASSIS) && other != null && !upg.isActivatedThisStep()) {
                        if (upg instanceof ModularChassis modularChassis) {
                            modularChassis.doAction(player, other);
                        }
                    }
                    if (upg.responsible(UpgradeResponsibility.PUSHLEFTORRIGHT) && other != null && !upg.isActivatedThisStep()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                }

                if (notWallsBlock(other.getSpace(), heading)) {
                    if (other.getSpace() != null) {
                        moveToSpace(target, other, heading);
                        assert space.getPlayer() == null : space;
                    } else {
                        throw new ImpossibleMoveException(player, space, heading);
                    }

                }
            }
        }

    }

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
                for //(int j = 0; j < Player.NO_CARDS; j++) {
                (CommandCardField cardField : player.getCards()) {
                    cardField.setCard(generateRandomCommandCard(player));
                    cardField.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard(Player player) {
        //Command[] commands = Command.values();
        int random; //= (int) (Math.random() * commands.length);
        List<Command> commands = new ArrayList<>(Arrays.asList(Command.FORWARD, Command.RIGHT, Command.LEFT, Command.FAST_FORWARD, Command.FAST_FAST_FORWARD, Command.U_TURN, Command.OPTION_LEFT_RIGHT));

        if (player != null) {
            if (!player.getDamageCards().isEmpty()) {
                for (Command damage : player.getDamageCards()) {
                    commands.add(damage);
                }
                //return new CommandCard(commands[random]);
            }
        }
        random = (int) (Math.random() * commands.size());
        Command card = commands.get(random);

        if (player != null) {
            if (player.getDamageCards().contains(card)) {
                player.getDamageCards().remove(card);
            }
        }
        return new CommandCard(card);
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
     * @author Qiao and Zainab.
     * This method executes the next step of the current player
     * and changes the phase to PLAYER_INTERACTION if the command
     * is interactive
     * <p>
     * This method is used in continuePrograms
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

                    executeCommand(currentPlayer, command, currentPlayer.getHeading());
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
     * @param player  the player who executes the command (must not be null) and must be the current player of the board
     * @param command the command to be executed (must not be null) and must not be interactive (i.e. isInteractive() must return false)
     * @author Daniel, Ismail and Zainab.
     * This method executes the given command for the given player
     * <p>
     * This method is used in executeNextStep
     */
    // XXX: V2
    public void executeCommand(@NotNull Player player, Command command, Heading heading) {
        if (player != null && player.board == board && command != null) {
            player.setFinalDestination(getDestination(player, heading, command));
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD -> this.moveForward(player);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case FAST_FORWARD -> this.fastForward(player);
                case FAST_FAST_FORWARD -> this.fastFastForward(player);
                case U_TURN -> this.uTurn(player);
                case BACK_UP -> this.backUp(player);
                default -> {
                }
                // DO NOTHING (for now)
            }
        }
    }


    // TODO Assignment V2

    /**
     * @param player the player to move one space forward (forward) (must not be null) and must be the current player of the board
     * @author Daniel, Ismail and Zainab.
     * This method moves the player one space forward
     */
    public void moveForward(@NotNull Player player) {
        Space newSpace = board.getNeighbour(player.getSpace(), player.getHeading());

        //check if space is wall
        if (newSpace.getPlayer() == null && newSpace.getIsWall() == false) {
            player.getSpace().setPlayer(null);
            player.setSpace(newSpace);
            newSpace.setPlayer(player);

        }
    }

    // TODO Assignment V2

    /**
     * @param player the player to move two spaces forward (fast forward) (must not be null) and must be the current player of the board
     * @author Daniel, Ismail and Zainab.
     * This method moves the player two spaces forward
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * @param player
     * @author Zainab.
     * This method moves the player three spaces forward
     */
    public void fastFastForward(@NotNull Player player) {
        fastForward(player);
        moveForward(player);
    }


    // TODO Assignment V2

    /**
     * @param player the player to turn to the right (must not be null) and must be the current player of the board
     * @author Daniel, Ismail and Zainab.
     * This method turns the player to the right
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }
    // TODO Assignment V2

    /**
     * @param player the player to turn to the left (must not be null) and must be the current player of the board
     * @author Daniel, Ismail and Zainab.
     * This method turns the player to the left
     */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * @param player the player to turn around (must not be null) and must be the current player of the board
     * @author Zainab.
     * This method turns the player around
     * (makes a U-turn)
     */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev().prev());
    }

    /**
     * @param player
     * @author Zainab.
     * This method moves the player one space backwards
     * without changing the heading
     */
    public void backUp(@NotNull Player player) {
        Space newSpace = board.getSpaceBehind(player.getSpace(), player.getHeading());
        /**
         * This method moves the player one space backwards
         * without changing the heading
         *
         * @param player the player to move one space backwards (back up) (must not be null) and must be the current player of the board
         */

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
     * @param source the source field (must not be null) and must be a field of the current player
     * @param target the target field (must not be null) and must be a field of the current player
     * @return true if the card was moved, false otherwise (i.e. if the card could not be moved)
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
     * @param option the command to be executed (must not be null) and must be interactive (i.e. isInteractive() must return true)
     * @author Qiao and Zainab.
     * This method executes the chosen option for the current player
     * and continues with the next step
     * (or the next player if the current player has no more steps)
     * or starts the programming phase if all players have finished their steps.
     */


    public void executeCommandOptionAndContinue(@NotNull Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null && Phase.PLAYER_INTERACTION == board.getPhase() && option != null) {
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer, option, currentPlayer.getHeading());
            nextPlayerOrPhase();
        }
        /*Phase phase = board.getPhase();
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
            }
            }*/
    }


    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    public boolean notWallsBlock(@NotNull Space space, Heading heading) {
        return (!isCurrentSpaceWallBlockingDirection(space, heading) && !isHeadingNeighbourWallBlockingDirection(space, heading));
    }

    public boolean isCurrentSpaceWallBlockingDirection(@NotNull Space space, Heading heading) {
        ArrayList<Heading> walls = (ArrayList<Heading>) space.getWalls();
        if (!walls.isEmpty()) {
            return walls.contains(heading);
        }
        return false;
    }

    public boolean isHeadingNeighbourWallBlockingDirection(@NotNull Space space, Heading heading) {
        Space neighbour = board.getNeighbour(space, heading);
        if (neighbour != null && !neighbour.getWalls().isEmpty()) {
            Heading oppositeHeading = heading.oppositeHeading();
            return neighbour.getWalls().contains(oppositeHeading);
        }
        return false;
    }

    public Space getDestination(@NotNull Player player, Heading heading, Command command) {
        Space move = board.getNeighbour(player.getSpace(), heading);
        Space moveX2 = board.getNeighbour(move, heading);
        Space moveX3 = board.getNeighbour(moveX2, heading);
        Space finalMove = player.getSpace();
        switch (command) {
            case FORWARD -> finalMove = move;
            case FAST_FORWARD -> finalMove = moveX2;
            case FAST_FAST_FORWARD -> finalMove = moveX3;
        }
        return finalMove;
    }

    public void directionMove(@NotNull Player player, Heading heading) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            Space target = current.board.getNeighbour(current, heading);
            if (target != null) {
                if (notWallsBlock(player.getSpace(), heading)) {
                    try {
                        moveToSpace(target, player, heading);
                    } catch (ImpossibleMoveException e) {
                    }
                }
            }
        }
    }

    public void findWinner(@NotNull Player player) {
        if (player.getLastCheckpointVisited() == board.getNumberOfCheckpoints()) {
            player.setPlayerWin(true);
            board.setPhase(Phase.GAME_WON);
        }
    }

    public void givePlayerRandomUpgrade(@NotNull Player player, EnergySpace energySpace) {
        player.addEnergy();
        energySpace.setEnergyAvailable(false);
        player.getSpace().playerChanged();

        boolean b = false;
        int r = 0;
        do {
            Upgrade upg = CreateUpgrade.getUpgrade(UpgradeResponsibility.getRandom());
            if (!player.getUpgrades().stream().anyMatch(upgrade -> upgrade.getName().equals(upgrade.getName())) && player.getUpgrades().size() != UpgradeResponsibility.values().length) {
                player.getUpgrades().add(upg);
                b = true;
            }
            if (player.getUpgrades().size() == UpgradeResponsibility.values().length) {
                b = true;
            }
        } while (false);
    }

    private void nextPlayerOrPhase() {
        setPlayersUpgradesNotActivated();
        Player currentPlayer = board.getCurrentPlayer();

        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        int step = board.getStep();
        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            continuePrograms();
        } else {
            //updateAllReboot();
            executeBoardElements();
            //updateAllReboot();
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
    }

    private void setPlayersUpgradesNotActivated() {
        for (Player player : board.getPlayers()) {
            for (Upgrade upgrade : player.getUpgrades()) {
                upgrade.setActivatedThisStep(false);
            }
        }
    }

    private void executeBoardElements() {
        if (board.getPlayers() != null) {
            for (Player player : board.getPlayers()) {
                if (player.getSpace() != null && !player.getSpace().getActions().isEmpty()) {
                    for (FieldAction fieldAction : player.getSpace().getActions()) {
                        fieldAction.doAction(this, player);
                    }
                }
            }
        }
    }

    /*public void updateAllReboot() {
        for (Player player : board.getPlayers()) {
            Space current = player.getSpace();
            if (current != null) {
                for (Integer i : board.getRebootBoarderXValues()) {
                    if (current.x > i && current.x < (i + 10)) {
                        for (Space rebootSpace : board.getRebootSpaceList()) {
                            if (rebootSpace.x > i && rebootSpace.x < (i + 10) && rebootSpace.getReboot().isStartField()) {
                                player.setRebootSpace(rebootSpace);
                            }
                        }
                    }
                }
            }
        }
    }*/
}

    /*class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }

    }*/
