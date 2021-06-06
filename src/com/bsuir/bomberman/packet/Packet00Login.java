package com.bsuir.bomberman.packet;

import com.bsuir.bomberman.online.client.GameClient;
import com.bsuir.bomberman.online.server.GameServer;



public class Packet00Login extends Packet {
    private String username;
    private int x;
    private int y;

    public Packet00Login(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
    }

    public Packet00Login(String username, int x, int y) {
        super(00);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    public Packet00Login(String username) {
        super(00);
        this.username = username;
    }

    public String getUsername() {
        return username;
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

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
        System.out.println("write data: " + getData().toString());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username + "," + getX() + "," + getY() ).getBytes();

    }
}
