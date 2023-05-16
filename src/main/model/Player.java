package model;


import org.json.JSONObject;
import persistence.Writable;

public class Player implements Writable {
    private final String name;           // name of the player
    private String team;                 // team the player plays for
    private double minutesPlayed;        // total minutes played by the player in the tournament
    private int goalScored;              // total goals scored so far

    // REQUIRES : both player name and team name must be of a non-zero character length
    // EFFECTS  : creates a player with given name and given team, with minutes played and goals scored
    //            initiated with null (aka zero)
    public Player(String name, String team) {
        this.name = name;
        this.team = team;
    }

    // REQUIRES: player name and team should be of non-zero character length, minutes played and goalScored should be
    //           non-zero.
    // EFFECTS: creates a player with given name, team, minutesPlayed and goalScored
    public Player(String name, String team, double minutesPlayed, int goalScored) {
        this.name = name;
        this.team = team;
        this.minutesPlayed = minutesPlayed;
        this.goalScored = goalScored;
    }

    // REQUIRES: tally > 0
    // MODIFIES: this
    // EFFECTS:  increases goals scored by given tally amount
    public void addToGoalTally(int tally) {
        this.goalScored = this.goalScored + tally;
    }

    // REQUIRES: 120.0 > gameTime > 0.0
    // MODIFIES: this
    // EFFECTS:  increases the amount of minutes played by given game time
    public void addToMinutes(double gameTime) {
        this.minutesPlayed = this.minutesPlayed + gameTime;
    }

    // REQUIRES: the input to must be a valid premier league team and string input to must have more than 1 character
    // MODIFIES: this
    // EFFECTS: changes the team of a player to the given team
    public void makeTransfer(String to) {
        this.team = to;
    }

    // EFFECTS: returns the player name
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns the player team
    public String getTeam() {
        return this.team;
    }

    //EFFECTS: returns the number of goals scored
    public int getGoalScored() {
        return this.goalScored;
    }

    //EFFECTS : returns the number of minutes played
    public double getMinutesPlayed() {
        return this.minutesPlayed;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("team", team);
        json.put("goalsScored", goalScored);
        json.put("minutesPlayed", minutesPlayed);
        return json;
    }
}


