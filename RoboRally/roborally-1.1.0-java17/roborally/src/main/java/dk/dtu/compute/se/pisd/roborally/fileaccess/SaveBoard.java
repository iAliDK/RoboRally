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
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.fileaccess.API.Repository;
import java.io.*;
import java.util.List;

/**
 * This class has the methods for saving the board and all its info into JSON files.
 * One method is for saving online and one is for saving locally.
 * @author Qiao
 */
public class SaveBoard {
    static Repository api = new Repository();

    private static final String BOARDSFOLDER = "boards";
    private static final String JSON_EXT = ".json";

    public static BoardTemplate newTemplate = null;

    /**
     * The save board API method saves all the information of the game into a boardtemplate and then serializes it into a JSON file.
     * After that it calls the API to send it to the server.
     * @param board The board object that is getting saved.
     * @param name The name of your JSON file.
     * @author Qiao.
     */
    public static void saveBoardAPI(Board board, String name) throws Exception {
        BoardTemplate boardTemplate = new BoardTemplate();
        boardTemplate.width = board.width;
        boardTemplate.height = board.height;
        boardTemplate.currentplayer = board.getPlayerNumber(board.getCurrentPlayer());
        boardTemplate.phase = board.getPhase();
        boardTemplate.saveName = name;
        boardTemplate.boardName = board.boardName;
        boardTemplate.step = board.getStep();
        //Board size + spaces
        for (int i = 0; i < board.width; i++) {
            for (int j = 0; j < board.height; j++) {
                Space space = board.getSpace(i, j);
                SpaceTemplate temp = new SpaceTemplate(space.x, space.y, space.getFieldAction());
                boardTemplate.spaces.add(temp);
            }
        }
        //Players + cards
        List<Player> players = board.getPlayers();
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            boardTemplate.players.add(players.get(i).createTemplate());
        }
        api.putBoardTemplate(boardTemplate, name);
    }

    /**
     * The save board method saves all the information of the game into a boardtemplate and then serializes it into a JSON file.
     * @param board The board object that is getting saved.
     * @param name The name of your JSON file.
     * @author Qiao.
     */
    public static void saveBoard(Board board, String name) {

        BoardTemplate boardTemplate = new BoardTemplate();
        boardTemplate.width = board.width;
        boardTemplate.height = board.height;
        boardTemplate.currentplayer = board.getPlayerNumber(board.getCurrentPlayer());
        boardTemplate.phase = board.getPhase();
        boardTemplate.step = board.getStep();
        boardTemplate.boardName = board.boardName;
        //Players + cards
        List<Player> players = board.getPlayers();
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            boardTemplate.players.add(players.get(i).createTemplate());
        }

        //Board size + spaces
        for (int i = 0; i < board.width; i++) {
            for (int j = 0; j < board.height; j++) {
                Space space = board.getSpace(i, j);
                SpaceTemplate temp = new SpaceTemplate(space.x, space.y, space.getFieldAction());
                boardTemplate.spaces.add(temp);
            }
        }
        ClassLoader classLoader = SaveBoard.class.getClassLoader();
        String filename = classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + JSON_EXT;
        boardTemplate.saveName = filename;
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);

            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(boardTemplate, boardTemplate.getClass(), writer);
            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {
                }
            }
        }
    }

}

