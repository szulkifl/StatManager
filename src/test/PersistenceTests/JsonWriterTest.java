package PersistenceTests;

import model.ListOfPlayer;
import model.Player;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ListOfPlayer players = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyListOfPlayer.json");
        try {
            ListOfPlayer players = reader.read();
            assertEquals("name", players.getName());
            assertEquals(0, players.numPlayers());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testWriterSampleListOfPlayer.json");
        try {
            ListOfPlayer wr = new ListOfPlayer("name");
            wr.addPlayerJson(new Player("Bukayo Saka", "Arsenal", 45.67, 5));
            wr.addPlayerJson(new Player("Erling Haaland", "Arsenal", 34.56, 3));
            JsonWriter writer = new JsonWriter("./data/testWriterSampleListOfPlayer.json");
            writer.open();
            writer.write(wr);
            writer.close();

            ListOfPlayer players = reader.read();
            assertEquals("name", players.getName());
            List<Player> playersList = players.getPlayers();
            assertEquals(2, playersList.size());
            checkPlayer("Bukayo Saka", "Arsenal", 5, 45.67 , playersList.get(0));
            checkPlayer("Erling Haaland", "Arsenal", 3, 34.56, playersList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
