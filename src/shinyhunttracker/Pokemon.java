package shinyhunttracker;

public class Pokemon {
    String name;
    int generation;
    Boolean breedable;
    Boolean huntable;

    Pokemon(){
        name = "";
        generation = 0;
        breedable = false;
        huntable = true;
    }

    Pokemon(String name, int generation){
        this.name = name;
        this.generation = generation;
        breedable = isBreedable();
        huntable = true;
    }

    public Boolean getBreedable(){
        return breedable;
    }

    public Boolean isBreedable(){
        return true;
    }

    public void isLegendary(){
        String[] Legendaries = {"Articuno", "Zapdos", "Moltres", "Mewtwo", "Raikou", "Entei", "Suicune", "Lugia", "Ho-Oh", "Regirock", "Regice", "Registeel", "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Uxie", "Mespirit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina", "Cresselia", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram", "Zekrom", "Landorus", "Kyurem", "Xerneas", "Yveltal", "Zygarde", "Type: Null", "Silvally","Tapu Koko","Tapu Lele","Tapu Bulu","Tapu Fini","Cosmog","Cosmoem","Solgaleo","Lunala","Necrozma","Zacian","Zamazenta","Eternatus","Kubfu","Urshifu","Nihilego","Buzzwole","Pheromosa","Zurkitree","Celesteela","Kartana","Guzzlord","Poipole","Naganadel","Stakataka","Blacephalon","Mew","Celebi","Jirachi","Deoxys","Manaphy","Darkrai","Shaymin","Arceus","Victini","Keldeo","Meloetta","Genesect","Diancie","Hoppa","Volcanion","Magearna","Marshadow","Zeraora","Meltan","Melmetal", "Zarude"};
        for(String i: Legendaries)
            if(i.compareTo(name) == 0)
                huntable = false;
        huntable = true;
    }

    public String getName(){
        return name;
    }

    public int getGeneration(){
        return generation;
    }

    public Boolean getHuntable(){
        return huntable;
    }

    public void setBreedable(Boolean breedable){
        this.breedable = breedable;
    }

    public void setHuntable(Boolean huntable){
        this.huntable = huntable;
    }
}
