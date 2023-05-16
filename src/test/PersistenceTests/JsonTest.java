package PersistenceTests;

import model.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    public void checkPlayer(String playerName, String playerTeam, int playerGoals, double minutes, Player man) {
        assertEquals(playerName, man.getName());
        assertEquals(playerTeam, man.getTeam());
        assertEquals(playerGoals, man.getGoalScored());
        assertEquals(minutes, man.getMinutesPlayed());
    }
}
