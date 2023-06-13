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

import dk.dtu.compute.se.pisd.roborally.fileaccess.API.Repository;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Gear;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.Wall;
import org.jetbrains.annotations.NotNull;



/**
 * This is the game controller class for the game. It is responsible for
 * the game logic.
 */

public class GameController {
    public class ImpossibleMoveException extends Exception {

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

    Repository api = new Repository();

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
     */
    public boolean nextTurnAndIsLastPlayer() {
        int currentPlayerIndex = board.getPlayerNumber(board.getCurrentPlayer());
        board.setCurrentPlayer(board.getPlayer((currentPlayerIndex + 1) % board.getPlayersNumber()));

        int lastPlayer = board.getPlayersNumber()-1;
        if(currentPlayerIndex == lastPlayer){
            return true;
        }
        return false;
    }
/*
        // returner true når det er sidste spiller
        if (nextPlayerIndex == 0) {
            // Last player reached, loop back to the first player
            return true;
        } else {
            board.setCurrentPlayer(board.getPlayer(nextPlayerIndex));
        }
        return false;

 */



/*
    public void nextTurn() {
        int currentPlayerIndex = board.getPlayerNumber(board.getCurrentPlayer());
        int nextPlayerIndex = (currentPlayerIndex + 1) % board.getPlayersNumber();

        if (nextPlayerIndex == 0) {
            // Last player reached, loop back to the first player
            board.setCurrentPlayer(board.getPlayer(board.getPlayersNumber()));
        } else {
            board.setCurrentPlayer(board.getPlayer(nextPlayerIndex));
        }
    }

 */


    /**
     * @param space the space to which the current player should move
     * @author Daniel, Ismail and Zainab.
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     */

