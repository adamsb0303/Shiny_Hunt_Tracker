package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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
    AnchorPane promptLayout = new AnchorPane();
    Label currentHuntingMethodLabel, currentHuntingPokemonLabel, oddFractionLabel, encountersLabel, previousEncountersLabel;
    int encounters, previousEncounters= 0;
    int increment = 1;

    Stage CustomizeHuntStage = new Stage();
    ImageView sprite;

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
        huntWindow.setTitle("Hunt Window");
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        this.encounters = encounters;

        currentHuntingPokemonLabel = new Label(selectedPokemon.getName());
        currentHuntingMethodLabel= new Label(selectedMethod.getName());
        oddFractionLabel= new Label("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        encountersLabel= new Label(String.valueOf(encounters));
        previousEncountersLabel = new Label();
        previousEncountersLabel.setVisible(selectedMethod.getName().compareTo("DexNav") == 0);

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

        promptLayout.getChildren().addAll(currentHuntingPokemonLabel, currentHuntingMethodLabel, encountersLabel, previousEncountersLabel, oddFractionLabel);

        currentHuntingPokemonLabel.setLayoutX(200);
        currentHuntingPokemonLabel.setLayoutY(65);

        encountersLabel.setLayoutX(200);
        encountersLabel.setLayoutY(80);

        currentHuntingMethodLabel.setLayoutX(200);
        currentHuntingMethodLabel.setLayoutY(95);

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
        CustomizeHuntStage.setTitle("Settings");
        VBox imageSettings = createImageSettings();
        VBox currentMethodSettings = createLabelSettings(currentHuntingMethodLabel, "Method");
        VBox currentPokemonSettings = createLabelSettings(currentHuntingPokemonLabel, "Pokemon");
        VBox encountersSettings = createLabelSettings(encountersLabel, "Encounters");
        VBox previousEncountersSettings = createLabelSettings(previousEncountersLabel, "Search Level/Total Encounters");
        VBox oddsFraction = createLabelSettings(oddFractionLabel, "Odds");

        VBox background = new VBox();
        Label backgroundGroup = new Label("Background");
        backgroundGroup.setUnderline(true);
        HBox backgroundColorSettings = new HBox();
        backgroundColorSettings.setSpacing(5);
        Label backgroundColorLabel = new Label("Color");
        ColorPicker backgroundColorPicker = new ColorPicker();
        background.setPadding(new Insets(10,10,10,10));
        background.setSpacing(10);
        backgroundColorSettings.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
        background.getChildren().addAll(backgroundGroup, backgroundColorSettings);

        HBox saveClose = new HBox();
        saveClose.setPadding(new Insets(10,10,10,10));
        saveClose.setSpacing(5);
        Button Save = new Button("Save");
        Button Load = new Button("Load");
        Button Close = new Button("Close");
        saveClose.getChildren().addAll(Save,Load,Close);

        VBox CustomizeHuntVBox = new VBox();
        CustomizeHuntVBox.getChildren().addAll(imageSettings, encountersSettings, currentPokemonSettings, oddsFraction, currentMethodSettings, previousEncountersSettings, background, saveClose);

        AnchorPane CustomizeHuntLayout = new AnchorPane();
        CustomizeHuntLayout.getChildren().add(CustomizeHuntVBox);
        AnchorPane.setTopAnchor(CustomizeHuntVBox,0d);

        ScrollPane CustomizeHuntScrollpane = new ScrollPane(CustomizeHuntLayout);
        CustomizeHuntScrollpane.setFitToHeight(true);

        Scene CustomizeHuntScene = new Scene(CustomizeHuntScrollpane, 300, 300);
        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomizeHuntStage.show();

        backgroundColorPicker.setOnAction(e -> {
            promptLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        Save.setOnAction(e -> {

        });

        Load.setOnAction(e -> {

        });

        Close.setOnAction(e -> {
            CustomizeHuntStage.close();
        });
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
        sizeField.setPromptText(String.valueOf(sprite.getScaleX()));
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.setPromptText(String.valueOf(sprite.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.setPromptText(String.valueOf(sprite.getLayoutY()));
        changeY.getChildren().addAll(YLabel, YField);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(true);
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY, visablility);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
            sizeField.setPromptText(String.valueOf(sprite.getScaleX()));
        });

        XField.setOnAction(e ->{
            double X = 0;
            try{
                X = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setLayoutX(X);
            sizeField.setPromptText(String.valueOf(sprite.getScaleX()));
        });

        YField.setOnAction(e ->{
            double Y = 0;
            try{
                Y = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            sprite.setLayoutY(Y);
            sizeField.setPromptText(String.valueOf(sprite.getScaleX()));
        });

        visableCheck.setOnAction(e ->{
            sprite.setVisible(visableCheck.isSelected());
        });

        return Settings;
    }

    public VBox createLabelSettings(Label label, String labelName){
        HBox groupLabel = new HBox();
        Label Group = new Label(labelName + " Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        sizeField.setPromptText(String.valueOf(label.getScaleX()));
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.setPromptText(String.valueOf(label.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.setPromptText(String.valueOf(label.getLayoutY()));
        changeY.getChildren().addAll(YLabel, YField);

        HBox font = new HBox();
        font.setSpacing(5);
        Label fontLabel = new Label("Font:");
        TextField fontField = new TextField();
        fontField.setPromptText(String.valueOf(label.getFont()).substring(10, String.valueOf(label.getFont()).indexOf(',')));
        font.getChildren().addAll(fontLabel, fontField);

        HBox color = new HBox();
        color.setSpacing(5);
        Label colorLabel = new Label("Color:");
        ColorPicker colorField = new ColorPicker();
        color.getChildren().addAll(colorLabel, colorField);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(true);
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox Settings = new VBox();
        Settings.setSpacing(10);
        Settings.setAlignment(Pos.CENTER);
        Settings.setPadding(new Insets(10,10,10,10));
        Settings.getChildren().addAll(groupLabel, changeSize, changeX, changeY, font, color, visablility);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            label.setScaleX(scale);
            label.setScaleY(scale);
            sizeField.setPromptText(String.valueOf(label.getScaleX()));
        });

        XField.setOnAction(e ->{
            double X = 0;
            try{
                X = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            label.setLayoutX(X);
            sizeField.setPromptText(String.valueOf(label.getScaleX()));
        });

        YField.setOnAction(e ->{
            double Y = 0;
            try{
                Y = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            label.setLayoutY(Y);
            sizeField.setPromptText(String.valueOf(label.getScaleX()));
        });

        fontField.setOnAction(e -> {
            String fontName = fontField.getText();
            label.setFont(new Font(fontName, 12));
            fontField.setPromptText(String.valueOf(label.getFont()).substring(10, String.valueOf(label.getFont()).indexOf(',')));
            fontField.setText("");
        });

        colorField.setOnAction(e -> {
            label.setTextFill(colorField.getValue());
        });

        visableCheck.setOnAction(e ->{
            label.setVisible(visableCheck.isSelected());
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