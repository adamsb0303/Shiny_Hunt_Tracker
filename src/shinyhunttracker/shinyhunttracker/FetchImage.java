package shinyhunttracker;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class FetchImage extends Thread{

    public static void setImage(ImageView sprite, Pokemon selectedPokemon, Game selectedGame){
        String filePath = "Images/Sprites/" + selectedGame.getImagePath() + "/";
        if(selectedPokemon.getFormId() > 0)
            filePath += "alt/" + selectedPokemon.getDexNumber() + "-" + selectedPokemon.getFormId() + ".gif";
        else
            filePath += selectedPokemon.getDexNumber() + ".gif";

        try {
            FileInputStream input = new FileInputStream(filePath);
            filePath = "file:" + filePath;
        }catch (FileNotFoundException e){
            filePath = "https://github.com/adamsb0303/Shiny_Hunt_Tracker/blob/Development/" + filePath + "?raw=true";
        }

        Image image = new Image(filePath, true);
        image.progressProperty().addListener(e -> {
            if (image.getProgress() != 1)
                return;

            sprite.setImage(image);
            adjustImageScale(sprite, image);
        });
    }

    public static void adjustImageScale(ImageView sprite, Image image){
        if(sprite.getFitWidth() == 0 || sprite.getFitHeight() == 0){
            sprite.setFitWidth(-200);
            sprite.setFitHeight(-200);
        }
        if(image.getWidth() != -sprite.getFitWidth() && image.getHeight() != -sprite.getFitHeight()){
            double width = image.getWidth();
            double height = image.getHeight();
            double scale;
            if((-sprite.getFitHeight() / height) * width <= -sprite.getFitWidth())
                scale = -sprite.getFitHeight() / height;
            else
                scale = -sprite.getFitWidth() / width;
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
        }else{
            sprite.setScaleX(1);
            sprite.setScaleY(1);
        }
        //imageViewFitAdjust(sprite);
        sprite.setTranslateX(-image.getWidth() / 2);
        sprite.setTranslateY(-((image.getHeight() / 2) + (image.getHeight() * sprite.getScaleX()) / 2));
    }

    //adjust ImageView fit
    public static void imageViewFitAdjust(ImageView image){
        double newWidth = image.getImage().getWidth() * image.getScaleX();
        double newHeight = image.getImage().getHeight() * image.getScaleY();
        if(newWidth > -image.getFitWidth())
            image.setFitWidth(-image.getImage().getWidth() * image.getScaleX());
        if(newHeight > -image.getFitHeight())
            image.setFitHeight(-image.getImage().getHeight() * image.getScaleY());
    }
}
