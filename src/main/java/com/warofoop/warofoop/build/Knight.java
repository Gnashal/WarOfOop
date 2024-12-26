package com.warofoop.warofoop.build;

public class Knight extends Unit {
    public Knight(double x, double y) {
        super(15.0f, 11, 15.0f, 35, 150.0f);
        setX(x);
        setY(y);
        setW(50);
        setH(50);
    }

    @Override
    public String toString() {
        return "Knight{" +
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
