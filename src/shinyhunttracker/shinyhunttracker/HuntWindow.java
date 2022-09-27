package shinyhunttracker;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.lang.Integer.parseInt;

class HuntWindow extends Window {
    //hunt window elements
    int huntLayoutSize = 0;
    ImageView Evo0, Evo1, sprite;
    Text currentHuntingMethodText, currentHuntingPokemonText, currentGameText, encountersText, currentComboText, oddFractionText;
    Text previousEncountersText = new Text();
    PreviouslyCaught previouslyCaughtWindow;
    IntegerProperty encounters = new SimpleIntegerProperty();
    IntegerProperty combo = new SimpleIntegerProperty();
    int previousEncounters, increment, huntNumber, huntID;
    char keybind;

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
        currentHuntingPokemonText = new Text(selectedPokemon.getName());
        currentHuntingMethodText= new Text(selectedMethod.getName());
        currentGameText = new Text(selectedGame.getName());
        oddFractionText= new Text("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        dynamicOddsMethods();
        encountersText= new Text(String.valueOf(encounters));
        currentComboText = new Text(String.valueOf(combo));
        currentComboText.setVisible(false);
        previousEncountersText = new Text();
        previousEncountersText.setVisible(false);

        currentHuntingPokemonText.setStroke(Color.web("0x00000000"));
        currentHuntingMethodText.setStroke(Color.web("0x00000000"));
        currentGameText.setStroke(Color.web("0x00000000"));
        oddFractionText.setStroke(Color.web("0x00000000"));
        encountersText.setStroke(Color.web("0x00000000"));
        currentComboText.setStroke(Color.web("0x00000000"));
        previousEncountersText.setStroke(Color.web("0x00000000"));

        quickEdit(currentHuntingPokemonText);
        quickEdit(currentHuntingMethodText);
        quickEdit(currentGameText);
        quickEdit(oddFractionText);
        quickEdit(encountersText);
        quickEdit(currentComboText);
        quickEdit(previousEncountersText);

        sprite = new ImageView();

        setPokemonSprite(sprite, selectedPokemon.getName(), selectedGame);
        if(selectedPokemon.getForm() != null && !selectedPokemon.getForm().equals("null"))
            setAlternateSprite(selectedPokemon, selectedGame, sprite);

        Evo0 = new ImageView();
        Evo1 = new ImageView();
        if(!selectedPokemon.getFamily()[0].equals("")) {
            setPokemonSprite(Evo0, selectedPokemon.getFamily()[0], selectedGame);
            if(Evo0 == null)
                Evo0 = new ImageView();
        }
        if(!selectedPokemon.getFamily()[1].equals("")) {
            setPokemonSprite(Evo1, selectedPokemon.getFamily()[1], selectedGame);
            if(Evo1 == null)
                Evo1 = new ImageView();
        }

        quickEdit(sprite);
        quickEdit(Evo0);
        quickEdit(Evo1);

        windowLayout.getChildren().addAll(sprite, Evo0, Evo1);

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

        int index = 3;
        for(int i = 3; i < windowLayout.getChildren().size(); i++){
            if(windowLayout.getChildren().get(i).isVisible()) {
                double evo1Width = 0;
                double evo0Width = 0;
                double spriteWidth = sprite.getImage().getWidth() * sprite.getScaleX();
                if(!selectedPokemon.getFamily()[0].equals(""))
                    evo1Width = Evo1.getImage().getWidth() * Evo1.getScaleX();
                if(!selectedPokemon.getFamily()[1].equals(""))
                    evo0Width = Evo0.getImage().getWidth() * Evo0.getScaleX();
                windowLayout.getChildren().get(i).setLayoutX(evo0Width + evo1Width + spriteWidth + 5);
                windowLayout.getChildren().get(i).setLayoutY(15 * (index - 2));
                index++;
            }
        }

        if(!this.selectedPokemon.getFamily()[1].equals("")) {
            Evo0.setLayoutX(Evo0.getImage().getWidth() * Evo0.getScaleX()/2);
            Evo0.setLayoutY((Evo0.getImage().getHeight() * Evo0.getScaleY()));

            Evo1.setLayoutX(Evo0.getImage().getWidth() * Evo0.getScaleX() + (Evo1.getImage().getWidth() * Evo1.getScaleX()/2));
            Evo1.setLayoutY(Evo1.getImage().getHeight() * Evo1.getScaleY());

            sprite.setLayoutX(Evo0.getImage().getWidth() * Evo0.getScaleX() + Evo1.getImage().getWidth()  * Evo1.getScaleX() + (sprite.getImage().getWidth() * sprite.getScaleX()/2));
            sprite.setLayoutY(sprite.getImage().getHeight() * sprite.getScaleY());
        }else if(!this.selectedPokemon.getFamily()[0].equals("")){
            Evo0.setLayoutX(Evo0.getImage().getWidth() * Evo0.getScaleX()/2);
            Evo0.setLayoutY(Evo0.getImage().getHeight() * Evo0.getScaleY());

            sprite.setLayoutX(Evo0.getImage().getWidth() * Evo0.getScaleX() + (sprite.getImage().getWidth() * sprite.getScaleX()/2));
            sprite.setLayoutY(sprite.getImage().getHeight() * sprite.getScaleY());
        }else {
            sprite.setLayoutX((sprite.getImage().getWidth() * sprite.getScaleX())/2);
            sprite.setLayoutY((sprite.getImage().getHeight() * sprite.getScaleY()));
        }

        encountersText.textProperty().bind(encounters.asString());
        currentComboText.textProperty().bind(combo.asString());

        Scene huntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(huntScene);
        windowStage.show();

        SaveData data = new SaveData();
        if(currentLayout != null && currentLayout.compareTo("null") != 0) {
            data.loadLayout("Hunts/" + currentLayout, windowLayout);
        }
    }

