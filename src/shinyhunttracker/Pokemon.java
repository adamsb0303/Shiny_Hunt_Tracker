package shinyhunttracker;

public class Pokemon{
    String name;
    int generation;
    Boolean breedable;
    Boolean huntable;

    //array of all legendary pokemon
    String[] Legendaries = {"Articuno", "Zapdos", "Moltres", "Mewtwo", "Raikou", "Entei", "Suicune", "Lugia", "Ho-Oh", "Regirock", "Regice", "Registeel", "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Uxie", "Mesprit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina", "Cresselia", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram", "Zekrom", "Landorus", "Kyurem", "Xerneas", "Yveltal", "Zygarde", "Type: Null", "Silvally","Tapu Koko","Tapu Lele","Tapu Bulu","Tapu Fini","Cosmog","Cosmoem","Solgaleo","Lunala","Necrozma","Zacian","Zamazenta","Eternatus","Kubfu","Urshifu","Nihilego","Buzzwole","Pheromosa","Zurkitree","Celesteela","Kartana","Guzzlord","Poipole","Naganadel","Stakataka","Blacephalon","Mew","Celebi","Jirachi","Deoxys","Manaphy","Darkrai","Shaymin","Arceus","Victini","Keldeo","Meloetta","Genesect", "Diancie", "Hoopa", "Volcanion", "Magearna", "Marshadow", "Zeraora", "Meltan", "Melmetal", "Zarude"};

    Pokemon(){
        name = null;
        generation = 0;
        breedable = false;
        huntable = true;
    }

    //main constructor
    Pokemon(String name, int generation){
        this.name = name;
        this.generation = generation;
        breedable = isBreedable();
        isLegendary();
    }

    //checks to see if pokemon is in the undiscovered egg group, and that it doesn't have any evolutions
    public Boolean isBreedable(){
        String[] nonBreedablePokemon = {"Unown", "Dracozolt", "Arctozolt", "Dracovish", "Arctovish"};
        for(String i: Legendaries)
            if(i.compareTo(name) == 0)
                return false;
        for(String i: nonBreedablePokemon)
            if(i.compareTo(name) == 0)
                return false;
        return true;
    }

    //checks to see if the pokemon is legendary
    //if they are we have to start by assuming that it is not available
    public void isLegendary(){
        //since unown is neither breedable or able to evolve, I needed to treat it as a legendary
        //to get it to only show games that it is wild in
        if(name.compareTo("Unown") == 0) {
            huntable = false;
            return;
        }
        for(String i: Legendaries)
            if(i.compareTo(name) == 0) {
                huntable = false;
                return;
            }
        huntable = true;
    }

    //checks to see if the pokemon has a alolan variant available
    public Boolean isAlolan(){
        String[] alolanPokemon = {"Rattata", "Raticate", "Raichu", "Sandshrew", "Sandslash", "Vulpix", "Ninetales", "Diglett", "Dugtrio", "Meowth", "Persian", "Geodude", "Graveler", "Golem", "Grimer", "Muk", "Exeggutor", "Marowak"};
        for(String i: alolanPokemon)
            if(i.compareTo(this.name) == 0)
                return true;
        return false;
    }

    //checks to see if the pokemon has a galarian variant available
    public Boolean isGalarian(){
        String[] galarianPokemon = {"Meowth", "Ponyta", "Rapidash", "Slowpoke", "Farfetch'd", "Weezing", "Mr. Mime", "Corsola", "Zigzagoon", "Linoone", "Darumaka", "Darmanitan", "Yamask", "Stunfisk", "Slowpoke", "Slowbro"};
        for(String i: galarianPokemon)
            if(i.compareTo(this.name) == 0)
                return true;
        return false;
    }

    public String getName(){
        return name;
    }

    public Boolean getBreedable(){
        return breedable;
    }

    public int getGeneration(){
        return generation;
    }

    public Boolean getHuntable(){
        return huntable;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHuntable(Boolean huntable){
        this.huntable = huntable;
    }
}
