package shinyhunttracker;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Vector;

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
        resetVariables();

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

        if(defaultGameList.size() == 0) {
            try (FileReader reader = new FileReader("GameData/game.json")) {
                JSONParser jsonParser = new JSONParser();
                JSONArray gameList = (JSONArray) jsonParser.parse(reader);

                for (int i = 0; i < gameList.size(); i++)
                    defaultGameList.add(new Game((JSONObject) gameList.get(i), i));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        gameComboBox.getItems().setAll(defaultGameList);

        methodComboBox.setPromptText("---Method---");
        methodComboBox.setMinWidth(200);
        methodComboBox.setLayoutY(305);
        methodComboBox.setLayoutX(87.5);

        if(defaultMethodList.size() == 0) {
            try (FileReader reader = new FileReader("GameData/method.json")) {
                JSONParser jsonParser = new JSONParser();
                JSONArray methodList = (JSONArray) jsonParser.parse(reader);

                for (int i = 0; i < methodList.size(); i++)
                    defaultMethodList.add(new Method((JSONObject) methodList.get(i), i));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
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

        if(defaultPokemonList.size() == 0) {
            try (FileReader reader = new FileReader("GameData/pokemon.json")) {
                JSONParser jsonParser = new JSONParser();
                JSONArray pokemonListJSON = (JSONArray) jsonParser.parse(reader);

                for (int i = 0; i < pokemonListJSON.size(); i++)
                    defaultPokemonList.add(new TreeItem<>(new Pokemon((JSONObject) pokemonListJSON.get(i), i)));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
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
                        setPokemonSprite(pokemonSprite, newValue.getValue().toString(), new Game(21));
                        updateGameList();
                        updateMethodList();

                        if(selectedGame != null & selectedMethod != null)
                            beginHunt.setDisable(false);
                    }
                });

        gameComboBox.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue != selectedGame) {
                        selectedGame = newValue;
                        updatePokemonList();
                        updateMethodList();

                        if(selectedPokemon != null & selectedGame != null)
                            beginHunt.setDisable(false);
                    }
                });

        methodComboBox.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue != selectedMethod) {
                        selectedMethod = newValue;
                        updateGameList();
                        updatePokemonList();

                        if(selectedPokemon != null & selectedGame != null)
                            beginHunt.setDisable(false);
                    }
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
        }
        else if(selectedMethod != null){
            for(int i : selectedMethod.getPokemon())
                updatedPokemonList.add(defaultPokemonList.get(i));

            if(updatedPokemonList.size() == 0){
                for(TreeItem<Pokemon> i : defaultPokemonList) {
                    Game oldestGame = gameComboBox.getItems().get(gameComboBox.getItems().size() - 1);
                    if (i.getValue().getGeneration() <= oldestGame.getGeneration()) {
                        if (!i.getValue().getBreedable() && selectedMethod.getBreeding()) {
                            for (int j : selectedMethod.getGames()) {
                                if (defaultGameList.get(j).hasUnbreedable(i.getValue().getDexNumber()) && !selectedMethod.getBreeding())
                                    updatedPokemonList.add(i);
                                break;
                            }
                        } else
                            updatedPokemonList.add(i);
                    }
                }
            }
        }

        if(selectedGame != null && selectedMethod != null){
            Vector<Integer> huntablePokemon = selectedGame.getMethodTable(selectedMethod.getId());
            if(huntablePokemon.size() != 0){
                for(int i = 0; i < updatedPokemonList.size(); i++){
                    if(i == huntablePokemon.size() || huntablePokemon.get(i) != updatedPokemonList.get(i).getValue().getDexNumber()) {
                        updatedPokemonList.remove(i);
                        i--;
                    }
                }
            }
            else{
                if(selectedMethod.getBreeding())
                    for(int i = 0; i < updatedPokemonList.size(); i++){
                        if (updatedPokemonList.get(i).getValue().getGeneration() <= selectedGame.getGeneration()) {
                            if(!updatedPokemonList.get(i).getValue().getBreedable()) {
                                updatedPokemonList.remove(i);
                                i--;
                            }
                        }
                    }
            }
        }

        pokemonListRoot.getChildren().addAll(updatedPokemonList);
        if(selectedPokemon != null)
            pokemonListTreeView.getSelectionModel().select(defaultPokemonList.get(selectedPokemon.getDexNumber()));
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
                    if (Collections.binarySearch(i.getPokedex(), selectedPokemon.getDexNumber()) >= 0)
                        updatedGameList.add(i);
                }
            }
        }
        else if(selectedMethod != null){
            for(int i : selectedMethod.getGames())
                updatedGameList.add(defaultGameList.get(i));

            if(updatedGameList.size() == 0)
                updatedGameList.addAll(defaultGameList);
        }

        if(selectedPokemon != null && selectedMethod != null){
            if(selectedMethod.getGames().size() != 0) {
                for (int i = 0; i < updatedGameList.size(); i++) {
                    if (selectedMethod.getBreeding()) {
                        if (!selectedMethod.getGames().contains(updatedGameList.get(i).getId())) {
                            updatedGameList.remove(i);
                            i--;
                        }
                        continue;
                    }

                    Vector<Integer> huntablePokemon = updatedGameList.get(i).getMethodTable(selectedMethod.getId());
                    if (huntablePokemon.size() == 0 || Collections.binarySearch(huntablePokemon, selectedPokemon.getDexNumber()) < 0) {
                        updatedGameList.remove(i);
                        i--;
                    }
                }
            }
        }

        gameComboBox.getItems().addAll(updatedGameList);
        if(selectedGame != null)
            gameComboBox.getSelectionModel().select(selectedGame);
    }

    private static void updateMethodList(){
        methodComboBox.getItems().clear();
        ObservableList<Method> updatedMethodList = FXCollections.observableArrayList();

        if(selectedPokemon != null){
            for(Game i : defaultGameList) {
                for (int j : i.getMethods()) {
                    if (!updatedMethodList.contains(defaultMethodList.get(j))) {
                        if (defaultMethodList.get(j).getBreeding()) {
                            if (selectedPokemon.getBreedable())
                                updatedMethodList.add(defaultMethodList.get(j));
                        } else {
                            Vector<Integer> huntablePokemon = i.getMethodTable(j);
                            if (huntablePokemon.size() == 0 || Collections.binarySearch(huntablePokemon, selectedPokemon.getDexNumber()) >= 0)
                                updatedMethodList.add(defaultMethodList.get(j));
                        }
                    }
                }
            }
        }
        else if(selectedGame != null){
            for(int i : selectedGame.getMethods())
                updatedMethodList.add(defaultMethodList.get(i));
        }

        if(selectedPokemon != null && selectedGame != null){
            for(int i = 0; i < updatedMethodList.size(); i++){
                if(i == selectedGame.getMethods().size() || updatedMethodList.get(i).getId() != selectedGame.getMethods().get(i) ||
                        (defaultMethodList.get(selectedGame.getMethods().get(i)).getBreeding() && !selectedPokemon.getBreedable())) {
                    updatedMethodList.remove(i);
                    i--;
                }
            }
        }

        methodComboBox.getItems().addAll(updatedMethodList);
        if(selectedMethod != null)
            methodComboBox.getSelectionModel().select(selectedMethod);
    }

    private static void resetVariables(){
        selectedPokemon = null;
        pokemonListTreeView = new TreeView<>();
        pokemonListRoot = new TreeItem<>();

         selectedGame = null;
         gameComboBox = new ComboBox<>();

         selectedMethod = null;
         methodComboBox = new ComboBox<>();
    }
}
