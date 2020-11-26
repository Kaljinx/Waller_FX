package org.kaljinx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ComfirmBox {
    static boolean answer;
    public static boolean display(String title, String message){
        Stage window = new Stage();

        //Contents
        Label Message = new Label(message);
        Button yesbtn = new Button("yes");
        Button nobtn = new Button("no");

        //Stage
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);

        //buttonfunc
        yesbtn.setOnAction(event -> {
            answer=true;
            window.close();
        });
        nobtn.setOnAction(event -> {
            answer=false;
            window.close();
        });

        VBox body = new VBox(15);
        HBox confirm = new HBox(5);
        confirm.getChildren().addAll(yesbtn,nobtn);
        body.getChildren().addAll(Message,confirm);
        body.setAlignment(Pos.CENTER);
        confirm.setAlignment(Pos.CENTER);

        //Show
        Scene main = new Scene(body,300,90);
        window.setResizable(false);
        window.setScene(main);
        window.showAndWait();

        return answer;

    }
}
