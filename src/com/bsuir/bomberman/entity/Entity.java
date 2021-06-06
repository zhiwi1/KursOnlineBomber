package com.bsuir.bomberman.entity;

import com.bsuir.bomberman.graphics.Screen;
import com.bsuir.bomberman.graphics.Sprite;
import com.bsuir.bomberman.level.Level;

public abstract class Entity {

    protected int x;
    protected int y;
    protected boolean removed;
    protected Sprite sprite;
    protected Level level;

    public abstract void update();

    public abstract void remove();

    public abstract void render(Screen screen);

    public boolean isRemoved() {
        return removed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}