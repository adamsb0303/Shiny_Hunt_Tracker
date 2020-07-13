package shinyhunttracker;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class huntController {
    huntWindow[] windows;

    public huntController(){
        Stage huntControls = new Stage();
        huntControls.setTitle("Hunt Controller");

        HBox huntControlsButtonHBox = new HBox();
        huntControlsButtonHBox.setAlignment(Pos.CENTER);
        huntControlsButtonHBox.setSpacing(10);

        Button encountersButton = new Button("Increment");
        huntControlsButtonHBox.getChildren().add(encountersButton);

        MenuBar Menu = new MenuBar();
        Menu Settings = new Menu("Settings");

        MenuItem customizeHuntLayout= new MenuItem("Layout Settings");

        Menu huntSettings = new Menu("Hunt Settings");
        MenuItem increment= new MenuItem("Change Increment");
        MenuItem resetEncounters = new MenuItem("Fail");
        MenuItem phaseHunt = new MenuItem("Phase Hunt");
        MenuItem pokemonCaught = new MenuItem("Pokemon Caught");
        MenuItem resetCombo = new MenuItem("Reset Combo");
        MenuItem saveHunt = new MenuItem("Save Hunt");
        huntSettings.getItems().addAll(increment, resetEncounters, phaseHunt, pokemonCaught, resetCombo, saveHunt);

        Settings.getItems().addAll(customizeHuntLayout, huntSettings);
        Menu.getMenus().add(Settings);

        BorderPane huntControlsLayout = new BorderPane();
        huntControlsLayout.setTop(Menu);
        huntControlsLayout.setCenter(huntControlsButtonHBox);

        Scene huntControlsScene = new Scene(huntControlsLayout, 350, 100);
        huntControls.setScene(huntControlsScene);
        huntControls.show();

        encountersButton.setOnAction(e -> windows[0].incrementEncounters());
        increment.setOnAction(e -> windows[0].changeIncrement());
        resetEncounters.setOnAction(e -> windows[0].resetEncounters());
        phaseHunt.setOnAction(e -> windows[0].phaseHunt());
        pokemonCaught.setOnAction(e -> windows[0].pokemonCaught());
        resetCombo.setOnAction(e -> windows[0].resetCombo());
        saveHunt.setOnAction(e -> windows[0].saveHunt());
    }

    public void addHuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, String evo0, String evo1, int encounters, int combo, int increment){
        windows = new huntWindow[]{new huntWindow(selectedPokemon, selectedGame, selectedMethod, evo0, evo1, encounters, combo, increment)};
    }
}
