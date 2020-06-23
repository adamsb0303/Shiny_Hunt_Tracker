package shinyhunttracker;

public class Game {
    String name;
    int generation;
    Method[] Methods;
    String[] ShinyLocked;

    Game(){
        name = "";
        generation = 0;
    }

    Game(String name, int generation, Pokemon selectedPokemon){
        this.name = name;
        this.generation = generation;
        generateMethods(name, generation, selectedPokemon);
    }

    public void generateMethods(String name, int generation, Pokemon selectedPokemon){
        switch(generation){
            case 2:
                if(selectedPokemon.breedable)
                    Methods[1] = new Method("Breeding with Shiny", 2);
            case 4:
                if (selectedPokemon.breedable)
                    Methods[1] = new Method("Masuda", 4);
                if (name.compareTo("Diamond") == 0 || name.compareTo("Pearl") == 0 || name.compareTo("Platinum") == 0)
                    if (isWild(selectedPokemon))
                        Methods[2] = new Method("Radar Chaining", 4);
            case 5:
                if(selectedPokemon.breedable)
                    Methods[1] = new Method("Masuda", 5);
            case 6:
                if(selectedPokemon.breedable)
                    Methods[1] = new Method("Masuda", 6);
                if(isFish(selectedPokemon))
                    Methods[2] = new Method("Chain Fishing", 6);
                if(name.compareTo("X") == 0 || name.compareTo("Y") == 0){
                    if(isWild(selectedPokemon))
                        Methods[3] = new Method("Radar Chaining", 6);
                    if(isFriendSafari(selectedPokemon))
                        Methods[4] = new Method("Friend Safari", 6);
                }
                else{
                    Methods[3] = new Method("DexNav", 6);
                }
            case 7:
                if(selectedPokemon.breedable)
                    Methods[1] = new Method("Masuda", 7);
                if(isSOS(selectedPokemon))
                    Methods[2] = new Method("SOS Chaining", 7);
                if(name.substring(0,5).compareTo("Ultra") == 0) {
                    if(isWormhole(selectedPokemon))
                    Methods[3] = new Method("Ultra Wormholes", 7);
                }
            case 8:
                if(selectedPokemon.breedable)
                    Methods[1] = new Method("Masuda", 8);
                if(isWild(selectedPokemon))
                    Methods[2] = new Method("Total Encounters", 8);
            default:
                break;
        }
        selectedPokemon.isLegendary();
        if(!selectedPokemon.getHuntable())
            legendaryIsAvaliable(selectedPokemon);
        setShinyLocked();
        for(String i: ShinyLocked)
            if(i.compareTo(selectedPokemon.name) == 0)
                selectedPokemon.setHuntable(false);
        if(!selectedPokemon.huntable)
            Methods[0] = new Method("None", generation);
    }

    public boolean isWild(Pokemon selectedPokemon){

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

    }

    public boolean isSOS(Pokemon selectedPokemon){

    }

    public boolean isWormhole(Pokemon selectedPokemon){

    }

