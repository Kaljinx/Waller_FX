package org.kaljinx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message){
        Stage window = new Stage();

        //Message Label
        Label Message = new Label("  "+message+"  ");

        //Close Window Button
        Button closewin = new Button(" OK ");
        closewin.setOnAction(event -> window.close());


        //Stage
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);

        //Vbox
        VBox layout = new VBox(15);
        layout.getChildren().addAll(Message,closewin);
        layout.setAlignment(Pos.CENTER);

        //Show
        Scene main = new Scene(layout,300,90);
        window.setResizable(false);
        window.setScene(main);
        window.showAndWait();
    }
}
