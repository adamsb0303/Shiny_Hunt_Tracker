package shinyhunttracker;

public class Game {
    String name;
    int generation;
    String[] Methods = new String[5];
    String[] ShinyLocked = new String[16];

    Game(){

    }

    Game(String name, int generation){
        this.name = name;
        this.generation = generation;
    }

    Game(String name, int generation, Pokemon selectedPokemon){
        this.name = name;
        this.generation = generation;
        generateMethods(name, generation, selectedPokemon);
    }

    public void generateMethods(String name, int generation, Pokemon selectedPokemon){
        switch(generation){
            case 2:
                if(selectedPokemon.getBreedable())
                    Methods[1] = "Breeding with Shiny";
                break;
            case 4:
                if (selectedPokemon.getBreedable())
                    Methods[1] = "Masuda";
                if (name.compareTo("Diamond") == 0 || name.compareTo("Pearl") == 0 || name.compareTo("Platinum") == 0)
                    if (isWild(selectedPokemon))
                        Methods[2] = "Radar Chaining";
                break;
            case 5:
                if(selectedPokemon.getBreedable())
                    Methods[1] = "Masuda";
                break;
            case 6:
                if(selectedPokemon.getBreedable())
                    Methods[1] = "Masuda";
                if(isFish(selectedPokemon))
                    Methods[2] = "Chain Fishing";
                if(name.compareTo("X") == 0 || name.compareTo("Y") == 0){
                    if(isWild(selectedPokemon))
                        Methods[3] = "Radar Chaining";
                    if(isFriendSafari(selectedPokemon))
                        Methods[4] = "Friend Safari";
                }
                else{
                    if(isWild(selectedPokemon))
                        Methods[3] = "DexNav";
                }
                break;
            case 7:
                if(selectedPokemon.getBreedable())
                    Methods[1] = "Masuda";
                if(isSOS(selectedPokemon) && !(name.substring(0,3).compareTo("Let") == 0))
                    Methods[2] = "SOS Chaining";
                if(name.substring(0,3).compareTo("Ult") == 0) {
                    if(isWormhole(selectedPokemon))
                    Methods[3] = "Ultra Wormholes";
                }
                if(name.substring(0,3).compareTo("Let") == 0) {
                    Methods[1] = null;
                    if (isWild(selectedPokemon))
                        Methods[2] = "Catch Combo";
                }
                break;
            case 8:
                if(selectedPokemon.getBreedable())
                    Methods[1] = "Masuda";
                if(isWild(selectedPokemon))
                    Methods[2] = "Total Encounters";
                break;
            default:
                break;
        }
        setShinyLocked();
        for(String i: ShinyLocked)
            if(i != null && i.compareTo(selectedPokemon.name) == 0)
                selectedPokemon.setHuntable(false);
        if(selectedPokemon.getHuntable() && selectedPokemon.getBreedable())
            Methods[0] = "None";
    }

