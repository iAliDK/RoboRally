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
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;


import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Filter;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = ".json";

    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }
        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname+JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            System.out.println("Inputstream null");
            return  new Board (8,8);
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

            result = new Board(template.width, template.height, boardname, (int)(Math.random()*1001));
            for (SpaceTemplate spaceTemplate : template.spaces) {
                Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }
            //Player positions
            for(int i = 0; i< template.players.size(); i++){
                Player player = new Player(result, template.players.get(i).color, "Player " + (i + 1));
                result.addPlayer(player);
                player.setSpace(result.getSpace(template.players.get(i).x, template.players.get(i).y));
                player.setHeading(template.players.get(i).heading);
                //Player cards
                if (player != null) {
                    for (int j = 0; j < Player.NO_CARDS; j++) {
                        CommandCardField field = player.getCardField(j);
                        field.setCard(template.players.get(i).cards.get(j).card);
                        field.setVisible(true);
                    }
                    for (int j = 0; j < Player.NO_REGISTERS; j++) {
                        CommandCardField field = player.getProgramField(j);
                        field.setCard(template.players.get(i).program.get(j).card);
                        field.setVisible(true);
                    }
                }
            }

            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
        }
        return null;
    }



    public static void saveBoard(Board board, String name) {

//        JsonObject myBoard = new JsonObject();
//
//
//        myBoard.addProperty("Boardwidth", board.width);
//
//        myBoard.addProperty("Boardheight", board.height);

        BoardTemplate boardTemplate = new BoardTemplate();

        boardTemplate.width = board.width;
        boardTemplate.height = board.height;

        //Players
        List<Player> players =  board.getPlayers();
        for(int i = 0; i< board.getPlayersNumber(); i++){
            boardTemplate.players.add(players.get(i).createTemplate());
        }

            //Board size + spaces
            for (int i = 0; i < board.width; i++) {
                for (int j = 0; j < board.height; j++) {
                    Space space = board.getSpace(i, j);
                    if (!space.getWalls().isEmpty() || !space.getActions().isEmpty()) {
                        if (!space.getIsWall() == false) {
                            SpaceTemplate spaceTemplate = new SpaceTemplate();
                            spaceTemplate.x = space.x;
                            spaceTemplate.y = space.y;
                            spaceTemplate.actions.addAll(space.getActions());
                            spaceTemplate.walls.addAll(space.getWalls());
//                            template.spaces.add(spaceTemplate);
                        }
                    }
                }

                ClassLoader classLoader = LoadBoard.class.getClassLoader();
                // TODO: this is not very defensive, and will result in a NullPointerException
                //       when the folder "resources" does not exist! But, it does not need
                //       the file "simpleCards.json" to exist!
//                String filename = "src/"+ name + JSON_EXT;
//             String filename = "resources/boards/"+ name + JSON_EXT;
               String filename = classLoader.getResource(BOARDSFOLDER).getPath() + "/" +  name +JSON_EXT;


                // In simple cases, we can create a Gson object with new:
                //
                //   Gson gson = new Gson();
                //
                // But, if you need to configure it, it is better to create it from
                // a builder (here, we want to configure the JSON serialisation with
                // a pretty printer):
                GsonBuilder simpleBuilder = new GsonBuilder().
                        registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                        setPrettyPrinting();
                Gson gson = simpleBuilder.create();

                FileWriter fileWriter = null;
                JsonWriter writer = null;
                try {
                    fileWriter = new FileWriter(filename);

                    fileWriter.write(boardTemplate.toString());
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
    }
