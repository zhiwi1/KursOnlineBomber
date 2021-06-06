package com.bsuir.bomberman.level.tile;

import com.bsuir.bomberman.graphics.Screen;
import com.bsuir.bomberman.graphics.Sprite;
import com.bsuir.bomberman.level.first.firsttile.BorderTile;

public class Tile {

    public Sprite sprite;
    protected boolean removed;

    public static Tile border_tile = new BorderTile(Sprite.border_sprite);
    public static Tile brick_tile = new BrickTile(Sprite.brick_sprite);
    public static Tile floor_tile = new FloorTile(Sprite.floor_sprite);

    public Tile(Sprite sprite) {
        this.sprite = sprite;
    }

    public boolean solid() {
        return false;
    }

    public void render(int x, int y, Screen screen) {

    }

    public boolean breakable() {
        return false;
    }

    public void remove() {

    }

    public boolean isRemove() {
        return removed;
    }

}
