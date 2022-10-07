package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Vector;

public class Game {
    StringProperty name = new SimpleStringProperty();
    int generation;
    int id;
    Vector<Integer> pokedex = new Vector<>();
    Vector<Integer> unbreedables = new Vector<>();
    Vector<Integer> methods = new Vector<>();

    Game(JSONObject gameObject, int id){
        this.name.setValue((String) gameObject.get("name"));
        generation = (int) (long)  gameObject.get("generation");
        this.id = id;

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
    }

    Game(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/game.json", id)), id);
    }

    public static int findID(String name){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("GameData/game.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray gameList = (JSONArray) obj;

            //parse pokemon data
            for (int i = 0; i < gameList.size(); i++) {
                JSONObject gameObject = (JSONObject) gameList.get(i);

                if (Objects.equals(gameObject.get("name").toString(), name))
                    return i;
            }
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return -1;
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

    public String getName() { return name.getValue(); }
    public StringProperty getNameProperty(){return name;}

    public Vector<Integer> getPokedex() { return pokedex; }
    public Vector<Integer> getMethods() { return methods; }

    public int getGeneration(){
        return generation;
    }
    public int getId(){ return id; }

    public void setGeneration(int generation){
        this.generation = generation;
    }
}
