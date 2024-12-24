package com.warofoop.warofoop.build;

import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Timeline;

public class Troll extends Unit {
    public Troll(double x, double y) {
        super(7.0f, 5, 9.0f, 14, 70.0f);
        setX(x);
        setY(y);
        setW(50);
        setH(50);
    }

    @Override
    public String toString() {
        return "Troll{" +
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
