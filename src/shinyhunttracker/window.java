package shinyhunttracker;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class window{
    Stage windowStage = new Stage();
    AnchorPane windowLayout = new AnchorPane();
    String[] fonts = generateFonts();
    String currentLayout;

    //create a string of the file path of the game
    public String createGameFilePath(Game selectedGame){
        String filePath = "Images/Sprites/";
        switch(selectedGame.getGeneration()) {
            case 2:
                filePath += "Generation 2/" + selectedGame.getName().toLowerCase() + "/";
                break;
            case 3:
                switch(selectedGame.getName()){
                    case "Ruby":
                    case "Sapphire":
                    case "Emerald":
                        filePath += "Generation 3/ruby-sapphire-emerald/";
                        break;
                    case "FireRed":
                    case "LeafGreen":
                        filePath += "Generation 3/firered-leafgreen/";
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch(selectedGame.getName()){
                    case "Diamond":
                    case "Pearl":
                        filePath += "Generation 4/diamond-pearl/";
                        break;
                    case "Platinum":
                        filePath += "Generation 4/platinum/";
                        break;
                    case "HeartGold":
                    case "SoulSilver":
                        filePath += "Generation 4/heartgold-soulsilver/";
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                filePath += "Generation 5/";
                break;
            default:
                filePath += "3d Sprites/";
                break;
        }
        return filePath;
    }

    //returns ImageView with the sprite of the given pokemon
    public ImageView createPokemonSprite(String name, Game selectedGame){
        String filePath = createGameFilePath(selectedGame);

        if(name.contains("Galarian")) {
            filePath += "galarian/";
            name = name.substring(9);
        }
        else if(name.contains("Alolan")) {
            filePath += "alolan/";
            name = name.substring(7);
        }

        if(!filePath.contains("alternateforms")) {
            switch (name) {
                case "Type: Null":
                    filePath += "type-null";
                    break;
                case "Nidoran♀":
                    filePath += "nidoran-f";
                    break;
                case "Nidoran♂":
                    filePath += "nidoran-m";
                    break;
                case "Mr. Mime":
                    filePath += "mr-mime";
                    break;
                case "Mr. Rime":
                    filePath += "mr-rime";
                    break;
                case "Mime Jr.":
                    filePath += "mime-jr";
                    break;
                case "Flabébé":
                    filePath += "flabebe";
                    break;
                case "Tapu Koko":
                    filePath += "tapu-koko";
                    break;
                case "Tapu Lele":
                    filePath += "tapu-lele";
                    break;
                case "Tapu Bulu":
                    filePath += "tapu-bulu";
                    break;
                case "Tapu Fini":
                    filePath += "tapu-fini";
                    break;
                case "Farfetch'd":
                    filePath += "farfetchd";
                    break;
                case "Sirfetch'd":
                    filePath += "sirfetchd";
                    break;
                default:
                    filePath += name.toLowerCase();
                    break;
            }
        }

        if(selectedGame.getGeneration() < 5)
            filePath += ".png";
        else
            filePath += ".gif";

        try {
            FileInputStream input = new FileInputStream(filePath);
            Image image = new Image(input);
            ImageView sprite = new ImageView(image);
            sprite.setTranslateX(-(sprite.getImage().getWidth()/2));
            sprite.setTranslateY(-(sprite.getImage().getHeight()/2));
            return sprite;
        }catch (FileNotFoundException e){
            System.out.println(name + " sprite not found");
            try {
                return new ImageView(new Image(new FileInputStream("Images/Sprites/blank.png")));
            }catch(IOException f){
                System.out.println("Placeholder not found");
            }
        }
        return null;
    }

    //comboBox of all variants of given pokemon
    public ComboBox<String> getPokemonForms(String name, int generation){
        ComboBox<String> comboBox = new ComboBox<>();

        String[] females = {"Abomasnow", "Aipom", "Alakazam", "Ambipom", "Beautifly", "Bibarel", "Bidoof", "Blaziken", "Buizel", "Butterfree", "Cacturne", "Camerupt", "Combee", "Combusken", "Croagunk", "Dodrio", "Doduo", "Donphan", "Dustox", "Finneon", "Floatzel", "Frillish", "Gabite", "Garchomp", "Gible", "Girafarig", "Gligar", "Gloom", "Golbat", "Goldeen", "Gulpin", "Gyarados", "Heracross", "Hippopotas", "Hippowdon", "Houndoom", "Hypno", "Indeedee", "Jellicent", "Kadabra", "Kricketot", "Kricketune", "Ledian", "Ledyba", "Ludicolo", "Lumineon", "Luxio", "Luxray", "Magikarp", "Mamoswine", "Medicham", "Meditite", "Meganium", "Meowstic", "Milotic", "Murkrow", "Numel", "Nuzleaf", "Octillery", "Pachirisu", "Pikachu", "Piloswine", "Politoed", "Pyroar", "Quagsire", "Raichu", "Raticate", "Rattata", "Relicanth", "Rhydon", "Rhyhorn", "Rhyperior", "Roselia", "Roserade", "Scizor", "Scyther", "Seaking", "Shiftry", "Shinx", "Sneasel", "Snover","Staraptor", "Staravia", "Starly", "Steelix", "Sudowoodo", "Swalot", "Tangrowth", "Torchic", "Toxicroak", "Unfeazant", "Ursaring", "Venusaur", "Vileplume", "Weavile", "Wobbuffet"};
        for(String i : females){
            if(i.compareTo(name) == 0) {
                comboBox.getItems().add("Male");
                comboBox.getItems().add("Female");
            }
        }

        switch(name){
            case "Aegislash":
                comboBox.getItems().add("Shield");
                comboBox.getItems().add("Sword");
                break;
            case "Alcremie":
                comboBox.getItems().add("Strawberry Sweet");
                comboBox.getItems().add("Berry Sweet");
                comboBox.getItems().add("Love Sweet");
                comboBox.getItems().add("Clover Sweet");
                comboBox.getItems().add("Flower Sweet");
                comboBox.getItems().add("Star Sweet");
                comboBox.getItems().add("Ribbon Sweet");
                break;
            case "Basculin":
                comboBox.getItems().add("Red Striped");
                comboBox.getItems().add("Blue Striped");
                break;
            case "Burmy":
            case "Wormadam":
                comboBox.getItems().add("Plant");
                comboBox.getItems().add("Sand");
                comboBox.getItems().add("Trash");
                break;
            case "Castform":
                comboBox.getItems().add("Normal");
                comboBox.getItems().add("Rainy");
                comboBox.getItems().add("Snowy");
                comboBox.getItems().add("Sunny");
                break;
            case "Cherrim":
                comboBox.getItems().add("Overcast");
                comboBox.getItems().add("Sunshine");
                break;
            case "Darmanitan":
            case "Galarian Darmanitan":
                comboBox.getItems().add("Standard");
                comboBox.getItems().add("Zen");
                break;
            case "Deerling":
            case "Sawsbuck":
                comboBox.getItems().add("Spring");
                comboBox.getItems().add("Autumn");
                comboBox.getItems().add("Summer");
                comboBox.getItems().add("Winter");
                break;
            case "Deoxys":
                comboBox.getItems().add("Normal");
                comboBox.getItems().add("Attack");
                comboBox.getItems().add("Defense");
                comboBox.getItems().add("Speed");
                break;
            case "Eiscue":
                comboBox.getItems().add("Ice");
                comboBox.getItems().add("No Ice");
                break;
            case "Flabébé":
            case "Florges":
                comboBox.getItems().add("Red");
                comboBox.getItems().add("Blue");
                comboBox.getItems().add("Orange");
                comboBox.getItems().add("White");
                comboBox.getItems().add("Yellow");
                break;
            case "Floette":
                comboBox.getItems().add("Red");
                comboBox.getItems().add("Blue");
                comboBox.getItems().add("Orange");
                comboBox.getItems().add("White");
                comboBox.getItems().add("Yellow");
                comboBox.getItems().add("Eternal");
                break;
            case "Gastrodon":
            case "Shellos":
                comboBox.getItems().add("West");
                comboBox.getItems().add("East");
                break;
            case "Giratina":
                comboBox.getItems().add("Altered");
                comboBox.getItems().add("Origin");
                break;
            case "Keldeo":
                comboBox.getItems().add("Ordinary");
                comboBox.getItems().add("Resolute");
                break;
            case "Kyurem":
                comboBox.getItems().add("Kyurem");
                comboBox.getItems().add("Black Kyurem");
                comboBox.getItems().add("White Kyurem");
                break;
            case "Thundurus":
            case "Tornadus":
            case "Landorus":
                comboBox.getItems().add("Incarnate");
                comboBox.getItems().add("Therian");
                break;
            case "Lycanroc":
                comboBox.getItems().add("Midday");
                comboBox.getItems().add("Midnight");
                comboBox.getItems().add("Dusk");
                break;
            case "Meloetta":
                comboBox.getItems().add("Aria");
                comboBox.getItems().add("Pirouette");
                break;
            case "Mimikyu":
                comboBox.getItems().add("Disguised");
                comboBox.getItems().add("Busted");
                break;
            case "Morpeko":
                comboBox.getItems().add("Full Belly");
                comboBox.getItems().add("Hangry");
                break;
            case "Necrozma":
                comboBox.getItems().add("Necrozma");
                comboBox.getItems().add("Dawn Mane");
                comboBox.getItems().add("Dusk Wing");
                break;
            case "Oricorio":
                comboBox.getItems().add("Baile");
                comboBox.getItems().add("Pom-Pom");
                comboBox.getItems().add("Pa'u");
                comboBox.getItems().add("Sensu");
                break;
            case "Rotom":
                comboBox.getItems().add("Rotom");
                comboBox.getItems().add("Fan");
                comboBox.getItems().add("Frost");
                comboBox.getItems().add("Heat");
                comboBox.getItems().add("Mow");
                comboBox.getItems().add("Wash");
                break;
            case "Shaymin":
                comboBox.getItems().add("Land");
                comboBox.getItems().add("Sky");
                break;
            case "Toxtricity":
                comboBox.getItems().add("High-Key");
                comboBox.getItems().add("Low-Key");
                break;
            case "Unown":
                comboBox.getItems().add("A");
                comboBox.getItems().add("B");
                comboBox.getItems().add("C");
                comboBox.getItems().add("D");
                comboBox.getItems().add("E");
                comboBox.getItems().add("F");
                comboBox.getItems().add("G");
                comboBox.getItems().add("H");
                comboBox.getItems().add("I");
                comboBox.getItems().add("J");
                comboBox.getItems().add("K");
                comboBox.getItems().add("L");
                comboBox.getItems().add("M");
                comboBox.getItems().add("N");
                comboBox.getItems().add("O");
                comboBox.getItems().add("P");
                comboBox.getItems().add("Q");
                comboBox.getItems().add("R");
                comboBox.getItems().add("S");
                comboBox.getItems().add("T");
                comboBox.getItems().add("U");
                comboBox.getItems().add("V");
                comboBox.getItems().add("W");
                comboBox.getItems().add("X");
                comboBox.getItems().add("Y");
                comboBox.getItems().add("Z");
                if(generation > 2) {
                    comboBox.getItems().add("?");
                    comboBox.getItems().add("!");
                }
                break;
            case "Urshifu":
                comboBox.getItems().add("Single Strike");
                comboBox.getItems().add("Rapid Strike");
                break;
            case "Vivillon":
                comboBox.getItems().add("Meadow");
                comboBox.getItems().add("Polar");
                comboBox.getItems().add("Tundra");
                comboBox.getItems().add("Continental");
                comboBox.getItems().add("Garden");
                comboBox.getItems().add("Elegant");
                comboBox.getItems().add("Icy Snow");
                comboBox.getItems().add("Modern");
                comboBox.getItems().add("Marine");
                comboBox.getItems().add("Archipelago");
                comboBox.getItems().add("High Plains");
                comboBox.getItems().add("Sandstorm");
                comboBox.getItems().add("River");
                comboBox.getItems().add("Monsoon");
                comboBox.getItems().add("Savannah");
                comboBox.getItems().add("Sun");
                comboBox.getItems().add("Ocean");
                comboBox.getItems().add("Jungle");
                comboBox.getItems().add("Fancy");
                comboBox.getItems().add("Poke Ball");
                break;
            case "Wishiwashi":
                comboBox.getItems().add("Solo");
                comboBox.getItems().add("School");
                break;
            case "Xerneas":
                comboBox.getItems().add("Neutral");
                comboBox.getItems().add("Active");
                break;
            case "Zacian":
            case "Zamazenta":
                comboBox.getItems().add("Hero of Many Battles");
                comboBox.getItems().add("Crowned");
                break;
            case "Zarude":
                comboBox.getItems().add("Zarude");
                comboBox.getItems().add("Dada");
                break;
            case "Zygarde":
                comboBox.getItems().add("50%");
                comboBox.getItems().add("10%");
                comboBox.getItems().add("100%");
                break;
            default:
                break;
        }

        if (generation < 8 && generation > 5) {
            String[] megas = {"Abomasnow", "Absol", "Alakazam", "Ampharos", "Banette", "Blasoise", "Blaziken", "Charizard", "Garchomp", "Gardevoir", "Gengar", "Gyarados", "Heracross", "Houndoom", "Kangaskhan", "Latias", "Latios", "Lucario", "Manectric", "Mawile", "Medicham", "Mewtwo", "Pinsir", "Scizor", "Tyranitar", "Venusaur"};
            for (String i : megas) {
                if (i.compareTo(name) == 0) {
                    if (comboBox.getItems().size() == 0)
                        comboBox.getItems().add("Regular");
                    comboBox.getItems().add("Mega");
                }
            }
        }

        if(generation == 8) {
            String[] GMax = {"Alcremie", "Appletun", "Blastoise", "Butterfree", "Centiskorch", "Charizard", "Cinderace", "Coalossal", "Copperajah", "Corviknight", "Drednew", "Duraludon", "Eevee", "Flapple", "Garbodor", "Gengar", "Grimmsnarl", "Hatterene", "Inteleon", "Kingler", "Lapras", "Machamp", "Melmetal", "Meowth", "Orbeetle", "Pikachu", "Rillaboom", "Sandaconda", "Snorlax", "Toxtricity", "Venusaur"};
            for(String i : GMax)
                if(i.compareTo(name) == 0) {
                    if (comboBox.getItems().size() == 0)
                        comboBox.getItems().add("Regular");
                    comboBox.getItems().add("Gigantamax");
                }
        }

        return comboBox;
    }

    //changes given image to given pokemon variant
    public void createAlternateSprite(String name, String form, Game selectedGame, ImageView image){
        String filePath = createGameFilePath(selectedGame);
        switch(form) {
            case "Female":
                filePath += "alternateforms/female/" + name.toLowerCase() + "-f";
                break;
            case "Gigantamax":
                filePath += "alternateforms/gmax/" + name.toLowerCase() + "-gigantamax";
                break;
            case "Mega":
                filePath += "alternateforms/mega/" + name.toLowerCase() + "-mega";
                break;
            case "Sword":
                filePath += "alternateforms/aegislash-blade";
                break;
            case "Berry Sweet":
                filePath += "alternateforms/alcremie-berry";
                break;
            case "Love Sweet":
                filePath += "alternateforms/alcremie-love";
                break;
            case "Clover Sweet":
                filePath += "alternateforms/alcremie-clover";
                break;
            case "Flower Sweet":
                filePath += "alternateforms/alcremie-flower";
                break;
            case "Star Sweet":
                filePath += "alternateforms/alcremie-star";
                break;
            case "Ribbon Sweet":
                filePath += "alternateforms/alcremie-ribbon";
                break;
            case "Blue Striped":
                filePath += "alternateforms/basculin-blue";
                break;
            case "Sand":
                filePath += "alternateforms/" + name.toLowerCase() + "-sandy";
                break;
            case "Trash":
                filePath += "alternateforms/" + name.toLowerCase() + "-trash";
                break;
            case "Rainy":
                filePath += "alternateforms/castform-rain";
                break;
            case "Snowy":
                filePath += "alternateforms/castform-snow";
                break;
            case "Sunny":
                filePath += "alternateforms/castform-sun";
                break;
            case "Sunshine":
                filePath += "alternateforms/cherrim-sunshine";
                break;
            case "Zen":
                if(name.compareTo("Galarian Darmanitan") == 0)
                    filePath += "alternateforms/galarian-darmanitan-zen";
                else filePath += "alternateforms/darmanitan-zen";
                break;
            case "Autumn":
                filePath += "alternateforms/" + name.toLowerCase() + "-autumn";
                break;
            case "Summer":
                filePath += "alternateforms/" + name.toLowerCase() + "-summer";
                break;
            case "Winter":
                filePath += "alternateforms/" + name.toLowerCase() + "-winter";
                break;
            case "Attack":
                filePath += "alternateforms/deoxys-attack";
                break;
            case "Defense":
                filePath += "alternateforms/deoxys-defense";
                break;
            case "Speed":
                filePath += "alternateforms/deoxys-speed";
                break;
            case "No Ice":
                filePath += "alternateforms/eiscue-noice";
                break;
            case "Blue":
                if(name.compareTo("Flabébé") == 0)
                    filePath += "alternateforms/flabebe-blue";
                else filePath += "alternateforms/" + name.toLowerCase() + "-blue";
                break;
            case "Orange":
                if(name.compareTo("Flabébé") == 0)
                    filePath += "alternateforms/flabebe-orange";
                else filePath += "alternateforms/" + name.toLowerCase() + "-orange";
                break;
            case "White":
                if(name.compareTo("Flabébé") == 0)
                    filePath += "alternateforms/flabebe-white";
                else filePath += "alternateforms/" + name.toLowerCase() + "-white";
                break;
            case "Yellow":
                if(name.compareTo("Flabébé") == 0)
                    filePath += "alternateforms/flabebe-yellow";
                else filePath += "alternateforms/" + name.toLowerCase() + "-yellow";
                break;
            case "Eternal":
                filePath += "alternateforms/floette-eternal";
                break;
            case "East":
                filePath += "alternateforms/" + name.toLowerCase() + "-east";
                break;
            case "Origin":
                filePath += "alternateforms/giratina-origin";
                break;
            case "Resolute":
                filePath += "alternateforms/keldeo-resolute";
                break;
            case "Black Kyurem":
                filePath += "alternateforms/kyurem-black";
                break;
            case "White Kyurem":
                filePath += "alternateforms/kyurem-white";
                break;
            case "Therian":
                filePath += "alternateforms/" + name.toLowerCase() + "-therian";
                break;
            case "Midnight":
                filePath += "alternateforms/lycanroc-midnight";
                break;
            case "Dusk":
                filePath += "alternateforms/lycanroc-dusk";
                break;
            case "Pirouette":
                filePath += "alternateforms/meloetta-pirouette";
                break;
            case "Busted":
                filePath += "alternateforms/mimikyu-broken";
                break;
            case "Hangry":
                filePath += "alternateforms/morpeko-hangry";
                break;
            case "Dawn Mane":
                filePath += "alternateforms/necrozma-dawnmane";
                break;
            case "Dusk Wing":
                filePath += "alternateforms/necrozma-dawnwing";
                break;
            case "Pom-Pom":
                filePath += "alternateforms/oricorio-pompom";
                break;
            case "Pa'u":
                filePath += "alternateforms/oricorio-pau";
                break;
            case "Sensu":
                filePath += "alternateforms/oricorio-sensu";
                break;
            case "Fan":
                filePath += "alternateforms/rotom-fan";
                break;
            case "Frost":
                filePath += "alternateforms/rotom-frost";
                break;
            case "Heat":
                filePath += "alternateforms/rotom-heat";
                break;
            case "Mow":
                filePath += "alternateforms/rotom-mow";
                break;
            case "Wash":
                filePath += "alternateforms/rotom-wash";
                break;
            case "Sky":
                filePath += "alternateforms/shaymin-sky";
                break;
            case "Low-Key":
                filePath += "alternateforms/toxtricity-lowkey";
                break;
            case "B":
                filePath += "alternateforms/unown-b";
                break;
            case "C":
                filePath += "alternateforms/unown-c";
                break;
            case "D":
                filePath += "alternateforms/unown-d";
                break;
            case "E":
                filePath += "alternateforms/unown-e";
                break;
            case "F":
                filePath += "alternateforms/unown-f";
                break;
            case "G":
                filePath += "alternateforms/unown-g";
                break;
            case "H":
                filePath += "alternateforms/unown-h";
                break;
            case "I":
                filePath += "alternateforms/unown-i";
                break;
            case "J":
                filePath += "alternateforms/unown-j";
                break;
            case "K":
                filePath += "alternateforms/unown-k";
                break;
            case "L":
                filePath += "alternateforms/unown-l";
                break;
            case "M":
                filePath += "alternateforms/unown-m";
                break;
            case "N":
                filePath += "alternateforms/unown-n";
                break;
            case "O":
                filePath += "alternateforms/unown-o";
                break;
            case "P":
                filePath += "alternateforms/unown-p";
                break;
            case "Q":
                filePath += "alternateforms/unown-q";
                break;
            case "R":
                filePath += "alternateforms/unown-r";
                break;
            case "S":
                filePath += "alternateforms/unown-s";
                break;
            case "T":
                filePath += "alternateforms/unown-t";
                break;
            case "U":
                filePath += "alternateforms/unown-u";
                break;
            case "V":
                filePath += "alternateforms/unown-v";
                break;
            case "W":
                filePath += "alternateforms/unown-w";
                break;
            case "X":
                filePath += "alternateforms/unown-x";
                break;
            case "Y":
                filePath += "alternateforms/unown-y";
                break;
            case "Z":
                filePath += "alternateforms/unown-z";
                break;
            case "?":
                filePath += "alternateforms/unown-question";
                break;
            case "!":
                filePath += "alternateforms/unown-!";
                break;
            case "Rapid Strike":
                filePath += "alternateforms/urshifu-water";
                break;
            case "Polar":
                filePath += "alternateforms/vivillon-polar";
                break;
            case "Tundra":
                filePath += "alternateforms/vivillon-tundra";
                break;
            case "Continental":
                filePath += "alternateforms/vivillon-continental";
                break;
            case "Garden":
                filePath += "alternateforms/vivillon-garden";
                break;
            case "Elegant":
                filePath += "alternateforms/vivillon-elegant";
                break;
            case "Icy Snow":
                filePath += "alternateforms/vivillon-icy";
                break;
            case "Modern":
                filePath += "alternateforms/vivillon-modern";
                break;
            case "Marine":
                filePath += "alternateforms/vivillon-marine";
                break;
            case "Archipelago":
                filePath += "alternateforms/vivillon-archipelago";
                break;
            case "High Plains":
                filePath += "alternateforms/vivillon-highplains";
                break;
            case "Sandstorm":
                filePath += "alternateforms/vivillon-sandstorm";
                break;
            case "River":
                filePath += "alternateforms/vivillon-river";
                break;
            case "Monsoon":
                filePath += "alternateforms/vivillon-monsoon";
                break;
            case "Savannah":
                filePath += "alternateforms/vivillon-savannah";
                break;
            case "Sun":
                filePath += "alternateforms/vivillon-sun";
                break;
            case "Ocean":
                filePath += "alternateforms/vivillon-ocean";
                break;
            case "Jungle":
                filePath += "alternateforms/vivillon-jungle";
                break;
            case "Fancy":
                filePath += "alternateforms/vivillon-fancy";
                break;
            case "Poke Ball":
                filePath += "alternateforms/vivillon-pokeball";
                break;
            case "School":
                filePath += "alternateforms/wishiwashi-school";
                break;
            case "Active":
                filePath += "alternateforms/xerneas-active";
                break;
            case "Crowned":
                filePath += "alternateforms/" + name.toLowerCase() + "-crowned";
                break;
            case "Dada":
                filePath += "alternateforms/zarude-dada";
                break;
            case "10%":
                filePath += "alternateforms/zygarde-10";
                break;
            case "100%":
                filePath += "alternateforms/zygarde-complete";
                break;
            default:
                if(name.compareTo("Flabébé") == 0)
                    filePath += "flabebe";
                else
                    filePath += name.toLowerCase();
                break;
        }

        if(selectedGame.getGeneration() < 5)
            filePath += ".png";
        else
            filePath += ".gif";

        try{
            image.setImage(new Image(new FileInputStream(filePath)));
        }catch(IOException ignored) {

        }
    }

    //creates ImageView settings VBox
    public VBox createImageSettings(ImageView image, String pokemonName, Game selectedGame){
        HBox groupLabel = new HBox();
        Label Group = new Label(pokemonName + " Sprite:");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        sizeField.promptTextProperty().bind(image.scaleXProperty().asString());
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.promptTextProperty().bind(image.layoutXProperty().asString());
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.promptTextProperty().bind(image.layoutYProperty().asString());
        changeY.getChildren().addAll(YLabel, YField);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(image.isVisible());
        visablility.getChildren().addAll(visableLabel, visableCheck);

        HBox form = new HBox();
        form.setSpacing(5);
        Label formLabel = new Label("Form");
        ComboBox<String> formCombo = getPokemonForms(pokemonName, selectedGame.getGeneration());
        formCombo.getSelectionModel().select(0);
        form.getChildren().addAll(formLabel, formCombo);

        VBox imageVBox = new VBox();
        imageVBox.setSpacing(10);
        if(formCombo.getItems().size() != 0)
            imageVBox.getChildren().add(form);
        imageVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, visablility);

        Accordion accordion = new Accordion();
        TitledPane imageTitledPane = new TitledPane(pokemonName + " Sprite", imageVBox);
        accordion.getPanes().add(imageTitledPane);

        VBox imageSettings = new VBox();
        imageSettings.getChildren().add(accordion);

        formCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                createAlternateSprite(pokemonName, newValue, selectedGame, image);
            }
        });

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            image.setScaleX(scale);
            image.setScaleY(scale);
            sizeField.setText("");
        });

        XField.setOnAction(e ->{
            try{
                image.setLayoutX(parseDouble(XField.getText()));
                XField.setText("");
            }catch(NumberFormatException f){
                XField.setText("");
            }
        });

        YField.setOnAction(e ->{
            try{
                image.setLayoutY(parseDouble(YField.getText()));
                YField.setText("");
            }catch(NumberFormatException f){
                YField.setText("");
            }
        });

        visableCheck.setOnAction(e -> image.setVisible(visableCheck.isSelected()));

        return imageSettings;
    }

    //creates Label settings VBox
    public VBox createLabelSettings(Text label, String labelName){
        HBox groupLabel = new HBox();
        Label Group = new Label(labelName + " Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale: ");
        TextField sizeField = new TextField();
        sizeField.promptTextProperty().bind(label.scaleXProperty().asString());
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.promptTextProperty().bind(label.layoutXProperty().asString());
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.promptTextProperty().bind(label.layoutYProperty().asString());
        changeY.getChildren().addAll(YLabel, YField);

        HBox font = new HBox();
        font.setSpacing(5);
        Label fontLabel = new Label("Font:");
        ComboBox<String> fontNameBox = new ComboBox<>();
        fontNameBox.setEditable(false);
        fontNameBox.getItems().addAll(fonts);
        font.getChildren().addAll(fontLabel, fontNameBox);

        HBox color = new HBox();
        color.setSpacing(5);
        Label colorLabel = new Label("Color:");
        ColorPicker colorField = new ColorPicker();
        colorField.setValue((Color) label.getFill());
        color.getChildren().addAll(colorLabel, colorField);

        HBox stroke = new HBox();
        stroke.setSpacing(5);
        Label strokeLabel = new Label("Stroke:");
        CheckBox strokeCheckbox = new CheckBox();
        stroke.getChildren().addAll(strokeLabel, strokeCheckbox);

        HBox strokeWidth = new HBox();
        strokeWidth.setSpacing(5);
        Label strokeWidthLabel = new Label("Stroke Width:");
        TextField strokeWidthField = new TextField();
        strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
        strokeWidthLabel.setDisable(true);
        strokeWidthField.setDisable(true);
        strokeWidth.getChildren().addAll(strokeWidthLabel, strokeWidthField);

        HBox strokeColor = new HBox();
        strokeColor.setSpacing(5);
        Label strokeColorLabel = new Label("Stroke Color:");
        ColorPicker strokeColorPicker = new ColorPicker();
        strokeColorPicker.setValue((Color) label.getStroke());
        strokeColorLabel.setDisable(true);
        strokeColorPicker.setDisable(true);
        strokeColor.getChildren().addAll(strokeColorLabel, strokeColorPicker);

        HBox textProperties = new HBox();
        textProperties.setSpacing(5);
        Label italics = new Label("Italics:");
        CheckBox italicsCheck = new CheckBox();
        italicsCheck.setSelected(label.getFont().getName().contains("Italic"));
        Label bold = new Label("Bold:");
        CheckBox boldCheck = new CheckBox();
        boldCheck.setSelected(label.getFont().getName().contains("Bold"));
        Label underlined = new Label("Underlined:");
        CheckBox underlinedCheck = new CheckBox();
        underlinedCheck.setSelected(label.isUnderline());
        textProperties.getChildren().addAll(italics, italicsCheck, bold, boldCheck, underlined, underlinedCheck);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(label.isVisible());
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox labelVBox = new VBox();
        labelVBox.setSpacing(10);
        labelVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, font, color, stroke, strokeWidth, strokeColor, textProperties, visablility);

        Accordion accordion = new Accordion();
        TitledPane labelTitledPane = new TitledPane(labelName, labelVBox);
        accordion.getPanes().add(labelTitledPane);

        VBox labelSettings = new VBox();
        labelSettings.getChildren().add(accordion);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
                sizeField.setText("");
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            label.setScaleX(scale);
            label.setScaleY(scale);
        });

        XField.setOnAction(e ->{
            double X = 0;
            try{
                X = parseDouble(XField.getText());
                XField.setText("");
            }catch(NumberFormatException f){
                XField.setText("");
            }
            label.setLayoutX(X);
        });

        YField.setOnAction(e ->{
            double Y = 0;
            try{
                Y = parseDouble(YField.getText());
                YField.setText("");
            }catch(NumberFormatException f){
                YField.setText("");
            }
            label.setLayoutY(Y);
        });

        fontNameBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                if(!canBold(newValue)) {
                    boldCheck.setSelected(false);
                    boldCheck.setDisable(true);
                }else
                    boldCheck.setDisable(false);

                if(!canItalic(newValue)){
                    italicsCheck.setSelected(false);
                    italicsCheck.setDisable(true);
                }else
                    italicsCheck.setDisable(false);

                if(boldCheck.isSelected() && italicsCheck.isSelected())
                    label.setFont(Font.font(newValue, FontWeight.BOLD, FontPosture.ITALIC, 12));
                else if(boldCheck.isSelected())
                    label.setFont(Font.font(newValue, FontWeight.BOLD, 12));
                else if(italicsCheck.isSelected())
                    label.setFont(Font.font(newValue, FontPosture.ITALIC, 12));
                else
                    label.setFont(new Font(newValue, 12));
            }
        });

        colorField.setOnAction(e -> label.setFill(colorField.getValue()));

        strokeCheckbox.setOnAction(e -> {
            boolean selected = strokeCheckbox.isSelected();
            if(selected) {
                strokeWidthLabel.setDisable(false);
                strokeWidthField.setDisable(false);
                strokeColorLabel.setDisable(false);
                strokeColorPicker.setDisable(false);
                label.setStrokeWidth(parseDouble(strokeWidthField.getPromptText()));
            }else {
                label.setStrokeWidth(0);
            }
        });

        strokeWidthField.setOnAction(e -> {
            try{
                double width = parseDouble(strokeWidthField.getText());
                label.setStrokeWidth(width);
                strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
                strokeWidthField.setText("");
            }catch(NumberFormatException f){
                strokeWidthField.setText("");
            }
        });

        strokeColorPicker.setOnAction(e -> label.setStroke(strokeColorPicker.getValue()));

        italicsCheck.setOnAction(e -> {
            String fontName = sanitizeFontName(String.valueOf(label.getFont().getName()));
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(fontName, 12));
        });

        boldCheck.setOnAction(e -> {
            String fontName = sanitizeFontName(String.valueOf(label.getFont().getName()));
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(fontName, 12));
        });

        underlinedCheck.setOnAction(e -> label.setUnderline(underlinedCheck.isSelected()));

        visableCheck.setOnAction(e -> label.setVisible(visableCheck.isSelected()));

        return labelSettings;
    }

    //change location of element when dragged
    public void quickEdit(Node element){
        element.setOnMousePressed(e -> {
            element.toFront();
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    //creates string array of avaliable fonts
    private String[] generateFonts(){
        String[] avaliableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        int fontArraySize = 0;
        for (String avaliableFont : avaliableFonts) {
            if (sanitizeAvaliableFontStrings(avaliableFont) != null) {
                fontArraySize++;
            }
        }

        String[] temp = new String[fontArraySize];
        int index = 0;
        for (String avaliableFont : avaliableFonts) {
            if (sanitizeAvaliableFontStrings(avaliableFont) != null) {
                temp[index] = avaliableFont;
                index++;
            }
        }

        return temp;
    }

    //removes "Bold", "Italics", and "Regular" from given string
    public String sanitizeFontName(String name){
        int index = 0;
        if(name.contains("Bold"))
            index += 5;
        if(name.contains("Italic"))
            index += 7;
        if(name.contains("Regular"))
            index += 8;
        return name.substring(0, name.length() - index);
    }

    //checks to see if using a given font returns the system default font
    public String sanitizeAvaliableFontStrings(String name){
        Font test = new Font(name, 12);
        if(test.getName().compareTo("System Regular") == 0){
            return null;
        }
        return name;
    }

    //checks to see if bolding and unbolding a font returns system default
    public boolean canBold(String name){
        Font test;
        test = Font.font(name,  FontWeight.BOLD, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }

    //checks to see if italicizing and unitalicizing a font returns system default
    public boolean canItalic(String name){
        Font test;
        test = Font.font(name,  FontPosture.ITALIC, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }

    //method to create Tree Item branches
    public void makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
    }

    public Stage getStage(){
        return windowStage;
    }

    public AnchorPane getWindowLayout(){
        return windowLayout;
    }

    public String getCurrentLayout(){
        return currentLayout;
    }

    public void setCurrentLayout(String currentLayout){
        this.currentLayout = currentLayout;
    }
}

class huntWindow extends window{
    //hunt window elements
    int huntLayoutSize = 0;
    ImageView Evo0, Evo1, sprite;
    String evo0, evo1;
    Text currentHuntingMethodText, currentHuntingPokemonText, currentGameText, encountersText, currentComboText, oddFractionText;
    Text previousEncountersText = new Text();
    int encounters, previousEncounters, combo;
    int increment;
    int huntNumber;
    char keybind;

    //hunt settings window elements
    Stage CustomizeHuntStage = new Stage();

    //current objects
    Pokemon selectedPokemon;
    Game selectedGame;
    Method selectedMethod;
    int methodBase;

    public huntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, String evo0, String evo1, String layout, int encounters, int combo, int increment, int huntNumber){
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        this.evo0 = evo0;
        this.evo1 = evo1;
        this.currentLayout = layout;
        this.encounters = encounters;
        this.combo = combo;
        this.increment = increment;
        this.huntNumber = huntNumber;
        if(selectedPokemon.getGeneration() > 0)
            createHuntWindow();
    }

    //creates hunt window
    public void createHuntWindow(){
        currentHuntingPokemonText = new Text(selectedPokemon.getName());
        currentHuntingMethodText= new Text(selectedMethod.getName());
        currentGameText = new Text(selectedGame.getName());
        oddFractionText= new Text("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        dynamicOddsMethods();
        encountersText= new Text(String.valueOf(encounters));
        currentComboText = new Text(String.valueOf(combo));
        currentComboText.setVisible(false);
        previousEncountersText = new Text();
        previousEncountersText.setVisible(false);

        currentHuntingPokemonText.setStroke(Color.web("0x00000000"));
        currentHuntingMethodText.setStroke(Color.web("0x00000000"));
        currentGameText.setStroke(Color.web("0x00000000"));
        oddFractionText.setStroke(Color.web("0x00000000"));
        encountersText.setStroke(Color.web("0x00000000"));
        currentComboText.setStroke(Color.web("0x00000000"));
        previousEncountersText.setStroke(Color.web("0x00000000"));

        quickEdit(currentHuntingPokemonText);
        quickEdit(currentHuntingMethodText);
        quickEdit(currentGameText);
        quickEdit(oddFractionText);
        quickEdit(encountersText);
        quickEdit(currentComboText);
        quickEdit(previousEncountersText);

        sprite = createPokemonSprite(selectedPokemon.getName(), selectedGame);
        Evo0 = new ImageView();
        Evo1 = new ImageView();
        if(evo0 != null) {
            Evo0 = createPokemonSprite(evo0, selectedGame);
            if(Evo0 == null)
                Evo0 = new ImageView();
        }
        if(evo1 != null) {
            Evo1 = createPokemonSprite(evo1, selectedGame);
            if(Evo1 == null)
                Evo1 = new ImageView();
        }

        quickEdit(sprite);
        quickEdit(Evo0);
        quickEdit(Evo1);

        windowLayout.getChildren().addAll(sprite, Evo0, Evo1);

        switch(selectedMethod.getName()){
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                currentComboText.setVisible(true);
                break;
            case "DexNav":
                currentComboText.setVisible(true);
                previousEncountersText.setVisible(true);
                break;
            case "Total Encounters":
                previousEncountersText.setVisible(true);
                break;
            default:
                break;
        }
        windowLayout.getChildren().addAll(currentHuntingPokemonText, currentHuntingMethodText, currentGameText, encountersText, previousEncountersText, currentComboText, oddFractionText);
        huntLayoutSize = windowLayout.getChildren().size();

        int index = 3;
        for(int i = 3; i < windowLayout.getChildren().size(); i++){
            if(windowLayout.getChildren().get(i).isVisible()) {
                if(evo1 != null)
                    windowLayout.getChildren().get(i).setLayoutX(Evo0.getImage().getWidth() + Evo1.getImage().getWidth() + sprite.getImage().getWidth() + 5);
                else if(evo0 != null)
                    windowLayout.getChildren().get(i).setLayoutX(Evo0.getImage().getWidth() + sprite.getImage().getWidth() + 5);
                else
                    windowLayout.getChildren().get(i).setLayoutX(sprite.getImage().getWidth() + 5);
                windowLayout.getChildren().get(i).setLayoutY(15 * (index - 2));
                index++;
            }
        }

        if(this.evo1 != null) {
            Evo0.setLayoutY(Evo0.getImage().getHeight()/2);
            Evo0.setLayoutX(Evo0.getImage().getWidth()/2);

            Evo1.setLayoutY(Evo1.getImage().getHeight()/2);
            Evo1.setLayoutX(Evo0.getImage().getWidth() + (Evo1.getImage().getWidth()/2));

            sprite.setLayoutY(sprite.getImage().getHeight()/2);
            sprite.setLayoutX(Evo0.getImage().getWidth() + Evo1.getImage().getWidth() + (sprite.getImage().getWidth()/2));
        }else if(this.evo0 != null){
            Evo0.setLayoutY(Evo0.getImage().getHeight()/2);
            Evo0.setLayoutX(Evo0.getImage().getWidth()/2);
            sprite.setLayoutY(sprite.getImage().getHeight()/2);
            sprite.setLayoutX(Evo0.getImage().getWidth() + (sprite.getImage().getWidth()/2));
        }else {
            sprite.setLayoutY(sprite.getImage().getHeight()/2);
            sprite.setLayoutX(sprite.getImage().getWidth()/2);
        }

        Scene huntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(huntScene);
        windowStage.show();

        SaveData data = new SaveData();
        if(currentLayout != null && currentLayout.compareTo("null") != 0) {
            data.loadLayout("Hunts/" + currentLayout, windowLayout);
        }
    }

    //creates window to prompt user for search level or previous encounters
    public void promptPreviousEncounters(){
        Stage promptWindow = new Stage();
        promptWindow.setResizable(false);
        promptWindow.initModality(Modality.APPLICATION_MODAL);

        Label promptLabel = new Label();
        if(selectedMethod.getName().compareTo("DexNav") == 0) {
            promptWindow.setTitle("Enter Search Level");
            promptLabel.setText("Please enter the current Search Level for " + selectedPokemon.getName());
        }
        else if(selectedMethod.getName().compareTo("Total Encounters") == 0) {
            promptWindow.setTitle("Enter Number Battled");
            promptLabel.setText("Please enter the current Number Battled for " + selectedPokemon.getName());
        }

        TextField previousInput = new TextField();

        previousInput.setOnAction(e-> {
            try{
                previousEncounters = parseInt(previousInput.getText());
                previousEncountersText.setText(String.valueOf(previousEncounters));
                if(selectedMethod.getName().compareTo("DexNav") == 0)
                    oddFractionText.setText("1/" + selectedMethod.dexNav(encounters, previousEncounters));
                else
                    oddFractionText.setText("1/" + simplifyFraction((selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters)), methodBase));
                promptWindow.close();
            }catch (NumberFormatException f){
                previousInput.setText("");
            }
        });

        VBox promptLayout = new VBox();
        promptLayout.setSpacing(10);
        promptLayout.setAlignment(Pos.CENTER);
        promptLayout.getChildren().addAll(promptLabel, previousInput);

        Scene promptScene = new Scene(promptLayout, 275, 75);
        promptWindow.setScene(promptScene);
        promptWindow.show();

        promptWindow.setOnCloseRequest(e -> windowStage.close());
    }

    //creates window for the hunt window settings
    public void CustomizeHuntWindow(){
        CustomizeHuntStage.setTitle("Settings");
        CustomizeHuntStage.setResizable(false);
        VBox spriteSettings = createImageSettings(sprite, selectedPokemon.getName(), selectedGame);
        VBox currentPokemonSettings = createLabelSettings(currentHuntingPokemonText, "Pokemon");
        VBox currentMethodSettings = createLabelSettings(currentHuntingMethodText, "Method");
        VBox currentGameSettings = createLabelSettings(currentGameText, "Game");
        VBox encountersSettings = createLabelSettings(encountersText, "Encounters");
        VBox oddsFraction = createLabelSettings(oddFractionText, "Odds");

        VBox backgroundVBox = new VBox();
        Label backgroundGroup = new Label("Background");
        backgroundGroup.setUnderline(true);
        HBox backgroundColorSettings = new HBox();
        backgroundColorSettings.setSpacing(5);
        Label backgroundColorLabel = new Label("Color");
        ColorPicker backgroundColorPicker = new ColorPicker();
        backgroundVBox.setPadding(new Insets(10,10,10,10));
        backgroundVBox.setSpacing(10);
        backgroundColorSettings.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
        backgroundVBox.getChildren().addAll(backgroundGroup, backgroundColorSettings);

        if(windowLayout.getBackground() != null)
            backgroundColorPicker.setValue((Color) windowLayout.getBackground().getFills().get(0).getFill());

        Accordion accordion = new Accordion();
        TitledPane backgroundTitledPane = new TitledPane("Background", backgroundVBox);
        accordion.getPanes().add(backgroundTitledPane);

        VBox backgroundSettings = new VBox();
        backgroundSettings.getChildren().add(accordion);

        HBox saveClose = new HBox();
        saveClose.setPadding(new Insets(10,10,10,10));
        saveClose.setSpacing(5);
        Button Save = new Button("Save");
        Button Load = new Button("Load");
        saveClose.getChildren().addAll(Save,Load);

        VBox CustomizeHuntVBox = new VBox();
        VBox Evo0Settings;
        VBox Evo1Settings;

        CustomizeHuntVBox.getChildren().add(spriteSettings);
        if(evo1 != null){
            Evo1Settings = createImageSettings(Evo1, evo1, selectedGame);
            CustomizeHuntVBox.getChildren().add(Evo1Settings);
        }
        if(evo0 != null){
            Evo0Settings = createImageSettings(Evo0, evo0, selectedGame);
            CustomizeHuntVBox.getChildren().addAll(Evo0Settings);
        }

        VBox comboSettings;
        VBox previousEncountersSettings;

        switch (selectedMethod.getName()) {
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                comboSettings = createLabelSettings(currentComboText, "Combo");
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, backgroundSettings, saveClose);
                break;
            case "DexNav":
                comboSettings = createLabelSettings(currentComboText, "Combo");
                previousEncountersSettings = createLabelSettings(previousEncountersText, "Search Level");
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, previousEncountersSettings, backgroundSettings, saveClose);
                break;
            case "Total Encounters":
                previousEncountersSettings = createLabelSettings(previousEncountersText, "Total Encounters");
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, previousEncountersSettings, backgroundSettings, saveClose);
                break;
            default:
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, backgroundSettings, saveClose);
                break;
        }

        AnchorPane CustomizeHuntLayout = new AnchorPane();
        CustomizeHuntLayout.getChildren().add(CustomizeHuntVBox);
        AnchorPane.setTopAnchor(CustomizeHuntVBox,0d);

        ScrollPane CustomizeHuntScrollpane = new ScrollPane(CustomizeHuntLayout);
        CustomizeHuntScrollpane.setFitToHeight(true);

        Scene CustomizeHuntScene = new Scene(CustomizeHuntScrollpane, 263, 500);
        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomizeHuntStage.show();

        backgroundColorPicker.setOnAction(e -> windowLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

        Save.setOnAction(e -> saveLayout());

        Load.setOnAction(e -> loadLayout());
    }

    //adds increment to the encounters
    public void incrementEncounters(){
        encounters += increment;
        combo += increment;
        encountersText.setText(String.valueOf(encounters));
        currentComboText.setText(String.valueOf(combo));
        dynamicOddsMethods();
    }

    //adds current pokemon to the caught pokemon file
    public void pokemonCaught() {
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
        data.pokemonCaught();

        CustomizeHuntStage.close();
        windowStage.close();
    }

    //prompts user for phase pokemon, resets encounters, and adds phased pokemon to the caught pokemon file
    public void phaseHunt(){
        Stage phaseStage = new Stage();
        phaseStage.initModality(Modality.APPLICATION_MODAL);
        phaseStage.setResizable(false);
        phaseStage.setTitle("Phase");

        VBox phaseLayout = new VBox();
        phaseLayout.setAlignment(Pos.CENTER);
        phaseLayout.setSpacing(10);

        Label phaseLabel = new Label("Please enter the phase pokemon");
        TextField phasePokemon = new TextField();
        phaseLayout.getChildren().addAll(phaseLabel, phasePokemon);

        Scene phaseScene = new Scene(phaseLayout, 200,100);
        phaseStage.setScene(phaseScene);
        phaseStage.show();

        phasePokemon.setOnAction(e -> {
            selectionPageController temp = new selectionPageController();
            String userInput = phasePokemon.getText().toLowerCase();
            userInput = userInput.substring(0,1).toUpperCase() + userInput.substring(1);
            if(temp.findGenerationPokemon(userInput) == 0)
                phasePokemon.setText("");
            else{
                SaveData data = new SaveData(new Pokemon(userInput, 0), selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
                data.pokemonCaught();
                resetCombo();
                phaseStage.close();
            }
        });
    }

    //resets encounters
    public void resetCombo(){
        combo = 0;
        currentComboText.setText(String.valueOf(combo));
        dynamicOddsMethods();
    }

    //writes objects to previous hunts file
    public void saveHunt(){
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
        data.saveHunt();
    }

    //save hunt, but it asks if the user would like to save, and it closes the window
    public void saveandCloseHunt(){
        Stage save = new Stage();
        save.setTitle("Save hunt");
        save.setResizable(false);
        save.initModality(Modality.APPLICATION_MODAL);
        VBox saveLayout = new VBox();
        saveLayout.setSpacing(10);
        saveLayout.setAlignment(Pos.CENTER);
        Label savePrompt = new Label("Would you like to save your " + selectedPokemon.getName() + " hunt?");
        HBox yn = new HBox();
        yn.setSpacing(5);
        yn.setAlignment(Pos.CENTER);
        Button yes = new Button("Yes");
        Button no = new Button("No");
        yn.getChildren().addAll(yes, no);
        saveLayout.getChildren().addAll(savePrompt, yn);

        Scene saveScene = new Scene(saveLayout, 275, 75);
        save.setScene(saveScene);
        save.show();

        yes.setOnAction(e -> {
            windowStage.close();
            CustomizeHuntStage.close();
            SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
            data.saveHunt();
            save.close();
        });

        no.setOnAction(e -> {
            windowStage.close();
            CustomizeHuntStage.close();
            save.close();
        });

        save.setOnCloseRequest(Event::consume);
    }

    //changes increment
    public void changeIncrement(){
        Stage changeIncrementStage = new Stage();
        changeIncrementStage.setResizable(false);
        changeIncrementStage.initModality(Modality.APPLICATION_MODAL);
        VBox changeIncrementsLayout = new VBox();
        changeIncrementsLayout.setAlignment(Pos.CENTER);
        changeIncrementsLayout.setSpacing(10);
        Label changeIncrementsLabel = new Label("Enter the number by which the encounters increase for every button press");
        TextField changeIncrementsText = new TextField();
        changeIncrementsLayout.getChildren().addAll(changeIncrementsLabel, changeIncrementsText);
        Scene changeIncrementsScene = new Scene(changeIncrementsLayout, 400, 100);
        changeIncrementStage.setScene(changeIncrementsScene);
        changeIncrementStage.show();

        changeIncrementsText.setOnAction(e -> {
            try{
                increment = parseInt(changeIncrementsText.getText());
                changeIncrementStage.close();
            }catch (NumberFormatException f){
                changeIncrementsText.setText("");
            }
        });
    }

    //reset encounters
    public void resetEncounters(){
        encounters = 0;
        encountersText.setText("0");
    }

    //since some methods' odds change based on encounters
    private void dynamicOddsMethods(){
        switch(selectedMethod.getName()){
            case "Radar Chaining":
                int tempEncounters;
                if (combo >= 40)
                    tempEncounters = 39;
                else
                    tempEncounters = combo;
                oddFractionText.setText("1/" + simplifyFraction(Math.round(((65535 / (8200.0 - tempEncounters * 200)) + selectedMethod.getModifier() - 1)), (65536 / (1 + (Math.abs(methodBase - 8196) / 4096)))));
                break;
            case "Chain Fishing":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.chainFishing(combo), methodBase));
                break;
            case "DexNav":
                if (previousEncounters < 999) {
                    previousEncounters++;
                    previousEncountersText.setText(String.valueOf(previousEncounters));
                }else
                    previousEncountersText.setText("999");
                oddFractionText.setText("1/" + selectedMethod.dexNav(combo, previousEncounters));
                break;
            case "SOS Chaining":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(combo), methodBase));
                break;
            case "Catch Combo":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(combo), methodBase));
                break;
            case "Total Encounters":
                previousEncounters++;
                previousEncountersText.setText(String.valueOf(previousEncounters));
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters), methodBase));
                break;
            default:
                break;
        }
    }

    //save layout
    public void saveLayout(){
        SaveData data = new SaveData();
        Stage promptLayoutSaveName = new Stage();
        promptLayoutSaveName.initModality(Modality.APPLICATION_MODAL);
        promptLayoutSaveName.setResizable(false);

        if(currentLayout != null) {
            promptLayoutSaveName.setTitle("Save Layout");

            VBox selectNewSaveLayout = new VBox();
            selectNewSaveLayout.setAlignment(Pos.CENTER);
            selectNewSaveLayout.setSpacing(5);
            Label newNameLabel = new Label("Would you like to save this layout to a new name?");

            HBox buttons = new HBox();
            buttons.setSpacing(10);
            buttons.setAlignment(Pos.CENTER);
            Button newSaveButton = new Button("New");
            Button oldSaveButton = new Button("Update");
            buttons.getChildren().addAll(newSaveButton, oldSaveButton);

            selectNewSaveLayout.getChildren().addAll(newNameLabel, buttons);

            Scene selectNewSaveScene = new Scene(selectNewSaveLayout, 275, 75);
            promptLayoutSaveName.setScene(selectNewSaveScene);
            promptLayoutSaveName.show();

            newSaveButton.setOnAction(f -> {
                promptLayoutSaveName.setTitle("Select Layout Name");

                VBox saveLayoutNameLayout = new VBox();
                saveLayoutNameLayout.setSpacing(10);
                saveLayoutNameLayout.setAlignment(Pos.CENTER);

                Label saveNameLabel = new Label("What would you like this layout to be called?");
                TextField saveNameText = new TextField();

                saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
                Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
                promptLayoutSaveName.setScene(saveLayoutNameScene);
                promptLayoutSaveName.show();

                saveNameText.setOnAction(g -> {
                    String text = saveNameText.getText();
                    if (text.contains("\\") || text.contains("/") || text.contains(":") || text.contains("*") || text.contains("?") || text.contains("\"") || text.contains("<") || text.contains(">") || text.contains("|") || text.contains(".")) {
                        saveNameText.setText("");
                    } else {
                        data.saveLayout("Hunts/" + saveNameText.getText(), windowLayout, true);
                        currentLayout = saveNameText.getText();
                        promptLayoutSaveName.close();
                    }
                });
            });

            oldSaveButton.setOnAction(f -> {
                data.saveLayout("Hunts/" + currentLayout, windowLayout, false);
                promptLayoutSaveName.close();
            });
        }else{
            promptLayoutSaveName.setTitle("Select Layout Name");

            VBox saveLayoutNameLayout = new VBox();
            saveLayoutNameLayout.setSpacing(10);
            saveLayoutNameLayout.setAlignment(Pos.CENTER);

            Label saveNameLabel = new Label("What would you like this layout to be called?");
            TextField saveNameText = new TextField();

            saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
            Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
            promptLayoutSaveName.setScene(saveLayoutNameScene);
            promptLayoutSaveName.show();

            saveNameText.setOnAction(g -> {
                String text = saveNameText.getText();
                if (text.indexOf('\\') > -1 || text.indexOf('/') > -1 || text.indexOf(':') > -1 || text.indexOf('*') > -1 || text.indexOf('?') > -1 || text.indexOf('\"') > -1 || text.indexOf('<') > -1 || text.indexOf('>') > -1 || text.indexOf('|') > -1 || text.indexOf('.') > -1) {
                    saveNameText.setText("");
                } else {
                    data.saveLayout("Hunts/" + saveNameText.getText(), windowLayout, true);
                    currentLayout = saveNameText.getText();
                    promptLayoutSaveName.close();
                }
            });
        }
    }

    //load layout
    public void loadLayout(){
        SaveData data = new SaveData();

        Stage loadSavedLayoutStage = new Stage();
        loadSavedLayoutStage.initModality(Modality.APPLICATION_MODAL);
        loadSavedLayoutStage.setResizable(false);
        loadSavedLayoutStage.setTitle("Select Layout Name");

        TreeView<String> savedLayouts = new TreeView<>();
        TreeItem<String> root = new TreeItem<>();
        for(int i = 0; i < data.getfileLength("Layouts/Hunts/~Layouts"); i++){
            makeBranch(data.getLinefromFile(i, "Layouts/Hunts/~Layouts"), root);
        }
        savedLayouts.setRoot(root);
        savedLayouts.setShowRoot(false);
        savedLayouts.setPrefWidth(300);
        savedLayouts.setPrefWidth(500);

        VBox savedLayoutsLayout = new VBox();
        savedLayoutsLayout.setSpacing(10);
        savedLayoutsLayout.setAlignment(Pos.CENTER);
        savedLayoutsLayout.getChildren().add(savedLayouts);

        Scene savedLayoutsScene = new Scene(savedLayoutsLayout, 300, 400);
        loadSavedLayoutStage.setScene(savedLayoutsScene);
        loadSavedLayoutStage.show();

        savedLayouts.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    currentLayout = newValue.toString().substring(18, String.valueOf(newValue).length() - 2);
                    data.loadLayout("Hunts/" + currentLayout, windowLayout);
                    loadSavedLayoutStage.close();
                    CustomizeHuntWindow();
                });
    }

    //simplifies odds fraction for easier reading
    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }

    public Pokemon getSelectedPokemon(){ return selectedPokemon; }

    public void setKeybind(char keybind){
        this.keybind = keybind;
    }

    public int getHuntNumber(){
        return huntNumber;
    }

    public char getKeyBinding(){
        return keybind;
    }

    public int getGameGeneration(){ return selectedGame.getGeneration(); }
}

