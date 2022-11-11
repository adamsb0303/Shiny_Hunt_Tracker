package shinyhunttracker;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class FetchImage extends Thread{
    /**
     * Loads image to given sprite
     * @param sprite ImageView for new image
     * @param selectedPokemon Pokemon to get sprite for
     * @param selectedGame Game to get sprite from
     */
    public static Image getImage(ProgressIndicator progress, ImageView sprite, Pokemon selectedPokemon, Game selectedGame){
        //Creates filepath of were pokemon's sprite is
        String filePath = "Images/Sprites/" + selectedGame.getImagePath() + "/";
        if(selectedPokemon.getFormId() > 0)
            filePath += "alt/" + selectedPokemon.getDexNumber() + "-" + selectedPokemon.getFormId() + ".gif";
        else
            filePath += selectedPokemon.getDexNumber() + ".gif";

        //If its saved locally, use it. If not, pull from github
        try {
            FileInputStream input = new FileInputStream(filePath);
            filePath = "file:" + filePath;
        }catch (FileNotFoundException e){
            filePath = "https://github.com/adamsb0303/Shiny_Hunt_Tracker/blob/Development/" + filePath + "?raw=true";
        }

        Image image = new Image(filePath, true);

        progress.progressProperty().bind(image.progressProperty());
        image.progressProperty().addListener(e -> {
            if (image.getProgress() != 1)
                return;

            if(image.getWidth() == 0) {
                sprite.setImage(new Image("https://github.com/adamsb0303/Shiny_Hunt_Tracker/blob/Development/Images/Sprites/" + selectedGame.getImagePath() + "/sub.gif?raw=true"));
            }else
                sprite.setImage(image);

            //Sets image and adjusts scale
            adjustImageScale(sprite);
        });

        return image;
    }

    /**
     * Adjusts image to fit within sprites fit height and width
     * @param sprite ImageView to adjust
     */
    public static void adjustImageScale(ImageView sprite){
        Image image = sprite.getImage();
        //Image should fit within 200x200 square by default
        if(sprite.getFitWidth() == 0 || sprite.getFitHeight() == 0){
            sprite.setFitWidth(-200);
            sprite.setFitHeight(-200);
        }
        if(image.getWidth() != -sprite.getFitWidth() || image.getHeight() != -sprite.getFitHeight()){
            double width = image.getWidth();
            double height = image.getHeight();
            double scale;
            //Finds scale that would make image fit within fit size (saved as negative)
            if((-sprite.getFitHeight() / height) * width <= -sprite.getFitWidth())
                scale = -sprite.getFitHeight() / height;
            else
                scale = -sprite.getFitWidth() / width;
            scale =  Math.round(scale * 100) / 100.0;
            sprite.setScaleX(scale);
            sprite.setScaleY(scale);
        }else{
            sprite.setScaleX(1);
            sprite.setScaleY(1);
        }
        //makes x and y location = bottom center of image
        sprite.setTranslateX(-image.getWidth() / 2);
        sprite.setTranslateY(-((image.getHeight() / 2) + (image.getHeight() * sprite.getScaleX()) / 2));
    }
}
