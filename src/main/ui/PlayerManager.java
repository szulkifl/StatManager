package ui;


import exceptions.InvalidPlayerException;
import model.Event;
import model.EventLog;
import model.ListOfPlayer;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

// PlayerManager Application
public class PlayerManager extends JFrame {
    private ListOfPlayer playerList;

    private Scanner input;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JFrame loginFrame;

    private JFrame optionsFrame;
    private JFrame regFrame;
    private JFrame addGoalFrame;
    private JFrame addMinsFrame;
    private JFrame transferFrame;
    private JFrame procureTeamFrame;
    private JFrame findGoalsFrame;
    private JFrame findMinutesFrame;
    private static final String JSON_STORE = "./data/workroom.json";

    // runs the player manager application
    public PlayerManager() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        loginFrame = new JFrame();// new frame created

        JPanel testPanel = new JPanel();
        testPanel.setBackground(Color.white);
        testPanel.setLayout(new GridLayout(0, 1));
        ImageIcon image = new ImageIcon("./data/logo.png");

        JLabel label = new JLabel();
        label.setIcon(image);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(Color.white);
        label.setOpaque(true);


        JButton mainMenu = new JButton(new MainMenu());
        mainMenu.setFont(new Font("Arial", Font.BOLD, 50));
        mainMenu.setForeground(new Color(120, 30, 200));
        mainMenu.setSize(10, 5);

        testPanel.add(label);
        testPanel.add(mainMenu);

