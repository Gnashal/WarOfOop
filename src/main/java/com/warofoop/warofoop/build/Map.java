package com.warofoop.warofoop.build;

public class Map extends Image {
    private Image[] map;
    private int max;
    private int count;

    public Map(int max){
        this.max = max;
        this.count = 0;
        this.map = new Image[max];
    }

    public boolean addMap(Image nameImage){
        if(this.count != this.max){
            this.map[this.count++] = nameImage;
            return true;
        }
        return false;
    }

    public boolean removeMap(String name){
        if(this.count > 0){
            for(int i = 0; i < this.count; i++){
                if(map[i].getImgName().equals(name)){
                    for(int j = i; j < this.count - 1; j++){
                        map[j] = map[j + 1];
                    }
                    map[--count] = null;
                    return true;
                }
            }
        }
        return false;
    }
}
