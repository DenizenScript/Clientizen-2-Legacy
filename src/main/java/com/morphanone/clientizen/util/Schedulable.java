package com.morphanone.clientizen.util;

public abstract class Schedulable implements Runnable {

    public double remainingDelay;

    public boolean run(double delta) {
        remainingDelay -= delta;
        if (remainingDelay <= 0) {
            run();
            return true;
        }
        return false;
    }
}
