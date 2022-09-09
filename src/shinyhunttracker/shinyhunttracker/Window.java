package shinyhunttracker;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Window {
    Stage windowStage = new Stage();
    AnchorPane windowLayout = new AnchorPane();
    String[] fonts = generateFonts();
    String currentLayout;

    //create a string of the file path of the game
    public String createGameFilePath(Game selectedGame){
        String filePath = "Images/Sprites/";
        switch(selectedGame.getGeneration()) {
            case 1:
                filePath += "Generation 2/crystal/";
                break;
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
            String[] GMax = {"Alcremie", "Appletun", "Blastoise", "Butterfree", "Centiskorch", "Charizard", "Cinderace", "Coalossal", "Copperajah", "Corviknight", "Drednaw", "Duraludon", "Eevee", "Flapple", "Garbodor", "Gengar", "Grimmsnarl", "Hatterene", "Inteleon", "Kingler", "Lapras", "Machamp", "Melmetal", "Meowth", "Orbeetle", "Pikachu", "Rillaboom", "Sandaconda", "Snorlax", "Toxtricity", "Venusaur"};
            for(String i : GMax)
                if(i.compareTo(name) == 0) {
                    if (comboBox.getItems().size() == 0)
                        comboBox.getItems().add("Regular");
                    comboBox.getItems().add("Gigantamax");
                }
        }

        return comboBox;
    }

    //returns ImageView with the sprite of the given pokemon
    public void setPokemonSprite(ImageView sprite, String name, Game selectedGame){
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
                case "NidoranF":
                    filePath += "nidoran-f";
                    break;
                case "NidoranM":
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

        FetchImage getImage = new FetchImage(filePath);
        getImage.setImage(sprite, name, selectedGame);
    }

    //changes given image to given pokemon variant
    public void setAlternateSprite(Pokemon selectedPokemon, Game selectedGame, ImageView sprite){
        String filePath = createGameFilePath(selectedGame);
        String form = selectedPokemon.getForm();
        String name = selectedPokemon.getName();
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

        FetchImage getImage = new FetchImage(filePath);
        getImage.setImage(sprite, selectedPokemon.getName(), selectedGame);
    }

    //creates ImageView settings VBox
    public VBox createImageSettings(ImageView image, Pokemon pokemon, Game selectedGame){
        HBox groupLabel = new HBox();
        Label Group = new Label(pokemon.getName() + " Sprite:");
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

        HBox imageFit = new HBox();
        imageFit.setSpacing(5);
        Button imageFitButton = new Button("Adjust Image Layout Bounds");
        imageFit.getChildren().add(imageFitButton);

        HBox form = new HBox();
        form.setSpacing(5);
        Label formLabel = new Label("Form");
        ComboBox<String> formCombo = getPokemonForms(pokemon.getName(), selectedGame.getGeneration());
        formCombo.getSelectionModel().select(0);
        form.getChildren().addAll(formLabel, formCombo);

        VBox imageVBox = new VBox();
        imageVBox.setSpacing(10);
        if(formCombo.getItems().size() != 0)
            imageVBox.getChildren().add(form);
        imageVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, visablility, imageFit);

        Accordion accordion = new Accordion();
        TitledPane imageTitledPane = new TitledPane(pokemon.getName() + " Sprite", imageVBox);
        accordion.getPanes().add(imageTitledPane);

        VBox imageSettings = new VBox();
        imageSettings.getChildren().add(accordion);

        formCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                pokemon.setForm(newValue);
                setAlternateSprite(pokemon, selectedGame, image);
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
            image.setTranslateX(-image.getImage().getWidth() / 2);
            image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
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

        imageFitButton.setOnAction(e -> {
            double originalScale = image.getScaleX();

            Rectangle square = new Rectangle();
            drawBoundaryGuide(image, square);
            imageFit.getChildren().remove(0);

            Button saveImageFit = new Button("Save Boundaries");
            Button cancelImageFit = new Button("Cancel");

            imageFit.getChildren().addAll(saveImageFit, cancelImageFit);

            saveImageFit.setOnAction(f -> {
                image.setFitHeight(-square.getHeight() * square.getScaleY());
                image.setFitWidth(-square.getWidth() * square.getScaleX());
                windowLayout.getChildren().remove(windowLayout.getChildren().size() - 13, windowLayout.getChildren().size());
                imageFit.getChildren().remove(0,2);
                imageFit.getChildren().add(imageFitButton);
            });

            cancelImageFit.setOnAction(f -> {
                image.setScaleX(originalScale);
                image.setScaleY(originalScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
                windowLayout.getChildren().remove(windowLayout.getChildren().size() - 13, windowLayout.getChildren().size());
                imageFit.getChildren().remove(0,2);
                imageFit.getChildren().add(imageFitButton);
            });
        });

        return imageSettings;
    }

    //draws square that shows where the fit width and length are
    public void drawBoundaryGuide(ImageView image, Rectangle square){
        square.setHeight(-image.getFitHeight());
        square.setWidth(-image.getFitWidth());
        square.setTranslateX(-square.getWidth() / 2);
        square.setTranslateY(-square.getHeight());
        square.setLayoutX(image.getLayoutX());
        square.setLayoutY(image.getLayoutY());
        square.setOpacity(0.8);
        windowLayout.getChildren().add(square);

        //cornered piece in the top left
        Line verticalTopLeft = new Line();
        verticalTopLeft.setStroke(Color.WHITE);
        verticalTopLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopLeft.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        verticalTopLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(7).divide(8).subtract(7 / 2)));
        verticalTopLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalTopLeft);

        verticalTopLeft.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            verticalTopLeft.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, f.getSceneX(), image);
            });
        });

        Line horizontalTopLeft = new Line();
        horizontalTopLeft.setStroke(Color.WHITE);
        horizontalTopLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalTopLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalTopLeft.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalTopLeft);

        horizontalTopLeft.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            horizontalTopLeft.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, f.getSceneX(), image);
            });
        });

        //top center line
        Line topCenter = new Line();
        topCenter.setStroke(Color.WHITE);
        topCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        topCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        topCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        topCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        topCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(topCenter);

        topCenter.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            topCenter.setOnMouseDragged(f -> adjustY(square, oldHeight, f.getSceneY(), image));
        });

        //cornered piece in the top right
        Line verticalTopRight = new Line();
        verticalTopRight.setStroke(Color.WHITE);
        verticalTopRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalTopRight.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        verticalTopRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(7).divide(8).subtract(7 / 2)));
        verticalTopRight.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalTopRight);

        verticalTopRight.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            verticalTopRight.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image);
            });
        });

        Line horizontalTopRight = new Line();
        horizontalTopRight.setStroke(Color.WHITE);
        horizontalTopRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalTopRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalTopRight.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).subtract(7 / 2)));
        horizontalTopRight.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalTopRight);

        horizontalTopRight.setOnMousePressed(e -> {
            double oldHeight = square.getHeight();
            double oldWidth = square.getWidth();
            horizontalTopRight.setOnMouseDragged(f -> {
                adjustY(square, oldHeight, f.getSceneY(), image);
                adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image);
            });
        });

        //center right line
        Line rightCenter = new Line();
        rightCenter.setStroke(Color.WHITE);
        rightCenter.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        rightCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        rightCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(5).divide(8)));
        rightCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(3).divide(8)));
        rightCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(rightCenter);

        rightCenter.setOnMousePressed(e -> {
            double oldWidth = square.getWidth();
            rightCenter.setOnMouseDragged(f -> adjustX(square, oldWidth, square.getLayoutX() - (f.getSceneX() - square.getLayoutX()), image));
        });

        //cornered piece in the bottom right
        Line verticalBottomRight = new Line();
        verticalBottomRight.setStroke(Color.WHITE);
        verticalBottomRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalBottomRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        verticalBottomRight.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        verticalBottomRight.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).divide(8).subtract(7/2)));
        verticalBottomRight.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalBottomRight);

        Line horizontalBottomRight = new Line();
        horizontalBottomRight.setStroke(Color.WHITE);
        horizontalBottomRight.startXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7 / 2)));
        horizontalBottomRight.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7 / 2)));
        horizontalBottomRight.startYProperty().bind(square.layoutYProperty().subtract(7 / 2));
        horizontalBottomRight.endYProperty().bind(square.layoutYProperty().subtract(7 / 2));
        horizontalBottomRight.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalBottomRight);

        //bottom center line
        Line bottomCenter = new Line();
        bottomCenter.setStroke(Color.WHITE);
        bottomCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        bottomCenter.endXProperty().bind(square.layoutXProperty().add(square.widthProperty().multiply(square.scaleXProperty()).divide(8)));
        bottomCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().divide(64)));
        bottomCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().divide(64)));
        bottomCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(bottomCenter);

        //bottom left corner piece
        Line verticalBottomLeft = new Line();
        verticalBottomLeft.setStroke(Color.WHITE);
        verticalBottomLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        verticalBottomLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        verticalBottomLeft.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        verticalBottomLeft.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).divide(8).subtract(7/2)));
        verticalBottomLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(verticalBottomLeft);

        Line horizontalBottomLeft = new Line();
        horizontalBottomLeft.setStroke(Color.WHITE);
        horizontalBottomLeft.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        horizontalBottomLeft.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).multiply(3).divide(8).subtract(7/2)));
        horizontalBottomLeft.startYProperty().bind(square.layoutYProperty().subtract(7/2));
        horizontalBottomLeft.endYProperty().bind(square.layoutYProperty().subtract(7/2));
        horizontalBottomLeft.setStrokeWidth(7);
        windowLayout.getChildren().add(horizontalBottomLeft);

        //left center line
        Line leftCenter = new Line();
        leftCenter.setStroke(Color.WHITE);
        leftCenter.startXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        leftCenter.endXProperty().bind(square.layoutXProperty().subtract(square.widthProperty().multiply(square.scaleXProperty()).divide(2).subtract(7/2)));
        leftCenter.startYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(5).divide(8)));
        leftCenter.endYProperty().bind(square.layoutYProperty().subtract(square.heightProperty().multiply(square.scaleYProperty()).multiply(3).divide(8)));
        leftCenter.setStrokeWidth(7);
        windowLayout.getChildren().add(leftCenter);

        leftCenter.setOnMousePressed(e -> {
            double oldWidth = square.getWidth();
            leftCenter.setOnMouseDragged(f -> adjustX(square, oldWidth, f.getSceneX(), image));
        });

        image.layoutXProperty().bindBidirectional(square.layoutXProperty());
        image.layoutYProperty().bindBidirectional(square.layoutYProperty());

        square.setOnMousePressed(e -> {
            double diffX = square.getLayoutX() - e.getSceneX();
            double diffY = square.getLayoutY() - e.getSceneY();
            square.setOnMouseDragged(f -> {
                square.setLayoutX(f.getSceneX() + diffX);
                square.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    //adjusts boundary Y
    public void adjustY(Rectangle square, double oldHeight, double mouseLocation, ImageView image){
        if(square.getHeight() * (square.getLayoutY() - mouseLocation) / square.getHeight() >= 20) {
            square.setScaleY((square.getLayoutY() - mouseLocation) / square.getHeight());
            square.setTranslateY(-(square.getHeight() * square.getScaleY() / 2) - oldHeight / 2);
            double newImageHeight = square.getHeight() * square.getScaleY() ;

            double imageScale = newImageHeight / image.getImage().getHeight();
            if(image.getImage().getWidth() * imageScale <= square.getWidth() * square.getScaleX()) {
                image.setScaleY(imageScale);
                image.setScaleX(imageScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
            }
        }
    }

    //adjusts boundary X
    public void adjustX(Rectangle square, double oldWidth, double mouseLocation, ImageView image){
        if(square.getWidth() * (square.getLayoutX() - mouseLocation) / square.getWidth() * 2 >= 20) {
            square.setScaleX((square.getLayoutX() - mouseLocation) / square.getWidth() * 2);
            square.setTranslateX(-oldWidth / 2);

            double newImageWidth = square.getWidth() * square.getScaleX();

            double imageScale = newImageWidth / image.getImage().getWidth();
            if(image.getImage().getHeight() * imageScale <= square.getHeight() * square.getScaleY()) {
                image.setScaleY(imageScale);
                image.setScaleX(imageScale);
                image.setTranslateX(-image.getImage().getWidth() / 2);
                image.setTranslateY(-((image.getImage().getHeight() / 2) + (image.getImage().getHeight() * image.getScaleX()) / 2));
            }
        }
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

    //change location of Text when dragged
    //change scale of Text when scrolled
    public void quickEdit(Text element){
        element.setOnScroll(e -> {
            element.setScaleX(element.getScaleX() + (e.getDeltaY() / 1000));
            element.setScaleY(element.getScaleY() + (e.getDeltaY() / 1000));
        });
        element.setOnMousePressed(e -> {
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    //change location of ImageView when dragged
    //change scale of ImageView when scrolled
    public void quickEdit(ImageView element){
        element.setOnScroll(e -> {
            double scale = element.getScaleX() + (e.getDeltaY() / 1000);
            if(element.getImage().getWidth() * scale > -element.getFitWidth())
                scale = -element.getFitWidth() / element.getImage().getWidth();
            if(element.getImage().getHeight() * scale > -element.getFitHeight())
                scale = -element.getFitHeight() / element.getImage().getHeight();
            element.setScaleX(scale);
            element.setScaleY(scale);
            element.setTranslateX(-element.getImage().getWidth() / 2);
            element.setTranslateY(-((element.getImage().getHeight() / 2) + (element.getImage().getHeight() * element.getScaleX()) / 2));
        });
        element.setOnMousePressed(e -> {
            double diffX = element.getLayoutX() - e.getSceneX();
            double diffY = element.getLayoutY() - e.getSceneY();
            element.setOnMouseDragged(f -> {
                element.setLayoutX(f.getSceneX() + diffX);
                element.setLayoutY(f.getSceneY() + diffY);
            });
        });
    }

    //creates string array of available fonts
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

    public String getCurrentLayout(){
        return currentLayout;
    }

    public void setCurrentLayout(String currentLayout){
        this.currentLayout = currentLayout;
    }
}