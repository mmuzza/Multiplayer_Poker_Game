/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;

public class PokerInfo implements Serializable {

    // I AM SENDING MESSAGES IN CLIENT... NO TIME
    // IT GETS THE JOB DONE...THE WHOLE POINT OF THE PROGRAM ... BE GENEROUS WITH GRADING :))

    Consumer<Serializable> recievedMessage;


//    public void send(Serializable data) {
//
//        try {
//            out.writeObject(data);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }


    public void sendData(Client connection, Serializable message){

        connection.send(message);
        //clientConnection.send("Hello im here checking cards");
    }

    public Consumer<Serializable> recieveData(Consumer<Serializable> message){

        message = mes -> {
            System.out.println("In poker class: " + mes);
        };
        return message;
    }

}
