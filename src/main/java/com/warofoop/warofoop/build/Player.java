package com.warofoop.warofoop.build;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private float currhealth;
    private float maxhealth;
    private int gold;
    private int threshold;
    private int unitcount;
    private List<Unit> units;

    public Player(float maxhealth, int gold, String name, int threshold) {
        this.currhealth = maxhealth;
        this.gold = gold;
        this.maxhealth = maxhealth;
        this.name = name;
        this.threshold = threshold;
        this.units = new ArrayList<>();
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
    public boolean isDefeated() {
        return getCurrhealth() <= 0;
    }
    public void addUnit(Unit unit) {
        units.add(unit);
    }
    public List<Unit> getUnits() {
        return units;
    }

    public void validateHealth() {
        this.currhealth = Math.max(0, Math.min(this.currhealth, this.maxhealth));
    }
    public void changeHealth(float h) {
        this.currhealth += h;
    }
    public void changeGold(int g) {
        this.gold += g;
    }
}

