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
}


