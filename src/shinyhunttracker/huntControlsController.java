package shinyhunttracker;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class huntControlsController implements Initializable {
    //controller elements
    Stage huntControls = new Stage();
    public Button encountersButton, pokemonCaughtButton, phaseButton, resetEncountersButton;
    public HBox huntControlsButtonHBox;
    boolean selectingNewHunt = false;

    //hunt window elements
    Stage huntWindow = new Stage();
    AnchorPane huntLayout = new AnchorPane();
    int huntLayoutSize = 0;
    Text currentHuntingMethodText, currentHuntingPokemonText, currentGameText, encountersText, previousEncountersText, currentComboText, oddFractionText;
    int encounters, previousEncounters, combo = 0;
    int increment = 1;

    //hunt settings window elements
    Stage CustomizeHuntStage = new Stage();
    ImageView sprite;

    //previously caught pokemon settings elements
    Stage previouslyCaughtStage = new Stage();
    Scene previouslyCaughtScene;
    int displayPrevious = 0;

    //current objects
    Pokemon selectedPokemon = new Pokemon();
    Game selectedGame = new Game();
    Method selectedMethod = new Method();
    int methodBase;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //moves HBox with all the buttons to the middle of the window
        huntControlsButtonHBox.setAlignment(Pos.CENTER);
        huntControlsButtonHBox.setSpacing(10);
    }

    //sets huntControls window to the given stage
    public void importStage(Stage stage){
        huntControls = stage;
        huntControls.setOnCloseRequest(e -> {
            huntWindow.close();
            CustomizeHuntStage.close();
            previouslyCaughtStage.close();
            saveHunt();
        });
    }

    //creates hunt window
    public void createHuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, int encounters, int combo, int increment){
        huntWindow.setTitle("Hunt Window");
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        this.encounters = encounters;
        this.combo = combo;
        this.increment = increment;

        currentHuntingPokemonText = new Text(selectedPokemon.getName());
        currentHuntingMethodText= new Text(selectedMethod.getName());
        currentGameText = new Text(selectedGame.getName());
        oddFractionText= new Text("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        dynamicOddsMethods();
        encountersText= new Text(String.valueOf(encounters));
        currentComboText = new Text(String.valueOf(combo));
        currentComboText.setVisible(false);
        previousEncountersText = new Text();
        previousEncountersText.setVisible(false);

        sprite = createPokemonSprite(selectedPokemon.getName(), selectedGame);
        huntLayout.getChildren().add(sprite);

        Button resetEncountersButton = new Button("Reset Combo");
        switch(selectedMethod.getName()){
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                huntControlsButtonHBox.getChildren().add(resetEncountersButton);
                currentComboText.setVisible(true);
                break;
            case "DexNav":
                huntControlsButtonHBox.getChildren().add(resetEncountersButton);
                currentComboText.setVisible(true);
                previousEncountersText.setVisible(true);
                break;
            case "Total Encounters":
                previousEncountersText.setVisible(true);
                break;
            default:
                break;
        }
        huntLayout.getChildren().addAll(currentHuntingPokemonText, currentHuntingMethodText, currentGameText, encountersText, previousEncountersText, currentComboText, oddFractionText);
        huntLayoutSize = huntLayout.getChildren().size();

        int index = 1;
        for(int i = 1; i < huntLayout.getChildren().size(); i++){
            if(huntLayout.getChildren().get(i).isVisible()) {
                huntLayout.getChildren().get(i).setLayoutX(200);
                huntLayout.getChildren().get(i).setLayoutY(65 + (15 * index));
                index++;
            }
        }

        Scene huntScene = new Scene(huntLayout, 750, 480);
        huntWindow.setScene(huntScene);
        huntWindow.show();

        huntWindow.setOnCloseRequest(e -> {
            CustomizeHuntStage.close();
            previouslyCaughtStage.close();
            huntControls.close();
            saveHunt();
        });
    }

    //returns ImageView with the sprite of the given pokemon
    public ImageView createPokemonSprite(String name, Game selectedGame){
        try {
            FileInputStream input;
            if(selectedPokemon.getName().compareTo("Type: Null") == 0)
                input = new FileInputStream("Images/Sprites/3d Sprites/type null.gif");
            else
                input = new FileInputStream("Images/Sprites/3d Sprites/" + name.toLowerCase() + ".gif");
            switch(selectedGame.getGeneration()) {
                case 2:
                    input = new FileInputStream("Images/Sprites/Generation 2/" + name.toLowerCase() + "/" + selectedPokemon.getName().toLowerCase() + ".png");
                    break;
                case 3:
                    switch(selectedGame.getName()){
                        case "Ruby":
                        case "Sapphire":
                            input = new FileInputStream("Images/Sprites/Generation 3/ruby-sapphire/" + name.toLowerCase() + ".png");
                            break;
                        case "Emerald":
                            input = new FileInputStream("Images/Sprites/Generation 3/emerald/" + name.toLowerCase() + ".png");
                            break;
                        case "FireRed":
                        case "LeafGreen":
                            input = new FileInputStream("Images/Sprites/Generation 3/firered-leafgreen/" + name.toLowerCase() + ".png");
                            break;
                        default:
                            break;
                    }
                    break;
                case 4:
                    switch(selectedGame.getName()){
                        case "Diamond":
                        case "Pearl":
                            input = new FileInputStream("Images/Sprites/Generation 4/diamond-pearl/" + name.toLowerCase() + ".png");
                            break;
                        case "Platinum":
                            input = new FileInputStream("Images/Sprites/Generation 4/platinum/" + name.toLowerCase() + ".png");
                            break;
                        case "HeartGold":
                        case "SoulSilver":
                            input = new FileInputStream("Images/Sprites/Generation 4/heartgold-soulsilver/" + name.toLowerCase() + ".png");
                            break;
                        default:
                            break;
                    }
                    break;
                case 5:
                    input = new FileInputStream("Images/Sprites/Generation 5/" + name.toLowerCase() + ".gif");
                    break;
                default:
                    break;
            }
            Image image = new Image(input);
            return new ImageView(image);
        }catch (FileNotFoundException e){
            System.out.println("Sprite not found");
            return null;
        }
    }

    //creates window to prompt user for search level or previous encounters
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
                previousEncountersText.setText(String.valueOf(previousEncounters));
                if(selectedMethod.getName().compareTo("DexNav") == 0)
                    oddFractionText.setText("1/" + selectedMethod.dexNav(encounters, previousEncounters));
                else
                    oddFractionText.setText("1/" + simplifyFraction((selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters)), methodBase));
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

    //creates window for the hunt window settings
    public void CustomizeHuntWindow(){
        CustomizeHuntStage.setTitle("Settings");
        CustomizeHuntStage.setResizable(false);
        VBox imageSettings = createImageSettings(sprite, selectedPokemon.getName());
        VBox currentPokemonSettings = createLabelSettings(currentHuntingPokemonText, "Pokemon");
        VBox currentMethodSettings = createLabelSettings(currentHuntingMethodText, "Method");
        VBox currentGameSettings = createLabelSettings(currentGameText, "Game");
        VBox encountersSettings = createLabelSettings(encountersText, "Encounters");
        VBox oddsFraction = createLabelSettings(oddFractionText, "Odds");

        VBox backgroundVBox = new VBox();
        Label backgroundGroup = new Label("Background");
        backgroundGroup.setUnderline(true);
        HBox backgroundColorSettings = new HBox();
        backgroundColorSettings.setSpacing(5);
        Label backgroundColorLabel = new Label("Color");
        ColorPicker backgroundColorPicker = new ColorPicker();
        backgroundVBox.setPadding(new Insets(10,10,10,10));
        backgroundVBox.setSpacing(10);
        backgroundColorSettings.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
        backgroundVBox.getChildren().addAll(backgroundGroup, backgroundColorSettings);

        Accordion accordion = new Accordion();
        TitledPane backgroundTitledPane = new TitledPane("Background", backgroundVBox);
        accordion.getPanes().add(backgroundTitledPane);

        VBox backgroundSettings = new VBox();
        backgroundSettings.getChildren().add(accordion);

        HBox saveClose = new HBox();
        saveClose.setPadding(new Insets(10,10,10,10));
        saveClose.setSpacing(5);
        Button Save = new Button("Save");
        Button Load = new Button("Load");
        Button Close = new Button("Close");
        saveClose.getChildren().addAll(Save,Load,Close);

        VBox CustomizeHuntVBox = new VBox();
        VBox comboSettings;
        VBox previousEncountersSettings;

        switch(selectedMethod.getName()){
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                comboSettings = createLabelSettings(currentComboText, "Combo");
                CustomizeHuntVBox.getChildren().addAll(imageSettings, encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, backgroundSettings, saveClose);
                break;
            case "DexNav":
                comboSettings = createLabelSettings(currentComboText, "Combo");
                previousEncountersSettings = createLabelSettings(previousEncountersText, "Search Level");
                CustomizeHuntVBox.getChildren().addAll(imageSettings, encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, previousEncountersSettings, backgroundSettings, saveClose);
                break;
            case "Total Encounters":
                previousEncountersSettings = createLabelSettings(previousEncountersText, "Total Encounters");
                CustomizeHuntVBox.getChildren().addAll(imageSettings, encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, previousEncountersSettings, backgroundSettings, saveClose);
                break;
            default:
                CustomizeHuntVBox.getChildren().addAll(imageSettings, encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, backgroundSettings, saveClose);
                break;
        }

        AnchorPane CustomizeHuntLayout = new AnchorPane();
        CustomizeHuntLayout.getChildren().add(CustomizeHuntVBox);
        AnchorPane.setTopAnchor(CustomizeHuntVBox,0d);

        ScrollPane CustomizeHuntScrollpane = new ScrollPane(CustomizeHuntLayout);
        CustomizeHuntScrollpane.setFitToHeight(true);

        Scene CustomizeHuntScene = new Scene(CustomizeHuntScrollpane, 263, 500);
        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomizeHuntStage.show();

        backgroundColorPicker.setOnAction(e -> {
            huntLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
        });

        Save.setOnAction(e -> {
            SaveData data = new SaveData();

            data.saveLayout("test", huntLayout);
        });

        Load.setOnAction(e -> {

        });

        Close.setOnAction(e -> {
            CustomizeHuntStage.close();
        });
    }

    //window that displays settings for previously caught pokemon
    public void previouslyCaughtPokemonSettings(){
        previouslyCaughtStage.setTitle("Previously Caught Pokemon Settings");
        previouslyCaughtStage.setResizable(false);

        Label numberCaught = new Label("Display Previously Caught: ");

        TextField numberCaughtField = new TextField();
        numberCaughtField.setPromptText(String.valueOf(displayPrevious));

        HBox numberPreviouslyCaught = new HBox();
        numberPreviouslyCaught.setAlignment(Pos.CENTER);
        numberPreviouslyCaught.setPadding(new Insets(5,5,5,5));
        numberPreviouslyCaught.getChildren().addAll(numberCaught,numberCaughtField);

        VBox caughtSettings = createPreviouslyCaught(displayPrevious);

        VBox previouslyCaughtSettingsLayout = new VBox();
        previouslyCaughtSettingsLayout.setAlignment(Pos.CENTER);
        previouslyCaughtSettingsLayout.setSpacing(5);
        ScrollPane scrollPane = new ScrollPane(previouslyCaughtSettingsLayout);
        previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, caughtSettings);
        previouslyCaughtScene = new Scene(scrollPane, 317, 500);
        previouslyCaughtStage.setScene(previouslyCaughtScene);
        previouslyCaughtStage.show();

        numberCaughtField.setOnAction(e ->{
            try{
                displayPrevious = parseInt(numberCaughtField.getText());
                VBox caughtPokemonSettings = createPreviouslyCaught(displayPrevious);
                previouslyCaughtSettingsLayout.getChildren().remove(1);
                previouslyCaughtSettingsLayout.getChildren().add(caughtPokemonSettings);
                numberCaughtField.setText("");
                numberCaughtField.setPromptText(String.valueOf(displayPrevious));
            }catch(NumberFormatException f){
                numberCaughtField.setText("");
            }
        });
    }

    //create elements of the last x previously caught pokemon
    public VBox createPreviouslyCaught(int previouslyCaught){
        huntLayout.getChildren().remove(huntLayoutSize, huntLayout.getChildren().size());
        VBox settings = new VBox();
        SaveData data = new SaveData();
        int numberCaught = data.getfileLength("CaughtPokemon");
        if(numberCaught < previouslyCaught)
            previouslyCaught = numberCaught;
        for(int i = numberCaught - 1; i >= (numberCaught - previouslyCaught); i--){
            String line = data.getLinefromFile(i, "CaughtPokemon");
            Text seperator = new Text("-------------------------------------------");
            Game caughtGame = new Game(data.splitString(line, 1), parseInt(data.splitString(line, 2)));
            ImageView sprite = createPokemonSprite(data.splitString(line, 0), caughtGame);
            Text pokemon = new Text(data.splitString(line, 0));
            Text method = new Text(data.splitString(line, 3));
            Text encounters = new Text(data.splitString(line, 5));

            sprite.setLayoutX(50 * (i - (numberCaught - previouslyCaught)));
            sprite.setLayoutY(50);

            pokemon.setLayoutX(50 * (i - (numberCaught - previouslyCaught)));
            pokemon.setLayoutY(75);

            method.setLayoutX(50 * (i - (numberCaught - previouslyCaught)));
            method.setLayoutY(90);

            encounters.setLayoutX(50 * (i - (numberCaught - previouslyCaught)));
            encounters.setLayoutY(105);

            VBox imageSettings = createImageSettings(sprite, data.splitString(line, 0));
            VBox currentPokemonSettings = createLabelSettings(pokemon, "Pokemon");
            VBox currentMethodSettings = createLabelSettings(method, "Method");
            VBox encountersSettings = createLabelSettings(encounters, "Encounters");

            VBox pokemonSettings = new VBox();
            pokemonSettings.getChildren().addAll(seperator, imageSettings, currentPokemonSettings, currentMethodSettings, encountersSettings);

            huntLayout.getChildren().addAll(sprite, pokemon, method, encounters);

            settings.getChildren().add(pokemonSettings);
        }
        return settings;
    }

    //creates ImageView settings VBox
    public VBox createImageSettings(ImageView image, String pokemonName){
        HBox groupLabel = new HBox();
        Label Group = new Label(pokemonName + " Sprite:");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        sizeField.setPromptText(String.valueOf(image.getScaleX()));
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.setPromptText(String.valueOf(image.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.setPromptText(String.valueOf(image.getLayoutY()));
        changeY.getChildren().addAll(YLabel, YField);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(true);
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox imageVBox = new VBox();
        imageVBox.setSpacing(10);
        imageVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, visablility);

        Accordion accordion = new Accordion();
        TitledPane imageTitledPane = new TitledPane(pokemonName + " Sprite", imageVBox);
        accordion.getPanes().add(imageTitledPane);

        VBox imageSettings = new VBox();
        imageSettings.getChildren().add(accordion);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            image.setTranslateX(-(image.getImage().getWidth() * ((1 - scale)/2)));
            image.setTranslateY(-(image.getImage().getHeight() * ((1 - scale)/2)));
            image.setScaleX(scale);
            image.setScaleY(scale);
            sizeField.setText("");
            sizeField.setPromptText(String.valueOf(image.getScaleX()));
        });

        XField.setOnAction(e ->{
            try{
                image.setLayoutX(parseDouble(XField.getText()));
                XField.setText("");
                XField.setPromptText(String.valueOf(image.getLayoutX()));
            }catch(NumberFormatException f){
                XField.setText("");
            }
        });

        YField.setOnAction(e ->{
            try{
                image.setLayoutY(parseDouble(YField.getText()));
                YField.setText("");
                YField.setPromptText(String.valueOf(image.getLayoutY()));
            }catch(NumberFormatException f){
                YField.setText("");
            }
        });

        visableCheck.setOnAction(e ->{
            image.setVisible(visableCheck.isSelected());
        });

        return imageSettings;
    }

    //creates Label settings VBox
    public VBox createLabelSettings(Text label, String labelName){
        HBox groupLabel = new HBox();
        Label Group = new Label(labelName + " Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale: ");
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
        colorField.setValue((Color) label.getFill());
        color.getChildren().addAll(colorLabel, colorField);

        HBox stroke = new HBox();
        stroke.setSpacing(5);
        Label strokeLabel = new Label("Stroke:");
        CheckBox strokeCheckbox = new CheckBox();
        stroke.getChildren().addAll(strokeLabel, strokeCheckbox);

        HBox strokeWidth = new HBox();
        strokeWidth.setSpacing(5);
        Label strokeWidthLabel = new Label("Stroke Width:");
        TextField strokeWidthField = new TextField();
        strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
        strokeWidthLabel.setDisable(true);
        strokeWidthField.setDisable(true);
        strokeWidth.getChildren().addAll(strokeWidthLabel, strokeWidthField);

        HBox strokeColor = new HBox();
        strokeColor.setSpacing(5);
        Label strokeColorLabel = new Label("Stroke Color:");
        ColorPicker strokeColorPicker = new ColorPicker();
        strokeColorPicker.setValue((Color) label.getStroke());
        strokeColorLabel.setDisable(true);
        strokeColorPicker.setDisable(true);
        strokeColor.getChildren().addAll(strokeColorLabel, strokeColorPicker);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(true);
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox labelVBox = new VBox();
        labelVBox.setSpacing(10);
        labelVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, font, color, stroke, strokeWidth, strokeColor, visablility);

        Accordion accordion = new Accordion();
        TitledPane labelTitledPane = new TitledPane(labelName, labelVBox);
        accordion.getPanes().add(labelTitledPane);

        VBox labelSettings = new VBox();
        labelSettings.getChildren().add(accordion);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
                sizeField.setText("");
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
                X = parseDouble(XField.getText());
                XField.setText("");
            }catch(NumberFormatException f){
                XField.setText("");
            }
            label.setLayoutX(X);
            XField.setPromptText(String.valueOf(label.getLayoutX()));
        });

        YField.setOnAction(e ->{
            double Y = 0;
            try{
                Y = parseDouble(YField.getText());
                YField.setText("");
            }catch(NumberFormatException f){
                YField.setText("");
            }
            label.setLayoutY(Y);
            YField.setPromptText(String.valueOf(label.getLayoutY()));
        });

        fontField.setOnAction(e -> {
            String fontName = fontField.getText();
            label.setFont(new Font(fontName, 12));
            fontField.setPromptText(String.valueOf(label.getFont()).substring(10, String.valueOf(label.getFont()).indexOf(',')));
            fontField.setText("");
        });

        colorField.setOnAction(e -> {
            label.setFill(colorField.getValue());
        });

        strokeCheckbox.setOnAction(e -> {
            boolean selected = strokeCheckbox.isSelected();
            if(selected) {
                strokeWidthLabel.setDisable(false);
                strokeWidthField.setDisable(false);
                strokeColorLabel.setDisable(false);
                strokeColorPicker.setDisable(false);
                label.setStrokeWidth(parseDouble(strokeWidthField.getPromptText()));
            }else {
                label.setStrokeWidth(0);
            }
        });

        strokeWidthField.setOnAction(e -> {
            try{
                double width = parseDouble(strokeWidthField.getText());
                label.setStrokeWidth(width);
                strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
                strokeWidthField.setText("");
            }catch(NumberFormatException f){
                strokeWidthField.setText("");
            }
        });

        strokeColorPicker.setOnAction(e -> {
            label.setStroke(strokeColorPicker.getValue());
        });

        visableCheck.setOnAction(e ->{
            label.setVisible(visableCheck.isSelected());
        });

        return labelSettings;
    }

    //adds increment to the encounters
    public void incrementEncounters(){
        encounters += increment;
        combo += increment;
        encountersText.setText(String.valueOf(encounters));
        dynamicOddsMethods();
    }

    //adds current pokemon to the caught pokemon file
    public void pokemonCaught() {
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment);
        data.pokemonCaught();
        sprite.setVisible(false);
        currentHuntingPokemonText.setVisible(false);
        currentHuntingMethodText.setVisible(false);
        oddFractionText.setVisible(false);
        encountersText.setVisible(false);
        previousEncountersText.setVisible(false);

        createPreviouslyCaught(displayPrevious);
