package shinyhunttracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import static javafx.util.Duration.INDEFINITE;
import static javafx.util.Duration.ZERO;

public class HuntSelection{
    static Stage selectionPageStage = new Stage();

    static Pokemon selectedPokemon = null;
    static TreeView<Pokemon> pokemonListTreeView = new TreeView<>();
    static ObservableList<TreeItem<Pokemon>> defaultPokemonList = FXCollections.observableArrayList();
    static ObservableList<TreeItem<Pokemon>> searchPokemonList = FXCollections.observableArrayList();
    static TreeItem<Pokemon> pokemonListRoot = new TreeItem<>();

    static Game selectedGame = null;
    static ObservableList<Game> defaultGameList = FXCollections.observableArrayList();
    static ComboBox<Game> gameComboBox = new ComboBox<>();

    static Method selectedMethod = null;
    static ObservableList<Method> defaultMethodList = FXCollections.observableArrayList();
    static ComboBox<Method> methodComboBox = new ComboBox<>();

    /**
     * Opens a selection page for the user to select the information for the hunt they want to do
     * @param controller huntController to pass back hunt information
     */
    public static void createHuntSelection(HuntController controller){
        //makes sure to not open a new window when one is open
        if(selectionPageStage.isShowing())
            return;

        selectionPageStage.setTitle("Hunt Selection");

        //resets variables for when the window is reopened
        resetVariables();

        //setup selection window layout & initialize default info lists
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

        Button beginHunt = new Button("Start");
        beginHunt.setMinWidth(50);
        beginHunt.setLayoutY(330);
        beginHunt.setLayoutX(162.5);
        beginHunt.setDisable(true);

        Button methodHelp = new Button("?");
        methodHelp.setLayoutY(305);
        methodHelp.setLayoutX(287.5);
        Tooltip methodToolTip = new Tooltip();
        methodToolTip.setShowDelay(ZERO);
        methodToolTip.setShowDuration(INDEFINITE);
        Tooltip.install(methodHelp, methodToolTip);
        methodHelp.visibleProperty().bind(methodComboBox.valueProperty().isNotNull());

        huntInformation.getChildren().addAll(pokemonSprite, gameComboBox, methodComboBox, methodHelp, beginHunt);

        VBox pokemonSelection = new VBox();
        pokemonSelection.setAlignment(Pos.CENTER_RIGHT);
        pokemonSelection.setMinWidth(375);

        TextField pokemonSearch = new TextField();

        pokemonListTreeView.setMinHeight(475);
        pokemonListTreeView.setRoot(pokemonListRoot);
        pokemonListTreeView.setShowRoot(false);

        if(defaultPokemonList.size() == 0) {
            try (FileReader reader = new FileReader("GameData/pokemon.json")) {
                JSONParser jsonParser = new JSONParser();
                JSONArray pokemonListJSON = (JSONArray) jsonParser.parse(reader);

                for (int i = 0; i < pokemonListJSON.size(); i++) {
                    defaultPokemonList.add(new TreeItem<>(new Pokemon((JSONObject) pokemonListJSON.get(i), i)));
                    searchPokemonList.add(defaultPokemonList.get(i));
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        pokemonListRoot.getChildren().addAll(defaultPokemonList);

        pokemonSelection.getChildren().addAll(pokemonSearch, pokemonListTreeView);

        selectionPageLayout.getChildren().addAll(huntInformation, pokemonSelection);
        Scene selectionScene = new Scene(selectionPageLayout, 750, 500);
        selectionPageStage.setScene(selectionScene);
        selectionPageStage.setResizable(false);
        selectionPageStage.show();

        //Listeners for when selection tools are changed
        pokemonListTreeView.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null && newValue.getValue() != selectedPokemon){
                        selectedPokemon = newValue.getValue();
                        FetchImage.setImage(pokemonSprite, newValue.getValue(), new Game(21));
                        updateGameList();
                        updateMethodList();

                        if (selectedGame != null & selectedMethod != null)
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
                        methodToolTip.setText(selectedMethod.getMethodInfo());

                        if(selectedPokemon != null & selectedGame != null)
                            beginHunt.setDisable(false);
                    }
                });

        pokemonSearch.setOnKeyReleased(e -> {
            searchPokemonList.clear();
            for (TreeItem<Pokemon> pokemonTreeItem : defaultPokemonList)
                if (pokemonTreeItem.getValue().getName().toLowerCase().contains(pokemonSearch.getCharacters()))
                    searchPokemonList.add(pokemonTreeItem);

            updatePokemonList();
        });

        //opens new hunt and closes the selection window
        beginHunt.setOnAction(e ->{
            SaveData.saveHunt(new HuntWindow(selectedPokemon, selectedGame, selectedMethod, "", 0, 0, 1, -1));
            SaveData.loadHunt(-1, controller);
            selectionPageStage.close();
        });
    }

