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

import java.io.IOException;

public class huntController {
    Stage huntControls = new Stage();
    MenuBar Menu = new MenuBar();
    Menu Settings = new Menu("Settings");
    VBox huntControlsButtonVBox;
    previouslyCaught previousCatches = new previouslyCaught(0);

    huntWindow[] windows = new huntWindow[0];
    String[] currentLayouts = new String[0];
    int huntNum = 1;

    Stage keyBindingSettingsStage = new Stage();

    public huntController(){
        huntControls.setTitle("Hunt Controller");

        huntControlsButtonVBox = new VBox();
        huntControlsButtonVBox.setAlignment(Pos.CENTER);
        huntControlsButtonVBox.setSpacing(5);
        Button addHunt = new Button("Add Hunt");
        huntControlsButtonVBox.getChildren().add(addHunt);

        Menu.getMenus().add(Settings);

        MenuItem keyBinding = new MenuItem("Key Binding");
        Settings.getItems().add(keyBinding);

        MenuItem previouslyCaught = new MenuItem("Previously Caught Settings");
        Settings.getItems().add(previouslyCaught);

        BorderPane huntControlsLayout = new BorderPane();
        huntControlsLayout.setTop(Menu);
        huntControlsLayout.setCenter(huntControlsButtonVBox);

        Scene huntControlsScene = new Scene(huntControlsLayout, 350, 100);
        huntControls.setScene(huntControlsScene);
        huntControls.show();

        huntControlsButtonVBox.setOnKeyTyped(e -> {
            for(huntWindow i: windows) {
                if (i.getKeyBinding() == e.getCharacter().toCharArray()[0])
                    i.incrementEncounters();
            }
        });

        keyBinding.setOnAction(e -> keyBindingSettings());

        previouslyCaught.setOnAction(e -> previousCatches.previouslyCaughtPokemonSettings());

        addHunt.setOnAction(e -> {
            //check save data file for previous saves
            //if anything is found, ask the user if they would like to start a new hunt or a previous one
            try {
                SaveData checkForData = new SaveData();
                if(checkForData.getLinefromFile(0, "PreviousHunts") != null) {
                    newOrOld newOrOld = new newOrOld();
                    newOrOld.setController(this);
                    if(windows.length > 0)
                        newOrOld.setCurrentLayout(windows[windows.length-1].getCurrentLayout());
                    newOrOld.newOrOldHunt();
                }else{
                    //creates selection page window
                    FXMLLoader selectionPageLoader = new FXMLLoader();
                    selectionPageLoader.setLocation(getClass().getResource("selectionPage.fxml"));
                    Parent root = selectionPageLoader.load();

                    Stage huntSelectionWindow = new Stage();
                    huntSelectionWindow.setTitle("Shiny Hunt Tracker");
                    huntSelectionWindow.setResizable(false);
                    huntSelectionWindow.setScene(new Scene(root, 750, 480));

                    selectionPageController selectionPageController = selectionPageLoader.getController();
                    selectionPageController.setController(this);
                    if(windows.length > 0)
                        selectionPageController.setCurrentLayout(windows[windows.length-1].getCurrentLayout());
                    huntSelectionWindow.show();
                }
            }catch(IOException f){
                f.printStackTrace();
            }
        });

        huntControls.setOnCloseRequest(e -> closeWindows());
    }

    public void addHuntWindowSettings(huntWindow window){
        HBox huntControlsButtonHBox = new HBox();
        huntControlsButtonHBox.setAlignment(Pos.CENTER);
        huntControlsButtonHBox.setSpacing(10);

        Button encountersButton = new Button("Increment " + window.getHuntNumber());
        huntControlsButtonHBox.getChildren().addAll(encountersButton);
        huntControlsButtonVBox.getChildren().add(huntControlsButtonHBox);

        Menu newHuntSettings = new Menu("Hunt " + window.getHuntNumber() + " Settings");

        MenuItem customizeHuntLayout= new MenuItem("Layout Settings");

        Menu huntSettings = new Menu("Hunt Settings");
        MenuItem increment= new MenuItem("Change Increment");
        MenuItem resetEncounters = new MenuItem("Fail");
        MenuItem phaseHunt = new MenuItem("Phase Hunt");
        MenuItem pokemonCaught = new MenuItem("Pokemon Caught");
        MenuItem resetCombo = new MenuItem("Reset Combo");
        MenuItem saveHunt = new MenuItem("Save Hunt");
        huntSettings.getItems().addAll(increment, resetEncounters, phaseHunt, pokemonCaught, resetCombo, saveHunt);

        newHuntSettings.getItems().addAll(customizeHuntLayout, huntSettings);
        Settings.getItems().add(newHuntSettings);

        encountersButton.setOnAction(e -> window.incrementEncounters());

        customizeHuntLayout.setOnAction(e -> window.CustomizeHuntWindow());
        increment.setOnAction(e -> window.changeIncrement());
        resetEncounters.setOnAction(e -> window.resetEncounters());
        phaseHunt.setOnAction(e -> window.phaseHunt());
        pokemonCaught.setOnAction(e -> {
            window.pokemonCaught();
            removeWindow(window);
            if(previousCatches.getStage().isShowing()) {
                SaveData temp = new SaveData();
                temp.saveLayout("Previously-Caught/layoutTransition", previousCatches.getWindowLayout(), false);
                boolean settingsIsOpen = previousCatches.getSettingsStage().isShowing();
                previousCatches.previouslyCaughtPokemonSettings();
                temp.loadLayout("Previously-Caught/layoutTransition", previousCatches.getWindowLayout());
                if(!settingsIsOpen)
                    previousCatches.getSettingsStage().close();
            }
        });
        resetCombo.setOnAction(e -> window.resetCombo());
        saveHunt.setOnAction(e -> window.saveHunt());
    }

