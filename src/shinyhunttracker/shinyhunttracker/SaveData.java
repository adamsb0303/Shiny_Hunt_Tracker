package shinyhunttracker;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class SaveData {
    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    String layout;
    int encounters, combo, increment;

    SaveData(){

    }

    SaveData(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, int encounters, int combo, int increment, String layout){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        this.encounters = encounters;
        this.combo = combo;
        this.increment = increment;
        this.layout = layout;
    }

    //writes information to previous hunts file
    public void saveHunt(int huntID){
        JSONObject pokemonData = new JSONObject();

        pokemonData.put("pokemon_id", selectedPokemon.getDexNumber());
        pokemonData.put("pokemon_form", selectedPokemon.getForm());
        pokemonData.put("game", selectedGame.getName());
        pokemonData.put("generation", selectedGame.getGeneration());
        pokemonData.put("method", selectedMethod.getName());
        pokemonData.put("modifier", selectedMethod.getModifier());
        pokemonData.put("encounters", encounters);
        pokemonData.put("combo", combo);
        pokemonData.put("increment", increment);
        pokemonData.put("layout", layout);

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            //Check for duplicate data
            if(huntID == -1)
                pokemonData.put("huntID", huntList.size());
            else {
                pokemonData.put("huntID", huntID);

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
    public static void loadHunt(int lineNumber, HuntController controller){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/previousHunts.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray huntList = (JSONArray) obj;

            //for loading the last entry in the list
            if(lineNumber == -1)
                lineNumber = huntList.size() - 1;

            //parse hunt data
            JSONObject huntObject = (JSONObject) huntList.get(lineNumber);
            int generation = parseInt(huntObject.get("generation").toString());
            Pokemon selectedPokemon = new Pokemon(Integer.parseInt(huntObject.get("pokemon_id").toString()));
            Game selectedGame = new Game(huntObject.get("game").toString());
            Method selectedMethod = new Method(huntObject.get("method").toString(), generation);
            selectedMethod.setModifier(parseInt(huntObject.get("modifier").toString()));
            int encounters = parseInt(huntObject.get("encounters").toString());
            int combo = parseInt(huntObject.get("combo").toString());
            int increment = parseInt(huntObject.get("increment").toString());
            int huntID = parseInt(huntObject.get("huntID").toString());
            String layout = "";

            //data-points that can be null
            Object formObject = huntObject.get("pokemon_form");
            Object layoutObject = huntObject.get("layout");
            if(formObject != null)
                selectedPokemon.setForm(formObject.toString());
            if(layoutObject != null)
                layout = layoutObject.toString();

            SelectionPageController family = new SelectionPageController();
            controller.addHunt(new HuntWindow(selectedPokemon, selectedGame, selectedMethod, layout, encounters, combo, increment, huntID));
        }catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //writes information to caught pokemon file
    public void pokemonCaught(int huntID){
        JSONObject pokemonData = new JSONObject();

        pokemonData.put("pokemon_id", selectedPokemon.getDexNumber());
        pokemonData.put("pokemon_form", selectedPokemon.getForm());
        pokemonData.put("game", selectedGame.getName());
        pokemonData.put("generation", selectedGame.getGeneration());
        pokemonData.put("method", selectedMethod.getName());
        pokemonData.put("modifier", selectedMethod.getModifier());
        pokemonData.put("encounters", encounters);
        pokemonData.put("combo", combo);

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("SaveData/caughtPokemon.json")){
            //Remove Hunt from previousHunts.json
            Object listObject = jsonParser.parse(new FileReader("SaveData/previousHunts.json"));
            JSONArray huntList = (JSONArray) listObject;

            for(int i = 0; i < huntList.size(); i++){
                JSONObject checkData = (JSONObject) huntList.get(i);
                int checkDataID = Integer.parseInt(checkData.get("huntID").toString());
                if(checkDataID == huntID) {
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
            for(int i = 1; i < layoutData.size() - 1; i++){
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

    //returns the given line from the previous hunts file
    public String getLinefromFile(int lineNumber, String file){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));

            for(int i = 0; i < getfileLength(file); i++) {
                String line = fileReader.readLine();
                if(i == lineNumber)
                    return line;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    //returns how many lines are in the previous hunts file
    public int getfileLength(String file){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));
            int lines = 0;
            while(reader.readLine() != null)
                lines++;
            return lines;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    //replaces the given line with the given string
    public void replaceLine(int lineNumber, String saveData, String file){
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));
            StringBuilder inputBuffer = new StringBuilder();

            for (int i = 0; i < getfileLength(file); i++) {
                String line = fileReader.readLine();
                if (i == lineNumber) {
                    line = saveData;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            FileOutputStream fileOut = new FileOutputStream("SaveData/" + file + ".txt");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String[] parseJSONArray(JSONArray json){
        if(json == null)
            return null;
        List<String> list = new ArrayList<>();
        for (Object o : json)
            list.add((String) o);
        return list.toArray(new String[0]);
    }
}
