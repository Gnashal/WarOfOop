package com.warofoop.warofoop.build;

import javafx.stage.FileChooser;
import java.io.File;

public class Image {
    private String imgName;

    public String getImgName(){
        return this.imgName;
    }

    public void setImageName(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            imgName = file.getName();
            System.out.println("Test added: " + imgName);
        }
    }
}
