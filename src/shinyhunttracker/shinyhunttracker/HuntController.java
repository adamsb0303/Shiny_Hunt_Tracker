package shinyhunttracker;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Vector;

public class HuntController {
    Stage huntControls = new Stage();
    MenuBar Menu = new MenuBar();
    Menu Settings = new Menu("Settings");
    VBox huntControlsVBox;
    PreviouslyCaught previousCatches = new PreviouslyCaught(0);

    Vector<HuntWindow> windowsList = new Vector<>();

    Stage keyBindingSettingsStage = new Stage();

    /**
     * Creates the Hunt Controller and
     * opens hunts from previous session
     */
    public HuntController(){
        //Initial Hunt Controller setup
        huntControls.setTitle("Hunt Controller");

        huntControlsVBox = new VBox();
        huntControlsVBox.setAlignment(Pos.CENTER);
        huntControlsVBox.setSpacing(5);
        Button addHunt = new Button("Add Hunt");
        huntControlsVBox.getChildren().add(addHunt);

        Menu.getMenus().add(Settings);

        MenuItem keyBinding = new MenuItem("Key Binding");
        Settings.getItems().add(keyBinding);

        MenuItem previouslyCaught = new MenuItem("Previously Caught Settings");
        Settings.getItems().add(previouslyCaught);

        BorderPane huntControlsLayout = new BorderPane();
        huntControlsLayout.setTop(Menu);
        huntControlsLayout.setCenter(huntControlsVBox);

        Scene huntControlsScene = new Scene(huntControlsLayout, 350, 100);
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
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //Listener for KeyBinds
        huntControlsVBox.setOnKeyTyped(e -> {
            for(HuntWindow i: windowsList) {
                if (i.getKeyBinding() == e.getCharacter().toCharArray()[0])
                    i.incrementEncounters();
            }
        });

        //Popups for given menus
        keyBinding.setOnAction(e -> keyBindingSettings());
        previouslyCaught.setOnAction(e -> previousCatches.previouslyCaughtPokemonSettings());

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
                else
                    createSelectionPage();
            }catch(IOException | ParseException f){
                f.printStackTrace();
            }
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
            while(windowsList.size() > 0)
                windowsList.lastElement().closeHunt();

