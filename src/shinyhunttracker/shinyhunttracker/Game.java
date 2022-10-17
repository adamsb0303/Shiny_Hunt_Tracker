package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;
import java.util.Vector;

public class Game {
    StringProperty name = new SimpleStringProperty();
    int generation;
    int id;
    int odds;
    String imagePath;
    Vector<Integer> pokedex = new Vector<>();
    Vector<Integer> unbreedables = new Vector<>();
    Vector<Integer> methods = new Vector<>();
    JSONArray oddModifiers = new JSONArray();

    Game(JSONObject gameObject, int id){
        this.name.setValue((String) gameObject.get("name"));
        generation = (int) (long)  gameObject.get("generation");
        odds = (int) (long) gameObject.get("odds");
        this.id = id;
        imagePath = String.valueOf(gameObject.get("imagePath"));

        JSONArray tempJSONArr = (JSONArray) gameObject.get("pokedex");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                pokedex.add(Integer.parseInt(i.toString()));

        tempJSONArr = (JSONArray) gameObject.get("unbreedables");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                unbreedables.add(Integer.parseInt(i.toString()));

        tempJSONArr = (JSONArray) gameObject.get("method");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                methods.add(Integer.parseInt(i.toString()));

        tempJSONArr = (JSONArray) gameObject.get("modifiers");
        if(tempJSONArr != null)
            oddModifiers = tempJSONArr;
    }

    Game(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/game.json", id)), id);
    }

    public boolean hasUnbreedable(int id){
        for (int j : unbreedables)
            if (id == j)
                return true;

        return false;
    }

    public Vector<Integer> getMethodTable(int method){
        Vector<Integer> methodVector = new Vector<>();
        JSONObject jsonObject = SaveData.readJSON("GameData/game.json", id);
        if(jsonObject != null) {
            JSONArray methodTable = (JSONArray) jsonObject.get("table-" + method);
            if(methodTable != null)
                for(Object i : methodTable)
                    methodVector.add(Integer.parseInt(i.toString()));
        }

        return methodVector;
    }

    @Override
    public String toString(){ return name.getValue(); }

    public JSONArray getOddModifiers(){ return oddModifiers; }

    public String getImagePath() { return imagePath; }
    public String getName() { return name.getValue(); }
    public StringProperty getNameProperty(){ return name; }

    public Vector<Integer> getPokedex() { return pokedex; }
    public Vector<Integer> getMethods() { return methods; }

    public int getGeneration(){ return generation; }
    public int getId(){ return id; }
    public int getOdds() { return odds; }

    public void setOdds(int odds){ this.odds = odds; }
}
