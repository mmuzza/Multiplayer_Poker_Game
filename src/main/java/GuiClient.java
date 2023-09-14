/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class GuiClient extends Application{

    TextArea ipAddressText, portNumText;
    Button saveButton;
    Button results, nextHand = new Button();
    Button anteWagerButton, plusWagerButton, finalizeBet; // These are the options to pick from for Ante wager/Plus wager $$
    Button fold, continueToPlay, continueToDealersCards, startGameButton;
    Button[] totalButtons1, totalButtons2;
    int userAnteBet, userPlusBet, userplayWager = 0;
    double totaLWinnings = 0;
    Boolean queenOrHigher = false;
    Scene startScene;
    BorderPane startPane;
    Client clientConnection;
    int counter = 0;
    PokerInfo moveInformation = new PokerInfo();

    ArrayList<String> clientDeck = new ArrayList<>();
    ArrayList<String> computerDeck = new ArrayList<>();
    ArrayList<String> gameWinner = new ArrayList<>();
    private Consumer<Serializable> serverMessage;

    TextArea textTexture(String font, Integer size, String color, String textInfo, Integer width, Integer height) {

        TextArea creatingText = new TextArea();

        creatingText.setWrapText(true);
        creatingText.setEditable(false);
        creatingText.setStyle("-fx-text-alignment: center;");

        creatingText.setFont(Font.font(font, FontWeight.NORMAL, size));
        creatingText.setStyle("-fx-text-alignment: center;"); // centers the text
        creatingText.setStyle(color); // color on text area
        creatingText.setText(textInfo);
        creatingText.setPrefSize(width, height);

        creatingText.setFocusTraversable(false);

        return creatingText;


    }

    //------------

    Button buttonTexture(String name, Integer width, Integer height, String color, String id, Color textColor, String font, Boolean disable) {

        Button creatingButton = new Button(name);
        creatingButton.setPrefSize(width, height);
        creatingButton.setId(id);
        creatingButton.setDisable(disable);
        creatingButton.setStyle(color);
        creatingButton.setTextFill(textColor);
        creatingButton.setStyle(font);

        return creatingButton;
    }

    //------------

    BackgroundImage changeBackground(String userImageName) {

        Image image = new Image(userImageName);

        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        return bgImage;
    }

    //------------

    ImageView imageview(String imageName, int height, int width){
        Image image = new Image(imageName);
        ImageView imageview = new ImageView(image);
        imageview.setFitHeight(height);
        imageview.setFitWidth(width);
        return imageview;
    }

    //-----------

    // this is called when user starts new game
    void getNewDeckOfCardsFromServer(String messageToGetSomethingFromServer){

        clientDeck.clear();
        computerDeck.clear();
        gameWinner.clear();

        counter = 0; // so its ready to recieve new decks
        queenOrHigher = false;

        moveInformation.sendData(clientConnection, messageToGetSomethingFromServer); // get new deck as reply
    }

    //------------

    VBox displayingCards(String deckCard1, String deckCard2, String deckCard3){

        //---------Client Card Handling Below-------------

        TextArea clientCardText = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                "Your Cards", 100, 70);
        //clientCardText.setStyle("-fx-text-alignment: center;");

        HBox clientCard1 = new HBox();
        HBox clientCard2 = new HBox();
        HBox clientCard3 = new HBox();

        clientCard1.setPrefWidth(150);
        clientCard1.setPrefHeight(200);
        clientCard2.setPrefWidth(150);
        clientCard2.setPrefHeight(200);
        clientCard3.setPrefWidth(150);
        clientCard3.setPrefHeight(200);

        clientCard1.getChildren().add(imageview(clientDeck.get(0)+".png", 200, 150));
        clientCard2.getChildren().add(imageview(clientDeck.get(1)+".png", 200, 150));
        clientCard3.getChildren().add(imageview(clientDeck.get(2)+".png", 200, 150));

        HBox clientCards = new HBox(10, clientCard1, clientCard2, clientCard3);
        VBox textAndClient = new VBox(0, clientCardText, clientCards);


        //---------Dealer Card Handling Below-------------

        TextArea dealerCardText = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                "Dealer's Cards", 50, 70);
        HBox dealerCard1 = new HBox();
        HBox dealerCard2 = new HBox();
        HBox dealerCard3 = new HBox();

        dealerCard1.setPrefWidth(150);
        dealerCard1.setPrefHeight(200);
        dealerCard2.setPrefWidth(150);
        dealerCard2.setPrefHeight(200);
        dealerCard3.setPrefWidth(150);
        dealerCard3.setPrefHeight(200);

        dealerCard1.getChildren().add(imageview(deckCard1, 200, 150));
        dealerCard2.getChildren().add(imageview(deckCard2, 200, 150));
        dealerCard3.getChildren().add(imageview(deckCard3, 200, 150));

        HBox dealerCards = new HBox(10, dealerCard1, dealerCard2, dealerCard3);
        VBox textAndDealer = new VBox(0, dealerCardText, dealerCards);

        dealerCards.setAlignment(Pos.CENTER);
        clientCards.setAlignment(Pos.CENTER);

        VBox cardDisplay = new VBox(30, textAndClient, clientCards, textAndDealer, dealerCards);
        cardDisplay.setAlignment(Pos.CENTER);


        return cardDisplay;
    }


    //----------

    EventHandler<ActionEvent> displayResults(Stage primaryStage) {
        EventHandler <ActionEvent> resultingWinner = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Button newGame = buttonTexture("New Game", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
                TextArea winnerMessage = textTexture("Arial", 30, "-fx-control-inner-background: #00805a;",
                        "Game Results: " + gameWinner.get(0) + "\nYour total Winnings are: $" + totaLWinnings, 100, 100);
                VBox displayWinnerBox = new VBox(winnerMessage, newGame);

                Background winnerImage = new Background(changeBackground("bets.jpg"));
                displayWinnerBox.setBackground(winnerImage);

                displayWinnerBox.setAlignment(Pos.CENTER);

                newGame.setOnAction(placeBettings(primaryStage));

                Scene displayWinnerScene = new Scene(displayWinnerBox, 800, 800);
                primaryStage.setScene(displayWinnerScene);

            }
        };
        return resultingWinner;
    }


    //---------

    EventHandler<ActionEvent> displayDealerCards(Stage primaryStage) {
        EventHandler<ActionEvent> dealerCardsVisible = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(queenOrHigher == false){
                    results = buttonTexture("Results", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", true);
                    nextHand = buttonTexture("Next Hand", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
                }
                else {
                    results = buttonTexture("Results", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
                    nextHand = buttonTexture("Next Hand", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", true);
                }

                HBox resultAndNextHand = new HBox(30, nextHand, results);
                resultAndNextHand.setAlignment(Pos.CENTER);

                VBox displayCards = new VBox(30, displayingCards(computerDeck.get(0)+".png", computerDeck.get(1)+".png", computerDeck.get(2)+".png"), resultAndNextHand);
                Background backgroupImage = new Background(changeBackground("bets.jpg"));
                displayCards.setBackground(backgroupImage);

                displayCards.setAlignment(Pos.CENTER);

                if(queenOrHigher == false) { // meaning the dealer does not have queen or higher
                    getNewDeckOfCardsFromServer("Client Plays Hand");
                }
                else{ // we send server message to calculate money
                    moveInformation.sendData(clientConnection, "Evaluating Winner of the game!");
                }

                results.setOnAction(displayResults(primaryStage)); // if dealer has queen or high we do this
                nextHand.setOnAction(displayCardsAndCheckFold(primaryStage));



                Scene displayDealersCardsScene = new Scene(displayCards, 800, 800);
                primaryStage.setScene(displayDealersCardsScene);

            }
        };
        return dealerCardsVisible;
    }

    //----------

    EventHandler<ActionEvent> displayPlayWagerDetails(Stage primaryStage) {
        EventHandler<ActionEvent> playWagerDetails = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                userplayWager = 2 * userAnteBet;
                TextArea playWagerText = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                        "Play Wager has been applied: 2 * $" + userAnteBet + "\nTotal Wagered: $" + userplayWager
                                + "\nIf you win, you will be paid double the wagered amount!\nGoodLuck!", 70, 150);

                moveInformation.sendData(clientConnection, "Placed a Play wager bet of " + userplayWager);

                continueToDealersCards = buttonTexture("Continue", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);

                VBox displayplayWager = new VBox(30, playWagerText, continueToDealersCards);
                Background backgroupImage = new Background(changeBackground("bets.jpg"));
                displayplayWager.setBackground(backgroupImage);

                displayplayWager.setAlignment(Pos.CENTER);

                continueToDealersCards.setOnAction(displayDealerCards(primaryStage)); // when user clicks on continue play

                Scene displayPlayWagerScene = new Scene(displayplayWager, 800, 800);
                primaryStage.setScene(displayPlayWagerScene);
            }
        };
        return playWagerDetails;
    }


    //---------

    EventHandler<ActionEvent> foldSoLost(Stage primaryStage){
        EventHandler<ActionEvent> lostGame = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                TextArea lostMessage = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                        "YOU LOST ALL OF YOUR BETS\nYOUR CURRENT WINNINGS FROM PLUS WAGER IS: $0", 100, 100);
                Button startNewGame = buttonTexture("New Game", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
                VBox putTogether = new VBox(lostMessage, startNewGame);

                BorderPane gameOver = new BorderPane();
                gameOver.setPadding(new Insets(70));
                gameOver.getChildren().add(imageview("gameLost.jpg", 800, 800)); // adding background image
                gameOver.setCenter(putTogether);

                startNewGame.setOnAction(placeBettings(primaryStage));

                Scene clickedFoldScene = new Scene(gameOver, 800, 800);
                primaryStage.setScene(clickedFoldScene);

            }
        };
        return lostGame;
    }

    //--------

    EventHandler<ActionEvent> displayCardsAndCheckFold(Stage primaryStage) {
        EventHandler<ActionEvent> checkFoldAndDisplay = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            //----------Putting images of Client cards and Dealer cards below each other: (And buttons)

            fold = buttonTexture("Fold", 150, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
            continueToPlay = buttonTexture("Continue Playing", 220, 50, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
            HBox buttonsTogether = new HBox(200, fold, continueToPlay);
            buttonsTogether.setAlignment(Pos.CENTER);


            VBox displayCards = new VBox(30, displayingCards("back.jpg", "back.jpg", "back.jpg"), buttonsTogether);
            Background backgroupImage = new Background(changeBackground("bets.jpg"));
            displayCards.setBackground(backgroupImage);

            displayCards.setAlignment(Pos.CENTER);


            continueToPlay.setOnAction(displayPlayWagerDetails(primaryStage)); // when user clicks on continue play to see dealers cards
            fold.setOnAction(foldSoLost(primaryStage));


            Scene displayCardsScene = new Scene(displayCards, 800, 800);
            primaryStage.setScene(displayCardsScene);

            }
        };
        return checkFoldAndDisplay;
    }

    //--------------------

    void connectAndGetCardsFromServer(){
//        clientConnection.start(); // the client thread starts here --> Client's individual game

        clientConnection.start(); // the client thread starts here --> Client's individual game

        clientConnection.setPort(Integer.parseInt(portNumText.getText())); // setting the port number for client class

        clientConnection.setIpAdd(ipAddressText.getText()); // setting the IP Address for client class

        //clientChoice.fire();

    }

    //-------------
    EventHandler<ActionEvent> handleAnteBets() {
        EventHandler<ActionEvent> anteBets = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Button clickedButton = (Button) event.getSource();
                userAnteBet = Integer.parseInt(clickedButton.getId()); // save the value of users bet for later

                for(int i = 0; i < totalButtons1.length; i++){
                    totalButtons1[i].setDisable(true);
                }
                finalizeBet.setDisable(false);

                moveInformation.sendData(clientConnection, "Placed an Ante wager bet of " + userAnteBet);

            }
        };
        return anteBets;
    }

    //------------

    // Disable all the buttons for plus bets once user chooses it
    EventHandler<ActionEvent> handlePlusBets() {
        EventHandler<ActionEvent> plusBets = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Button clickedButton = (Button) event.getSource();
                userPlusBet = Integer.parseInt(clickedButton.getId()); // save the value of users bet for later

                for(int i = 0; i < totalButtons2.length; i++){
                    totalButtons2[i].setDisable(true);
                }

                moveInformation.sendData(clientConnection, "Placed a Plus wager bet of " + userPlusBet);
            }
        };
        return plusBets;
    }

    //--------------------------------------------

    EventHandler<ActionEvent> gameStartOption(Stage primaryStage) {
        EventHandler<ActionEvent> gameOn = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                connectAndGetCardsFromServer();

                startGameButton = buttonTexture("Start Game", 150, 45, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.BOTTOM_RIGHT);
                hbox.getChildren().add(startGameButton);
                VBox gameStartBox = new VBox(hbox);

                gameStartBox.setAlignment(Pos.CENTER);
                Background startGameImage = new Background(changeBackground("startGame.jpg"));
                gameStartBox.setBackground(startGameImage);

                Scene gameStartScene = new Scene(gameStartBox, 800, 800);


                startGameButton.setOnAction(placeBettings(primaryStage));
                primaryStage.setScene(gameStartScene);
            }
        };
        return gameOn;
    }


    //--------------------------------------------

    // This is start of new game where user picks bets
    EventHandler<ActionEvent> placeBettings(Stage primaryStage) {

        EventHandler<ActionEvent> placeBets = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                userAnteBet = 0;
                userPlusBet = 0;
                userplayWager = 0;
                totaLWinnings = 0;

                //-------------------------------------------------------------------------------------------------------------

                TextArea askAnteBet = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                        "Place your Ante Wager from the options below!", 200, 70);

                TextArea askPairPlusWager = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                        "Place an optional Pair Plus Wager from the options below!", 200, 70);

                HBox betButtons1 = new HBox();
                HBox betButtons2 = new HBox();

                totalButtons1 = new Button[5];
                totalButtons2 = new Button[5];

                int setMoney = 5; // Starting bet option is 5 and maximum 25
                for(int i = 0; i < 5; i++){

                    anteWagerButton = buttonTexture("" + setMoney, 150, 45, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);
                    plusWagerButton = buttonTexture("" + setMoney, 150, 45, "-fx-background-color: WHITE", "0", Color.BLACK, "-fx-font-size: 16pt", false);

                    anteWagerButton.setId("" + setMoney);
                    plusWagerButton.setId("" + setMoney);

                    anteWagerButton.setStyle("-fx-background-color: #3b718d");
                    plusWagerButton.setStyle("-fx-background-color: #3b718d");

                    anteWagerButton.setPrefSize(50,50);
                    plusWagerButton.setPrefSize(50,50);

                    betButtons1.getChildren().add(anteWagerButton);
                    betButtons2.getChildren().add(plusWagerButton);

                    setMoney = setMoney + 5; // increment by 5

                    totalButtons1[i] = anteWagerButton;
                    totalButtons2[i] = plusWagerButton;

                    anteWagerButton.setOnAction(handleAnteBets());
                    plusWagerButton.setOnAction(handlePlusBets());
                }

                finalizeBet = buttonTexture("Finalize Betting", 150, 50, "-fx-background-color: BLACK", "0", Color.BLUE, "-fx-font-size: 16pt", true);
                VBox betPageDisplay = new VBox(10, askAnteBet, betButtons1, askPairPlusWager, betButtons2, finalizeBet);

                betPageDisplay.setAlignment(Pos.CENTER);

                Background betImage = new Background(changeBackground("bets.jpg"));
                betPageDisplay.setBackground(betImage);

                Scene betScene = new Scene(betPageDisplay, 800, 800);
                primaryStage.setScene(betScene);

                finalizeBet.setOnAction(displayCardsAndCheckFold(primaryStage));
                getNewDeckOfCardsFromServer("Client Plays Hand");

            }
        };
        return placeBets;
    }

    //--------------------------------------------

    void startGame(Stage primaryStage){
        TextArea askInfo = textTexture("Arial", 22, "-fx-control-inner-background: #00805a;",
                "Please first enter the IP Address and then the Port number to connect!", 200, 70);
        portNumText = new TextArea();
        ipAddressText = new TextArea();

        saveButton = new Button("Connect");

        clientConnection = new Client(serverMessage);

        VBox savePort = new VBox(100, askInfo, portNumText, ipAddressText, saveButton);
        startPane = new BorderPane();
        startPane.setPadding(new Insets(70));


        startPane.getChildren().add(imageview("start.jpg", 800, 800)); // adding background image
        startPane.setCenter(savePort); // add the saveport afterwards

        startScene = new Scene(startPane, 800,800);

        primaryStage.setScene(startScene);
        //primaryStage.show();

        saveButton.setOnAction(gameStartOption(primaryStage)); // we will start thread here

    }

    //-----------


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("The Networked Client/Server GUI Example");


        serverMessage = message-> {


            if(message instanceof String){
                System.out.println("Received Message: " + message);
            }
            else if(message instanceof ArrayList){
                if(counter == 0){ // 3 shuffled cards sent by Server to Client
                    clientDeck = (ArrayList<String>) message;
                    counter++;
                }
                else if(counter == 1){ // 3 shuffled cards sent by Server to Dealer
                    computerDeck = (ArrayList<String>) message;
                    counter++;
                }
                else if(counter == 2){ // The winner of the game sent by Server
                    gameWinner =  (ArrayList<String>) message;
                }
                else{
                    System.out.println("else: " + message.toString());
                }
            }
            else if(message instanceof Boolean){
                queenOrHigher = (Boolean) message; // checks if dealer cards have a queen or high
            }
            else if(message instanceof Double){
                totaLWinnings = (Double) message;
            }
        };



        //--------------------------------------------------
        startGame(primaryStage); // program starts
        //--------------------------------------------------



        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });


        primaryStage.show();

    }


} // end of class

