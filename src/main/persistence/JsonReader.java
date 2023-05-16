package persistence;

import model.ListOfPlayer;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents reader that reads the list of players from the JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs a reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads the list of player from the file and returns it
    // throws IOException if an error occurs in data reading
    public ListOfPlayer read() throws IOException {
       String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseListOfPlayer(jsonObject);
    }

    // EFFECTS: reads source file as a string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses the list of players from the JSON object and returns it
    private ListOfPlayer parseListOfPlayer(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        ListOfPlayer wr = new ListOfPlayer(name);
        addPlayers(wr, jsonObject);
        return wr;
    }

    // MODIFIES: wr
    // EFFECTS: parses the players from the JSON object and adds them to the list of players
    private void addPlayers(ListOfPlayer wr, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("players");
        for (Object json : jsonArray) {
            JSONObject nextPlayer = (JSONObject) json;
            addPlayer(wr, nextPlayer);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses the player from the JSON object and adds said player to the list of player
    private void addPlayer(ListOfPlayer wr, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String team = jsonObject.getString("team");
        int    goalsScored = jsonObject.getInt("goalsScored");
        Double minutesPlayed = jsonObject.getDouble("minutesPlayed");
        Player player = new Player(name, team, minutesPlayed, goalsScored);
        wr.addPlayerJson(player);
    }
}
