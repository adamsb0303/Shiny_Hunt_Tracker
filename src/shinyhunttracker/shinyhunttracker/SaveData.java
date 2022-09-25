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
    int encounters, combo, increment, huntID;

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
    public void loadHunt(int lineNumber, HuntController controller){
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
            selectedPokemon = new Pokemon(Integer.parseInt(huntObject.get("pokemon_id").toString()));
            selectedGame = new Game(huntObject.get("game").toString());
            selectedMethod = new Method(huntObject.get("method").toString(), generation);
            selectedMethod.setModifier(parseInt(huntObject.get("modifier").toString()));
            encounters = parseInt(huntObject.get("encounters").toString());
            combo = parseInt(huntObject.get("combo").toString());
            increment = parseInt(huntObject.get("increment").toString());
            huntID = parseInt(huntObject.get("huntID").toString());

            //data-points that can be null
            Object formObject = huntObject.get("pokemon_form");
            Object layoutObject = huntObject.get("layout");
            if(formObject != null)
                selectedPokemon.setForm(formObject.toString());
            if(layoutObject != null)
                layout = layoutObject.toString();

            beginHunt(controller);
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

    //opens huntControls window
    public void beginHunt(HuntController controller){
        SelectionPageController family = new SelectionPageController();

        controller.addHunt(selectedPokemon, selectedGame, selectedMethod, selectedPokemon.getFamily()[0], selectedPokemon.getFamily()[1], layout, encounters, combo, 1, huntID);
    }

    //saves layout
    public void saveLayout(String layoutName, AnchorPane huntLayout, boolean newSave){
        if(newSave) {
            try {
                File file;
                if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Hunts") == 0)
                    file = new File("SaveData/Layouts/Hunts/~Layouts.txt");
                else
                    file = new File("SaveData/Layouts/Previously-Caught/~Layouts.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(layoutName.substring(layoutName.indexOf('/') + 1) + '\n');
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            File file = new File("SaveData/Layouts/" + layoutName + ".txt");
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(Node i : huntLayout.getChildren()) {
                if(i instanceof ImageView){
                    ImageView image = (ImageView) i;
                    bufferedWriter.write(image.getLayoutX() + "," + image.getLayoutY() + "," + image.getFitWidth() + "," + image.getFitHeight() + "," + image.isVisible() + ",\n");
                }else if(i instanceof Text){
                    Text text = (Text) i;
                    bufferedWriter.write(text.getLayoutX() + "," + text.getLayoutY() + "," + text.getScaleX() + "," + text.getFont().getName() + "," + text.getFill() + "," + text.getStrokeWidth() + "," + text.getStroke() + "," + text.isUnderline() + "," + text.isVisible() + ",\n");
                }
            }
            bufferedWriter.write(String.valueOf(huntLayout.getBackground().getFills().get(0).getFill()) + '\n');
            if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Previously-Caught") == 0)
                bufferedWriter.write(String.valueOf(huntLayout.getChildren().size() / 4) + '\n');
            bufferedWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //load saved layout
    public void loadLayout(String layoutName, AnchorPane huntLayout){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/Layouts/" + layoutName + ".txt"));

            if(layoutName.substring(0, layoutName.indexOf('/')).compareTo("Hunts") == 0) {
                for (int i = 0; i < getfileLength("Layouts/" + layoutName) - 1; i++) {
                    String line = fileReader.readLine();
                    String[] data = line.split(",");
                    if (i < 3) {
                        ImageView image = (ImageView) huntLayout.getChildren().get(i);
                        image.setLayoutX(parseDouble(data[0]));
                        image.setLayoutY(parseDouble(data[1]));
                        double imageWidth = -parseDouble(data[2]);
                        double imageHeight = -parseDouble(data[3]);
                        if(-image.getFitWidth() != imageWidth && -image.getFitHeight() != imageHeight){
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
                        image.setVisible(data[4].compareTo("true") == 0);
                    } else if (i < 10) {
                        Text text = (Text) huntLayout.getChildren().get(i);
                        text.setLayoutX(parseDouble(data[0]));
                        text.setLayoutY(parseDouble(data[1]));
                        text.setScaleX(parseDouble(data[2]));
                        text.setScaleY(parseDouble(data[2]));
                        text.setFont(new Font(data[3], 12));
                        text.setFill(Color.web(data[4]));
                        text.setStrokeWidth(parseDouble(data[5]));
                        text.setStroke(Color.web(data[6]));
                        text.setUnderline(data[7].compareTo("true") == 0);
                        text.setVisible(data[8].compareTo("true") == 0);
                    }
                }
                huntLayout.setBackground(new Background(new BackgroundFill(Color.web(getLinefromFile(getfileLength("Layouts/" + layoutName) - 1, ("Layouts/" + layoutName))), CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                for(int i = 0; i < getfileLength("Layouts/" + layoutName) - 2; i++){
                    String line = fileReader.readLine();
                    String[] data = line.split(",");
                    if(i % 4 == 0){
                        ImageView image = (ImageView) huntLayout.getChildren().get(i);
                        image.setLayoutX(parseDouble(data[0]));
                        image.setLayoutY(parseDouble(data[1]));
                        double imageWidth = -parseDouble(data[2]);
                        double imageHeight = -parseDouble(data[3]);
                        if(-image.getFitWidth() != imageWidth && -image.getFitHeight() != imageHeight){
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
                        image.setVisible(data[4].compareTo("true") == 0);
                    }else{
                        Text text = (Text) huntLayout.getChildren().get(i);
                        text.setLayoutX(parseDouble(data[0]));
                        text.setLayoutY(parseDouble(data[1]));
                        text.setScaleX(parseDouble(data[2]));
                        text.setScaleY(parseDouble(data[2]));
                        text.setFont(new Font(data[3], 12));
                        text.setFill(Color.web(data[4]));
                        text.setStrokeWidth(parseDouble(data[5]));
                        text.setStroke(Color.web(data[6]));
                        text.setUnderline(data[7].compareTo("true") == 0);
                        text.setVisible(data[8].compareTo("true") == 0);
                    }
                }
                huntLayout.setBackground(new Background(new BackgroundFill(Color.web(getLinefromFile(getfileLength("Layouts/" + layoutName) - 2, ("Layouts/" + layoutName))), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }catch(IOException e){
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

    //deletes the given line
    public void deleteLine(int lineNumber, String file){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("SaveData/" + file + ".txt"));
            StringBuilder inputBuffer = new StringBuilder();

            for (int i = 0; i < getfileLength(file); i++) {
                String line = fileReader.readLine();
                if (i == lineNumber) {
                    continue;
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