    public void setShinyLocked(){
        switch (generation){
            case 5:
                ShinyLocked[0] = "Reshiram";
                ShinyLocked[1] = "Zekrom";
                if(name.compareTo("Black") == 0 || name.compareTo("White") == 0)
                    ShinyLocked[2] = "Victini";
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
            case 7:
                if(name.substring(0,5).compareTo("Ultra") == 0) {
                    ShinyLocked[0] = "Tapu Koko";
                    ShinyLocked[1] = "Tapu Lele";
                    ShinyLocked[2] = "Tapu Bulu";
                    ShinyLocked[3] = "Tapu Fini";
                    ShinyLocked[4] = "Cosmog";
                    ShinyLocked[5] = "Solgaleo";
                    ShinyLocked[6] = "Lunala";
                    ShinyLocked[7] = "Zygarde";
                }else {
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
            case 8:
                ShinyLocked[0] = "Type: Null";
                ShinyLocked[1] = "Zacian";
                ShinyLocked[2] = "Zamazenta";
                ShinyLocked[3] = "Eternatus";
                ShinyLocked[4] = "Kubfu";
            default:
                break;
        }
    }

    public void legendaryIsAvaliable(Pokemon selectedPokemon){
        switch(this.generation) {
            case 2:
                switch (selectedPokemon.getName()) {
                    case "Raikou":
                        selectedPokemon.setHuntable(true);
                    case "Entei":
                        selectedPokemon.setHuntable(true);
                    case "Suicune":
                        selectedPokemon.setHuntable(true);
                    case "Lugia":
                        selectedPokemon.setHuntable(true);
                    case "Ho-Oh":
                        selectedPokemon.setHuntable(true);
                    default:
                        break;
                }
            case 3:
                switch (this.name) {
                    case "Ruby":
                        switch(selectedPokemon.name) {
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Groudon":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Sapphire":
                        switch(selectedPokemon.name) {
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Kyogre":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "LeafGreen":
                        switch(selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "FireRed":
                        switch(selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Emerald":
                        switch(selectedPokemon.name) {
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Kyogre":
                                selectedPokemon.setHuntable(true);
                            case "Groudon":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }

                }
            case 4:
                switch (this.name) {
                    case "Diamond":
                        switch (selectedPokemon.name) {
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Dialga":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Pearl":
                        switch (selectedPokemon.name) {
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Palkia":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Platinum":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Dialga":
                                selectedPokemon.setHuntable(true);
                            case "Palkia":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "SoulSilver":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Groudon":
                                selectedPokemon.setHuntable(true);
                            case "Dialga":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "HeartGold":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Kyogre":
                                selectedPokemon.setHuntable(true);
                            case "Dialga":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                }
            case 5:
                switch (this.name) {
                    case "Black":
                        switch (selectedPokemon.name) {
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Thundurus":
                                selectedPokemon.setHuntable(true);
                            case "Zekrom":
                                selectedPokemon.setHuntable(true);
                            case "Landorus":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "White":
                        switch (selectedPokemon.name) {
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Thundurus":
                                selectedPokemon.setHuntable(true);
                            case "Reshiram":
                                selectedPokemon.setHuntable(true);
                            case "Landorus":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Black 2":
                        switch (selectedPokemon.name) {
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Zekrom":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "White 2":
                        switch (selectedPokemon.name) {
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Reshiram":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                }
            case 6:
                switch (this.name) {
                    case "X":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Xerneas":
                                selectedPokemon.setHuntable(true);
                            case "Zygarde":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Y":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Yveltal":
                                selectedPokemon.setHuntable(true);
                            case "Zygarde":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Omega Ruby":
                        switch (selectedPokemon.name) {
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Groudon":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Palkia":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Tornadus":
                                selectedPokemon.setHuntable(true);
                            case "Reshiram":
                                selectedPokemon.setHuntable(true);
                            case "Landorus":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Alpha Sapphire":
                        switch (selectedPokemon.name) {
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Kyogre":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Dialga":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Thundurus":
                                selectedPokemon.setHuntable(true);
                            case "Zekrom":
                                selectedPokemon.setHuntable(true);
                            case "Landorus":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                }
            case 7:
                switch (this.name) {
                    case "Sun":
                        switch (selectedPokemon.name) {
                            case "Zygarde":
                                selectedPokemon.setHuntable(true);
                            case "Type: Null":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Koko":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Lele":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Bulu":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Fini":
                                selectedPokemon.setHuntable(true);
                            case "Cosmog":
                                selectedPokemon.setHuntable(true);
                            case "Lunala":
                                selectedPokemon.setHuntable(true);
                            case "Necrozma":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Moon":
                        switch (selectedPokemon.name) {
                            case "Zygarde":
                                selectedPokemon.setHuntable(true);
                            case "Type: Null":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Koko":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Lele":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Bulu":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Fini":
                                selectedPokemon.setHuntable(true);
                            case "Cosmog":
                                selectedPokemon.setHuntable(true);
                            case "Solgaleo":
                                selectedPokemon.setHuntable(true);
                            case "Necrozma":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Ultra Sun":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Raikou":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Ho-Oh":
                                selectedPokemon.setHuntable(true);
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latios":
                                selectedPokemon.setHuntable(true);
                            case "Groudon":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Dialga":
                                selectedPokemon.setHuntable(true);
                            case "Heatran":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Tornadus":
                                selectedPokemon.setHuntable(true);
                            case "Reshiram":
                                selectedPokemon.setHuntable(true);
                            case "Landorus":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            case "Xerneas":
                                selectedPokemon.setHuntable(true);
                            case "Zygarde":
                                selectedPokemon.setHuntable(true);
                            case "Type: Null":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Koko":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Lele":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Bulu":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Fini":
                                selectedPokemon.setHuntable(true);
                            case "Cosmog":
                                selectedPokemon.setHuntable(true);
                            case "Solgaleo":
                                selectedPokemon.setHuntable(true);
                            case "Necrozma":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Ultra Moon":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            case "Entei":
                                selectedPokemon.setHuntable(true);
                            case "Suicune":
                                selectedPokemon.setHuntable(true);
                            case "Lugia":
                                selectedPokemon.setHuntable(true);
                            case "Regirock":
                                selectedPokemon.setHuntable(true);
                            case "Regice":
                                selectedPokemon.setHuntable(true);
                            case "Registeel":
                                selectedPokemon.setHuntable(true);
                            case "Latias":
                                selectedPokemon.setHuntable(true);
                            case "Kyogre":
                                selectedPokemon.setHuntable(true);
                            case "Rayquaza":
                                selectedPokemon.setHuntable(true);
                            case "Uxie":
                                selectedPokemon.setHuntable(true);
                            case "Mesprit":
                                selectedPokemon.setHuntable(true);
                            case "Azelf":
                                selectedPokemon.setHuntable(true);
                            case "Palkia":
                                selectedPokemon.setHuntable(true);
                            case "Regigigas":
                                selectedPokemon.setHuntable(true);
                            case "Giratina":
                                selectedPokemon.setHuntable(true);
                            case "Cresselia":
                                selectedPokemon.setHuntable(true);
                            case "Cobalion":
                                selectedPokemon.setHuntable(true);
                            case "Terrakion":
                                selectedPokemon.setHuntable(true);
                            case "Virizion":
                                selectedPokemon.setHuntable(true);
                            case "Thundurus":
                                selectedPokemon.setHuntable(true);
                            case "Zekrom":
                                selectedPokemon.setHuntable(true);
                            case "Landorus":
                                selectedPokemon.setHuntable(true);
                            case "Kyurem":
                                selectedPokemon.setHuntable(true);
                            case "Yveltal":
                                selectedPokemon.setHuntable(true);
                            case "Zygarde":
                                selectedPokemon.setHuntable(true);
                            case "Type: Null":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Koko":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Lele":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Bulu":
                                selectedPokemon.setHuntable(true);
                            case "Tapu Fini":
                                selectedPokemon.setHuntable(true);
                            case "Cosmog":
                                selectedPokemon.setHuntable(true);
                            case "Lunala":
                                selectedPokemon.setHuntable(true);
                            case "Necrozma":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Let's Go Pikachu":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Let's Go Eevee":
                        switch (selectedPokemon.name) {
                            case "Articuno":
                                selectedPokemon.setHuntable(true);
                            case "Zapdos":
                                selectedPokemon.setHuntable(true);
                            case "Moltres":
                                selectedPokemon.setHuntable(true);
                            case "Mewtwo":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                }
            case 8:
                switch (this.name) {
                    case "Sword":
                        switch (selectedPokemon.name) {
                            case "Type: Null":
                                selectedPokemon.setHuntable(true);
                            case "Zacian":
                                selectedPokemon.setHuntable(true);
                            case "Eternatus":
                                selectedPokemon.setHuntable(true);
                            case "Kubfu":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                    case "Shield":
                        switch (selectedPokemon.name) {
                            case "Type: Null":
                                selectedPokemon.setHuntable(true);
                            case "Zamazenta":
                                selectedPokemon.setHuntable(true);
                            case "Eternatus":
                                selectedPokemon.setHuntable(true);
                            case "Kubfu":
                                selectedPokemon.setHuntable(true);
                            default:
                                break;
                        }
                }
            default:
        }
    }

    public String getName() {
        return name;
    }

    public int getGeneration(){
        return generation;
    }
}
