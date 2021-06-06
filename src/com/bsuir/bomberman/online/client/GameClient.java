package com.bsuir.bomberman.online.client;

import com.bsuir.bomberman.Game;
import com.bsuir.bomberman.entity.bomb.Bomb;
import com.bsuir.bomberman.entity.mob.PlayerMp;
import com.bsuir.bomberman.graphics.Sprite;
import com.bsuir.bomberman.packet.*;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;

    public GameClient(Game game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Start Client");
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    public void sendData(byte[] data) {
        System.out.println(2);
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketType type = Packet.lookupPocket(message.substring(0, 2));
        System.out.println("Client parsePacket");
        Packet packet = null;
        switch (type) {
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                handleLogin((Packet00Login) packet, address, port);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                        + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
                game.level.removePlayerMP(((Packet01Disconnect) packet).getUsername());
                break;
            case MOVE:
                packet = new Packet02Move(data);
                handleMove((Packet02Move) packet);
                break;
            case BOMB:
                packet = new Packet03Bomb(data);
                handleBomb((Packet03Bomb) packet);
                break;
        }

    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername()
                + " has joined the game...");
        PlayerMp player = new PlayerMp(packet.getX(), packet.getY(), game.level, address, port, packet.getUsername());
        game.level.addEntity(player);
    }

    private void handleMove(Packet02Move packet) {
        this.game.level.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getNumSteps(),
                packet.isMoving(), packet.getMovingDir());
    }

    private void handleBomb(Packet03Bomb packet) {
        this.game.level.addBomb(new Bomb(packet.getX(), packet.getY(), Sprite.bomb_1, game.level, 5));
    }

}

