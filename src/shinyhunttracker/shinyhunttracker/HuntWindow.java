package shinyhunttracker;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    public HuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, String layout, int encounters, int combo, int increment, int huntID){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        if(selectedMethod.getBase() != 0)
            selectedGame.setOdds(selectedMethod.getBase());
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
        oddFractionText = new Text("1/"+simplifyFraction(selectedMethod.getModifier(), selectedGame.getOdds()));
        dynamicOddsMethods();
        encountersText = new Text();
        encountersText.textProperty().bind(Bindings.createStringBinding(() -> String.format("%,d", encounters.getValue()), encounters));
        currentComboText = new Text();
        currentComboText.textProperty().bind(Bindings.createStringBinding(() -> String.format("%,d", combo.getValue()), combo));
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

        //Set scene and show screen
        Scene huntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(huntScene);

        windowStage.show();

        //load layout
        if(currentLayout.compareTo("") != 0)
            SaveData.loadLayout(currentLayout, windowLayout, true);
    }

    //creates window for the hunt window settings
    public void customizeHuntWindowSettings(){
        if(CustomizeHuntVBox.getChildren().size() == 0) {
            CustomizeHuntStage.setTitle("Settings");
            Accordion settings = new Accordion();

            TitledPane spriteSettings = createImageSettings(windowLayout, sprite, selectedPokemon, selectedGame);
            TitledPane currentPokemonSettings = createLabelSettings(currentHuntingPokemonText, "Pokemon");
            TitledPane currentMethodSettings = createLabelSettings(currentHuntingMethodText, "Method");
            TitledPane currentGameSettings = createLabelSettings(currentGameText, "Game");
            TitledPane encountersSettings = createLabelSettings(encountersText, "Encounters");
            TitledPane oddsFraction = createLabelSettings(oddFractionText, "Odds");

            VBox gameModifierSettings = new VBox();
            if(selectedGame.getOddModifiers().size() != 0) {
                gameModifierSettings.setSpacing(10);

                HBox groupLabel = new HBox();
                Label Group = new Label("Game Odd Modifiers");
                Group.setUnderline(true);
                groupLabel.getChildren().add(Group);

                gameModifierSettings.getChildren().add(groupLabel);
                for(Object i : selectedGame.getOddModifiers()){
                    JSONObject gameMod = (JSONObject) i;
                    CheckBox checkBox = new CheckBox(gameMod.get("name").toString());
                    checkBox.setSelected(selectedMethod.getGameMods().contains(gameMod.get("name").toString()));
                    gameModifierSettings.getChildren().add(checkBox);

                    checkBox.selectedProperty().addListener(e -> {
                        if(checkBox.isSelected())
                            selectedMethod.addGameMod(gameMod.get("name").toString(), Integer.parseInt(gameMod.get("extra-rolls").toString()));
                        else
                            selectedMethod.removeGameMod(gameMod.get("name").toString(), Integer.parseInt(gameMod.get("extra-rolls").toString()));

                        oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier(), selectedGame.getOdds()));
                    });
                }
                VBox oddPaneSettings = (VBox) oddsFraction.getContent();
                oddPaneSettings.getChildren().add(0, gameModifierSettings);
            }

            Button layoutSettings = new Button("Layouts");

            settings.getPanes().add(spriteSettings);

            if (Evo1.getImage() != null)
                settings.getPanes().add(createImageSettings(windowLayout, Evo1, new Pokemon(selectedPokemon.getFamily().get(0).get(1)), selectedGame));
            if (Evo0.getImage() != null)
                settings.getPanes().add(createImageSettings(windowLayout, Evo0, new Pokemon(selectedPokemon.getFamily().get(0).get(0)), selectedGame));

            settings.getPanes().addAll(currentPokemonSettings, currentMethodSettings, currentGameSettings, encountersSettings, oddsFraction);

            switch (selectedMethod.getName()) {
                case "Radar Chaining":
                case "Chain Fishing":
                case "SOS Chaining":
                case "Catch Combo":
                case "DexNav":
                    TitledPane comboSettings = createLabelSettings(currentComboText, "Combo");
                    Button resetComboButton = new Button("Reset Combo");
                    resetComboButton.setOnAction(e -> resetCombo());

                    VBox oddPaneSettings = (VBox) comboSettings.getContent();
                    oddPaneSettings.getChildren().add(0, resetComboButton);
                    settings.getPanes().add(comboSettings);
                    break;
                default:
                    break;
            }

            settings.getPanes().add(createBackgroundSettings(windowStage, windowLayout));

            VBox CustomizeHuntLayout = new VBox();
            CustomizeHuntLayout.setAlignment(Pos.TOP_CENTER);
            CustomizeHuntLayout.setId("background");
            CustomizeHuntLayout.setSpacing(10);
            CustomizeHuntLayout.getChildren().addAll(HuntController.titleBar(CustomizeHuntStage), settings, layoutSettings);

            Scene CustomizeHuntScene = new Scene(CustomizeHuntLayout, 0, 0);
            CustomizeHuntScene.getStylesheets().add("file:shinyTracker.css");

            if(CustomizeHuntStage.getScene() == null)
                CustomizeHuntStage.initStyle(StageStyle.UNDECORATED);

            CustomizeHuntStage.setScene(CustomizeHuntScene);
            HuntController.makeDraggable(CustomizeHuntScene);

            settings.heightProperty().addListener((o, oldVal, newVal) -> {
                CustomizeHuntStage.setHeight(settings.getHeight() + 80);
                CustomizeHuntStage.setWidth(315);
            });

            layoutSettings.setOnAction(e -> showLayoutList());
        }
        CustomizeHuntStage.show();
        CustomizeHuntStage.setOnCloseRequest(e -> CustomizeHuntStage.hide());
    }

    //adds increment to the encounters
    public void incrementEncounters(){
        encounters.setValue(encounters.getValue() + increment);
        combo.setValue(combo.getValue() + increment);
        if(oddFractionText != null)
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
                oddFractionText.setText("1/" + simplifyFraction(Math.round(((65535 / (8200.0 - tempEncounters * 200)) + selectedMethod.getModifier() - 1)), (65536 / (1 + (Math.abs(selectedGame.getOdds() - 8196) / 4096)))));
                break;
            case "Chain Fishing":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.chainFishing(combo.getValue()), selectedGame.getOdds()));
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
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(combo.getValue()), selectedGame.getOdds()));
                break;
            case "Catch Combo":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(combo.getValue()), selectedGame.getOdds()));
                break;
            case "Total Encounters":
                previousEncounters++;
                previousEncountersText.setText(String.valueOf(previousEncounters));
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters), selectedGame.getOdds()));
                break;
            default:
                break;
        }
    }

    Stage layoutListStage = new Stage();
    public void showLayoutList(){
        GridPane layoutListLayout = new GridPane();
        layoutListLayout.setHgap(10);
        layoutListLayout.setVgap(5);
        layoutListLayout.setPadding(new Insets(10, 10, 10, 10));

        layoutListLayout.heightProperty().addListener((o, oldVal, newVal) -> {
            if(layoutListLayout.getHeight() + 40 <= 540) {
                layoutListStage.setHeight(layoutListLayout.getHeight() + 40);
                layoutListStage.setWidth(layoutListLayout.getWidth());
            }
            else {
                layoutListStage.setHeight(540);
                layoutListStage.setWidth(layoutListLayout.getWidth() + 10);
            }
        });

        try(FileReader reader = new FileReader("SaveData/huntLayouts.json")){
            JSONParser jsonParser = new JSONParser();
            JSONArray layoutList = (JSONArray) jsonParser.parse(reader);

            for(Object i : layoutList){
                JSONArray layoutObject = (JSONArray) i;

                int row = layoutListLayout.getRowCount();

                Label layoutNameLabel = new Label(layoutObject.get(0).toString());
                GridPane.setHalignment(layoutNameLabel, HPos.CENTER);
                GridPane.setValignment(layoutNameLabel, VPos.CENTER);
                layoutListLayout.add(layoutNameLabel, 0, row);

                Button updateButton = new Button("Update");
                GridPane.setHalignment(updateButton, HPos.CENTER);
                GridPane.setValignment(updateButton, VPos.CENTER);
                layoutListLayout.add(updateButton, 1, row);

                Button loadButton = new Button("Load");
                GridPane.setHalignment(loadButton, HPos.CENTER);
                GridPane.setValignment(loadButton, VPos.CENTER);
                layoutListLayout.add(loadButton, 2, row);

                Button removeButton = new Button("Delete");
                GridPane.setHalignment(removeButton, HPos.CENTER);
                GridPane.setValignment(removeButton, VPos.CENTER);
                layoutListLayout.add(removeButton, 3, row);

                updateButton.setOnAction(e -> {
                    SaveData.saveLayout(layoutObject.get(0).toString(), windowLayout, true);
                    showLayoutList();
                });
                loadButton.setOnAction(e -> {
                    SaveData.loadLayout(layoutObject.get(0).toString(), windowLayout, true);
                    currentLayout = layoutObject.get(0).toString();
                    HuntController.saveHuntOrder();
                });
                removeButton.setOnAction(e -> {
                    SaveData.removeLayout(layoutObject.get(0).toString(), true);
                    showLayoutList();
                });
            }
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }

        Button newLayoutButton = new Button("Add Layout");
        GridPane.setColumnSpan(newLayoutButton, 4);
        GridPane.setHalignment(newLayoutButton, HPos.CENTER);
        GridPane.setValignment(newLayoutButton, VPos.CENTER);
        layoutListLayout.add(newLayoutButton, 0, layoutListLayout.getRowCount());

        ScrollPane parentPane = new ScrollPane();
        parentPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        parentPane.setId("background");
        parentPane.setContent(layoutListLayout);

        VBox masterLayout = new VBox();
        masterLayout.setId("background");
        masterLayout.getChildren().addAll(HuntController.titleBar(layoutListStage), parentPane);

        Scene layoutListScene = new Scene(masterLayout, 0, 0);
        layoutListScene.getStylesheets().add("file:shinyTracker.css");

        if(layoutListStage.getScene() == null)
            layoutListStage.initStyle(StageStyle.UNDECORATED);

        layoutListStage.setScene(layoutListScene);
        HuntController.makeDraggable(layoutListScene);
        layoutListStage.show();

        newLayoutButton.setOnAction(e -> {
            TextInputDialog newNameDialog = new TextInputDialog();
            newNameDialog.setTitle("New Layout Name");
            newNameDialog.setHeaderText("Enter name of new layout.");
            newNameDialog.initStyle(StageStyle.UNDECORATED);
            HuntController.makeDraggable(newNameDialog.getDialogPane().getScene());
            newNameDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
            newNameDialog.showAndWait().ifPresent(f -> {
                SaveData.saveLayout(newNameDialog.getEditor().getText(), windowLayout, true);
                currentLayout = newNameDialog.getEditor().getText();
                HuntController.saveHuntOrder();
                showLayoutList();
            });
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