class previouslyCaught extends window{
    int displayCaught;
    int displayPrevious = 0;

    Stage previouslyCaughtSettingsStage = new Stage();
    VBox previouslyCaughtSettingsLayout;
    ColorPicker backgroundColorPicker = new ColorPicker();

    previouslyCaught(int displayCaught){
        this.displayCaught = displayCaught;
        windowStage.setTitle("Previously Caught Pokemon");
    }

    //creates window with previously caught pokemon
    public void createPreviouslyCaughtPokemonWindow(){
        windowLayout = new AnchorPane();
        Scene previousHuntScene = new Scene(windowLayout, 750, 480);
        windowStage.setScene(previousHuntScene);
        backgroundColorPicker.setDisable(false);
        windowStage.show();
    }

    //creates elements for previously caught element settings
    public void previouslyCaughtPokemonSettings(){
        windowLayout.getChildren().remove(0, windowLayout.getChildren().size());
        previouslyCaughtSettingsStage.setTitle("Previously Caught Pokemon Settings");

        Label numberCaught = new Label("Display Previously Caught: ");
        TextField numberCaughtField = new TextField();
        numberCaughtField.setMaxWidth(50);
        numberCaughtField.setPromptText(String.valueOf(displayCaught));

        HBox numberPreviouslyCaught = new HBox();
        numberPreviouslyCaught.setAlignment(Pos.CENTER);
        numberPreviouslyCaught.setSpacing(5);
        numberPreviouslyCaught.setPadding(new Insets(10,0,0,10));
        numberPreviouslyCaught.getChildren().addAll(numberCaught,numberCaughtField);

        Label backgroundColorLabel = new Label("Background: ");
        backgroundColorPicker.setDisable(!windowStage.isShowing());
        if(windowLayout.getBackground() != null)
            backgroundColorPicker.setValue((Color) windowLayout.getBackground().getFills().get(0).getFill());

        HBox backgroundColor = new HBox();
        backgroundColor.setAlignment(Pos.CENTER);
        backgroundColor.setSpacing(5);
        backgroundColor.setPadding(new Insets(10,0,0,10));
        backgroundColor.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);

