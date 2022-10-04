package shinyhunttracker;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

class FetchImage extends Thread{
    String filePath;

    FetchImage(String filePath){
        this.filePath = filePath;
    }

    public void setImage(TreeItem<String> item){
        try {
            Image image = new Image(new FileInputStream(filePath));
            ImageView sprite = new ImageView(image);
            item.setGraphic(sprite);
        }catch(FileNotFoundException e){
            System.out.println("image at " + filePath + " not found");
        }
    }

    public void setImage(ImageView sprite, String selectedPokemon, Game selectedGame){
        try {
            FileInputStream input = new FileInputStream(filePath);
            Image image = new Image(input);
            sprite.setImage(image);
            adjustImageScale(sprite, image);
        }catch (FileNotFoundException e){
            System.out.println("Image at " + filePath + " not found");
            String imageUrl = "";
            try {
                //imageUrl = getImageURL(selectedPokemon, selectedGame);
                URL url = new URL(imageUrl);
                WritableImage test = new WritableImage(1,1);
                Image image = SwingFXUtils.toFXImage(ImageIO.read(url), test);
                sprite.setImage(image);
                adjustImageScale(sprite, image);
            }catch(IOException f){
                System.out.println("Could not reach " + imageUrl);
                try {
                    sprite.setImage(new Image(new FileInputStream("Images/Sprites/blank.png")));
                }catch (IOException g){
                    System.out.println("Could not find placeholder");
                }
            }
        }
    }

    public String getImageURL(Pokemon selectedPokemon, Game selectedGame){
        int pokemonDexNumber = selectedPokemon.getDexNumber();
        int numberPlace = 0;
        int tempdex = pokemonDexNumber;
        while(tempdex != 0){
            numberPlace++;
            tempdex = tempdex / 10;
        }
        String dexNumber = "000".substring(0, 3 - numberPlace) + pokemonDexNumber;

        String gameAbbreviation;
        switch(selectedGame.getGeneration()){
            case 1:
                gameAbbreviation = "Crystal";
                break;
            case 2:
                gameAbbreviation = selectedGame.toString();
                break;
            case 3:
                switch(selectedGame.toString()){
                    case "Ruby":
                    case "Sapphire":
                        gameAbbreviation = "RuSa";
                        break;
                    case "FireRed":
                    case "LeafGreen":
                        gameAbbreviation = "FRLG";
                        break;
                    default:
                        gameAbbreviation = "Em";
                        break;
                }
                break;
            case 4:
                switch(selectedGame.toString()){
                    case "HeartGold":
                    case "SoulSilver":
                        gameAbbreviation = "HGSS";
                        break;
                    default:
                        gameAbbreviation = "DP";
                        break;
                }
                break;
            case 5:
                gameAbbreviation = "BW";
                break;
            case 8:
                gameAbbreviation = "SWSH";
                break;
            default:
                gameAbbreviation = "SM";
                break;
        }

        return "https://www.serebii.net/Shiny/"+ gameAbbreviation +"/"+ dexNumber + ".png";
    }

    public void adjustImageScale(ImageView sprite, Image image){
        if(sprite.getFitWidth() == 0 || sprite.getFitHeight() == 0){
            sprite.setFitWidth(-200);
            sprite.setFitHeight(-200);
        }
        if(image.getWidth() != -sprite.getFitWidth() && image.getHeight() != -sprite.getFitHeight()){
            double height = image.getHeight();
            double width = image.getWidth();
            double scale;
            if(height > width)
                scale = -sprite.getFitHeight() / height;
            else
                scale = -sprite.getFitWidth() / width;
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
        }else{
            sprite.setScaleX(1);
            sprite.setScaleY(1);
        }
        imageViewFitAdjust(sprite);
        sprite.setTranslateX(-image.getWidth() / 2);
        sprite.setTranslateY(-((image.getHeight() / 2) + (image.getHeight() * sprite.getScaleX()) / 2));
    }

    //adjust ImageView fit
    public void imageViewFitAdjust(ImageView image){
        double newWidth = image.getImage().getWidth() * image.getScaleX();
        double newHeight = image.getImage().getHeight() * image.getScaleY();
        if(newWidth > -image.getFitWidth())
            image.setFitWidth(-image.getImage().getWidth() * image.getScaleX());
        if(newHeight > -image.getFitHeight())
            image.setFitHeight(-image.getImage().getHeight() * image.getScaleY());
    }
}
