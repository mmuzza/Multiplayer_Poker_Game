/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server{

    private int portNumber;
    int count = 1;
    int anteWagerBet, plusWagerBet, playWagerBet = 0;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    GuiServer guiSer = new GuiServer();
    Computations compute = new Computations();
    Winnings calculations = new Winnings();
    ArrayList<Integer> cards;
    Boolean queenAndUp = false;


    Server(Consumer<Serializable> call){

        callback = call;
        server = new TheServer();
        server.start();
    }

    void setPortNum(int portNumber){
        this.portNumber = portNumber;
    }
    int getPortNum(){
        return getPortNum();
    }


    public class TheServer extends Thread{

        public void run() { //

            System.out.println("This is port: " + portNumber);

            try(ServerSocket mysocket = new ServerSocket(portNumber);){
                System.out.println("Server is waiting for a client!");

                while(true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);

                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c); // add the client to an array to keep track of it
                    c.start(); // we start the client thread here

                    count++; // numbering the threads

                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }


    class ClientThread extends Thread{

        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        CardDeck shuffledCards = new CardDeck();
        ArrayList<String> SixCards;
        ArrayList<String> clientCards;
        ArrayList<String> computerCards;
        ArrayList<String> gameWinner;

        // give 3 cards to client and give 3 cards to dealer
        void distributeCards(){

            SixCards = new ArrayList<>(); // has 6 cards which we will split to client and dealer
            clientCards = new ArrayList<>();
            computerCards = new ArrayList<>();
            gameWinner = new ArrayList<>();
            queenAndUp = false;

            SixCards.clear();
            clientCards.clear();
            computerCards.clear();
            gameWinner.clear();

            SixCards = shuffledCards.shuffleDeckAndDealCards();

            for(int i = 0; i < 3; i++){
                clientCards.add(SixCards.get(i));
            }

            for(int i = 3; i < 6; i++){
                computerCards.add(SixCards.get(i));
            }

            gameWinner.add(compute.findWinner(clientCards, computerCards)); // get the winner
            queenAndUp = calculations.queenOrHigher(computerCards);

        }

        //-----------------------------------------------

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }


        // update clients function sends message to clients from server
        public void updateClients(String message) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(message);
                }
                catch(Exception e) {}
            }
        }

        // We will use this function to send message to that specific client
        // We will use this function to send shuffled cards to the client
        public void giveCardsToClients(int clientNumber, Serializable sendingInfo) {

            //for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(clientNumber-1);
                try {
                    t.out.writeObject(sendingInfo); // send cards to user
                    System.out.println("Inside out.write");
                    t.out.flush(); // sent immediately
                }
                catch(Exception e) {}
            //}
        }

        public void run(){

            //updateClients("5555");

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }


            while(true) {
                try {
                    Serializable info = in.readObject().toString();

                   if(info.toString().contains("Plays")){ // shuffle deck and give cards
                        distributeCards();
                        giveCardsToClients(count, clientCards);
                        giveCardsToClients(count, computerCards);
                        giveCardsToClients(count, gameWinner);
                        giveCardsToClients(count, queenAndUp);


                        System.out.println("Winner: " + gameWinner.get(0));
                        System.out.println("Clients card: " + clientCards.get(0) + ", " + clientCards.get(1) + ", " + clientCards.get(2));
                        System.out.println("Computer card: " + computerCards.get(0) + ", " + computerCards.get(1) + ", " + computerCards.get(2));
                        if(queenAndUp){
                            System.out.println("Yes queen and up");
                        }
                    }

                   else if(info.toString().contains("Ante wager")){
                       String[] parts = info.toString().split(" "); // Split the string into an array of words
                       String lastWord = parts[parts.length-1];
                       anteWagerBet = Integer.parseInt(lastWord);
                       System.out.println("Ante wager: " + anteWagerBet);
                   }
                   else if(info.toString().contains("Plus wager")){
                       String[] parts = info.toString().split(" "); // Split the string into an array of words
                       String lastWord = parts[parts.length-1];
                       plusWagerBet = Integer.parseInt(lastWord);
                       System.out.println("Plus wager: " + plusWagerBet);
                   }
                   else if(info.toString().contains("Play wager")){
                       String[] parts = info.toString().split(" "); // Split the string into an array of words
                       String lastWord = parts[parts.length-1];
                       playWagerBet = Integer.parseInt(lastWord);
                       System.out.println("Play wager: " + playWagerBet);
                   }
                   else if(info.toString().contains("Winner")){
                       calculations.findWinnerMoney(anteWagerBet, playWagerBet, plusWagerBet, gameWinner.get(0));

                   }


                    callback.accept("client: " + count + ": " + info);
                    //updateClients("client #"+count+" said: "+info);
                }

                catch(Exception e) {
                    callback.accept("Client " + count + " left!");
                    updateClients("Client #"+count+" has left the server!");
                    clients.remove(this);
                    break;
                }
            }
        }//end of run

    }//end of client thread
}






