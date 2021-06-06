package com.bsuir.bomberman.level.first.firsttile;

import com.bsuir.bomberman.graphics.Screen;
import com.bsuir.bomberman.graphics.Sprite;
import com.bsuir.bomberman.level.tile.Tile;

public class BorderTile extends Tile {

    public BorderTile(Sprite sprite) {
        super(sprite);
        removed = false;
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 6, y << 6, this);
    }

    public boolean solid() {
        return true;
    }

    public boolean isRemove() {
        return removed;
    }

}
