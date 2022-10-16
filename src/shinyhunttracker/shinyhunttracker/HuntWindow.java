package shinyhunttracker;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import static shinyhunttracker.ElementSettings.*;

class HuntWindow {
    //hunt window elements
    Stage windowStage = new Stage();
    AnchorPane windowLayout = new AnchorPane();
    String currentLayout;
    int huntLayoutSize = 0;
    ImageView Evo0, Evo1, sprite;
    Text currentHuntingMethodText, currentHuntingPokemonText, currentGameText, encountersText, currentComboText, oddFractionText;
    Text previousEncountersText = new Text();
    IntegerProperty encounters = new SimpleIntegerProperty();
    IntegerProperty combo = new SimpleIntegerProperty();
    int previousEncounters, increment, huntNumber, huntID;
    KeyCode keybind;

    //hunt settings window elements
    Stage CustomizeHuntStage = new Stage();
    VBox CustomizeHuntVBox = new VBox();

    //current objects
    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    int methodBase;

    public HuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, String layout, int encounters, int combo, int increment, int huntID){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        this.currentLayout = layout;
        this.encounters.setValue(encounters);
        this.combo.setValue(combo);
        this.increment = increment;
        this.huntID = huntID;
    }

    //creates hunt window
    public void createHuntWindow(){
        //Initializes Labels
        currentHuntingPokemonText = new Text(selectedPokemon.getName());
        currentHuntingMethodText= new Text(selectedMethod.getName());
        currentGameText = new Text(selectedGame.toString());
        oddFractionText= new Text("1/"+simplifyFraction(selectedMethod.getModifier(), selectedGame.getOdds()));
        dynamicOddsMethods();
        encountersText= new Text(String.valueOf(encounters));
        currentComboText = new Text(String.valueOf(combo));
        currentComboText.setVisible(false);
        previousEncountersText = new Text();
        previousEncountersText.setVisible(false);

        //Sets the outlines of the labels to white
        currentHuntingPokemonText.setStroke(Color.web("0x00000000"));
        currentHuntingMethodText.setStroke(Color.web("0x00000000"));
        currentGameText.setStroke(Color.web("0x00000000"));
        oddFractionText.setStroke(Color.web("0x00000000"));
        encountersText.setStroke(Color.web("0x00000000"));
        currentComboText.setStroke(Color.web("0x00000000"));
        previousEncountersText.setStroke(Color.web("0x00000000"));

        //Makes labels draggable
        quickEdit(currentHuntingPokemonText);
        quickEdit(currentHuntingMethodText);
        quickEdit(currentGameText);
        quickEdit(oddFractionText);
        quickEdit(encountersText);
        quickEdit(currentComboText);
        quickEdit(previousEncountersText);

        //Creates the pokemon's sprite
        sprite = new ImageView();
        FetchImage.setImage(sprite, selectedPokemon, selectedGame);

        //Creates the pokemon's family's sprites
        Evo0 = new ImageView();
        Evo1 = new ImageView();

        if(selectedPokemon.getEvoStage() >= 1)
            FetchImage.setImage(Evo0, new Pokemon(selectedPokemon.getFamily().get(0).get(0)), selectedGame);
        if(selectedPokemon.getEvoStage() >= 2)
            FetchImage.setImage(Evo1, new Pokemon(selectedPokemon.getFamily().get(0).get(1)), selectedGame);

        //Makes family sprites draggable
        quickEdit(sprite);
        quickEdit(Evo0);
        quickEdit(Evo1);

        windowLayout.getChildren().addAll(sprite, Evo0, Evo1);

        //Adds combo text and prompts user for previous encounters when needed
        switch(selectedMethod.getName()){
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                currentComboText.setVisible(true);
                break;
            case "DexNav":
                currentComboText.setVisible(true);
                previousEncountersText.setVisible(true);
                break;
            case "Total Encounters":
                previousEncountersText.setVisible(true);
                break;
            default:
                break;
        }
        windowLayout.getChildren().addAll(currentHuntingPokemonText, currentHuntingMethodText, currentGameText, encountersText, previousEncountersText, currentComboText, oddFractionText);
        huntLayoutSize = windowLayout.getChildren().size();

        //Positions the labels to the right of the sprite images
        int index = 3;
        for(int i = 3; i < windowLayout.getChildren().size(); i++){
            if(windowLayout.getChildren().get(i).isVisible()) {
                double evo1Width = 0;
                double evo0Width = 0;
                double spriteWidth = 200;
                if(Evo0.getImage() != null)
                    evo0Width = 200;
                if(Evo1.getImage() != null)
                    evo1Width = 200;
                windowLayout.getChildren().get(i).setLayoutX(evo0Width + evo1Width + spriteWidth + 5);
                windowLayout.getChildren().get(i).setLayoutY(15 * (index - 2));
                index++;
            }
        }

        //Positions the sprites so that they are in order of the family i.e stage 0, 1, 2
        if(selectedPokemon.getEvoStage() == 2) {
            Evo0.setLayoutX(100);
            Evo0.setLayoutY(200);

            Evo1.setLayoutX(200 + 100);
            Evo1.setLayoutY(200);

            sprite.setLayoutX(200 + 200 + 100);
            sprite.setLayoutY(200);
        }else if(selectedPokemon.getEvoStage() == 1){
            Evo0.setLayoutX(100);
            Evo0.setLayoutY(200);

            sprite.setLayoutX(200 + 100);
            sprite.setLayoutY(200);
        }else {
            sprite.setLayoutX(100);
            sprite.setLayoutY(200);
        }

        //automatically updates encounters and combo texts
        encountersText.textProperty().bind(encounters.asString());
        currentComboText.textProperty().bind(combo.asString());

        //Set scene and show screen
        Scene huntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(huntScene);
        windowStage.show();

        //load layout
        if(currentLayout.compareTo("") != 0)
            SaveData.loadLayout(currentLayout, windowLayout, true);
    }

    //creates window for the hunt window settings
    public void customizeHuntWindow(){
        if(CustomizeHuntVBox.getChildren().size() == 0) {
            CustomizeHuntStage.setTitle("Settings");
            CustomizeHuntStage.setResizable(false);
            VBox spriteSettings = createImageSettings(windowLayout, sprite, selectedPokemon, selectedGame);
            VBox currentPokemonSettings = createLabelSettings(currentHuntingPokemonText, "Pokemon");
            VBox currentMethodSettings = createLabelSettings(currentHuntingMethodText, "Method");
            VBox currentGameSettings = createLabelSettings(currentGameText, "Game");
            VBox encountersSettings = createLabelSettings(encountersText, "Encounters");
            VBox oddsFraction = createLabelSettings(oddFractionText, "Odds");

            VBox backgroundVBox = new VBox();
            Label backgroundGroup = new Label("Background");
            backgroundGroup.setUnderline(true);
            HBox backgroundColorSettings = new HBox();
            backgroundColorSettings.setSpacing(5);
            Label backgroundColorLabel = new Label("Color");
            ColorPicker backgroundColorPicker = new ColorPicker();
            backgroundVBox.setPadding(new Insets(10, 10, 10, 10));
            backgroundVBox.setSpacing(10);
            backgroundColorSettings.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
            backgroundVBox.getChildren().addAll(backgroundGroup, backgroundColorSettings);

            if (windowLayout.getBackground() != null)
                backgroundColorPicker.setValue((Color) windowLayout.getBackground().getFills().get(0).getFill());

            Accordion accordion = new Accordion();
            TitledPane backgroundTitledPane = new TitledPane("Background", backgroundVBox);
            accordion.getPanes().add(backgroundTitledPane);

            VBox backgroundSettings = new VBox();
            backgroundSettings.getChildren().add(accordion);

            HBox saveClose = new HBox();
            saveClose.setPadding(new Insets(10, 10, 10, 10));
            saveClose.setSpacing(5);
            Button Save = new Button("Save");
            Button Load = new Button("Load");
            saveClose.getChildren().addAll(Save, Load);

            VBox Evo0Settings;
            VBox Evo1Settings;

            CustomizeHuntVBox.getChildren().add(spriteSettings);
            if (Evo1.getImage() != null) {
                Evo1Settings = createImageSettings(windowLayout, Evo1, new Pokemon(selectedPokemon.getFamily().get(0).get(1)), selectedGame);
                CustomizeHuntVBox.getChildren().add(Evo1Settings);
            }
            if (Evo0.getImage() != null) {
                Evo0Settings = createImageSettings(windowLayout, Evo0, new Pokemon(selectedPokemon.getFamily().get(0).get(0)), selectedGame);
                CustomizeHuntVBox.getChildren().addAll(Evo0Settings);
            }

            VBox comboSettings;
            VBox previousEncountersSettings;

            switch (selectedMethod.getName()) {
                case "Radar Chaining":
                case "Chain Fishing":
                case "SOS Chaining":
                case "Catch Combo":
                    comboSettings = createLabelSettings(currentComboText, "Combo");
                    CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, backgroundSettings, saveClose);
                    break;
                case "DexNav":
                    comboSettings = createLabelSettings(currentComboText, "Combo");
                    previousEncountersSettings = createLabelSettings(previousEncountersText, "Search Level");
                    CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, previousEncountersSettings, backgroundSettings, saveClose);
                    break;
                case "Total Encounters":
                    previousEncountersSettings = createLabelSettings(previousEncountersText, "Total Encounters");
                    CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, previousEncountersSettings, backgroundSettings, saveClose);
                    break;
                default:
                    CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, backgroundSettings, saveClose);
                    break;
            }

            AnchorPane CustomizeHuntLayout = new AnchorPane();
            CustomizeHuntLayout.getChildren().add(CustomizeHuntVBox);
            AnchorPane.setTopAnchor(CustomizeHuntVBox, 0d);

            ScrollPane CustomizeHuntScrollpane = new ScrollPane(CustomizeHuntLayout);
            CustomizeHuntScrollpane.setFitToHeight(true);

            Scene CustomizeHuntScene = new Scene(CustomizeHuntScrollpane, 263, 500);
            CustomizeHuntStage.setScene(CustomizeHuntScene);

            backgroundColorPicker.setOnAction(e -> windowLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

            Save.setOnAction(e -> saveLayout());

            Load.setOnAction(e -> loadLayout());
        }
        CustomizeHuntStage.show();
        CustomizeHuntStage.setOnCloseRequest(e -> CustomizeHuntStage.hide());
    }

    //adds increment to the encounters
    public void incrementEncounters(){
        encounters.setValue(encounters.getValue() + increment);
        combo.setValue(combo.getValue() + increment);
        dynamicOddsMethods();
    }

    //adds current pokemon to the caught pokemon file
    public void pokemonCaught() {
        SaveData.pokemonCaught(this);
        if(PreviouslyCaught.isShowing())
            PreviouslyCaught.refreshPreviouslyCaughtPokemon();

        CustomizeHuntStage.close();
        windowStage.close();
    }

    //prompts user for phase pokemon, resets encounters, and adds phased pokemon to the caught pokemon file
    public void phaseHunt(int pokemonID){
        SaveData.pokemonCaught(new HuntWindow(new Pokemon(pokemonID), selectedGame, selectedMethod, currentLayout, encounters.getValue(), combo.getValue(), increment, 0));
        resetCombo();
        resetEncounters();
        if(PreviouslyCaught.isShowing())
            PreviouslyCaught.refreshPreviouslyCaughtPokemon();
    }

    //resets encounters
    public void resetCombo(){
        combo.setValue(0);
        currentComboText.setText(String.valueOf(combo));
        dynamicOddsMethods();
    }

    //save hunt and it closes the window
    public void closeHuntWindow(){
        windowStage.close();
        CustomizeHuntStage.close();
    }

    //reset encounters
    public void resetEncounters(){
        encounters.setValue(0);
        encountersText.setText("0");
    }

    //since some methods' odds change based on encounters
    private void dynamicOddsMethods(){
        switch(selectedMethod.getName()){
            case "Radar Chaining":
                int tempEncounters;
                if (combo.getValue() >= 40)
                    tempEncounters = 39;
                else
                    tempEncounters = combo.getValue();
                oddFractionText.setText("1/" + simplifyFraction(Math.round(((65535 / (8200.0 - tempEncounters * 200)) + selectedMethod.getModifier() - 1)), (65536 / (1 + (Math.abs(methodBase - 8196) / 4096)))));
                break;
            case "Chain Fishing":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.chainFishing(combo.getValue()), methodBase));
                break;
            case "DexNav":
                if (previousEncounters < 999) {
                    previousEncounters++;
                    previousEncountersText.setText(String.valueOf(previousEncounters));
                }else
                    previousEncountersText.setText("999");
                oddFractionText.setText("1/" + selectedMethod.dexNav(combo.getValue(), previousEncounters));
                break;
            case "SOS Chaining":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(combo.getValue()), methodBase));
                break;
            case "Catch Combo":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(combo.getValue()), methodBase));
                break;
            case "Total Encounters":
                previousEncounters++;
                previousEncountersText.setText(String.valueOf(previousEncounters));
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters), methodBase));
                break;
            default:
                break;
        }
    }

    //save layout
    public void saveLayout(){
        Stage promptLayoutSaveName = new Stage();
        promptLayoutSaveName.initModality(Modality.APPLICATION_MODAL);
        promptLayoutSaveName.setResizable(false);

        if(!currentLayout.equals("")) {
            promptLayoutSaveName.setTitle("Save Layout");

            VBox selectNewSaveLayout = new VBox();
            selectNewSaveLayout.setAlignment(Pos.CENTER);
            selectNewSaveLayout.setSpacing(5);
            Label newNameLabel = new Label("Would you like to save this layout to a new name?");

            HBox buttons = new HBox();
            buttons.setSpacing(10);
            buttons.setAlignment(Pos.CENTER);
            Button newSaveButton = new Button("New");
            Button oldSaveButton = new Button("Update");
            buttons.getChildren().addAll(newSaveButton, oldSaveButton);

            selectNewSaveLayout.getChildren().addAll(newNameLabel, buttons);

            Scene selectNewSaveScene = new Scene(selectNewSaveLayout, 275, 75);
            promptLayoutSaveName.setScene(selectNewSaveScene);
            promptLayoutSaveName.show();

            newSaveButton.setOnAction(f -> {
                promptLayoutSaveName.setTitle("Select Layout Name");

                VBox saveLayoutNameLayout = new VBox();
                saveLayoutNameLayout.setSpacing(10);
                saveLayoutNameLayout.setAlignment(Pos.CENTER);

                Label saveNameLabel = new Label("What would you like this layout to be called?");
                TextField saveNameText = new TextField();

                saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
                Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
                promptLayoutSaveName.setScene(saveLayoutNameScene);
                promptLayoutSaveName.show();

                saveNameText.setOnAction(g -> {
                    String text = saveNameText.getText();
                    if (text.contains("\\") || text.contains("/") || text.contains(":") || text.contains("*") || text.contains("?") || text.contains("\"") || text.contains("<") || text.contains(">") || text.contains("|") || text.contains(".")) {
                        saveNameText.setText("");
                    } else {
                        SaveData.saveLayout(saveNameText.getText(), windowLayout, true);
                        currentLayout = saveNameText.getText();
                        promptLayoutSaveName.close();
                    }
                });
            });

            oldSaveButton.setOnAction(f -> {
                SaveData.saveLayout(currentLayout, windowLayout, true);
                promptLayoutSaveName.close();
            });
        }else{
            promptLayoutSaveName.setTitle("Select Layout Name");

            VBox saveLayoutNameLayout = new VBox();
            saveLayoutNameLayout.setSpacing(10);
            saveLayoutNameLayout.setAlignment(Pos.CENTER);

            Label saveNameLabel = new Label("What would you like this layout to be called?");
            TextField saveNameText = new TextField();

            saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
            Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
            promptLayoutSaveName.setScene(saveLayoutNameScene);
            promptLayoutSaveName.show();

            saveNameText.setOnAction(g -> {
                String text = saveNameText.getText();
                if (text.indexOf('\\') > -1 || text.indexOf('/') > -1 || text.indexOf(':') > -1 || text.indexOf('*') > -1 || text.indexOf('?') > -1 || text.indexOf('\"') > -1 || text.indexOf('<') > -1 || text.indexOf('>') > -1 || text.indexOf('|') > -1 || text.indexOf('.') > -1) {
                    saveNameText.setText("");
                } else {
                    SaveData.saveLayout(saveNameText.getText(), windowLayout, true);
                    currentLayout = saveNameText.getText();
                    promptLayoutSaveName.close();
                }
            });
        }
    }

    //load layout
    public void loadLayout(){
        Stage loadSavedLayoutStage = new Stage();
        loadSavedLayoutStage.initModality(Modality.APPLICATION_MODAL);
        loadSavedLayoutStage.setResizable(false);
        loadSavedLayoutStage.setTitle("Select Layout Name");

        TreeView<String> savedLayouts = new TreeView<>();
        TreeItem<String> root = new TreeItem<>();

        try (FileReader reader = new FileReader("SaveData/huntLayouts.json")){
            //Read JSON file
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONArray layoutList = (JSONArray) obj;

            for(int i = layoutList.size() - 1; i >= 0; i--){
                JSONArray layoutInfo = (JSONArray) layoutList.get(i);
                String name = layoutInfo.get(0).toString();

                TreeItem<String> item = new TreeItem<>(name);
                root.getChildren().add(item);
            }
        }catch (IOException | ParseException f) {
            f.printStackTrace();
        }

        savedLayouts.setRoot(root);
        savedLayouts.setShowRoot(false);
        savedLayouts.setPrefWidth(300);
        savedLayouts.setPrefWidth(500);

        VBox savedLayoutsLayout = new VBox();
        savedLayoutsLayout.setSpacing(10);
        savedLayoutsLayout.setAlignment(Pos.CENTER);
        savedLayoutsLayout.getChildren().add(savedLayouts);

        Scene savedLayoutsScene = new Scene(savedLayoutsLayout, 300, 400);
        loadSavedLayoutStage.setScene(savedLayoutsScene);
        loadSavedLayoutStage.show();

        savedLayouts.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    currentLayout = newValue.toString().substring(18, String.valueOf(newValue).length() - 2);
                    SaveData.loadLayout(currentLayout, windowLayout, true);
                    loadSavedLayoutStage.close();
                    customizeHuntWindow();
                });
    }

    //simplifies odds fraction for easier reading
    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }

    public int getCombo(){ return combo.getValue(); }
    public int getEncounters(){ return encounters.getValue(); }
    public int getHuntID(){ return huntID; }
    public int getHuntNumber(){ return huntNumber; }
    public int getIncrement(){ return increment; }

    public Pokemon getPokemon(){ return selectedPokemon; }
    public Game getGame(){ return selectedGame; }
    public Method getMethod() { return selectedMethod; }

    public KeyCode getKeyBinding(){ return keybind; }
    public AnchorPane getScene() { return windowLayout; }
    public Stage getStage() { return windowStage; }
    public String getCurrentLayout() { return currentLayout; }
    public IntegerProperty encounterProperty(){ return encounters; }

    public void setHuntNumber(int huntNumber){ this.huntNumber = huntNumber; }
    public void setKeybind(KeyCode keybind){ this.keybind = keybind; }
    public void setIncrement(int increment){ this.increment = increment; }
}
