package model;

import exceptions.InvalidPlayerException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ListOfPlayerTest {
    private ListOfPlayer playerList1;

    private ListOfPlayer playerList2;

      @BeforeEach
      void runBefore() {
          playerList1 = new ListOfPlayer("PlayerList1");
          playerList2 = new ListOfPlayer("PLayerList2");
          playerList2.register("Bukayo Saka", "Arsenal");
          playerList2.register("Erling Haaland", "Man City");
      }

      @Test
      void testConstructor() {
          // to test that a ListOfPlayer is created with empty
          assertEquals("PlayerList1", playerList1.getName());
          ArrayList<String> emptyList = new ArrayList<>();
          assertEquals(0, playerList1.getSize());
      }

      @Test
      void testRegisterPlayer() {
          playerList1.register("Bukayo Saka", "Arsenal");
          assertEquals(1, playerList1.getSize());
          playerList1.register("Erling Haaland", "Man City");
          assertEquals(2, playerList1.getSize());
          assertEquals("Bukayo Saka", playerList1.getPlayer(0).getName());
          assertEquals("Man City" , playerList1.getPlayer(1).getTeam());
      }

      @Test
      void testAddMinutes() {
          playerList1.register("Bukayo Saka", "Arsenal");
          // add minutes to initial minutes zero
          Boolean result = playerList1.findPlayerAndAddMinutes("Bukayo Saka", "Arsenal",
                  78.4);
          assertTrue(result);
          assertEquals(78.4, playerList1.getPlayer(0).getMinutesPlayed());
          Boolean result1 = playerList1.findPlayerAndAddMinutes("Smalling", "Man Utd",
                  88.8);
          assertFalse(result1);
          // add minutes to existing minutes that are not zero
          Boolean result2 = playerList1.findPlayerAndAddMinutes("Bukayo Saka","Arsenal",
                  29.5);
          assertEquals(107.9,playerList1.getPlayer(0).getMinutesPlayed());
          assertTrue(result2);
          assertFalse(playerList2.findPlayerAndAddMinutes("Erling Haaland", "Arsenal",
                  56.9));

      }

      @Test
      void testTransferMethod() {
          playerList1.register("Bukayo Saka", "Arsenal");
          playerList1.register("Erling Haaland", "Man City");
          Boolean result = playerList1.transferPlayer("Bukayo Saka",
                  "Arsenal" ,"Man City");
          assertTrue(result);
          assertEquals("Man City", playerList1.getPlayer(0).getTeam());
          assertFalse(playerList1.transferPlayer("Smalling", "Man Utd", "Arsenal"));
          // test a condition where player has incorrect team name
          assertFalse(playerList2.transferPlayer("Erling Haaland",
                  "Arsenal", "Man Utd"));

      }


      @Test
      void testGoalTally() {
          // adding to an initial tally of zero
          Boolean result = playerList2.tallyGoals("Erling Haaland", "Man City", 5);
          Boolean result2 = playerList2.tallyGoals("Speed", "Bayern", 8);
          assertTrue(result);
          assertFalse(result2);
          assertEquals(5, playerList2.getPlayer(1).getGoalScored());
          // adding to an initial tally of an amount of goals that is not zero
          Boolean result3 = playerList2.tallyGoals("Erling Haaland", "Man City", 6);
          Boolean result4 = playerList2.tallyGoals("Bukayo Saka", "Arsenal", 4);
          assertEquals(11, playerList2.getPlayer(1).getGoalScored());
          assertEquals(4, playerList2.getPlayer(0).getGoalScored());
          assertTrue(result3);
          assertTrue(result4);
          assertFalse(playerList2.tallyGoals("Bukayo Saka", "Man City", 4));
      }

      @Test
      void testGetGoalScore() {
          playerList2.tallyGoals("Bukayo Saka", "Arsenal", 6);
          try {
              assertEquals("6", playerList2.goalScore("Bukayo Saka", "Arsenal"));
              // all good
          } catch (InvalidPlayerException e) {
              fail("Exception not supposed to be thrown");
          }
      }

      @Test
      void testInvalidPlayerForGoalsTest() {
          // test invalid player being entered
          try {
              playerList2.goalScore("Thierry Henry", "Arsenal");
              fail("An exception is expected");
          } catch (InvalidPlayerException e) {
              // all good
          }
          // valid team invalid name
          try {
             playerList2.goalScore("Erling Haaland", "Arsenal");
             fail("An exception is expected");
          } catch (InvalidPlayerException e) {
            // all good
          }
      }

      @Test
      void topGoalScorer() {
          // test topGoal Scorer part one
          playerList2.register("Thierry Henry", "Arsenal");
          playerList2.tallyGoals("Thierry Henry", "Arsenal", 15);
          playerList2.tallyGoals("Bukayo Saka", "Arsenal", 10);
          playerList2.tallyGoals("Erling Haaland", "Man City", 5);
          assertEquals("Thierry Henry", playerList2.topScorer());
          // test after top goal scorer has changed
          playerList2.tallyGoals("Bukayo Saka", "Arsenal", 12 );
          assertEquals("Bukayo Saka", playerList2.topScorer());
          // now make top goal scorer equal
          playerList2.tallyGoals("Thierry Henry", "Arsenal", 7);
          assertEquals("Thierry Henry", playerList2.topScorer());

      }
     @Test
     void testGiveMinutesPlayed() {
        playerList2.register("Thierry Henry", "Arsenal");
        playerList2.findPlayerAndAddMinutes("Bukayo Saka", "Arsenal", 68.9);
        playerList2.findPlayerAndAddMinutes("Erling Haaland", "Man City", 67.9);
        playerList2.findPlayerAndAddMinutes("Thierry Henry", "Arsenal", 45.7);
         try {
             assertEquals("68.9", playerList2.minutesPlayed("Bukayo Saka", "Arsenal"));
             assertEquals("67.9", playerList2.minutesPlayed("Erling Haaland", "Man City"));
         } catch (InvalidPlayerException e) {
             fail("No exception been thrown");
         }

      }

      @Test
      void testInvalidMinutePlayers() {
          try {
            playerList2.minutesPlayed("Chris Smalling", "Man Utd");
            fail("An exception is supposed to be throwm");
          } catch (InvalidPlayerException e) {
              // good as exception is expected
          }
      }

      @Test
      void testIncorrectName() {
          // wrong team right name
          try {
              playerList2.minutesPlayed("Bukayo Saka", "Man City");
              fail("An exception is supposed to be thrown");
          } catch (InvalidPlayerException e) {
              // good as exception is expected.
          }
      }

      @Test
      void testIncorrectTeamName() {
         // wrong team right name
          try {
              playerList2.minutesPlayed("Martinelli", "Arsenal");
          } catch (InvalidPlayerException e) {
              // good as exception is expected;
          }
      }

      @Test
      void testFindTeam() {
          playerList2.register("Thierry Henry", "Arsenal");
          playerList2.register("Odegaard", "Arsenal");
          ArrayList<String> result = new ArrayList<>();
          result.add("Bukayo Saka");
          result.add("Thierry Henry");
          result.add("Odegaard");
          assertEquals(result, playerList2.findTeam("Arsenal"));
          ArrayList<String> emptyResult = new ArrayList<>();
          assertEquals(emptyResult, playerList2.findTeam("Man Utd"));
      }


}