        displayInitiatior(loginFrame, testPanel, "Premier League Stat Manager");
        runManager();
    }

    // EFFECTS: this class represents a MainMenu panel and all associated actions

    private class MainMenu extends AbstractAction {
        // EFFECTS: creates a new main menu window after clicking the enter button
        MainMenu() {
            super("Enter");
        }

        // EFFECTS: closes the login frame window and opens the display which gives user access to all actions
        @Override
        public void actionPerformed(ActionEvent e) {
            loginFrame.dispose();
            userStoryDisplay();

            optionsFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    EventLog eventLog = EventLog.getInstance();
                    for (Event event : eventLog) {
                        System.out.println("\n");
                        System.out.println(event);
                    }
                    System.exit(0);
                }
            });
        }

        // EFFECTS: creates and sets the GUI elements for all the possible actions
        private void userStoryDisplay() {
            optionsFrame = new JFrame();
            JPanel optionsPanel = new JPanel();
            JButton addPlayerButton = new JButton(new RegisterMenu());
            JButton addToGoalTally = new JButton(new AddGoalsToAPlayer());
            JButton addMins = new JButton(new AddMinutes());
            JButton transfer = new JButton(new Transfer());
            JButton topScorer = new JButton("Find top scorer");
            JButton goalsScored = new JButton(new FindGoals());
            JButton minutesPlayed = new JButton(new FindMinutes());
            JButton viewPlayers = new JButton("View All the Players");
            JButton procureTeam = new JButton(new ProcureTeam());
            JButton save = new JButton("Save");
            JButton load = new JButton("Load stat panel");

            adjustFont(addPlayerButton, addToGoalTally, addMins, transfer,
                    topScorer, goalsScored, minutesPlayed, viewPlayers, procureTeam, save, load);
            optionsPanel.setLayout(new GridLayout(0, 1));

            allOptioonsButtonOrganizer(optionsPanel, addPlayerButton, addToGoalTally, addMins, transfer, topScorer,
                    goalsScored, minutesPlayed, viewPlayers, procureTeam, save, load);

            displayInitiatior(optionsFrame, optionsPanel, "What do you want to do?");

        }

        // EFFECTS: sets the font of all the buttons to standard style
        private void adjustFont(JButton addPlayer, JButton addGoal, JButton addMin, JButton transfer, JButton topScorer,
                                JButton goalsScored, JButton minutesPlayed, JButton vp,
                                JButton procureTeam, JButton save, JButton load) {
            setFontStyleToProgramStandar(addPlayer);
            setFontStyleToProgramStandar(addGoal);
            setFontStyleToProgramStandar(addMin);
            setFontStyleToProgramStandar(transfer);
            setFontStyleToProgramStandar(topScorer);
            setFontStyleToProgramStandar(goalsScored);
            setFontStyleToProgramStandar(minutesPlayed);
            setFontStyleToProgramStandar(vp);
            setFontStyleToProgramStandar(procureTeam);
            setFontStyleToProgramStandar(save);
            setFontStyleToProgramStandar(load);
        }

        // EFFECTS: sets the font of each button to standard
        private void setFontStyleToProgramStandar(JButton button) {
            button.setFont(new Font("Arial", Font.BOLD, 25));
            button.setForeground(new Color(120, 30, 200));
        }

        // EFFECTS: sets up the GUI elements structure of the menu panel
        private void allOptioonsButtonOrganizer(JPanel optionsPanel, JButton register, JButton addGoal, JButton addMins,
                                                JButton transfer, JButton findTop, JButton viewGoals, JButton viewMins,
                                                JButton viewPlayers, JButton procureTeam,
                                                JButton save, JButton load) {
            optionsPanel.add(register);
            optionsPanel.add(addGoal);
            optionsPanel.add(addMins);
            optionsPanel.add(transfer);
            optionsPanel.add(findTop);
            optionsPanel.add(viewGoals);
            optionsPanel.add(viewMins);
            optionsPanel.add(viewPlayers);
            optionsPanel.add(procureTeam);
            optionsPanel.add(save);
            optionsPanel.add(load);

            viewPopUpActions(viewPlayers, findTop, save, load);

        }

        // EFFECTS: carry out all teh actions that don't need an entire new panel, only a pop up
        private void viewPopUpActions(JButton viewPlayers, JButton findTop, JButton save, JButton load) {

            viewPlayers.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, getStringList());
            });

            findTop.addActionListener(e -> {
                JOptionPane.showMessageDialog(null,
                        "The leagues top scorer is " + playerList.topScorer());
            });

            save.addActionListener(e -> {
                saveListOfPlayer();
                JOptionPane.showMessageDialog(null, "Progress Saved!");
            });

            load.addActionListener(e -> {
                loadListOfPlayer();
                JOptionPane.showMessageDialog(null, "Progress Loaded!");
            });
        }
    }

    // A private class representing the register player action
    private class RegisterMenu extends AbstractAction {

        // creates a new RegisterMenu objects which is associated to the button with given name
        RegisterMenu() {
            super("Add Player");
        }

        // EFFECTS: closes the option frame and opens up the player register display
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            displayRegisterer();
        }

        // EFFECTS: displays all the GUI elements
        private void displayRegisterer() {
            regFrame = new JFrame();

            JPanel structurePanel = new JPanel();
            structurePanel.setLayout(new GridLayout(3, 2));

            JLabel nameLabel = new JLabel("Player Name?");
            JTextField nameFld = new JTextField();

            JLabel teamLabel = new JLabel("Player Team?");
            JTextField teamFld = new JTextField();

            JButton sbmit = new JButton("Submit");
            JButton goBack = new JButton("Go Back");

            regPageDisplaySetter(structurePanel, nameLabel, nameFld, teamLabel, teamFld, sbmit, goBack);
            displayInitiatior(regFrame, structurePanel, "Add Player");

            carryOutPlayerRegistration(nameFld, teamFld, sbmit, goBack, regFrame);


        }

        // EFFECTS: sets up all the GUI elements of the register player panel
        private void regPageDisplaySetter(JPanel structurePanel, JLabel nameL, JTextField nameF, JLabel teamL,
                                          JTextField teamF, JButton submit, JButton goBack) {
            structurePanel.add(nameL);
            structurePanel.add(nameF);
            structurePanel.add(teamL);
            structurePanel.add(teamF);
            structurePanel.add(submit);
            structurePanel.add(goBack);
        }

        // MODIFIES: playerList
        // EFFECTS: Conduct the action of adding a player to the list of players
        private void carryOutPlayerRegistration(JTextField nameField, JTextField textField, JButton sbm,
                                                JButton gb, JFrame frame) {

            sbm.addActionListener(e -> {
                playerList.register(nameField.getText().trim(), textField.getText().trim());
                JOptionPane.showMessageDialog(null, nameField.getText().trim()
                        + " has been added!");
                returnToOptionMenu(frame);
            });

            gb.addActionListener(e -> {
                returnToOptionMenu(frame);
            });
        }
    }

    // A private class representing the add goal action
    private class AddGoalsToAPlayer extends AbstractAction {

        //EFFECTS: creates a new AddGoalsToPlayerObject that is associated to the button with the given label
        AddGoalsToAPlayer() {
            super("Add to Goal Tally");
        }

        // EFFECTS: closes the options window and opens the addGoals GUI window
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            addGoalsDisplay();
        }

        //EFFECTS: shows produces the Add to Goal Tally Panel
        private void addGoalsDisplay() {
            addGoalFrame = new JFrame();

            JPanel addGoalPanel = new JPanel(new GridLayout(4, 2));

            JLabel nameLabel = new JLabel("Player Name?");
            JTextField nameField = new JTextField();
            JLabel teamLabel = new JLabel("Player Team?");
            JTextField teamField = new JTextField();
            JLabel goalsLabel = new JLabel("Goals?");
            JTextField goalsField = new JTextField();

            JButton sbm = new JButton("Submit");
            JButton gb = new JButton("Go Back");

            addGoalsDisplaySetter(addGoalPanel, nameLabel, nameField, teamLabel, teamField, goalsLabel, goalsField,
                    sbm, gb);
            displayInitiatior(addGoalFrame, addGoalPanel, "Add to goal tally of a player");
            commitAddGoalAction(nameField, teamField, goalsField, sbm, gb, addGoalFrame);
        }

        // EFFECTS: sets the GUI elements up for to display the goal addition panel
        private void addGoalsDisplaySetter(JPanel panel, JLabel nameL, JTextField nameField, JLabel teamL,
                                           JTextField teamField, JLabel goalsL, JTextField goalField,
                                           JButton sbm, JButton gb) {
            panel.add(nameL);
            panel.add(nameField);
            panel.add(teamL);
            panel.add(teamField);
            panel.add(goalsL);
            panel.add(goalField);
            panel.add(sbm);
            panel.add(gb);
        }

        // MODIFIES: playerList
        // EFFECTS: carries out the implementations that can be caused due to user actions
        private void commitAddGoalAction(JTextField nameField, JTextField teamField, JTextField goalsField,
                                         JButton submit, JButton goBack, JFrame addGoalFrame) {
            submit.addActionListener(e -> {
                Boolean result = playerList.tallyGoals(nameField.getText().trim(), teamField.getText().trim(),
                        Integer.parseInt(goalsField.getText().trim()));
                if (result) {
                    JOptionPane.showMessageDialog(null, nameField.getText().trim()
                            + "'s goals have been tallied!");
                    returnToOptionMenu(addGoalFrame);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Entry");
                }

            });

            goBack.addActionListener(e -> {
                returnToOptionMenu(addGoalFrame);
            });
        }
    }

    // A private class the represents the add mins to a player action
    private class AddMinutes extends AbstractAction {
        // EFFECTS: creates a new AddMins and associates the button with given name to the action
        AddMinutes() {
            super("Add to minutes played");
        }

        // EFFECTS: closes the options window and opens the window to add mins to a player
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            displayAddToMinutesSetUp();
        }

        // EFFECTS: displays the GUI elements of the add to minutes window
        private void displayAddToMinutesSetUp() {
            addMinsFrame = new JFrame();

            JPanel addMinsPanel = new JPanel();
            addMinsPanel.setLayout(new GridLayout(4, 2));

            JLabel nameLabel = new JLabel("Player Name?");
            JTextField nameField = new JTextField();
            JLabel teamLabel = new JLabel("Player Team?");
            JTextField teamField = new JTextField();
            JLabel minsLabel = new JLabel("Minutes Played?");
            JTextField minsField = new JTextField();

            JButton sbm = new JButton("Submit");
            JButton gb = new JButton("Go Back");

            addGMinsDisplaySetter(addMinsPanel, nameLabel, nameField, teamLabel, teamField, minsLabel, minsField,
                    sbm, gb);
            displayInitiatior(addMinsFrame, addMinsPanel, "Add minutes to a player");
            addTheMinsPlayed(addMinsFrame, nameField, teamField, minsField, sbm, gb);


        }

        // EFFECTS: sets the GUI elements up for to display the goal addition panel
        private void addGMinsDisplaySetter(JPanel panel, JLabel nameL, JTextField nameField, JLabel teamL,
                                           JTextField teamField, JLabel minutesL, JTextField minsField,
                                           JButton sbm, JButton gb) {
            panel.add(nameL);
            panel.add(nameField);
            panel.add(teamL);
            panel.add(teamField);
            panel.add(minutesL);
            panel.add(minsField);
            panel.add(sbm);
            panel.add(gb);
        }

        // EFFECTS uses the user input to actually conduct the changes to minutes Played
        private void addTheMinsPlayed(JFrame minsFrmae, JTextField nameField, JTextField teamField,
                                      JTextField minsField, JButton sbm, JButton gb) {
            sbm.addActionListener(e -> {
                Boolean result =
                        playerList.findPlayerAndAddMinutes(nameField.getText().trim(), teamField.getText().trim(),
                                Double.parseDouble(minsField.getText().trim()));
                if (result) {
                    JOptionPane.showMessageDialog(null, nameField.getText().trim()
                            + "'s minutes have been tallied!");
                    returnToOptionMenu(minsFrmae);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid entry");
                }
            });

            gb.addActionListener(e -> {
                returnToOptionMenu(minsFrmae);
            });
        }
    }

    // A private class that represents the actions that allows the user to Transfer a player
    private class Transfer extends AbstractAction {
        // EFFECTS: creates a new action that allows the user to carry out transfer action, on the button with given
        //          name
        Transfer() {
            super("Transfer");
        }

        // EFFECTS: closes the options window and opens the window where the user can do a transfer request
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            displayTransfer();
        }

        // EFFECTS; sets up the GUI elements for transfer panel
        private void displayTransfer() {
            transferFrame = new JFrame();

            JPanel transferPanel = new JPanel();
            transferPanel.setLayout(new GridLayout(4, 2));

            JLabel nameLabel = new JLabel("Name?");
            JTextField nameField = new JTextField();
            JLabel teamLabel = new JLabel("From?");
            JTextField teamField = new JTextField();
            JLabel to = new JLabel("To?");
            JTextField toField = new JTextField();

            JButton sbm = new JButton("Submit");
            JButton gb = new JButton("Go back");

            transferDisplaySetter(transferPanel, nameLabel, nameField, teamLabel, teamField, to, toField,
                    sbm, gb);
            displayInitiatior(transferFrame, transferPanel, "Do Transfer");
            commitToTransfer(transferFrame, nameField, teamField, toField, sbm, gb);
        }

        // EFFECTS: sets up the GUi elements for transfer of player
        private void transferDisplaySetter(JPanel transferPanel, JLabel nameLabel, JTextField nameField,
                                           JLabel teamLabel, JTextField teamField, JLabel toLabel,
                                           JTextField toField, JButton sb, JButton gb) {
            transferPanel.add(nameLabel);
            transferPanel.add(nameField);
            transferPanel.add(teamLabel);
            transferPanel.add(teamField);
            transferPanel.add(toLabel);
            transferPanel.add(toField);
            transferPanel.add(sb);
            transferPanel.add(gb);
        }

        // MODIFIES: playerList
        // EFFECTS: changes the teams of the given player depending on the user input
        private void commitToTransfer(JFrame frame, JTextField nameField, JTextField teamField, JTextField toField,
                                      JButton sb, JButton gb) {
            sb.addActionListener(e -> {
                Boolean result = playerList.transferPlayer(nameField.getText().trim(), teamField.getText().trim(),
                        toField.getText().trim());
                if (result) {
                    JOptionPane.showMessageDialog(null, nameField.getText().trim()
                            + " has been transferred to " + toField.getText().trim());
                    returnToOptionMenu(frame);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid entry");
                }
            });
            gb.addActionListener(e -> {
                returnToOptionMenu(frame);
            });
        }
    }

    // A private class that represents the Procure team action
    private class ProcureTeam extends AbstractAction {
        // Creates a new action associated to the procureTeam button, with the given label
        ProcureTeam() {
            super("Find players in a team");
        }

        // EFFECTS: closes the options window and opens up the window for procure team action
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            openProcureTeamWindow();
        }

        //EFFECTS: opens up the procure team panel
        private void openProcureTeamWindow() {
            procureTeamFrame = new JFrame();

            JPanel procureTeamPanel = new JPanel();
            procureTeamPanel.setLayout(new GridLayout(2, 2));

            JLabel team = new JLabel("Which team?");
            JTextField teamField = new JTextField();
            JButton sbm = new JButton("Submit");
            JButton gb = new JButton("Go Back");

            procureTeamDisplayer(procureTeamPanel, team, teamField, sbm, gb);
            displayInitiatior(procureTeamFrame, procureTeamPanel, "Which team?");
            doProcureTeamAction(procureTeamFrame, teamField, sbm, gb);
        }

        //EFFECTS: set up the GUI element for the procure team structure
        private void procureTeamDisplayer(JPanel teamPanel, JLabel team, JTextField input, JButton sb, JButton gb) {
            teamPanel.add(team);
            teamPanel.add(input);
            teamPanel.add(sb);
            teamPanel.add(gb);
        }

        //EFFECTS: take user input to carry out intended user action
        private void doProcureTeamAction(JFrame procureFrame, JTextField teamInput, JButton sbm, JButton gb) {
            sbm.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, getPlayersInATeam(teamInput.getText().trim()));
                returnToOptionMenu(procureFrame);
            });
            gb.addActionListener(e -> {
                returnToOptionMenu(procureFrame);
            });
        }
    }

    // A private class that represents the action where the user wants to find goals scored by a player
    private class FindGoals extends AbstractAction {
        // Initiates an abstract action associated to a button with the given label
        FindGoals() {
            super("Find goals scored by a player");
        }

        // EFFECTS: closes the optionFrame and opens the new frame for finding goals scored
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            openFindGoalsPanel();
        }

        // EFFECTS: opens up the GUI panel for the Find goals scored by a player window
        private void openFindGoalsPanel() {
            findGoalsFrame = new JFrame();

            JPanel findGoalsPanel = new JPanel();
            findGoalsPanel.setLayout(new GridLayout(3, 2));
            JLabel nameLabel = new JLabel("Name: ");
            JTextField nameField = new JTextField();
            JLabel teamLabel = new JLabel("Team: ");
            JTextField teamField = new JTextField();

            JButton sbm = new JButton("Submit");
            JButton gb = new JButton("Go back");

            setFindGoalsPanel(findGoalsPanel, nameLabel, nameField, teamLabel, teamField, sbm, gb);
            displayInitiatior(findGoalsFrame, findGoalsPanel, "Find goals scored by a player");
            handleFindGoalsOperation(findGoalsFrame, nameField, teamField, sbm, gb);
        }

        //EFFECTS: sets up the GUI elements of the find goals panel
        private void setFindGoalsPanel(JPanel panel, JLabel nameLabel, JTextField nameField, JLabel teamLabel,
                                       JTextField teamField, JButton sbm, JButton gb) {
            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(teamLabel);
            panel.add(teamField);
            panel.add(sbm);
            panel.add(gb);
        }

        // MODIFIES: playerList
        // EFFECTS: carries out the changes as determined by user input
        private void handleFindGoalsOperation(JFrame findGoalsFrame, JTextField nameField, JTextField teamField,
                                              JButton sbm, JButton gb) {
            sbm.addActionListener(e -> {
                try {
                    String result = playerList.goalScore(nameField.getText().trim(), teamField.getText().trim());
                    JOptionPane.showMessageDialog(null, nameField.getText().trim()
                            + " has scored " + result + " goals");
                    returnToOptionMenu(findGoalsFrame);
                } catch (InvalidPlayerException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Entry");
                }
            });
            gb.addActionListener(e -> {
                returnToOptionMenu(findGoalsFrame);
            });
        }
    }

    // A private class that represents the user action where a user finds the minutes played by a player
    private class FindMinutes extends AbstractAction {
        // creates a new find minutes action associated to the JButton with the given label
        FindMinutes() {
            super("Find minutes played by a player");
        }

        // EFFECTS: closes the options window and opens up the find minutes played window
        @Override
        public void actionPerformed(ActionEvent e) {
            optionsFrame.dispose();
            openFindMinutesPanel();
        }

        //EFFECTS: creates and sets up the elements of the find minutes played by a player panel
        private void openFindMinutesPanel() {
            findMinutesFrame = new JFrame();

            JPanel findMinutesPanel = new JPanel();
            findMinutesPanel.setLayout(new GridLayout(3, 2));
            JLabel nameLabel = new JLabel("Name: ");
            JTextField nameField = new JTextField();
            JLabel teamLabel = new JLabel("Team: ");
            JTextField teamField = new JTextField();

            JButton sbm = new JButton("Submit");
            JButton gb = new JButton("Go back");

            setFindMinutesPanel(findMinutesPanel, nameLabel, nameField, teamLabel, teamField, sbm, gb);
            displayInitiatior(findMinutesFrame, findMinutesPanel, "Find minutes played by a player");
            handleFindMinutesOperation(findMinutesFrame, nameField, teamField, sbm, gb);
        }

        // EFFECTS: sets up the GUI elements of the Find Minutes Played panel
        private void setFindMinutesPanel(JPanel panel, JLabel nameLabel, JTextField nameField, JLabel teamLabel,
                                         JTextField teamField, JButton sbm, JButton gb) {
            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(teamLabel);
            panel.add(teamField);
            panel.add(sbm);
            panel.add(gb);
        }

        //MODIFIES: playerList
        //EFFECTS: carries out the changes as dictated by the user input
        private void handleFindMinutesOperation(JFrame findGoalsFrame, JTextField nameField, JTextField teamField,
                                                JButton sbm, JButton gb) {
            sbm.addActionListener(e -> {
                try {
                    String result = playerList.minutesPlayed(nameField.getText().trim(), teamField.getText().trim());
                    JOptionPane.showMessageDialog(null, nameField.getText().trim()
                            + " has played " + result + " minutes");
                    returnToOptionMenu(findGoalsFrame);
                } catch (InvalidPlayerException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Entry");
                }
            });
            gb.addActionListener(e -> {
                returnToOptionMenu(findGoalsFrame);
            });
        }
    }


    // EFFECTS: closes the given frame, and sets optionFrame to visible, can be operated on any existing frame
    private void returnToOptionMenu(JFrame closeFrame) {
        closeFrame.dispose();
        optionsFrame.setVisible(true);
    }


    // MODIFIES: this
    // EFFECTS: runs and processes the user input
    private void runManager() {
        boolean keepGoing = true;
        String command = null;

        init();
        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase(Locale.ROOT);

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nThanks");
    }

    // EFFECTS: this
    // initializes a premier league stat database as an empty ListOfPlayer
    private void init() {
        playerList = new ListOfPlayer("Test");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays the options menu to the user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Register a player");
        System.out.println("\tg -> Add to goal tally");
        System.out.println("\tm -> Add to minutes played");
        System.out.println("\tt -> Transfer");
        System.out.println("\tf -> Find Top Goal Scorer so far");
        System.out.println("\tb -> Find goals scored by a player");
        System.out.println("\tc -> Find minutes played by a player");
        System.out.println("\tp -> Find players playing in a team");
        System.out.println("\ts -> Save this list of player");
        System.out.println("\tl -> Load your list of player");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes the user command
    private void processCommand(String command) {
        if (command.equals("g")) {
            doPlayerGoalAddition();
        } else if (command.equals("m")) {
            doMinuteAddition();
        } else if (command.equals("t")) {
            doTransfer();
        } else if (command.equals("f")) {
            findTopScorer();
        } else if (command.equals("p")) {
            procureTeam();
        } else if (command.equals("a")) {
            registerPlayer();
        } else if (command.equals("b")) {
            giveGoalScored();
        } else if (command.equals("c")) {
            giveMinutesPlayed();
        } else if (command.equals("s")) {
            saveListOfPlayer();
        } else if (command.equals("l")) {
            loadListOfPlayer();
        } else {
            System.out.println("Invalid Selection");
        }
    }

    // this
    // adds the given amount of goals to the goal tally of the given player
    private void doPlayerGoalAddition() {
        System.out.println("Player name?");
        String playerName = input.next().trim();
        System.out.println("Player team?");
        String playerTeam = input.next().trim();
        System.out.println("Goals?");
        int goals = Integer.parseInt(input.next());

        Boolean result = playerList.tallyGoals(playerName, playerTeam, goals);
        if (!result) {
            System.out.println("Invalid player or team");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the given number of minutes to the total minutes played by a player
    private void doMinuteAddition() {
        System.out.println("Which player?");
        String playerName = input.next().trim();
        System.out.println("Which team?");
        String playerTeam = input.next().trim();
        System.out.println("Minutes played:");
        Double minutesPlayed = input.nextDouble();

        Boolean result = playerList.findPlayerAndAddMinutes(playerName, playerTeam, minutesPlayed);
        if (!result) {
            System.out.println("Invalid player or team");
        }
    }

    // MODIFIES: this
    // EFFECTS: transfers a given player from their current team to the other
    private void doTransfer() {
        System.out.println("Which player?");
        String playerName = input.next().trim();
        System.out.println("Which team?");
        String currentTeam = input.next().trim();
        System.out.println("To?");
        String team = input.next().trim();


        Boolean result = playerList.transferPlayer(playerName, currentTeam, team);
        if (!result) {
            System.out.println("Invalid player or team");

        } else {
            System.out.println(playerName + " " + "has been transferred to" + " " + team);

        }

    }


    // EFFECTS: gives the name of the top scorer of the league
    private void findTopScorer() {
        System.out.println("The league's top scorer is  " + playerList.topScorer());
    }

    // EFFECTS: produces a list of all the players playing for a particular team
    private void procureTeam() {
        System.out.println("Which team?");
        String team = input.next().trim();
        ArrayList<String> result = playerList.findTeam(team);
        for (String s : result) {
            System.out.println(s);
        }
    }

    // MODIFIES: this
    // EFFECTS: registers the player onto the list of players
    private void registerPlayer() {
        System.out.println("Player Name");
        String name = input.next().trim();
        System.out.println("Player Team");
        String team = input.next().trim();

        playerList.register(name, team);
        System.out.println(name + " " + "has been registered");
    }

    // EFFECTS: gives the number of goals scored by a given player
    private void giveGoalScored() {
        System.out.println("Player Name?");
        String name = input.next().trim();
        System.out.println("Team?");
        String team = input.next().trim();

        try {
            System.out.println(name + " has scored " + playerList.goalScore(name, team) + " goals ");
        } catch (InvalidPlayerException e) {
            System.out.println("Invalid player or team");
        }

    }

    // EFFECTS: gives the number of minutes played by a given player
    private void giveMinutesPlayed() {
        System.out.println("Player Name");
        String name = input.next().trim();
        System.out.println("Team?");
        String team = input.next().trim();

        try {
            System.out.println(name + " " + "has played" + " " + playerList.minutesPlayed(name, team) + " "
                    + "minutes");
        } catch (InvalidPlayerException e) {
            System.out.println("Invalid player or team");
        }

    }

    // EFFECTS: saves the workroom to file
    private void saveListOfPlayer() {
        try {
            jsonWriter.open();
            jsonWriter.write(playerList);
            jsonWriter.close();
            System.out.println("Saved " + "playerList" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private void loadListOfPlayer() {
        try {
            playerList = jsonReader.read();
            System.out.println("Loaded " + "playerList" + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


    // EFFECTS: this method sets up the basic structure of a GUI window, and places panels appropriately with
    //          with respect to the frame, and sets various aspects of the frame.
    private void displayInitiatior(JFrame frame, JPanel panel, String toolBarTitle) {
        panel.setBorder(new EmptyBorder(10, 50, 10, 50));
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setTitle(toolBarTitle);
        frame.setVisible(true);
    }

    // EFFECTS: produces a list of all players and their stats in the form of a single string
    private StringBuilder getStringList() {
        StringBuilder outputString = new StringBuilder();
        List<Player> result = playerList.getPlayers();
        for (Player p : result) {
            outputString.append(p.getName()).append("\n");
            outputString.append(p.getTeam()).append("\n");
            outputString.append(p.getGoalScored()).append(" goals ").append("\n");
            outputString.append(p.getMinutesPlayed()).append(" minutes ").append("\n");
            outputString.append("\n");
        }
        return outputString;
    }

    // EFFECTS: produces a list of in the form of a single  string the name of all players in a particular team
    private StringBuilder getPlayersInATeam(String team) {
        StringBuilder outputString = new StringBuilder();
        List<String> result = playerList.findTeam(team);
        for (String s : result) {
            outputString.append(s).append("\n");
        }
        return outputString;
    }
}




