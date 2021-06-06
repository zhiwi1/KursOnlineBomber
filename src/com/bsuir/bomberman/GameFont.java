package com.bsuir.bomberman;

import com.bsuir.bomberman.graphics.Screen;
import com.bsuir.bomberman.graphics.SpriteSheet;

public class GameFont {
        private String str;
        int x, y;
        private String text;
        public int[] pixels;
        private SpriteSheet font;
        public final int SIZE;

        public static GameFont gameover = new GameFont(480, 270, "GAMEOVER");
        public static GameFont completed = new GameFont(480, 270, "COMPLETED");

        public GameFont(int x, int y, String str) {
            this.str = str;
            this.x = x - (str.length() * 32 / 2);
            this.y = y;
            SIZE = str.length() * 32;
            pixels = new int[SIZE * 32];
            font = SpriteSheet.text;
            text = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
            load();
        }

        public void render(Screen screen) {
            screen.renderText(x, y, this);
        }

        private void load() {
            for(int c = 0; c < str.length(); c++) {
                int textIndex = text.indexOf(str.charAt(c));
                for(int y = 0; y < 32; y++) {
                    for(int x = 0; x < 32; x++) {
                        int index = textIndex * 32 + x;
                        int col = font.pixels[index + y * font.SIZE];
                        pixels[((c * 32) + x) + y * SIZE] = col;
                    }
                }
            }
        }

    }

