package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Vector;

public class Game {
    StringProperty name = new SimpleStringProperty();
    int generation;
    int id;
    int odds;//Base odds for game
    String imagePath;//where the sprites for game are located
    Vector<Integer> pokedex = new Vector<>();//game specific pokedex
    Vector<Integer> unbreedables = new Vector<>();//obtainable unbreedable pokemon
    Vector<Integer> methods = new Vector<>();//available methods
    JSONArray oddModifiers = new JSONArray();//available odd modifiers

    /**
     * Saves data from jsonobject to variables
     * @param gameObject data
     * @param id id
     */
    Game(JSONObject gameObject, int id){
        this.name.setValue(gameObject.getString("name"));
        generation = gameObject.getInt("generation");
        odds = gameObject.getInt("odds");
        this.id = id;
        imagePath = gameObject.getString("imagePath");

        try {
            JSONArray tempJSONArr = gameObject.getJSONArray("pokedex");
            if (tempJSONArr != null)
                for (Object i : tempJSONArr)
                    pokedex.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){

        }

        try{
            for(Object i : gameObject.getJSONArray("unbreedables"))
                unbreedables.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){

        }

        try{
            for(Object i : gameObject.getJSONArray("method"))
                methods.add(Integer.parseInt(i.toString()));
        }catch(JSONException ignored){

        }

        try{
            oddModifiers = gameObject.getJSONArray("modifiers");
        }catch(JSONException ignored){

        }
    }

    /**
     * Calls main constructor with id and passes JSONObject from that index
     * @param id index
     */
    Game(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/game.json", id)), id);
    }

    /**
     * Checks if given un-breedable pokemon is obtainable
     * @param id ID of given pokemon
     * @return true/false of whether it is obtainable
     */
    public boolean hasUnbreedable(int id){
        for (int j : unbreedables)
            if (id == j)
                return true;

        return false;
    }

    /**
     * Returns the table for the given method
     * @param method Method
     * @return list of huntable pokemon for that method
     */
    public Vector<Integer> getMethodTable(int method){
        Vector<Integer> methodVector = new Vector<>();
        try{
            JSONObject jsonObject = SaveData.readJSON("GameData/game.json", id);
            if(jsonObject != null) {
                JSONArray methodTable = jsonObject.getJSONArray("table-" + method);
                if (methodTable != null)
                    for (Object i : methodTable)
                        methodVector.add(Integer.parseInt(i.toString()));
            }
        }catch(JSONException ignored){

        }

        return methodVector;
    }

    @Override
    public String toString(){ return name.getValue(); }

    public JSONArray getOddModifiers(){ return oddModifiers; }
    public String getImagePath() { return imagePath; }
    public String getName() { return name.getValue(); }
    public Vector<Integer> getPokedex() { return pokedex; }
    public Vector<Integer> getMethods() { return methods; }

    public int getGeneration(){ return generation; }
    public int getId(){ return id; }
    public int getOdds() { return odds; }

    public void setOdds(int odds){ this.odds = odds; }
}
