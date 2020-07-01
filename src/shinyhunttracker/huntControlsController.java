package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class huntControlsController implements Initializable {
    Stage huntControls = new Stage();
    public Button encountersButton, pokemonCaughtButton, phaseButton, resetEncountersButton;
    public HBox huntControlsButtonHBox;

    Stage huntWindow = new Stage();
    Label oddFractionLabel, encountersLabel, previousEncountersLabel;
    int encounters, previousEncounters= 0;
    int increment = 1;

    Stage CustomizeHuntStage = new Stage();
    ImageView sprite;
    double scale = 1;

    Pokemon selectedPokemon = new Pokemon();
    Game selectedGame = new Game();
    Method selectedMethod = new Method();
    int methodBase;

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
        AnchorPane promptLayout = new AnchorPane();

        try {
            FileInputStream input;
            if(selectedPokemon.getName().compareTo("Type: Null") == 0)
                input = new FileInputStream("Images/Sprites/3d Sprites/type null.gif");
            else
                input = new FileInputStream("Images/Sprites/3d Sprites/" + selectedPokemon.getName().toLowerCase() + ".gif");
            switch(selectedGame.getGeneration()) {
                case 2:
                    input = new FileInputStream("Images/Sprites/Generation 2/" + selectedGame.getName().toLowerCase() + "/" + selectedPokemon.getName().toLowerCase() + ".png");
                    break;
                case 3:
                    switch(selectedGame.getName()){
                        case "Ruby":
                        case "Sapphire":
                            input = new FileInputStream("Images/Sprites/Generation 3/ruby-sapphire/" + selectedPokemon.getName().toLowerCase() + ".png");
                            break;
                        case "Emerald":
                            input = new FileInputStream("Images/Sprites/Generation 3/emerald/" + selectedPokemon.getName().toLowerCase() + ".png");
                            break;
                        case "FireRed":
                        case "LeafGreen":
                            input = new FileInputStream("Images/Sprites/Generation 3/firered-leafgreen/" + selectedPokemon.getName().toLowerCase() + ".png");
                            break;
                        default:
                            break;
                    }
                    break;
                case 4:
                    switch(selectedGame.getName()){
                        case "Diamond":
                        case "Pearl":
                            input = new FileInputStream("Images/Sprites/Generation 4/diamond-pearl/" + selectedPokemon.getName().toLowerCase() + ".png");
                            break;
                        case "Platinum":
                            input = new FileInputStream("Images/Sprites/Generation 4/platinum/" + selectedPokemon.getName().toLowerCase() + ".png");
                            break;
                        case "HeartGold":
                        case "SoulSilver":
                            input = new FileInputStream("Images/Sprites/Generation 4/heartgold-soulsilver/" + selectedPokemon.getName().toLowerCase() + ".png");
                            break;
                        default:
                            break;
                    }
                    break;
                case 5:
                    input = new FileInputStream("Images/Sprites/Generation 5/" + selectedPokemon.getName().toLowerCase() + ".png");
                    break;
                default:
                    break;
            }
            Image image = new Image(input);
            sprite = new ImageView(image);
            promptLayout.getChildren().add(sprite);
        }catch (FileNotFoundException e){
            System.out.println("Sprite not found");
        }

        promptLayout.getChildren().addAll(currentHuntingGameLabel, currentHuntingMethodLabel, currentHuntingPokemonLabel, encountersLabel, previousEncountersLabel, oddFractionLabel);
        currentHuntingGameLabel.setLayoutX(200);
        currentHuntingGameLabel.setLayoutY(50);

        currentHuntingMethodLabel.setLayoutX(200);
        currentHuntingMethodLabel.setLayoutY(65);

        currentHuntingPokemonLabel.setLayoutX(200);
        currentHuntingPokemonLabel.setLayoutY(80);

        encountersLabel.setLayoutX(200);
        encountersLabel.setLayoutY(95);

        previousEncountersLabel.setLayoutX(200);
        previousEncountersLabel.setLayoutY(110);

        oddFractionLabel.setLayoutX(200);
        oddFractionLabel.setLayoutY(125);

        Scene promptScene = new Scene(promptLayout, 750, 480);
        huntWindow.setScene(promptScene);
        huntWindow.show();

        huntWindow.setOnCloseRequest(e -> {
            CustomizeHuntStage.close();
            huntControls.close();
        });
    }

    public void importStage(Stage stage){
        huntControls = stage;
        huntControls.setOnCloseRequest(e -> {
            huntWindow.close();
            CustomizeHuntStage.close();
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
        VBox imageSettings = createImageSettings();
        VBox currentGameSettings = createCurrentGameLabelSettings();
        VBox currentMethodSettings = createCurrentMethodLabelSettings();
        VBox currentPokemonSettings = createCurrentPokemonLabelSettings();
        VBox encountersSettings = createEncountersLabelSettings();
        VBox previousEncountersSettings = createPreviousEncountersLabelSettings();
        VBox oddsFraction = createOddsFractionLabel();

        VBox CustomizeHuntVBox = new VBox();
        CustomizeHuntVBox.getChildren().addAll(imageSettings, currentGameSettings, currentMethodSettings, currentPokemonSettings, encountersSettings, previousEncountersSettings, oddsFraction);

        AnchorPane CustomizeHuntLayout = new AnchorPane();
        CustomizeHuntLayout.getChildren().add(CustomizeHuntVBox);
        AnchorPane.setTopAnchor(CustomizeHuntVBox,0d);

        ScrollPane CustomizeHuntScrollpane = new ScrollPane(CustomizeHuntLayout);
        CustomizeHuntScrollpane.setFitToHeight(true);

        Scene CustomizeHuntScene = new Scene(CustomizeHuntScrollpane, 300, 300);
        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomizeHuntStage.show();
    }

    public VBox createImageSettings(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Image");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
    }

    public VBox createCurrentGameLabelSettings(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Game Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
    }

    public VBox createCurrentMethodLabelSettings(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Method Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
    }

    public VBox createCurrentPokemonLabelSettings(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Pokemon Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
    }

    public VBox createEncountersLabelSettings(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Encounters Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
    }

    public VBox createPreviousEncountersLabelSettings(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Search Level/Previous Encounters Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
    }

    public VBox createOddsFractionLabel(){
        HBox groupLabel = new HBox();
        Label Group = new Label("Odds Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        changeY.getChildren().addAll(YLabel, YField);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY);

        sizeField.setOnAction(e -> {
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setText("");
        });

        return Settings;
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