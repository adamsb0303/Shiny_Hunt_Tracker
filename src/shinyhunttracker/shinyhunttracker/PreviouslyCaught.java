package shinyhunttracker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static java.lang.Integer.parseInt;

class PreviouslyCaught extends Window {
    int displayCaught;
    int displayPrevious = 0;

    Stage previouslyCaughtSettingsStage = new Stage();
    VBox previouslyCaughtSettingsLayout = new VBox();
    ColorPicker backgroundColorPicker = new ColorPicker();
    TextField numberCaughtField = new TextField();

    PreviouslyCaught(int displayCaught){
        this.displayCaught = displayCaught;
        this.currentLayout = "null";
        windowStage.setTitle("Previously Caught Pokemon");
    }

    //creates window with previously caught pokemon
    public void createPreviouslyCaughtPokemonWindow(){
        windowLayout = new AnchorPane();
        Scene previousHuntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(previousHuntScene);
        backgroundColorPicker.setDisable(false);
        windowStage.show();
    }

    //creates elements for previously caught element settings
    public void previouslyCaughtPokemonSettings(){
        if(previouslyCaughtSettingsLayout.getChildren().size() == 0) {
            previouslyCaughtSettingsStage.setTitle("Previously Caught Pokemon Settings");

            Label numberCaught = new Label("Display Previously Caught: ");
            numberCaughtField.setMaxWidth(50);
            numberCaughtField.setPromptText(String.valueOf(displayCaught));
            Button previouslyCaughtList = new Button("List");
            HBox numberPreviouslyCaught = new HBox();
            numberPreviouslyCaught.setAlignment(Pos.CENTER);
            numberPreviouslyCaught.setSpacing(5);
            numberPreviouslyCaught.setPadding(new Insets(10, 0, 0, 10));
            numberPreviouslyCaught.getChildren().addAll(numberCaught, numberCaughtField, previouslyCaughtList);

            Label backgroundColorLabel = new Label("Background: ");
            backgroundColorPicker.setDisable(!windowStage.isShowing());
            if (windowLayout.getBackground() != null)
                backgroundColorPicker.setValue((Color) windowLayout.getBackground().getFills().get(0).getFill());

            HBox backgroundColor = new HBox();
            backgroundColor.setAlignment(Pos.CENTER);
            backgroundColor.setSpacing(5);
            backgroundColor.setPadding(new Insets(10, 0, 0, 10));
            backgroundColor.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);

            Label layoutLabel = new Label("Layout: ");
            Button loadLayout = new Button("Load");
            Button saveLayout = new Button("Save");
            saveLayout.disableProperty().bind(windowStage.showingProperty().not());

            HBox layoutSettings = new HBox();
            layoutSettings.setAlignment(Pos.CENTER);
            layoutSettings.setSpacing(5);
            layoutSettings.setPadding(new Insets(10, 0, 0, 10));
            layoutSettings.getChildren().addAll(layoutLabel, saveLayout, loadLayout);

            previouslyCaughtSettingsLayout = new VBox();
            previouslyCaughtSettingsLayout.setAlignment(Pos.TOP_CENTER);
            previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, backgroundColor, layoutSettings);

            ScrollPane scrollPane = new ScrollPane(previouslyCaughtSettingsLayout);

            Scene previouslyCaughtSettingsScene = new Scene(scrollPane, 263, 500);
            previouslyCaughtSettingsStage.setScene(previouslyCaughtSettingsScene);

            numberCaughtField.setOnAction(e ->{
                try{
                    if(displayCaught == 0)
                        createPreviouslyCaughtPokemonWindow();
                    displayPrevious = displayCaught;
                    displayCaught = parseInt(numberCaughtField.getText());
                    if(displayCaught == 0) {
                        windowStage.close();
                        backgroundColorPicker.setDisable(true);
                        previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
                    }
                    else {
                        windowStage.show();
                        addPreviouslyCaughtPokemon(displayCaught);
                    }
                    numberCaughtField.setText("");
                    numberCaughtField.setPromptText(String.valueOf(displayCaught));
                }catch(NumberFormatException f){
                    numberCaughtField.setText("");
                }
            });

