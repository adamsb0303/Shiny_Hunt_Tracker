package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class Method {
    StringProperty name = new SimpleStringProperty();
    int id;
    int base;
    int modifier;
    boolean breeding;
    String methodInfo;
    Vector<Integer> games = new Vector<>();
    Vector<Integer> pokemon = new Vector<>();

    Method(JSONObject methodObject, int id){
        name.setValue(methodObject.get("name").toString());
        breeding = (Boolean) methodObject.get("breeding");
        methodInfo = methodObject.get("method-info").toString();
        this.id = id;

        JSONArray tempJSONArr = (JSONArray) methodObject.get("games");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                games.add(Integer.parseInt(i.toString()));

        tempJSONArr = (JSONArray) methodObject.get("pokemon");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                pokemon.add(Integer.parseInt(i.toString()));
    }

    Method(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/method.json", id)), id);
    }

    //returns modifier to added to the base modifier by selected method with given encounters

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

    @Override public String toString(){ return name.getValue(); }

    public Boolean getBreeding(){ return breeding; }

    public StringProperty getNameProperty(){ return name; }
    public String getMethodInfo(){ return methodInfo; }
    public String getName(){ return name.getValue(); }

    public int getBase(){ return base; }
    public int getId(){ return id; }
    public int getModifier(){ return modifier; }

    public Vector<Integer> getPokemon(){ return pokemon; }
    public Vector<Integer> getGames(){ return games; }

    public void setModifier(int modifier){ this.modifier = modifier; }
}
