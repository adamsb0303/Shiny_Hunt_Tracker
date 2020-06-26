package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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
                break;
            default:
                break;
        }
    }
}
