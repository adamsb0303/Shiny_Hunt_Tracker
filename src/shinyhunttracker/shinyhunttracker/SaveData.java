package shinyhunttracker;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class SaveData {
    //writes information to previous hunts file
    public static void saveHunt(HuntWindow huntData){
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

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            //Check for duplicate data
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

                for(int i = 0; i < huntList.size(); i++){
                    JSONObject checkData = (JSONObject) huntList.get(i);
                    int checkDataID = Integer.parseInt(checkData.get("huntID").toString());
                    int pokemonDataID = Integer.parseInt(pokemonData.get("huntID").toString());
                    if(checkDataID == pokemonDataID) {
                        huntList.remove(i);
                        break;
                    }
                }
            }

            huntList.add(pokemonData);

            FileWriter file = new FileWriter("SaveData/previousHunts.json");
            file.write(huntList.toJSONString());
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException f){
            try{
                FileWriter file = new FileWriter("SaveData/previousHunts.json");
                pokemonData.put("huntID", 0);
                file.write("[" + pokemonData.toJSONString() + "]");
                file.close();
            } catch (IOException g) {
                g.printStackTrace();
            }
        }
    }

    //pulls information from previous hunts file
    public static void loadHunt(int index){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            //for loading the last entry in the list
            if(index == -1)
                index = huntList.size() - 1;

            //parse hunt data
            JSONObject huntObject = (JSONObject) huntList.get(index);
            Pokemon selectedPokemon = new Pokemon(Integer.parseInt(huntObject.get("pokemon").toString()));
            Game selectedGame = new Game(Integer.parseInt(huntObject.get("game").toString()));
            Method selectedMethod = new Method(Integer.parseInt(huntObject.get("method").toString()));
            for(Object i : (JSONArray) huntObject.get("game_mods")){
                for(int j = 0; j < selectedGame.getOddModifiers().size(); j++) {
                    JSONObject checkMod = (JSONObject) selectedGame.getOddModifiers().get(j);
                    if (checkMod.get("name").toString().equals(i.toString())){
                        selectedMethod.addGameMod(i.toString(), Integer.parseInt(checkMod.get("extra-rolls").toString()));
                        break;
                    }
                }
            }
            int encounters = parseInt(huntObject.get("encounters").toString());
            int combo = parseInt(huntObject.get("combo").toString());
            int increment = parseInt(huntObject.get("increment").toString());
            int huntID = parseInt(huntObject.get("huntID").toString());
            String layout = "";

            //data-points that can be null
            Object formObject = huntObject.get("pokemon_form");
            Object layoutObject = huntObject.get("layout");
            if(formObject != null)
                selectedPokemon.setForm(Integer.parseInt(formObject.toString()));
            if(layoutObject != null)
                layout = layoutObject.toString();

            HuntController.addHunt(new HuntWindow(selectedPokemon, selectedGame, selectedMethod, layout, encounters, combo, increment, huntID));
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void removeHunt(int index){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            huntList.remove(index);

            FileWriter file = new FileWriter("SaveData/previousHunts.json");
            file.write(huntList.toJSONString());
            file.close();
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void updateHunt(int index, JSONObject data){
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            huntList.remove(index);
            huntList.add(index, data);

            FileWriter file = new FileWriter("SaveData/previousHunts.json");
            file.write(huntList.toJSONString());
            file.close();
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //writes information to caught pokemon file
    public static void pokemonCaught(HuntWindow huntData){
        JSONObject pokemonData = new JSONObject();

        pokemonData.put("pokemon", huntData.getPokemon().getDexNumber());
        pokemonData.put("form", huntData.getPokemon().getFormId());
        pokemonData.put("game", huntData.getGame().getId());
        pokemonData.put("generation", huntData.getGame().getGeneration());
        pokemonData.put("method", huntData.getMethod().getId());
        pokemonData.put("modifier", huntData.getMethod().getModifier());
        pokemonData.put("encounters", huntData.getEncounters());
        pokemonData.put("combo", huntData.getCombo());

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/caughtPokemon.json")){
            //Remove Hunt from previousHunts.json
            Object listObject = jsonParser.parse(new FileReader("SaveData/previousHunts.json"));
            JSONArray huntList = (JSONArray) listObject;

            for(int i = 0; i < huntList.size(); i++){
                JSONObject checkData = (JSONObject) huntList.get(i);
                int checkDataID = Integer.parseInt(checkData.get("huntID").toString());
                if(checkDataID == huntData.getHuntID()) {
                    huntList.remove(i);

                    FileWriter listFile = new FileWriter("SaveData/previousHunts.json");
                    listFile.write(huntList.toJSONString());
                    listFile.close();
                    break;
                }
            }

            //Read JSON file
            Object caughtObj = jsonParser.parse(reader);
            JSONArray caughtList = (JSONArray) caughtObj;

            caughtList.add(pokemonData);

            FileWriter caughtFile = new FileWriter("SaveData/caughtPokemon.json");
            caughtFile.write(caughtList.toJSONString());
            caughtFile.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException f){
            try{
                FileWriter file = new FileWriter("SaveData/caughtPokemon.json");
                file.write("[" + pokemonData.toJSONString() + "]");
                file.close();
            } catch (IOException g) {
                g.printStackTrace();
            }
        }
    }

    //saves layout
    public static void saveLayout(String layoutName, AnchorPane huntLayout, boolean currentHunt){
        JSONArray layoutData = new JSONArray();

        //Layout name
        layoutData.add(layoutName);

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

                layoutData.add(imageData);
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

                layoutData.add(textData);
            }
        }
        layoutData.add(huntLayout.getBackground().getFills().get(0).getFill().toString());

        JSONParser jsonParser = new JSONParser();

        //Changes if the file is for current hunt or previously caught window
        String filePath = "SaveData/";
        if(currentHunt)
            filePath += "huntLayouts.json";
        else
            filePath += "caughtLayouts.json";

        try (FileReader reader = new FileReader(filePath)){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray layoutList = (JSONArray) obj;

            //Check list for another layout with same name and remove it
            for(int i = 0; i < layoutList.size(); i++){
                JSONArray layoutCheck = (JSONArray) layoutList.get(i);
                if(layoutCheck.get(0).equals(layoutName)) {
                    layoutList.remove(layoutCheck);
                    break;
                }
            }

            layoutList.add(layoutData);

            //Write to file
            FileWriter file = new FileWriter(filePath);
            file.write(layoutList.toJSONString());
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException f){
            //If the file is empty, add brackets and write to file
            try{
                FileWriter file = new FileWriter(filePath);
                file.write("[" + layoutData.toJSONString() + "]");
                file.close();
            } catch (IOException g) {
                g.printStackTrace();
            }
        }
    }

    //load saved layout
    public static void loadLayout(String layoutName, AnchorPane huntLayout, boolean currentHunt){
        JSONParser jsonParser = new JSONParser();

        //Changes if the file is for current hunt or previously caught window
        String filePath = "SaveData/";
        if(currentHunt)
            filePath += "huntLayouts.json";
        else
            filePath += "caughtLayouts.json";

        try (FileReader reader = new FileReader(filePath)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray layoutList = (JSONArray) obj;

            //set layoutData to the matching save data
            JSONArray layoutData = new JSONArray();

            for(int i = layoutList.size() - 1; i >= 0; i--) {
                layoutData = (JSONArray) layoutList.get(i);
                if (layoutData.get(0).toString().equals(layoutName))
                    break;
            }

            //Loads data from file onto elements of layout
            for(int i = 1; i <= huntLayout.getChildren().size(); i++){
                if(i >= layoutData.size() - 1)
                    break;

                JSONObject elementData = (JSONObject) layoutData.get(i);
                Node element = huntLayout.getChildren().get(i - 1);
                if(element instanceof ImageView){
                    ImageView image = (ImageView) element;

                    image.setLayoutX((Double) elementData.get("X"));
                    image.setLayoutY((Double) elementData.get("Y"));
                    double imageWidth = -(Double) elementData.get("width");
                    double imageHeight = -(Double) elementData.get("height");
                    if(-image.getFitWidth() != imageWidth && -image.getFitHeight() != imageHeight && image.getImage() != null){
                        double width = -image.getFitWidth();
                        double height = -image.getFitHeight();
                        double scale;
                        if(height > width)
                            scale = imageHeight / height;
                        else
                            scale = imageWidth / width;
                        image.setScaleX(image.getScaleX() * scale);
                        image.setScaleY(image.getScaleY() * scale);
                        image.setTranslateX(-image.getImage().getWidth() / 2);
                        image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
                    }
                    image.setVisible(elementData.get("visible").toString().compareTo("true") == 0);
                }else{
                    Text text = (Text) element;

                    text.setLayoutX((Double) elementData.get("X"));
                    text.setLayoutY((Double) elementData.get("Y"));
                    text.setScaleX((Double) elementData.get("scale"));
                    text.setScaleY((Double) elementData.get("scale"));
                    text.setFont(new Font(elementData.get("font").toString(), 12));
                    text.setFill(Color.web(elementData.get("fill").toString()));
                    text.setStrokeWidth((Double) elementData.get("stroke_width"));
                    text.setStroke(Color.web(elementData.get("stroke").toString()));
                    text.setUnderline(elementData.get("underline").toString().compareTo("true") == 0);
                    text.setVisible(elementData.get("visible").toString().compareTo("true") == 0);
                }
            }
            huntLayout.setBackground(new Background(new BackgroundFill(Color.web(layoutData.get(layoutData.size() - 1).toString()), CornerRadii.EMPTY, Insets.EMPTY)));
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public static void removeLayout(String layoutName, boolean currentHunt){
        //Changes if the file is for current hunt or previously caught window
        String filePath = "SaveData/";
        if(currentHunt)
            filePath += "huntLayouts.json";
        else
            filePath += "caughtLayouts.json";

        try(FileReader reader = new FileReader(filePath)){
            JSONParser jsonParser = new JSONParser();

            JSONArray layoutList = (JSONArray) jsonParser.parse(reader);
            for(int i = 0; i < layoutList.size(); i++){
                JSONArray layoutData = (JSONArray) layoutList.get(i);
                if(!layoutData.get(0).toString().equals(layoutName))
                    continue;

                layoutList.remove(i);
                FileReader prevHuntsReader = new FileReader("SaveData/previousHunts.json");
                JSONArray prevHunts = (JSONArray) jsonParser.parse(prevHuntsReader);
                for(int j = 0; j < prevHunts.size(); j++){
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
            file.write(layoutList.toJSONString());
            file.close();
        }catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public static JSONObject readJSON(String filePath, int index){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray jsonList = (JSONArray) obj;

            //parse pokemon data
            return (JSONObject) jsonList.get(index);
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