        Label layoutLabel = new Label("Layout: ");
        Button loadLayout = new Button("Load");
        Button saveLayout = new Button("Save");

        HBox layoutSettings = new HBox();
        layoutSettings.setAlignment(Pos.CENTER);
        layoutSettings.setSpacing(5);
        layoutSettings.setPadding(new Insets(10,0,0,10));
        layoutSettings.getChildren().addAll(layoutLabel, saveLayout, loadLayout);

        previouslyCaughtSettingsLayout = new VBox();
        previouslyCaughtSettingsLayout.setAlignment(Pos.TOP_CENTER);
        previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, backgroundColor, layoutSettings);

        if(displayCaught != 0) {
            addPreviouslyCaughtPokemon(displayCaught);
        }

        ScrollPane scrollPane = new ScrollPane(previouslyCaughtSettingsLayout);

        Scene previouslyCaughtSettingsScene = new Scene(scrollPane, 263, 500);
        previouslyCaughtSettingsStage.setScene(previouslyCaughtSettingsScene);
        previouslyCaughtSettingsStage.show();

        if(currentLayout != null) {
            SaveData data = new SaveData();
            data.loadLayout("Previously-Caught/" + currentLayout, windowLayout);
        }

        numberCaughtField.setOnAction(e ->{
            try{
                if(displayCaught == 0)
                    createPreviouslyCaughtPokemonWindow();
                displayPrevious = displayCaught;
                displayCaught = parseInt(numberCaughtField.getText());
                if(displayCaught == 0) {
                    windowStage.close();
                    backgroundColorPicker.setDisable(true);
                    previouslyCaughtSettingsLayout.getChildren().remove(3, previouslyCaughtSettingsLayout.getChildren().size());
                }
                else {
                    windowStage.show();
                    addPreviouslyCaughtPokemon(displayCaught);
                }
                numberCaughtField.setText("");
                numberCaughtField.setPromptText(String.valueOf(displayCaught));
            }catch(NumberFormatException f){
                numberCaughtField.setText("");
            }
        });

        backgroundColorPicker.setOnAction(e -> windowLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

        saveLayout.setOnAction(e -> saveLayout());
        loadLayout.setOnAction(e -> loadLayout());
    }

    //create elements of the last x previously caught pokemon
    public void addPreviouslyCaughtPokemon(int previouslyCaught){
        if(previouslyCaught < displayPrevious){
            windowLayout.getChildren().remove(previouslyCaught * 4, windowLayout.getChildren().size());
            previouslyCaughtSettingsLayout.getChildren().remove(previouslyCaught * 5 + 3, previouslyCaughtSettingsLayout.getChildren().size());
        }
        SaveData data = new SaveData();
        int numberCaught = data.getfileLength("CaughtPokemon");
        if(numberCaught < previouslyCaught)
            previouslyCaught = numberCaught;
        for(int i = numberCaught - 1; i >= (numberCaught - previouslyCaught); i--){
            if((i - numberCaught) * -1 > displayPrevious) {
                String line = data.getLinefromFile(i, "CaughtPokemon");
                Game caughtGame = new Game(data.splitString(line, 1), parseInt(data.splitString(line, 2)));
                selectionPageController findGeneration = new selectionPageController();
                int selectedPokemonGeneration = findGeneration.findGenerationPokemon(data.splitString(line, 0));
                if (caughtGame.getGeneration() < findGeneration.findGenerationPokemon(data.splitString(line, 0)))
                    caughtGame.setGeneration(selectedPokemonGeneration);
                ImageView sprite = createPokemonSprite(data.splitString(line, 0), caughtGame);
                Text pokemon = new Text(data.splitString(line, 0));
                Text method = new Text(data.splitString(line, 3));
                Text encounters = new Text(data.splitString(line, 5));

                windowLayout.getChildren().addAll(sprite, pokemon, method, encounters);

                pokemon.setStroke(Color.web("0x00000000"));
                method.setStroke(Color.web("0x00000000"));
                encounters.setStroke(Color.web("0x00000000"));

                sprite.setLayoutX(50 * (numberCaught - i));
                sprite.setLayoutY(50);

                pokemon.setLayoutX(50 * (numberCaught - i));
                pokemon.setLayoutY(75);

                method.setLayoutX(50 * (numberCaught - i));
                method.setLayoutY(90);

                encounters.setLayoutX(50 * (numberCaught - i));
                encounters.setLayoutY(105);

                quickEdit(sprite);
                quickEdit(pokemon);
                quickEdit(method);
                quickEdit(encounters);

                VBox spriteSettings = createImageSettings(sprite, data.splitString(line, 0), caughtGame);
                VBox pokemonLabelSettings = createLabelSettings(pokemon, "Pokemon");
                VBox methodLabelSettings = createLabelSettings(method, "Method");
                VBox encountersLabelSettings = createLabelSettings(encounters, "Encounters");
                previouslyCaughtSettingsLayout.getChildren().addAll(new Text("-------------------------------------------"), spriteSettings, pokemonLabelSettings, methodLabelSettings, encountersLabelSettings);
            }
        }
    }

    //save layout
    public void saveLayout(){
        SaveData data = new SaveData();
        Stage promptLayoutSaveName = new Stage();
        promptLayoutSaveName.initModality(Modality.APPLICATION_MODAL);
        promptLayoutSaveName.setResizable(false);

        if(currentLayout != null) {
            promptLayoutSaveName.setTitle("Save Layout");

            VBox selectNewSaveLayout = new VBox();
            selectNewSaveLayout.setAlignment(Pos.CENTER);
            selectNewSaveLayout.setSpacing(5);
            Label newNameLabel = new Label("Would you like to save this layout to a new name?");

            HBox buttons = new HBox();
            buttons.setSpacing(10);
            buttons.setAlignment(Pos.CENTER);
            Button newSaveButton = new Button("New");
            Button oldSaveButton = new Button("Update");
            buttons.getChildren().addAll(newSaveButton, oldSaveButton);

            selectNewSaveLayout.getChildren().addAll(newNameLabel, buttons);

            Scene selectNewSaveScene = new Scene(selectNewSaveLayout, 275, 75);
            promptLayoutSaveName.setScene(selectNewSaveScene);
            promptLayoutSaveName.show();

            newSaveButton.setOnAction(f -> {
                promptLayoutSaveName.setTitle("Select Layout Name");

                VBox saveLayoutNameLayout = new VBox();
                saveLayoutNameLayout.setSpacing(10);
                saveLayoutNameLayout.setAlignment(Pos.CENTER);

                Label saveNameLabel = new Label("What would you like this layout to be called?");
                TextField saveNameText = new TextField();

                saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
                Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
                promptLayoutSaveName.setScene(saveLayoutNameScene);
                promptLayoutSaveName.show();

                saveNameText.setOnAction(g -> {
                    String text = saveNameText.getText();
                    if (text.contains("\\") || text.contains("/") || text.contains(":") || text.contains("*") || text.contains("?") || text.contains("\"") || text.contains("<") || text.contains(">") || text.contains("|") || text.contains(".")) {
                        saveNameText.setText("");
                    } else {
                        data.saveLayout("Previously-Caught/" + saveNameText.getText(), windowLayout, true);
                        promptLayoutSaveName.close();
                    }
                });
            });

            oldSaveButton.setOnAction(f -> {
                data.saveLayout("Previously-Caught/" + currentLayout, windowLayout, false);
                promptLayoutSaveName.close();
            });
        }else{
            promptLayoutSaveName.setTitle("Select Layout Name");

            VBox saveLayoutNameLayout = new VBox();
            saveLayoutNameLayout.setSpacing(10);
            saveLayoutNameLayout.setAlignment(Pos.CENTER);

            Label saveNameLabel = new Label("What would you like this layout to be called?");
            TextField saveNameText = new TextField();

            saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
            Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
            promptLayoutSaveName.setScene(saveLayoutNameScene);
            promptLayoutSaveName.show();

            saveNameText.setOnAction(g -> {
                String text = saveNameText.getText();
                if (text.indexOf('\\') > -1 || text.indexOf('/') > -1 || text.indexOf(':') > -1 || text.indexOf('*') > -1 || text.indexOf('?') > -1 || text.indexOf('\"') > -1 || text.indexOf('<') > -1 || text.indexOf('>') > -1 || text.indexOf('|') > -1 || text.indexOf('.') > -1) {
                    saveNameText.setText("");
                } else {
                    data.saveLayout("Previously-Caught/" + saveNameText.getText(), windowLayout, true);
                    promptLayoutSaveName.close();
                }
            });
        }
    }

    //load layout
    public void loadLayout(){
        SaveData data = new SaveData();

        Stage loadSavedLayoutStage = new Stage();
        loadSavedLayoutStage.initModality(Modality.APPLICATION_MODAL);
        loadSavedLayoutStage.setResizable(false);
        loadSavedLayoutStage.setTitle("Select Layout Name");

        TreeView<String> savedLayouts = new TreeView<>();
        TreeItem<String> root = new TreeItem<>();
        for(int i = 0; i < data.getfileLength("Layouts/Previously-Caught/~Layouts"); i++){
            makeBranch(data.getLinefromFile(i, "Layouts/Previously-Caught/~Layouts"), root);
        }
        savedLayouts.setRoot(root);
        savedLayouts.setShowRoot(false);
        savedLayouts.setPrefWidth(300);
        savedLayouts.setPrefWidth(500);

        VBox savedLayoutsLayout = new VBox();
        savedLayoutsLayout.setSpacing(10);
        savedLayoutsLayout.setAlignment(Pos.CENTER);
        savedLayoutsLayout.getChildren().add(savedLayouts);

        Scene savedLayoutsScene = new Scene(savedLayoutsLayout, 300, 400);
        loadSavedLayoutStage.setScene(savedLayoutsScene);
        loadSavedLayoutStage.show();

        savedLayouts.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    currentLayout = newValue.toString().substring(18, String.valueOf(newValue).length() - 2);
                    displayCaught = parseInt(data.getLinefromFile(data.getfileLength("Layouts/Previously-Caught/" + currentLayout) - 1, "Layouts/Previously-Caught/" + currentLayout));
                    createPreviouslyCaughtPokemonWindow();
                    previouslyCaughtPokemonSettings();
                    data.loadLayout("Previously-Caught/" + currentLayout, windowLayout);
                    loadSavedLayoutStage.close();
                });
    }

    public Stage getSettingsStage(){
        return previouslyCaughtSettingsStage;
    }
}

