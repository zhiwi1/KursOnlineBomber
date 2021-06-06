package com.bsuir.bomberman.packet;

import com.bsuir.bomberman.online.client.GameClient;
import com.bsuir.bomberman.online.server.GameServer;

public class Packet03Bomb extends Packet {

    private int x;
    private int y;


    public Packet03Bomb(byte[] data) {
        super(03);
        String[] dataArray = readData(data).split(",");
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);

    }

    public Packet03Bomb( int x, int y) {
        super(03);
        this.x = x;
        this.y = y;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }


    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }


    @Override
    public byte[] getData() {
        return ("03," + this.x + "," + this.y ).getBytes();

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }


}

