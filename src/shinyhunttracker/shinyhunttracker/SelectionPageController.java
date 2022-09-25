package shinyhunttracker;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.util.Duration.INDEFINITE;
import static javafx.util.Duration.ZERO;

public class SelectionPageController implements Initializable {
    //selection page elements
    public BorderPane shinyTrackerScene;
    public TreeView<String> PokemonList, GameList, MethodList;
    public Label pokemonLabel = new Label(), gameLabel = new Label(), methodLabel = new Label();
    public CheckBox alolanCheckBox, galarianCheckBox, shinyCharmCheckBox, lureCheckBox, dexComplete, dexPerfect;
    public Button beginHuntButton, helpButton;
    public TextField pokemonSearchBar = new TextField();
    Tooltip methodToolTip = new Tooltip();

    //tree view elements
    public TreeItem<String> pokemonRoot, Gen1, Gen2, Gen3, Gen4, Gen5, Gen6, Gen7, Gen8;
    public TreeItem<String> gameRoot, treeGamesGen1, treeGamesGen2, treeGamesGen3, treeGamesGen4, treeGamesGen5, treeGamesGen6, treeGamesGen7, treeGamesGen8;
    public TreeItem<String> methodRoot, evolution0, evolution1, evolution2;
    int index = 0;

    //selected objects
    Game selectedGame = new Game();
    Pokemon selectedPokemon = new Pokemon();
    Method selectedMethod = new Method();

    //hunt page after pokemon is caught variables
    HuntController controller;
    String currentLayout;

    //misc variables
    int oldSelectionGeneration, oldSelectionGameGeneration = 0;
    int evolutionStage = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        InitializePokemonList();//creates pokemon list

        pokemonSearchBar.setPromptText("Search");

        PokemonList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        selectedPokemon = new Pokemon(PokemonList.getSelectionModel().getSelectedIndex() + 1);//creates Pokemon object
                        pokemonLabel.textProperty().bind(selectedPokemon.getNameProperty());

                        //checks if pokemon has regional variant, and resets checkbox
                        alolanCheckBox.setDisable(!selectedPokemon.isAlolan());
                        alolanCheckBox.setSelected(false);
                        galarianCheckBox.setDisable(!selectedPokemon.isGalarian());
                        galarianCheckBox.setSelected(false);

                        //captures old selected game generation
                        if(!selectedGame.getName().equals(""))
                            oldSelectionGameGeneration = selectedGame.getGeneration();

