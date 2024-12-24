package com.warofoop.warofoop.build;

import com.warofoop.warofoop.build.interfaces.UnitActions;
import com.warofoop.warofoop.build.interfaces.UnitType;
import javafx.scene.image.ImageView;


public abstract class Unit implements UnitActions {
    //stats for units
    private float baseDamage;
    private float health;
    private float armor;
    private int cost;
    private int unitSize;
    private UnitType unitType;
    private double x;
    private double y;
    private double w;
    private double h;

    public Unit(float armor, int unitSize, float baseDamage, int cost, float health) {
        this.armor = armor;
        this.unitSize = unitSize;
        this.baseDamage = baseDamage;
        this.cost = cost;
        this.health = health;
    }

    public float getBaseDamage() {
        return baseDamage;
    }
    public void setBaseDamage(float baseDamage) {
        this.baseDamage = baseDamage;
    }
    public float getHealth() {
        return health;
    }
    public void setHealth(float health) {
        this.health = health;
    }
    public float getArmor() {
        return armor;
    }
    public void setArmor(float armor) {
        this.armor = armor;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public int getUnitSize() {
        return unitSize;
    }
    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setH(double h) {
        this.h = h;
    }

    public abstract int movementSpeed();

    private ImageView imageView;

    // Getter and Setter for ImageView
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public abstract String toString();
}


