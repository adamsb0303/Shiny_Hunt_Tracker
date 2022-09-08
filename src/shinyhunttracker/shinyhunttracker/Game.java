package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static shinyhunttracker.SaveData.parseJSONArray;

public class Game {
    StringProperty name = new SimpleStringProperty();
    int generation;
    String[] legends;
    String[] methods = new String[5];
    String[] ShinyLocked = new String[18];

    Game(){
        name.setValue("");
        generation = 0;
    }

    Game(int id){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("GameData/game.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray gameList = (JSONArray) obj;

            //parse pokemon data
            JSONObject gameObject = (JSONObject) gameList.get(id);
            name.setValue((String) gameObject.get("name"));
            generation = (int) (long)  gameObject.get("generation");
            methods = parseJSONArray((JSONArray) gameObject.get("method"));
            legends = parseJSONArray((JSONArray) gameObject.get("legends"));
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //add method names to the Method array based on the game
    public void generateMethods(Pokemon selectedPokemon){
        switch(generation){
            case 2:
                if(selectedPokemon.getBreedable())
                    methods[1] = "Breeding with Shiny";
                break;
            case 4:
                if (selectedPokemon.getBreedable())
                    methods[1] = "Masuda";
                if (name.getValue().equals("Diamond") || name.getValue().equals("Pearl") || name.getValue().equals("Platinum"))
                    if (isWild(selectedPokemon))
                        methods[2] = "Radar Chaining";
                break;
            case 5:
                if(selectedPokemon.getBreedable())
                    methods[1] = "Masuda";
                break;
            case 6:
                if(selectedPokemon.getBreedable())
                    methods[1] = "Masuda";
                if(isFish(selectedPokemon))
                    methods[2] = "Chain Fishing";
                if(name.getValue().equals("X") || name.getValue().equals("Y")){
                    if(isWild(selectedPokemon))
                        methods[3] = "Radar Chaining";
                    if(isFriendSafari(selectedPokemon))
                        methods[4] = "Friend Safari";
                }
                else{
                    if(isWild(selectedPokemon))
                        methods[3] = "DexNav";
                }
                break;
            case 7:
                if(selectedPokemon.getBreedable())
                    methods[1] = "Masuda";
                if(isSOS(selectedPokemon) && !(name.getValue().startsWith("Let")))
                    methods[2] = "SOS Chaining";
                if(name.getValue().startsWith("Ult")) {
                    if(isWormhole(selectedPokemon))
                    methods[3] = "Ultra Wormholes";
                }
                if(name.getValue().startsWith("Let")) {
                    methods[1] = null;
                    if (isWild(selectedPokemon))
                        methods[2] = "Catch Combo";
                }
                break;
            case 8:
                if(selectedPokemon.getBreedable() && !name.getValue().equals("Legends: Arceus"))
                    methods[1] = "Masuda";
                if (name.getValue().equals("Brilliant Diamond") || name.getValue().equals("Shining Pearl")) {
                    if (isWild(selectedPokemon))
                        methods[2] = "Radar Chaining";
                    methods[3] = "Underground";
                }
                if (name.getValue().equals("Sword") || name.getValue().equals("Shield")) {
                    if (isWild(selectedPokemon))
                        methods[2] = "Total Encounters";
                    if (inDynamaxAdventure(selectedPokemon))
                        methods[3] = "Dynamax Adventure";
                }else
                    methods[2] = "Mass Outbreak";
                break;
            default:
                break;
        }
        setShinyLocked();
        if(!selectedPokemon.getHuntable())
            selectedPokemon.setHuntable(legendaryIsAvaliable(selectedPokemon));
        for(String i: ShinyLocked) {
            if (i != null && i.equals(selectedPokemon.getName())) {
                selectedPokemon.setHuntable(false);
                break;
            }
        }
        if(selectedPokemon.getHuntable())
            methods[0] = "None";
    }

    //for XY, DPP, ORAS, and SWSH
    //checks to see if the pokemon is wild for shiny hunt methods that require the pokemon to be wild
    public boolean isWild(Pokemon selectedPokemon){
        String[] Platinum = {"Slowpoke", "Larvitar", "Mightyena", "Aron", "Kecleon", "Bagon", "Stantler", "Houndour"};
        String[] Diamond = {"Murkow", "Stunky", "Skuntank", "Larvitar", "Mightyena", "Aron", "Trapinch", "Vibrava", "Kecleon"};
        String[] Pearl = {"Misdreavus", "Glameow", "Purugly", "Slowpoke", "Trapinch", "Vibrava", "Bagon", "Stantler", "Houndoom"};

        String[] X = {"Clauncher", "Clawitzer", "Swirlix", "Sawk", "Aron", "Lairon", "Mightyena", "Houndour", "Pinsir"};
        String[] Y = {"Heracross", "Larvitar", "Pupitar", "Electrike", "Liepard", "Throh", "Spritzee", "Skrelp", "Dragalge"};

        String[] OmegaRuby = {"Seedot","Nuzleaf", "Mawile", "Zangoose"};
        String[] AlphaSapphire = {"Seviper", "Lunatone", "Sableye", "Lombre"};

        String[] LetsGoPikachu = {"Scyther", "Grimer", "Muk", "Mankey", "Growlithe", "Oddish", "Gloom", "Vileplume", "Sandshrew"};
        String[] LetsGoEevee = {"Ekans", "Vulpix", "Ninetales", "Meowth", "Bellsprout", "Weepinbell", "Victreebel", "Koffing", "Pinsir"};

        String[] Sword = {"Galarian Darumaka", "Galarian Darmanitan", "Stonjourner", "Jangmo-o", "Hakamo-o", "Kommo-o", "Turtonator", "Passimian", "Swirlix", "Deino", "Zweilous", "Rufflet", "Braviary", "Gothita", "Gothorita", "Scraggy", "Seedot", "Nuzleaf", "Shiftry", "Mawile", "Pinsir", "Clauncher", "Clawitzer", "Bagon", "Salamence", "Omanyte", "Omastar", "Deino", "Hydreigon"};
        String[] Shield = {"Galarian Corsola", "Eiscue", "Drampa", "Oranguru", "Goomy", "Sliggoo", "Spritzee", "Vullaby", "Mandibuzz", "Solosis", "Duosion", "Reuniclus", "Croagunk", "Toxicroak", "Solrock", "Larvitar", "Pupitar", "Lotad", "Lombre", "Ludicolo", "Heracross", "Skrelp", "Dragalge", "Galarian Rapidash", "Gible", "Garchomp", "Kabuto", "Kabutops", "Larvitar", "Tyranitar"};

        try {
            BufferedReader fileReader;
            String line;
            switch (this.generation) {
                case 4:
                    fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/DPP.txt"));
                    while ((line = fileReader.readLine()) != null)
                        if (line.equals(selectedPokemon.getName()))
                            return true;
                    switch (this.name.getValue()) {
                        case "Diamond":
                            for (String i : Diamond)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Pearl":
                            for (String i : Pearl)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Platinum":
                            for (String i : Platinum)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        default:
                            break;
                    }
                    break;
                case 6:
                    switch (this.name.getValue()) {
                        case "X":
                            fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/XY.txt"));
                            while ((line = fileReader.readLine()) != null)
                                if (line.equals(selectedPokemon.getName()))
                                    return true;
                            for (String i : X)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Y":
                            fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/XY.txt"));
                            while ((line = fileReader.readLine()) != null)
                                if (line.equals(selectedPokemon.getName()))
                                    return true;
                            for (String i : Y)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Omega Ruby":
                            fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/ORAS.txt"));
                            while ((line = fileReader.readLine()) != null)
                                if (line.equals(selectedPokemon.getName()))
                                    return true;
                            for (String i : OmegaRuby)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Alpha Sapphire":
                            fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/ORAS.txt"));
                            while ((line = fileReader.readLine()) != null)
                                if (line.equals(selectedPokemon.getName()))
                                    return true;
                            for (String i : AlphaSapphire)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        default:
                            break;
                    }
                    break;
                case 7:
                    fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/LetsGo.txt"));
                    while ((line = fileReader.readLine()) != null)
                        if (line.equals(selectedPokemon.getName()))
                            return true;
                    switch (this.name.getValue()) {
                        case "Let's Go Pikachu":
                            for (String i : LetsGoPikachu)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Let's Go Eevee":
                            for (String i : LetsGoEevee)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        default:
                            break;
                    }
                    break;
                case 8:
                    fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/SWSH.txt"));
                    while ((line = fileReader.readLine()) != null)
                        if (line.equals(selectedPokemon.getName()))
                            return true;
                    switch (this.name.getValue()) {
                        case "Sword":
                            for (String i : Sword)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        case "Shield":
                            for (String i : Shield)
                                if (i.equals(selectedPokemon.getName()))
                                    return true;
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }catch(IOException e){
            System.out.println("Information file could not be found");
        }
        return false;
    }

    //for gen 6 games
    //checks to see if the pokemon can be fished for the chain fishing method
    public boolean isFish(Pokemon selectedPokemon){
        String[] fishXY = {"Barboach", "Whiscash", "Carvanha", "Sharpedo", "Chinchou", "Lanturn", "Clamperl", "Corphish", "Crawdaunt", "Dratini", "Dragonair", "Goldeen", "Seaking", "Horsea", "Seadra", "Magikarp", "Gyarados", "Poliwag", "Poliwhirl", "Poliwrath", "Politoad", "Remoraid", "Octillery", "Alomomola", "Basculin", "Luvdisc", "Qwilfish", "Relicanth", "Corsola"};
        String[] fishX = {"Huntail", "Clauncher", "Clawitzer", "Staryu", "Starmie"};
        String[] fishY = {"Gorebyss", "Shellder", "Cloyster", "Skrelp", "Dragalge"};
        String[] fishORAS = {"Magikarp", "Tentacool", "Goldeen", "Feebas", "Wailmer", "Corphish", "Barboach", "Carvanha", "Luvdisc", "Remoraid", "Crawdaunt", "Sharpedo", "Seaking", "Staryu", "Corsola", "Gyarados", "Whiscash", "Horsea", "Seadra", "Octillery"};
        if(this.name.getValue().equals("Omega Ruby") || this.name.getValue().equals("Alpha Sapphire"))
            for(String i: fishORAS)
                if(selectedPokemon.getName().equals(i))
                    return true;
        switch(this.name.getValue()) {
            case "X":
                for(String i: fishXY)
                    if(selectedPokemon.getName().equals(i))
                        return true;
                for(String i: fishX)
                    if(selectedPokemon.getName().equals(i))
                        return true;
            case "Y":
                for(String i: fishXY)
                    if(selectedPokemon.getName().equals(i))
                        return true;
                for(String i: fishY)
                    if(selectedPokemon.getName().equals(i))
                        return true;
        }
        return false;
    }

    //for XY
    //checks to see if the pokemon is in available in the friend safari
    public boolean isFriendSafari(Pokemon selectedPokemon) {
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/DPP.txt"));
            String line;
            while ((line = fileReader.readLine()) != null)
                if (line.equals(selectedPokemon.getName()))
                    return true;
        }catch (IOException e){
            System.out.println("Fish file not found");
        }
        return false;
    }

    //for SM and USUM
    //checks to see if the pokemon can be called through SOS
    public boolean isSOS(Pokemon selectedPokemon){
        if(this.name.getValue().contains("Ultra")) {
            try{
                BufferedReader fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/UltraSOS.txt"));
                String line;
                while ((line = fileReader.readLine()) != null)
                    if (line.equals(selectedPokemon.getName()))
                        return true;
            }catch (IOException e){
                System.out.println("Ultra SOS file not found");
            }
        }
        else{
            try{
                BufferedReader fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/SOS.txt"));
                String line;
                while ((line = fileReader.readLine()) != null)
                    if (line.equals(selectedPokemon.getName()))
                        return true;
            }catch (IOException e){
                System.out.println("SOS file not found");
            }
        }
        return false;
    }

    //for USUM
    //checks to see if the pokemon is available in the deep space wormholes
    public boolean isWormhole(Pokemon selectedPokemon){
        String[] Wormhole = {"Quagsire", "Lombre", "Floatzel", "Stunfisk", "Barbaracle", "Nuzleaf", "Grumpig", "Drapion", "Audino", "Heliolisk", "Swellow", "Altaria", "Yanmega", "Sigilyph", "Swanna", "Magcargo", "Medicham", "Hippowdon", "Abomasnow", "Crustle"};
        for(String i: Wormhole)
            if(i.equals(selectedPokemon.getName()))
                return true;
        return false;
    }

    //for SWSH
    //checks to see if the pokemon is available in dynamax adventures
    public boolean inDynamaxAdventure(Pokemon selectedPokemon){
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader("Game Data/Available Pokemon/DynamaxAdventure.txt"));
            String line;
            while ((line = fileReader.readLine()) != null)
                if (line.equals(selectedPokemon.getName()))
                    return true;
        }catch (IOException e){
            System.out.println("Dynamax Adventure file not found");
        }

        return false;
    }

    //checks to see if the legendary pokemon is available in the selected Game
    public boolean legendaryIsAvaliable(Pokemon selectedPokemon){
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
                    case "Unown":
                        return true;
                    default:
                        break;
                }
                return this.name.getValue().equals("Crystal") && selectedPokemon.getName().equals("Celebi");
            case 3:
                switch (this.name.getValue()) {
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
                            case "Jirachi":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("Ruby"))
                            return selectedPokemon.getName().equals("Groudon");
                        else if (this.name.getValue().equals("Sapphire"))
                            return selectedPokemon.getName().equals("Kyogre");
                        else
                            switch (selectedPokemon.getName()) {
                                case "Lugia":
                                case "Ho-Oh":
                                case "Kyogre":
                                case "Groudon":
                                case "Mew":
                                case "Deoxys":
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
                            case "Deoxys":
                            case "Unown":
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
                switch (this.name.getValue()) {
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
                            case "Darkrai":
                            case "Shaymin":
                            case "Arceus":
                            case "Manaphy":
                            case "Unown":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("Diamond"))
                            return selectedPokemon.getName().equals("Dialga");
                        else if (this.name.getValue().equals("Pearl"))
                            return selectedPokemon.getName().equals("Palkia");
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
                            case "Unown":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("SoulSilver"))
                            return selectedPokemon.getName().equals("Groudon");
                        else if(this.name.getValue().equals("HeartGold"))
                            return selectedPokemon.getName().equals("Kyogre");
                        return false;
                    }
                }
            case 5:
                switch (this.name.getValue()) {
                    case "Black":
                    case "White": {
                        switch (selectedPokemon.getName()) {
                            case "Cobalion":
                            case "Terrakion":
                            case "Virizion":
                            case "Thundurus":
                            case "Landorus":
                            case "Kyurem":
                            case "Victini":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("White")) {
                            return selectedPokemon.getName().equals("Zekrom");
                        } else if(this.name.getValue().equals("Black"))
                            return selectedPokemon.getName().equals("Reshiram");
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
                        if (this.name.getValue().equals("White 2")) {
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
                switch (this.name.getValue()) {
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
                        if (this.name.getValue().equals("X")) {
                            return selectedPokemon.getName().equals("Xerneas");
                        } else if (this.name.getValue().equals("Y"))
                            return selectedPokemon.getName().equals("Yveltal");
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
                            case "Deoxys":
                            case "Unown":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("Omega Ruby")) {
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
                switch (this.name.getValue()) {
                    case "Sun":
                    case "Moon": {
                        switch (selectedPokemon.getName()) {
                            case "Zygarde":
                            case "Type: Null":
                            case "Silvally":
                            case "Tapu Koko":
                            case "Tapu Lele":
                            case "Tapu Bulu":
                            case "Tapu Fini":
                            case "Cosmog":
                            case "Necrozma":
                            case "Nihilego":
                            case "Xurkitree":
                            case "Guzzlord":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("Sun"))
                            switch (selectedPokemon.getName()){
                                case "Solgaleo":
                                case "Buzzwole":
                                case "Kartana":
                                    return true;
                                default:
                                    break;
                            }
                        else if (this.name.getValue().equals("Moon"))
                            switch (selectedPokemon.getName()){
                                case "Lunala":
                                case "Pheromosa":
                                case "Celesteela":
                                    return true;
                                default:
                                    break;
                            }
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
                            case "Silvally":
                            case "Tapu Koko":
                            case "Tapu Lele":
                            case "Tapu Bulu":
                            case "Tapu Fini":
                            case "Cosmog":
                            case "Necrozma":
                            case "Nihilego":
                            case "Xurkitree":
                            case "Guzzlord":
                            case "Poipole":
                            case "Naganadel":
                            case "Magearna":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("Ultra Sun")) {
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
                                case "Buzzwole":
                                case "Kartana":
                                case "Blacephalon":
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
                                case "Pheromosa":
                                case "Celesteela":
                                case "Stakataka":
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
                switch (this.name.getValue()) {
                    case "Sword":
                    case "Shield": {
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
                            case "Regirock":
                            case "Regice":
                            case "Registeel":
                            case "Latias":
                            case "Latios":
                            case "Kyogre":
                            case "Groudon":
                            case "Rayquaza":
                            case "Uxie":
                            case "Mesprit":
                            case "Azelf":
                            case "Dialga":
                            case "Palkia":
                            case "Heatran":
                            case "Regigigas":
                            case "Giratina":
                            case "Cresselia":
                            case "Cobalion":
                            case "Terrakion":
                            case "Virizion":
                            case "Tornadus":
                            case "Thundurus":
                            case "Reshiram":
                            case "Zekrom":
                            case "Landorus":
                            case "Kyurem":
                            case "Keldeo":
                            case "Xerneas":
                            case "Yveltal":
                            case "Zygarde":
                            case "Tapu Koko":
                            case "Tapu Lele":
                            case "Tapu Bulu":
                            case "Tapu Fini":
                            case "Solgaleo":
                            case "Lunala":
                            case "Nihilego":
                            case "Buzzwole":
                            case "Pheromosa":
                            case "Xurkitree":
                            case "Celesteela":
                            case "Kartana":
                            case "Guzzlord":
                            case "Necrozma":
                            case "Stakataka":
                            case "Blacephalon":
                            case "Type: Null":
                            case "Eternatus":
                            case "Kubfu":
                            case "Urshifu":
                            case "Regieleki":
                            case "Regidrago":
                            case "Galarian Articuno":
                            case "Galarian Zapdos":
                            case "Galarian Moltres":
                            case "Glastrier":
                            case "Spectrier":
                            case "Calyrex":
                                return true;
                            default:
                                break;
                        }
                        if (this.name.getValue().equals("Sword")) {
                            return selectedPokemon.name.getValue().equals("Zacian");
                        } else if(this.name.getValue().equals("Shield")){
                            return selectedPokemon.name.getValue().equals("Zamazenta");
                        }
                        return false;
                    }
                }
            default:
                break;
        }
        return false;
    }

    public StringProperty getNameProperty(){return name;}

    public String getName() {
        return name.getValue();
    }

    public int getGeneration(){
        return generation;
    }

    public String[] getMethods(){
        return methods;
    }

    public void setGeneration(int generation){
        this.generation = generation;
    }

    //adds shiny locked pokemon to ShinyLocked array based on the selected game
    public void setShinyLocked(){
        switch (generation){
            case 5:
                ShinyLocked[0] = "Reshiram";
                ShinyLocked[1] = "Zekrom";
                if(name.getValue().compareTo("Black") == 0 || name.getValue().compareTo("White") == 0)
                    ShinyLocked[2] = "Victini";
                break;
            case 6:
                if(name.getValue().compareTo("X") == 0 || name.getValue().compareTo("Y") == 0) {
                    ShinyLocked = new String[]{"Articuno", "Zapdos", "Moltres", "Mewtwo", "Xerneas", "Yveltal", "Zygarde"};
                }else{
                    ShinyLocked = new String[]{"Zyogre", "Groudon", "Rayquaza", "Deoxys"};
                }
                break;
            case 7:
                if(name.getValue().substring(0,3).compareTo("Ult") == 0) {
                    ShinyLocked = new String[]{"Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Cosmog", "Solgaleo", "Lunala", "Zygarde", "Silvally", "Naganadel"};

                }
                else if(name.getValue().substring(0,3).compareTo("Let") == 0){
                    ShinyLocked = new String[]{"Articuno", "Zapdos", "Moltres", "Mewtwo"};
                }
                else{
                    ShinyLocked = new String[]{"Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Cosmog", "Solgaleo", "Lunala", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Necrozma", "Zygarde", "Silvally", "Naganadel"};
                }
                break;
            case 8:
                if(name.getValue().substring(0,1).compareTo("S") == 0)
                    ShinyLocked = new String[]{"Type: Null", "Silvally", "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Zarude", "Cosmog", "Poipole", "Keldeo", "Galarian Articuno", "Galarian Zapdos", "Galarian Moltres", "Glastrier", "Spectrier", "Calyrex"};
                else if(name.getValue().substring(0,7).compareTo("Legends") == 0)
                    ShinyLocked = new String[]{"Cyndaquil", "Oshawott", "Rowlet", "Alolan Vulpix", "Dialga", "Palkia", "Uxie", "Mesprit", "Azelf", "Heatran", "Regigigas", "Giratina", "Cresselia", "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus", "Tornadus", "Thundurus", "Landorus", "Enamorus"};
                else
                    ShinyLocked = new String[]{"Mew", "Jirachi"};
                break;
            default:
                break;
        }
    }
}
