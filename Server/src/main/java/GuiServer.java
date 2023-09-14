/*
Muhammad Muzzammil
Jose Nava
CS 342
Professor Approved Extension With NO DEDUCTION
 */

import java.io.Serializable;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{


    TextField c1;
    Button startServer;
    Button b1;
    TextArea getPortFromUser;
    Scene startScene;
    BorderPane startPane;
    Server serverConnection;
    Consumer<Serializable> messages;
    int portNumber;
    String userInfo;

    ListView<String> listItems = new ListView<>();

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


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    EventHandler<ActionEvent> gatherInfo(Stage primaryStage){
        EventHandler<ActionEvent> allInformation = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                listItems = new ListView<>();

                BorderPane pane = new BorderPane();
                pane.setPadding(new Insets(70));
                pane.setStyle("-fx-background-color: coral");

                pane.setCenter(listItems);

                listItems.getItems().add(userInfo);

                c1 = new TextField();
                b1 = new Button("Send");


                Scene infoScene = new Scene(pane);
                primaryStage.setScene(infoScene);
            }
        };
        return allInformation;
    }




    EventHandler<ActionEvent> startServerup(Stage primaryStage){
        EventHandler<ActionEvent> startingUp = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

//                serverConnection = new Server(messages);

                serverConnection = new Server(data -> {
                    Platform.runLater(()->{
                        serverConnection.setPortNum(Integer.parseInt(getPortFromUser.getText()));
                        listItems.getItems().add(data.toString());
                    });
                });

                serverConnection.setPortNum(Integer.parseInt(getPortFromUser.getText()));

                startServer = buttonTexture("Start Server", 150, 50, "-fx-background-color: BLACK", "0", Color.BLUE, "-fx-font-size: 16pt", false);
                VBox serverLoading = new VBox(startServer);
                serverLoading.setAlignment(Pos.CENTER);

                startServer.setOnAction(gatherInfo(primaryStage));

                Scene serverScene = new Scene(serverLoading, 600, 600);
                primaryStage.setScene(serverScene);
            }
        };
        return startingUp;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("The Networked Client/Server GUI Example");


        TextArea text = textTexture("Arial", 30, "-fx-control-inner-background: #00805a;",
                "Please Choose A Port Number Below To Start Your Server", 100, 100);
        getPortFromUser = new TextArea();
        Button loadServer = buttonTexture("Connect To Server", 180, 50, "-fx-background-color: BLACK", "0", Color.BLUE, "-fx-font-size: 16pt", false);
        VBox settingPort = new VBox(30, text, getPortFromUser, loadServer);
        settingPort.setAlignment(Pos.CENTER);

        startScene = new Scene(settingPort, 600, 600);
        
        loadServer.setOnAction(startServerup(primaryStage)); // go to loading page


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    public Scene createServerGui() {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(70));
        pane.setStyle("-fx-background-color: coral");

        pane.setCenter(listItems);

        return new Scene(pane, 500, 400);

    }

}
