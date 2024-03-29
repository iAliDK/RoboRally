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
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Wall;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.controller.AppController.gameName;
import static dk.dtu.compute.se.pisd.roborally.fileaccess.SaveBoard.saveBoardAPI;

/**
 * This is the game controller class for the game. It is responsible for
 * the game logic.
 */

public class GameController {


    final public Board board;

    /**
     * Constructor for the game controller. It needs a board to operate on. The board is the model of the game.
     * @param board the board to operate on.
     */

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * @author Daniel, Ismail and Zainab.
     * Changes the current player to the next one.
     * If the current player is the last player, the first player is chosen next.
     * @author Zainab
     */
    public boolean nextTurnAndIsLastPlayer() {
        int currentPlayerIndex = board.getPlayerNumber(board.getCurrentPlayer());
        board.setCurrentPlayer(board.getPlayer((currentPlayerIndex + 1) % board.getPlayersNumber()));

        int lastPlayer = board.getPlayersNumber()-1;
        return currentPlayerIndex == lastPlayer;
    }

    /**
     * It sets the phase to programming, the current player to the first player and the step to 0
     * It also sets the cards in the program field to null and the cards in the card field to random cards
     * It is used in executeCommandOptionAndContinue and executeNextStep.
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

    /**
     * This method generates a random command card
     * It is used in executeCommandOptionAndContinue and executeNextStep.
     */
    public CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * @author Zainab
     * This method checks if the player have chosen all their cards
     * @return true if all cards have been chosen, false if not
     */
    public boolean allCardsChosen() {
        Player player = board.getCurrentPlayer();

        for (CommandCardField command : player.getProgram()) {
            if (command.getCard() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @author Zainab
     * This method calls nextTurnAndIsLastPlayer and makes the program fields invisible
     * This method calls allCardsChosen
     * If all cards have been chosen, it sets the phase to activation and the current player to the first player
     */
    public void finishProgrammingPhase() {
        if (!allCardsChosen()) {
            return;
        }

        if (nextTurnAndIsLastPlayer()) {
            //makeProgramFieldsInvisible();
            makeProgramFieldsVisible(0);

            board.setPhase(Phase.ACTIVATION);
            board.setCurrentPlayer(board.getPlayer(0));
            board.setStep(0);
        }
    }


    /**
     * This method is used to make the program fields visible
     * It is used in finishProgrammingPhase
     * It is used in executeCommandOptionAndContinue and executeNextStep
     * @param register the register to which the program fields should be made visible to
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * This method executes all steps of the current player.
     */
    public void executePrograms()  {
        board.setStepMode(false);
        continuePrograms();
    }


    /**
     * This method executes the next step of the current player.
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * @author Zainab
     * This method is used to continue the programs of the current player
     * It is used in executePrograms and executeStep
     */
    private void continuePrograms() {
        Player currentPlayer = board.getCurrentPlayer();
        int step = board.getStep();
        do {
            updateProgramFieldVisibility(currentPlayer, step);
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    /**
     * @author Zainab
     * This method updates the program field visibility
     * @param player
     * @param step
     */
    private void updateProgramFieldVisibility(Player player, int step) {
        CommandCardField programField = player.getProgramField(step);
        programField.setVisible(false);
    }

    /**
     * @author Qiao and Zainab.
     * This method executes the next step of the current player
     * and changes the phase to PLAYER_INTERACTION if the command
     * is interactive.
     * <p>
     * This method is used in continuePrograms
     */
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
            }
                    try {
                        saveBoardAPI(board, gameName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * @param player
     * @param command
     * @author Daniel, Ismail and Zainab.
     * This method is used in executeNextStep
     * This method executes the given command for the given player.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player.board == board && command != null) {
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
            }
        }
    }

    /**
     * @param player
     * @author Zainab.
     * This method moves the given player one step forward.
     * It also checks if the space is a wall.
     * This method is used in executeCommand
     */
    public void moveForward(Player player) {
        // Get the current player and the space they are currently standing on
        Player currentPlayer = board.getCurrentPlayer();
        Space currentSpace = currentPlayer.getSpace();

        // Get the heading of the current space's wall (if any)
        Heading currentWallHeading = getWallHeading(currentSpace);

        // Get the next space the player wants to move to
        Space nextSpace = getNextSpace(currentPlayer);

        // Get the heading of the next space's wall (if any)
        Heading nextWallHeading = getWallHeading(nextSpace);

        Heading playerHeading = player.getHeading();

        // Check if there already is a robot in the space they want to move to
        if (nextSpace.getPlayer() != null) {
            return;
        }
        // Check if there are walls in the current space
        if (currentWallHeading != null && currentWallHeading == playerHeading){
            return;
        }
        // Check if there is a wall in the space they want to move to
        if (nextWallHeading != null && nextWallHeading == playerHeading.getOpposite()) {
        } else {
            // No walls in both spaces, move the player forward
            player.setSpace(nextSpace, true);
        }
    }

    /**
     * @param player
     * @author Daniel, Ismail and Zainab.
     * This method moves the given player two steps forward.
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

    /**
     * @param player
     * @author Daniel, Ismail and Zainab.
     * This method turns the given player to the right.
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    /**
     * @param player
     * @author Daniel, Ismail and Zainab.
     * This method turns the given player to the left.
     * */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * @param player
     * @author Zainab.
     * This method makes a U-turn for the given player.
     * */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev().prev());
    }

    /**
     * @param player
     * @author Zainab.
     * This method moves the given player one step backwards without changing the heading.
     */
    public void backUp(@NotNull Player player) {
            // Get the current player and the space they are currently standing on
            Player currentPlayer = board.getCurrentPlayer();
            Space currentSpace = currentPlayer.getSpace();

            // Get the heading of the current space's wall (if any)
            Heading currentWallHeading = getWallHeading(currentSpace);

            // Get the previous space the player wants to move to (backup space)
            Space backupSpace = getBackupSpace(currentPlayer);

            // Get the heading of the backup space's wall (if any)
            Heading backupWallHeading = getWallHeading(backupSpace);

            Heading playerHeading = player.getHeading();

        // Check if there already is a robot in the space they want to move to
            if (backupSpace.getPlayer() != null) {
                return;
        }
            // Check if there are walls current space
            if (currentWallHeading != null && currentWallHeading != playerHeading) {
                return;
            }
            // Check if the backup space has a wall
            if (backupWallHeading != null && backupWallHeading != playerHeading.getOpposite()) {
            } else {
                // No walls in both spaces, move the player to the backup space
                player.setSpace(backupSpace, true);
            }
        }

    /**
     * @author Zainab.
     * This method gets the space behind the player.
     * @param player
     * @return
     */
    private Space getBackupSpace(Player player) {
    Space backUpSpace = board.getSpaceBehind(player.getSpace(), player.getHeading());
    return backUpSpace;
    }

    /**
     *  this method gets the heading of the wall in the space.
     * @author Zainab.
     * @param space
     * @return
     */
    private Heading getWallHeading(Space space) {
        FieldAction fieldAction = space.getFieldAction();
        if (fieldAction instanceof Wall wall) {
            return wall.getHeading();
        }
        return null;
    }

    /**
     * @author Zainab.
     * This method gets the next space in the direction of the player.
     * @param player
     * @return
     */
    private Space getNextSpace(Player player) {
        Space nextSpace = board.getNeighbour(player.getSpace(), player.getHeading());
        return nextSpace;
    }

    /**
     * This method checks if a card can be moved from the source field to the target field.
     * If it can, the card is moved and the method returns true.
     * If it can't, the method returns false.
     *
     * @param source The field to move the card from
     * @param target The field to move the card to
     * @return true if the card was moved, false otherwise
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
     * @param command The command to execute for the current player
     * @author Qiao and Zainab.
     * This method executes the chosen option for the current player
     * and continues with the next step
     * (or the next player if the current player has no more steps)
     * or starts the programming phase if all players have finished their steps.
     */
    public void executeCommandOptionAndContinue(Command command) {
        Phase phase = board.getPhase();
        Player currentPlayer = board.getCurrentPlayer();

        // Setting the phase to ACTIVATION when and INTERACTIVE card is put
        if (phase == Phase.PLAYER_INTERACTION && currentPlayer != null) {
            board.setPhase(Phase.ACTIVATION);

            // execute selected option for current player
            switch (command) {
                case LEFT -> executeCommand(currentPlayer, Command.LEFT);
                case RIGHT -> executeCommand(currentPlayer, Command.RIGHT);
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
                if (board.getPhase() == Phase.ACTIVATION && !board.isStepMode()) {
                    updateProgramFieldVisibility(currentPlayer, step);
                }
            }
        }
    }
}
