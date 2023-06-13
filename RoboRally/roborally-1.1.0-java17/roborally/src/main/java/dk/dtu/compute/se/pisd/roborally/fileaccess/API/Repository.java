package dk.dtu.compute.se.pisd.roborally.fileaccess.API;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;

public class Repository {

    HttpClient client = new HttpClient();


    public BoardTemplate getBoardTemplate() throws Exception {
        Gson gson = new Gson();
        String jsonBoardTemplateString = client.sendGetRequest("loadGame");
        return gson.fromJson(jsonBoardTemplateString,BoardTemplate.class);
    }

    public void sendBoardTemplate(BoardTemplate boardTemplate, String saveName) throws Exception {
        Gson gson = new Gson();
        String fullPath = "saveGame/"+saveName;

        String jsonBoardTemplate = gson.toJson(boardTemplate);
        client.sendPostRequest(fullPath,jsonBoardTemplate);
    }

}