            previouslyCaughtList.setOnAction(e -> displayPreviouslyCaughtList());

            backgroundColorPicker.setOnAction(e -> windowLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

            windowStage.setOnCloseRequest(e -> {
                displayCaught = 0;
                numberCaughtField.setPromptText("0");
                previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
            });

            saveLayout.setOnAction(e -> saveLayout());
            loadLayout.setOnAction(e -> loadLayoutMenu());
        }
        previouslyCaughtSettingsStage.show();
        previouslyCaughtSettingsStage.setOnCloseRequest(e -> previouslyCaughtSettingsStage.hide());
    }

    //refreshes previously caught pokemon window
    public void refreshPreviouslyCaughtPokemon(){
        SaveData.saveLayout("Previously-Caught/layoutTransition", windowLayout, false);

        previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
        windowLayout.getChildren().remove(0,windowLayout.getChildren().size());
        addPreviouslyCaughtPokemon(displayCaught);

        SaveData.loadLayout("Previously-Caught/layoutTransition", windowLayout, false);
    }

    //create elements of the last x previously caught pokemon
    public void addPreviouslyCaughtPokemon(int previouslyCaught){
/*
        if(previouslyCaught < displayPrevious){
            windowLayout.getChildren().remove(previouslyCaught * 4, windowLayout.getChildren().size());
            previouslyCaughtSettingsLayout.getChildren().remove(previouslyCaught * 5 + 3, previouslyCaughtSettingsLayout.getChildren().size());
        }
        SaveData SaveData = new SaveData();
        int numberCaught = SaveData.getfileLength("CaughtPokemon");
        if(numberCaught < previouslyCaught)
            previouslyCaught = numberCaught;
        double widthTotal = 0;
        for(int i = numberCaught - 1; i >= (numberCaught - previouslyCaught); i--){
            if((i - numberCaught) * -1 > displayPrevious) {
                String line = SaveData.getLinefromFile(i, "CaughtPokemon");
                String[] data = line.split(",");
                Game caughtGame = new Game(data[2], parseInt(data[3]));
                int selectedPokemonGeneration = new Pokemon (data[0], Pokedex.getPokedex()).getGeneration();
                if (caughtGame.getGeneration() < selectedPokemonGeneration)
                    caughtGame.setGeneration(selectedPokemonGeneration);
                Pokemon previouslyCaughtPokemon = new Pokemon(data[0], Pokedex.getPokedex());
                previouslyCaughtPokemon.setForm(data[1]);
                ImageView sprite = new ImageView();
                setPokemonSprite(sprite, previouslyCaughtPokemon, caughtGame);
                if(previouslyCaughtPokemon.getForm() != null && !previouslyCaughtPokemon.getForm().equals("null"))
                    setAlternateSprite(previouslyCaughtPokemon, caughtGame, sprite);
                Text pokemon = new Text(data[0]);
                Text method = new Text(data[4]);
                Text encounters = new Text(data[6]);

                windowLayout.getChildren().addAll(sprite, pokemon, method, encounters);

                pokemon.setStroke(Color.web("0x00000000"));
                method.setStroke(Color.web("0x00000000"));
                encounters.setStroke(Color.web("0x00000000"));

                double currentImageWidth = sprite.getImage().getWidth() * sprite.getScaleX();

                sprite.setLayoutX(widthTotal + currentImageWidth / 2);
                sprite.setLayoutY((sprite.getImage().getHeight() * sprite.getScaleY()));

                pokemon.setLayoutX(widthTotal + currentImageWidth / 2);
                pokemon.setLayoutY(75);

                method.setLayoutX(widthTotal + currentImageWidth / 2);
                method.setLayoutY(90);

                encounters.setLayoutX(widthTotal + currentImageWidth / 2);
                encounters.setLayoutY(105);

                widthTotal += sprite.getImage().getWidth() * sprite.getScaleX();

                quickEdit(sprite);
                quickEdit(pokemon);
                quickEdit(method);
                quickEdit(encounters);
                VBox spriteSettings = createImageSettings(sprite, new Pokemon(data[0], 0), caughtGame);
                VBox pokemonLabelSettings = createLabelSettings(pokemon, "Pokemon");
                VBox methodLabelSettings = createLabelSettings(method, "Method");
                VBox encountersLabelSettings = createLabelSettings(encounters, "Encounters");
                previouslyCaughtSettingsLayout.getChildren().addAll(new Text("-------------------------------------------"), spriteSettings, pokemonLabelSettings, methodLabelSettings, encountersLabelSettings);
            }
        }
*/
    }

    public void displayPreviouslyCaughtList(){
        Stage displayCaughtListStage = new Stage();
        displayCaughtListStage.setTitle("Previously Caught Pokemon");
        TreeView<String> previouslyCaughtView = new TreeView<>();
        TreeItem<String> previoulyCaughtRoot = new TreeItem<>();

        //SaveData previoulyCaughtData = new SaveData();

        /*for(int i = previoulyCaughtData.getfileLength("CaughtPokemon") - 1; i >= 0 ; i--){
            String line = previoulyCaughtData.getLinefromFile(i, "CaughtPokemon");
            String[] data = line.split(",");
            String name = data[0];
            String game = data[2];
            String method = data[4];
            String encounters = data[6];
            makeBranch((previoulyCaughtData.getfileLength("CaughtPokemon") - i) + ") " + name + " | " + game + " | " + method + " | " + encounters + " encounters", previoulyCaughtRoot);
        }*/

        previouslyCaughtView.setRoot(previoulyCaughtRoot);
        previouslyCaughtView.setShowRoot(false);

        VBox previousHuntsLayout = new VBox();
        previousHuntsLayout.getChildren().addAll(previouslyCaughtView);

        Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
        displayCaughtListStage.setScene(previousHuntsScene);
        displayCaughtListStage.show();
    }

    //save layout
    public void saveLayout(){
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
                        SaveData.saveLayout(saveNameText.getText(), windowLayout, false);
                        promptLayoutSaveName.close();
                    }
                });
            });

            oldSaveButton.setOnAction(f -> {
                SaveData.saveLayout(currentLayout, windowLayout, false);
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
                    SaveData.saveLayout(saveNameText.getText(), windowLayout, false);
                    promptLayoutSaveName.close();
                }
            });
        }
    }

    //load layout
    public void loadLayoutMenu(){
        Stage loadSavedLayoutStage = new Stage();
        loadSavedLayoutStage.initModality(Modality.APPLICATION_MODAL);
        loadSavedLayoutStage.setResizable(false);
        loadSavedLayoutStage.setTitle("Select Layout Name");

        TreeView<String> savedLayouts = new TreeView<>();
        TreeItem<String> root = new TreeItem<>();
        /*for(int i = 0; i < data.getfileLength("Layouts/Previously-Caught/~Layouts"); i++){
            makeBranch(data.getLinefromFile(i, "Layouts/Previously-Caught/~Layouts"), root);
        }*/
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
                    loadSavedLayoutStage.close();
                    loadLayout();
                });
    }

    public void loadLayout(){
        //SaveData data = new SaveData();
        displayPrevious = 0;
/*
        displayCaught = parseInt(data.getLinefromFile(data.getfileLength("Layouts/Previously-Caught/" + currentLayout) - 1, "Layouts/Previously-Caught/" + currentLayout));
*/
        numberCaughtField.setPromptText(String.valueOf(displayCaught));
        if(previouslyCaughtSettingsLayout.getChildren().size() > 0)
            previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
        windowLayout.getChildren().remove(0, windowLayout.getChildren().size());
        if(!windowStage.isShowing())
            createPreviouslyCaughtPokemonWindow();
        previouslyCaughtPokemonSettings();
        addPreviouslyCaughtPokemon(displayCaught);
        SaveData.loadLayout(currentLayout, windowLayout, false);
    }

    public Stage getSettingsStage(){
        return previouslyCaughtSettingsStage;
    }

    public String getCurrentLayout(){
        return currentLayout;
    }

    public void setCurrentLayout(String currentLayout){
        this.currentLayout = currentLayout;
    }
}
