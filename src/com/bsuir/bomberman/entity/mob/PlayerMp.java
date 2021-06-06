package com.bsuir.bomberman.entity.mob;

import com.bsuir.bomberman.input.Keyboard;
import com.bsuir.bomberman.level.Level;

import java.net.InetAddress;

public class PlayerMp extends Player {
    private InetAddress inetAddress;
    private int port;


    public PlayerMp(int x, int y, Keyboard input, Level level, InetAddress inetAddress, int port, String username) {
        super(x, y, input, level, username);
        this.inetAddress = inetAddress;
        this.port = port;

    }

    public PlayerMp(int x, int y, Level level, InetAddress address, int port, String username) {
        super(x, y, level);
        this.inetAddress = address;
        this.port = port;
        super.username = username;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
