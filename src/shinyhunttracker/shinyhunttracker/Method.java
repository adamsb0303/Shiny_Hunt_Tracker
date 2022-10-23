package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Vector;

public class Method {
    StringProperty name = new SimpleStringProperty();
    int id;
    int base;//some methods have a custom base odds
    double modifier;//numerator in calculating odds
    boolean breeding;//if it's a breeding method
    boolean dynamic;
    String methodInfo;//Helpful information on method to display in help menu
    Vector<Integer> games = new Vector<>();//All games that method is available in
    Vector<String> gameMods = new Vector<>();//Currently active, game specific, odd modifiers
    Vector<Integer> comboGates = new Vector<>();//The # of encounters where the odds change
    Vector<Double> comboMods = new Vector<>();//The extra rolls when gates are passed
    Vector<Integer> pokemon = new Vector<>();//List of pokemon that can be hunted with this method
    Vector<Vector<String>> resources = new Vector<>();//List of links to useful websites

    /**
     * Saves data from jsonobject to variables
     * @param methodObject data
     * @param id id
     */
    Method(JSONObject methodObject, int id){
        name.setValue(methodObject.getString("name"));
        breeding = methodObject.getBoolean("breeding");
        dynamic = methodObject.getBoolean("dynamic");
        methodInfo = methodObject.getString("method-info");
        modifier = methodObject.getInt("modifier");
        this.id = id;

        try {
            if (methodObject.get("base") != null)
                base = Integer.parseInt(methodObject.get("base").toString());
        }catch(JSONException ignored){}

        try{
            for (Object i : methodObject.getJSONArray("combo-gate"))
                comboGates.add(Integer.parseInt(i.toString()));
            for (Object i : methodObject.getJSONArray("combo-mod"))
                comboMods.add(Double.parseDouble(i.toString()));
        }catch(JSONException ignored){}

        try {
            for (Object i : methodObject.getJSONArray("games"))
                games.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){}

        try {
            for (Object i : methodObject.getJSONArray("pokemon"))
                pokemon.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){}

        try{
            JSONArray namesArray = (JSONArray) methodObject.getJSONArray("resources").get(0);
            resources.add(new Vector<>());

            JSONArray linksArray = (JSONArray) methodObject.getJSONArray("resources").get(1);
            resources.add(new Vector<>());

            for(int i = 0; i < namesArray.length(); i++){
                resources.get(0).add(namesArray.getString(i));
                resources.get(1).add(linksArray.getString(i));
            }
        }catch(JSONException ignored){}
    }

    /**
     * Calls main constructor with id and passes JSONObject from that index
     * @param id index
     */
    Method(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/method.json", id)), id);
    }

    /**
     * Adds extra rolls according to how long the combo is
     * @param length the length of the combo
     */
    public double comboExtraRolls(int length){
        if(!dynamic)
            return 0;

        for(int i = 0; i < comboGates.size() - 1; i++)
            if(comboGates.get(i + 1) > length)
                return modifier + comboMods.get(i);

        return modifier + comboMods.lastElement();
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
    public Vector<Vector<String>> getResources(){ return resources; }

    public Boolean getBreeding(){ return breeding; }
    public int getBase(){ return base; }
    public int getId(){ return id; }
    public double getModifier(){ return modifier; }
}
