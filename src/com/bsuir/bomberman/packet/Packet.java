package com.bsuir.bomberman.packet;

import com.bsuir.bomberman.online.client.GameClient;
import com.bsuir.bomberman.online.server.GameServer;

public abstract class Packet {


    public enum PacketType {
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02),BOMB(03);;
        private int packetId;
        PacketType(int pocketId) {
            this.packetId = pocketId;
        }

        public int getPacketId() {
            return packetId;
        }
    }

    public byte pocketId;

    public Packet(int id) {
        this.pocketId = (byte) id;
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();

    public static PacketType lookupPocket(int id) {
        for (PacketType p : PacketType.values()) {
            if (p.getPacketId() == id) {
                return p;
            }
        }
        return PacketType.INVALID;
    }

    public static PacketType lookupPocket(String id) {
        try {
            return lookupPocket(Integer.parseInt(id));

        } catch (NumberFormatException e) {
            return PacketType.INVALID;
        }
    }

}