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

public class Pokemon{
    StringProperty name = new SimpleStringProperty(); //Pokemon name
    int generation; //Generation of pokemon debut
    int id; //dex number - 1
    int evoStage; //evolution stage of pokemon
    Boolean breedable; //if the pokemon can breed
    Boolean huntable = true; //if the pokemon is huntable
    Boolean legendary; //if the pokemon is legendary
    int form; //current form of the pokemon
    Vector<Vector<Integer>> family = new Vector<>(); //all pokemon in evolution line
    Vector<Integer> regionalForms = new Vector<>(); //all possible regional forms
    Vector<String> forms = new Vector<>(); //all possible forms
    int[] base; //Stores Speed, HP, Special, Attack, Defense

    /**
     * Parses pokemon.json and initializes variables with data from given index
     * @param id index of the given pokemon
     */
    Pokemon(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/pokemon.json", id)), id);
    }

    Pokemon(JSONObject pokemonObject, int id){
        name.setValue((String) pokemonObject.get("name"));
        generation = (int) (long)  pokemonObject.get("generation");
        breedable = (Boolean) pokemonObject.get("breedable");
        legendary = (Boolean) pokemonObject.get("legendary");
        this.id = id;

        JSONArray tempJSONArr = (JSONArray) pokemonObject.get("family");
        if(tempJSONArr != null) {
            family.add(new Vector<>());
            for (int i = 0; i < tempJSONArr.size(); i++)
                if (tempJSONArr.get(i) instanceof Long) {
                    family.get(0).add(Integer.parseInt(tempJSONArr.get(i).toString()));
                    if (Integer.parseInt(tempJSONArr.get(i).toString()) == this.id)
                        evoStage = i;
                } else {
                    JSONArray multipleFamilies = (JSONArray) tempJSONArr.get(i);
                    if(family.size() <= i)
                        family.add(new Vector<>());

                    for (Object j : multipleFamilies)
                        family.get(i).add(Integer.parseInt(j.toString()));
                }
        }

        tempJSONArr = (JSONArray) pokemonObject.get("forms");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                forms.add(i.toString());

        tempJSONArr = (JSONArray) pokemonObject.get("regional-forms");
        if(tempJSONArr != null)
            for(Object i : tempJSONArr)
                regionalForms.add(Integer.parseInt(i.toString()));

        JSONArray baseStats = (JSONArray) pokemonObject.get("base");
        if(baseStats != null) {
            base = new int[5];
            for (int i = 0; i < 5; i++)
                base[i] = Integer.parseInt(baseStats.get(i).toString());
        }
    }

    /**
     * Finds name with given dex number
     * @param dexNum Dex number
     * @return Pokemon name
     */
    public static String findName(int dexNum){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("GameData/pokemon.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray pokemonList = (JSONArray) obj;

            //parse pokemon data
            JSONObject pokemonObject = (JSONObject) pokemonList.get(dexNum - 1);
            return pokemonObject.get("name").toString();
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString(){ return name.getValue(); }

    public int[] getBase() { return base; }
    public int getDexNumber(){ return this.id; }
    public int getEvoStage(){ return this.evoStage; }
    public int getFormId(){ return this.form; }
    public int getGeneration(){ return generation; }

    public Vector<Vector<Integer>> getFamily(){ return family; }
    public Vector<String> getForms(){ return forms; }
    public String getForm(){ return forms.get(form);}
    public String getName(){ return name.getValue(); }
    public StringProperty getNameProperty(){ return name; }

    public Boolean getBreedable(){ return breedable; }
    public Boolean getHuntable(){ return huntable; }

    public void setForm(int form){ this.form = form; }
    public void setHuntable(Boolean huntable){ this.huntable = huntable; }
    public void setName(String name){ this.name.setValue(name); }
}
