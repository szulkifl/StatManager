package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exceptions.InvalidPlayerException;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

public class ListOfPlayer implements Writable {

    private String name; // since this is a workroom we also want the name of the list of players
    private ArrayList<Player> players;    // a list of players

    // EFFECTS: creates a new empty list of players
    public ListOfPlayer(String name) {
        this.name = name;
        players = new ArrayList<>();
    }

    public void addPlayerJson(Player player) {
        players.add(player);
    }

    public String getName() {
        return name;
    }


    // REQUIRES: playerName is more than 0 characters.
    // MODIFIES: this
    // EFFECTS: looks for a player with the given name and if player is found in the list
    //          then minutes are added to that players total game time, after which it returns true
    //           and if the player is not found then it returns false
    public Boolean findPlayerAndAddMinutes(String playerName, String team, Double minutesPlayed) {
        Boolean checker = false;
        for (Player p : players) {
            if ((p.getName().equals(playerName)) && (p.getTeam().equals(team))) {
                checker = true;
                p.addToMinutes(minutesPlayed);
                EventLog.getInstance().logEvent(new Event(Double.toString(minutesPlayed) + " minutes"
                        + " have been added to " + playerName + "'s gametime"));
            }
        }
        return checker;
    }

    // REQUIRES: newTeam must not be the same team as the players current team
    // MODIFIES: this
    // EFFECTS: returns true if both the player and the team name match and then changes the players current team to
    //          the given to team, returns false if both name and team name don't match.
    public Boolean transferPlayer(String playerName, String currentTeam, String newTeam) {
        Boolean checker = false;
        for (Player p : players) {
            if ((p.getName().equals(playerName)) && (p.getTeam().equals(currentTeam))) {
                checker = true;
                p.makeTransfer(newTeam);
                EventLog.getInstance().logEvent(new Event(playerName + " transferred to "
                        + newTeam + " from " + currentTeam));
            }
        }
        return checker;
    }

    // MODIFIES: this
    // EFFECTS: creates a new player with the given name and adds the player to the list
    public void register(String name, String team) {
        Player newPlayer = new Player(name, team);
        players.add(newPlayer);
        EventLog.getInstance().logEvent(new Event(name + " player for " + team + " has been registered."));
    }

    // MODIFIES: this
    // EFFECTS: adds to the goal tally of a certain player if that players name and team match any player in the list
    //          and returns true, if that player is not in the team then it returns false;
    public Boolean tallyGoals(String name, String team, int goals) {
        Boolean checker = false;
        for (Player p : players) {
            if ((p.getName().equals(name)) && (p.getTeam().equals(team))) {
                checker = true;
                p.addToGoalTally(goals);
                EventLog.getInstance().logEvent(new Event(Integer.toString(goals)
                        + " goals added to goal tally of " + name));
            }
        }
        return checker;
    }

    // REQUIRES: player and team name must be greater than zero
    // EFFECTS: returns true if player has been found
    public String goalScore(String name, String team) throws InvalidPlayerException {
        Boolean result = false;
        String goals = null;
        for (Player p : players) {
            if ((p.getName().equals(name)) && (p.getTeam().equals(team))) {
                result = true;
                goals = Integer.toString(p.getGoalScored());
                EventLog.getInstance().logEvent(new Event("Display goals scored by " + name));
            }
        }
        if (result) {
            return goals;
        } else {
            throw new InvalidPlayerException();
        }
    }


    // REQUIRES: player and team name must be greater than zero
    // EFFECTS:  gives minutes played by a player
    //           throws InvalidPlayerException when name or team don't match
    public String minutesPlayed(String name, String team) throws InvalidPlayerException {
        Boolean result = false;
        String minute = null;
        for (Player p : players) {
            if (p.getName().equals(name) && (p.getTeam().equals(team))) {
                result = true;
                minute = Double.toString(p.getMinutesPlayed());
                EventLog.getInstance().logEvent(new Event("Display minutes played by " + name));
            }
        }
        if (result) {
            return minute;
        } else {
            throw new InvalidPlayerException();
        }
    }


    // REQUIRES: ListOfPlayer should have at least one player
    // EFFECTS: returns the top goalscorer on the basis of goals scored
    //          if two players have equal amount of goals then they are just chosen at random
    public String topScorer() {
        int maxGoalsSoFar = 0;
        String topGoalScorer = players.get(0).getName(); // does this initiate as " "
        EventLog.getInstance().logEvent(new Event("Top goal scorer is shown"));
        for (Player p : players) {
            if (p.getGoalScored() > maxGoalsSoFar) {
                maxGoalsSoFar = p.getGoalScored();
                topGoalScorer = p.getName();
            } else if (p.getGoalScored() == maxGoalsSoFar) {
                topGoalScorer = p.getName();
            }
        }
        return topGoalScorer;
    }

    // REQUIRES: the team should be a valid premier league team
    // EFFECTS:  produces a list of all the players if they belong to the given team
    public ArrayList<String> findTeam(String team) {
        ArrayList<String> teamPlayers = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Players playing for " + team + " are shown."));
        for (Player p : players) {
            if (p.getTeam().equals(team)) {
                teamPlayers.add(p.getName());
            }
        }
        return teamPlayers;
    }

    // EFFECTS: returns size of a list of players
    public int getSize() {
        return players.size();
    }

    // REQUIRES : the index has to be zero based
    // EFFECTS: returns a player with the given index
    public Player getPlayer(int index) {
        return players.get(index);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("players", playersToJson());
        return json;
    }

    // EFFECTS: returns the players in this list as a JSON Array
    private JSONArray playersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Player p : players) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns number of players on the list;
    public int numPlayers() {
        int number = 0;
        for (Player p : players) {
            number = number + 1;
        }
        return number;
    }

    // EFFECTS: returns an unmodifiable list
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

}

