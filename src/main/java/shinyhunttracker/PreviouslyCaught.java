package shinyhunttracker;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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

import static java.lang.Integer.parseInt;
import static shinyhunttracker.ElementSettings.*;

class PreviouslyCaught {
    static Stage windowStage = new Stage();
    static AnchorPane windowLayout = new AnchorPane();
    static int displayCaught = 0;
    static int displayPrevious = 0;

    static Stage previouslyCaughtSettingsStage = new Stage();
    static VBox previouslyCaughtSettingsLayout = new VBox();
    static Accordion settingsAccordion = new Accordion();
    static TextField numberCaughtField = new TextField();

    /**
     * Creates elements for previously caught settings
     */
    public static void previouslyCaughtPokemonSettings() {
        if (previouslyCaughtSettingsLayout.getChildren().size() != 0) {
            previouslyCaughtSettingsStage.show();
            return;
        }

        //Field for user to load last n caught pokemon
        Label numberCaught = new Label("Display Previously Caught");
        numberCaughtField.setMaxWidth(50);
        numberCaughtField.setPromptText(String.valueOf(displayCaught));

        HBox numberPreviouslyCaught = new HBox();
        numberPreviouslyCaught.setAlignment(Pos.CENTER);
        numberPreviouslyCaught.setSpacing(5);
        numberPreviouslyCaught.setPadding(new Insets(10, 0, 0, 10));

        //List button to display all caught pokemon
        Button previouslyCaughtList = new Button("List");
        numberPreviouslyCaught.getChildren().addAll(numberCaught, numberCaughtField, previouslyCaughtList);

        //Add background settings
        settingsAccordion.getPanes().add(ElementSettings.createBackgroundSettings(windowStage, windowLayout));

        //Layout button to display list of saved layouts
        HBox layoutButton = new HBox();
        layoutButton.setAlignment(Pos.CENTER);
        layoutButton.setPadding(new Insets(0, 0, 10, 0));
        Button layoutSettingsButton = new Button("Layouts");
        layoutButton.getChildren().add(layoutSettingsButton);

        previouslyCaughtSettingsLayout = new VBox();
        previouslyCaughtSettingsLayout.setAlignment(Pos.TOP_CENTER);
        previouslyCaughtSettingsLayout.setSpacing(10);
        previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, settingsAccordion, layoutButton);

        //Adds scroll bar
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setId("background");
        scrollPane.setContent(previouslyCaughtSettingsLayout);

        //Adds new title bar
        VBox masterLayout = new VBox();
        masterLayout.setId("background");
        masterLayout.getChildren().addAll(HuntController.titleBar(previouslyCaughtSettingsStage), scrollPane);

        Scene previouslyCaughtSettingsScene = new Scene(masterLayout, 0, 0);
        previouslyCaughtSettingsScene.getStylesheets().add("file:shinyTracker.css");

        if(previouslyCaughtSettingsStage.getScene() == null)
            previouslyCaughtSettingsStage.initStyle(StageStyle.UNDECORATED);

        previouslyCaughtSettingsStage.setScene(previouslyCaughtSettingsScene);
        HuntController.makeDraggable(previouslyCaughtSettingsScene);

        //Caps window height at 540
        settingsAccordion.heightProperty().addListener((o, oldVal, newVal) -> {
            if(settingsAccordion.getHeight() + 125 <= 540)
                previouslyCaughtSettingsStage.setHeight(settingsAccordion.getHeight() + 125);
            else
                previouslyCaughtSettingsStage.setHeight(540);
            previouslyCaughtSettingsStage.setWidth(315);
        });

