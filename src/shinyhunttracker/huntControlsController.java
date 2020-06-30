package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class huntControlsController implements Initializable {
    public Button encountersButton, pokemonCaughtButton, phaseButton, resetEncountersButton;
    public HBox huntControlsButtonHBox;
    Stage huntControls, huntWindow = new Stage();

    Label oddFractionLabel, encountersLabel, previousEncountersLabel;

    Pokemon selectedPokemon = new Pokemon();
    Game selectedGame = new Game();
    Method selectedMethod = new Method();

    int methodBase;
    int encounters, previousEncounters= 0;
    int increment = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        huntControlsButtonHBox.setAlignment(Pos.CENTER);
        huntControlsButtonHBox.setSpacing(10);
    }

    public void createHuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, int encounters){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        this.encounters = encounters;

        Label currentHuntingPokemonLabel = new Label(selectedPokemon.getName());
        Label currentHuntingGameLabel = new Label(selectedGame.getName());
        Label currentHuntingMethodLabel= new Label(selectedMethod.getName());
        oddFractionLabel= new Label("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        encountersLabel= new Label(String.valueOf(encounters));
        previousEncountersLabel = new Label();
        previousEncountersLabel.setVisible(selectedMethod.getName().compareTo("DexNav") == 0);
        VBox promptLayout = new VBox();
        promptLayout.setAlignment(Pos.CENTER);
        try {
            FileInputStream input;
            if(selectedGame.getGeneration() >= 6)
                input = new FileInputStream("Images/Sprites/3d Sprites/" + selectedPokemon.getName().toLowerCase() + ".gif");
            else
                input = new FileInputStream("Images/Sprites/Generation "+ selectedGame.getGeneration() + "/" + selectedPokemon.getName().toLowerCase() + ".png");
            Image image = new Image(input);
            ImageView sprite = new ImageView(image);
            promptLayout.getChildren().add(sprite);
        }catch (FileNotFoundException e){
            System.out.println("Sprite not found");
        }

        promptLayout.getChildren().addAll(currentHuntingGameLabel, currentHuntingMethodLabel, currentHuntingPokemonLabel, encountersLabel, previousEncountersLabel, oddFractionLabel);

        Scene promptScene = new Scene(promptLayout, 750, 480);
        huntWindow.setScene(promptScene);
        huntWindow.show();

        huntWindow.setOnCloseRequest(e -> {
            huntControls.close();
        });
    }

    public void importStage(Stage stage){
        huntControls = stage;
        huntControls.setOnCloseRequest(e -> {
            huntWindow.close();
        });
    }

    public void promptPreviousEncounters(){
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

        previousInput.setOnAction(e-> {
            try{
                previousEncounters = parseInt(previousInput.getText());
                previousEncountersLabel.setText(String.valueOf(previousEncounters));
                if(selectedMethod.getName().compareTo("DexNav") == 0)
                    oddFractionLabel.setText("1/" + selectedMethod.dexNav(encounters, previousEncounters));
                else
                    oddFractionLabel.setText("1/" + simplifyFraction((selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters)), methodBase));
                promptWindow.close();
            }catch (NumberFormatException f){
                previousInput.setText("");
            }
        });

        VBox promptLayout = new VBox();
        promptLayout.setSpacing(10);
        promptLayout.setAlignment(Pos.CENTER);
        promptLayout.getChildren().addAll(promptLabel, previousInput);

        Scene promptScene = new Scene(promptLayout, 275, 75);
        promptWindow.setScene(promptScene);
        promptWindow.show();

        promptWindow.setOnCloseRequest(e -> {
            huntControls.close();
            huntWindow.close();
        });
    }

    public void CustomizeHuntWindow(){
        Stage CustomizeHuntStage = new Stage();

        VBox CustomizeHuntLayout = new VBox();
        CustomizeHuntLayout.setSpacing(10);
        CustomizeHuntLayout.setAlignment(Pos.CENTER);
        CustomizeHuntLayout.getChildren().addAll();

        Scene CustomizeHuntScene = new Scene(CustomizeHuntLayout, 300, 300);
        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomizeHuntStage.show();
    }

    public void incrementEncounters(){
        encounters += increment;
        encountersLabel.setText(String.valueOf(encounters));
        dynamicOddsMethods();
    }

    public void pokemonCaught() {
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters);
        data.pokemonCaught();
        huntControls.close();
        huntWindow.close();
    }

    public void phaseHunt(){
        Stage phaseStage = new Stage();
        phaseStage.initModality(Modality.APPLICATION_MODAL);
        phaseStage.setResizable(false);
        phaseStage.setTitle("Phase");

        VBox phaseLayout = new VBox();
        phaseLayout.setAlignment(Pos.CENTER);
        phaseLayout.setSpacing(10);

        Label phaseLabel = new Label("Please enter the phase pokemon");
        TextField phasePokemon = new TextField();
        phaseLayout.getChildren().addAll(phaseLabel, phasePokemon);

        Scene phaseScene = new Scene(phaseLayout, 200,100);
        phaseStage.setScene(phaseScene);
        phaseStage.show();

        phasePokemon.setOnAction(e -> {
            selectionPageController temp = new selectionPageController();
            String userInput = phasePokemon.getText().toLowerCase();
            userInput = userInput.substring(0,1).toUpperCase() + userInput.substring(1);
            if(temp.findGenerationPokemon(userInput) == 0)
                phasePokemon.setText("");
            else{
                SaveData data = new SaveData(new Pokemon(userInput, 0), selectedGame, selectedMethod, encounters);
                data.pokemonCaught();
                resetEncounters();
                phaseStage.close();
            }
        });
    }

    public void resetEncounters(){
        encounters = 0;
        encountersLabel.setText(String.valueOf(encounters));
    }

    public void saveHunt(){
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters);
        data.saveHunt();
    }

    public void changeIncrement(){
        Stage changeIncrementStage = new Stage();
        changeIncrementStage.setResizable(false);
        changeIncrementStage.initModality(Modality.APPLICATION_MODAL);
        VBox changeIncrementsLayout = new VBox();
        changeIncrementsLayout.setAlignment(Pos.CENTER);
        changeIncrementsLayout.setSpacing(10);
        Label changeIncrementsLabel = new Label("Enter the number by which the encounters increase for every button press");
        TextField changeIncrementsText = new TextField();
        changeIncrementsLayout.getChildren().addAll(changeIncrementsLabel, changeIncrementsText);
        Scene changeIncrementsScene = new Scene(changeIncrementsLayout, 400, 100);
        changeIncrementStage.setScene(changeIncrementsScene);
        changeIncrementStage.show();

        changeIncrementsText.setOnAction(e -> {
            try{
                increment = parseInt(changeIncrementsText.getText());
                changeIncrementStage.close();
            }catch (NumberFormatException f){
                changeIncrementsText.setText("");
            }
        });
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
                if (previousEncounters < 999) {
                    previousEncounters++;
                    previousEncountersLabel.setText(String.valueOf(previousEncounters));
                }else
                    previousEncountersLabel.setText("999");
                oddFractionLabel.setText("1/" + selectedMethod.dexNav(encounters, previousEncounters));
                break;
            case "SOS Chaining":
                oddFractionLabel.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(encounters), methodBase));
                break;
            case "Catch Combo":
                oddFractionLabel.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(encounters), methodBase));
                break;
            case "Total Encounters":
                previousEncounters++;
                oddFractionLabel.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters), methodBase));
                break;
            default:
                break;
        }
    }

    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }
}