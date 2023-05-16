package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player BukayoSaka;
    private Player ErlingHaaland;

    @BeforeEach
    void runBefore() {
        BukayoSaka = new Player("Bukayo Saka", "Arsenal");
        ErlingHaaland = new Player("Erling Haaland", "Man City" );
    }

    // Tests the constructor
    @Test
    void testConstructor() {
        assertEquals("Bukayo Saka", BukayoSaka.getName());
        assertEquals("Man City",  ErlingHaaland.getTeam());
        assertEquals(0, ErlingHaaland.getGoalScored());
        assertEquals(0.0, BukayoSaka.getMinutesPlayed());
    }

    // Tests for adding goals
    @Test
    void testAddToGoalTally() {
        // adding to a goal tally of zero
        BukayoSaka.addToGoalTally(3);
        assertEquals(3,BukayoSaka.getGoalScored());
        // adding to a goal tally of something not zero
        BukayoSaka.addToGoalTally(2);
        assertEquals(5,BukayoSaka.getGoalScored());
    }

    @Test
    void testAddMinutesPlayed() {
        // adding to a minutes played 0.0 and adding a whole number
        ErlingHaaland.addToMinutes(80.0);
        assertEquals(80.0, ErlingHaaland.getMinutesPlayed());
        // adding to a minutes played amount of not zero
        ErlingHaaland.addToMinutes(32.8);
        assertEquals(112.8, ErlingHaaland.getMinutesPlayed());
    }

    @Test
    void testMakeTransferTo() {
        // making one transfer only
        ErlingHaaland.makeTransfer("Arsenal");
        assertEquals("Arsenal", ErlingHaaland.getTeam());
    }







}
