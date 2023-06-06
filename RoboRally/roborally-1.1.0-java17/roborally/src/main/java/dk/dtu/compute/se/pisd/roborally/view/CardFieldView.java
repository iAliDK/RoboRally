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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 * The view of the board.
 * This class handles the drag detection on the card field view
 * and the drag and drop of the cards.
 * It also handles the highlighting of the card field view
 * when the player is active.
 * <p>
 * The card field view is a grid pane with a label for the player name
 * and a card view for the card.
 * The card field view is also a subject of the observer pattern
 * and notifies its observers when the card field view is clicked.
 * The observers are the game controller and the player view.
 * The game controller is notified when the card field view is clicked
 * and the player view is notified when the card field view is clicked
 * and the player is active.
 * The card field view is also a subject of the observer pattern
 * and notifies its observers when the card field view is clicked.
 */
public class CardFieldView extends GridPane implements ViewObserver {

    // This data format helps avoiding transfers of e.g. Strings from other
    // programs which can copy/paste Strings.
    final public static DataFormat ROBO_RALLY_CARD = new DataFormat("games/roborally/cards");

    final public static int CARDFIELD_WIDTH = 65;
    final public static int CARDFIELD_HEIGHT = 100;

    final public static Border BORDER = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));

    final public static Background BG_DEFAULT = new Background(new BackgroundFill(Color.WHITE, null, null));
    final public static Background BG_DRAG = new Background(new BackgroundFill(Color.GRAY, null, null));
    final public static Background BG_DROP = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));

    final public static Background BG_ACTIVE = new Background(new BackgroundFill(Color.YELLOW, null, null));
    final public static Background BG_DONE = new Background(new BackgroundFill(Color.GREENYELLOW, null, null));

    private final CommandCardField field;

    private final Label label;

    private final GameController gameController;
    private final Player player;

    /**
     * This class handles the drag detection on the card field view. It is
     * responsible for starting the drag and drop operation.
     * <p>
     * The drag detection is started when the mouse is pressed on the
     * card field view. The dragboard is set with the card field view
     * and the card field view is set to be the source of the dragboard.
     * The dragboard is also set with the data format of the card field view.
     * The dragboard is set with the card field view and the card field view
     * is set to be the source of the dragboard.
     *
     * @param gameController the game controller of the game view
     * @param field         the card field view of the card field view
     */
    public CardFieldView(@NotNull GameController gameController, @NotNull CommandCardField field, Player player) {
        this.gameController = gameController;
        this.field = field;
        this.player = player;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(5, 5, 5, 5));

        this.setBorder(BORDER);
        this.setBackground(BG_DEFAULT);

        this.setPrefWidth(CARDFIELD_WIDTH);
        this.setMinWidth(CARDFIELD_WIDTH);
        this.setMaxWidth(CARDFIELD_WIDTH);
        this.setPrefHeight(CARDFIELD_HEIGHT);
        this.setMinHeight(CARDFIELD_HEIGHT);
        this.setMaxHeight(CARDFIELD_HEIGHT);

        label = new Label("This is a slightly longer text");
        label.setWrapText(true);
        label.setMouseTransparent(true);
        this.add(label, 0, 0);

        this.setOnDragDetected(new OnDragDetectedHandler());
        this.setOnDragOver(new OnDragOverHandler());
        this.setOnDragEntered(new OnDragEnteredHandler());
        this.setOnDragExited(new OnDragExitedHandler());
        this.setOnDragDropped(new OnDragDroppedHandler());
        this.setOnDragDone(new OnDragDoneHandler());

        field.attach(this);
        update(field);
    }

    private String cardFieldRepresentation(CommandCardField cardField) {
        if (cardField.player != null) {

            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CommandCardField other = cardField.player.getProgramField(i);
                if (other == cardField) {
                    return "P," + i;
                }
            }

            for (int i = 0; i < player.getNumberOfCards(); i++) {
                CommandCardField other = cardField.player.getCards().get(i);
                if (other == cardField) {
                    return "C," + i;
                }
            }
        }
        return null;

    }

    private CommandCardField cardFieldFromRepresentation(String rep) {
        if (rep != null && field.player != null) {
            String[] strings = rep.split(",");
            if (strings.length == 2) {
                int i = Integer.parseInt(strings[1]);
                if ("P".equals(strings[0])) {
                    if (i < Player.NO_REGISTERS) {
                        return field.player.getProgramField(i);
                    }
                } else if ("C".equals(strings[0])) {
                    if (i < player.getNumberOfCards()) {
                        return field.player.getCards().get(i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method is called when the view is updated. It updates the
     * background color of the card field view according to the state of the
     * card field.
     * <p>
     * If the card field is visible, the background color is set to
     * {@link #BG_DEFAULT}. If the card field is active, the background
     * color is set to {@link #BG_ACTIVE}. If the card field is done,
     * the background color is set to {@link #BG_DONE}.
     * If the card field is not visible, the background color is set to
     * {@link #BG_DEFAULT}.
     *
     * @param subject the subject that called the update method (not used) {@inheritDoc}
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == field && subject != null) {
            CommandCard card = field.getCard();
            if (card != null && field.isVisible()) {
                label.setText(card.getName());
            } else {
                label.setText("");
            }
        }
    }

    private class OnDragDetectedHandler implements EventHandler<MouseEvent> {
        /**
         * This method is called when a drag is detected on the card field
         * view. It starts the drag and drop operation.
         * <p>
         * The dragboard is set with the card field view and the card field
         * view is set to be the source of the dragboard. The dragboard is
         * also set with the data format of the card field view.
         * The background color of the card field view is set to
         * {@link #BG_DRAG}.
         * The event is consumed.
         * The drag and drop operation is started.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(MouseEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView source) {
                CommandCardField cardField = source.field;
                if (cardField.getCard() != null && cardField.player != null && cardField.player.board.getPhase().equals(Phase.PROGRAMMING)) {
                    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                    Image image = source.snapshot(null, null);
                    db.setDragView(image);

                    ClipboardContent content = new ClipboardContent();
                    content.put(ROBO_RALLY_CARD, cardFieldRepresentation(cardField));

                    db.setContent(content);
                    source.setBackground(BG_DRAG);
                }
            }
            event.consume();
        }

    }

    private static class OnDragOverHandler implements EventHandler<DragEvent> {
        /**
         * This method is called when a full press-drag-release gesture enters
         * the target.
         * The transfer mode of the gesture is set to move.
         * The event is consumed.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;
                if ((cardField.getCard() == null || event.getGestureSource() == target) && cardField.player != null) {
                    if (event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
            }
            event.consume();
        }

    }

    private static class OnDragEnteredHandler implements EventHandler<DragEvent> {
        /**
         * This method is called when a full press-drag-release gesture enters
         * the target.
         * The transfer mode of the gesture is transferred to the target.
         * The accept transfer modes for the gesture are checked and if the
         * gesture source is not equal to the target, the accept transfer modes
         * are updated to have only the common transfer modes with the gesture
         * source.
         * The drag event is consumed.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;
                if (cardField.getCard() == null && cardField.player != null) {
                    if (event.getGestureSource() != target && event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        target.setBackground(BG_DROP);
                    }
                }
            }
            event.consume();
        }

    }

    private static class OnDragExitedHandler implements EventHandler<DragEvent> {
        /**
         * This method is called when a drag and drop gesture exits the target.
         * The drag and drop gesture entered the target before this event
         * occurred.
         * The background color of the card field view is set to
         * {@link #BG_DEFAULT}.
         * The event is consumed.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;
                if (cardField.getCard() == null && cardField.player != null) {
                    if (event.getGestureSource() != target && event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        target.setBackground(BG_DEFAULT);
                    }
                }
            }
            event.consume();
        }

    }

    private class OnDragDroppedHandler implements EventHandler<DragEvent> {
        /**
         * This method is called when a drag and drop gesture is dropped on the
         * target. The drag and drop gesture is accepted by the target before
         * this method is called.
         * The background color of the card field view is set to
         * {@link #BG_DEFAULT}.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (cardField.getCard() == null && cardField.player != null) {
                    if (event.getGestureSource() != target && db.hasContent(ROBO_RALLY_CARD)) {
                        Object object = db.getContent(ROBO_RALLY_CARD);
                        if (object instanceof String) {
                            CommandCardField source = cardFieldFromRepresentation((String) object);
                            if (source != null && gameController.moveCards(source, cardField)) {
                                // CommandCard card = source.getCard();
                                // if (card != null) {
                                // if (gameController.moveCards(source, cardField)) {
                                // cardField.setCard(card);
                                success = true;
                                // }
                            }
                        }
                    }
                }
                event.setDropCompleted(success);
                target.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

    private static class OnDragDoneHandler implements EventHandler<DragEvent> {

        /**
         * This method is called when the drag and drop gesture is done.
         * The background color of the card field view is set to
         * {@link #BG_DEFAULT}.
         * The event is consumed.
         *
         * @param event the event which occurred
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView source) {
                source.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

}
