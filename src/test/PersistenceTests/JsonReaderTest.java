package PersistenceTests;

import model.ListOfPlayer;
import model.Player;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{

    private JsonReader reader;

   @Test
   void testNonExistentFile() {
        reader = new JsonReader("./data/noSuchFile.json");
       try {
           ListOfPlayer playerList = reader.read();
           fail("Exception thrown");
       } catch (IOException e) {
           // IOException caught
       }
   }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyListOfPlayer.json");
        try {
            ListOfPlayer playerList = reader.read();
            assertEquals("name", playerList.getName());
            assertEquals(0, playerList.numPlayers());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testSampleListOfPlayers() {
        JsonReader reader = new JsonReader("./data/testReaderSampleListOfPlayer.json");
       try {
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
