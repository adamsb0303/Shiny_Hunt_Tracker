package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Vector;

public class Method {
    StringProperty name = new SimpleStringProperty();
    int id;
    int base;//some methods have a custom base odds
    int modifier;//numerator in calculating odds
    boolean breeding;//if it's a breeding method
    String methodInfo;//Helpful information on method to display in help menu
    Vector<Integer> games = new Vector<>();//All games that method is available in
    Vector<String> gameMods = new Vector<>();//Currently active, game specific, odd modifiers
    Vector<Integer> pokemon = new Vector<>();//List of pokemon that can be hunted with this method

    /**
     * Saves data from jsonobject to variables
     * @param methodObject data
     * @param id id
     */
    Method(JSONObject methodObject, int id){
        name.setValue(methodObject.getString("name"));
        breeding = methodObject.getBoolean("breeding");
        methodInfo = methodObject.getString("method-info");
        modifier = methodObject.getInt("modifier");
        this.id = id;

        try {
            if (methodObject.get("base") != null)
                base = Integer.parseInt(methodObject.get("base").toString());
        }catch(JSONException ignored){

        }

        try {
            for (Object i : methodObject.getJSONArray("games"))
                games.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){

        }

        try {
            for (Object i : methodObject.getJSONArray("pokemon"))
                pokemon.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){

        }
    }

    /**
     * Calls main constructor with id and passes JSONObject from that index
     * @param id index
     */
    Method(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/method.json", id)), id);
    }

    //Still trying to figure out how I am going to move these to json. They stay for now
    public int chainFishing(int encounters){
        if(encounters >= 20)
            encounters = 20;
        return encounters * 2;
    }
    public int dexNav(int encounters, int searchLevel){
        int searchPoints = searchLevel;
        double points = 0;
        if (searchLevel < 999) {
            searchLevel++;
            if (searchPoints >= 100) {
                points += 100 * 6.0;
                searchPoints -= 100;
            }
            else
                points += searchLevel * 6.0;

            if (searchPoints >= 100) {
                points += 100 * 2.0;
                searchPoints -= 100;
            }
            else
                points += searchLevel * 2.0;

            if (searchPoints > 0) {
                points += searchPoints;
            }
        }
        else
            points = 1599;
        points = points / 100;
        double shinyOdds = points / 10000;
        double normalOdds = 1 - shinyOdds;

        if(encounters == 50)
            return (int)(1/(1 - Math.pow(normalOdds, modifier + 5)));
        else if (encounters == 100)
            return (int)(1 /(1 - Math.pow(normalOdds, modifier + 10)));
        else if((int)(1 / (1 - Math.pow(normalOdds, modifier))) < base && searchLevel != 0)
            return (int)(1 /(1 - Math.pow(normalOdds, modifier)));
        else
            return 1;
    }
    public int sosChaining(int encounters){
        if(encounters >= 255 && (name.getValue().compareTo("Sun") == 0 || name.getValue().compareTo("Moon") == 0)) {
            while(encounters >= 255){
                encounters = encounters - 255;
            }
        }
        if (encounters < 10)
            return 0;
        else if (encounters < 20)
            return 4;
        else if (encounters < 30)
            return 8;
        else
            return 12;
    }
    public int catchCombo(int encounters){
        if(encounters >= 0 && encounters <= 10)
            return 0;
        else if (encounters > 10 && encounters <= 20)
            return 3;
        else if (encounters > 20 && encounters <= 30)
            return 7;
        else
            return 11;
    }
    public int totalEncounters(int previousEncounters){
        if(previousEncounters < 50)
            return 0;
        else if(previousEncounters < 100)
            return 1;
        else if (previousEncounters < 200)
            return 2;
        else if (previousEncounters < 300)
            return 3;
        else if (previousEncounters < 500)
            return 4;
        else
            return 5;
    }

    /**
     * Adds game specific odd modifier
     * @param name name of modifier
     * @param modifier how many extra rolls that are added
     */
    public void addGameMod(String name, int modifier){
        gameMods.add(name);
        this.modifier += modifier;
    }

    /**
     * Removes game specific odd modifier
     * @param name name of modifier
     * @param modifier how many extra rolls that are removed
     */
    public void removeGameMod(String name, int modifier){
        gameMods.remove(name);
        this.modifier -= modifier;
    }

    @Override public String toString(){ return name.getValue(); }

    public String getMethodInfo(){ return methodInfo; }
    public String getName(){ return name.getValue(); }
    public Vector<Integer> getPokemon(){ return pokemon; }
    public Vector<Integer> getGames(){ return games; }
    public Vector<String> getGameMods(){ return gameMods; }

    public Boolean getBreeding(){ return breeding; }
    public int getBase(){ return base; }
    public int getId(){ return id; }
    public int getModifier(){ return modifier; }
}
