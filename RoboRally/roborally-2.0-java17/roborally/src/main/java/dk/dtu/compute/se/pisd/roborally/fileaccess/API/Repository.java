package dk.dtu.compute.se.pisd.roborally.fileaccess.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;
/**
 * This Repository class, holds the methods that interacts with the games that is played on the server.
 * The methods include functionalities such as creating a new game, and using board templates to create the board, for those games.
 */
public class Repository {

    SaveClient client = new SaveClient();
    /**
     *
     * Makes the SaveClient send a post request with the pathname that is on saves/saveName.
     * It also serializes the board template to a json and provides it to the post request.
     * @param boardTemplate the boardTemplate object of the game you're trying to save.
     * @param saveName the save name you're trying to post to the server.
     * @author Qiao.
     */
    public void newGame(BoardTemplate boardTemplate, String saveName) throws Exception {
        String fullPath = "saves/"+saveName;
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();
        String jsonBoardTemplateString = gson.toJson(boardTemplate, boardTemplate.getClass());
        client.sendPostRequest(fullPath,jsonBoardTemplateString);
    }

    /**
     * Provides the sendGetRequest method in SaveClient a path name for the save its trying to load.
     * After that it returns the board template on the path name.
     * @param savename the name of the save you want to get as a board template.
     * @return The board template of the save you requested.
     * @author Qiao.
     */
    public BoardTemplate getBoardTemplate(String savename) throws Exception {
        String fullPath = "saves/"+savename;
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();
        String jsonBoardTemplateString = client.sendGetRequest(fullPath);
        BoardTemplate boardTemplate = gson.fromJson(jsonBoardTemplateString, BoardTemplate.class);
        return boardTemplate;
    }

    /**
     * Makes the SaveClient class send a put request using the parameters given.
     * The board template is first serialized before sending it.
     * @param boardTemplate the updated board template you want to put.
     * @param saveName the name of the save you're putting to.
     * @author Qiao.
     */
    public void putBoardTemplate(BoardTemplate boardTemplate, String saveName) throws Exception {
        String fullPath = "saves/"+saveName;
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();
        String jsonBoardTemplateString = gson.toJson(boardTemplate, boardTemplate.getClass());
        client.sendPutRequest(fullPath,jsonBoardTemplateString);
    }
}