    public void addHuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, String evo0, String evo1, String layout, int encounters, int combo, int increment){
        if(windows.length == 0){
            MenuItem saveAll = new MenuItem("Save All");
            Settings.getItems().add(saveAll);

            saveAll.setOnAction(e -> saveAll());
        }
        boolean numFound = false;
        for(int i = 0; i < windows.length; i++){
            numFound = false;
            for(huntWindow j : windows){
                if (j.getHuntNumber() == (i + 1)) {
                    numFound = true;
                    break;
                }
            }
            if(!numFound) {
                huntNum = i + 1;
                break;
            }
        }
        if(numFound)
            huntNum = windows.length + 1;

        if(currentLayouts.length > windows.length) {
            layout = currentLayouts[huntNum - 1];
        }
        else {
            String[] temp = new String[currentLayouts.length + 1];
            System.arraycopy(currentLayouts, 0, temp, 0, currentLayouts.length);
            currentLayouts = temp;
            currentLayouts[huntNum - 1] = layout;
        }

        huntWindow newWindow = new huntWindow(selectedPokemon, selectedGame, selectedMethod, evo0, evo1, layout, encounters, combo, increment, huntNum);
        newWindow.getStage().setTitle("Hunt " + newWindow.getHuntNumber());

        SaveData data = new SaveData();
        newWindow.setKeybind(data.getLinefromFile(newWindow.getHuntNumber() - 1, "keyBinds").charAt(0));

        addHuntWindowSettings(newWindow);

        huntWindow[] temp = new huntWindow[windows.length + 1];
        System.arraycopy(windows, 0, temp, 0, windows.length);
        temp[windows.length] = newWindow;
        windows = temp;

        if(!numFound && windows.length != 1) {
            orderWindowsArray();
            refreshHuntWindowSettings();
        }

        if(keyBindingSettingsStage.isShowing()){
            keyBindingSettings();
        }

        //since the search level or total encounters can change between uses, this value needs to be captured after every startup
        if (selectedMethod.getName().compareTo("DexNav") == 0 || selectedMethod.getName().compareTo("Total Encounters") == 0) {
            windows[windows.length - 1].promptPreviousEncounters();
        }

        newWindow.getStage().setOnCloseRequest(e -> {
            e.consume();
            currentLayouts[newWindow.getHuntNumber()-1] = newWindow.getCurrentLayout();
            newWindow.saveandCloseHunt();
            removeWindow(newWindow);
        });
    }

    public void removeWindow(huntWindow window){
        if(window.getHuntNumber() == huntNum - 1)
            huntNum--;

        huntWindow[] temp = new huntWindow[windows.length - 1];
        int index = 0;
        for (huntWindow i : windows) {
            if (i != window) {
                temp[index] = i;
                index++;
            }
        }
        windows = temp;
        refreshHuntWindowSettings();
    }

    public void refreshHuntWindowSettings(){
        if(windows.length == 0)
            Settings.getItems().remove(2, Settings.getItems().size());
        else
            Settings.getItems().remove(3, Settings.getItems().size());
        huntControlsButtonVBox.getChildren().remove(1, huntControlsButtonVBox.getChildren().size());
        for(huntWindow i : windows)
            if(i != null)
                addHuntWindowSettings(i);
    }

    public void closeWindows(){
        keyBindingSettingsStage.close();
        previousCatches.getStage().close();
        previousCatches.getSettingsStage().close();
        for(int i = 0; i < windows.length; i++) {
            if(windows[i] != null) {
                windows[i].saveandCloseHunt();
                windows[i] = null;
            }
        }
    }

    public void saveAll(){
        for(huntWindow i: windows)
            i.saveHunt();
    }

    public void orderWindowsArray(){
        huntWindow temp;
        for(int i = 0; i < windows.length; i++){
            for (int j = i; j > 0; j--) {
                if(windows[j].getHuntNumber() < windows[j-1].getHuntNumber()) {
                    temp = windows[j];
                    windows[j] = windows[j - 1];
                    windows[j - 1] = temp;
                }
            }
        }
    }

    public void keyBindingSettings(){
        keyBindingSettingsStage.setTitle("Key Bindings");

        VBox keyBindingsLayout = new VBox();
        keyBindingsLayout.setAlignment(Pos.CENTER);
        keyBindingsLayout.setSpacing(10);
        keyBindingsLayout.setPadding(new Insets(10,0,50,75));
        for (huntWindow i : windows) {
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
                    for(huntWindow j: windows)
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
}