    /**
     * updates pokemonListTreeView when either the game or the method boxes are updated
     */
    private static void updatePokemonList(){
        //clears current pokemon list
        pokemonListRoot.getChildren().clear();
        ObservableList<TreeItem<Pokemon>> updatedPokemonList = FXCollections.observableArrayList();

        if(selectedGame != null){
            //Adds all pokemon from game n's pokedex and makes sure that non-breedable pokemon can be caught
            for(int i : selectedGame.getPokedex()) {
                if(!searchPokemonList.contains(defaultPokemonList.get(i)))
                    continue;

                if (!defaultPokemonList.get(i).getValue().getBreedable()) {
                    if (selectedGame.hasUnbreedable(i))
                        updatedPokemonList.add(defaultPokemonList.get(i));
                } else
                    updatedPokemonList.add(defaultPokemonList.get(i));
            }

            //All games since before Let's Go Pikachu & Eevee didn't have restricted pokedexes.
            //In this case, all pokemon with a generation lower than the game are added while removing unobtainable non-breedable pokemon
            if(updatedPokemonList.size() == 0)
                for(TreeItem<Pokemon> i : searchPokemonList)
                    if(i.getValue().getGeneration() <= selectedGame.getGeneration())
                        if(!i.getValue().getBreedable()) {
                            if(selectedGame.hasUnbreedable(i.getValue().getDexNumber()))
                                updatedPokemonList.add(i);
                        }
                        else
                            updatedPokemonList.add(i);
        }
        else if(selectedMethod != null){
            //Most methods have restricted lists attached, so just add those to the list
            for(int i : selectedMethod.getPokemon())
                if(searchPokemonList.contains(defaultPokemonList.get(i)))
                    updatedPokemonList.add(defaultPokemonList.get(i));

            if(updatedPokemonList.size() == 0){
                for(TreeItem<Pokemon> i : searchPokemonList) {
                    //Finds the newest generation and excludes all older games
                    Game newestGame = gameComboBox.getItems().get(gameComboBox.getItems().size() - 1);
                    if (i.getValue().getGeneration() <= newestGame.getGeneration())
                        if (i.getValue().getBreedable() || !selectedMethod.getBreeding())//If its a breeding method, we need to filter out non-breedable pokemon
                            updatedPokemonList.add(i);
                }
            }
        }
        else
            updatedPokemonList = searchPokemonList;

        //takes already made game list and removes the ones that aren't in the method list
        if(selectedGame != null && selectedMethod != null){
            //removes pokemon that aren't listed in the method table
            Vector<Integer> huntablePokemon = selectedGame.getMethodTable(selectedMethod.getId());
            if(huntablePokemon.size() != 0){
                for(int i = 0; i < updatedPokemonList.size(); i++){
                    if(i == huntablePokemon.size() || Collections.binarySearch(huntablePokemon, updatedPokemonList.get(i).getValue().getDexNumber()) < 0) {
                        updatedPokemonList.remove(i);
                        i--;
                    }
                }
            }
            else{
                //removes non-breedable pokemon if its a breeding method
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

    /**
     * updates gameComboBox when either the or the method box are updated
     */
    private static void updateGameList(){
        //resets current game box
        gameComboBox.getItems().clear();
        ObservableList<Game> updatedGameList = FXCollections.observableArrayList();

        if(selectedPokemon != null){
            for(Game i : defaultGameList) {
                //if its unbreedable, its makes sure its catchable in n game
                if (!selectedPokemon.getBreedable() && !i.hasUnbreedable(selectedPokemon.getDexNumber()))
                    continue;

                //for unrestricted gens, add all. for restricted add only if they are listed in the pokedex of n game
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
            //the only method that is in every game is full odds, so add all listed games of each
            for(int i : selectedMethod.getGames())
                updatedGameList.add(defaultGameList.get(i));

            if(updatedGameList.size() == 0)
                updatedGameList.addAll(defaultGameList);
        }
        else
            updatedGameList = defaultGameList;

        //removes from list if they aren't listed in method's list
        if(selectedPokemon != null && selectedMethod != null){
            if(selectedMethod.getGames().size() != 0) {
                for (int i = 0; i < updatedGameList.size(); i++) {
                    if(selectedMethod.getBreeding()) {
                        if (!selectedMethod.getGames().contains(updatedGameList.get(i).getId())) { //makes sure that a breeding method is in n game
                            updatedGameList.remove(i);
                            i--;
                        }
                    }else {
                        //removes from list if it's not in the game method's list
                        Vector<Integer> huntablePokemon = updatedGameList.get(i).getMethodTable(selectedMethod.getId());
                        if (huntablePokemon.size() == 0 || Collections.binarySearch(huntablePokemon, selectedPokemon.getDexNumber()) < 0) {
                            updatedGameList.remove(i);
                            i--;
                        }
                    }
                }
            }
        }

        gameComboBox.getItems().addAll(updatedGameList);
        if(selectedGame != null)
            gameComboBox.getSelectionModel().select(selectedGame);
    }

    /**
     * updates methodComboBox when either the pokemon list or the game box are updated
     */
    private static void updateMethodList(){
        //resets current method box
        methodComboBox.getItems().clear();
        ObservableList<Method> updatedMethodList = FXCollections.observableArrayList();

        if(selectedPokemon != null){
            for(Game i : gameComboBox.getItems()) {
                for (int j : i.getMethods()) {
                    if (!updatedMethodList.contains(defaultMethodList.get(j))) { //don't add method twice
                        if (defaultMethodList.get(j).getBreeding()) {
                            if (selectedPokemon.getBreedable()) //adds breedable methods if pokemon n can breed
                                updatedMethodList.add(defaultMethodList.get(j));
                        } else {
                            Vector<Integer> huntablePokemon = i.getMethodTable(j);
                            if (huntablePokemon.size() == 0 || Collections.binarySearch(huntablePokemon, selectedPokemon.getDexNumber()) >= 0)//full odds or method lists pokemon n
                                updatedMethodList.add(defaultMethodList.get(j));
                        }
                    }
                }
            }
        }
        else if(selectedGame != null){
            for(int i : selectedGame.getMethods())//adds every method listed in game n
                updatedMethodList.add(defaultMethodList.get(i));
        }
        else
            updatedMethodList = defaultMethodList;

        //removes if not in game list if pokemon list is already made
        if(selectedPokemon != null && selectedGame != null){
            for(int i = 0; i < updatedMethodList.size(); i++){
                if(i == selectedGame.getMethods().size() ||  !selectedGame.getMethods().contains(updatedMethodList.get(i).getId()) ||
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

    /**
     * resets all variables when opening a new window
     */
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