        numberCaughtField.setOnAction(e -> {
            try {
                //Captures user input so that window doesn't open if non-int is entered
                int newDisplay = parseInt(numberCaughtField.getText().replaceAll(",", ""));

                //If the previous number of pokemon displayed is 0, it creates a new blank window
                if (displayCaught == 0)
                    createPreviouslyCaughtPokemonWindow();

                //updates displayPrevious and displayCaught
                displayPrevious = displayCaught;
                displayCaught = newDisplay;

                //Closes window and removes settings if new display = 0
                if (displayCaught == 0) {
                    windowStage.close();
                    settingsAccordion.getPanes().remove(0, settingsAccordion.getPanes().size() - 1);
                } else {
                    addPreviouslyCaughtPokemon();
                    windowStage.show();
                }
                numberCaughtField.setText("");
                numberCaughtField.setPromptText(String.valueOf(displayCaught));
                previouslyCaughtSettingsStage.toFront();
            } catch (NumberFormatException f) {
                numberCaughtField.setText("");
            }
        });

        previouslyCaughtList.setOnAction(e -> displayPreviouslyCaughtList());

        windowStage.setOnCloseRequest(e -> {
            displayCaught = 0;
            numberCaughtField.setPromptText("0");
            settingsAccordion.getPanes().remove(0, settingsAccordion.getPanes().size() - 1);
        });

        layoutSettingsButton.setOnAction(e -> showLayoutList());

