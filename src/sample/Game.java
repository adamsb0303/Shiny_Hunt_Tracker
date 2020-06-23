package sample;

public class Game {
    public static String[] gen1 = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch’d", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew"};

    String name;
    int generation;
    Method[] Methods;
    Pokemon[] ShinyLocked;
    Game(){
        name = "";
        generation = 0;
    }

    Game(String name){
        switch(name){
            case "Red":
                this.name = name;
                generation = 1;
            case "Blue":
                this.name = name;
                generation = 1;
            case "Green":
                this.name = name;
                generation = 1;
            case "Yellow":
                this.name = name;
                generation = 1;
            case "Gold":
                this.name = name;
                generation = 2;
            case "Silver":
                this.name = name;
                generation = 2;
            case "Crystal":
                this.name = name;
                generation = 2;
            case "Ruby":
                this.name = name;
                generation = 3;
            case "Sapphire":
                this.name = name;
                generation = 3;
            case "FireRed":
                this.name = name;
                generation = 3;
            case "LeafGreen":
                this.name = name;
                generation = 3;
            case "Emerald":
                this.name = name;
                generation = 3;
            case "Diamond":
                this.name = name;
                generation = 4;
            case "Pearl":
                this.name = name;
                generation = 4;
            case "Platinum":
                this.name = name;
                generation = 4;
            case "HeartGold":
                this.name = name;
                generation = 4;
            case "SoulSilver":
                this.name = name;
                generation = 4;
            case "Black":
                this.name = name;
                generation = 5;
            case "White":
                this.name = name;
                generation = 5;
            case "Black 2":
                this.name = name;
                generation = 5;
            case "White 2":
                this.name = name;
                generation = 5;
            case "X":
                this.name = name;
                generation = 6;
            case "Y":
                this.name = name;
                generation = 6;
            case "Omega Ruby":
                this.name = name;
                generation = 6;
            case "Alpha Sapphire":
                this.name = name;
                generation = 6;
            case "Sun":
                this.name = name;
                generation = 7;
            case "Moon":
                this.name = name;
                generation = 7;
            case "Ultra Sun":
                this.name = name;
                generation = 7;
            case "Ultra Moon":
                this.name = name;
                generation = 7;
            case "Let's Go Pikachu":
                this.name = name;
                generation = 7;
            case "Let's Go Eevee":
                this.name = name;
                generation = 7;
            case "Sword":
                this.name = name;
                generation = 8;
            case "Shield":
                this.name = name;
                generation = 8;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }

    public int getGeneration(){
        return generation;
    }
}