    //creates window to prompt user for search level or previous encounters
    public void promptPreviousEncounters(){
        Stage promptWindow = new Stage();
        promptWindow.setResizable(false);
        promptWindow.initModality(Modality.APPLICATION_MODAL);

        Label promptLabel = new Label();
        if(selectedMethod.getName().compareTo("DexNav") == 0) {
            promptWindow.setTitle("Enter Search Level");
            promptLabel.setText("Please enter the current Search Level for " + selectedPokemon.getName());
        }
        else if(selectedMethod.getName().compareTo("Total Encounters") == 0) {
            promptWindow.setTitle("Enter Number Battled");
            promptLabel.setText("Please enter the current Number Battled for " + selectedPokemon.getName());
        }

        TextField previousInput = new TextField();

        previousInput.setOnAction(e-> {
            try{
                previousEncounters = parseInt(previousInput.getText());
                previousEncountersText.setText(String.valueOf(previousEncounters));
                if(selectedMethod.getName().compareTo("DexNav") == 0)
                    oddFractionText.setText("1/" + selectedMethod.dexNav(encounters.getValue(), previousEncounters));
                else
                    oddFractionText.setText("1/" + simplifyFraction((selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters)), methodBase));
                promptWindow.close();
            }catch (NumberFormatException f){
                previousInput.setText("");
            }
        });

        VBox promptLayout = new VBox();
        promptLayout.setSpacing(10);
        promptLayout.setAlignment(Pos.CENTER);
        promptLayout.getChildren().addAll(promptLabel, previousInput);

        Scene promptScene = new Scene(promptLayout, 275, 75);
        promptWindow.setScene(promptScene);
        promptWindow.show();

