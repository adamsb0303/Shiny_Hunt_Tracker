package shinyhunttracker;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
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

    //creates elements for previously caught element settings
    public static void previouslyCaughtPokemonSettings() {
        if (previouslyCaughtSettingsLayout.getChildren().size() != 0) {
            previouslyCaughtSettingsStage.show();
            return;
        }

        previouslyCaughtSettingsStage.setTitle("Previously Caught Pokemon Settings");

        Label numberCaught = new Label("Display Previously Caught");
        numberCaughtField.setMaxWidth(50);
        numberCaughtField.setPromptText(String.valueOf(displayCaught));
        Button previouslyCaughtList = new Button("List");
        HBox numberPreviouslyCaught = new HBox();
        numberPreviouslyCaught.setAlignment(Pos.CENTER);
        numberPreviouslyCaught.setSpacing(5);
        numberPreviouslyCaught.setPadding(new Insets(10, 0, 0, 10));
        numberPreviouslyCaught.getChildren().addAll(numberCaught, numberCaughtField, previouslyCaughtList);

        settingsAccordion.getPanes().add(ElementSettings.createBackgroundSettings(windowStage, windowLayout));

        HBox layoutButton = new HBox();
        layoutButton.setAlignment(Pos.CENTER);
        layoutButton.setPadding(new Insets(0, 0, 10, 0));
        Button layoutSettingsButton = new Button("Layouts");
        layoutButton.getChildren().add(layoutSettingsButton);

        previouslyCaughtSettingsLayout = new VBox();
        previouslyCaughtSettingsLayout.setAlignment(Pos.TOP_CENTER);
        previouslyCaughtSettingsLayout.setSpacing(10);
        previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, settingsAccordion, layoutButton);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setId("background");
        scrollPane.setContent(previouslyCaughtSettingsLayout);

        Scene previouslyCaughtSettingsScene = new Scene(scrollPane, 0, 0);
        previouslyCaughtSettingsScene.getStylesheets().add("file:shinyTracker.css");
        previouslyCaughtSettingsStage.setScene(previouslyCaughtSettingsScene);

        settingsAccordion.heightProperty().addListener((o, oldVal, newVal) -> {
            if(settingsAccordion.getHeight() + 125 <= 540)
                previouslyCaughtSettingsStage.setHeight(settingsAccordion.getHeight() + 125);
            else
                previouslyCaughtSettingsStage.setHeight(540);
            previouslyCaughtSettingsStage.setWidth(315);
        });

        numberCaughtField.setOnAction(e -> {
            try {
                if (displayCaught == 0)
                    createPreviouslyCaughtPokemonWindow();
                displayPrevious = displayCaught;
                displayCaught = parseInt(numberCaughtField.getText());
                if (displayCaught == 0) {
                    windowStage.close();
                    settingsAccordion.getPanes().remove(0, settingsAccordion.getPanes().size() - 1);
                } else {
                    addPreviouslyCaughtPokemon();
                    windowStage.show();
                }
                numberCaughtField.setText("");
                numberCaughtField.setPromptText(String.valueOf(displayCaught));
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

    //creates window with previously caught pokemon
    public static void createPreviouslyCaughtPokemonWindow() {
        windowLayout = new AnchorPane();
        Scene previousHuntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(previousHuntScene);
        windowStage.setTitle("Previously Caught Pokemon");
        windowStage.show();
    }

    //refreshes previously caught pokemon window
    public static void refreshPreviouslyCaughtPokemon() {
        SaveData.saveLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", windowLayout, false);

        settingsAccordion.getPanes().remove(0, settingsAccordion.getPanes().size() - 1);
        windowLayout.getChildren().remove(0, windowLayout.getChildren().size());
        addPreviouslyCaughtPokemon();

        SaveData.loadLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", windowLayout, false);
        SaveData.removeLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", false);
    }

    //create elements of the last x previously caught pokemon
    public static void addPreviouslyCaughtPokemon() {
        if (displayCaught < displayPrevious) {
            windowLayout.getChildren().remove(displayCaught * 4, windowLayout.getChildren().size());
            settingsAccordion.getPanes().remove(displayCaught * 4, settingsAccordion.getPanes().size() - 1);
            previouslyCaughtSettingsLayout.getChildren().remove(displayCaught * 5 + 3, previouslyCaughtSettingsLayout.getChildren().size());
            return;
        }

        try (FileReader reader = new FileReader("SaveData/caughtPokemon.json")) {
            JSONParser jsonParser = new JSONParser();
            JSONArray caughtPokemonList = (JSONArray) jsonParser.parse(reader);

            int caughtListSize = caughtPokemonList.size();
            if (caughtListSize < displayCaught)
                displayCaught = caughtListSize;
            double widthTotal = 0;
            for (int i = caughtListSize - displayPrevious - 1; i >= (caughtListSize - displayCaught); i--) {
                JSONObject caughtData = (JSONObject) caughtPokemonList.get(i);

                Game caughtGame = new Game(Integer.parseInt(caughtData.get("game").toString()));

                Pokemon previouslyCaughtPokemon = new Pokemon(Integer.parseInt(caughtData.get("pokemon").toString()));
                previouslyCaughtPokemon.setForm(Integer.parseInt(caughtData.get("form").toString()));
                ImageView sprite = new ImageView();
                FetchImage.setImage(sprite, previouslyCaughtPokemon, caughtGame);

                Text pokemon = new Text(previouslyCaughtPokemon.getName());
                Text method = new Text(caughtGame.getName());
                Text encounters = new Text(caughtData.get("encounters").toString());

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
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    static Stage prevCatchesStage = new Stage();
    public static void displayPreviouslyCaughtList() {
        prevCatchesStage.setTitle("Select a previous hunt");
        GridPane previousCatches = new GridPane();
        previousCatches.setHgap(20);
        previousCatches.setVgap(5);
        previousCatches.setPadding(new Insets(5, 10, 5, 10));

        previousCatches.heightProperty().addListener((o, oldVal, newVal) -> {
            if(previousCatches.getHeight() + 40 <= 540) {
                prevCatchesStage.setHeight(previousCatches.getHeight() + 40);
                prevCatchesStage.setWidth(previousCatches.getWidth() + 10);
            }
            else {
                prevCatchesStage.setHeight(540);
                prevCatchesStage.setWidth(previousCatches.getWidth() + 20);
            }
        } );

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/caughtPokemon.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            for(int i = huntList.size() - 1; i >= 0; i--){
                JSONObject huntData = (JSONObject) huntList.get(i);

                Pokemon pokemon = new Pokemon(Integer.parseInt(huntData.get("pokemon").toString()));
                Game game = new Game(Integer.parseInt(huntData.get("game").toString()));
                Method method = new Method(Integer.parseInt(huntData.get("method").toString()));

                int row = previousCatches.getRowCount();
                Label caughtNumber = new Label(String.valueOf(huntList.size() - i));
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

            if(previousCatches.getRowCount() == 0) {
                prevCatchesStage.close();
                return;
            }
        }catch (IOException | ParseException f) {
            f.printStackTrace();
        }

        ScrollPane previousHuntsLayout = new ScrollPane();
        previousHuntsLayout.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        previousHuntsLayout.setId("background");
        previousHuntsLayout.setContent(previousCatches);

        Scene previousHuntsScene = new Scene(previousHuntsLayout, 0, 0);
        previousHuntsScene.getStylesheets().add("file:shinyTracker.css");
        prevCatchesStage.setScene(previousHuntsScene);
        prevCatchesStage.show();
    }

    public static void close() {
        windowStage.close();
        previouslyCaughtSettingsStage.close();
    }

    public static boolean isShowing() {
        return windowStage.isShowing();
    }

    static Stage layoutListStage = new Stage();
    public static void showLayoutList(){
        GridPane layoutListLayout = new GridPane();
        layoutListLayout.setHgap(10);
        layoutListLayout.setVgap(5);
        layoutListLayout.setPadding(new Insets(5, 10, 5, 10));

        layoutListLayout.heightProperty().addListener((o, oldVal, newVal) -> {
            if(layoutListLayout.getHeight() + 40 <= 540) {
                layoutListStage.setHeight(layoutListLayout.getHeight() + 40);
                layoutListStage.setWidth(layoutListLayout.getWidth() + 15);
            }
            else {
                layoutListStage.setHeight(540);
                layoutListStage.setWidth(layoutListLayout.getWidth() + 25);
            }
        });

        try(FileReader reader = new FileReader("SaveData/caughtLayouts.json")){
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
                    SaveData.saveLayout(layoutObject.get(0).toString(), windowLayout, false);
                    showLayoutList();
                });
                loadButton.setOnAction(e -> {
                    if (displayCaught == 0) {
                        createPreviouslyCaughtPokemonWindow();
                        displayPrevious = displayCaught;
                        displayCaught = (layoutObject.size() - 2) / 4;
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
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }

        Button newLayoutButton = new Button("Add Layout");
        newLayoutButton.disableProperty().bind(windowStage.showingProperty().not());
        GridPane.setColumnSpan(newLayoutButton, 4);
        GridPane.setHalignment(newLayoutButton, HPos.CENTER);
        GridPane.setValignment(newLayoutButton, VPos.CENTER);
        layoutListLayout.add(newLayoutButton, 0, layoutListLayout.getRowCount());

        ScrollPane parentPane = new ScrollPane();
        parentPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        parentPane.setId("background");
        parentPane.setContent(layoutListLayout);

        Scene layoutListScene = new Scene(parentPane, 0, 0);
        layoutListScene.getStylesheets().add("file:shinyTracker.css");
        layoutListStage.setTitle("Layouts");
        layoutListStage.setScene(layoutListScene);
        if(!layoutListStage.isShowing())
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
}