    //Musseclick
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        if (space.board == board) {

            Player currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space, true);
                int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
                board.setCurrentPlayer(board.getPlayer(playerNumber));
                if(space.getFieldAction() != null) {
                    if (space.getFieldAction().getClass().equals(ConveyorBelt.class)) {
//                        activateFieldAction();
                    }
                }
            }
        }
    }

    public void loadBoard(){
        BoardTemplate template = api.loadBoard();
        board.setPlayers(template.players);
    }



    // TODO Assignment V1: method should be implemented by the students:
    //   - the current player should be moved to the given space
    //     (if it is free()
    //   - and the current player should be set to the player
    //     following the current player
    //   - the counter of moves in the game should be increased by one
    //     if the player is moved

        /*
        // Checks whether the space is free
        if (space.getPlayer() != null) {
            return;
        }
        // Moves player to chosen space
        board.getCurrentPlayer().setSpace(space);

         */


    // XXX: V2


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

    // XXX: V2

    /**
     * This method generates a random command card
     * It is used in executeCommandOptionAndContinue and executeNextStep.
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2


    /**
     * This method checks if the player have chosen all their cards
     * @return true if all cards have been chosen, false if not
     */
    // Lav en metode der tjekker om alle spillere har valgt alle deres kort
    public boolean allCardsChosen() {
        Player player = board.getCurrentPlayer();

        for (CommandCardField command : player.getProgram()) {
            if (command.getCard() == null) {
                return false;
            }
        }
        return true; // All cards have been chosen
    }

    /**
     * This method calls nextTurnAndIsLastPlayer and makes the program fields invisible
     * This method calls allCardsChosen
     * If all cards have been chosen, it sets the phase to activation and the current player to the first player
     */
    public void finishProgrammingPhase() {
        if (!allCardsChosen()) {
            return;
        }


        if (nextTurnAndIsLastPlayer()) {
            makeProgramFieldsInvisible();
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


    /**
     * This method is used to make the program fields invisible
     * It is used in finishProgrammingPhase.
     */
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
     * This method executes all steps of the current player.
     */
    public void executePrograms() {
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


    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());

    }

    /**
     * @author Qiao and Zainab.
     * This method executes the next step of the current player
     * and changes the phase to PLAYER_INTERACTION if the command
     * is interactive.
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
                    executeCommand(currentPlayer, command);
                }

                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    //activateFieldAction();
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
     * @param player This method moves the given player one step forward.
     * @param command This method executes the given command for the given player.
     * @author Daniel, Ismail and Zainab.
     * <p>
     * This method is used in executeNextStep
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
                // DO NOTHING (for now)
            }
        }
    }

    /**
     * @param player This method moves the given player two steps forward. It also checks if the space is a wall. If it is, the player is moved one step forward instead.
     * @author Daniel, Ismail and Zainab.
     *
     * <p>
     *     This method is used in executeCommand
     */
    public void moveForward(@NotNull Player player) {
        Space newSpace = board.getNeighbour(player.getSpace(), player.getHeading());

        player.setSpace(newSpace, true);

        //check if space is wall
        //If there isn't a player on the new space AND (if the space is not a wall OR the player is not facing the wall
        /*if ((!player.getSpace().getClass().equals(Wall.class)|| player.getHeading() != player.getSpace().getHeading()) && (!newSpace.getClass().equals(Wall.class) || player.getHeading() != newSpace.getHeading().getOpposite())) {
            player.setSpace(newSpace);

            //newSpace.setPlayer(player, this, true);
        } */
    }
    /**
     * @param player This method moves the given player two steps forward.
     * @author Daniel, Ismail and Zainab.
     *
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * @param player This method moves the given player three steps forward.
     * @author Zainab.
     * This method moves the player three spaces forward
     */
    public void fastFastForward(@NotNull Player player) {
        fastForward(player);
        moveForward(player);
    }

    /**
     * @param player This method turns the given player to the right.
     * @author Daniel, Ismail and Zainab.
     *
     */
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    /**
     * @param player This method turns the given player to the left.
     * @author Daniel, Ismail and Zainab.
     *
     * */
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**
     * @param player This method makes a U-turn for the given player.
     * @author Zainab.
     *
     * */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev().prev());
    }

    /**
     * @param player This method moves the given player one step backwards without changing the heading.
     * @author Zainab.
     *
     * without changing the heading
     */
    public void backUp(@NotNull Player player) {
        Space newSpace = board.getSpaceBehind(player.getSpace(), player.getHeading());


        player.setSpace(newSpace, true);

        /*
        //check if space is wall
        if (newSpace.getPlayer() == null && ((!player.getSpace().getClass().equals(Wall.class)|| player.getHeading() == player.getSpace().getHeading())) && (!newSpace.getClass().equals(Wall.class)|| player.getHeading() != newSpace.getHeading())) {
            player.getSpace().setPlayer(null, this, true);
            player.setSpace(newSpace);
            newSpace.setPlayer(player, this, true);

        } */
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
     * @param player  The player to move to the given space
     * @param space   The space to move the player to
     * @param heading The heading the player should have after moving to the space
     * @throws ImpossibleMoveException if the player cannot be moved to the space with the given heading, or if the player cannot be moved to the space because there is another player on the space and the other player cannot be moved to the space behind it with the same heading. In this case, the player and the other player is not moved. The exception is thrown.
     */


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

            // We have made a switch cases?
            switch (command) {
                case LEFT -> executeCommand(currentPlayer, Command.LEFT);
                case RIGHT -> executeCommand(currentPlayer, Command.RIGHT);

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

   /*
    private void activateFieldAction() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player currentPlayer = board.getPlayer(i);
            Space currentSpace = currentPlayer.getSpace();
            for (FieldAction fiac : currentSpace.getActions()) {
                if (fiac instanceof ConveyorBelt) {
                    fiac.doAction(this, currentSpace);
                }
            }
        }
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player currentPlayer = board.getPlayer(i);
            Space currentSpace = currentPlayer.getSpace();
            for (FieldAction fiac : currentSpace.getActions()) {
                if (!(fiac instanceof Gear)) {
                    fiac.doAction(this, currentSpace);
                }
            }
        }
    }


    */

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }
}