/*
        try {
            Stage huntSelectionWindow = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("selectionPage.fxml"));
            huntSelectionWindow.setTitle("Shiny Hunt Tracker");
            huntSelectionWindow.setResizable(false);
            huntSelectionWindow.setScene(new Scene(root, 750, 480));
            selectingNewHunt = true;
            huntSelectionWindow.show();
        }catch(IOException e){
            e.printStackTrace();
        }
 */
    }

    //prompts user for phase pokemon, resets encounters, and adds phased pokemon to the caught pokemon file
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
                SaveData data = new SaveData(new Pokemon(userInput, 0), selectedGame, selectedMethod, encounters, combo, increment);
                data.pokemonCaught();
                resetCombo();
                phaseStage.close();
            }
        });
    }

    //resets encounters
    public void resetCombo(){
        combo = 0;
        dynamicOddsMethods();
    }

    //writes objects to previous hunts file
    public void saveHunt(){
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment);
        data.saveHunt();
    }

    //changes increment
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

    //since some methods' odds change based on encounters
    private void dynamicOddsMethods(){
        switch(selectedMethod.getName()){
            case "Radar Chaining":
                int tempEncounters;
                if (combo >= 40)
                    tempEncounters = 39;
                else
                    tempEncounters = combo;
                oddFractionText.setText("1/" + simplifyFraction(Math.round(((65535 / (8200.0 - tempEncounters * 200)) + selectedMethod.getModifier() - 1)), (65536 / (1 + (Math.abs(methodBase - 8196) / 4096)))));
                break;
            case "Chain Fishing":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.chainFishing(combo), methodBase));
                break;
            case "DexNav":
                if (previousEncounters < 999) {
                    previousEncounters++;
                    previousEncountersText.setText(String.valueOf(previousEncounters));
                }else
                    previousEncountersText.setText("999");
                oddFractionText.setText("1/" + selectedMethod.dexNav(combo, previousEncounters));
                break;
            case "SOS Chaining":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(combo), methodBase));
                break;
            case "Catch Combo":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(combo), methodBase));
                break;
            case "Total Encounters":
                previousEncounters++;
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters), methodBase));
                break;
            default:
                break;
        }
    }

    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }
}