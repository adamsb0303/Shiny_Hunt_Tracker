package shinyhunttracker;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Vector;

public class HuntController {
    static Stage huntControls = new Stage();
    static VBox huntControlsVBox = new VBox();
    static double xOffset, yOffset;

    static ObservableList<Pokemon> pokedex = FXCollections.observableArrayList();

    static Vector<HuntWindow> windowsList = new Vector<>();

    static Stage keyBindingSettingsStage = new Stage();

    /**
     * Creates the Hunt Controller and
     * opens hunts from previous session
     */
    public static void createHuntController(){
        //Initial Hunt Controller setup
        huntControls.setTitle("Hunt Controller");
        huntControls.initStyle(StageStyle.UNDECORATED);
        huntControls.setResizable(false);
        huntControlsVBox.setAlignment(Pos.CENTER);
        huntControlsVBox.setSpacing(10);
        huntControlsVBox.setPadding(new Insets(10, 15, 10, 15));

        //replacement for normal window control buttons, exit and minimize
        HBox windowControls = new HBox();
        Button exit = new Button("X");
        exit.setMinSize(25, 25);
        Button minimize = new Button("_");
        minimize.setMinSize(25, 25);
        windowControls.getChildren().addAll(minimize, exit);
        windowControls.setAlignment(Pos.CENTER_RIGHT);
        windowControls.setSpacing(5);
        windowControls.setPadding(new Insets(5,5,0,5));

        //add hunt and general settings buttons
        Button addHunt = new Button("+");
        addHunt.setMinSize(25, 25);
        MenuButton masterSettings = new MenuButton("O");
        masterSettings.setMinSize(30, 25);
        MenuItem editSavedHunts = new MenuItem("Edit Saved Hunts");
        MenuItem keyBinding = new MenuItem("Key Bind Settings");
        MenuItem previouslyCaught = new MenuItem("Previously Caught Window Settings");
        masterSettings.getItems().addAll(editSavedHunts, keyBinding, previouslyCaught);

        BorderPane masterButtonsPane = new BorderPane();
        masterButtonsPane.setRight(addHunt);
        masterButtonsPane.setLeft(masterSettings);
        masterButtonsPane.setPadding(new Insets(0,5,5,5));

        BorderPane huntControlsLayout = new BorderPane();
        huntControlsLayout.setTop(windowControls);
        huntControlsLayout.setCenter(huntControlsVBox);
        huntControlsLayout.setBottom(masterButtonsPane);
        huntControlsLayout.setId("background");

        Scene huntControlsScene = new Scene(huntControlsLayout, 405, 75);
        huntControlsScene.getStylesheets().add("file:shinyTracker.css");
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
            if(huntList.size() != 0)
                for(int i = 1; i <= previousHuntsNum; i++)
                    SaveData.loadHunt(huntList.size() - i);
        }catch (IOException | ParseException ignored) {

        }

        exit.setOnAction(e -> huntControls.fireEvent(new WindowEvent(huntControls, WindowEvent.WINDOW_CLOSE_REQUEST)));
        minimize.setOnAction(e -> huntControls.setIconified(true));

        editSavedHunts.setOnAction(e -> editSavedHuntsWindow());
        keyBinding.setOnAction(e -> keyBindingSettings());
        previouslyCaught.setOnAction(e -> PreviouslyCaught.previouslyCaughtPokemonSettings());

