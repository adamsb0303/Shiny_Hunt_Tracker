package shinyhunttracker;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class HuntSelection extends Window {
    static Pokemon selectedPokemon = null;
    static TreeView<Pokemon> pokemonListTreeView = new TreeView<>();
    static ObservableList<TreeItem<Pokemon>> defaultPokemonList = FXCollections.observableArrayList();
    static TreeItem<Pokemon> pokemonListRoot = new TreeItem<>();

    static Game selectedGame = null;
    static ObservableList<Game> defaultGameList = FXCollections.observableArrayList();
    static ComboBox<Game> gameComboBox = new ComboBox<>();

    static Method selectedMethod = null;
    static ObservableList<Method> defaultMethodList = FXCollections.observableArrayList();
    static ComboBox<Method> methodComboBox = new ComboBox<>();

    public static void createHuntSelection(HuntController controller){
        Stage selectionPageStage = new Stage();
        HBox selectionPageLayout = new HBox();

        AnchorPane huntInformation = new AnchorPane();
        huntInformation.setMinWidth(375);

        ImageView pokemonSprite = new ImageView();
        pokemonSprite.setLayoutX(180);
        pokemonSprite.setLayoutY(275);

        gameComboBox.setPromptText("---Game---");
        gameComboBox.setMinWidth(200);
        gameComboBox.setLayoutY(280);
        gameComboBox.setLayoutX(87.5);

        try(FileReader reader = new FileReader("GameData/game.json")){
            JSONParser jsonParser = new JSONParser();
            JSONArray gameList = (JSONArray) jsonParser.parse(reader);

            for(Object i : gameList)
                defaultGameList.add(new Game((JSONObject) i));
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
        gameComboBox.getItems().setAll(defaultGameList);

        methodComboBox.setPromptText("---Method---");
        methodComboBox.setMinWidth(200);
        methodComboBox.setLayoutY(305);
        methodComboBox.setLayoutX(87.5);

        try(FileReader reader = new FileReader("GameData/method.json")){
            JSONParser jsonParser = new JSONParser();
            JSONArray methodList = (JSONArray) jsonParser.parse(reader);

            for(Object i : methodList)
                defaultMethodList.add(new Method((JSONObject) i));
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
        methodComboBox.getItems().setAll(defaultMethodList);

        huntInformation.getChildren().addAll(pokemonSprite, gameComboBox, methodComboBox);

        VBox pokemonSelection = new VBox();
        pokemonSelection.setAlignment(Pos.CENTER_RIGHT);
        pokemonSelection.setMinWidth(375);

        TextField pokemonSearch = new TextField();

        pokemonListTreeView.setMinHeight(450);
        pokemonListTreeView.setRoot(pokemonListRoot);
        pokemonListTreeView.setShowRoot(false);

        Button beginHunt = new Button("Start >");
        beginHunt.setDisable(true);

        try(FileReader reader = new FileReader("GameData/pokemon.json")){
            JSONParser jsonParser = new JSONParser();
            JSONArray pokemonListJSON = (JSONArray) jsonParser.parse(reader);

            for (int i = 0; i < pokemonListJSON.size(); i++)
                defaultPokemonList.add(new TreeItem<>(new Pokemon((JSONObject) pokemonListJSON.get(i), i)));
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
        pokemonListRoot.getChildren().setAll(defaultPokemonList);

        pokemonSelection.getChildren().addAll(pokemonSearch, pokemonListTreeView, beginHunt);

        selectionPageLayout.getChildren().addAll(huntInformation, pokemonSelection);
        Scene selectionScene = new Scene(selectionPageLayout, 750, 500);
        selectionPageStage.setScene(selectionScene);
        selectionPageStage.setResizable(false);
        selectionPageStage.show();

        pokemonListTreeView.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue.getValue() != selectedPokemon){
                        selectedPokemon = newValue.getValue();
                        setPokemonSprite(pokemonSprite, newValue.getValue().toString(), new Game(32));
                        updateGameList();
                        updateMethodList();
                    }
                });

        gameComboBox.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue != selectedGame) {
                        selectedGame = newValue;
                        updatePokemonList();
                        updateMethodList();
                    }
                });

        methodComboBox.setOnAction(e -> {
            selectedMethod = methodComboBox.getSelectionModel().getSelectedItem();
            updatePokemonList();
            updateGameList();
        });

        beginHunt.setOnAction(e ->{
            SaveData.saveHunt(new HuntWindow(selectedPokemon, selectedGame, selectedMethod, "", 0, 0, 1, -1));
            SaveData.loadHunt(-1, controller);
        });
    }

    private static void updatePokemonList(){
        pokemonListRoot.getChildren().clear();
        ObservableList<TreeItem<Pokemon>> updatedPokemonList = FXCollections.observableArrayList();

        if(selectedGame != null){
            for(int i : selectedGame.getPokedex())
                if(!defaultPokemonList.get(i).getValue().getBreedable()) {
                    if(selectedGame.hasUnbreedable(i))
                        updatedPokemonList.add(defaultPokemonList.get(i));
                }
                else
                    updatedPokemonList.add(defaultPokemonList.get(i));

            if(updatedPokemonList.size() == 0)
                for(TreeItem<Pokemon> i : defaultPokemonList)
                    if(i.getValue().getGeneration() <= selectedGame.getGeneration())
                        if(!i.getValue().getBreedable()) {
                            if(selectedGame.hasUnbreedable(i.getValue().getDexNumber()))
                                updatedPokemonList.add(i);
                        }
                        else
                            updatedPokemonList.add(i);
            pokemonListRoot.getChildren().addAll(updatedPokemonList);
            if(selectedPokemon != null)
                pokemonListTreeView.getSelectionModel().select(defaultPokemonList.get(selectedPokemon.getDexNumber()));
            return;
        }

        if(selectedMethod != null){

            return;
        }
    }

    private static void updateGameList(){
        gameComboBox.getItems().clear();
        ObservableList<Game> updatedGameList = FXCollections.observableArrayList();

        if(selectedPokemon != null){
            for(Game i : defaultGameList) {
                if (!selectedPokemon.getBreedable()) {
                    if(i.hasUnbreedable(selectedPokemon.getDexNumber()))
                        updatedGameList.add(i);
                    continue;
                }
                if (i.getPokedex().size() == 0) {
                    if (i.getGeneration() >= selectedPokemon.getGeneration())
                        updatedGameList.add(i);
                } else {
                    if (Collections.binarySearch(i.getPokedex(), selectedPokemon.getDexNumber()) > 0)
                        updatedGameList.add(i);
                }
            }
            gameComboBox.getItems().addAll(updatedGameList);
            if(selectedGame != null)
                gameComboBox.getSelectionModel().select(selectedGame);
            return;
        }

        if(selectedMethod != null){

        }
    }

    private static void updateMethodList(){
        ObservableList<Method> updatedMethodList = FXCollections.observableArrayList();

        if(selectedPokemon != null){

        }

        if(selectedGame != null){

        }
    }
}