class newOrOld extends window{
    huntController controller;

    public void newOrOldHunt(){
        Stage loadStage = new Stage();

        Label prompt1 = new Label("Would you like to continue a previous hunt or");
        Label prompt2 = new Label("start a new one?");
        Button continuePrevious = new Button("Continue Previous Hunt");
        Button newHunt = new Button("Start New Hunt");

        VBox loadLayout = new VBox();
        loadLayout.getChildren().addAll(prompt1, prompt2, continuePrevious, newHunt);
        loadLayout.setSpacing(10);
        loadLayout.setAlignment(Pos.CENTER);

        Scene loadScene = new Scene(loadLayout, 275, 150);
        loadStage.setTitle("Load previous save");
        loadStage.setResizable(false);
        loadStage.setScene(loadScene);
        loadStage.show();

        //show the previously created Selection Page Window
        newHunt.setOnAction(e -> {
            //creates selection page window
            try {
                //creates selection page window
                FXMLLoader selectionPageLoader = new FXMLLoader();
                selectionPageLoader.setLocation(getClass().getResource("selectionPage.fxml"));
                Parent root = selectionPageLoader.load();

                windowStage.setTitle("Shiny Hunt Tracker");
                windowStage.setResizable(false);
                windowStage.setScene(new Scene(root, 750, 480));

                selectionPageController selectionPageController = selectionPageLoader.getController();
                selectionPageController.setController(controller);
                selectionPageController.setCurrentLayout(currentLayout);
                windowStage.show();
            }catch(IOException f){
                f.printStackTrace();
            }
            loadStage.close();
        });

        //if they would like to continue, show a list of the previous hunts found on the file
        continuePrevious.setOnAction(e -> {
            windowStage.setTitle("Select a previous hunt");
            TreeView<String> previousHuntsView = new TreeView<>();
            TreeItem<String> previousHuntsRoot = new TreeItem<>();

            SaveData previousHuntData = new SaveData();

            for(int i = 0; i < previousHuntData.getfileLength("PreviousHunts"); i++){
                String line = previousHuntData.getLinefromFile(i, "PreviousHunts");
                String name = previousHuntData.splitString(line, 0);
                String game = previousHuntData.splitString(line, 1);
                String method = previousHuntData.splitString(line, 3);
                String encounters = previousHuntData.splitString(line, 5);
                makeBranch((i+1) + ") " + name + " | " + game + " | " + method + " | " + encounters + " encounters", previousHuntsRoot);
            }

            previousHuntsView.setRoot(previousHuntsRoot);
            previousHuntsView.setShowRoot(false);

            VBox previousHuntsLayout = new VBox();
            previousHuntsLayout.getChildren().addAll(previousHuntsView);

            Scene previousHuntsScene = new Scene(previousHuntsLayout, 300, 400);
            windowStage.setScene(previousHuntsScene);
            windowStage.show();
            loadStage.close();

            //skip selection window and go straight to hunt page
            previousHuntsView.getSelectionModel().selectedItemProperty()
                    .addListener((v, oldValue, newValue) -> {
                        String line = newValue.toString().substring(18);
                        previousHuntData.loadHunt(parseInt(line.substring(0, line.indexOf(')'))) - 1, controller);
                        windowStage.close();
                    });
        });
    }

    public void setController(huntController controller){
        this.controller = controller;
    }
}