        //Listener for KeyBinds
        huntControlsScene.setOnKeyPressed(e -> {
            for(int i = windowsList.size() - 1; i >= 0 ; i--){
                if (windowsList.get(i).getKeyBinding() == e.getCode())
                    windowsList.get(i).incrementEncounters();
                SaveData.saveHunt(windowsList.get(i));
            }
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
                if(huntList.size() > 0 && windowsList.size() < huntList.size())
                    newOrOld();
                else
                    HuntSelection.createHuntSelection();
            }catch(IOException | ParseException f){
                HuntSelection.createHuntSelection();
            }
        });

        //Make window draggable
        makeDraggable(huntControlsScene);

        //Makes sure to save all currently open hunts before closing
        huntControls.setOnCloseRequest(e -> {
            while(windowsList.size() > 0)
                windowsList.remove(windowsList.lastElement());

            //Close all possibly open windows
            keyBindingSettingsStage.close();
            PreviouslyCaught.close();

            System.exit(0);
        });
    }

    /**
     * Adds a new HuntWindow to the windowsList and
     * adds appropriate elements to controller with new hunt's information
     * @param newWindow HuntWindow with new hunt information
     */
    public static void addHunt(HuntWindow newWindow){
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
        huntInformationHBox.setSpacing(5);
        huntInformationHBox.setMinWidth(325);

        Button exitHuntButton = new Button("X");
        exitHuntButton.setFocusTraversable(false);
        exitHuntButton.setMinSize(25, 25);

        Label huntNumberLabel = new Label(String.valueOf(newWindow.getHuntNumber()));
        huntNumberLabel.setAlignment(Pos.CENTER);
        huntNumberLabel.setMinWidth(15);

        Button encountersButton = new Button("+");
        encountersButton.setMinSize(25, 25);

        Label nameLabel = new Label(newWindow.getPokemon().getName());
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMinWidth(75);
        nameLabel.setMaxWidth(75);

        Label encounterLabel = new Label();
        encounterLabel.setAlignment(Pos.CENTER);
        encounterLabel.textProperty().bind(Bindings.createStringBinding(() -> String.format("%,d", newWindow.encounterProperty().getValue()), newWindow.encounterProperty()));
        encounterLabel.setMinWidth(75);
        encounterLabel.setMaxWidth(75);

        Button caughtButton = new Button("C");
        caughtButton.setMinSize(25, 25);

        StackPane windowPopout = new StackPane();
        Button popOutButton = new Button("P");
        popOutButton.setMinSize(25, 25);
        Button windowSettingsButton = new Button("X");
        windowSettingsButton.setMinSize(25, 25);
        windowSettingsButton.setVisible(false);
        windowPopout.getChildren().addAll(popOutButton, windowSettingsButton);

        MenuButton settingsButton = new MenuButton("O");
        settingsButton.setMinSize(30, 25);
        MenuItem increment= new MenuItem("Change Increment");
        MenuItem resetEncounters = new MenuItem("Fail");
        MenuItem phaseHunt = new MenuItem("Phase");
        MenuItem DVTable = new MenuItem("DV Table");

        settingsButton.getItems().addAll(increment, resetEncounters, phaseHunt);
        if(newWindow.getGame().getGeneration() == 1)
            settingsButton.getItems().add(DVTable);

        Button helpButton = new Button("?");
        helpButton.setMinSize(25, 25);

        huntInformationHBox.getChildren().addAll(exitHuntButton, huntNumberLabel, encountersButton, nameLabel, encounterLabel, caughtButton, windowPopout, settingsButton, helpButton);
        huntControlsVBox.getChildren().add(newWindow.getHuntNumber() - 1, huntInformationHBox);
        huntControls.setHeight(huntControls.getHeight() + 35);

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

        exitHuntButton.setOnAction(e -> {
            updatePreviousSessionDat(-1);
            newWindow.closeHuntWindow();
            huntControlsVBox.getChildren().remove(huntInformationHBox);
            windowsList.remove(newWindow);
            huntControls.setHeight(huntControls.getHeight() - 35);

            for(int i = windowsList.size() - 1; i >= 0 ; i--)
                SaveData.saveHunt(windowsList.get(i));
        });

        encountersButton.setOnAction(e -> {
            newWindow.incrementEncounters();
            for(int i = windowsList.size() - 1; i >= 0 ; i--)
                SaveData.saveHunt(windowsList.get(i));
        });

        caughtButton.setOnAction(e -> {
            updatePreviousSessionDat(-1);
            huntControls.setHeight(huntControls.getHeight() - 35);
            newWindow.pokemonCaught();
            huntControlsVBox.getChildren().remove(huntInformationHBox);
            windowsList.remove(newWindow);
        });

        popOutButton.setOnAction(e -> {
            popOutButton.setVisible(false);
            windowSettingsButton.setVisible(true);
            if(newWindow.getScene().getChildren().size() == 0)
                newWindow.createHuntWindow();
            else
                newWindow.getStage().show();
        });

        increment.setOnAction(e -> {
            TextInputDialog changeIncrementDialog = new TextInputDialog(String.valueOf(newWindow.getIncrement()));
            changeIncrementDialog.setTitle("Increment Settings");
            changeIncrementDialog.setHeaderText("Increment encounters by: ");
            changeIncrementDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
            makeDraggable(changeIncrementDialog.getDialogPane().getScene());
            changeIncrementDialog.initStyle(StageStyle.UNDECORATED);
            changeIncrementDialog.showAndWait().ifPresent(response -> {
                try{
                    newWindow.setIncrement(Integer.parseInt(response));
                }catch(NumberFormatException ignored){

                }
            });
        });
        resetEncounters.setOnAction(e -> newWindow.resetEncounters());
        phaseHunt.setOnAction(e -> {
            if(pokedex.size() == 0) {
                try (FileReader reader = new FileReader("GameData/pokemon.json")) {
                    JSONParser jsonParser = new JSONParser();
                    JSONArray pokemonListJSON = (JSONArray) jsonParser.parse(reader);

                    for (int i = 0; i < pokemonListJSON.size(); i++)
                        pokedex.add(new Pokemon((JSONObject) pokemonListJSON.get(i), i));
                } catch (IOException | ParseException f) {
                    f.printStackTrace();
                }
            }
            ChoiceDialog<Pokemon> phaseDialog = new ChoiceDialog<>(pokedex.get(0), pokedex);
            phaseDialog.setTitle("Phase Hunt");
            phaseDialog.setHeaderText("Phased Pokemon: ");
            phaseDialog.showAndWait().ifPresent(response -> newWindow.phaseHunt(response.getDexNumber()));
        });
        DVTable.setOnAction(e -> generateDVTable(newWindow.getPokemon()));

        windowSettingsButton.setOnAction(e -> newWindow.customizeHuntWindowSettings());

        helpButton.setOnAction(e -> {
            Alert huntInformation = new Alert(Alert.AlertType.INFORMATION);
            huntInformation.setTitle("Hunt Information");
            huntInformation.setHeaderText(null);
            huntInformation.initStyle(StageStyle.UNDECORATED);
            huntInformation.getDialogPane().getStylesheets().add("file:shinyTracker.css");
            makeDraggable(huntInformation.getDialogPane().getScene());
            huntInformation.setContentText( "Pokemon: " + newWindow.getPokemon().getName() + "\n" +
                                            "Game: " + newWindow.getGame().getName() + "\n" +
                                            "Method: " + newWindow.getMethod().getName() + "\n\n" +
                                            "Method Info: \n" + newWindow.getMethod().getMethodInfo());
            huntInformation.show();
        });

        newWindow.getStage().setOnCloseRequest(e -> {
            e.consume();
            popOutButton.setVisible(true);
            windowSettingsButton.setVisible(false);
            newWindow.closeHuntWindow();
        });
    }

    public static void refreshHunts(){
        int oldHunts = windowsList.size();
        huntControlsVBox.getChildren().clear();
        huntControls.setHeight(75);
        while(windowsList.size() != 0){
            windowsList.lastElement().closeHuntWindow();
            windowsList.remove(windowsList.lastElement());
        }

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
            if(huntList.size() != 0)
                for(int i = 1; i <= previousHuntsNum; i++)
                    SaveData.loadHunt(huntList.size() - i);
        }catch (IOException | ParseException ignored) {

        }
        if(oldHunts < windowsList.size()){
            updatePreviousSessionDat(windowsList.size() - oldHunts);
        }
    }

    /**
     * Prompts the user if they would like to continue an old hunt, or start a new one
     */
    public static void newOrOld(){
        Stage prevHuntsStage = new Stage();
        Stage promptStage = new Stage();

        Label prompt = new Label("Would you like to continue a previous hunt or\nstart a new one?");
        prompt.setTextAlignment(TextAlignment.CENTER);
        Button continuePrevious = new Button("Continue Previous Hunt");
        Button newHunt = new Button("Start New Hunt");

        VBox loadLayout = new VBox();
        loadLayout.setId("background");
        loadLayout.getChildren().addAll(prompt, continuePrevious, newHunt);
        loadLayout.setSpacing(10);
        loadLayout.setAlignment(Pos.CENTER);

        Scene loadScene = new Scene(loadLayout, 275, 150);
        loadScene.getStylesheets().add("file:shinyTracker.css");
        promptStage.setTitle("Load previous save");
        promptStage.setResizable(false);
        promptStage.setScene(loadScene);
        promptStage.show();

        //show the previously created Selection Page Window in order of last used
        newHunt.setOnAction(e -> {
            //creates selection page window
            HuntSelection.createHuntSelection();

            //closes prompt window
            prevHuntsStage.close();
            promptStage.close();
        });

        //if they would like to continue, show a list of the previous hunts found on the file
        continuePrevious.setOnAction(e -> {
            prevHuntsStage.setTitle("Select a previous hunt");
            TreeView<String> previousHuntsView = new TreeView<>();
            TreeItem<String> previousHuntsRoot = new TreeItem<>();

            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
                //Read JSON file
                Object obj = jsonParser.parse(reader);
                JSONArray huntList = (JSONArray) obj;

                for(int i = huntList.size() - 1; i >= 0; i--){
                    JSONObject huntData = (JSONObject) huntList.get(i);
                    boolean newData = true;
                    for(HuntWindow j : windowsList)
                        if(Integer.parseInt(huntData.get("huntID").toString()) == j.getHuntID())
                            newData = false;

                    if(newData) {
                        Pokemon pokemon = new Pokemon(Integer.parseInt(huntData.get("pokemon").toString()));
                        Game game = new Game(Integer.parseInt(huntData.get("game").toString()));
                        Method method = new Method(Integer.parseInt(huntData.get("method").toString()));
                        String encounters = huntData.get("encounters").toString();

                        TreeItem<String> item = new TreeItem<>((huntList.size() - i) + ") " + pokemon.getName() + " | " + game.getName() + " | " + method.getName() + " | " + encounters + " encounters");
                        previousHuntsRoot.getChildren().add(item);
                    }
                }

                //skip selection window and go straight to hunt page
                previousHuntsView.getSelectionModel().selectedItemProperty()
                        .addListener((v, oldValue, newValue) -> {
                            //Reads the hunt number from the beginning of the string
                            int index = Integer.parseInt(newValue.toString().substring(18, newValue.toString().indexOf(')')));
                            updatePreviousSessionDat(1);
                            SaveData.loadHunt(huntList.size() - index);
                            prevHuntsStage.close();
                        });
            }catch (IOException | ParseException f) {
                f.printStackTrace();
            }

            previousHuntsView.setRoot(previousHuntsRoot);
            previousHuntsView.setShowRoot(false);

            VBox previousHuntsLayout = new VBox();
            previousHuntsLayout.getChildren().addAll(previousHuntsView);

            Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
            prevHuntsStage.setScene(previousHuntsScene);
            prevHuntsStage.show();
            promptStage.close();
        });
    }

    /**
     * Pop up listing the keys that the hunts are bound too and allows the user to change them
     */
    public static void keyBindingSettings(){
        keyBindingSettingsStage.setTitle("Key Bindings");

        //Setup layout
        VBox keyBindingsLayout = new VBox();
        keyBindingsLayout.setAlignment(Pos.TOP_CENTER);
        keyBindingsLayout.setSpacing(10);
        keyBindingsLayout.setPadding(new Insets(10,0,0,0));

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

        keyBindingsLayout.setId("background");
        Scene keyBindingScene = new Scene(keyBindingsLayout, 250, 300);
        keyBindingScene.getStylesheets().add("file:shinyTracker.css");
        keyBindingSettingsStage.setScene(keyBindingScene);
        keyBindingSettingsStage.show();
    }

    /**
     * Creates the window that displays the projected stats with shiny DVs
     * @param selectedPokemon pokemon that is going to have calculated IVs
     */
    public static void generateDVTable(Pokemon selectedPokemon){
        //Select level between 1-100
        ComboBox<Integer> levelSelect = new ComboBox<>();
        for(int i = 1; i <= 100; i++)
            levelSelect.getItems().add(i);
        levelSelect.getSelectionModel().select(49);

        //Labels for each stat
        VBox health = new VBox();
        health.setAlignment(Pos.TOP_CENTER);
        Label healthLabel = new Label("Health");
        healthLabel.setUnderline(true);
        Label healthifShiny = new Label();
        health.getChildren().addAll(healthLabel, healthifShiny);

        VBox attack = new VBox();
        attack.setAlignment(Pos.TOP_CENTER);
        Label attackLabel = new Label("Attack");
        attackLabel.setUnderline(true);
        Label attackifShiny = new Label();
        attack.getChildren().addAll(attackLabel, attackifShiny);

        VBox defense = new VBox();
        defense.setAlignment(Pos.TOP_CENTER);
        Label defenseLabel = new Label("Defense");
        defenseLabel.setUnderline(true);
        Label defenseifShiny = new Label();
        defense.getChildren().addAll(defenseLabel, defenseifShiny);

        VBox speed = new VBox();
        speed.setAlignment(Pos.TOP_CENTER);
        Label speedLabel = new Label("Speed");
        speedLabel.setUnderline(true);
        Label speedifShiny = new Label();
        speed.getChildren().addAll(speedLabel, speedifShiny);

        VBox special = new VBox();
        special.setAlignment(Pos.TOP_CENTER);
        Label specialLabel = new Label("Special");
        specialLabel.setUnderline(true);
        Label specialifShiny = new Label();
        special.getChildren().addAll(specialLabel, specialifShiny);

        updateStatLabels(healthifShiny, attackifShiny, defenseifShiny, speedifShiny, specialifShiny, 50, selectedPokemon);

        HBox statTable = new HBox();
        statTable.setAlignment(Pos.TOP_CENTER);
        statTable.setSpacing(10);
        statTable.getChildren().addAll(health, attack, defense, speed, special);

        VBox DVTableLayout = new VBox();
        DVTableLayout.setAlignment(Pos.TOP_CENTER);
        DVTableLayout.setSpacing(10);
        DVTableLayout.setPadding(new Insets(10, 0, 0, 0));
        DVTableLayout.setId("background");
        DVTableLayout.getChildren().addAll(levelSelect, statTable);

        Stage DVTableStage = new Stage();
        Scene DVTableScene = new Scene(DVTableLayout, 300, 200);
        DVTableScene.getStylesheets().add("file:shinyTracker.css");
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
    public static void updateStatLabels(Label health, Label attack, Label defense, Label speed, Label special, int level, Pokemon selectedPokemon){
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
    public static int calculateShinyStat(int base, int dv, int level, boolean isHealth){
        int health = isHealth ? 1 : 0;
        return (((base + dv) * 2 * level) / 100) + (level * health) + 5 + (5 * health);
    }

    public static void updatePreviousSessionDat(int change){
        //saves current hunts to open next time the program is started
        try {
            OutputStream out = new FileOutputStream("SaveData/previousSession.dat");
            out.write(windowsList.size() + change);
            out.close();
        }catch (IOException f){
            f.printStackTrace();
        }
    }

    static Stage editHunts = new Stage();
    static Pane parentPane;
    static GridPane editHuntsLayout = new GridPane();
    public static void editSavedHuntsWindow(){
        parentPane = new Pane();
        editHuntsLayout.getChildren().clear();
        editHuntsLayout.setPadding(new Insets(5, 10, 5, 10));
        editHuntsLayout.setHgap(10);
        editHuntsLayout.setVgap(5);

        editHuntsLayout.heightProperty().addListener((o, oldValue, newValue) -> {
            editHunts.setWidth(editHuntsLayout.getWidth() + 10);
            editHunts.setHeight(editHuntsLayout.getHeight() + 40);
        });

        try(FileReader reader = new FileReader("SaveData/previousHunts.json")){
            JSONParser jsonParser = new JSONParser();
            JSONArray huntList = (JSONArray) jsonParser.parse(reader);

            if(huntList.size() == 0) {
                editHunts.close();
                return;
            }

            for(int i = 0; i < huntList.size(); i++){
                JSONObject huntData = (JSONObject) huntList.get(i);
                Pokemon huntPokemon = new Pokemon(Integer.parseInt(huntData.get("pokemon").toString()));
                Game huntGame = new Game(Integer.parseInt(huntData.get("game").toString()));
                Method huntMethod = new Method(Integer.parseInt(huntData.get("method").toString()));

                Button pokemonButton = new Button(huntPokemon.getName());
                GridPane.setHalignment(pokemonButton, HPos.CENTER);
                GridPane.setValignment(pokemonButton, VPos.CENTER);
                editHuntsLayout.add(pokemonButton, 0, i);

                Button gameButton = new Button(huntGame.getName());
                GridPane.setHalignment(gameButton, HPos.CENTER);
                GridPane.setValignment(gameButton, VPos.CENTER);
                editHuntsLayout.add(gameButton, 1, i);

                Button methodButton = new Button(huntMethod.getName());
                GridPane.setHalignment(methodButton, HPos.CENTER);
                GridPane.setValignment(methodButton, VPos.CENTER);
                editHuntsLayout.add(methodButton, 2, i);

                Button encountersButton = new Button(String.format("%,d", Integer.parseInt(huntData.get("encounters").toString())));
                GridPane.setHalignment(encountersButton, HPos.CENTER);
                GridPane.setValignment(encountersButton, VPos.CENTER);
                editHuntsLayout.add(encountersButton, 3, i);

                Button delete = new Button("Delete");
                GridPane.setHalignment(delete, HPos.CENTER);
                GridPane.setValignment(delete, VPos.CENTER);
                editHuntsLayout.add(delete, 4, i);

                int index = i;

                pokemonButton.setOnAction(f -> {
                    ChoiceDialog<Pokemon> pokemonChoiceDialog = new ChoiceDialog<>();
                    pokemonChoiceDialog.setHeaderText("Select New Pokemon");
                    pokemonChoiceDialog.initStyle(StageStyle.UNDECORATED);
                    makeDraggable(pokemonChoiceDialog.getDialogPane().getScene());
                    pokemonChoiceDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
                    try (FileReader gameReader = new FileReader("GameData/pokemon.json")) {
                        JSONArray gameList = (JSONArray) jsonParser.parse(gameReader);

                        for (int j = 0; j < gameList.size(); j++)
                            pokemonChoiceDialog.getItems().add(new Pokemon((JSONObject) gameList.get(j), j));
                    } catch (IOException | ParseException g) {
                        g.printStackTrace();
                    }
                    pokemonChoiceDialog.setSelectedItem(huntPokemon);
                    pokemonChoiceDialog.showAndWait().ifPresent(g -> {
                        huntData.put("pokemon", pokemonChoiceDialog.getSelectedItem().getDexNumber());
                        huntData.put("pokemon_form", 0);
                        SaveData.updateHunt(index, huntData);
                        editSavedHuntsWindow();
                        refreshHunts();
                    });
                });

                gameButton.setOnAction(f -> {
                    ChoiceDialog<Game> gameChoiceDialog = new ChoiceDialog<>();
                    gameChoiceDialog.setHeaderText("Select New Game");
                    gameChoiceDialog.initStyle(StageStyle.UNDECORATED);
                    makeDraggable(gameChoiceDialog.getDialogPane().getScene());
                    gameChoiceDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
                    try (FileReader gameReader = new FileReader("GameData/game.json")) {
                        JSONArray gameList = (JSONArray) jsonParser.parse(gameReader);

                        for (int j = 0; j < gameList.size(); j++)
                            gameChoiceDialog.getItems().add(new Game((JSONObject) gameList.get(j), j));
                    } catch (IOException | ParseException g) {
                        g.printStackTrace();
                    }
                    gameChoiceDialog.setSelectedItem(huntGame);
                    gameChoiceDialog.showAndWait().ifPresent(g -> {
                        huntData.put("game", gameChoiceDialog.getSelectedItem().getId());
                        huntData.put("game_mods", new JSONArray());
                        SaveData.updateHunt(index, huntData);
                        editSavedHuntsWindow();
                        refreshHunts();
                    });
                });

                methodButton.setOnAction(f -> {
                    ChoiceDialog<Method> methodChoiceDialog = new ChoiceDialog<>();
                    methodChoiceDialog.setHeaderText("Select New Method");
                    methodChoiceDialog.initStyle(StageStyle.UNDECORATED);
                    makeDraggable(methodChoiceDialog.getDialogPane().getScene());
                    methodChoiceDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
                    try (FileReader gameReader = new FileReader("GameData/method.json")) {
                        JSONArray gameList = (JSONArray) jsonParser.parse(gameReader);

                        for (int j = 0; j < gameList.size(); j++)
                            methodChoiceDialog.getItems().add(new Method((JSONObject) gameList.get(j), j));
                    } catch (IOException | ParseException g) {
                        g.printStackTrace();
                    }
                    methodChoiceDialog.setSelectedItem(huntMethod);
                    methodChoiceDialog.showAndWait().ifPresent(g -> {
                        huntData.put("method", methodChoiceDialog.getSelectedItem().getId());
                        SaveData.updateHunt(index, huntData);
                        editSavedHuntsWindow();
                        refreshHunts();
                    });
                });

                encountersButton.setOnAction(f -> {
                    TextInputDialog encountersDialog = new TextInputDialog();
                    encountersDialog.setHeaderText("Input New Encounters");
                    encountersDialog.initStyle(StageStyle.UNDECORATED);
                    makeDraggable(encountersDialog.getDialogPane().getScene());
                    encountersDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
                    encountersDialog.showAndWait().ifPresent(g -> {
                        huntData.put("encounters", Integer.parseInt(encountersDialog.getEditor().getText()));
                        SaveData.updateHunt(index, huntData);
                        editSavedHuntsWindow();
                        refreshHunts();
                    });
                });

                delete.setOnAction(e -> {
                    SaveData.removeHunt(index);
                    editSavedHuntsWindow();
                    refreshHunts();
                });
            }
            parentPane.getChildren().add(editHuntsLayout);
            parentPane.setId("background");
            Scene huntListScene = new Scene(parentPane, 1000, 400);
            huntListScene.getStylesheets().add("file:shinyTracker.css");
            editHunts.setScene(huntListScene);
            if(!editHunts.isShowing())
                editHunts.show();
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public static void makeDraggable(Scene scene){
        Window stage = scene.getWindow();
        scene.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        scene.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }
}