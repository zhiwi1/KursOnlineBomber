package com.bsuir.bomberman.level.tile;


import com.bsuir.bomberman.graphics.Screen;
import com.bsuir.bomberman.graphics.Sprite;

public class FloorTile extends Tile {

    public FloorTile(Sprite sprite) {
        super(sprite);
        removed = false;
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 6, y << 6, this);
    }

    public boolean solid() {
        return false;
    }

    public boolean isRemove() {
        return removed;
    }

}
