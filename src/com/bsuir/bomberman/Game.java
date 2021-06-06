package com.bsuir.bomberman;

import com.bsuir.bomberman.entity.mob.Player;

import com.bsuir.bomberman.entity.mob.PlayerMp;
import com.bsuir.bomberman.graphics.Screen;
import com.bsuir.bomberman.handler.WindowHandler;
import com.bsuir.bomberman.input.Keyboard;
import com.bsuir.bomberman.level.Level;
import com.bsuir.bomberman.online.client.GameClient;
import com.bsuir.bomberman.online.server.GameServer;
import com.bsuir.bomberman.packet.Packet00Login;



import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;



public class Game extends Canvas implements Runnable {
    public static Game game;
    public WindowHandler windowHandler;
    private static final long serialVersionUID = 1L;
    public JFrame frame;
    private int width = 960, height = 540;
    private int scale = 1;
    private Dimension size;
    private String title = "Bomberman";
    private Thread thread;
    private boolean running = false;
    private int levelCounter = 0;

    private Screen screen;
    public Keyboard input;
    public Player player;
    public Level level;
    private GameFont font;


    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public GameClient gameClient;
    private GameServer gameServer;

    public Game() {
        size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        frame = new JFrame();
        screen = new Screen(width, height);
        input = new Keyboard();
        setLevel();
        font = GameFont.completed;
        addKeyListener(input);

    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();

        if (JOptionPane.showConfirmDialog(this, "Do you want run the server") == 0) {
            gameServer = new GameServer(this);
            gameServer.start();
        }
        gameClient = new GameClient(this, "localhost");
        gameClient.start();

        Packet00Login loginPacket = new Packet00Login(player.getUsername());
        if (gameServer != null) {
            gameServer.addConnection((PlayerMp) player, loginPacket);
            System.out.println("gameserver not null");
        }
        loginPacket.writeData(gameClient);
    }


    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;
        game=this;
        requestFocus();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;

            if ((System.currentTimeMillis() - timer) > 1000) {
                frame.setTitle(title + " Alpha - " + updates + " ups , " + frames + " fps");
                frames = 0;
                updates = 0;
                timer += 1000;
            }
        }

    }

    public void update() {
        if (player.isAlive) {
            input.update();
            level.update();
            player.update();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        if (level.isComplete()) {
            screen.clear();
            level.render(screen);
            player.render(screen);
            font = GameFont.completed;
            font.render(screen);
            setLevel();
        } else if (player.isAlive) {
            screen.clear();
            level.render(screen);
            player.render(screen);
        } else {
            screen.clear();
            level.render(screen);
            player.render(screen);
            font = GameFont.gameover;
            font.render(screen);
        }

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();


    }

    private void setLevel() {
        windowHandler=new WindowHandler(this);
        levelCounter++;
        level = new Level("/textures/levels/Level" + levelCounter + ".png", levelCounter);
        level.addCreeps();

        player = new PlayerMp(1, 1, input, level,
                null, -1, JOptionPane.showInputDialog("Please enter a username"));
        level.addEntity(player);

    }

    public static void main(String[] args) {


        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle(game.title + " Alpha");

        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        game.start();

    }

}