        previouslyCaughtSettingsStage.show();
        previouslyCaughtSettingsStage.setOnCloseRequest(e -> previouslyCaughtSettingsStage.hide());
    }

    /**
     * Creates empty window for previously caught pokemon
     */
    public static void createPreviouslyCaughtPokemonWindow() {
        windowLayout = new AnchorPane();
        Scene previousHuntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(previousHuntScene);
        windowStage.setTitle("Previously Caught Pokemon");
        windowStage.show();
    }

    /**
     * Refreshes previously caught pokemon window
     */
    public static void refreshPreviouslyCaughtPokemon() {
        //Save layout with long name, so that it doesn't accidentally delete any of the user's layouts
        //This is to preserve where the elements are when refreshing, even if the layout isn't saved or updated.
        SaveData.saveLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", windowLayout, false);

        //removes all elements and re-adds them
        settingsAccordion.getPanes().remove(0, settingsAccordion.getPanes().size() - 1);
        windowLayout.getChildren().remove(0, windowLayout.getChildren().size());
        addPreviouslyCaughtPokemon();

        //Loads and deletes the layout with the really long name
        SaveData.loadLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", windowLayout, false);
        SaveData.removeLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", false);
    }

    /**
     * Create elements of the last n previously caught pokemon
     */
    public static void addPreviouslyCaughtPokemon() {
        //Removes elements if the number that needs to be displayed goes down
        if (displayCaught < displayPrevious) {
            windowLayout.getChildren().remove(displayCaught * 4, windowLayout.getChildren().size());
            settingsAccordion.getPanes().remove(displayCaught * 4, settingsAccordion.getPanes().size() - 1);
            previouslyCaughtSettingsLayout.getChildren().remove(displayCaught * 5 + 3, previouslyCaughtSettingsLayout.getChildren().size());
            return;
        }

        try {
            JSONArray caughtPokemonList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/caughtPokemon.json")));

            //Makes sure that the user doesn't load more than what is saved
            int caughtListSize = caughtPokemonList.length();
            if (caughtListSize < displayCaught)
                displayCaught = caughtListSize;

            //Creates elements from caught list from displayPrevious up to displayCaught
            double widthTotal = 0;
            for (int i = caughtListSize - displayPrevious - 1; i >= (caughtListSize - displayCaught); i--) {
                JSONObject caughtData = caughtPokemonList.getJSONObject(i);

                Game caughtGame = new Game(caughtData.getInt("game"));

                Pokemon previouslyCaughtPokemon = new Pokemon(caughtData.getInt("pokemon"));
                previouslyCaughtPokemon.setForm(caughtData.getInt("form"));
                ImageView sprite = new ImageView();
                sprite.setImage(FetchImage.getImage(new ProgressIndicator(), sprite, previouslyCaughtPokemon, caughtGame));

                Text pokemon = new Text(previouslyCaughtPokemon.getName());
                Text method = new Text(caughtGame.getName());
                Text encounters = new Text(String.valueOf(caughtData.getInt("encounters")));

                windowLayout.getChildren().addAll(sprite, pokemon, method, encounters);

                pokemon.setStroke(Color.web("0x00000000"));
                method.setStroke(Color.web("0x00000000"));
                encounters.setStroke(Color.web("0x00000000"));

                double currentImageWidth = 200;

                sprite.setLayoutX(widthTotal + currentImageWidth / 2);
                sprite.setLayoutY(200);

                pokemon.setLayoutX(widthTotal + currentImageWidth / 2);
                pokemon.setLayoutY(75);

                method.setLayoutX(widthTotal + currentImageWidth / 2);
                method.setLayoutY(90);

                encounters.setLayoutX(widthTotal + currentImageWidth / 2);
                encounters.setLayoutY(105);

                widthTotal += currentImageWidth;

                quickEdit(sprite);
                quickEdit(pokemon);
                quickEdit(method);
                quickEdit(encounters);

                TitledPane spriteSettings = createImageSettings(windowLayout, sprite, previouslyCaughtPokemon, caughtGame);
                TitledPane pokemonLabelSettings = createLabelSettings(pokemon, "Pokemon");
                TitledPane methodLabelSettings = createLabelSettings(method, "Method");
                TitledPane encountersLabelSettings = createLabelSettings(encounters, "Encounters");
                encountersLabelSettings.setPadding(new Insets(0, 0, 25, 0));

                settingsAccordion.getPanes().add(0, encountersLabelSettings);
                settingsAccordion.getPanes().add(0, methodLabelSettings);
                settingsAccordion.getPanes().add(0, pokemonLabelSettings);
                settingsAccordion.getPanes().add(0, spriteSettings);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates list of previously caught hunts
     */
    static Stage prevCatchesStage = new Stage();
    public static void displayPreviouslyCaughtList() {
        GridPane previousCatches = new GridPane();
        previousCatches.setHgap(20);
        previousCatches.setVgap(5);
        previousCatches.setPadding(new Insets(10, 10, 10, 10));

        //Caps height at 540
        previousCatches.heightProperty().addListener((o, oldVal, newVal) -> {
            if(previousCatches.getHeight() + 40 <= 540) {
                prevCatchesStage.setHeight(previousCatches.getHeight() + 40);
                prevCatchesStage.setWidth(previousCatches.getWidth());
            }
            else {
                prevCatchesStage.setHeight(540);
                prevCatchesStage.setWidth(previousCatches.getWidth() + 10);
            }
        });

        try {
            //Read JSON file
            JSONArray huntList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/caughtPokemon.json")));

            //adds data to grid pane
            for(int i = huntList.length() - 1; i >= 0; i--){
                JSONObject huntData = (JSONObject) huntList.get(i);

                Pokemon pokemon = new Pokemon(huntData.getInt("pokemon"));
                Game game = new Game(huntData.getInt("game"));
                Method method = new Method(huntData.getInt("method"));

                int row = previousCatches.getRowCount();
                Label caughtNumber = new Label(String.valueOf(huntList.length() - i));
                GridPane.setHalignment(caughtNumber, HPos.CENTER);
                GridPane.setValignment(caughtNumber, VPos.CENTER);
                previousCatches.add(caughtNumber, 0, row);

                Label pokemonLabel = new Label(pokemon.getName());
                GridPane.setHalignment(pokemonLabel, HPos.CENTER);
                GridPane.setValignment(pokemonLabel, VPos.CENTER);
                previousCatches.add(pokemonLabel, 1, row);

                Label gameLabel = new Label(game.getName());
                GridPane.setHalignment(gameLabel, HPos.CENTER);
                GridPane.setValignment(gameLabel, VPos.CENTER);
                previousCatches.add(gameLabel, 2, row);

                Label methodLabel = new Label(method.getName());
                GridPane.setHalignment(methodLabel, HPos.CENTER);
                GridPane.setValignment(methodLabel, VPos.CENTER);
                previousCatches.add(methodLabel, 3, row);

                Label encounters = new Label(String.format("%,2d", Integer.parseInt(huntData.get("encounters").toString())));
                GridPane.setHalignment(encounters, HPos.CENTER);
                GridPane.setValignment(encounters, VPos.CENTER);
                previousCatches.add(encounters, 4, row);
            }

            //closes the stage if there are no elements
            if(previousCatches.getRowCount() == 0) {
                prevCatchesStage.close();
                return;
            }
        }catch (IOException f) {
            f.printStackTrace();
        }

        //add scroll bar
        ScrollPane parentPane = new ScrollPane();
        parentPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        parentPane.setId("background");
        parentPane.setContent(previousCatches);

        //add custom title bar
        VBox masterLayout = new VBox();
        masterLayout.setId("background");
        masterLayout.getChildren().addAll(HuntController.titleBar(prevCatchesStage), parentPane);

        Scene previousHuntsScene = new Scene(masterLayout, 0, 0);
        previousHuntsScene.getStylesheets().add("file:shinyTracker.css");

        if(prevCatchesStage.getScene() == null)
            prevCatchesStage.initStyle(StageStyle.UNDECORATED);

        prevCatchesStage.setScene(previousHuntsScene);
        HuntController.makeDraggable(previousHuntsScene);
        prevCatchesStage.show();
    }

    /**
     * Creates a list of saved layouts for user to load, update, or delete
     */
    static Stage layoutListStage = new Stage();
    public static void showLayoutList(){
        GridPane layoutListLayout = new GridPane();
        layoutListLayout.setHgap(10);
        layoutListLayout.setVgap(5);
        layoutListLayout.setPadding(new Insets(10, 10, 10, 10));

        //caps height at 540
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

        try {
            JSONArray layoutList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/caughtLayouts.json")));

            //adds data to grid pane
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
                    SaveData.saveLayout(layoutObject.get(0).toString(), windowLayout, false);
                    showLayoutList();
                });
                loadButton.setOnAction(e -> {
                    //ensures that the proper number of elements are in the window to load layout to if display caught is 0
                    if (displayCaught == 0) {
                        createPreviouslyCaughtPokemonWindow();
                        displayPrevious = displayCaught;
                        displayCaught = (layoutObject.length() - 2) / 4;
                        addPreviouslyCaughtPokemon();
                        windowStage.show();
                        numberCaughtField.setPromptText(String.valueOf(displayCaught));
                    }

                    SaveData.loadLayout(layoutObject.get(0).toString(), windowLayout, false);
                });
                removeButton.setOnAction(e -> {
                    SaveData.removeLayout(layoutObject.get(0).toString(), false);
                    showLayoutList();
                });
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        //opens list of layouts for user to load from
        Button newLayoutButton = new Button("Add Layout");
        newLayoutButton.disableProperty().bind(windowStage.showingProperty().not());
        GridPane.setColumnSpan(newLayoutButton, 4);
        GridPane.setHalignment(newLayoutButton, HPos.CENTER);
        GridPane.setValignment(newLayoutButton, VPos.CENTER);
        layoutListLayout.add(newLayoutButton, 0, layoutListLayout.getRowCount());

        //add scroll bar
        ScrollPane parentPane = new ScrollPane();
        parentPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        parentPane.setId("background");
        parentPane.setContent(layoutListLayout);

        //add custom title bar
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
                showLayoutList();
            });
        });
    }

    /**
     * Closes all Previously Caught associated windows
     */
    public static void close() {
        windowStage.close();
        previouslyCaughtSettingsStage.close();
        prevCatchesStage.close();
        layoutListStage.close();
    }

    /**
     * @return if window is showing
     */
    public static boolean isShowing() { return windowStage.isShowing(); }
}