    public boolean isWild(Pokemon selectedPokemon){
        String[] DPP = {"Starly", "Staravia", "Bidoof", "Bibarel", "Kricketot", "Kricketune", "Shinx", "Luxio", "Abra", "Kadabra", "Magikarp", "Gyarados", "Budew", "Roselia", "Zubat", "Goldbat", "Geodude", "Graveler", "Onix", "Steelix", "Machop", "Machoke", "Psyduck", "Golduck", "Burmy", "Wurmple", "Silcoon", "Beautifly", "Casoon", "Dustox", "Combee", "Pachirisu", "Buizel", "Floatzel", "Cherubi", "Shellos", "Gastrodon", "Heracross", "Aipom", "Drifloon", "Buneary", "Gastly", "Haunter", "Goldeen", "Seaking", "Barboach", "Whiscash", "Chingling", "Chimecho", "Meditite", "Medicham", "Bronzor", "Bronzong", "Ponyta", "Bonsly", "Sudowoodo", "Mime Jr.", "Mr. Mime", "Happiny", "Chansey", "Cleffa", "Clefairy", "Chatot", "Pichu", "Pikachu", "Hoothoot", "Noctowl", "Gible", "Unown", "Riolu", "Wooper", "Quagsire", "Wingull", "Pelipper", "Girafarig", "Hippopotas", "Hippowdon", "Azurill", "Marill", "Skorupi", "Drapion", "Croagunk", "Toxicroak", "Carnivine", "Remoraid", "Octillery", "Finneon", "Lumieon", "Tentacool", "Tentacruel", "Feebas", "Mantyke", "Snover", "Abomasnow", "Sneasel", "Nidoran♀", "Nidorina", "Nidoran♂", "Nidorino", "Venonat", "Venomoth", "Mankey", "Primape", "Grimer", "Tauros", "Ditto", "Sentret", "Togepi", "Mareep", "Flaaffy", "Hoppip", "Skiploom", "Sunkern", "Wobbuffet", "Smeargle", "Tyrogue", "Miltank", "Swellow", "Ralts", "Kirlia", "Nincada", "Loudred", "Torkoal", "Swablu", "Baltoy", "Duskull", "Dusclops", "Snorunt"};
        String[] Platinum = {"Slowpoke", "Larvitar", "Mightyena", "Aron", "Kecleon", "Bagon", "Stantler", "Houndour"};
        String[] Diamond = {"Murkow", "Stunky", "Skuntank", "Larvitar", "Mightyena", "Aron", "Trapinch", "Vibrava", "Kecleon"};
        String[] Pearl = {"Misdreavus", "Glameow", "Purugly", "Slowpoke", "Trapinch", "Vibrava", "Bagon", "Stantler", "Houndoom"};

        String[] XY = {"Carterpie", "Weedle", "Kakuna", "Pidgey", "Fearow", "Ekans", "Arbok", "Pikachu", "Sandslash", "Nidoran♀", "Nidorina", "Nidoran♂", "Nidorino", "Jigglypuff", "Zubat", "Oddish", "Dugtrio", "Psyduck", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Machop", "Bellsprout", "Weepinbell", "Tentacool", "Geodude", "Graveler", "Slowpoke", "Magneton", "Farfetch'd", "Doduo", "Haunter", "Onix", "Electrode", "Exeggcute", "Cubone", "Lickitung", "Rhyhorn", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Mr. Mime", "Scyther", "Jynx", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Dratini", "Dragonair", "Sentret", "Noctowl", "Ledyba", "Ariados", "Chinchou", "Lanturn", "Mareep", "Azumarill", "Sudowoodo", "Politoed", "Hoppip", "Yanma", "Quagsire", "Murkrow", "Wobbuffet", "Dunsparce", "Gligar", "Snubbull", "Shuckle", "Sneasel", "Ursaring", "Slugma", "Piloswine", "Remoraid", "Octillery", "Delibird", "Skarmory", "Smeargle", "Smoochum", "Miltank", "Zigzagoon", "Lombre", "Taillow", "Wingull", "Ralts", "Masquerain", "Nincada", "Whismur", "Hariyama", "Azurill", "Nosepass", "Skitty", "Sableye", "Mawile", "Meditite", "Plusle", "Minun", "Volbeat", "Illumise", "Roselia", "Gulpin", "Carvanha", "Sharpedo", "Wailmer", "Torkoal", "Spoink", "Spinda", "Trapinch", "Swablu", "Altaria", "Zangoose", "Seviper", "Lunatone", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Kecleon", "Banette", "Absol", "Sealeo", "Luvdisc", "Bagon", "Starly", "Staravia", "Bidoof", "Bibarel", "Luxio", "Budew", "Burmy", "Combee", "Pachirisu", "Floatzel", "Chingling", "Stunky", "Mime Jr.", "Chatot", "Gible", "Riolu", "Hippopotas", "Skorupi", "Drapion", "Croagunk", "Carnivine", "Mantyke", "Snover", "Abomasnow", "Watchog", "Pansage", "Pansear", "Panpour", "Roggenrola", "Woobat", "Audino", "Gurdurr", "Venipede", "Basculin", "Sandile", "Dwebble", "Scraggy", "Sigilyph", "Trubbish", "Garbodor", "Zoroark", "Gothorita", "Solosis", "Ducklett", "Vanillite", "Emolga", "Karrablast", "Foongus", "Amoonguss", "Ferroseed", "Litwick", "Axew", "Cubchoo", "Beartic", "Cryogonal", "Stunfisk", "Mienfoo", "Druddigon", "Golett", "Pawniard", "Rufflet", "Heatmor", "Durant", "Zweilous", "Hydreigon", "Bunnelby", "Diggersby", "Fletchling", "Scatterbug", "Litleo", "Flabebe", "Skiddo", "Pancham", "Furfrou", "Espurr", "Honedge", "Inkay", "Binacle", "Helioptile", "Hawlucha", "Dedenne", "Carbink", "Sliggoo", "Klefki", "Phantump", "Trevenant", "Bergmite", "Noibat"};
        String[] X = {"Clauncher", "Clawitzer", "Swirlix", "Sawk", "Aron", "Lairon", "Mightyena", "Houndour", "Pinsir"};
        String[] Y = {"Heracross", "Larvitar", "Pupitar", "Electrike", "Liepard", "Throh", "Spritzee", "Skrelp", "Dragalge"};

        String[] ORAS = {"Kakuna", "Pidgeotto", "Rattata", "Raticate", "Pikachu", "Sandshrew", "Clefairy", "Vulpix", "Jigglypuff", "Zubat", "Golbat", "Oddish", "Gloom", "Paras", "Venomoth", "Diglett", "Persian", "Psyduck", "Growlithe", "Abra", "Machop", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Ponyta", "Slowpoke", "Magnemite", "Doduo", "Seel", "Dewgong", "Grimer", "Onix", "Hypno", "Krabby", "Voltorb", "Electrode", "Koffing", "Tangela", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Pinsir", "Magikarp", "Gyarados", "Ditto", "Eevee", "Porygon", "Ariados", "Chinchou", "Lanturn", "Xatu", "Marill", "Azumarill", "Aipom", "Sunkern", "Misdreavus", "Unown", "Wobbuffet", "Girafarig", "Forretress", "Heracross", "Slugma", "Corsola", "Remoraid", "Octillery", "Delibird", "Mantine", "Skarmory", "Donphan", "Stantler", "Tyrogue", "Elekid", "Magby", "Poochyena", "Zigzagoon", "Linoone", "Wurmple", "Silcoon", "Cascoon", "Taillow", "Wingull", "Pelipper", "Ralts", "Surskit", "Masquerain", "Shroomish", "Slakoth", "Nincada", "Whismur", "Loudred", "Makuhita", "Hariyama", "Nosepass", "Skitty", "Aron", "Lairon", "Meditite", "Electrike", "Plusle", "Minun", "Volbeat", "Illumise", "Roselia", "Gulpin", "Carvanha", "Sharpedo", "Wailmer", "Numel", "Torkoal", "Spoink", "Spinda", "Trapinch", "Cacnea", "Swablu", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Baltoy", "Claydol", "Feebas", "Kecleon", "Shuppet", "Duskull", "Tropuis", "Chimecho", "Absol", "Snorunt", "Spheal", "Clamperl", "Relicanth", "Luvdisc", "Bagon", "Kricketune", "Cherubi", "Shellos", "Beneary", "Glameow", "Purugly", "Bronzor", "Happiny", "Chatot", "Gible", "Skorupi", "Finneon", "Mantyke", "Munna", "Pidove", "Zebstrika", "Roggenrola", "Boldore", "Excadrill", "Audino", "Timburr", "Tympole", "Throh", "Sawk", "Sewaddle", "Cottonee", "Petilil", "Sandile", "Darmanitan", "Maractus", "Dwebble", "Crustle", "Scraggy", "Cofagrigus", "Trubbish", "Zorua", "Minccino", "Deerling", "Frillish", "Alomomola", "Joltik", "Klink", "Tynamo", "Elgyem", "Axew", "Cubchoo", "Druddigon", "Bouffalant", "Rufflet", "Vullaby", "Larvesta", "Skrelp", "Clauncher", "Klefki", "Phantump"};
        String[] OmegaRuby = {"Seedot","Nuzleaf", "Mawile", "Zangoose"};
        String[] AlphaSapphire = {"Seviper", "Lunatone", "Sableye", "Lombre"};

        String[] LetsGo = {"Bulbasaur", "Charmander", "Charizard", "Squirtle", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Pikachu", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Jigglypuff", "Zubat", "Golbat", "Paras", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Psyduck", "Golduck", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Machop", "Machoke", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo", "Dodrio", "Seel", "Dewgong", "Shellder", "Cloyster", "Gastly", "Haunter", "Onix", "Drowzee", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Hitmonlee", "Hitmonchan", "Lickitung", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Staryu", "Starmie", "Mr. Mime", "Jynx", "Electabuzz", "Magmar", "Tauros", "Magikarp", "Gyarados", "Lapras", "Eevee", "Porygon", "Snorlax", "Dratini", "Dragonair", "Dragonite"};
        String[] LetsGoPikachu = {"Scyther", "Grimer", "Muk", "Mankey", "Growlithe", "Oddish", "Gloom", "Vileplume", "Sandshrew"};
        String[] LetsGoEevee = {"Ekans", "Vulpix", "Ninetales", "Meowth", "Bellsprout", "Weepinbell", "Victreebel", "Koffing", "Pinsir"};

        String[] SWSH = {"Caterpie", "Metapod", "Butterfree", "Pikachu", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Oddish", "Gloom", "Vileplume", "Diglett", "Dugtrio", "Persian", "Growlithe", "Arcanine", "Machop", "Machoke", "Machamp", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Krabby", "Kingler", "Hitmonlee", "Hitmonchan", "Koffing", "Rhyhorn", "Rhydon", "Goldeen", "Seaking", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Snorlax", "Hoothoot", "Noctowl", "Chinchou", "Lanturn", "Togetic", "Natu", "Xatu", "Bellossom", "Sudowoodo", "Wooper", "Quagsire", "Espeon", "Umbreon", "Wobbuffet", "Steelix", "Qwilfish", "Shuckle", "Sneasel", "Swinub", "Piloswine", "Remoraid", "Octillery", "Delibird", "Mantine", "Tyrogue", "Hitmontop", "Wingull", "Pelipper", "Ralts", "Kirlia", "Gardevoir", "Nincada", "Ninjask", "Shedinja", "Electrike", "Manectric", "Roselia", "Wailmer", "Wailord", "Torkoal", "Trapinch", "Flygon", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Baltoy", "Claydol", "Feebas", "Milotic", "Duskull", "Dusclops", "Snorunt", "Glalie", "Budew", "Roserade", "Combee", "Vespiquen", "Cherubi", "Shellos", "Gastrodon", "Stunky", "Skuntank", "Bronzor", "Bronzong", "Bonsly", "Mime Jr.", "Munchlax", "Riolu", "Lucario", "Hippopotas", "Hippowdon", "Skorupi", "Drapion", "Mantyke", "Snover", "Abomasnow", "Weavile", "Togekiss", "Leafeon", "Glaceon", "Gallade", "Dusknoir", "Rotom", "Purrloin", "Liepard", "Munna", "Musharna", "Pidove", "Tranquill", "Unfezant", "Roggenrola", "Boldore", "Gigalith", "Woobat", "Drilbur", "Excadrill", "Timburr", "Gurdurr", "Conkeldurr", "Tympole", "Palpitoad", "Seismitoad", "Throh", "Sawk", "Cottonee", "Basculin","Maractus", "Dwebble", "Crustle", "Sigilyph", "Trubbish", "Garbodor", "Minccino", "Cinccino", "Vanillite", "Vanillish", "Vanilluxe", "Karrablast", "Frillish", "Jellicent", "Joltik", "Galvantula", "Ferroseed", "Ferrothorn", "Klink", "Klank", "Klinklang", "Elgyem", "Beheeyem", "Litwick", "Lampent", "Chandelure", "Axew", "Haxorus", "Cubchoo", "Beartic", "Shelmet", "Golett", "Golurk", "Pawniard", "Bisharp", "Heatmor", "Durant", "Bunnelby", "Diggersby", "Pancham", "Pangoro", "Espurr", "Meowstic", "Honedge", "Doublade", "Aegislash", "Inkay", "Binacle", "Barbaracle", "Helioptile", "Sylveon", "Hawlucha", "Phantump", "Pumpkaboo", "Gourgeist", "Bergmite", "Avalugg", "Noibat", "Noivern", "Grubbin", "Charjabug", "Vikavolt", "Cutiefly", "Ribombee", "Wishiwashi", "Mareanie", "Toxapex", "Mudbray", "Mudsdale", "Dewpider", "Araquanid", "Morelull", "Shiinotic", "Salandit", "Stufful", "Bewear", "Bounsweet", "Steenee", "Tsareena", "Wimpod", "Golisopod", "Pyukumuku", "Togedemaru", "Mimikyu", "Dhelmise", "Skwovet", "Greedent", "Rookidee", "Corvisquire", "Corviknight", "Blipbug", "Dottler", "Orbeetle", "Nickit", "Thievul", "Gossifleur", "Eldegoss", "Wooloo", "Dubwool", "Chewtle", "Drednaw", "Yamper", "Boltund", "Rolycoly", "Carkol", "Coalossal", "Applin", "Silicobra", "Sandaconda", "Cramorant", "Arrokuda", "Barraskewda", "Toxel", "Sizzlipede", "Clobbopus", "Grapploct", "Sinistea", "Hatenna", "Hattrem", "Hatterene", "Impidimp", "Morgrem", "Grimmsnarl", "Obstagoon", "Perrserker", "Milcery", "Falinks", "Pincurchin", "Snom", "Indeedee", "Morpeko", "Cufant", "Copperajah", "Duraludon", "Dreepy", "Drakloak", "Galarian Meowth", "Galarian Ponyta", "Galarian Farfetch'd", "Galarian Weezing", "Galarian Mr Mime", "Galarian Zigzagoon", "Galarian Linoone", "Galarian Yamask", "Galarian Stunfisk"};
        String[] Sword = {"Galarian Darumaka", "Galarian Darmanitan", "Stonjourner", "Jangmo-o", "Hakamo-o", "Kommo-o", "Turtonator", "Passimian", "Swirlix", "Deino", "Zweilous", "Rufflet", "Braviary", "Gothita", "Gothorita", "Scraggy", "Seedot", "Nuzleaf", "Shiftry", "Mawile"};
        String[] Shield = {"Galarian Corsola", "Eiscue", "Drampa", "Oranguru", "Goomy", "Sliggoo", "Spritzee", "Vullaby", "Mandibuzz", "Solosis", "Duosion", "Reuniclus", "Croagunk", "Toxicroak", "Solrock", "Larvitar", "Pupitar", "Lotad", "Lombre", "Ludicolo"};

        switch (this.generation){
            case 4:
                for(String i: DPP)
                    if(i.compareTo(selectedPokemon.getName()) == 0)
                        return true;
                switch(this.name){
                    case "Diamond":
                        for(String i: Diamond)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Pearl":
                        for(String i: Pearl)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Platinum":
                        for(String i: Platinum)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                }
            case 6:
                switch (this.name){
                    case "X":
                        for(String i: XY)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        for(String i: X)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Y":
                        for(String i: XY)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        for(String i: Y)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Omega Ruby":
                        for(String i: ORAS)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        for(String i: OmegaRuby)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Alpha Sapphire":
                        for(String i: ORAS)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        for(String i: AlphaSapphire)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                }
            case 7:
                for(String i: LetsGo)
                    if(i.compareTo(selectedPokemon.getName()) == 0)
                        return true;
                switch (this.name){
                    case "Let's Go Pikachu":
                        for(String i: LetsGoPikachu)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Let's Go Eevee":
                        for(String i: LetsGoEevee)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                }
            case 8:
                for(String i: SWSH)
                    if(i.compareTo(selectedPokemon.getName()) == 0)
                        return true;
                switch (this.name){
                    case "Sword":
                        for(String i: Sword)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                    case "Shield":
                        for(String i: Shield)
                            if(i.compareTo(selectedPokemon.getName()) == 0)
                                return true;
                        break;
                }
            default:
                break;
        }
        return false;
    }

    public boolean isFish(Pokemon selectedPokemon){
        String[] fishXY = {"Barboach", "Whiscash", "Carvanha", "Sharpedo", "Chinchou", "Lanturn", "Clamperl", "Corphish", "Crawdaunt", "Dratini", "Dragonair", "Goldeen", "Seaking", "Horsea", "Seadra", "Magikarp", "Gyarados", "Poliwag", "Poliwhirl", "Poliwrath", "Politoad", "Remoraid", "Octillery", "Alomomola", "Basculin", "Luvdisc", "Qwilfish", "Relicanth", "Corsola"};
        String[] fishX = {"Huntail", "Clauncher", "Clawitzer", "Staryu", "Starmie"};
        String[] fishY = {"Gorebyss", "Shellder", "Cloyster", "Skrelp", "Dragalge"};
        String[] fishORAS = {"Magikarp", "Tentacool", "Goldeen", "Feebas", "Wailmer", "Corphish", "Barboach", "Carvanha", "Luvdisc", "Remoraid", "Crawdaunt", "Sharpedo", "Seaking", "Staryu", "Corsola", "Gyarados", "Whiscash", "Horsea", "Seadra", "Octillery"};
        if(this.name.compareTo("Omega Ruby") == 0 || this.name.compareTo("Alpha Sapphire") == 0)
            for(String i: fishORAS)
                if(selectedPokemon.getName().compareTo(i) == 0)
                    return true;
        switch(this.name) {
            case "X":
                for(String i: fishXY)
                    if(selectedPokemon.getName().compareTo(i) == 0)
                        return true;
                for(String i: fishX)
                    if(selectedPokemon.getName().compareTo(i) == 0)
                        return true;
            case "Y":
                for(String i: fishXY)
                    if(selectedPokemon.getName().compareTo(i) == 0)
                        return true;
                for(String i: fishY)
                    if(selectedPokemon.getName().compareTo(i) == 0)
                        return true;
        }
        return false;
    }

    public boolean isFriendSafari(Pokemon selectedPokemon){
        String[] FriendSafari = {"Teddiursa", "Aipom", "Dunsparce", "Lillipup", "Loudred", "Kecleon", "Audino", "Minccino", "Chansey", "Ditto", "Eevee", "Smeargle", "Growlithe", "Ponyta", "Magmar", "Pansear", "Charmeleon", "Slugma", "Larvesta", "Pyroar", "Ninetales", "Braixen", "Fletchinder", "Mankey", "Machoke", "Meditite", "Mienfoo", "Throh", "Sawk", "Pancham", "Tyrogue", "Breloom", "Hariyama", "Riolu", "Krabby", "Octillery", "Vivarel", "Panpour", "Wartortle", "Gyarados", "Quagsire", "Floatzel", "Poliwhirl", "Azumarill", "Frogadier", "Pidget", "Spearow", "Farfetch'd", "Doduo", "Hoothoot", "Tranquill", "Woodbat", "Swanna", "Tropuis", "Rufflet", "Fletchinder", "Hawlucha", "Oddish", "Tangela", "Sunkern", "Pansage", "Ivysaur", "Swadloon", "Petilil", "Sawsbuck", "Maractus", "Quilladin", "Gogoat", "Kakuna", "Gloom", "Cascoon", "Seviper", "Venomoth", "Ariados", "Swalot", "Garbodor", "Muk", "Drapion", "Toxicroak", "Whirlipede", "Electrode", "Pachirisu", "Emolga", "Dedenne", "Pikachu", "Electabuzz", "Stunfisk", "Helioptile", "Manectric", "Luxio", "Zebstrika", "Galvantula", "Sandshrew", "Wooper", "Phanpy", "Trapinch", "Dugrio", "Marowak", "Nincada", "Camerupt", "Gastrodon", "Papitoad", "Diggersby", "Abra", "Drowzee", "Grumpig", "Munna", "Wobbuffet", "Sigilyph", "Espurr", "Xatu", "Girafarig", "Gothorita", "Duosion", "Nosepass", "Boldore", "Dwebble", "Onix", "Magcargo", "Corsola", "Pupitar", "Rhydon", "Shuckle", "Barbaracle", "Delibird", "Snorunt", "Snover", "Sneasel", "Bearic", "Bergmite", "Dewgong", "Clowster", "Lapras", "Piloswine", "Butterfree", "Paras", "Ledyba", "Combee", "Beautifly", "Masquerain", "Volbeat", "Illumise", "Venomoth", "Pinsir", "Heracross", "Vivillion", "Gabite", "Fraxure", "Dragonair", "Shelgon", "Noibat", "Druddigon", "Sliggoo", "Shuppet", "Lampent", "Phantump", "Pumpkaboo", "Dusclops", "Drifblim", "Spiritomb", "Golurk", "Mightyena", "Nuzleaf", "Pawniard", "Vullaby", "Sneasel", "Cacturne", "Crawdaunt", "Sandile", "Sableye", "Absol", "Liepard", "Inkay", "Magneton", "Mawile", "Ferroseed", "Forretress", "Skamory", "Metang", "Klang", "Bronzong", "Excadrill", "Klefki", "Togepi", "Snubbull", "Kirlia", "Dedenne", "Jigglypuff", "Mawile", "Spirtzee", "Swirlix", "Clefairy", "Floette"};
        for(String i: FriendSafari)
            if(i.compareTo(selectedPokemon.getName()) == 0)
                return true;
        return false;
    }

    public boolean isSOS(Pokemon selectedPokemon){
        String[] SOS ={"Caterpie", "Metapod", "Butterfree", "Golbat", "Crobat", "Tentacruel", "Lumineon", "Slowpoke", "Slowbro", "Haunter", "Gengar", "Cubone", "Kangaskhan", "Goldeen", "Seaking", "Staryu", "Starmie", "Tauros", "Miltank", "Magikarp", "Gyarados", "Eevee", "Espeon", "Umbreon", "Dratini", "Dragonair", "Dragonite", "Pichu", "Pikachu", "Happiny", "Igglybuff", "Jigglypuff", "Corsola", "Mareanie", "Elekid", "Electabuzz", "Chansey", "Magby", "Magmar", "Wailmer", "Wailord", "Barbaoch", "Whiscash", "Snorunt", "Bagon", "Shelgon", "Salamence", "Bonsly", "Sudowoodo", "Muchlax", "Snorlax", "Riolu", "Lucario", "Tubbish", "Pancham", "Carbink", "Sableye", "Trumbeak", "Oranguru", "Passimian", "Jangmo-o", "Hakamo-o", "Kommo-o"};
        String[] SOSUltra = {"Caterpie", "Metapod", "Butterfree", "Rattata", "Raticate", "Zubat", "Golbat", "Crobat", "Dugtrio", "Meowth", "Persian", "Psyduck", "Slowpoke", "Slowbro", "Slowking", "Haunter", "Gengar", "Cubone", "Kangaskhan", "Chansey", "Blissey", "Goldeen", "Seaking", "Staryu", "Starmie", "Tauros", "Miltank", "Magikarp", "Gyarados", "Eevee", "Espeon", "Umbreon", "Dratini", "Dragonair", "Dragonite", "Hoothoot", "Noctowl", "Chinchou", "Lanturn", "Pichu", "Pikachu", "Cleffa", "Clefairy", "Happiny", "Natu", "Xatu", "Aipom", "Ambipom", "Corsola", "Mareanie", "Remoraid", "Octillery", "Smoochum", "Jynx", "Elekid", "Electabuzz", "Magby", "Magmar", "Carvanha", "Sharpedo", "Wailmer", "Wailord", "Trapinch", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Clamperl", "Huntail", "Gorebyss", "Bagon", "Shelgon", "Salamence", "Buneary", "Lopunny", "Bonsly", "Sudowoodo", "Mime Jr.", "Mr. Mime", "Munchlax", "Snorlax", "Riolu", "Lucario", "Finneon", "Lumineon", "Mantyke", "Krokorok", "Scraggy", "Fearow", "Druddigon", "Bisharp", "Pawniard", "Larvesta", "Volcarona", "Fletchling", "Fletchinder", "Pancham", "Pangoro", "Dedenne", "Togedemaru", "Carbink", "Sableye", "Trumbeak", "Toucannon", "Yungoos", "Gumshoos", "Charjabug", "Grubbin", "Vikavolt", "Mudbray", "Mudsdale", "Salandit", "Salazzle", "Stufful", "Oranguru", "Passimian", "Pyukumuku", "Wingull", "Togedemaru", "Janhmo-o", "Hakamo-o", "Kommo-o"};
        if(this.name.substring(0,3).compareTo("Ult") == 0) {
            for (String i : SOSUltra)
                if (i.compareTo(selectedPokemon.getName()) == 0)
                    return true;
            return false;
        }
        else{
            for (String i : SOS)
                if (i.compareTo(selectedPokemon.getName()) == 0)
                    return true;
            return false;
        }
    }

    public boolean isWormhole(Pokemon selectedPokemon){
        String[] Wormhole = {"Quagsire", "Lombre", "Floatzel", "Stunfisk", "Barbaracle", "Nuzleaf", "Grumpig", "Drapion", "Audino", "Heliolisk", "Swellow", "Altaria", "Yanmega", "Sigilyph", "Swanna", "Magcargo", "Medicham", "Hippowdon", "Abomasnow", "Crustle"};
        for(String i: Wormhole)
            if(i.compareTo(selectedPokemon.getName()) == 0)
                return true;
        return false;
    }

    public void setShinyLocked(){
        switch (generation){
            case 5:
                ShinyLocked[0] = "Reshiram";
                ShinyLocked[1] = "Zekrom";
                if(name.compareTo("Black") == 0 || name.compareTo("White") == 0)
                    ShinyLocked[2] = "Victini";
                break;
            case 6:
                if(name.compareTo("X") == 0 || name.compareTo("Y") == 0) {
                    ShinyLocked[0] = "Articuno";
                    ShinyLocked[1] = "Zapdos";
                    ShinyLocked[2] = "Moltres";
                    ShinyLocked[3] = "Mewtwo";
                    ShinyLocked[4] = "Xerneas";
                    ShinyLocked[5] = "Yveltal";
                    ShinyLocked[6] = "Zygarde";
                }else{
                    ShinyLocked[0] = "Zyogre";
                    ShinyLocked[1] = "Groudon";
                    ShinyLocked[2] = "Rayquaza";
                    ShinyLocked[3] = "Deoxys";
                }
                break;
            case 7:
                if(name.substring(0,3).compareTo("Ult") == 0) {
                    ShinyLocked[0] = "Tapu Koko";
                    ShinyLocked[1] = "Tapu Lele";
                    ShinyLocked[2] = "Tapu Bulu";
                    ShinyLocked[3] = "Tapu Fini";
                    ShinyLocked[4] = "Cosmog";
                    ShinyLocked[5] = "Solgaleo";
                    ShinyLocked[6] = "Lunala";
                    ShinyLocked[7] = "Zygarde";
                }
                else if(name.substring(0,3).compareTo("Let") == 0){
                    ShinyLocked[0] = "Articuno";
                    ShinyLocked[1] = "Zapdos";
                    ShinyLocked[2] = "Moltres";
                    ShinyLocked[3] = "Mewtwo";
                }
                else{
                    ShinyLocked[0] = "Tapu Koko";
                    ShinyLocked[1] = "Tapu Lele";
                    ShinyLocked[2] = "Tapu Bulu";
                    ShinyLocked[3] = "Tapu Fini";
                    ShinyLocked[4] = "Cosmog";
                    ShinyLocked[5] = "Solgaleo";
                    ShinyLocked[6] = "Lunala";
                    ShinyLocked[7] = "Nihilego";
                    ShinyLocked[8] = "Buzzwole";
                    ShinyLocked[9] = "Pheromosa";
                    ShinyLocked[10] = "Xurkitree";
                    ShinyLocked[11] = "Celesteela";
                    ShinyLocked[12] = "Kartana";
                    ShinyLocked[13] = "Guzzlord";
                    ShinyLocked[14] = "Necrozma";
                    ShinyLocked[15] = "Zygarde";
                }
                break;
            case 8:
                ShinyLocked[0] = "Type: Null";
                ShinyLocked[1] = "Zacian";
                ShinyLocked[2] = "Zamazenta";
                ShinyLocked[3] = "Eternatus";
                ShinyLocked[4] = "Kubfu";
                break;
            default:
                break;
        }
    }

    public Boolean legendaryIsAvaliable(Pokemon selectedPokemon){
        switch(this.generation) {
            case 1:
                switch(selectedPokemon.getName()){
                    case "Articuno":
                    case "Zapdos":
                    case "Moltres":
                    case "Mewtwo":
                        return true;
                    default:
                        break;
                }
                return false;
            case 2:
                switch (selectedPokemon.getName()) {
                    case "Raikou":
                    case "Lugia":
                    case "Entei":
                    case "Suicune":
                    case "Ho-Oh":
                        return true;
                    default:
                        break;
                }
                return false;
            case 3:
                switch (this.name) {
                    case "Ruby":
                    case "Sapphire":
                    case "Emerald": {
                        switch (selectedPokemon.getName()) {
                            case "Regirock":
                            case "Regice":
                            case "Registeel":
                            case "Latias":
                            case "Latios":
                            case "Rayquaza":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Ruby") == 0)
                            return selectedPokemon.getName().compareTo("Groudon") == 0;
                        else if (this.name.compareTo("Sapphire") == 0)
                            return selectedPokemon.getName().compareTo("Kyogre") == 0;
                        else
                            switch (selectedPokemon.getName()) {
                                case "Lugia":
                                case "Ho-Oh":
                                case "Kyogre":
                                case "Groudon":
                                    return true;
                                default:
                                    break;
                            }
                        return false;
                    }
                    case "LeafGreen":
                    case "FireRed": {
                        switch (selectedPokemon.getName()) {
                            case "Articuno":
                            case "Zapdos":
                            case "Moltres":
                            case "Mewtwo":
                            case "Raikou":
                            case "Entei":
                            case "Suicune":
                            case "Lugia":
                            case "Ho-Oh":
                                return true;
                            default:
                                break;
                        }
                        return false;
                    }
                    default:
                        break;
                }
            case 4:
                switch (this.name) {
                    case "Diamond":
                    case "Pearl":
                    case "Platinum": {
                        switch (selectedPokemon.getName()) {
                            case "Uxie":
                            case "Mesprit":
                            case "Azelf":
                            case "Heatran":
                            case "Regigigas":
                            case "Giratina":
                            case "Cresselia":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Diamond") == 0)
                            return selectedPokemon.getName().compareTo("Dialga") == 0;
                        else if (this.name.compareTo("Pearl") == 0)
                            return selectedPokemon.getName().compareTo("Palkia") == 0;
                        else
                            switch (selectedPokemon.getName()) {
                                case "Articuno":
                                case "Zapdos":
                                case "Moltres":
                                case "Regirock":
                                case "Regice":
                                case "Registeel":
                                case "Dialga":
                                case "Palkia":
                                    return true;
                                default:
                                    break;
                            }
                        return false;
                    }
                    case "SoulSilver":
                    case "HeartGold": {
                        switch (selectedPokemon.getName()) {
                            case "Articuno":
                            case "Zapdos":
                            case "Moltres":
                            case "Mewtwo":
                            case "Raikou":
                            case "Entei":
                            case "Suicune":
                            case "Lugia":
                            case "Ho-Oh":
                            case "Latias":
                            case "Latios":
                            case "Dialga":
                            case "Palkia":
                            case "Giratina":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("SoulSilver") == 0)
                            return selectedPokemon.getName().compareTo("Groudon") == 0;
                        else if(this.name.compareTo("HeartGold") == 0)
                            return selectedPokemon.getName().compareTo("Kyogre") == 0;
                        return false;
                    }
                }
            case 5:
                switch (this.name) {
                    case "Black":
                    case "White":{
                        switch (selectedPokemon.getName()) {
                            case "Cobalion":
                            case "Terrakion":
                            case "Virizion":
                            case "Thundurus":
                            case "Zekrom":
                            case "Landorus":
                            case "Kyurem":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Black") == 0) {
                            return selectedPokemon.getName().compareTo("Zekrom") == 0;
                        } else if(this.name.compareTo("White") == 0)
                            return selectedPokemon.getName().compareTo("Reshiram") == 0;
                        return false;
                    }
                    case "Black 2":
                    case "White 2": {
                        switch (selectedPokemon.getName()) {
                            case "Regirock":
                            case "Regice":
                            case "Registeel":
                            case "Uxie":
                            case "Mesprit":
                            case "Azelf":
                            case "Heatran":
                            case "Regigigas":
                            case "Cresselia":
                            case "Cobalion":
                            case "Terrakion":
                            case "Virizion":
                            case "Kyurem":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Black 2") == 0) {
                            switch (selectedPokemon.getName()) {
                                case "Latias":
                                case "Reshiram":
                                    return true;
                                default:
                                    break;
                            }
                        } else
                            switch (selectedPokemon.getName()) {
                                case "Latios":
                                case "Zekrom":
                                    return true;
                                default:
                                    break;
                            }
                        return false;
                    }
                }
            case 6:
                switch (this.name) {
                    case "X":
                    case "Y": {
                        switch (selectedPokemon.getName()) {
                            case "Articuno":
                            case "Zapdos":
                            case "Moltres":
                            case "Mewtwo":
                            case "Zygarde":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("X") == 0) {
                            return selectedPokemon.getName().compareTo("Xerneas") == 0;
                        } else if (this.name.compareTo("Y") == 0)
                            return selectedPokemon.getName().compareTo("Yveltal") == 0;
                        return false;
                    }
                    case "Omega Ruby":
                    case "Alpha Sapphire": {
                        switch (selectedPokemon.getName()) {
                            case "Raikou":
                            case "Entei":
                            case "Suicune":
                            case "Regirock":
                            case "Regice":
                            case "Registeel":
                            case "Latias":
                            case "Latios":
                            case "Rayquaza":
                            case "Uxie":
                            case "Mesprit":
                            case "Azelf":
                            case "Heatran":
                            case "Regigigas":
                            case "Giratina":
                            case "Cresselia":
                            case "Cobalion":
                            case "Terrakion":
                            case "Virizion":
                            case "Landorus":
                            case "Kyurem":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Omega Ruby") == 0) {
                            switch (selectedPokemon.getName()) {
                                case "Ho-Oh":
                                case "Groudon":
                                case "Palkia":
                                case "Tornadus":
                                case "Reshiram":
                                    return true;
                                default:
                                    break;
                            }
                        } else
                            switch (selectedPokemon.getName()) {
                                case "Lugia":
                                case "Kyogre":
                                case "Dialga":
                                case "Thundurus":
                                case "Zekrom":
                                    return true;
                                default:
                                    break;
                            }
                        return false;
                    }
                }
            case 7:
                switch (this.name) {
                    case "Sun":
                    case "Moon": {
                        switch (selectedPokemon.getName()) {
                            case "Zygarde":
                            case "Type: Null":
                            case "Tapu Koko":
                            case "Tapu Lele":
                            case "Tapu Bulu":
                            case "Tapu Fini":
                            case "Cosmog":
                            case "Necrozma":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Sun") == 0)
                            return selectedPokemon.getName().compareTo("Solgaleo") == 0;
                        else if (this.name.compareTo("Moon") == 0)
                            return selectedPokemon.getName().compareTo("Lunala") == 0;
                        return false;
                    }
                    case "Ultra Sun":
                    case "Ultra Moon": {
                        switch (selectedPokemon.getName()) {
                            case "Articuno":
                            case "Zapdos":
                            case "Moltres":
                            case "Mewtwo":
                            case "Suicune":
                            case "Regirock":
                            case "Regice":
                            case "Registeel":
                            case "Rayquaza":
                            case "Uxie":
                            case "Mesprit":
                            case "Azelf":
                            case "Giratina":
                            case "Cresselia":
                            case "Cobalion":
                            case "Terrakion":
                            case "Virizion":
                            case "Landorus":
                            case "Kyurem":
                            case "Zygarde":
                            case "Type: Null":
                            case "Tapu Koko":
                            case "Tapu Lele":
                            case "Tapu Bulu":
                            case "Tapu Fini":
                            case "Cosmog":
                            case "Necrozma":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Ultra Sun") == 0) {
                            switch (selectedPokemon.getName()) {
                                case "Raikou":
                                case "Ho-Oh":
                                case "Latios":
                                case "Groudon":
                                case "Dialga":
                                case "Heatran":
                                case "Tornadus":
                                case "Reshiram":
                                case "Xerneas":
                                case "Solgaleo":
                                    return true;
                                default:
                                    break;
                            }
                        } else
                            switch (selectedPokemon.getName()) {
                                case "Entei":
                                case "Lugia":
                                case "Latias":
                                case "Kyogre":
                                case "Palkia":
                                case "Regigigas":
                                case "Thundurus":
                                case "Zekrom":
                                case "Yveltal":
                                case "Lunala":
                                    return true;
                                default:
                                    break;
                            }
                        return false;
                    }
                    case "Let's Go Pikachu":
                    case "Let's Go Eevee": {
                        switch (selectedPokemon.getName()) {
                            case "Articuno":
                            case "Zapdos":
                            case "Moltres":
                            case "Mewtwo":
                                return true;
                            default:
                                break;
                        }
                        return false;
                    }
                }
            case 8:
                switch (this.name) {
                    case "Sword":
                    case "Shield": {
                        switch (selectedPokemon.getName()) {
                            case "Type: Null":
                            case "Eternatus":
                            case "Kubfu":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.compareTo("Sword") == 0) {
                            return selectedPokemon.name.compareTo("Zacian") == 0;
                        } else if(this.name.compareTo("Sheild") == 0){
                            return selectedPokemon.name.compareTo("Zamazenta") == 0;
                        }
                        return false;
                    }
                }
            default:
                break;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getGeneration(){
        return generation;
    }

    public String[] getMethods(){
        return Methods;
    }
}
