package com.bsuir.bomberman.online.server;

import com.bsuir.bomberman.Game;

import com.bsuir.bomberman.entity.mob.PlayerMp;

import com.bsuir.bomberman.packet.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private Game game;
    private List<PlayerMp> connectedPlayers = new ArrayList<PlayerMp>();

    public GameServer(Game game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331);

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        System.out.println("Start Server:");
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        System.out.println("parse packet");
        String message = new String(data).trim();
        Packet.PacketType type = Packet.lookupPocket(message.substring(0, 2));

        Packet packet = null;
        switch (type) {

            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                        + ((Packet00Login) packet).getUsername() + " has connected...");
                PlayerMp player = new PlayerMp(1, 2, game.level, address, port, ((Packet00Login) packet).getUsername());
                //   game.level.addEntity(player);
                this.addConnection(player, (Packet00Login) packet);
                break;

            case INVALID:
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                        + ((Packet01Disconnect) packet).getUsername() + " has left...");
                this.removeConnection((Packet01Disconnect) packet);
                break;
            case MOVE:
                packet = new Packet02Move(data);
                this.handleMove(((Packet02Move) packet));
                break;
            case BOMB:
                packet=new Packet03Bomb(data);
                this.handleBomb((Packet03Bomb) packet);
                break;

        }

    }

    public void addConnection(PlayerMp player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMp p : this.connectedPlayers) {

            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if (p.getInetAddress() == null) {
                    p.setInetAddress(player.getInetAddress());
                }
                if (p.getPort() == -1) {
                    p.setPort(player.getPort());
                }
                alreadyConnected = true;
            } else {
                System.out.println("not equals");
                // relay to the current connected player that there is a new
                // player
                sendData(packet.getData(), p.getInetAddress(), p.getPort());

                // relay to the new player that the currently connect player
                // exists
                int x = p.getX() >> 6;
                int y = p.getY() >> 6;
                packet = new Packet00Login(p.getUsername(), x, y);
                sendData(packet.getData(), player.getInetAddress(), player.getPort());
            }
        }
        if (!alreadyConnected) {

            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }

    public int getPlayerMPIndex(String username) {
        int index = 0;
        for (PlayerMp player : this.connectedPlayers) {
            if (player.getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        System.out.println("server sends data to client");
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendDataToAllClients(byte[] data) {
        System.out.println("server sends data to all clients");
        for (PlayerMp player : connectedPlayers) {
            sendData(data, player.getInetAddress(), player.getPort());
        }

    }

    public PlayerMp getPlayerMP(String username) {
        for (PlayerMp player : this.connectedPlayers) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    private void handleMove(Packet02Move packet) {
        if (getPlayerMP(packet.getUsername()) != null) {
            int index = getPlayerMPIndex(packet.getUsername());
            PlayerMp player = this.connectedPlayers.get(index);
            player.setX( packet.getX());
            player.setY(packet.getY());
            player.setWalking(packet.isMoving());
            player.setDir(packet.getMovingDir());
            player.setNumSteps(packet.getNumSteps());
            packet.writeData(this);
        }
    }
    private void handleBomb(Packet03Bomb packet){
        packet.writeData(this);
    }
}
