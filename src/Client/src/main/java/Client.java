/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
 */

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;


public class Client extends Thread{

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    private int port;
    private String ipAdd;
    int arrayCounter = 0;
    //String message;
    Serializable message;
    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call){

        callback = call;
    }


    // Gets port so gui can use it
    public int getPort() {
        return port;
    }
    // Get IP Address so we can set it in client
    public String getIpAdd() {
        return ipAdd;
    }

    // we set the port from what user picks
    public void setPort(int port) {
        this.port = port;
    }
    // we set the ip address from what user picks
    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }


    public void run() {


    // IP Address: "127.0.0.1"
        try {

            socketClient = new Socket(ipAdd, port);
            out = new ObjectOutputStream(socketClient.getOutputStream()); // sockets out stream
            in = new ObjectInputStream(socketClient.getInputStream()); // sockets in stream
            socketClient.setTcpNoDelay(true);

            System.out.print("here #3\n");

        }
        catch(Exception e) {}


        while(true) {

            try {

                message = (Serializable) in.readObject(); // recieving the message from the server
                callback.accept(message); // take server's message and take it to itself
            }
            catch(Exception e) {}
        }
    }

    public void send(Serializable data) {

        try {
            out.writeObject(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}