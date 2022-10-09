package shinyhunttracker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Vector;

public class HuntController {
    Stage huntControls = new Stage();
    VBox huntControlsVBox = new VBox();
    PreviouslyCaught previousCatches = new PreviouslyCaught(0);
    double xOffset, yOffset;

    Vector<HuntWindow> windowsList = new Vector<>();

    Stage keyBindingSettingsStage = new Stage();

    /**
     * Creates the Hunt Controller and
     * opens hunts from previous session
     */
    public HuntController(){
        //Initial Hunt Controller setup
        huntControls.setTitle("Hunt Controller");
        huntControls.initStyle(StageStyle.UNDECORATED);
        huntControls.setResizable(false);
        huntControlsVBox.setAlignment(Pos.CENTER);
        huntControlsVBox.setSpacing(5);

        //replacement for normal window control buttons, exit and minimize
        HBox windowControls = new HBox();
        Button exit = new Button("X");
        Button minimize = new Button("_");
        windowControls.getChildren().addAll(minimize, exit);
        windowControls.setAlignment(Pos.CENTER_RIGHT);
        windowControls.setSpacing(5);
        windowControls.setPadding(new Insets(5,5,0,5));

        //add hunt and general settings buttons
        Button addHunt = new Button("+");
        MenuButton masterSettings = new MenuButton("O");
        MenuItem keyBinding = new MenuItem("Key Bind Settings");
        MenuItem previouslyCaught = new MenuItem("Previously Caught Window Settings");
        masterSettings.getItems().addAll(keyBinding, previouslyCaught);

        BorderPane masterButtonsPane = new BorderPane();
        masterButtonsPane.setRight(addHunt);
        masterButtonsPane.setLeft(masterSettings);
        masterButtonsPane.setPadding(new Insets(0,5,5,5));

        BorderPane huntControlsLayout = new BorderPane();
        huntControlsLayout.setTop(windowControls);
        huntControlsLayout.setCenter(huntControlsVBox);
        huntControlsLayout.setBottom(masterButtonsPane);

        Scene huntControlsScene = new Scene(huntControlsLayout, 350, 75);
        huntControls.setScene(huntControlsScene);
        huntControls.show();

        //Check to see if there were hunts open when the hunt controller was last closed
        JSONParser jsonParser = new JSONParser();
        try {
            //reads the number of hunts that were open when the program was last closed
            FileInputStream reader = new FileInputStream("SaveData/previousSession.dat");
            DataInputStream previousSessionData = new DataInputStream(reader);
            int previousHuntsNum = previousSessionData.read();

            //Read JSON file
            Object obj = jsonParser.parse(new FileReader("SaveData/previousHunts.json"));
            JSONArray huntList = (JSONArray) obj;

            //Load the last n hunts in the json
            if(huntList != null)
                for(int i = 1; i <= previousHuntsNum; i++)
                    SaveData.loadHunt(huntList.size() - i, this);
        }catch (IOException | ParseException ignored) {

        }

        exit.setOnAction(e -> huntControls.fireEvent(new WindowEvent(huntControls, WindowEvent.WINDOW_CLOSE_REQUEST)));
        minimize.setOnAction(e -> huntControls.setIconified(true));

        keyBinding.setOnAction(e -> keyBindingSettings());
        previouslyCaught.setOnAction(e -> previousCatches.previouslyCaughtPokemonSettings());

        //Listener for KeyBinds
        huntControlsScene.setOnKeyPressed(e -> {
            for(HuntWindow i: windowsList)
                if (i.getKeyBinding() == e.getCode())
                    i.incrementEncounters();
        });

        //Opens pop up for Selection Window
        addHunt.setOnAction(e -> {
            //check save data file for previous saves
            //if anything is found, ask the user if they would like to start a new hunt or a previous one
            try (FileReader reader = new FileReader("SaveData/previousHunts.json")) {
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                JSONArray huntList = (JSONArray) obj;

                //If there are no previous hunts, it goes straight to selection page
                if(huntList.size() > 0)
                    newOrOld();
            }catch(IOException | ParseException f){
                HuntSelection.createHuntSelection(this);
            }
        });

        //Make window draggable
        huntControlsScene.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        huntControlsScene.setOnMouseDragged(e -> {
            huntControls.setX(e.getScreenX() - xOffset);
            huntControls.setY(e.getScreenY() - yOffset);
        });

        //Makes sure to save all currently open hunts before closing
        huntControls.setOnCloseRequest(e -> {
            //saves current hunts to open next time the program is started
            try {
                OutputStream out = new FileOutputStream("SaveData/previousSession.dat");
                out.write(windowsList.size());
                out.close();
            }catch (IOException f){
                f.printStackTrace();
            }

            //Close all possibly open windows
            keyBindingSettingsStage.close();
            previousCatches.getStage().close();
            previousCatches.getSettingsStage().close();

            //save and close all currently open hunts
            while(windowsList.size() > 0) {
                SaveData.saveHunt(windowsList.lastElement());
                windowsList.lastElement().closeHuntWindow();
                windowsList.remove(windowsList.lastElement());
            }

            System.exit(0);
        });
    }

