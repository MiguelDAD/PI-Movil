package com.example.pi_movil.comunicacion;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

/*

class Tasks extends AsyncTasks<String, Float, String> {
        Socket s;
        PrintWriter out = null;
        BufferedReader in = null;

        @Override
        public void onPreExecute() {
            s = Session.getSocket();
        }

        @Override
        public String doInBackground(String... strings) {
            Log.v("Enviar: ", strings[0]);

            String recibido = "";
            try {
                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                out.println(strings[0]);
                try {
                    recibido = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.v("Recibido del servidor: ", recibido);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "El servidor esta offline", Toast.LENGTH_SHORT).show();
            }

            return recibido;
        }

        @Override
        public void onPostExecute(String respuesta) {

            Log.v("RESPUESTA", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.USERS_MYTEAMS_OK)) {

                }


        }
    }





 */

public class ServerHilo extends Thread {
    private String ipAddress;
    private int port;
    private boolean conectado;
    private Socket socket;

    private Queue<String> respuestas;

    private Queue<String> peticiones;


    public ServerHilo(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        conectado = true;
        respuestas = new LinkedList<>();
        peticiones = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ipAddress, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (conectado) {
                String peticion;
                synchronized (peticiones) {
                    peticion = peticiones.poll();
                    if (peticion == null) {

                        peticiones.wait();

                    } else {
                        synchronized (respuestas) {
                            out.println(peticion);
                            String respuesta = in.readLine();
                            //System.out.println(respuesta);

                            respuestas.add(respuesta);
                            respuestas.notify();
                        }
                    }
                }

            }
        } catch (UnknownHostException e) {
            Log.e("Conexion", "Host desconocido: " + e.getMessage());
        } catch (IOException e) {
            Log.e("Conexion", "Error de I/O: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void enviarMensaje(String peticion) {
        synchronized (peticiones) {
            peticiones.add(peticion);
            peticiones.notify();
        }
    }

    public String recibirRespuesta() {
        synchronized (respuestas) {
            return respuestas.poll();
        }
    }

    public Queue<String> getRespuestas() {
        return respuestas;
    }

    public Queue<String> getPeticiones() {
        return peticiones;
    }

    public String obtenerIp(){
        return socket.getLocalAddress().toString();
    }
}