                        InitializeGameList(selectedPokemon.getGeneration());//creates game list
                        //collapseGeneration(oldSelectionGameGeneration);//opens game generation tree view based on previously selected game
                    }
                });

        GameList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        helpButton.setVisible(false);

                        //disables shiny charm and lure checkboxes
                        shinyCharmCheckBox.setDisable(true);
                        lureCheckBox.setDisable(true);
                        dexComplete.setDisable(true);
                        dexPerfect.setDisable(true);

                        String newSelectionGame = newValue.toString().substring(18, newValue.toString().length() - 2);
                        selectedGame = new Game(newSelectionGame);//create Game object
                        gameLabel.textProperty().bind(selectedGame.getNameProperty());

                        //resets shiny charm and lure checkboxes
                        shinyCharmCheckBox.setSelected(false);
                        lureCheckBox.setSelected(false);
                        dexComplete.setSelected(false);
                        dexPerfect.setSelected(false);

                        //Enables Shiny Charm if it is available in the selected Game
                        //Shiny Charm is available from Black 2, White 2 up to the present games
                        if(selectedGame.generation >= 5) {
                            if (!(selectedGame.getName().compareTo("Black") == 0 || selectedGame.getName().compareTo("White") == 0))
                                shinyCharmCheckBox.setDisable(false);
                        }else
                            shinyCharmCheckBox.setDisable(true);

                        //Enables Lure if the selected game is one of the let's go games or legends arceus
                        if(selectedGame.getName().length() >= 3) {
                            lureCheckBox.setDisable(!(selectedGame.getName().substring(0, 3).compareTo("Let") == 0));
                            dexComplete.setDisable(!(selectedGame.getName().substring(0, 3).compareTo("Leg") == 0));
                            dexPerfect.setDisable(!(selectedGame.getName().substring(0, 3).compareTo("Leg") == 0));
                        }

                        InitializeMethodList();//creates method list
                    }
                });

        MethodList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        String newSelectionMethod = newValue.toString().substring(18, newValue.toString().length() - 2);
                        selectedMethod = new Method(newSelectionMethod, selectedGame.getGeneration());//creates Method object
                        methodLabel.textProperty().bind(selectedMethod.getNameProperty());
                        beginHuntButton.setDisable(selectedMethod.getName() == null);//disables begin hunt button if the selected method is a pokemon name
                        helpButton.setVisible(selectedMethod.getName() != null);//displays tooltip button
                        setToolTip(selectedMethod.getName());//creates the tool tip that appears when method is selected
                        methodToolTip.setShowDelay(ZERO);
                        methodToolTip.setShowDuration(INDEFINITE);
                    }
                });
    }

    //creates pokemon list
    public void InitializePokemonList(){
        pokemonRoot = new TreeItem<>();

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("GameData/pokemon.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray pokemonList = (JSONArray) obj;

            //Iterate over array
            pokemonList.forEach( e -> {
                //Get pokemon object within list
                JSONObject pokemonObject = (JSONObject) e;

                //Get pokemon name
                String pokemonName = (String) pokemonObject.get("name");
                makeBranch(pokemonName, pokemonRoot);
            });
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        PokemonList.setRoot(pokemonRoot);
        PokemonList.setShowRoot(false);
    }

    //creates game list
    public void InitializeGameList(int generation){
        if(methodRoot != null)//clears Method list
            clearMethodList();
        selectedGame = new Game();//resets selected Game

        //resets Shiny Charm and Lure checkboxes
        shinyCharmCheckBox.setSelected(false);
        lureCheckBox.setSelected(false);

        //since legendaries are only available in certain games, I created a method to restrict what games are displayed
        /*if(!selectedPokemon.getHuntable()) {
            InitializeRestrictedGameList(generation);
            return;
        }*/

        //initializes all tree items
        gameRoot = new TreeItem<>();

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("GameData/game.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray gameList = (JSONArray) obj;

            //Iterate over array
            gameList.forEach( e -> {
                //Get game object within list
                JSONObject gameObject = (JSONObject) e;

                //Get game name
                String gameName = (String) gameObject.get("name");
                if(generation <= (int) (long) gameObject.get("generation"))
                    makeBranch(gameName, gameRoot);
            });
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    //creates method list
    public void InitializeMethodList(){
        selectedMethod = new Method();
        methodRoot = new TreeItem<>();

        selectedGame.generateMethods(selectedPokemon);
        for(String i: selectedGame.getMethods())
            if (i != null)
                makeBranch(i, methodRoot);
        //evolution2.setExpanded(true);
/*
        if(evolutionStage == 2 && Stage1.getGeneration() <= selectedGame.getGeneration()){
            selectedGame.generateMethods(Stage1);
            evolution1 = makeBranch(Stage1.getName(), methodRoot);
            for(String i: selectedGame.getMethods())
                if (i != null)
                    makeBranch(i, evolution1);

        }if(evolutionStage >= 1 && Stage0.getGeneration() <= selectedGame.getGeneration()){
            selectedGame.generateMethods(Stage0);
            evolution0 = makeBranch(Stage0.getName(), methodRoot);
            for(String i: selectedGame.getMethods())
                if (i != null)
                    makeBranch(i, evolution0);
        }*/

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    //clears method list
    public void clearMethodList(){
        selectedMethod = new Method();
        methodRoot.getChildren().clear();
        shinyCharmCheckBox.setDisable(true);
        lureCheckBox.setDisable(true);
        beginHuntButton.setDisable(true);
        helpButton.setVisible(false);
    }

    //expands the given generation tree view
    public void collapseGeneration(int generation){
        switch(generation){
            case 1:
                treeGamesGen1.setExpanded(true);
                break;
            case 2:
                treeGamesGen2.setExpanded(true);
                break;
            case 3:
                treeGamesGen3.setExpanded(true);
                break;
            case 4:
                treeGamesGen4.setExpanded(true);
                break;
            case 5:
                treeGamesGen5.setExpanded(true);
                break;
            case 6:
                treeGamesGen6.setExpanded(true);
                break;
            case 7:
                treeGamesGen7.setExpanded(true);
                break;
            case 8:
                treeGamesGen8.setExpanded(true);
                break;
            default:
                break;
        }
    }

    //appends "Alolan " to the front of the selected pokemon's name
    public void setAlolan(){
        galarianCheckBox.setSelected(false);
        if(selectedPokemon.getName().length() > 9 && selectedPokemon.getName().substring(0, 9).compareTo("Galarian ") == 0)
            selectedPokemon.setName(selectedPokemon.getName().substring(9));
        if(alolanCheckBox.isSelected())
            selectedPokemon.setName("Alolan " + selectedPokemon.getName());
        else
            selectedPokemon.setName(selectedPokemon.getName().substring(7));
        InitializeGameList(selectedPokemon.getGeneration());
    }

    //appends "Galarian " to the front of the selected pokemon's name
    public void setGalarian(){
        alolanCheckBox.setSelected(false);
        if(selectedPokemon.getName().length() > 7 && selectedPokemon.getName().substring(0, 7).compareTo("Alolan ") == 0)
            selectedPokemon.setName(selectedPokemon.getName().substring(7));
        if(galarianCheckBox.isSelected())
            selectedPokemon.setName("Galarian " + selectedPokemon.getName());
        else
            selectedPokemon.setName(selectedPokemon.getName().substring(9));
        InitializeGameList(selectedPokemon.getGeneration());
    }

    //adds 2 to the selected method modifier
    public void setShinyCharm(){
        if(shinyCharmCheckBox.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 2);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 2);
    }
    public void setDexPerfect(){
        if(dexPerfect.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 2);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 2);
    }

    //adds 1 to the selected method modifier
    public void setLure(){
        if(lureCheckBox.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 1);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 1);
    }
    public void setDexComplete(){
        if(dexComplete.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 1);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 1);
    }

    //creates a tool tip with breif description of the selected hunting method
    public void setToolTip(String selectedMethod){
        switch (selectedMethod){
            case "None":
                methodToolTip.setText("Base odds are being used");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Breeding with Shiny":
                methodToolTip.setText("Breeding a normal pokemon with a shiny pokemon increases your shiny odds dramatically");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Masuda":
                methodToolTip.setText("Breeding pokemon from 2 different languages (ie. English and French) increases your shiny odds");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Radar Chaining":
                methodToolTip.setText("Using the PokeRadar, encounter and defeat a " + selectedPokemon.getName() + "\nOnce the chain has started run into grass that is violently shaking\nIf no violently shaking grass appears, walk 50 steps to reset your radar\nOnce a shiny pokemon appears, sparkles will accompany the shaking grass\nThe shiny odds cap at a chain of 40\n\nThe chain is broken when a pokemon other than " + selectedPokemon.getName() + " is encountered or you leave the grass area that you where hunting in\nIf the chain is broken, the music will change back to default music of the route");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Chain Fishing":
                methodToolTip.setText("For every fish that is encountered, your shiny odds increase\n\nCaps at a chain of 20\nChain is broken when no fish is encountered (\"Nothing seems to be biting...\", \"You Reeled it in too fast!\", or \"You Reeled it in too slow!\")");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Friend Safari":
                methodToolTip.setText("Shiny odds are better in the Friend Safari");
                helpButton.setTooltip(methodToolTip);
                break;
            case "DexNav":
                methodToolTip.setText("Shiny odds increase as Search Level increases\nLarge shiny odd boosts are given at a chain of 50 and 100\nChain is broken when different Pokemon is encountered or the pokemon runs away");
                helpButton.setTooltip(methodToolTip);
                break;
            case "SOS Chaining":
                methodToolTip.setText("Every time a pokemon calls for help, the shiny odds increase\nAdrenaline orbs and the Intimidate ability increase the chance of a pokemon calling for help\n\nShiny odds cap at a chain of 30\nChain is broken when all pokemon are defeated");
                if(selectedGame.getName().compareTo("Sun") == 0 || selectedGame.getName().compareTo("Moon") == 0)
                    methodToolTip.setText(methodToolTip.getText() + "\nShiny odds reset at a chain of 255");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Catch Combo":
                methodToolTip.setText("When you catch the same pokemon back to back the shiny odds increase\n\nShiny odds cap at a chain of 31\nChain is broken when the pokemon flees or you catch a different pokemon");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Ultra Wormholes":
                methodToolTip.setText("Shiny odds increase based on distance, and number of rings surrounding the wormhole");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Total Encounters":
                methodToolTip.setText("The more of the pokemon that you have caught/defeated in total your shiny odds increase\n\nShiny odds cap at 500 total encounters");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Dynamax Adventures":
                methodToolTip.setText("When catching pokemon in dynamax adventures, odds are dramatically higher");
                helpButton.setTooltip(methodToolTip);
                break;
            case "Underground":
                methodToolTip.setText("Digletts drop a buff that gives you slightly better shiny odds");
                helpButton.setTooltip(methodToolTip);
            case "Mass Outbreak":
                methodToolTip.setText("Mass Outbreaks have an increased shiny chance");
                helpButton.setTooltip(methodToolTip);
            default:
                break;
        }
    }

    //method to create branch on given tree item
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
        return item;
    }

    //closes selection window and opens huntControls window
    public void beginHunt(ActionEvent event) {
        Stage selectionWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        selectionWindow.close();

        SaveData saveData = new SaveData(selectedPokemon, selectedGame, selectedMethod, 0, 0, 1, null);
        saveData.saveHunt(-1);
        saveData.loadHunt(-1, controller, "");
    }

    public void pokemonQuickSelect(){
        /*int PokedexSize = 898;

        String searchString = pokemonSearchBar.getText();

        if(searchString.isEmpty() || searchString.length() > 20)
            return;

        boolean alolan = false, galarian = false;

        if(searchString.length() > 7 && searchString.substring(0, 7).toLowerCase().compareTo("alolan ") == 0) {
            alolan = true;
            searchString = searchString.substring(7);
        } else if(searchString.length() > 9 && searchString.substring(0, 9).toLowerCase().compareTo("galarian ") == 0) {
            galarian = true;
            searchString = searchString.substring(9);
        }

        while (index < PokedexSize) {
            if (index < 151) {
                String selectedPokemon = Pokedex[0][index];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen1.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen1.getChildren().get(index));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen1);
                    index++;
                    return;
                }
            } else if (index < 251) {
                String selectedPokemon = Pokedex[1][index - 151];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen2.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen2.getChildren().get(index - 151));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen2);
                    index++;
                    return;
                }
            } else if (index < 386) {
                String selectedPokemon = Pokedex[2][index - 251];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen3.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen3.getChildren().get(index - 251));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen3);
                    index++;
                    return;
                }
            } else if (index < 493) {
                String selectedPokemon = Pokedex[3][index - 386];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen4.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen4.getChildren().get(index - 386));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen4);
                    index++;
                    return;
                }
            } else if (index < 649) {
                String selectedPokemon = Pokedex[4][index - 493];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen5.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen5.getChildren().get(index - 493));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen5);
                    index++;
                    return;
                }
            } else if (index < 721) {
                String selectedPokemon = Pokedex[5][index - 649];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen6.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen6.getChildren().get(index - 649));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen6);
                    index++;
                    return;
                }
            } else if (index < 809) {
                String selectedPokemon = Pokedex[6][index - 721];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen7.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen7.getChildren().get(index - 721));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen7);
                    index++;
                    return;
                }
            } else {
                String selectedPokemon = Pokedex[7][index - 809];
                if (selectedPokemon.length() >= searchString.length() && selectedPokemon.toLowerCase().substring(0, searchString.length()).contains(searchString.toLowerCase())) {
                    Gen8.setExpanded(true);
                    PokemonList.getSelectionModel().select(Gen8.getChildren().get(index - 809));
                    if(searchString.toLowerCase().equals(selectedPokemon.toLowerCase()))
                        pokemonSearchBar.setText("");
                    if(alolan && !alolanCheckBox.isDisable()) {
                        alolanCheckBox.setSelected(true);
                        setAlolan();
                    }
                    if(galarian && !galarianCheckBox.isDisable()){
                        galarianCheckBox.setSelected(true);
                        setGalarian();
                    }
                    collapsePokemonTrees(Gen8);
                    index++;
                    return;
                }
            }
            index++;
        }
        selectedPokemon.setName("");
        collapsePokemonTrees(new TreeItem<>());
        index = 0;*/
    }

    //method to collapse all but the given treeitem
    public void collapsePokemonTrees(TreeItem<String> currentGeneration){
        for(int i = 0; i < pokemonRoot.getChildren().size(); i++){
            if(!PokemonList.getTreeItem(i).equals(currentGeneration))
                PokemonList.getTreeItem(i).setExpanded(false);
        }
    }

    public void setController(HuntController controller){
        this.controller = controller;
    }

    public void setCurrentLayout(String currentLayout){
        this.currentLayout = currentLayout;
    }
}