    /**
     * Adds a new HuntWindow to the windowsList and
     * adds appropriate elements to controller with new hunt's information
     * @param newWindow HuntWindow with new hunt information
     */
    public void addHunt(HuntWindow newWindow){
        //Set huntNum to first available
        int huntNum = 0;
        for(; huntNum < windowsList.size(); ++huntNum)
            if(windowsList.get(huntNum).getHuntNumber() != huntNum + 1)
                break;
        newWindow.setHuntNumber(huntNum + 1);
        windowsList.add(huntNum, newWindow);

        //Add hunt Labels to controller
        newWindow.getStage().setTitle("Hunt " + newWindow.getHuntNumber());

        //Hunt Information to add to controller vbox
        HBox huntInformationHBox = new HBox();
        huntInformationHBox.setAlignment(Pos.CENTER);
        huntInformationHBox.setSpacing(10);

        Button exitHuntButton = new Button("X");
        exitHuntButton.setFocusTraversable(false);

        Label huntNumberLabel = new Label(String.valueOf(newWindow.getHuntNumber()));

        Button encountersButton = new Button("+");

        Label encounterLabel = new Label();
        encounterLabel.textProperty().bind(newWindow.encounterProperty().asString());

        Label nameLabel = new Label(newWindow.getPokemon().getName());

        Button caughtButton = new Button("C");

        StackPane windowPopout = new StackPane();
        Button popOutButton = new Button("P");
        Button windowSettingsButton = new Button("X");
        windowSettingsButton.setVisible(false);
        windowPopout.getChildren().addAll(popOutButton, windowSettingsButton);

        MenuButton settingsButton = new MenuButton("O");
        MenuItem increment= new MenuItem("Change Increment");
        MenuItem resetEncounters = new MenuItem("Fail");
        MenuItem phaseHunt = new MenuItem("Phase");
        MenuItem resetCombo = new MenuItem("Reset Combo");
        MenuItem saveHunt = new MenuItem("Save Hunt");
        MenuItem DVTable = new MenuItem("DV Table");
        settingsButton.getItems().addAll(increment, resetEncounters, phaseHunt, resetCombo, saveHunt);
        if(newWindow.getGame().getGeneration() == 1)
            settingsButton.getItems().add(DVTable);

        Button helpButton = new Button("H");

        huntInformationHBox.getChildren().addAll(exitHuntButton, huntNumberLabel, encountersButton, nameLabel, encounterLabel, caughtButton, windowPopout, settingsButton, helpButton);
        huntControlsVBox.getChildren().add(newWindow.getHuntNumber() - 1, huntInformationHBox);
        huntControls.setHeight(huntControls.getHeight() + 25);

        //Set keybinds
        try (FileReader reader = new FileReader("SaveData/keybinds.json")) {
            JSONParser jsonParser = new JSONParser();
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray keybindList = (JSONArray) obj;

            newWindow.setKeybind(KeyCode.valueOf(keybindList.get(newWindow.getHuntNumber() - 1).toString()));
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }

        //update keybind window if it is open
        if(keyBindingSettingsStage.isShowing())
            keyBindingSettings();

        //since the search level or total encounters can change between uses, this value needs to be captured after every startup
        if (newWindow.getMethod().getName().compareTo("DexNav") == 0 || newWindow.getMethod().getName().compareTo("Total Encounters") == 0) {
            newWindow.promptPreviousEncounters();
        }

        exitHuntButton.setOnAction(e -> {
            SaveData.saveHunt(windowsList.lastElement());
            newWindow.closeHuntWindow();
            huntControlsVBox.getChildren().remove(huntInformationHBox);
            windowsList.remove(newWindow);
        });

        encountersButton.setOnAction(e -> newWindow.incrementEncounters());

        caughtButton.setOnAction(e -> {
            newWindow.pokemonCaught();
            huntControlsVBox.getChildren().remove(huntInformationHBox);
            windowsList.remove(newWindow);
            /*if(previousCatches.getStage().isShowing())
                previousCatches.refreshPreviouslyCaughtPokemon();*/
        });

        popOutButton.setOnAction(e -> {
            popOutButton.setVisible(false);
            windowSettingsButton.setVisible(true);
            if(newWindow.getScene().getChildren().size() == 0)
                newWindow.createHuntWindow();
            else
                newWindow.getStage().show();
        });

        increment.setOnAction(e -> newWindow.changeIncrement());
        resetEncounters.setOnAction(e -> newWindow.resetEncounters());
        phaseHunt.setOnAction(e -> {
            //newWindow.setPreviouslyCaughtWindow(previousCatches);
            newWindow.phaseHunt();
        });
        resetCombo.setOnAction(e -> newWindow.resetCombo());
        saveHunt.setOnAction(e -> SaveData.saveHunt(newWindow));
        DVTable.setOnAction(e -> generateDVTable(newWindow.getPokemon()));

        windowSettingsButton.setOnAction(e -> newWindow.CustomizeHuntWindow());

        helpButton.setOnAction(e -> {

        });

        newWindow.getStage().setOnCloseRequest(e -> {
            e.consume();
            popOutButton.setVisible(true);
            windowSettingsButton.setVisible(false);
            newWindow.closeHuntWindow();
        });
    }

