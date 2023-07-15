package com.example.pi_movil.comunicacion;

import java.io.IOException;
import java.net.Socket;

public class Session {
    private static Socket socket;
    private static String ip;
    private static int port;


    public Session() throws IOException{

            socket = new Socket(ip, port);

    }

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket s){
        socket = s;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Session.ip = ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Session.port = port;
    }
}