        promptWindow.setOnCloseRequest(e -> windowStage.close());
    }

    //creates window for the hunt window settings
    public void CustomizeHuntWindow(){
        if(CustomizeHuntVBox.getChildren().size() == 0) {
            CustomizeHuntStage.setTitle("Settings");
            CustomizeHuntStage.setResizable(false);
            VBox spriteSettings = createImageSettings(sprite, selectedPokemon, selectedGame);
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
            /*if (!evo1.equals("")) {
                Evo1Settings = createImageSettings(Evo1, new Pokemon(evo1, 0), selectedGame);
                CustomizeHuntVBox.getChildren().add(Evo1Settings);
            }
            if (!evo0.equals("")) {
                Evo0Settings = createImageSettings(Evo0, new Pokemon(evo0, 0), selectedGame);
                CustomizeHuntVBox.getChildren().addAll(Evo0Settings);
            }*/

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
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters.getValue(), combo.getValue(), increment, currentLayout);
        data.pokemonCaught(huntID);

        CustomizeHuntStage.close();
        windowStage.close();
    }

    //prompts user for phase pokemon, resets encounters, and adds phased pokemon to the caught pokemon file
    public void phaseHunt(){
        Stage phaseStage = new Stage();
        phaseStage.initModality(Modality.APPLICATION_MODAL);
        phaseStage.setResizable(false);
        phaseStage.setTitle("Phase");

        VBox phaseLayout = new VBox();
        phaseLayout.setAlignment(Pos.CENTER);
        phaseLayout.setSpacing(10);

        Label phaseLabel = new Label("Please enter the phase pokemon");
        TextField phasePokemon = new TextField();
        phaseLayout.getChildren().addAll(phaseLabel, phasePokemon);

        Scene phaseScene = new Scene(phaseLayout, 200,100);
        phaseStage.setScene(phaseScene);
        phaseStage.show();

        phasePokemon.setOnAction(e -> {
            /*String userInput = phasePokemon.getText().toLowerCase();
            userInput = userInput.substring(0,1).toUpperCase() + userInput.substring(1);
            if(new Pokemon(userInput).getGeneration() == 0)
                phasePokemon.setText("");
            else{
                SaveData data = new SaveData(new Pokemon(userInput, 0), selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
                data.pokemonCaught();
                resetCombo();
                resetEncounters();
                if(previouslyCaughtWindow.getStage().isShowing())
                    previouslyCaughtWindow.refreshPreviouslyCaughtPokemon();
                phaseStage.close();
            }*/
        });
    }

    //resets encounters
    public void resetCombo(){
        combo.setValue(0);
        currentComboText.setText(String.valueOf(combo));
        dynamicOddsMethods();
    }

    //writes objects to previous hunts file
    public void saveHunt(){
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters.getValue(), combo.getValue(), increment, currentLayout);
        data.saveHunt(huntID);
    }

    //save hunt, but it asks if the user would like to save, and it closes the window
    public void saveandCloseHunt(){
        windowStage.close();
        CustomizeHuntStage.close();
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters.getValue(), combo.getValue(), increment, currentLayout);
        data.saveHunt(huntID);
    }

    //changes increment
    public void changeIncrement(){
        Stage changeIncrementStage = new Stage();
        changeIncrementStage.setResizable(false);
        changeIncrementStage.initModality(Modality.APPLICATION_MODAL);
        VBox changeIncrementsLayout = new VBox();
        changeIncrementsLayout.setAlignment(Pos.CENTER);
        changeIncrementsLayout.setSpacing(10);
        Label changeIncrementsLabel = new Label("Enter the number by which the encounters increase for every button press");
        TextField changeIncrementsText = new TextField();
        changeIncrementsLayout.getChildren().addAll(changeIncrementsLabel, changeIncrementsText);
        Scene changeIncrementsScene = new Scene(changeIncrementsLayout, 400, 100);
        changeIncrementStage.setScene(changeIncrementsScene);
        changeIncrementStage.show();

        changeIncrementsText.setOnAction(e -> {
            try{
                increment = parseInt(changeIncrementsText.getText());
                changeIncrementStage.close();
            }catch (NumberFormatException f){
                changeIncrementsText.setText("");
            }
        });
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
        SaveData data = new SaveData();
        Stage promptLayoutSaveName = new Stage();
        promptLayoutSaveName.initModality(Modality.APPLICATION_MODAL);
        promptLayoutSaveName.setResizable(false);

        if(currentLayout != null) {
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
                        data.saveLayout("Hunts/" + saveNameText.getText(), windowLayout, true);
                        currentLayout = saveNameText.getText();
                        promptLayoutSaveName.close();
                    }
                });
            });

            oldSaveButton.setOnAction(f -> {
                data.saveLayout("Hunts/" + currentLayout, windowLayout, false);
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
                    data.saveLayout("Hunts/" + saveNameText.getText(), windowLayout, true);
                    currentLayout = saveNameText.getText();
                    promptLayoutSaveName.close();
                }
            });
        }
    }

    //load layout
    public void loadLayout(){
        SaveData data = new SaveData();

        Stage loadSavedLayoutStage = new Stage();
        loadSavedLayoutStage.initModality(Modality.APPLICATION_MODAL);
        loadSavedLayoutStage.setResizable(false);
        loadSavedLayoutStage.setTitle("Select Layout Name");

        TreeView<String> savedLayouts = new TreeView<>();
        TreeItem<String> root = new TreeItem<>();
        for(int i = 0; i < data.getfileLength("Layouts/Hunts/~Layouts"); i++){
            makeBranch(data.getLinefromFile(i, "Layouts/Hunts/~Layouts"), root);
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
                    data.loadLayout("Hunts/" + currentLayout, windowLayout);
                    loadSavedLayoutStage.close();
                    CustomizeHuntWindow();
                });
    }

    //simplifies odds fraction for easier reading
    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }

    public Pokemon getSelectedPokemon(){ return selectedPokemon; }

    public Method getMethod() {
        return selectedMethod;
    }

    public int getHuntNumber(){
        return huntNumber;
    }

    public char getKeyBinding(){
        return keybind;
    }

    public IntegerProperty encounterProperty(){ return encounters; }

    public int getGameGeneration(){ return selectedGame.getGeneration(); }

    public void setHuntNumber(int huntNumber){ this.huntNumber = huntNumber; };

    public void setKeybind(char keybind){
        this.keybind = keybind;
    }

    public void setPreviouslyCaughtWindow(PreviouslyCaught previouslyCaughtWindow) { this.previouslyCaughtWindow = previouslyCaughtWindow; }
}
