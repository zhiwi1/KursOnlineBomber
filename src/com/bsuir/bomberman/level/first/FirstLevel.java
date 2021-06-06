package com.bsuir.bomberman.level.first;

import com.bsuir.bomberman.level.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FirstLevel extends Level {

    public FirstLevel(String path, int creeps) {
        super(path, creeps);
    }

    public void loadLevel(String path) {
        try {
            BufferedImage image = ImageIO.read(FirstLevel.class.getResource(path));
            int w =((BufferedImage) image).getWidth();
            int h =image.getHeight();
            tiles = new int[w * h];
            image.getRGB(0, 0, w, h, tiles, 0, w);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


}
