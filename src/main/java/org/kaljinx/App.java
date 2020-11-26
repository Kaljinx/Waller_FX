package org.kaljinx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App extends Application {
    Stage window;
    File f;
    ListView<String> blist;
    File loc;
    String sep;
    String nl;
    File file;

    @Override
    public void start(Stage primarystage) {
        //Stage
        window = primarystage;
        window.setTitle("Waller");

        VBox root = new VBox(10);
        HBox toadd = new HBox(10);
        TextField fileinput = new TextField();
        Button add = new Button("Add");
        Button select = new Button("Select");
        Button set = new Button("Set");
        blist = new ListView<>();
        FileChooser fc = new FileChooser();
        sep = File.separator;
        nl = System.lineSeparator();

        toadd.getChildren().addAll(fileinput,select,add);
        root.getChildren().addAll(toadd,blist,set);
        root.setAlignment(Pos.TOP_CENTER);
        toadd.setAlignment(Pos.CENTER);
        load("C:/Users/shiva/IdeaProjects/Waller_FX/res/background/location.txt");

        select.setOnAction(event -> {
            try {
                f= fc.showOpenDialog(primarystage);
                fileinput.setText(f.getPath());
            }
            catch (NullPointerException e){fileinput.setText("");}
        });
        //Add to List
        add.setOnAction(event -> {
            f = new File(fileinput.getText());
            if(f.exists()){
                listadd(f.getName());
                writefile(fileinput.getText());
            }
            else {
                AlertBox.display("File Not Found","The File You Selected does not exist");
            }
        });

        //Show
        Scene scene = new Scene(root, 400, 500);
        window.setResizable(false);
        window.setScene(scene);
        window.show();

        //Close
        window.setOnCloseRequest(e -> {
            e.consume();
            closefunction();
        });

        //Set
        set.setOnAction(event -> {
            System.out.println(getline(2,file));
        });
    }
    private void closefunction(){
        boolean answer = ComfirmBox.display("Quit","Are you sure?");
        if (answer){window.close();}

    }

    private void listadd(String item){
        blist.getItems().add(item);
    }

    private String getline(int line, File f){
        String st=null;
        try {
            Scanner fl = new Scanner(f);
            int i = 0;
            while (fl.hasNextLine()){
                i=i+1;
                st = fl.nextLine();
                if (i==line){break;}
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found");
        }
        return st;
    }

    private void writefile(String writing){
        try {
            FileWriter writer = new FileWriter(file,true);
            writer.append(writing);
            writer.append(nl);
            writer.close();
            System.out.println("Successfully wrote to the file. "+file.getPath());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void load(String location){
        try {
            file = new File(location);
            file.createNewFile();
            file.setWritable(true);
            file.setReadable(true);
            Scanner fl = new Scanner(file);
            while (fl.hasNextLine()){
                String s = fl.nextLine();
                if (!(s.equals(""))){
                    File file = new File(s);
                    listadd(file.getName());
                }
            }
            System.out.println("File Loaded: "+location);
        }
        catch (FileNotFoundException e){System.out.println("File Does Not Exist: [load()]");}
        catch (NullPointerException e){ System.out.println("Nothing in file: [load()]"); }
        catch (IOException e){System.out.println("IOExeption: [load()]");}
    }

    public static void main(String[] args) {
        launch();
    }

}