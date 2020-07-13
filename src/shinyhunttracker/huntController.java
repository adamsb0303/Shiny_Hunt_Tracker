package shinyhunttracker;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class huntController {
    MenuBar Menu = new MenuBar();
    Menu Settings = new Menu("Settings");
    VBox huntControlsButtonVBox;
    huntWindow previousCatches = new huntWindow(new Pokemon("Bulbasaur",0), new Game("Gold",0), new Method("None",0),null,null,null,0,0, 0);

    huntWindow[] windows = new huntWindow[0];

    public huntController(){
        Stage huntControls = new Stage();
        huntControls.setTitle("Hunt Controller");

        huntControlsButtonVBox = new VBox();
        huntControlsButtonVBox.setAlignment(Pos.CENTER);
        huntControlsButtonVBox.setSpacing(5);
        Button addHunt = new Button("Add Hunt");
        huntControlsButtonVBox.getChildren().add(addHunt);

        Menu.getMenus().add(Settings);

        MenuItem previouslyCaught = new MenuItem("Previously Caught Settings");
        Settings.getItems().add(previouslyCaught);

        BorderPane huntControlsLayout = new BorderPane();
        huntControlsLayout.setTop(Menu);
        huntControlsLayout.setCenter(huntControlsButtonVBox);

        Scene huntControlsScene = new Scene(huntControlsLayout, 350, 100);
        huntControls.setScene(huntControlsScene);
        huntControls.show();

        previouslyCaught.setOnAction(e -> previousCatches.previouslyCaughtPokemonSettings());

        addHunt.setOnAction(e -> {
            try {
                FXMLLoader selectionPageLoader = new FXMLLoader();
                selectionPageLoader.setLocation(getClass().getResource("selectionPage.fxml"));
                Parent root = selectionPageLoader.load();

                Stage huntSelectionWindow = new Stage();
                huntSelectionWindow.setTitle("Shiny Hunt Tracker");
                huntSelectionWindow.setResizable(false);
                huntSelectionWindow.setScene(new Scene(root, 750, 480));

                selectionPageController selectionPageController = selectionPageLoader.getController();
                selectionPageController.setSelectingNewHunt(true);
                selectionPageController.setController(this);
                huntSelectionWindow.show();
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

        Button encountersButton = new Button("Increment " + window.getSelectedPokemon().getName());
        huntControlsButtonHBox.getChildren().addAll(encountersButton);
        huntControlsButtonVBox.getChildren().add(huntControlsButtonHBox);

        Menu newHuntSettings = new Menu(window.getSelectedPokemon().getName() + " Hunt Settings");

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
        pokemonCaught.setOnAction(e -> window.pokemonCaught());
        resetCombo.setOnAction(e -> window.resetCombo());
        saveHunt.setOnAction(e -> window.saveHunt());
    }

    public void refreshHuntWindowSettings(){
        Settings.getItems().remove(1, Settings.getItems().size());
        huntControlsButtonVBox.getChildren().remove(1, huntControlsButtonVBox.getChildren().size());
        for(huntWindow i : windows)
            if(i != null)
                addHuntWindowSettings(i);
    }

    public void addHuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, String evo0, String evo1, String layout, int encounters, int combo, int increment){
        huntWindow newWindow = new huntWindow(selectedPokemon, selectedGame, selectedMethod, evo0, evo1, layout, encounters, combo, increment);
        addHuntWindowSettings(newWindow);

        huntWindow[] temp = new huntWindow[windows.length + 1];
        System.arraycopy(windows, 0, temp, 0, windows.length);
        temp[windows.length] = newWindow;
        windows = temp;

        //since the search level or total encounters can change between uses, this value needs to be captured after every startup
        if (selectedMethod.getName().compareTo("DexNav") == 0 || selectedMethod.getName().compareTo("Total Encounters") == 0) {
            windows[windows.length - 1].promptPreviousEncounters();
        }

        newWindow.getStage().setOnCloseRequest(e -> {
            e.consume();
            newWindow.saveHunt();
            removeWindow(newWindow);
        });
    }

    public void removeWindow(huntWindow window){
        for(int i = 0; i < windows.length; i++){
            if(windows[i] == window)
                windows[i] = null;
        }
        refreshHuntWindowSettings();
    }

    public void closeWindows(){
        for(int i = 0; i < windows.length; i++) {
            if(windows[i] != null) {
                windows[i].saveHunt();
                windows[i] = null;
            }
        }
    }
}
