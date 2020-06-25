package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public BorderPane shinyTrackerScene;
    public TreeView<String> PokemonList, GameList, MethodList;
    public Label pokemonLabel, gameLabel, methodLabel;
    public CheckBox alolanCheckBox, galarianCheckBox, shinyCharmCheckBox, lureCheckBox;
    public TreeItem<String> gameRoot, treeGamesGen1, treeGamesGen2, treeGamesGen3, treeGamesGen4, treeGamesGen5, treeGamesGen6, treeGamesGen7, treeGamesGen8;
    public TreeItem<String> methodRoot, evolution0, evolution1, evolution2;
    Game selectedGame = new Game();
    Pokemon selectedPokemon, Stage0, Stage1 = new Pokemon();
    Method selectedMethod = new Method();
    String newSelectionPokemon, oldSelectionPokemon = "";
    String newSelectionGame = "";
    int oldSelectionGeneration, oldSelectionGameGeneration = 0;
    int evolutionStage;
    String[] gen1 = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch’d", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew"};
    String[] gen2 = {"Chikorita", "Bayleef", "Meganium", "Cyndaquil", "Quilava", "Typhlosion", "Totodile", "Croconaw", "Feraligatr", "Sentret", "Furret", "Hoothoot", "Noctowl", "Ledyba", "Ledian", "Spinarak", "Ariados", "Crobat", "Chinchou", "Lanturn", "Pichu", "Cleffa", "Igglybuff", "Togepi", "Togetic", "Natu", "Xatu", "Mareep", "Flaaffy", "Ampharos", "Bellossom", "Marill", "Azumarill", "Sudowoodo", "Politoed", "Hoppip", "Skiploom", "Jumpluff", "Aipom", "Sunkern", "Sunflora", "Yanma", "Wooper", "Quagsire", "Espeon", "Umbreon", "Murkrow", "Slowking", "Misdreavus", "Unown", "Wobbuffet", "Girafarig", "Pineco", "Forretress", "Dunsparce", "Gligar", "Steelix", "Snubbull", "Granbull", "Qwilfish", "Scizor", "Shuckle", "Heracross", "Sneasel", "Teddiursa", "Ursaring", "Slugma", "Magcargo", "Swinub", "Piloswine", "Corsola", "Remoraid", "Octillery", "Delibird", "Mantine", "Skarmory", "Houndour", "Houndoom", "Kingdra", "Phanpy", "Donphan", "Porygon2", "Stantler", "Smeargle", "Tyrogue", "Hitmontop", "Smoochum", "Elekid", "Magby", "Miltank", "Blissey", "Raikou", "Entei", "Suicune", "Larvitar", "Pupitar", "Tyranitar", "Lugia", "Ho-Oh", "Celebi"};
    String[] gen3 = {"Treecko", "Grovyle", "Sceptile", "Torchic", "Combusken", "Blaziken", "Mudkip", "Marshtomp", "Swampert", "Poochyena", "Mightyena", "Zigzagoon", "Linoone", "Wurmple", "Silcoon", "Beautifly", "Cascoon", "Dustox", "Lotad", "Lombre", "Ludicolo", "Seedot", "Nuzleaf", "Shiftry", "Taillow", "Swellow", "Wingull", "Pelipper", "Ralts", "Kirlia", "Gardevoir", "Surskit", "Masquerain", "Shroomish", "Breloom", "Slakoth", "Vigoroth", "Slaking", "Nincada", "Ninjask", "Shedinja", "Whismur", "Loudred", "Exploud", "Makuhita", "Hariyama", "Azurill", "Nosepass", "Skitty", "Delcatty", "Sableye", "Mawile", "Aron", "Lairon", "Aggron", "Meditite", "Medicham", "Electrike", "Manectric", "Plusle", "Minun", "Volbeat", "Illumise", "Roselia", "Gulpin", "Swalot", "Carvanha", "Sharpedo", "Wailmer", "Wailord", "Numel", "Camerupt", "Torkoal", "Spoink", "Grumpig", "Spinda", "Trapinch", "Vibrava", "Flygon", "Cacnea", "Cacturne", "Swablu", "Altaria", "Zangoose", "Seviper", "Lunatone", "Solrock", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Baltoy", "Claydol", "Lileep", "Cradily", "Anorith", "Armaldo", "Feebas", "Milotic", "Castform", "Kecleon", "Shuppet", "Banette", "Duskull", "Dusclops", "Tropius", "Chimecho", "Absol", "Wynaut", "Snorunt", "Glalie", "Spheal", "Sealeo", "Walrein", "Clamperl", "Huntail", "Gorebyss", "Relicanth", "Luvdisc", "Bagon", "Shelgon", "Salamence", "Beldum", "Metang", "Metagross", "Regirock", "Regice", "Registeel", "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Jirachi", "Deoxys"};
    String[] gen4 = {"Turtwig", "Grotle", "Torterra", "Chimchar", "Monferno", "Infernape", "Piplup", "Prinplup", "Empoleon", "Starly", "Staravia", "Staraptor", "Bidoof", "Bibarel", "Kricketot", "Kricketune", "Shinx", "Luxio", "Luxray", "Budew", "Roserade", "Cranidos", "Rampardos", "Shieldon", "Bastiodon", "Burmy", "Wormadam", "Mothim", "Combee", "Vespiquen", "Pachirisu", "Buizel", "Floatzel", "Cherubi", "Cherrim", "Shellos", "Gastrodon", "Ambipom", "Drifloon", "Drifblim", "Buneary", "Lopunny", "Mismagius", "Honchkrow", "Glameow", "Purugly", "Chingling", "Stunky", "Skuntank", "Bronzor", "Bronzong", "Bonsly", "Mime Jr.", "Happiny", "Chatot", "Spiritomb", "Gible", "Gabite", "Garchomp", "Munchlax", "Riolu", "Lucario", "Hippopotas", "Hippowdon", "Skorupi", "Drapion", "Croagunk", "Toxicroak", "Carnivine", "Finneon", "Lumineon", "Mantyke", "Snover", "Abomasnow", "Weavile", "Magnezone", "Lickilicky", "Rhyperior", "Tangrowth", "Electivire", "Magmortar", "Togekiss", "Yanmega", "Leafeon", "Glaceon", "Gliscor", "Mamoswine", "Porygon-Z", "Gallade", "Probopass", "Dusknoir", "Froslass", "Rotom", "Uxie", "Mesprit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina", "Cresselia", "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus"};
    String[] gen5 = {"Victini", "Snivy", "Servine", "Serperior", "Tepig", "Pignite", "Emboar", "Oshawott", "Dewott", "Samurott", "Patrat", "Watchog", "Lillipup", "Herdier", "Stoutland", "Purrloin", "Liepard", "Pansage", "Simisage", "Pansear", "Simisear", "Panpour", "Simipour", "Munna", "Musharna", "Pidove", "Tranquill", "Unfezant", "Blitzle", "Zebstrika", "Roggenrola", "Boldore", "Gigalith", "Woobat", "Swoobat", "Drilbur", "Excadrill", "Audino", "Timburr", "Gurdurr", "Conkeldurr", "Tympole", "Palpitoad", "Seismitoad", "Throh", "Sawk", "Sewaddle", "Swadloon", "Leavanny", "Venipede", "Whirlipede", "Scolipede", "Cottonee", "Whimsicott", "Petilil", "Lilligant", "Basculin", "Sandile", "Krokorok", "Krookodile", "Darumaka", "Darmanitan", "Maractus", "Dwebble", "Crustle", "Scraggy", "Scrafty", "Sigilyph", "Yamask", "Cofagrigus", "Tirtouga", "Carracosta", "Archen", "Archeops", "Trubbish", "Garbodor", "Zorua", "Zoroark", "Minccino", "Cinccino", "Gothita", "Gothorita", "Gothitelle", "Solosis", "Duosion", "Reuniclus", "Ducklett", "Swanna", "Vanillite", "Vanillish", "Vanilluxe", "Deerling", "Sawsbuck", "Emolga", "Karrablast", "Escavalier", "Foongus", "Amoonguss", "Frillish", "Jellicent", "Alomomola", "Joltik", "Galvantula", "Ferroseed", "Ferrothorn", "Klink", "Klang", "Klinklang", "Tynamo", "Eelektrik", "Eelektross", "Elgyem", "Beheeyem", "Litwick", "Lampent", "Chandelure", "Axew", "Fraxure", "Haxorus", "Cubchoo", "Beartic", "Cryogonal", "Shelmet", "Accelgor", "Stunfisk", "Mienfoo", "Mienshao", "Druddigon", "Golett", "Golurk", "Pawniard", "Bisharp", "Bouffalant", "Rufflet", "Braviary", "Vullaby", "Mandibuzz", "Heatmor", "Durant", "Deino", "Zweilous", "Hydreigon", "Larvesta", "Volcarona", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram", "Zekrom", "Landorus", "Kyurem", "Keldeo", "Meloetta", "Genesect"};
    String[] gen6 = {"Chespin", "Quilladin", "Chesnaught", "Fennekin", "Braixen", "Delphox", "Froakie", "Frogadier", "Greninja", "Bunnelby", "Diggersby", "Fletchling", "Fletchinder", "Talonflame", "Scatterbug", "Spewpa", "Vivillon", "Litleo", "Pyroar", "Flabébé", "Floette", "Florges", "Skiddo", "Gogoat", "Pancham", "Pangoro", "Furfrou", "Espurr", "Meowstic", "Honedge", "Doublade", "Aegislash", "Spritzee", "Aromatisse", "Swirlix", "Slurpuff", "Inkay", "Malamar", "Binacle", "Barbaracle", "Skrelp", "Dragalge", "Clauncher", "Clawitzer", "Helioptile", "Heliolisk", "Tyrunt", "Tyrantrum", "Amaura", "Aurorus", "Sylveon", "Hawlucha", "Dedenne", "Carbink", "Goomy", "Sliggoo", "Goodra", "Klefki", "Phantump", "Trevenant", "Pumpkaboo", "Gourgeist", "Bergmite", "Avalugg", "Noibat", "Noivern", "Xerneas", "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion"};
    String[] gen7 = {"Rowlet", "Dartrix", "Decidueye", "Litten", "Torracat", "Incineroar", "Popplio", "Brionne", "Primarina", "Pikipek", "Trumbeak", "Toucannon", "Yungoos", "Gumshoos", "Grubbin", "Charjabug", "Vikavolt", "Crabrawler", "Crabominable", "Oricorio", "Cutiefly", "Ribombee", "Rockruff", "Lycanroc", "Wishiwashi", "Mareanie", "Toxapex", "Mudbray", "Mudsdale", "Dewpider", "Araquanid", "Fomantis", "Lurantis", "Morelull", "Shiinotic", "Salandit", "Salazzle", "Stufful", "Bewear", "Bounsweet", "Steenee", "Tsareena", "Comfey", "Oranguru", "Passimian", "Wimpod", "Golisopod", "Sandygast", "Palossand", "Pyukumuku", "Type: Null", "Silvally", "Minior", "Komala", "Turtonator", "Togedemaru", "Mimikyu", "Bruxish", "Drampa", "Dhelmise", "Jangmo-o", "Hakamo-o", "Kommo-o", "Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Necrozma", "Magearna", "Marshadow", "Poipole", "Naganadel", "Stakataka", "Blacephalon", "Zeraora"};
    String[] gen8 = {"Grookey", "Thwackey", "Rillaboom", "Scorbunny", "Raboot", "Cinderace", "Sobble", "Drizzile", "Skwovet", "Greedent", "Rookidee", "Corvisquire", "Corviknight", "Blipbug", "Dottler", "Orbeetle", "Nickit", "Thievul", "Gossifleur", "Eldegoss", "Wooloo", "Dubwool", "Chewtle", "Drednaw", "Yamper", "Boltund", "Rolycoly", "Carkol", "Coalossal", "Applin", "Flapple", "Appletun", "Silicobra", "Sandaconda", "Cramorant", "Arrokuda", "Barraskewda", "Toxel", "Toxtricity", "Sizzlipede", "Centiskorch", "Clobbopus", "Grapploct", "Sinistea", "Polteaseist", "Hatenna", "Hattrem", "Hatterene", "Impidimp", "Morgrem", "Grimmsnarl", "Obstagoon", "Perrserker", "Cursola", "Sirfetch'd", "Mr. Rime", "Runerigus", "Milcery", "Alcremie", "Falinks", "Pincurchin", "Snom", "Frosmoth", "Stonjourner", "Eiscue", "Indeedee", "Morpeko", "Cufant", "Copperajah", "Dracozolt", "Actozolt", "Dracovish", "Arctovish", "Duraludon", "Dreepy", "Drakloak", "Dragapult", "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Zarude"};
    String[] Games1= {"Red", "Green", "Blue", "Yellow"};
    String[] Games2= {"Gold", "Silver", "Crystal"};
    String[] Games3= {"Ruby", "Sapphire", "FireRed", "LeafGreen", "Emerald"};
    String[] Games4= {"Diamond", "Pearl", "Platinum", "HeartGold", "SoulSilver"};
    String[] Games5= {"Black", "White","Black 2", "White 2"};
    String[] Games6= {"X", "Y", "Omega Ruby", "Alpha Sapphire"};
    String[] Games7= {"Sun", "Moon", "Ultra Sun", "Ultra Moon", "Let's Go Pikachu", "Let's Go Eevee"};
    String[] Games8= {"Sword", "Shield"};

    @Override
    public void initialize(URL url, ResourceBundle rb){
        InitializePokemonList();
        PokemonList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        newSelectionPokemon = newValue.toString().substring(18, newValue.toString().length()-2);
                        if(findGeneration(newSelectionPokemon) != 0) {
                            if (oldValue != null) {
                                oldSelectionPokemon = oldValue.toString().substring(18, oldValue.toString().length() - 2);
                                if (findGeneration(oldSelectionPokemon) != 0) {
                                    oldSelectionGeneration = findGeneration(oldSelectionPokemon);
                                }
                            }
                            selectedPokemon = new Pokemon(newSelectionPokemon, findGeneration(newSelectionPokemon));
                            pokemonLabel.setText(selectedPokemon.getName());
                            alolanCheckBox.setDisable(!selectedPokemon.isAlolan());
                            alolanCheckBox.setSelected(false);
                            galarianCheckBox.setDisable(!selectedPokemon.isGalarian());
                            galarianCheckBox.setSelected(false);
                            if(selectedGame.getName() != null)
                                oldSelectionGameGeneration = selectedGame.getGeneration();
                            InitializeGameList(selectedPokemon.getGeneration());
                            collapseGeneration(oldSelectionGameGeneration);
                            clearMethodList();
                        }
                    }
                });
        GameList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        newSelectionGame = newValue.toString().substring(18, newValue.toString().length() - 2);
                        if(findGeneration(newSelectionGame) != 0) {
                            selectedGame = new Game(newSelectionGame, findGeneration(newSelectionGame), selectedPokemon);
                            gameLabel.setText(selectedGame.getName());
                            shinyCharmCheckBox.setSelected(false);
                            lureCheckBox.setSelected(false);
                            if(selectedGame.generation >= 5) {
                                if (!(selectedGame.getName().compareTo("Black") == 0 || selectedGame.getName().compareTo("White") == 0))
                                    shinyCharmCheckBox.setDisable(false);
                            }else
                                shinyCharmCheckBox.setDisable(true);
                            if(selectedGame.getName().length() >= 3)
                                lureCheckBox.setDisable(!(selectedGame.getName().substring(0,3).compareTo("Let") == 0));
                            InitializeMethodList();
                        }
                    }
                });
        MethodList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        selectedMethod = new Method(newValue.toString().substring(18, newValue.toString().length() - 2), selectedGame.getGeneration());
                        methodLabel.setText(selectedMethod.getName());
                    }
                });
    }

    public void InitializePokemonList(){
        TreeItem<String> pokemonRoot, Gen1, Gen2, Gen3, Gen4, Gen5, Gen6, Gen7, Gen8;
        selectedPokemon = new Pokemon();
        pokemonLabel.setText(selectedPokemon.getName());
        pokemonRoot = new TreeItem<>();

        Gen1 = makeBranch("Generation 1", pokemonRoot);
        Gen2 = makeBranch("Generation 2", pokemonRoot);
        Gen3 = makeBranch("Generation 3", pokemonRoot);
        Gen4 = makeBranch("Generation 4", pokemonRoot);
        Gen5 = makeBranch("Generation 5", pokemonRoot);
        Gen6 = makeBranch("Generation 6", pokemonRoot);
        Gen7 = makeBranch("Generation 7", pokemonRoot);
        Gen8 = makeBranch("Generation 8", pokemonRoot);

        for(String i: gen1)
            makeBranch(i, Gen1);
        for(String i: gen2)
            makeBranch(i, Gen2);
        for(String i: gen3)
            makeBranch(i, Gen3);
        for(String i: gen4)
            makeBranch(i, Gen4);
        for(String i: gen5)
            makeBranch(i, Gen5);
        for(String i: gen6)
            makeBranch(i, Gen6);
        for(String i: gen7)
            makeBranch(i, Gen7);
        for(String i: gen8)
            makeBranch(i, Gen8);

        PokemonList.setRoot(pokemonRoot);
        PokemonList.setShowRoot(false);
    }

    public void InitializeGameList(int generation){
        selectedGame = new Game();
        shinyCharmCheckBox.setSelected(false);
        lureCheckBox.setSelected(false);
        gameLabel.setText(selectedGame.getName());/*
        if(!selectedPokemon.getHuntable()) {
            InitializeRestrictedGameList(generation);
            return;
        }*/
        gameRoot = new TreeItem<>();
        treeGamesGen1 = new TreeItem<>();
        treeGamesGen2 = new TreeItem<>();
        treeGamesGen3 = new TreeItem<>();
        treeGamesGen4 = new TreeItem<>();
        treeGamesGen5 = new TreeItem<>();
        treeGamesGen6 = new TreeItem<>();
        treeGamesGen7 = new TreeItem<>();
        treeGamesGen8 = new TreeItem<>();
        Game testGame = new Game();

        if(generation == 0)
            return;
        if(generation <= 1)
            treeGamesGen1 = makeBranch("Generation 1", gameRoot);
            for (String i: Games1) {
                testGame = new Game(i, 1, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen1);
            }
        if(generation <= 2)
            treeGamesGen2 = makeBranch("Generation 2", gameRoot);
            for(String i: Games2) {
                testGame = new Game(i, 2, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen2);
            }
        if(generation <= 3)
            treeGamesGen3 = makeBranch("Generation 3", gameRoot);
            for(String i: Games3) {
                testGame = new Game(i, 3, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen3);
            }
        if(generation <= 4)
            treeGamesGen4 = makeBranch("Generation 4", gameRoot);
            for(String i: Games4) {
                testGame = new Game(i, 4, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen4);
            }
        if(generation <= 5)
            treeGamesGen5 = makeBranch("Generation 5", gameRoot);
            for(String i: Games5) {
                testGame = new Game(i, 5, selectedPokemon);
                if (isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen5);
            }
        if(generation <= 6)
            treeGamesGen6 = makeBranch("Generation 6", gameRoot);
            for(String i: Games6) {
                testGame = new Game(i, 6, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen6);
            }
        if(generation <= 7)
            treeGamesGen7 = makeBranch("Generation 7", gameRoot);
            for(String i: Games7) {
                testGame = new Game(i, 7, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen7);
            }
        if(generation <= 8)
            treeGamesGen8 = makeBranch("Generation 8", gameRoot);
            for(String i: Games8) {
                testGame = new Game(i, 8, selectedPokemon);
                if(isEmpty(testGame.getMethods()))
                    makeBranch(i, treeGamesGen8);
            }

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    public void InitializeMethodList(){
        selectedMethod = new Method();
        methodLabel.setText(selectedMethod.getName());
        if(selectedGame.getGeneration() == 1)
            return;
        methodRoot = new TreeItem<>();
        createFamily();
        selectedGame.generateMethods(selectedPokemon);
        evolution2 = makeBranch(selectedPokemon.getName(), methodRoot);
        for(String i: selectedGame.getMethods())
            if (i != null)
                makeBranch(i, evolution2);
            evolution2.setExpanded(true);
        if(evolutionStage == 2 && findGeneration(Stage1.getName()) <= selectedGame.getGeneration()){
            selectedGame.generateMethods(Stage1);
            evolution1 = makeBranch(Stage1.getName(), methodRoot);
            for(String i: selectedGame.getMethods())
                if (i != null)
                    makeBranch(i, evolution1);
        }if(evolutionStage >= 1 && findGeneration(Stage0.getName()) <= selectedGame.getGeneration()){
            selectedGame.generateMethods(Stage0);
            evolution0 = makeBranch(Stage0.getName(), methodRoot);
            for(String i: selectedGame.getMethods())
                if (i != null)
                    makeBranch(i, evolution0);
        }

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    public void clearMethodList(){
        selectedMethod = new Method();
        methodLabel.setText(selectedMethod.getName());
        methodRoot = new TreeItem<>();

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    public void createFamily(){
        String[][] pokemonEvolutions= {{"Bulbasaur","Ivysaur","Venusaur"},{"Charmander", "Charmeleon", "Charizard"}, {"Squirtle", "Wartortle", "Blastoise"}, {"Azurill", "Marill", "Azumarill"}};
        for (String[] pokemonEvolution : pokemonEvolutions) {
            for (int j = 0; j < pokemonEvolutions[0].length; j++) {
                if (selectedPokemon.getName().compareTo(pokemonEvolution[j]) == 0) {
                    evolutionStage = j;
                    if (j >= 1) {
                        Stage0 = new Pokemon(pokemonEvolution[0], findGeneration(pokemonEvolution[0]));
                    }
                    if (j == 2) {
                        Stage1 = new Pokemon(pokemonEvolution[1], findGeneration(pokemonEvolution[1]));
                    }
                    return;
                }
            }
        }
    }

    public int findGeneration(String name){
        for(String i: Games1)
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: Games2)
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: Games3)
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: Games4)
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: Games5)
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: Games6)
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: Games7)
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: Games8)
            if(i.compareTo(name) == 0)
                return 8;

        for(String i: gen1)
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: gen2)
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: gen3)
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: gen4)
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: gen5)
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: gen6)
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: gen7)
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: gen8)
            if(i.compareTo(name) == 0)
                return 8;
        return 0;
    }

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

    public void setAlolan(){
        galarianCheckBox.setSelected(false);
        if(selectedPokemon.getName().length() > 9 && selectedPokemon.getName().substring(0, 9).compareTo("Galarian ") == 0)
            selectedPokemon.setName(selectedPokemon.getName().substring(9, selectedPokemon.getName().length()));
        if(alolanCheckBox.isSelected())
            selectedPokemon.setName("Alolan " + selectedPokemon.getName());
        else
            selectedPokemon.setName(selectedPokemon.getName().substring(7, selectedPokemon.getName().length()));
        pokemonLabel.setText(selectedPokemon.getName());
    }

    public void setGalarian(){
        alolanCheckBox.setSelected(false);
        if(selectedPokemon.getName().length() > 7 && selectedPokemon.getName().substring(0, 7).compareTo("Alolan ") == 0)
            selectedPokemon.setName(selectedPokemon.getName().substring(7, selectedPokemon.getName().length()));
        if(galarianCheckBox.isSelected())
            selectedPokemon.setName("Galarian " + selectedPokemon.getName());
        else
            selectedPokemon.setName(selectedPokemon.getName().substring(9, selectedPokemon.getName().length()));
        pokemonLabel.setText(selectedPokemon.getName());
    }

    public void setShinyCharm(){
        if(shinyCharmCheckBox.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 2);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 2);
    }

    public void setLure(){
        if(shinyCharmCheckBox.isSelected())
            selectedMethod.setModifier(selectedMethod.getModifier() + 1);
        else
            selectedMethod.setModifier(selectedMethod.getModifier() - 1);
    }

    public boolean isEmpty(String[] arr){
        for(String i: arr)
            if(i != null)
                return true;
        return false;
    }

    public TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
        return item;
    }
}
