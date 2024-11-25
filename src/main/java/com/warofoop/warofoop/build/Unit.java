package com.warofoop.warofoop.build;

import com.warofoop.warofoop.build.interfaces.UnitActions;
import com.warofoop.warofoop.build.interfaces.UnitType;


public abstract class Unit implements UnitActions {
    //stats for units
    private float basedamage;
    private float health;
    private float armor;
    private int cost;
    private UnitType unitType;
    private double x;
    private double y;
    private double w;
    private double h;

    public Unit(float armor, float basedamage, int cost, float health) {
        this.armor = armor;
        this.basedamage = basedamage;
        this.cost = cost;
        this.health = health;
    }


    public float getBasedamage() {
        return basedamage;
    }
    public void setBasedamage(float basedamage) {
        this.basedamage = basedamage;
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

    public boolean collidesWith(Unit other) {
        return this.x < other.x + other.w &&
                this.x + this.w > other.x &&
                this.y < other.y + other.h &&
                this.y + this.h > other.y;
    }
}


