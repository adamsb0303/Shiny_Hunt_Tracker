package shinyhunttracker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Vector;

public class Pokemon{
    StringProperty name = new SimpleStringProperty(); //Pokemon name
    int generation; //Generation of pokemon debut
    int id; //dex number - 1
    int evoStage; //evolution stage of pokemon
    Boolean breedable; //if the pokemon can breed
    int form; //current form of the pokemon
    Vector<Vector<Integer>> family = new Vector<>(); //all pokemon in evolution line
    Vector<Integer> regionalForms = new Vector<>(); //all possible regional forms
    Vector<String> forms = new Vector<>(); //all possible forms
    int[] base; //Stores Speed, HP, Special, Attack, Defense

    /**
     * Loads data from JSONObject
     * @param pokemonObject data
     * @param id index
     */
    Pokemon(JSONObject pokemonObject, int id){
        name.setValue(pokemonObject.getString("name"));
        generation = pokemonObject.getInt("generation");
        breedable = pokemonObject.getBoolean("breedable");
        this.id = id;

        //Families are 1D unless they branch, then they are 2D
        try {
            JSONArray tempJSONArr = pokemonObject.getJSONArray("family");
            family.add(new Vector<>());
            for (int i = 0; i < tempJSONArr.length(); i++) {
                if (tempJSONArr.get(i) instanceof Integer) {
                    family.get(0).add(tempJSONArr.getInt(i));
                    if (tempJSONArr.getInt(i) == this.id)
                        evoStage = i;
                } else {
                    JSONArray multipleFamilies = tempJSONArr.getJSONArray(i);
                    if (family.size() <= i)
                        family.add(new Vector<>());

                    for (Object j : multipleFamilies)
                        family.get(i).add(Integer.parseInt(j.toString()));
                }
            }
        }catch(JSONException ignored){

        }

        try{
            for(Object i : pokemonObject.getJSONArray("forms"))
                forms.add(i.toString());
        }catch (JSONException ignored){

        }

        try {
            for (Object i : pokemonObject.getJSONArray("regional-forms"))
                regionalForms.add(Integer.parseInt(i.toString()));
        } catch(JSONException ignored){

        }

        try {
            JSONArray baseStats = pokemonObject.getJSONArray("base");
            if (baseStats != null) {
                base = new int[5];
                for (int i = 0; i < 5; i++)
                    base[i] = Integer.parseInt(baseStats.get(i).toString());
            }
        }catch(JSONException ignored){

        }
    }

    /**
     * calls the main constructor with id and passes JSONObject from that index
     * @param id index
     */
    Pokemon(int id){
        this(Objects.requireNonNull(SaveData.readJSON("GameData/pokemon.json", id)), id);
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

    public Boolean getBreedable(){ return breedable; }

    public void setForm(int form){ this.form = form; }
    public void setName(String name){ this.name.setValue(name); }
}
