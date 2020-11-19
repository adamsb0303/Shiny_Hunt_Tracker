package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.util.Duration.INDEFINITE;
import static javafx.util.Duration.ZERO;

public class selectionPageController implements Initializable {
    //selection page elements
    public BorderPane shinyTrackerScene;
    public TreeView<String> PokemonList, GameList, MethodList;
    public Label pokemonLabel = new Label(), gameLabel = new Label(), methodLabel = new Label();
    public CheckBox alolanCheckBox, galarianCheckBox, shinyCharmCheckBox, lureCheckBox;
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
    Pokemon Stage0 = new Pokemon();
    Pokemon Stage1 = new Pokemon();
    Method selectedMethod = new Method();

    //hunt page after pokemon is caught variables
    huntController controller;
    String currentLayout;

    //misc variables
    int oldSelectionGeneration, oldSelectionGameGeneration = 0;
    int evolutionStage = 0;

    //array with every pokemon
    String[][] Pokedex = selectedPokemon.getPokedex();

    //array with pokemon not avaliable in SWSH
    String[] SWSHPokedex = {"Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Alolan Rattata", "Alolan Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Paras", "Parasect", "Venonat", "Venomoth", "Mankey", "Primeape", "Bellsprout", "Weepinbell", "Victribell", "Geodude", "Graveler", "Golem", "Alolan Geodude", "Alolan Graveler", "Alolan Golem", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Alolan Grimer", "Alolan Muk", "Drowzee", "Hypno", "Voltorb", "Electrode", "Chikorita", "Bayleef", "Meganium", "Cynadquil", "Quilava", "Typhlosion", "Totodile", "Craconaw", "Feraligatr", "Sentret", "Ferret", "Ledyba", "Ledian", "Spinarak", "Ariados", "Mareep", "Flaaffy", "Ampharos", "Hoppip", "Skiploom", "Jumpluff", "Aipom", "Sunkern", "Sunfloura", "Yanma", "Murkow", "Misdreavus", "Unown", "Girafarig", "Pineco", "Forretress", "Gligar", "Snubbull", "Granbull", "Teddiursa", "Ursaring", "Slugma", "Magcargo", "Houndour", "Houndoom", "Phanpey", "Donphan", "Stantler", "Smeargle", "Poochyena", "Mightyena", "Wurmple", "Cascoon", "Beautifly", "Silcoon", "Dustox", "Taillow", "Sweallow", "Surskit", "Masquerain", "Shroomish", "Breloom", "Slackoth", "Vigoroth", "Slacking", "Makuhita", "Hariyama", "Nosepass", "Skitty", "Delcatty", "Meditite", "Medicham", "Plusle", "Minun", "Illumise", "Volbeat", "Gulpin", "Swallot", "Numel", "Camerupt", "Spoink", "Grumpig", "Spinda", "Cacnea", "Cactern", "Zangoose", "Serviper", "Castform", "Kecleon", "Shuppet", "Bannette", "Tropius", "Chimecho", "Clamperl", "Huntail", "Gorebyss", "Luvdisc", "Deoxys", "Turtwig", "Grotle", "Torterra", "Chimchar", "Monferno", "Infernape", "Piplup", "Prinplup", "Empoleon", "Starly", "Staravia", "Staraptor", "Bidoof", "Bibarel", "Kricketune", "Kricketot", "Cranidos", "Rampardos", "Shieldon", "Bastiodon", "Burmy", "Wormadam", "Mothim", "Pachirisu", "Buizel", "Floatzel", "Ambipom", "Mismagius", "Honchkrow", "Glameow", "Purugly", "Chingling", "Chatot", "Carnivine", "Finneon", "Lumineon", "Yanmega", "Gliscor", "Probopass", "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus", "Snivy", "Servine", "Serperior", "Tepig", "Pignite", "Emboar", "Oshawott", "Dewott", "Samurott", "Patrat", "Watchog", "Pansage", "Simisage", "Pansear", "Simisear", "Panpour", "Simipour", "Blitzle", "Zebstrika", "Sewaddle", "Swadloon", "Levanny", "Ducklett", "Swanna", "Deerling", "Sawsbuck", "Alomomola", "Tynamo", "Elektrik", "Elektross", "Meloetta", "Chespin", "Quilladin", "Chesnaught", "Fennekin", "Braixen", "Delphox", "Froakie", "Frogadeir", "Greninja", "Scatterbug", "Spewpa", "Vivillion", "Litleo", "Pyroar", "Flabébé", "Floette", "Florges", "Skiddo", "Gogoat", "Furfrou", "Hoopa", "Pikipek", "Trumbeak", "Toucannon", "Yungoos", "Gumshoos", "Crabrawler", "Crabominable", "Oricorio", "Minior", "Komala", "Bruxish"};


    //array with every main line pokemon game
    public String[][] Games= {{"Red", "Green", "Blue", "Yellow"},
                    {"Gold", "Silver", "Crystal"},
                    {"Ruby", "Sapphire", "FireRed", "LeafGreen", "Emerald"},
                    {"Diamond", "Pearl", "Platinum", "HeartGold", "SoulSilver"},
                    {"Black", "White","Black 2", "White 2"},
                    {"X", "Y", "Omega Ruby", "Alpha Sapphire"},
                    {"Sun", "Moon", "Ultra Sun", "Ultra Moon", "Let's Go Pikachu", "Let's Go Eevee"},
                    {"Sword", "Shield"}};

    @Override
    public void initialize(URL url, ResourceBundle rb){
        InitializePokemonList();//creates pokemon list

        PokemonList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        String newSelectionPokemon, oldSelectionPokemon;

                        newSelectionPokemon = newValue.toString().substring(18, newValue.toString().length()-2);
                        if(new Pokemon (newSelectionPokemon).getGeneration() != 0) {//throws away input if selection is Generation 1, Generation 2, etc.
                            if (oldValue != null) {//sets old value to old selected pokemon name if it wasn't one of the generation tree views
                                oldSelectionPokemon = oldValue.toString().substring(18, oldValue.toString().length() - 2);
                                if (new Pokemon (oldSelectionPokemon).getGeneration() != 0) {
                                    oldSelectionGeneration = new Pokemon(oldSelectionPokemon).getGeneration();
                                }
                            }
                            selectedPokemon = new Pokemon(newSelectionPokemon);//creates Pokemon object
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
                            collapseGeneration(oldSelectionGameGeneration);//opens game generation tree view based on previously selected game
                        }
                    }
                });

        GameList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        helpButton.setVisible(false);

                        //disables shiny charm and lure checkboxes
                        shinyCharmCheckBox.setDisable(true);
                        lureCheckBox.setDisable(true);

                        String newSelectionGame = newValue.toString().substring(18, newValue.toString().length() - 2);
                        if(findGenerationGame(newSelectionGame) != 0) {//throws away input if selection is Generation 1, Generation 2, etc.
                            selectedGame = new Game(newSelectionGame, findGenerationGame(newSelectionGame));//create Game object
                            gameLabel.textProperty().bind(selectedGame.getNameProperty());

                            //resets shiny charm and lure checkboxes
                            shinyCharmCheckBox.setSelected(false);
                            lureCheckBox.setSelected(false);

                            //Enables Shiny Charm if it is avaliable in the selected Game
                            //Shiny Charm is available from Black 2, White 2 up to the present games
                            if(selectedGame.generation >= 5) {
                                if (!(selectedGame.getName().compareTo("Black") == 0 || selectedGame.getName().compareTo("White") == 0))
                                    shinyCharmCheckBox.setDisable(false);
                            }else
                                shinyCharmCheckBox.setDisable(true);

                            //Enables Lure if the selected game is one of the let's go games
                            if(selectedGame.getName().length() >= 3)
                                lureCheckBox.setDisable(!(selectedGame.getName().substring(0,3).compareTo("Let") == 0));

                            InitializeMethodList();//creates method list
                        }
                    }
                });

        MethodList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        String newSelectionMethod = newValue.toString().substring(18, newValue.toString().length() - 2);
                        if(new Pokemon (newSelectionMethod).getGeneration() == 0) {
                            selectedMethod = new Method(newSelectionMethod, selectedGame.getGeneration());//creates Method object
                            methodLabel.textProperty().bind(selectedMethod.getNameProperty());
                            beginHuntButton.setDisable(selectedMethod.getName() == null);//disables begin hunt button if the selected method is a pokemon name
                            helpButton.setVisible(selectedMethod.getName() != null);//displays tooltip button
                            setToolTip(selectedMethod.getName());//creates the tool tip that appears when method is selected
                            methodToolTip.setShowDelay(ZERO);
                            methodToolTip.setShowDuration(INDEFINITE);
                        }
                    }
                });
    }

    //creates pokemon list
    public void InitializePokemonList(){
        pokemonRoot = new TreeItem<>();

        //create tree views by pokemon generation
        Gen1 = makeBranch("Generation 1", pokemonRoot);
        Gen2 = makeBranch("Generation 2", pokemonRoot);
        Gen3 = makeBranch("Generation 3", pokemonRoot);
        Gen4 = makeBranch("Generation 4", pokemonRoot);
        Gen5 = makeBranch("Generation 5", pokemonRoot);
        Gen6 = makeBranch("Generation 6", pokemonRoot);
        Gen7 = makeBranch("Generation 7", pokemonRoot);
        Gen8 = makeBranch("Generation 8", pokemonRoot);

        //placeholders so that the user can expand the lists
        makeBranch("placeholder", Gen1);
        makeBranch("placeholder", Gen2);
        makeBranch("placeholder", Gen3);
        makeBranch("placeholder", Gen4);
        makeBranch("placeholder", Gen5);
        makeBranch("placeholder", Gen6);
        makeBranch("placeholder", Gen7);
        makeBranch("placeholder", Gen8);

        //add listeners for when the lists are expanded, and add all pokemon and sprites to the list
        Gen1.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen1.getChildren().get(0).getValue().contains("placeholder"))
                Gen1.getChildren().remove(0);
            for(String i: Pokedex[0]) {
                String filePath;
                if(i.compareTo("Nidoran♀") == 0)
                    filePath = "Images/PC Sprites/Generation 1/nidoran-f.png";
                else if(i.compareTo("Nidoran♂") == 0)
                    filePath = "Images/PC Sprites/Generation 1/nidoran-m.png";
                else if(i.compareTo("Mr. Mime") == 0)
                    filePath = "Images/PC Sprites/Generation 1/mr-mime.png";
                else if(i.compareTo("Farfetch'd") == 0)
                    filePath = "Images/PC Sprites/Generation 1/farfetchd.png";
                else
                    filePath = "Images/PC Sprites/Generation 1/" + i.toLowerCase() + ".png";
                fetchImage getImage = new fetchImage(filePath);
                getImage.setImage(makeBranch(i, Gen1));
            }
        });
        Gen2.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen2.getChildren().get(0).getValue().contains("placeholder"))
                Gen2.getChildren().remove(0);
            for(String i: Pokedex[1]) {
                fetchImage getImage = new fetchImage("Images/PC Sprites/Generation 2/" + i.toLowerCase() + ".png");
                getImage.setImage(makeBranch(i, Gen2));
            }
        });
        Gen3.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen3.getChildren().get(0).getValue().contains("placeholder"))
                Gen3.getChildren().remove(0);
            for(String i: Pokedex[2]) {
                fetchImage getImage = new fetchImage("Images/PC Sprites/Generation 3/" + i.toLowerCase() + ".png");
                getImage.setImage(makeBranch(i, Gen3));
            }
        });
        Gen4.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen4.getChildren().get(0).getValue().contains("placeholder"))
                Gen4.getChildren().remove(0);
            for(String i: Pokedex[3]) {
                String filePath;
                if(i.compareTo("Mime Jr.") == 0)
                    filePath = ("Images/PC Sprites/Generation 4/mime-jr.png");
                else
                    filePath = "Images/PC Sprites/Generation 4/" + i.toLowerCase() + ".png";
                fetchImage getImage = new fetchImage(filePath);
                getImage.setImage(makeBranch(i, Gen4));
            }
        });
        Gen5.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen5.getChildren().get(0).getValue().contains("placeholder"))
                Gen5.getChildren().remove(0);
            for(String i: Pokedex[4]) {
                fetchImage getImage = new fetchImage("Images/PC Sprites/Generation 5/" + i.toLowerCase() + ".png");
                getImage.setImage(makeBranch(i, Gen5));
            }
        });
        Gen6.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen6.getChildren().get(0).getValue().contains("placeholder"))
                Gen6.getChildren().remove(0);
            for(String i: Pokedex[5]) {
                String filePath;
                if(i.compareTo("Flabébé") == 0)
                    filePath = ("Images/PC Sprites/Generation 6/flabebe.png");
                else
                    filePath = ("Images/PC Sprites/Generation 6/" + i.toLowerCase() + ".png");
                fetchImage getImage = new fetchImage(filePath);
                getImage.setImage(makeBranch(i, Gen6));
            }
        });
        Gen7.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen7.getChildren().get(0).getValue().contains("placeholder"))
                Gen7.getChildren().remove(0);
            for(String i: Pokedex[6]) {
                String filePath;
                if(i.compareTo("Type: Null") == 0)
                    filePath = ("Images/PC Sprites/Generation 7/type-null.png");
                else if(i.compareTo("Tapu Koko") == 0)
                    filePath = ("Images/PC Sprites/Generation 7/tapu-koko.png");
                else if(i.compareTo("Tapu Lele") == 0)
                    filePath = ("Images/PC Sprites/Generation 7/tapu-lele.png");
                else if(i.compareTo("Tapu Bulu") == 0)
                    filePath = ("Images/PC Sprites/Generation 7/tapu-bulu.png");
                else if(i.compareTo("Tapu Fini") == 0)
                    filePath = ("Images/PC Sprites/Generation 7/tapu-fini.png");
                else
                    filePath = ("Images/PC Sprites/Generation 7/" + i.toLowerCase() + ".png");
                fetchImage getImage = new fetchImage(filePath);
                getImage.setImage(makeBranch(i, Gen7));
            }
        });
        Gen8.addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> {
            if(Gen8.getChildren().get(0).getValue().contains("placeholder"))
                Gen8.getChildren().remove(0);
            for(String i: Pokedex[7]) {
                String filePath;
                if(i.compareTo("Mr. Rime") == 0)
                    filePath = ("Images/PC Sprites/Generation 8/mr-rime.png");
                else if(i.compareTo("Sirfetch'd") == 0)
                    filePath = ("Images/PC Sprites/Generation 8/sirfetchd.png");
                else
                    filePath = ("Images/PC Sprites/Generation 8/" + i.toLowerCase() + ".png");
                fetchImage getImage = new fetchImage(filePath);
                getImage.setImage(makeBranch(i, Gen8));
            }
        });

        PokemonList.setRoot(pokemonRoot);
        PokemonList.setShowRoot(false);
    }

    //creates game list
    public void InitializeGameList(int generation){
        clearMethodList();//clears Method list
        selectedGame = new Game();//resets selected Game

        //resets Shiny Charm and Lure checkboxes
        shinyCharmCheckBox.setSelected(false);
        lureCheckBox.setSelected(false);

        //since legendaries are only available in certain games, I created a method to restrict what games are displayed
        if(!selectedPokemon.getHuntable()) {
            InitializeRestrictedGameList(generation);
            return;
        }

        //initializes all tree items
        gameRoot = new TreeItem<>();
        treeGamesGen1 = new TreeItem<>();
        treeGamesGen2 = new TreeItem<>();
        treeGamesGen3 = new TreeItem<>();
        treeGamesGen4 = new TreeItem<>();
        treeGamesGen5 = new TreeItem<>();
        treeGamesGen6 = new TreeItem<>();
        treeGamesGen7 = new TreeItem<>();
        treeGamesGen8 = new TreeItem<>();

        //checks to see if the selected pokemon is alolan or galarian and sets the generation accordingly
        if(selectedPokemon.getName().length() > 6)
            if(selectedPokemon.getName().substring(0,6).compareTo("Alolan") == 0)
                generation = 7;
        if(selectedPokemon.getName().length() > 8)
            if(selectedPokemon.getName().substring(0,8).compareTo("Galarian") == 0)
                generation = 8;

        //goes through and adds the games to the respective generations based on the selected pokemon's generation
        if(generation == 0)
            return;
        if(generation <= 1) {
            treeGamesGen1 = makeBranch("Generation 1", gameRoot);
            for (String i : Games[0]) {
                makeBranch(i, treeGamesGen1);
            }
        }
        if(generation <= 2) {
            treeGamesGen2 = makeBranch("Generation 2", gameRoot);
            for (String i : Games[1]) {
                makeBranch(i, treeGamesGen2);
            }
        }
        if(generation <= 3) {
            treeGamesGen3 = makeBranch("Generation 3", gameRoot);
            for (String i : Games[2]) {
                makeBranch(i, treeGamesGen3);
            }
        }
        if(generation <= 4) {
            treeGamesGen4 = makeBranch("Generation 4", gameRoot);
            for (String i : Games[3]) {
                makeBranch(i, treeGamesGen4);
            }
        }
        if(generation <= 5) {
            treeGamesGen5 = makeBranch("Generation 5", gameRoot);
            for (String i : Games[4]) {
                makeBranch(i, treeGamesGen5);
            }
        }
        if(generation <= 6) {
            treeGamesGen6 = makeBranch("Generation 6", gameRoot);
            for (String i : Games[5]) {
                makeBranch(i, treeGamesGen6);
            }
        }
        if(generation <= 7){
            treeGamesGen7 = makeBranch("Generation 7", gameRoot);
            for(String i: Games[6]) {
                //only generation 1 pokemon are available in the lets go games
                if(i.substring(0,3).compareTo("Let") == 0) {
                    if (generation == 1)
                        makeBranch(i, treeGamesGen7);
                    if(selectedPokemon.getName().length() > 6)
                        if(selectedPokemon.getName().substring(0,6).compareTo("Alolan") == 0)
                            makeBranch(i, treeGamesGen7);
                }else
                    makeBranch(i, treeGamesGen7);
            }
        }
        if(generation <= 8) {
            treeGamesGen8 = makeBranch("Generation 8", gameRoot);
            for (String i : Games[7]) {
                //since the whole national dex isn't in SWSH it needs to check if the pokemon is in the SWSH pokedex
                //if the selected pokemon's generation is lower than 8
                boolean inSWSH = true;
                if (generation < 8) {
                    for (String j : SWSHPokedex)
                        if (j.compareTo(selectedPokemon.getName()) == 0) {
                            inSWSH = false;
                            break;
                        }
                    if(inSWSH)
                        makeBranch(i, treeGamesGen8);
                } else
                    makeBranch(i, treeGamesGen8);
            }
        }

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    //creates game list for legendary pokemon
    public void InitializeRestrictedGameList(int generation){
        //initializes needed tree elements
        gameRoot = new TreeItem<>();
        treeGamesGen1 = new TreeItem<>();
        treeGamesGen2 = new TreeItem<>();
        treeGamesGen3 = new TreeItem<>();
        treeGamesGen4 = new TreeItem<>();
        treeGamesGen5 = new TreeItem<>();
        treeGamesGen6 = new TreeItem<>();
        treeGamesGen7 = new TreeItem<>();
        treeGamesGen8 = new TreeItem<>();

        //creates object to allow access to legendaryIsAvaliable method
        Game testGame;

        //goes through games array and checks if the legendary is avaliable in any given game
        //if they aren't the game isn't displayed
        if(generation == 0)
            return;
        if(generation <= 1) {
            treeGamesGen1 = makeBranch("Generation 1", gameRoot);
            for (String i : Games[0]) {
                testGame = new Game(i, 1);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen1);
            }
        }
        if(generation <= 2) {
            treeGamesGen2 = makeBranch("Generation 2", gameRoot);
            for (String i : Games[1]) {
                testGame = new Game(i, 2);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen2);
            }
        }
        if(generation <= 3) {
            treeGamesGen3 = makeBranch("Generation 3", gameRoot);
            for (String i : Games[2]) {
                testGame = new Game(i, 3);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen3);
            }
        }
        if(generation <= 4) {
            treeGamesGen4 = makeBranch("Generation 4", gameRoot);
            for (String i : Games[3]) {
                testGame = new Game(i, 4);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen4);
            }
        }
        if(generation <= 5) {
            treeGamesGen5 = makeBranch("Generation 5", gameRoot);
            for (String i : Games[4]) {
                testGame = new Game(i, 5);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen5);
            }
        }
        if(generation <= 6) {
            treeGamesGen6 = makeBranch("Generation 6", gameRoot);
            for (String i : Games[5]) {
                testGame = new Game(i, 6);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen6);
            }
        }
        if(generation <= 7) {
            treeGamesGen7 = makeBranch("Generation 7", gameRoot);
            for (String i : Games[6]) {
                testGame = new Game(i, 7);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen7);
            }
        }
        if(generation <= 8) {
            treeGamesGen8 = makeBranch("Generation 8", gameRoot);
            for (String i : Games[7]) {
                testGame = new Game(i, 8);
                if(testGame.legendaryIsAvaliable(selectedPokemon))
                    makeBranch(i, treeGamesGen8);
            }
        }

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    //creates method list
    public void InitializeMethodList(){
        selectedMethod = new Method();
        methodRoot = new TreeItem<>();
        createFamily(selectedPokemon.getName());
        selectedGame.generateMethods(selectedPokemon);
        evolution2 = makeBranch(selectedPokemon.getName(), methodRoot);
        for(String i: selectedGame.getMethods())
            if (i != null) {
                makeBranch(i, evolution2);
            }
            evolution2.setExpanded(true);

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
        }

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    //clears method list
    public void clearMethodList(){
        selectedMethod = new Method();
        methodRoot = new TreeItem<>();
        Stage0 = new Pokemon();
        Stage1 = new Pokemon();
        evolutionStage = 0;
        shinyCharmCheckBox.setDisable(true);
        lureCheckBox.setDisable(true);
        beginHuntButton.setDisable(true);
        helpButton.setVisible(false);

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    //creates Stage0 and Stage1 objects based on selected pokemon's prior evolutions
    public void createFamily(String name){
        String [][] pokemonEvolutions = {{"Bulbasaur", "Ivysaur", "Venusaur"}, {"Charmander", "Charmeleon", "Charizard"}, {"Squirtle", "Wartortle", "Blastoise"}, {"Caterpie", "Metapod", "Butterfree"}, {"Weedle", "Kakuna", "Beedrill"}, {"Pidgey", "Pidgeotto", "Pidgeot"}, {"Rattata", "Raticate", ""}, {"Spearow", "Fearow", ""}, {"Ekans", "Arbok", ""}, {"Pichu", "Pikachu", "Raichu"}, {"Sandshrew", "Sandslash", ""}, {"Nidoran♀", "Nidorina", "Nidoqueen"}, {"Nidoran♂", "Nidorino", "Nidoking"}, {"Cleffa", "Clefairy", "Clefable"}, {"Vulpix", "Ninetales", ""}, {"Igglybuff", "Jigglypuff", "Wigglytuff"}, {"Zubat", "Golbat", "Crobat"}, {"Oddish", "Gloom", "Vileplume"}, {"Paras", "Parasect", ""}, {"Venonat", "Venomoth", ""}, {"Diglett", "Dugtrio", ""}, {"Meowth", "Persian", ""}, {"Psyduck", "Golduck", ""}, {"Mankey", "Primeape", ""}, {"Growlithe", "Arcanine", ""}, {"Poliwag", "Poliwhirl", "Poliwrath"}, {"Abra", "Kadabra", "Alakazam"}, {"Machop", "Machoke", "Machamp"}, {"Bellsprout", "Weepinbell", "Victreebel"}, {"Tentacool", "Tentacruel", ""}, {"Geodude", "Graveler", "Golem"}, {"Ponyta", "Rapidash", ""}, {"Slowpoke", "Slowbro", ""}, {"Magnemite", "Magneton", "Magnezone"}, {"Doduo", "Dodrio", ""}, {"Seel", "Dewgong", ""}, {"Grimer", "Muk", ""}, {"Shellder", "Cloyster", ""}, {"Gastly", "Haunter", "Gengar"}, {"Onix", "Steelix", ""}, {"Drowzee", "Hypno", ""}, {"Krabby", "Kingler", ""}, {"Voltorb", "Electrode", ""}, {"Exeggcute", "Exeggutor", ""}, {"Cubone", "Marowak", ""}, {"Tyrogue", "Hitmonlee", ""}, {"Tyrogue", "Hitmonchan", ""}, {"Lickitung", "Lickilicky", ""}, {"Koffing", "Weezing", ""}, {"Rhyhorn", "Rhydon", "Rhyperior"}, {"Happiny", "Chansey", "Blissey"}, {"Tangela", "Tangrowth", ""}, {"Horsea", "Seadra", "Kingdra"}, {"Goldeen", "Seaking", ""}, {"Staryu", "Starmie", ""}, {"Mime Jr.", "Mr. Mime", ""}, {"Scyther", "Scizor", ""}, {"Smoochum", "Jynx", ""}, {"Elekid", "Electabuzz", "Electivire"}, {"Magby", "Magmar", "Magmortar"}, {"Magikarp", "Gyarados", ""}, {"Eevee", "Vaporeon", ""}, {"Eevee", "Jolteon", ""}, {"Eevee", "Flareon", ""}, {"Porygon", "Porygon2", "Porygon-Z"}, {"Omanyte", "Omastar", ""}, {"Kabuto", "Kabutops", ""}, {"Munchlax", "Snorlax", ""}, {"Dratini", "Dragonair", "Dragonite"},
                {"Chikorita", "Bayleef", "Meganium"}, {"Cyndaquil", "Quilava", "Typhlosion"}, {"Totodile", "Croconaw", "Feraligatr"}, {"Sentret", "Furret", ""}, {"Hoothoot", "Noctowl", ""}, {"Ledyba", "Ledian", ""}, {"Spinarak", "Ariados", ""}, {"Chinchou", "Lanturn", ""}, {"Togepi", "Togetic", "Togekiss"}, {"Natu", "Xatu", ""}, {"Mareep", "Flaaffy", "Ampharos"}, {"Oddish", "Gloom", "Bellossom"}, {"Azurill", "Marill", "Azumarill"}, {"Bonsly", "Sudowoodo", ""}, {"Poliwag", "Poliwhirl", "Politoed"}, {"Hoppip", "Skiploom", "Jumpluff"}, {"Aipom", "Amibipom", ""}, {"Sunkern", "Sunflora", ""}, {"Yanma", "Yanmega", ""}, {"Wooper", "Quagsire", ""}, {"Eevee", "Espeon", ""}, {"Eevee", "Umbreon", ""}, {"Murkrow", "Honchkrow", ""}, {"Slowpoke", "Slowking", ""}, {"Misdreavus", "Mismagius", ""}, {"Wynaut", "Wobbuffet", ""}, {"Pineco", "Forretress", ""}, {"Gligar", "Gliscor", ""}, {"Snubbull", "Granbull", ""}, {"Sneasel", "Weavile", ""}, {"Teddiursa", "Ursaring", ""}, {"Slugma", "Magcargo", ""}, {"Swinub", "Piloswine", "Mamoswine"}, {"Remoraid", "Octillery", ""}, {"Mantyke", "Mantine", ""}, {"Houndour", "Houndoom", ""}, {"Phanpy", "Donphan", ""}, {"Tyrogue", "Hitmontop", ""}, {"Larvitar", "Pupitar", "Tyranitar"},
                {"Treecko", "Grovyle", "Sceptile"}, {"Torchic", "Combusken", "Blaziken"}, {"Mudkip", "Marshtomp", "Swampert"}, {"Poochyena", "Mightyena", ""}, {"Zigzagoon", "Linoone", ""}, {"Wurmple", "Silcoon", "Beautifly"}, {"Wurmple", "Cascoon", "Dustox"}, {"Lotad", "Lombre", "Ludicolo"}, {"Seedot", "Nuzleaf", "Shiftry"}, {"Taillow", "Swellow", ""}, {"Wingull", "Pelipper", ""}, {"Ralts", "Kirlia", "Gardevoir"}, {"Surskit", "Masquerain", ""}, {"Shroomish", "Breloom", ""}, {"Slakoth", "Vigoroth", "Slaking"}, {"Nincada", "Ninjask", "Shedinja"}, {"Whismur", "Loudred", "Exploud"}, {"Makuhita", "Hariyama", ""}, {"Nosepass", "Probopass", ""}, {"Skitty", "Delcatty", ""}, {"Aron", "Lairon", "Aggron"}, {"Meditite", "Medicham", ""}, {"Electrike", "Manectric", ""}, {"Budew", "Roselia", "Roserade"}, {"Gulpin", "Swalot", ""}, {"Carvanha", "Sharpedo", ""}, {"Wailmer", "Wailord", ""}, {"Numel", "Camerupt", ""}, {"Spoink", "Grumpig", ""}, {"Trapinch", "Vibrava", "Flygon"}, {"Cacnea", "Cacturne", ""}, {"Swablu", "Altaria", ""}, {"Barboach", "Whiscash", ""}, {"Corphish", "Crawdaunt", ""}, {"Baltoy", "Claydol", ""}, {"Lileep", "Cradily", ""}, {"Anorith", "Armaldo", ""}, {"Feebas", "Milotic", ""}, {"Shuppet", "Banette", ""}, {"Duskull", "Dusclops", "Dusknoir"}, {"Chingling", "Chimecho", ""}, {"Snorunt", "Glalie", ""}, {"Spheal", "Sealeo", "Walrein"}, {"Clamperl", "Huntail", ""}, {"Clamperl", "Gorebyss", ""}, {"Bagon", "Shelgon", "Salamence"}, {"Beldum", "Metang", "Metagross"},
                {"Turtwig", "Grotle", "Torterra"}, {"Chimchar", "Monferno", "Infernape"}, {"Piplup", "Prinplup", "Empoleon"}, {"Starly", "Staravia", "Staraptor"}, {"Bidoof", "Bibarel", ""}, {"Kricketot", "Kricketune", ""}, {"Shinx", "Luxio", "Luxray"}, {"Cranidos", "Rampardos", ""}, {"Shieldon", "Bastiodon", ""}, {"Burmy", "Wormadam", ""}, {"Burmy", "Mothim", ""}, {"Combee", "Vespiquen", ""}, {"Buizel", "Floatzel", ""}, {"Cherubi", "Cherrim", ""}, {"Shellos", "Gastrodon", ""}, {"Drifloon", "Drifblim", ""}, {"Buneary", "Lopunny", ""}, {"Glameow", "Purugly", ""}, {"Stunky", "Skuntank", ""}, {"Bronzor", "Bronzong", ""}, {"Gible", "Gabite", "Garchomp"}, {"Riolu", "Lucario", ""}, {"Hippopotas", "Hippowdon", ""}, {"Skorupi", "Drapion", ""}, {"Croagunk", "Toxicroak", ""}, {"Finneon", "Lumineon", ""}, {"Snover", "Abomasnow", ""}, {"Eevee", "Leafeon", ""}, {"Eevee", "Glaceon", ""}, {"Ralts", "Kirlia", "Gallade"}, {"Snorunt", "Froslass", ""},
                {"Snivy", "Servine", "Serperior"}, {"Tepig", "Pignite", "Emboar"}, {"Oshawott", "Dewott", "Samurott"}, {"Patrat", "Watchog", ""}, {"Lillipup", "Herdier", "Stoutland"}, {"Purrloin", "Liepard", ""}, {"Pansage", "Simisage", ""}, {"Pansear", "Simisear", ""}, {"Panpour", "Simipour", ""}, {"Munna", "Musharna", ""}, {"Pidove", "Tranquill", "Unfezant"}, {"Blitzle", "Zebstrika", ""}, {"Roggenrola", "Boldore", "Gigalith"}, {"Woobat", "Swoobat", ""}, {"Drilbur", "Excadrill", ""}, {"Timburr", "Gurdurr", "Conkeldurr"}, {"Tympole", "Palpitoad", "Seismitoad"}, {"Sewaddle", "Swadloon", "Leavanny"}, {"Venipede", "Whirlipede", "Scolipede"}, {"Cottonee", "Whimsicott", ""}, {"Petilil", "Lilligant", ""}, {"Sandile", "Krokorok", "Krookodile"}, {"Darumaka", "Darmanitan", ""}, {"Dwebble", "Crustle", ""}, {"Scraggy", "Scrafty", ""}, {"Yamask", "Cofagrigus", ""}, {"Tirtouga", "Carracosta", ""}, {"Archen", "Archeops", ""}, {"Trubbish", "Garbodor", ""}, {"Zorua", "Zoroark", ""}, {"Minccino", "Cinccino", ""}, {"Gothita", "Gothorita", "Gothitelle"}, {"Solosis", "Duosion", "Reuniclus"}, {"Ducklett", "Swanna", ""}, {"Vanillite", "Vanillish", "Vanilluxe"}, {"Deerling", "Sawsbuck", ""}, {"Karrablast", "Escavalier", ""}, {"Foongus", "Amoonguss", ""}, {"Frillish", "Jellicent", ""}, {"Joltik", "Galvantula", ""}, {"Ferroseed", "Ferrothorn", ""}, {"Klink", "Klang", "Klinklang"}, {"Tynamo", "Eelektrik", "Eelektross"}, {"Elgyem", "Beheeyem", ""}, {"Litwick", "Lampent", "Chandelure"}, {"Axew", "Fraxure", "Haxorus"}, {"Cubchoo", "Beartic", ""}, {"Shelmet", "Accelgor", ""}, {"Mienfoo", "Mienshao", ""}, {"Golett", "Golurk", ""}, {"Pawniard", "Bisharp", ""}, {"Rufflet", "Braviary", ""}, {"Vullaby", "Mandibuzz", ""}, {"Deino", "Zweilous", "Hydreigon"}, {"Larvesta", "Volcarona", ""},
                {"Chespin", "Quilladin", "Chesnaught"}, {"Fennekin", "Braixen", "Delphox"}, {"Froakie", "Frogadier", "Greninja"}, {"Bunnelby", "Diggersby", ""}, {"Fletchling", "Fletchinder", "Talonflame"}, {"Scatterbug", "Spewpa", "Vivillon"}, {"Litleo", "Pyroar", ""}, {"Flabébé", "Floette", "Florges"}, {"Skiddo", "Gogoat", ""}, {"Pancham", "Pangoro", ""}, {"Espurr", "Meowstic", ""}, {"Honedge", "Doublade", "Aegislash"}, {"Spritzee", "Aromatisse", ""}, {"Swirlix", "Slurpuff", ""}, {"Inkay", "Malamar", ""}, {"Binacle", "Barbaracle", ""}, {"Skrelp", "Dragalge", ""}, {"Clauncher", "Clawitzer", ""}, {"Helioptile", "Heliolisk", ""}, {"Tyrunt", "Tyrantrum", ""}, {"Amaura", "Aurorus", ""}, {"Eevee", "Sylveon", ""}, {"Goomy", "Sliggoo", "Goodra"}, {"Phantump", "Trevenant", ""}, {"Pumpkaboo", "Gourgeist", ""}, {"Bergmite", "Avalugg", ""}, {"Noibat", "Noivern", ""},
                {"Rowlet", "Dartrix", "Decidueye"}, {"Litten", "Torracat", "Incineroar"}, {"Popplio", "Brionne", "Primarina"}, {"Pikipek", "Trumbeak", "Toucannon"}, {"Yungoos", "Gumshoos", ""}, {"Grubbin", "Charjabug", "Vikavolt"}, {"Crabrawler", "Crabominable", ""}, {"Cutiefly", "Ribombee", ""}, {"Rockruff", "Lycanroc", ""}, {"Mareanie", "Toxapex", ""}, {"Mudbray", "Mudsdale", ""}, {"Dewpider", "Araquanid", ""}, {"Fomantis", "Lurantis", ""}, {"Morelull", "Shiinotic", ""}, {"Salandit", "Salazzle", ""}, {"Stufful", "Bewear", ""}, {"Bounsweet", "Steenee", "Tsareena"}, {"Wimpod", "Golisopod", ""}, {"Sandygast", "Palossand", ""}, {"Type: Null", "Silvally", ""}, {"Jangmo-o", "Hakamo-o", "Kommo-o"}, {"Cosmog", "Cosmoem", "Solgaleo"}, {"Cosmog", "Cosmoem", "Lunala"}, {"Poipole", "Naganadel", ""},
                {"Grookey", "Thwackey", "Rillaboom"}, {"Scorbunny", "Raboot", "Cinderace"}, {"Sobble", "Drizzile", "Inteleon"}, {"Skwovet", "Greedent", ""}, {"Rookidee", "Corvisquire", "Corviknight"}, {"Blipbug", "Dottler", "Orbeetle"}, {"Nickit", "Thievul", ""}, {"Gossifleur", "Eldegoss", ""}, {"Wooloo", "Dubwool", ""}, {"Chewtle", "Drednaw", ""}, {"Yamper", "Boltund", ""}, {"Rolycoly", "Carkol", "Coalossal"}, {"Applin", "Flapple", ""}, {"Applin", "Appletun", ""}, {"Silicobra", "Sandaconda", ""}, {"Arrokuda", "Barraskewda", ""}, {"Toxel", "Toxtricity", ""}, {"Sizzlipede", "Centiskorch", ""}, {"Clobbopus", "Grapploct", ""}, {"Sinistea", "Polteageist", ""}, {"Hatenna", "Hattrem", "Hatterene"}, {"Impidimp", "Morgrem", "Grimmsnarl"}, {"Galarian Zigzagoon", "Galarian Linoone", "Obstagoon"}, {"Galarian Meowth", "Perrserker", ""}, {"Galarian Corsola", "Cursola", ""}, {"Galarian Farfetch'd", "Sirfetch'd", ""}, {"Mime Jr.", "Galarian Mr. Mime", "Mr. Rime"}, {"Galarian Yamask", "Runerigus", ""}, {"Milcery", "Alcremie", ""}, {"Snom", "Frosmoth", ""}, {"Cufant", "Copperajah", ""}, {"Dreepy", "Drakloak", "Dragapult"}, {"Kubfu", "Urshifu", ""},
                {"Exeggcute", "Alolan Exeggutor", ""}, {"Alolan Geodude", "Alolan Graveler", "Alolan Golem"}, {"Alolan Grimer", "Alolan Muk", ""}, {"Cubone", "Alolan Marowak", ""}, {"Alolan Vulpix", "Alolan Ninetales", ""}, {"Alolan Meowth", "Alolan Persian", ""}, {"Pichu", "Pikachu", "Alolan Raichu"},{"Alolan Rattata", "Alolan Raticate", ""}, {"Alolan Sandshrew", "Alolan Sandslash", ""},
                {"Galarian Darumaka","Galarian Darmanitan", ""}, {"Galarian Ponyta", "Galarian Rapidash", ""}, {"Koffing", "Galarian Weezing", ""}, {"Galarian Slowpoke", "Galarian Slowbro", ""}};

        for (String[] pokemonEvolution : pokemonEvolutions) {
            for (int j = 0; j < pokemonEvolutions[0].length; j++) {
                if (pokemonEvolution[j].compareTo(name) == 0) {
                    evolutionStage = j;
                    if (j >= 1) {
                        Stage0 = new Pokemon(pokemonEvolution[0]);
                    }
                    if (j == 2) {
                        Stage1 = new Pokemon(pokemonEvolution[1]);
                    }
                    return;
                }
            }
        }
    }

    //returns the generation of the given game
    public int findGenerationGame(String name){
        for(String i: Games[0])
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: Games[1])
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: Games[2])
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: Games[3])
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: Games[4])
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: Games[5])
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: Games[6])
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: Games[7])
            if(i.compareTo(name) == 0)
                return 8;
        return 0;
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

    //adds 1 to the selected method modifier
    public void setLure(){
        if(lureCheckBox.isSelected())
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

        controller.addHuntWindow(selectedPokemon, selectedGame, selectedMethod, Stage0.getName(), Stage1.getName(),currentLayout,0, 0, 1);
    }

    public void pokemonQuickSelect(){
        int PokedexSize = 0;
        for(String[] i : Pokedex)
            PokedexSize += i.length;

        String searchString = pokemonSearchBar.getText();

        if(searchString.isEmpty())
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
        index = 0;
    }

    //method to collapse all but the given treeitem
    public void collapsePokemonTrees(TreeItem<String> currentGeneration){
        for(int i = 0; i < pokemonRoot.getChildren().size(); i++){
            if(!PokemonList.getTreeItem(i).equals(currentGeneration))
                PokemonList.getTreeItem(i).setExpanded(false);
        }
    }

    public Pokemon getStage0(){
        return Stage0;
    }

    public Pokemon getStage1(){
        return Stage1;
    }

    public void setController(huntController controller){
        this.controller = controller;
    }

    public void setCurrentLayout(String currentLayout){
        this.currentLayout = currentLayout;
    }
}
