package dk.dtu.compute.se.pisd.roborally.fileaccess.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.boardElements.FieldAction;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Repository {

    HttpClient client = new HttpClient();

    //WIP
    public void newGame(BoardTemplate boardTemplate, String saveName) throws Exception {

        String fullPath = "saves/"+saveName;


        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        String jsonBoardTemplateString = gson.toJson(boardTemplate, boardTemplate.getClass());
        client.sendPostRequest(fullPath,jsonBoardTemplateString);
    }
    public BoardTemplate getBoardTemplate(String savename) throws Exception {
        String fullPath = "saves/"+savename;
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();
        String jsonBoardTemplateString = client.sendGetRequest(fullPath);
        BoardTemplate boardTemplate = gson.fromJson(jsonBoardTemplateString, BoardTemplate.class);
        return boardTemplate;
    }


    public void putBoardTemplate(BoardTemplate boardTemplate, String saveName) throws Exception {
        String fullPath = "saves/"+saveName;
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();
        String jsonBoardTemplateString = gson.toJson(boardTemplate, boardTemplate.getClass());
        client.sendPutRequest(fullPath,jsonBoardTemplateString);

    }



}