    /**
     * Prompts the user if they would like to continue an old hunt, or start a new one
     */
    public void newOrOld(){
        Stage windowStage = new Stage();
        Stage loadStage = new Stage();

        Label prompt1 = new Label("Would you like to continue a previous hunt or");
        Label prompt2 = new Label("start a new one?");
        Button continuePrevious = new Button("Continue Previous Hunt");
        Button newHunt = new Button("Start New Hunt");

        VBox loadLayout = new VBox();
        loadLayout.getChildren().addAll(prompt1, prompt2, continuePrevious, newHunt);
        loadLayout.setSpacing(10);
        loadLayout.setAlignment(Pos.CENTER);

        Scene loadScene = new Scene(loadLayout, 275, 150);
        loadStage.setTitle("Load previous save");
        loadStage.setResizable(false);
        loadStage.setScene(loadScene);
        loadStage.show();

        //show the previously created Selection Page Window in order of last used
        newHunt.setOnAction(e -> {
            //creates selection page window
            HuntSelection.createHuntSelection(this);

            //closes prompt window
            windowStage.close();
            loadStage.close();
        });

        //if they would like to continue, show a list of the previous hunts found on the file
        continuePrevious.setOnAction(e -> {
            windowStage.setTitle("Select a previous hunt");
            TreeView<String> previousHuntsView = new TreeView<>();
            TreeItem<String> previousHuntsRoot = new TreeItem<>();

            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                JSONArray huntList = (JSONArray) obj;

                for(int i = huntList.size() - 1; i >= 0; i--){
                    JSONObject huntData = (JSONObject) huntList.get(i);
                    Pokemon pokemon = new Pokemon(Integer.parseInt(huntData.get("pokemon_id").toString()));
                    Game game = new Game(Integer.parseInt(huntData.get("game").toString()));
                    Method method = new Method(Integer.parseInt(huntData.get("method").toString()));
                    String encounters = huntData.get("encounters").toString();

                    TreeItem<String> item = new TreeItem<>((huntList.size() - i) + ") " + pokemon.getName() + " | " + game.getName() + " | " + method.getName() + " | " + encounters + " encounters");
                    previousHuntsRoot.getChildren().add(item);
                }

                //skip selection window and go straight to hunt page
                previousHuntsView.getSelectionModel().selectedItemProperty()
                        .addListener((v, oldValue, newValue) -> {
                            //Reads the hunt number from the beginning of the string
                            int index = Integer.parseInt(newValue.toString().substring(18, newValue.toString().indexOf(')')));
                            SaveData.loadHunt(huntList.size() - index, this);
                            windowStage.close();
                        });
            }catch (IOException | ParseException f) {
                f.printStackTrace();
            }

            previousHuntsView.setRoot(previousHuntsRoot);
            previousHuntsView.setShowRoot(false);

            VBox previousHuntsLayout = new VBox();
            previousHuntsLayout.getChildren().addAll(previousHuntsView);

            Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
            windowStage.setScene(previousHuntsScene);
            windowStage.show();
            loadStage.close();
        });
    }

    /**
     * Pop up listing the keys that the hunts are bound too and allows the user to change them
     */
    public void keyBindingSettings(){
        keyBindingSettingsStage.setTitle("Key Bindings");

        //Setup layout
        VBox keyBindingsLayout = new VBox();
        keyBindingsLayout.setAlignment(Pos.CENTER);
        keyBindingsLayout.setSpacing(10);
        keyBindingsLayout.setPadding(new Insets(10,0,50,75));

        try(FileReader reader = new FileReader("SaveData/keyBinds.json")){
            JSONParser jsonParser = new JSONParser();

            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray keyBindsList = (JSONArray) obj;

            //temporarily stores keys in order
            Vector<KeyCode> keyBindsTemp = new Vector<>();
            for (int i = 0; i < windowsList.lastElement().getHuntNumber(); i++) {
                keyBindsTemp.add(KeyCode.valueOf((String) keyBindsList.get(i)));

                //adds text-field and label to layout
                HBox windowSettings = new HBox();
                windowSettings.setAlignment(Pos.CENTER);
                windowSettings.setSpacing(10);
                Label huntWindowLabel = new Label("Hunt " + (i + 1));
                TextField keyField = new TextField();
                keyField.setText(String.valueOf(keyBindsList.get(i)));
                keyField.setEditable(false);
                keyField.setMaxWidth(100);
                windowSettings.getChildren().addAll(huntWindowLabel, keyField);
                keyBindingsLayout.getChildren().add(windowSettings);

                //replaces the keybind in the temporary list
                int index = i;
                keyField.setOnKeyPressed(f -> {
                    keyField.setText(f.getCode().toString());
                    keyBindsTemp.set(index, f.getCode());
                });
            }

            //Allows user to save changes and closes window
            Button apply = new Button("Apply");
            keyBindingsLayout.getChildren().add(apply);

            apply.setOnAction(e->{
                try {
                    //updates the JSONArray updates the currently open hunts with the new binds
                    for(int i = 0; i < keyBindsTemp.size(); i++) {
                        keyBindsList.set(i, keyBindsTemp.get(i).toString());
                        if(windowsList.get(i).getHuntNumber() == i + 1)
                            windowsList.get(i).setKeybind(keyBindsTemp.get(i));
                    }

                    //Write to file
                    FileWriter file = new FileWriter("SaveData/keyBinds.json");
                    file.write(keyBindsList.toJSONString());
                    file.close();
                }catch(IOException f){
                    f.printStackTrace();
                }
                keyBindingSettingsStage.close();
            });
        }catch(IOException | ParseException f){
            f.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane(keyBindingsLayout);
        Scene keyBindingScene = new Scene(scrollPane, 250, 500);
        keyBindingSettingsStage.setScene(keyBindingScene);
        keyBindingSettingsStage.show();
    }

    /**
     * Creates the window that displays the projected stats with shiny DVs
     * @param selectedPokemon pokemon that is going to have calculated IVs
     */
    public void generateDVTable(Pokemon selectedPokemon){
        //Select level between 1-100
        ComboBox<Integer> levelSelect = new ComboBox<>();
        for(int i = 1; i <= 100; i++)
            levelSelect.getItems().add(i);
        levelSelect.getSelectionModel().select(49);

        //Labels for each stat
        VBox health = new VBox();
        health.setAlignment(Pos.TOP_CENTER);
        Label healthLabel = new Label("Health");
        Label healthifShiny = new Label();
        health.getChildren().addAll(healthLabel, healthifShiny);

        VBox attack = new VBox();
        attack.setAlignment(Pos.TOP_CENTER);
        Label attackLabel = new Label("Attack");
        Label attackifShiny = new Label();
        attack.getChildren().addAll(attackLabel, attackifShiny);

        VBox defense = new VBox();
        defense.setAlignment(Pos.TOP_CENTER);
        Label defenseLabel = new Label("Defense");
        Label defenseifShiny = new Label();
        defense.getChildren().addAll(defenseLabel, defenseifShiny);

        VBox speed = new VBox();
        speed.setAlignment(Pos.TOP_CENTER);
        Label speedLabel = new Label("Speed");
        Label speedifShiny = new Label();
        speed.getChildren().addAll(speedLabel, speedifShiny);

        VBox special = new VBox();
        special.setAlignment(Pos.TOP_CENTER);
        Label specialLabel = new Label("Special");
        Label specialifShiny = new Label();
        special.getChildren().addAll(specialLabel, specialifShiny);

        updateStatLabels(healthifShiny, attackifShiny, defenseifShiny, speedifShiny, specialifShiny, 1, selectedPokemon);

        HBox statTable = new HBox();
        statTable.setAlignment(Pos.TOP_CENTER);
        statTable.setSpacing(10);
        statTable.getChildren().addAll(health, attack, defense, speed, special);

        VBox DVTableLayout = new VBox();
        DVTableLayout.setAlignment(Pos.CENTER);
        DVTableLayout.getChildren().addAll(levelSelect, statTable);

        Stage DVTableStage = new Stage();
        Scene DVTableScene = new Scene(DVTableLayout, 500, 100);
        DVTableStage.setScene(DVTableScene);
        DVTableStage.setTitle(selectedPokemon.getName() + " Shiny DV Table");

        DVTableStage.show();

        //Updates labels with projected stats with given level
        levelSelect.setOnAction(e -> {
            int level = levelSelect.getSelectionModel().getSelectedItem();
            updateStatLabels(healthifShiny, attackifShiny, defenseifShiny, speedifShiny, specialifShiny, level, selectedPokemon);
        });
    }

    /**
     * Updates stat labels with projected shiny stats
     * @param health Health Label
     * @param attack Attack Label
     * @param defense Defense Label
     * @param speed Speed Label
     * @param special Special Label
     * @param level Pokemon Level
     * @param selectedPokemon Pokemon
     */
    public void updateStatLabels(Label health, Label attack, Label defense, Label speed, Label special, int level, Pokemon selectedPokemon){
        int speedStat = selectedPokemon.getBase()[0];
        int healthStat = selectedPokemon.getBase()[1];
        int specialStat = selectedPokemon.getBase()[2];
        int attackStat = selectedPokemon.getBase()[3];
        int defenseStat = selectedPokemon.getBase()[4];

        speed.setText(calculateShinyStat(speedStat, 10, level, false) + "");

        health.setText(calculateShinyStat(healthStat, 0,level, true) + "\n"
                     + calculateShinyStat(healthStat, 8,level, true));

        special.setText(calculateShinyStat(specialStat, 10, level, false) + "");

        attack.setText(calculateShinyStat(attackStat, 2, level, false) + "\n"
                     + calculateShinyStat(attackStat, 3, level, false) + "\n"
                     + calculateShinyStat(attackStat, 6, level, false) + "\n"
                     + calculateShinyStat(attackStat, 7, level, false) + "\n"
                     + calculateShinyStat(attackStat, 10, level, false) + "\n"
                     + calculateShinyStat(attackStat, 11, level, false) + "\n"
                     + calculateShinyStat(attackStat, 14, level, false) + "\n"
                     + calculateShinyStat(attackStat, 15, level, false));

        defense.setText(calculateShinyStat(defenseStat, 10, level, false) + "");
    }

    /**
     * Calculates projected stat at given level and returns
     * @param base Base Stat of selected pokemon
     * @param dv Projected DV to calculate
     * @param level Pokemon level
     * @param isHealth Changes calculation if the stat is the health stat
     * @return calculated Stat
     */
    public int calculateShinyStat(int base, int dv, int level, boolean isHealth){
        int health = isHealth ? 1 : 0;
        return (((base + dv) * 2 * level) / 100) + (level * health) + 5 + (5 * health);
    }
}