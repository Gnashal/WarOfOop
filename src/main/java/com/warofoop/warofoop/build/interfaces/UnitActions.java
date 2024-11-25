package com.warofoop.warofoop.build.interfaces;

import com.warofoop.warofoop.build.Unit;

public interface UnitActions {
    void move(int x, int y); // For movement
    void attack(Unit target); // For attacking another unit
    void takeDamage(float damage); // For receiving damage
    void playAnimation(String animationName);
}
