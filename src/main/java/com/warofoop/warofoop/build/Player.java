package com.warofoop.warofoop.build;

public class Player {
    private String name;
    private float currhealth;
    private float maxhealth;
    private int gold;
    private int threshold;
    private int unitcount;

    public Player(float currhealth, int gold, float maxhealth, String name, int threshold, int unitcount) {
        this.currhealth = currhealth;
        this.gold = gold;
        this.maxhealth = maxhealth;
        this.name = name;
        this.threshold = threshold;
        this.unitcount = unitcount;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getCurrhealth() {
        return currhealth;
    }
    public void setCurrhealth(float currhealth) {
        this.currhealth = currhealth;
    }
    public float getMaxhealth() {
        return maxhealth;
    }
    public void setMaxhealth(float maxhealth) {
        this.maxhealth = maxhealth;
    }
    public int getGold() {
        return gold;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public int getThreshold() {
        return threshold;
    }
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
    public int getUnitcount() {
        return unitcount;
    }
    public void setUnitcount(int unitcount) {
        this.unitcount = unitcount;
    }
}

