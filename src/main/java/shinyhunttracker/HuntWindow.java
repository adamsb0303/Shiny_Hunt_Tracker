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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Vector;

import static shinyhunttracker.ElementSettings.*;

class HuntWindow {
    //hunt window elements
    Stage windowStage = new Stage();
    AnchorPane windowLayout = new AnchorPane();
    String currentLayout;
    ImageView sprite;
    LinkedHashSet<Integer> familyMembers = new LinkedHashSet<>();
    Vector<ImageView> familySprites = new Vector<>();
    Text currentHuntingMethodText, currentHuntingPokemonText, currentGameText, encountersText, oddFractionText;
    IntegerProperty encounters = new SimpleIntegerProperty();
    IntegerProperty combo = new SimpleIntegerProperty();
    int increment, huntNumber, huntID;
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
        selectedMethod.checkSpecialModifier(selectedGame.getGeneration());
        this.currentLayout = layout;
        this.encounters.setValue(encounters);
        this.combo.setValue(combo);
        selectedMethod.comboExtraRolls(combo);
        this.increment = increment;
        this.huntID = huntID;
    }

    /**
     * Adds all elements to the hunt window
     */
    public void createHuntWindow(){
        //Initializes Labels
        currentHuntingPokemonText = new Text(selectedPokemon.getName());
        currentHuntingMethodText= new Text(selectedMethod.getName());
        currentGameText = new Text(selectedGame.toString());
        oddFractionText = new Text();
        oddFractionText.textProperty().bind(Bindings.createStringBinding(() -> "1/" + simplifyFraction(selectedMethod.comboExtraRolls(combo.getValue()), selectedGame.getOdds()), combo));
        encountersText = new Text();
        encountersText.textProperty().bind(Bindings.createStringBinding(() -> String.format("%,d", encounters.getValue()), encounters));

        //Sets the outlines of the labels to white
        currentHuntingPokemonText.setStroke(Color.web("0x00000000"));
        currentHuntingMethodText.setStroke(Color.web("0x00000000"));
        currentGameText.setStroke(Color.web("0x00000000"));
        oddFractionText.setStroke(Color.web("0x00000000"));
        encountersText.setStroke(Color.web("0x00000000"));

        //Makes labels draggable
        quickEdit(currentHuntingPokemonText);
        quickEdit(currentHuntingMethodText);
        quickEdit(currentGameText);
        quickEdit(oddFractionText);
        quickEdit(encountersText);

        //Creates the pokemon's sprite
        sprite = new ImageView();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.managedProperty().bind(progressIndicator.visibleProperty());
        progressIndicator.visibleProperty().bind(progressIndicator.progressProperty().lessThan(1));
        sprite.setImage(FetchImage.getImage(progressIndicator, sprite, selectedPokemon, selectedGame));
        quickEdit(sprite);

        windowLayout.getChildren().addAll(progressIndicator, sprite);

        //Positions the main sprite and progress indicator
        sprite.setLayoutX(100);
        sprite.setLayoutY(200);

        progressIndicator.setLayoutX(100);
        progressIndicator.setLayoutY(100);

        //Creates list of family members without duplicates
        for(int i = 0; i < selectedPokemon.getFamily().size(); i++){
            familyMembers.addAll(selectedPokemon.getFamily().get(i));
        }
        familyMembers.remove(selectedPokemon.getDexNumber());

        //Creates the pokemon's family's sprites
        for(int i = 0; i < familyMembers.size(); i++){
            Pokemon familyMember = new Pokemon((int) familyMembers.toArray()[i]);
            if(familyMember.getGeneration() > selectedGame.getGeneration()) {
                familyMembers.remove(familyMember.getDexNumber());
                i--;
                continue;
            }

            ProgressIndicator familyProgressIndicator = new ProgressIndicator();
            familyProgressIndicator.managedProperty().bind(progressIndicator.visibleProperty());
            familyProgressIndicator.visibleProperty().bind(progressIndicator.progressProperty().lessThan(1));
            familyProgressIndicator.setLayoutX(100 + 200 * ((i + 1) % 4));
            familyProgressIndicator.setLayoutY(100 + 200 * (int)((i + 1) / 4.0));

            ImageView memberSprite = new ImageView();
            memberSprite.setImage(FetchImage.getImage(familyProgressIndicator, memberSprite, familyMember, selectedGame));
            memberSprite.setLayoutX(100 + 200 * ((i + 1) % 4));
            memberSprite.setLayoutY(200 + 200 * (int)((i + 1) / 4.0));
            memberSprite.setVisible(false);
            quickEdit(memberSprite);
            windowLayout.getChildren().addAll(familyProgressIndicator, memberSprite);
            familySprites.add(memberSprite);
        }

        windowLayout.getChildren().addAll(currentHuntingPokemonText, currentHuntingMethodText, currentGameText, encountersText, oddFractionText);

        //Positions the labels to the right of the sprite images
        currentHuntingPokemonText.setLayoutX(205);
        currentHuntingPokemonText.setLayoutY(15);

        currentHuntingMethodText.setLayoutX(205);
        currentHuntingMethodText.setLayoutY(30);

        currentGameText.setLayoutX(205);
        currentGameText.setLayoutY(45);

        encountersText.setLayoutX(205);
        encountersText.setLayoutY(60);

        oddFractionText.setLayoutX(205);
        oddFractionText.setLayoutY(75);

        //Set scene and show screen
        Scene huntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(huntScene);

        windowStage.show();

        //load layout
        if(currentLayout.compareTo("") != 0)
            SaveData.loadLayout(currentLayout, windowLayout, true);
    }

    /**
     * Creates the settings window
     */
    public void customizeHuntWindowSettings(){
        if(CustomizeHuntStage.getScene() != null) {
            CustomizeHuntStage.show();
            return;
        }

        CustomizeHuntStage.setTitle("Hunt " + huntNumber + " Settings");

        //Settings for all elements
        TitledPane spriteSettings = createImageSettings(windowLayout, sprite, selectedPokemon, selectedGame);
        TitledPane currentPokemonSettings = createLabelSettings(currentHuntingPokemonText, "Pokemon");
        TitledPane currentMethodSettings = createLabelSettings(currentHuntingMethodText, "Method");
        TitledPane currentGameSettings = createLabelSettings(currentGameText, "Game");
        TitledPane encountersSettings = createLabelSettings(encountersText, "Encounters");
        TitledPane oddsFraction = createLabelSettings(oddFractionText, "Odds");

        //Adds game modifiers under the Odds TitlePane
        if(selectedGame.getOddModifiers().length() != 0) {
            VBox gameModifierSettings = new VBox();
            gameModifierSettings.setSpacing(10);

            //Loops through game modifiers in groups of 2 as rows
            for(int i = 0; i < selectedGame.getOddModifiers().length(); i += 2) {
                //row hbox
                HBox row = new HBox();
                row.setAlignment(Pos.CENTER);
                row.setSpacing(20);

                //adds game modifiers in groups of 2
                for(int j = 0; j < 2 && i + j < selectedGame.getOddModifiers().length(); j++) {
                    JSONObject gameMod = (JSONObject) selectedGame.getOddModifiers().get(i + j);
                    CheckBox checkBox = new CheckBox(gameMod.get("name").toString());
                    checkBox.setSelected(selectedMethod.getGameMods().contains(gameMod.get("name").toString()));
                    row.getChildren().add(checkBox);

                    checkBox.selectedProperty().addListener(e -> {
                        if(checkBox.isSelected())
                            selectedMethod.addGameMod(gameMod.get("name").toString(), Integer.parseInt(gameMod.get("extra-rolls").toString()));
                        else
                            selectedMethod.removeGameMod(gameMod.get("name").toString(), Integer.parseInt(gameMod.get("extra-rolls").toString()));
                        //Triggers combo change event to update odds label
                        combo.setValue(combo.getValue() + 1);
                        combo.setValue(combo.getValue() - 1);
                    });
                }
                gameModifierSettings.getChildren().add(row);
            }
            VBox oddPaneSettings = (VBox) oddsFraction.getContent();
            oddPaneSettings.getChildren().add(1, gameModifierSettings);
        }

        if(selectedMethod.getDynamic()){
            VBox comboSettings = new VBox();
            comboSettings.setAlignment(Pos.CENTER);
            comboSettings.setSpacing(10);

            Label comboLength = new Label();
            comboLength.textProperty().bind(Bindings.createStringBinding(() -> "Combo: " + combo.getValue(), combo));

            Button resetComboButton = new Button("Reset Combo");
            resetComboButton.setOnAction(e -> resetCombo());

            comboSettings.getChildren().addAll(comboLength, resetComboButton);

            VBox oddPaneSettings = (VBox) oddsFraction.getContent();
            oddPaneSettings.getChildren().add(1, comboSettings);
        }

        //Adds all settings Panes to accordion
        Accordion settings = new Accordion();
        settings.getPanes().add(spriteSettings);

        for(int i = 0; i < familySprites.size(); i++) {
            TitledPane familyPane = createImageSettings(windowLayout, familySprites.get(i), new Pokemon((int) familyMembers.toArray()[i]), selectedGame);
            settings.getPanes().add(familyPane);
        }

        //add settings for family sprites

        settings.getPanes().addAll(currentPokemonSettings, currentMethodSettings, currentGameSettings, encountersSettings, oddsFraction);

        //Adds background settings to accordion
        settings.getPanes().add(createBackgroundSettings(windowStage, windowLayout));

        Button layoutSettings = new Button("Saved Layouts");

        VBox CustomizeHuntLayout = new VBox();
        CustomizeHuntLayout.setAlignment(Pos.TOP_CENTER);
        CustomizeHuntLayout.setId("background");
        CustomizeHuntLayout.setSpacing(10);
        CustomizeHuntLayout.getChildren().addAll(CustomWindowElements.titleBar(CustomizeHuntStage), settings, layoutSettings);

        Scene CustomizeHuntScene = new Scene(CustomizeHuntLayout, 0, 0);
        CustomizeHuntScene.getStylesheets().add("file:shinyTracker.css");

        if(CustomizeHuntStage.getScene() == null)
            CustomizeHuntStage.initStyle(StageStyle.UNDECORATED);

        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomWindowElements.makeDraggable(CustomizeHuntScene);

        settings.heightProperty().addListener((o, oldVal, newVal) -> {
            CustomizeHuntStage.setHeight(settings.getHeight() + 85);
            CustomizeHuntStage.setWidth(315);
        });

        layoutSettings.setOnAction(e -> showLayoutList());
        CustomizeHuntStage.setOnCloseRequest(e -> CustomizeHuntStage.hide());
        CustomizeHuntStage.show();
    }

    /**
     * Creates a list of layouts that the user can load from, delete, or add too
     */
    Stage layoutListStage = new Stage();
    public void showLayoutList(){
        layoutListStage.setTitle("Saved Layouts");
        GridPane layoutListLayout = new GridPane();
        layoutListLayout.setHgap(10);
        layoutListLayout.setVgap(5);
        layoutListLayout.setPadding(new Insets(10, 10, 10, 10));

        layoutListLayout.heightProperty().addListener((o, oldVal, newVal) -> {
            if(layoutListLayout.getHeight() + 45 <= 540) {
                layoutListStage.setHeight(layoutListLayout.getHeight() + 45);
                layoutListStage.setWidth(layoutListLayout.getWidth());
            }
            else {
                layoutListStage.setHeight(540);
                layoutListStage.setWidth(layoutListLayout.getWidth() + 10);
            }
        });

        try {
            JSONArray layoutList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/huntLayouts.json")));

            //Adds all saved layouts to GridPane
            for(Object i : layoutList){
                JSONArray layoutObject = (JSONArray) i;

                int row = layoutListLayout.getRowCount();

                Label layoutNameLabel = new Label(layoutObject.get(0).toString());
                GridPane.setHalignment(layoutNameLabel, HPos.CENTER);
                GridPane.setValignment(layoutNameLabel, VPos.CENTER);
                layoutListLayout.add(layoutNameLabel, 0, row);

                Button loadButton = new Button("Load");
                GridPane.setHalignment(loadButton, HPos.CENTER);
                GridPane.setValignment(loadButton, VPos.CENTER);
                layoutListLayout.add(loadButton, 1, row);

                Button updateButton = new Button("Update");
                GridPane.setHalignment(updateButton, HPos.CENTER);
                GridPane.setValignment(updateButton, VPos.CENTER);
                layoutListLayout.add(updateButton, 2, row);

                Button removeButton = new Button("Delete");
                GridPane.setHalignment(removeButton, HPos.CENTER);
                GridPane.setValignment(removeButton, VPos.CENTER);
                layoutListLayout.add(removeButton, 3, row);

                loadButton.setOnAction(e -> {
                    SaveData.loadLayout(layoutObject.get(0).toString(), windowLayout, true);
                    currentLayout = layoutObject.get(0).toString();
                    HuntController.saveHuntOrder();
                });
                updateButton.setOnAction(e -> {
                    SaveData.saveLayout(layoutObject.get(0).toString(), windowLayout, true);
                    showLayoutList();
                });
                removeButton.setOnAction(e -> {
                    SaveData.removeLayout(layoutObject.get(0).toString(), true);
                    showLayoutList();
                });
            }
        }catch(IOException e){
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
        masterLayout.getChildren().addAll(CustomWindowElements.titleBar(layoutListStage), parentPane);

        Scene layoutListScene = new Scene(masterLayout, 0, 0);
        layoutListScene.getStylesheets().add("file:shinyTracker.css");

        if(layoutListStage.getScene() == null)
            layoutListStage.initStyle(StageStyle.UNDECORATED);

        layoutListStage.setScene(layoutListScene);
        CustomWindowElements.makeDraggable(layoutListScene);
        layoutListStage.show();

        //Allows user to save a new layout and prompts for name
        newLayoutButton.setOnAction(e -> {
            TextInputDialog newNameDialog = new TextInputDialog();
            newNameDialog.setTitle("New Layout Name");
            newNameDialog.setHeaderText("Enter name of new layout.");
            newNameDialog.initStyle(StageStyle.UNDECORATED);
            CustomWindowElements.makeDraggable(newNameDialog.getDialogPane().getScene());
            newNameDialog.getDialogPane().getStylesheets().add("file:shinyTracker.css");
            newNameDialog.showAndWait().ifPresent(f -> {
                SaveData.saveLayout(newNameDialog.getEditor().getText(), windowLayout, true);
                currentLayout = newNameDialog.getEditor().getText();
                HuntController.saveHuntOrder();
                showLayoutList();
            });
        });
    }

    /**
     * Increases encounters by increment value
     */
    public void incrementEncounters(){
        encounters.setValue(encounters.getValue() + increment);
        combo.setValue(combo.getValue() + increment);
    }

    /**
     * Decreases encounters by increment value
     */
    public void decrementEncounters(){
        if(encounters.getValue() == 0)
            return;
        encounters.setValue(encounters.getValue() - increment);
        combo.setValue(combo.getValue() - increment);
    }

    /**
     * Closes this window and moves pokemon data to caught list
     */
    public void pokemonCaught() {
        SaveData.pokemonCaught(this);
        if(PreviouslyCaught.isShowing())
            PreviouslyCaught.refreshPreviouslyCaughtPokemon();

        close();
    }

    /**
     * Prompts user for phase pokemon, resets encounters, and adds phased pokemon to the caught pokemon file
     */
    public void phaseHunt(int pokemonID){
        SaveData.pokemonCaught(new HuntWindow(new Pokemon(pokemonID), selectedGame, selectedMethod, currentLayout, encounters.getValue(), combo.getValue(), increment, 0));
        resetCombo();
        resetEncounters();
        if(PreviouslyCaught.isShowing())
            PreviouslyCaught.refreshPreviouslyCaughtPokemon();
    }

    /**
     * Resets encounters
     */
    public void resetCombo(){
        combo.setValue(0);
    }

    /**
     * Save hunt and it closes the window
     */
    public void close(){
        windowStage.close();
        CustomizeHuntStage.close();
        layoutListStage.close();
    }

    /**
     * Returns if window is open, since the stage when transparent is different from windowStage
     * @return if the window is showing
     */
    public boolean isShowing(){
        if(windowLayout.getScene() == null)
            return false;
        return windowLayout.getScene().getWindow().isShowing();
    }

    /**
     * Reset Encounters
     */
    public void resetEncounters(){
        encounters.setValue(0);
    }

    /**
     * Simplifies odds fraction for easier reading
     */
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
