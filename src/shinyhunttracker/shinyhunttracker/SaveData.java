package shinyhunttracker;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

public class SaveData {
    /**
     * Saves data from hunt window to previous hunts json
     * @param huntData hunt window
     */
    public static void saveHunt(HuntWindow huntData){
        //Creates new pokemon data
        JSONObject pokemonData = new JSONObject();
        pokemonData.put("pokemon", huntData.getPokemon().getDexNumber());
        pokemonData.put("pokemon_form", huntData.getPokemon().getFormId());
        pokemonData.put("game", huntData.getGame().getId());
        pokemonData.put("method", huntData.getMethod().getId());
        pokemonData.put("game_mods", huntData.getMethod().getGameMods());
        pokemonData.put("encounters", huntData.getEncounters());
        pokemonData.put("combo", huntData.getCombo());
        pokemonData.put("increment", huntData.getIncrement());
        pokemonData.put("layout", huntData.getCurrentLayout());

        try {
            //Read JSON file
            JSONArray huntList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/previousHunts.json")));

            //set the hunt ID of the new data, -1 being a new pokemon
            int largest = 0;
            if(huntData.getHuntID() == -1) {
                for(Object i : huntList){
                    JSONObject data = (JSONObject) i;
                    if(Integer.parseInt(data.get("huntID").toString()) > largest)
                        largest = Integer.parseInt(data.get("huntID").toString());
                }
                pokemonData.put("huntID", largest + 1);
            }
            else {
                pokemonData.put("huntID", huntData.getHuntID());

                //removes duplicate data
                for(int i = 0; i < huntList.length(); i++){
                    JSONObject checkData = (JSONObject) huntList.get(i);
                    int checkDataID = checkData.getInt("huntID");
                    int pokemonDataID = pokemonData.getInt("huntID");
                    if(checkDataID == pokemonDataID) {
                        huntList.remove(i);
                        break;
                    }
                }
            }

            //Writes new data to file
            huntList.put(pokemonData);

            FileWriter file = new FileWriter("SaveData/previousHunts.json");
            file.write(huntList.toString());
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads hunt data from json and creates a new hunt
     * @param index index of hunt data
     */
    public static void loadHunt(int index){
        try {
            //Read JSON file
            JSONArray huntList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/previousHunts.json")));

            //for loading the last entry in the list
            if(index == -1)
                index = huntList.length() - 1;

            //parse hunt data
            JSONObject huntObject = (JSONObject) huntList.get(index);
            Pokemon selectedPokemon = new Pokemon(huntObject.getInt("pokemon"));
            Game selectedGame = new Game(huntObject.getInt("game"));
            Method selectedMethod = new Method(huntObject.getInt("method"));
            for(Object i : (JSONArray) huntObject.get("game_mods")){
                for(int j = 0; j < selectedGame.getOddModifiers().length(); j++) {
                    JSONObject checkMod = (JSONObject) selectedGame.getOddModifiers().get(j);
                    if (checkMod.getString("name").equals(i.toString())){
                        selectedMethod.addGameMod(i.toString(), Integer.parseInt(checkMod.get("extra-rolls").toString()));
                        break;
                    }
                }
            }
            int encounters = huntObject.getInt("encounters");
            int combo = huntObject.getInt("combo");
            int increment = huntObject.getInt("increment");
            int huntID = huntObject.getInt("huntID");
            String layout = "";

            //data-points that can be null
            Object formObject = huntObject.get("pokemon_form");
            Object layoutObject = huntObject.get("layout");
            if(formObject != null)
                selectedPokemon.setForm(Integer.parseInt(formObject.toString()));
            if(layoutObject != null)
                layout = layoutObject.toString();

            HuntController.addHunt(new HuntWindow(selectedPokemon, selectedGame, selectedMethod, layout, encounters, combo, increment, huntID));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes hunt data from json
     * @param index index of hunt data
     */
    public static void removeHunt(int index){
        try {
            //Read JSON file
            JSONArray huntList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/previousHunts.json")));

            //removes data point at index
            huntList.remove(index);

            FileWriter file = new FileWriter("SaveData/previousHunts.json");
            file.write(huntList.toString());
            file.close();
        }catch (IOException  e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates hunt data without changing the order of json
     * @param index index of hunt data
     * @param data new hunt data
     */
    public static void updateHunt(int index, JSONObject data){
        try {
            //Read JSON file
            JSONArray huntList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/previousHunts.json")));

            //removes the data at index, and replaces it with new data
            huntList.remove(index);
            huntList.put(index, data);

            FileWriter file = new FileWriter("SaveData/previousHunts.json");
            file.write(huntList.toString());
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes hunt data from previous hunts json and creates new entry in caught pokemon json
     * @param huntData hunt data
     */
    public static void pokemonCaught(HuntWindow huntData){
        //Creates the new pokemon data
        JSONObject pokemonData = new JSONObject();
        pokemonData.put("pokemon", huntData.getPokemon().getDexNumber());
        pokemonData.put("form", huntData.getPokemon().getFormId());
        pokemonData.put("game", huntData.getGame().getId());
        pokemonData.put("generation", huntData.getGame().getGeneration());
        pokemonData.put("method", huntData.getMethod().getId());
        pokemonData.put("modifier", huntData.getMethod().getModifier());
        pokemonData.put("encounters", huntData.getEncounters());
        pokemonData.put("combo", huntData.getCombo());

        try {
            //Read previous hunts JSON
            JSONArray huntList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/previousHunts.json")));

            //Finds the pokemon from the list and removes it
            for(int i = 0; i < huntList.length(); i++){
                JSONObject checkData = (JSONObject) huntList.get(i);
                int checkDataID = checkData.getInt("huntID");
                if(checkDataID == huntData.getHuntID()) {
                    huntList.remove(i);

                    FileWriter listFile = new FileWriter("SaveData/previousHunts.json");
                    listFile.write(huntList.toString());
                    listFile.close();
                    break;
                }
            }

            //reads caught pokemon json
            JSONArray caughtList = new JSONArray(new JSONTokener(new FileInputStream("SaveData/caughtPokemon.json")));

            //adds data to caught pokemon list
            caughtList.put(pokemonData);

            FileWriter caughtFile = new FileWriter("SaveData/caughtPokemon.json");
            caughtFile.write(caughtList.toString());
            caughtFile.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves or updates layout information
     * @param layoutName name of layout
     * @param huntLayout all elements to save data from
     * @param currentHunt flag for if it's for HuntWindow or PreviouslyCaught
     */
    public static void saveLayout(String layoutName, AnchorPane huntLayout, boolean currentHunt){
        //saves layout name in index 0
        JSONArray layoutData = new JSONArray();
        layoutData.put(layoutName);

        //Saves every element from layout to json
        for(Node i : huntLayout.getChildren()) {
            if(i instanceof ImageView) {
                ImageView image = (ImageView) i;
                JSONObject imageData = new JSONObject();
                imageData.put("X", image.getLayoutX());
                imageData.put("Y", image.getLayoutY());
                imageData.put("width", image.getFitWidth());
                imageData.put("height", image.getFitHeight());
                imageData.put("visible", image.isVisible());

                layoutData.put(imageData);
            }else if(i instanceof Text){
                Text text = (Text) i;
                JSONObject textData = new JSONObject();
                textData.put("X", text.getLayoutX());
                textData.put("Y", text.getLayoutY());
                textData.put("scale", text.getScaleX());
                textData.put("font", text.getFont().getName());
                textData.put("fill", text.getFill().toString());
                textData.put("stroke_width", text.getStrokeWidth());
                textData.put("stroke", text.getStroke().toString());
                textData.put("underline", text.isUnderline());
                textData.put("visible", text.isVisible());

                layoutData.put(textData);
            }
        }
        layoutData.put(huntLayout.getBackground().getFills().get(0).getFill().toString());

        //Changes if the file is for current hunt or previously caught window
        String filePath = "SaveData/";
        if(currentHunt)
            filePath += "huntLayouts.json";
        else
            filePath += "caughtLayouts.json";

        try {
            //Read JSON file
            JSONArray layoutList = new JSONArray(new JSONTokener(new FileInputStream(filePath)));

            //Check list for another layout with same name and update it
            boolean newData = true;
            for(int i = 0; i < layoutList.length(); i++){
                JSONArray layoutCheck = (JSONArray) layoutList.get(i);
                if(layoutCheck.get(0).equals(layoutName)) {
                    layoutList.remove(i);
                    layoutList.put(i, layoutData);
                    newData = false;
                    break;
                }
            }

            //add new data if new
            if(newData)
                layoutList.put(layoutData);

            //Write to file
            FileWriter file = new FileWriter(filePath);
            file.write(layoutList.toString());
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads data from layout json to elements of the window
     * @param layoutName name of layout
     * @param huntLayout all elements to load data to
     * @param currentHunt flag for if it's for HuntWindow or PreviouslyCaught
     */
    public static void loadLayout(String layoutName, AnchorPane huntLayout, boolean currentHunt){
        //Changes if the file is for current hunt or previously caught window
        String filePath = "SaveData/";
        if(currentHunt)
            filePath += "huntLayouts.json";
        else
            filePath += "caughtLayouts.json";

        try {
            //Read JSON file
            JSONArray layoutList = new JSONArray(new JSONTokener(new FileInputStream(filePath)));

            //set layoutData to the matching save data
            JSONArray layoutData = new JSONArray();

            for(int i = layoutList.length() - 1; i >= 0; i--) {
                layoutData = (JSONArray) layoutList.get(i);
                if (layoutData.get(0).toString().equals(layoutName))
                    break;
            }

            //Loads data from file onto elements of layout
            for(int i = 1; i <= huntLayout.getChildren().size(); i++){
                if(i >= layoutData.length() - 1)
                    break;

                JSONObject elementData = (JSONObject) layoutData.get(i);
                Node element = huntLayout.getChildren().get(i - 1);
                if(element instanceof ImageView){
                    ImageView image = (ImageView) element;

                    image.setLayoutX(elementData.getDouble("X"));
                    image.setLayoutY(elementData.getDouble("Y"));
                    image.setFitWidth(elementData.getDouble("width"));
                    image.setFitHeight(elementData.getDouble("height"));
                    if(image.getImage() != null)
                        FetchImage.adjustImageScale(image, image.getImage());
                    image.setVisible(elementData.getBoolean("visible"));
                }else{
                    Text text = (Text) element;

                    text.setLayoutX(elementData.getDouble("X"));
                    text.setLayoutY(elementData.getDouble("Y"));
                    text.setScaleX(elementData.getDouble("scale"));
                    text.setScaleY(elementData.getDouble("scale"));
                    text.setFont(new Font(elementData.getString("font"), 12));
                    text.setFill(Color.web(elementData.getString("fill")));
                    text.setStrokeWidth(elementData.getDouble("stroke_width"));
                    text.setStroke(Color.web(elementData.getString("stroke")));
                    text.setUnderline(elementData.getBoolean("underline"));
                    text.setVisible(elementData.getBoolean("visible"));
                }
            }
            huntLayout.setBackground(new Background(new BackgroundFill(Color.web(layoutData.get(layoutData.length() - 1).toString()), CornerRadii.EMPTY, Insets.EMPTY)));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Deletes layout data from json with the given name
     * @param layoutName name of layout
     * @param currentHunt flag for if it's for HuntWindow or PreviouslyCaught
     */
    public static void removeLayout(String layoutName, boolean currentHunt){
        //Changes if the file is for current hunt or previously caught window
        String filePath = "SaveData/";
        if(currentHunt)
            filePath += "huntLayouts.json";
        else
            filePath += "caughtLayouts.json";

        try {
            JSONArray layoutList = new JSONArray(new JSONTokener(new FileInputStream(filePath)));

            //Finds the layout and removes it
            for(int i = 0; i < layoutList.length(); i++){
                JSONArray layoutData = (JSONArray) layoutList.get(i);
                if(!layoutData.get(0).toString().equals(layoutName))
                    continue;

                layoutList.remove(i);

                //Removes layout from any saved hunts
                JSONArray prevHunts = new JSONArray(new JSONTokener(new FileInputStream("SaveData/previousHunts.json")));
                for(int j = 0; j < prevHunts.length(); j++){
                    JSONObject huntData = (JSONObject) prevHunts.get(j);
                    if(huntData.get("layout").toString().equals(layoutName)){
                        huntData.put("layout", "");
                        updateHunt(j, huntData);
                    }
                }
                break;
            }

            //Write to file
            FileWriter file = new FileWriter(filePath);
            file.write(layoutList.toString());
            file.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads data from json
     * @param filePath file path of json file
     * @param index index of data
     * @return JSONObject from given index
     */
    public static JSONObject readJSON(String filePath, int index){
        try {
            //Read JSON file
            JSONArray jsonList = new JSONArray(new JSONTokener(new FileInputStream(filePath)));

            //return json data at index
            return (JSONObject) jsonList.get(index);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
