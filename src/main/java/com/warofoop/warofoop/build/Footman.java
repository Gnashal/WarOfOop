package com.warofoop.warofoop.build;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Timeline;

public class Footman extends Unit {
    public Footman(double x, double y) {
        super(10.0f, 4, 10.0f, 18, 100.0f);
        setX(x);
        setY(y);
        setW(50);
        setH(50);
    }

    @Override
    public String toString() {
        return "Footman{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", width=" + getW() +
                ", height=" + getH() +
                ", health=" + getHealth() +
                '}';
    }

    @Override
    public int movementSpeed() {
        return (int) (500 + (getUnitSize() * getArmor()));
    }

    @Override
    public void attack(Unit target) {
        target.setHealth(target.getHealth() - this.getBaseDamage());
    }

    @Override
    public void takeDamage(float damage) {
        this.setHealth(this.getHealth() - damage);
    }
}
