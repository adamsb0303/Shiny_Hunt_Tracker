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
import javafx.stage.Modality;
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
    static ColorPicker backgroundColorPicker = new ColorPicker();
    static TextField numberCaughtField = new TextField();

    //creates elements for previously caught element settings
    public static void previouslyCaughtPokemonSettings() {
        if (previouslyCaughtSettingsLayout.getChildren().size() != 0) {
            previouslyCaughtSettingsStage.show();
            return;
        }

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
        Button layoutSettingsButton = new Button("Layouts");

        HBox layoutSettings = new HBox();
        layoutSettings.setAlignment(Pos.CENTER);
        layoutSettings.setSpacing(5);
        layoutSettings.setPadding(new Insets(10, 0, 0, 10));
        layoutSettings.getChildren().addAll(layoutLabel, layoutSettingsButton);

        previouslyCaughtSettingsLayout = new VBox();
        previouslyCaughtSettingsLayout.setAlignment(Pos.TOP_CENTER);
        previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, backgroundColor, layoutSettings);

        ScrollPane scrollPane = new ScrollPane(previouslyCaughtSettingsLayout);

        Scene previouslyCaughtSettingsScene = new Scene(scrollPane, 300, 500);
        previouslyCaughtSettingsStage.setScene(previouslyCaughtSettingsScene);

        numberCaughtField.setOnAction(e -> {
            try {
                if (displayCaught == 0)
                    createPreviouslyCaughtPokemonWindow();
                displayPrevious = displayCaught;
                displayCaught = parseInt(numberCaughtField.getText());
                if (displayCaught == 0) {
                    windowStage.close();
                    backgroundColorPicker.setDisable(true);
                    previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
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

        backgroundColorPicker.setOnAction(e -> windowLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

        windowStage.setOnCloseRequest(e -> {
            displayCaught = 0;
            numberCaughtField.setPromptText("0");
            previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
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
        backgroundColorPicker.setDisable(false);
        windowStage.show();
    }

    //refreshes previously caught pokemon window
    public static void refreshPreviouslyCaughtPokemon() {
        SaveData.saveLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", windowLayout, false);

        previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
        windowLayout.getChildren().remove(0, windowLayout.getChildren().size());
        addPreviouslyCaughtPokemon();

        SaveData.loadLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", windowLayout, false);
        SaveData.removeLayout("temporaryTransitionLayoutForRefreshingPreviouslyCaughtWindow", false);
    }

    //create elements of the last x previously caught pokemon
    public static void addPreviouslyCaughtPokemon() {
        if (displayCaught < displayPrevious) {
            windowLayout.getChildren().remove(displayCaught * 4, windowLayout.getChildren().size());
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
                VBox spriteSettings = createImageSettings(windowLayout, sprite, previouslyCaughtPokemon, caughtGame);
                VBox pokemonLabelSettings = createLabelSettings(pokemon, "Pokemon");
                VBox methodLabelSettings = createLabelSettings(method, "Method");
                VBox encountersLabelSettings = createLabelSettings(encounters, "Encounters");
                previouslyCaughtSettingsLayout.getChildren().addAll(new Text("-------------------------------------------"), spriteSettings, pokemonLabelSettings, methodLabelSettings, encountersLabelSettings);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void displayPreviouslyCaughtList() {
        Stage displayCaughtListStage = new Stage();
        displayCaughtListStage.setTitle("Previously Caught Pokemon");
        TreeView<String> previouslyCaughtView = new TreeView<>();
        TreeItem<String> previoulyCaughtRoot = new TreeItem<>();

        try (FileReader reader = new FileReader("SaveData/caughtPokemon.json")) {
            JSONParser jsonParser = new JSONParser();
            JSONArray caughtPokemonList = (JSONArray) jsonParser.parse(reader);

            for (Object i : caughtPokemonList) {
                JSONObject caughtData = (JSONObject) i;
                Pokemon caughtPokemon = new Pokemon(Integer.parseInt(caughtData.get("pokemon").toString()));
                Game caughtGame = new Game(Integer.parseInt(caughtData.get("game").toString()));
                Method caughtMethod = new Method(Integer.parseInt(caughtData.get("method").toString()));
                TreeItem<String> item = new TreeItem<>(caughtPokemon.getName() + " | " + caughtGame.getName() + " | " + caughtMethod.getName() + " | " + caughtData.get("encounters").toString() + " encounters");
                previoulyCaughtRoot.getChildren().add(item);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        previouslyCaughtView.setRoot(previoulyCaughtRoot);
        previouslyCaughtView.setShowRoot(false);

        VBox previousHuntsLayout = new VBox();
        previousHuntsLayout.getChildren().addAll(previouslyCaughtView);

        Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
        displayCaughtListStage.setScene(previousHuntsScene);
        displayCaughtListStage.show();
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
            layoutListStage.setWidth(layoutListLayout.getWidth() + 15);
            layoutListStage.setHeight(layoutListLayout.getHeight() + 40);
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

        Pane parentPane = new Pane();
        parentPane.setId("background");
        parentPane.getChildren().add(layoutListLayout);

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
