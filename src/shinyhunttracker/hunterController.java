package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class hunterController implements Initializable{
    public Pane hunterScene;
    public Label oddFractionLabel, encountersLabel, currentHuntingGameLabel, currentHuntingPokemonLabel, currentHuntingMethodLabel;
    public ImageView currentPokemonImage;

    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    int methodBase;

    int encounters = 0;
    int previousEncounters = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void importData(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod){
        this.selectedPokemon = selectedPokemon;
        currentHuntingPokemonLabel.setText(selectedPokemon.getName());

        this.selectedGame = selectedGame;
        currentHuntingGameLabel.setText(selectedGame.getName());

        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        currentHuntingMethodLabel.setText(selectedMethod.getName());

        oddFractionLabel.setText("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        encountersLabel.setText(String.valueOf(encounters));

        if(selectedMethod.getName().compareTo("DexNav") == 0 || selectedMethod.getName().compareTo("Total Encounters") == 0) {
            promptPreviousEncounters();
        }
    }

    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }

    public void incrementEncounters(){
        encounters++;
        encountersLabel.setText(String.valueOf(encounters));
        dynamicOddsMethods();
    }

    public void resetEncounters(){
        encounters = 0;
        encountersLabel.setText("0");
    }

    private void dynamicOddsMethods(){
        switch(selectedMethod.getName()){
            case "Radar Chaining":
                int tempEncounters;
                if (encounters >= 40)
                    tempEncounters = 39;
                else
                    tempEncounters = encounters;
                oddFractionLabel.setText("1/" + simplifyFraction(Math.round(((65535 / (8200.0 - tempEncounters * 200)) + selectedMethod.getModifier() - 1)), (65536 / (1 + (Math.abs(methodBase - 8196) / 4096)))));
                break;
            case "Chain Fishing":
                oddFractionLabel.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.chainFishing(encounters), methodBase));
                break;
            case "DexNav":
                break;
            case "SOS Chaining":
                oddFractionLabel.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(encounters), methodBase));
                break;
            case "Catch Combo":
                oddFractionLabel.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(encounters), methodBase));
                break;
            case "Total Encounters":
                promptPreviousEncounters();
                break;
            default:
                break;
        }
    }

    private void promptPreviousEncounters(){
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

        Button enterPreviousEncounters = new Button("Enter");
        enterPreviousEncounters.setOnAction(e-> System.out.println(previousInput.getText()));

        VBox promptLayout = new VBox();
        promptLayout.setAlignment(Pos.CENTER);
        promptLayout.getChildren().addAll(promptLabel, previousInput, enterPreviousEncounters);

        Scene promptScene = new Scene(promptLayout, 300, 200);
        promptWindow.setScene(promptScene);
        promptWindow.show();
    }
}