            System.exit(0);
        });
    }

    /**
     * Adds a new HuntWindow to the windowsList and
     * adds appropriate elements to controller with new hunt's information
     * @param newWindow HuntWindow with new hunt information
     */
    public void addHunt(HuntWindow newWindow){
        //Add "Save All" button to menu
        if(windowsList.size() == 0){
            MenuItem saveAll = new MenuItem("Save All");
            Settings.getItems().add(saveAll);

            saveAll.setOnAction(e -> {
                for (HuntWindow i : windowsList)
                    i.saveHunt();
            });
        }

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

        Button closeWindowButton = new Button("X");
        closeWindowButton.setOnAction(e -> newWindow.closeHunt());

        Label huntNumberLabel = new Label(String.valueOf(newWindow.getHuntNumber()));

        Button encountersButton = new Button("+");
        encountersButton.setOnAction(e -> newWindow.incrementEncounters());

        Label encounterLabel = new Label();
        encounterLabel.textProperty().bind(newWindow.encounterProperty().asString());

        Label nameLabel = new Label(newWindow.getSelectedPokemon().getName());

        Button caughtButton = new Button("O");

        Button popOutButton = new Button("O");

        Button windowSettingsButton = new Button("X");
        windowSettingsButton.setVisible(false);

        Button settingsButton = new Button("O");

        Button helpButton = new Button("O");

        huntInformationHBox.getChildren().addAll(closeWindowButton, huntNumberLabel, encountersButton, nameLabel, encounterLabel, caughtButton, popOutButton, windowSettingsButton, settingsButton, helpButton);
        huntControlsVBox.getChildren().add(newWindow.getHuntNumber(), huntInformationHBox);

        //Set keybinds
        SaveData data = new SaveData();
        newWindow.setKeybind(data.getLinefromFile(newWindow.getHuntNumber() - 1, "keyBinds").charAt(0));

        //Add new settings to settings menu
        addHuntWindowSettings(newWindow);

        //update keybind window if it is open
        if(keyBindingSettingsStage.isShowing()){
            keyBindingSettings();
        }

        //since the search level or total encounters can change between uses, this value needs to be captured after every startup
        if (newWindow.getMethod().getName().compareTo("DexNav") == 0 || newWindow.getMethod().getName().compareTo("Total Encounters") == 0) {
            newWindow.promptPreviousEncounters();
        }

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
            newWindow.createHuntWindow();
        });

        windowSettingsButton.setOnAction(e -> {
            popOutButton.setVisible(true);
            windowSettingsButton.setVisible(false);
        });

        settingsButton.setOnAction(e -> {

        });

        helpButton.setOnAction(e -> {

        });

        newWindow.getStage().setOnCloseRequest(e -> {
            e.consume();
            newWindow.saveHunt();
            huntControlsVBox.getChildren().remove(huntInformationHBox);
            windowsList.remove(newWindow);
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
            createSelectionPage();

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
                    String name = Pokemon.findName(Integer.parseInt(huntData.get("pokemon_id").toString()));
                    String game = huntData.get("game").toString();
                    String method = huntData.get("method").toString();
                    String encounters = huntData.get("encounters").toString();

                    TreeItem<String> item = new TreeItem<>((huntList.size() - i) + ") " + name + " | " + game + " | " + method + " | " + encounters + " encounters");
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

    public void createSelectionPage(){
        try {
            FXMLLoader selectionPageLoader = new FXMLLoader();
            selectionPageLoader.setLocation(getClass().getResource("selectionPage.fxml"));
            Parent root = selectionPageLoader.load();

            Stage huntSelectionWindow = new Stage();
            huntSelectionWindow.setTitle("Shiny Hunt Tracker");
            huntSelectionWindow.setResizable(false);
            huntSelectionWindow.setScene(new Scene(root, 750, 480));

            SelectionPageController selectionPageController = selectionPageLoader.getController();
            selectionPageController.setController(this);
            if (windowsList.size() > 0)
                selectionPageController.setCurrentLayout(windowsList.lastElement().getCurrentLayout());
            huntSelectionWindow.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addHuntWindowSettings(HuntWindow window){
        Menu newHuntSettings = new Menu("Hunt " + window.getHuntNumber() + " Settings");

        MenuItem customizeHuntLayout= new MenuItem("Layout Settings");

        Menu huntSettings = new Menu("Hunt Settings");
        MenuItem increment= new MenuItem("Change Increment");
        MenuItem resetEncounters = new MenuItem("Fail");
        MenuItem phaseHunt = new MenuItem("Phase");
        MenuItem resetCombo = new MenuItem("Reset Combo");
        MenuItem saveHunt = new MenuItem("Save Hunt");
        MenuItem DVTable = new MenuItem("DV Table");
        huntSettings.getItems().addAll(increment, resetEncounters, phaseHunt, resetCombo, saveHunt);

        if(window.getGameGeneration() == 1)
            huntSettings.getItems().add(DVTable);

        newHuntSettings.getItems().addAll(customizeHuntLayout, huntSettings);
        Settings.getItems().add(newHuntSettings);

        customizeHuntLayout.setOnAction(e -> window.CustomizeHuntWindow());
        increment.setOnAction(e -> window.changeIncrement());
        resetEncounters.setOnAction(e -> window.resetEncounters());
        phaseHunt.setOnAction(e -> {
            window.setPreviouslyCaughtWindow(previousCatches);
            window.phaseHunt();
        });
        resetCombo.setOnAction(e -> window.resetCombo());
        saveHunt.setOnAction(e -> window.saveHunt());
        DVTable.setOnAction(e -> generateDVTable(window.getSelectedPokemon()));
    }

    public void keyBindingSettings(){
        keyBindingSettingsStage.setTitle("Key Bindings");

        VBox keyBindingsLayout = new VBox();
        keyBindingsLayout.setAlignment(Pos.CENTER);
        keyBindingsLayout.setSpacing(10);
        keyBindingsLayout.setPadding(new Insets(10,0,50,75));
        for (HuntWindow i : windowsList) {
            HBox windowSettings = new HBox();
            windowSettings.setAlignment(Pos.CENTER);
            windowSettings.setSpacing(10);
            Label huntWindowLabel = new Label("Hunt " + i.getHuntNumber());
            TextField keyField = new TextField();
            keyField.setPromptText(String.valueOf(i.getKeyBinding()));
            keyField.setMaxWidth(50);
            windowSettings.getChildren().addAll(huntWindowLabel, keyField);
            keyBindingsLayout.getChildren().add(windowSettings);

            keyField.setOnAction(f -> {
                if(keyField.getText().length() == 1)
                    for(HuntWindow j: windowsList)
                        if(j.getHuntNumber() == i.getHuntNumber()) {
                            j.setKeybind(keyField.getText().charAt(0));
                            SaveData data = new SaveData();
                            data.replaceLine(j.getHuntNumber() - 1, keyField.getText().substring(0,1), "keyBinds");
                            keyField.setPromptText(String.valueOf(j.getKeyBinding()));
                            break;
                        }
                keyField.setText("");
            });
        }
        ScrollPane scrollPane = new ScrollPane(keyBindingsLayout);
        Scene keyBindingScene = new Scene(scrollPane, 250, 500);
        keyBindingSettingsStage.setScene(keyBindingScene);
        keyBindingSettingsStage.show();
    }

    public void generateDVTable(Pokemon selectedPokemon){
        ComboBox<Integer> levelSelect = new ComboBox<>();
        for(int i = 1; i <= 100; i++)
            levelSelect.getItems().add(i);
        levelSelect.getSelectionModel().select(0);

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

        levelSelect.setOnAction(e -> {
            int level = levelSelect.getSelectionModel().getSelectedItem();
            updateStatLabels(healthifShiny, attackifShiny, defenseifShiny, speedifShiny, specialifShiny, level, selectedPokemon);
        });
    }

    public void updateStatLabels(Label health, Label attack, Label defense, Label speed, Label special, int level, Pokemon selectedPokemon){
        health.setText(calculateShinyStat(selectedPokemon.getStats("health"), 0,level, true) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("health"), 8,level, true));

        attack.setText(calculateShinyStat(selectedPokemon.getStats("attack"), 2, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 3, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 6, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 7, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 10, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 11, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 14, level, false) + "\n"
                     + calculateShinyStat(selectedPokemon.getStats("attack"), 15, level, false));

        defense.setText(calculateShinyStat(selectedPokemon.getStats("defense"), 10, level, false) + "");

        speed.setText(calculateShinyStat(selectedPokemon.getStats("speed"), 10, level, false) + "");

        special.setText(calculateShinyStat(selectedPokemon.getStats("special"), 10, level, false) + "");
    }

    public int calculateShinyStat(int base, int iv, int level, boolean isHealth){
        int health = isHealth ? 1 : 0;
        return (((base + iv) * 2 * level) / 100) + (level * health) + 5 + (2 * health);
    }
}