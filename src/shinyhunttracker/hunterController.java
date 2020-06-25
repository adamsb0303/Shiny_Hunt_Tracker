package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class hunterController implements Initializable{
    public Pane hunterScene;
    public Label oddFractionLabel, encountersLabel, currentHuntingGameLabel, currentHuntingPokemonLabel, currentHuntingMethodLabel;
    public Button increment;
    public ImageView currentPokemonImage;

    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;

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
        currentHuntingMethodLabel.setText(selectedMethod.getName());
        oddFractionLabel.setText("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        encountersLabel.setText(String.valueOf(encounters));
    }

    private int simplifyFraction(double num, int den){
        return (int)(den / num);
    }

    public void incrementEncounters(){
        encounters++;
        encountersLabel.setText(String.valueOf(encounters));
    }
}
