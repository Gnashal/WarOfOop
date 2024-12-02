package com.warofoop.warofoop.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Player {
    private String name;
    private float currhealth;
    private float maxhealth;
    private int gold;
    private int threshold;
    private int unitcount;
    private int currCap;
    private List<Unit> units;
    private Queue<Unit> spawnQueue;

    public Player(float maxhealth, int gold, String name, int threshold) {
        this.currhealth = maxhealth;
        this.gold = gold;
        this.maxhealth = maxhealth;
        this.name = name;
        this.threshold = threshold;
        this.units = new ArrayList<>();
        this.spawnQueue = new LinkedList<>();
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
    public Queue<Unit> getSpawnQueue() {
        return spawnQueue;
    }
    public int getCurrCap() { return currCap; }
    public void changeCurrCap(int addCap){ this.currCap += addCap; }

    public void addToSpawnQueue(Unit unit) {
        spawnQueue.add(unit);
    }

    public Unit processNextSpawn() {
        return spawnQueue.poll();
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

